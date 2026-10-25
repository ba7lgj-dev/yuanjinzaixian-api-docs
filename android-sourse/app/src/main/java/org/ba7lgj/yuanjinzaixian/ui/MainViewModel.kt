package org.ba7lgj.yuanjinzaixian.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.ba7lgj.yuanjinzaixian.YuanJinApplication
import org.ba7lgj.yuanjinzaixian.data.BatteryStateStore
import org.ba7lgj.yuanjinzaixian.data.BiotApiException
import org.ba7lgj.yuanjinzaixian.data.DeviceInfo
import org.ba7lgj.yuanjinzaixian.data.VoltageSampleEntity
import org.ba7lgj.yuanjinzaixian.domain.BatteryState
import org.ba7lgj.yuanjinzaixian.domain.HistoryOverview
import org.ba7lgj.yuanjinzaixian.domain.LiveRangeSnapshot
import org.ba7lgj.yuanjinzaixian.domain.PeopleRules
import org.ba7lgj.yuanjinzaixian.domain.TrainingStatus

/** 应用会话阶段：决定启动页，不参与页面之间的跳转。 */
enum class SessionStage {
    LOADING,
    LOGIN,
    CHOOSE_DEVICE,
    READY
}

/** ViewModel 只发出“清栈/入栈”意图，具体由 NavHost 执行。 */
enum class NavEvent {
    TO_LOGIN,
    TO_DEVICES_CLEAR,
    TO_DEVICES_PUSH,
    TO_HOME
}

data class UiState(
    val sessionStage: SessionStage = SessionStage.LOADING,
    val navEvent: NavEvent? = null,
    val loading: Boolean = false,
    val devices: List<DeviceInfo> = emptyList(),
    val account: String = "",
    val deviceLabel: String? = null,
    val nickname: String = "",
    val fullVoltage: String = "65.5",
    val emptyVoltage: String = "55.0",
    val error: String? = null,
    val diagnostics: List<VoltageSampleEntity> = emptyList(),
    val chartSamples: List<VoltageSampleEntity> = emptyList(),
    val history: HistoryOverview? = null,
    val training: TrainingStatus = TrainingStatus(),
    val historySyncing: Boolean = false,
    val historyMessage: String? = null,
    val liveRange: LiveRangeSnapshot? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = (application as YuanJinApplication).container.repository
    private val trips = (application as YuanJinApplication).container.trips
    val battery: MutableStateFlow<BatteryState> = BatteryStateStore.state

    var ui by mutableStateOf(UiState())
        private set

    init {
        viewModelScope.launch { bootstrap() }
    }

    private suspend fun bootstrap() {
        val settings = repo.getSettings()
        ui = ui.copy(
            account = settings.account.orEmpty(),
            deviceLabel = settings.deviceLabel,
            nickname = settings.nickname.orEmpty(),
            fullVoltage = "%.1f".format(settings.fullAnchorVoltage),
            emptyVoltage = "%.1f".format(settings.emptyAnchorVoltage)
        )
        // 数据库升级到 v8 后，按 1–2 人规则重训一次；样本不足时保持物理基准。
        runCatching { trips.retrainPendingModel() }
        ui = ui.copy(training = runCatching { trips.trainingStatus() }.getOrDefault(TrainingStatus()))
        ui = ui.copy(liveRange = runCatching { trips.currentRangeSnapshot() }.getOrNull())
        if (repo.hasCredentials() && settings.deviceSimei != null) {
            ui = ui.copy(sessionStage = SessionStage.READY, loading = true)
            repo.bootstrap()
            ui = ui.copy(
                sessionStage = SessionStage.READY,
                loading = false,
                chartSamples = repo.recentSamples(30)
            )
            reloadHistory()
        } else {
            ui = ui.copy(sessionStage = SessionStage.LOGIN, loading = false)
        }
    }

    fun login(account: String, password: String) {
        if (account.isBlank() || password.isBlank()) {
            ui = ui.copy(error = "请输入账号和密码")
            return
        }
        viewModelScope.launch {
            ui = ui.copy(loading = true, error = null)
            runCatching { repo.login(account.trim(), password) }
                .onSuccess { devices ->
                    val single = devices.size == 1
                    ui = ui.copy(
                        loading = false,
                        devices = devices,
                        account = account.trim(),
                        nickname = "",
                        sessionStage = if (single) SessionStage.READY else SessionStage.CHOOSE_DEVICE,
                        navEvent = if (single) NavEvent.TO_HOME else NavEvent.TO_DEVICES_CLEAR
                    )
                    if (single) {
                        runCatching { repo.sample() }
                            .onSuccess {
                                ui = ui.copy(
                                    deviceLabel = devices.first().label,
                                    chartSamples = repo.recentSamples(30)
                                )
                            }
                            .onFailure { ui = ui.copy(error = it.readableMessage()) }
                    }
                    reloadHistory()
                }
                .onFailure { error ->
                    ui = ui.copy(loading = false, error = error.readableMessage())
                }
        }
    }

    fun selectDevice(device: DeviceInfo) {
        viewModelScope.launch {
            ui = ui.copy(loading = true, error = null)
            runCatching {
                repo.selectDevice(device)
                repo.sample()
            }.onSuccess {
                ui = ui.copy(
                    loading = false,
                    deviceLabel = device.label,
                    nickname = "",
                    sessionStage = SessionStage.READY,
                    navEvent = NavEvent.TO_HOME,
                    chartSamples = repo.recentSamples(30)
                )
                reloadHistory()
            }.onFailure {
                ui = ui.copy(loading = false, error = it.readableMessage())
            }
        }
    }

    /** 设置页“切换设备”：拉取账号下设备列表，并压入设备选择页。 */
    fun requestDeviceSwitch() {
        viewModelScope.launch {
            ui = ui.copy(loading = true, error = null)
            runCatching { repo.listAccountDevices() }
                .onSuccess { devices ->
                    ui = ui.copy(
                        loading = false,
                        devices = devices,
                        sessionStage = SessionStage.CHOOSE_DEVICE,
                        navEvent = NavEvent.TO_DEVICES_PUSH
                    )
                }
                .onFailure { ui = ui.copy(loading = false, error = it.readableMessage()) }
        }
    }

    fun consumeNavEvent() {
        if (ui.navEvent != null) ui = ui.copy(navEvent = null)
    }

    fun refresh(silent: Boolean = false) {
        viewModelScope.launch {
            if (!silent) ui = ui.copy(loading = true, error = null)
            runCatching { repo.sample() }
                .onSuccess {
                    syncAnchorFields()
                    ui = ui.copy(
                        loading = false,
                        error = null,
                        chartSamples = repo.recentSamples(30),
                        liveRange = runCatching { trips.currentRangeSnapshot() }.getOrNull()
                    )
                }
                .onFailure {
                    ui = ui.copy(
                        loading = false,
                        error = if (silent) null else it.readableMessage()
                    )
                }
        }
    }

    fun markFull() {
        viewModelScope.launch {
            runCatching { repo.markFull() }
                .onSuccess {
                    syncAnchorFields()
                    refreshLiveRange()
                    ui = ui.copy(
                        chartSamples = repo.recentSamples(30),
                        historyMessage = "已记录充满标记；电压稳定后才会写入满电锚点"
                    )
                }
                .onFailure { ui = ui.copy(error = it.readableMessage()) }
        }
    }

    fun markEmpty() {
        viewModelScope.launch {
            runCatching { repo.markEmpty() }
                .onSuccess {
                    syncAnchorFields()
                    refreshLiveRange()
                    ui = ui.copy(
                        chartSamples = repo.recentSamples(30),
                        historyMessage = "已用当前电压写入实际空电锚点（0%）"
                    )
                }
                .onFailure { ui = ui.copy(error = it.readableMessage()) }
        }
    }

    fun resetCalibration() {
        viewModelScope.launch {
            runCatching { repo.resetCalibration() }
                .onSuccess {
                    syncAnchorFields()
                    refreshLiveRange()
                    ui = ui.copy(
                        error = null,
                        chartSamples = repo.recentSamples(30),
                        historyMessage = "已恢复默认满电 65.5V / 空电 55.0V"
                    )
                }
                .onFailure { ui = ui.copy(error = it.readableMessage()) }
        }
    }

    fun saveCalibrationAnchors(fullText: String, emptyText: String) {
        val full = fullText.toDoubleOrNull()
        val empty = emptyText.toDoubleOrNull()
        if (full == null || empty == null) {
            ui = ui.copy(error = "请输入有效的满电和空电电压")
            return
        }
        if (full !in 40.0..75.0 || empty !in 40.0..75.0) {
            ui = ui.copy(error = "满/空电压应在 40.0–75.0V 之间")
            return
        }
        if (full < empty + 2.0) {
            ui = ui.copy(error = "满电电压应至少比空电电压高 2.0V")
            return
        }
        viewModelScope.launch {
            runCatching { repo.saveCalibrationAnchors(full, empty) }
                .onSuccess {
                    ui = ui.copy(
                        fullVoltage = "%.1f".format(full),
                        emptyVoltage = "%.1f".format(empty),
                        error = null,
                        historyMessage = "已保存满/空电压，电量与续航已重算"
                    )
                    refreshLiveRange()
                }
                .onFailure { ui = ui.copy(error = it.readableMessage()) }
        }
    }

    fun saveNickname(value: String) {
        viewModelScope.launch {
            repo.updateNickname(value)
            ui = ui.copy(nickname = value.trim(), error = null)
        }
    }

    fun setPeople(peopleCount: Int) {
        viewModelScope.launch {
            runCatching { trips.setCurrentPeople(PeopleRules.clamp(peopleCount)) }
                .onSuccess { snapshot ->
                    ui = ui.copy(liveRange = snapshot)
                    repo.refreshFromDatabase()
                }
                .onFailure { ui = ui.copy(error = it.readableMessage()) }
        }
    }

    fun syncHistory() {
        viewModelScope.launch {
            ui = ui.copy(historySyncing = true, error = null, historyMessage = null)
            runCatching { trips.syncHistory(initialBackfill = false) }
                .onSuccess { result ->
                    reloadHistory()
                    ui = ui.copy(
                        historySyncing = false,
                        historyMessage = buildString {
                            append("已导入 ${result.importedDays} 天，")
                            append("共 ${result.cycles} 个周期，")
                            append("可训练 ${result.trainingCycles} 个")
                            if (result.morePending) append("；还有更早的数据，后台会继续同步")
                            result.lastError?.let { append("；部分日期失败：$it") }
                        }
                    )
                    repo.refreshFromDatabase()
                    refreshLiveRange()
                }
                .onFailure {
                    ui = ui.copy(historySyncing = false, error = it.readableMessage())
                }
        }
    }

    fun setSegmentPeople(segmentKey: String, peopleCount: Int?) {
        viewModelScope.launch {
            runCatching { trips.setSegmentPeople(segmentKey, PeopleRules.clampOrNull(peopleCount)) }
                .onSuccess { reloadHistory() }
                .onFailure { ui = ui.copy(error = it.readableMessage()) }
        }
    }

    fun retrainModel() {
        viewModelScope.launch {
            runCatching { trips.retrainModel() }
                .onSuccess {
                    reloadHistory()
                    repo.refreshFromDatabase()
                    ui = ui.copy(historyMessage = "已按最新标注重新训练")
                }
                .onFailure { ui = ui.copy(error = it.readableMessage()) }
        }
    }

    fun rollbackModel() {
        viewModelScope.launch {
            runCatching { trips.rollbackModel() }
                .onSuccess {
                    reloadHistory()
                    repo.refreshFromDatabase()
                    ui = ui.copy(historyMessage = "已回滚到上一版模型")
                }
                .onFailure { ui = ui.copy(error = it.readableMessage()) }
        }
    }

    fun resetModelToBaseline() {
        viewModelScope.launch {
            runCatching { trips.resetModel() }
                .onSuccess {
                    reloadHistory()
                    repo.refreshFromDatabase()
                    ui = ui.copy(historyMessage = "已恢复物理基准估算")
                }
                .onFailure { ui = ui.copy(error = it.readableMessage()) }
        }
    }

    fun refreshHistory() {
        viewModelScope.launch { reloadHistory() }
    }

    fun loadDiagnostics() {
        viewModelScope.launch {
            ui = ui.copy(
                diagnostics = repo.recentSamples(10),
                chartSamples = repo.recentSamples(30)
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
            ui = UiState(
                sessionStage = SessionStage.LOGIN,
                navEvent = NavEvent.TO_LOGIN
            )
        }
    }

    fun clearError() {
        ui = ui.copy(error = null)
    }

    private suspend fun reloadHistory() {
        val overview = runCatching { trips.historyOverview() }.getOrNull()
        val training = runCatching { trips.trainingStatus() }.getOrDefault(TrainingStatus())
        val live = runCatching { trips.currentRangeSnapshot() }.getOrNull()
        ui = ui.copy(history = overview, training = training, liveRange = live)
    }

    private suspend fun refreshLiveRange() {
        ui = ui.copy(liveRange = runCatching { trips.currentRangeSnapshot() }.getOrNull())
    }

    private fun Throwable.readableMessage(): String =
        when (this) {
            is BiotApiException -> message
            else -> message ?: "操作失败"
        }

    private suspend fun syncAnchorFields() {
        val settings = repo.getSettings()
        ui = ui.copy(
            fullVoltage = "%.1f".format(settings.fullAnchorVoltage),
            emptyVoltage = "%.1f".format(settings.emptyAnchorVoltage)
        )
    }
}
