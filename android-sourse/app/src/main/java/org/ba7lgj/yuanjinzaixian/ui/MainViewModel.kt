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

enum class AppScreen {
    LOGIN,
    DEVICES,
    HOME,
    SETTINGS,
    DIAGNOSTICS
}

data class UiState(
    val screen: AppScreen = AppScreen.LOGIN,
    val loading: Boolean = false,
    val devices: List<DeviceInfo> = emptyList(),
    val account: String = "",
    val deviceLabel: String? = null,
    val nickname: String = "",
    val protectionVoltage: String = "57.8",
    val error: String? = null,
    val diagnostics: List<VoltageSampleEntity> = emptyList(),
    val chartSamples: List<VoltageSampleEntity> = emptyList()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = (application as YuanJinApplication).container.repository
    val battery: MutableStateFlow<BatteryState> = BatteryStateStore.state

    var ui by mutableStateOf(UiState())
        private set

    init {
        viewModelScope.launch { bootstrap() }
    }

    private suspend fun bootstrap() {
        val settings = repo.getSettings()
        val hasCredentials = repo.hasCredentials()
        ui = ui.copy(
            account = settings.account.orEmpty(),
            deviceLabel = settings.deviceLabel,
            nickname = settings.nickname.orEmpty(),
            protectionVoltage = settings.protectionVoltage.toString()
        )
        if (hasCredentials && settings.deviceSimei != null) {
            ui = ui.copy(screen = AppScreen.HOME, loading = true)
            repo.bootstrap()
            ui = ui.copy(
                screen = AppScreen.HOME,
                loading = false,
                chartSamples = repo.recentSamples(30)
            )
        } else {
            ui = ui.copy(screen = AppScreen.LOGIN, loading = false)
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
                    ui = ui.copy(
                        loading = false,
                        devices = devices,
                        account = account.trim(),
                        nickname = "",
                        screen = if (devices.size == 1) AppScreen.HOME else AppScreen.DEVICES
                    )
                    if (devices.size == 1) {
                        runCatching { repo.sample() }
                            .onSuccess {
                                ui = ui.copy(
                                    deviceLabel = devices.first().label,
                                    chartSamples = repo.recentSamples(30)
                                )
                            }
                            .onFailure {
                                ui = ui.copy(error = it.readableMessage())
                            }
                    }
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
                    screen = AppScreen.HOME,
                    deviceLabel = device.label,
                    nickname = "",
                    chartSamples = repo.recentSamples(30)
                )
            }.onFailure {
                ui = ui.copy(loading = false, error = it.readableMessage())
            }
        }
    }

    fun refresh(silent: Boolean = false) {
        viewModelScope.launch {
            if (!silent) ui = ui.copy(loading = true, error = null)
            runCatching { repo.sample() }
                .onSuccess {
                    ui = ui.copy(
                        loading = false,
                        error = null,
                        chartSamples = repo.recentSamples(30)
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
                    ui = ui.copy(chartSamples = repo.recentSamples(30))
                }
                .onFailure { ui = ui.copy(error = it.readableMessage()) }
        }
    }

    fun markEmpty() {
        viewModelScope.launch {
            runCatching { repo.markEmpty() }
                .onSuccess {
                    ui = ui.copy(chartSamples = repo.recentSamples(30))
                }
                .onFailure { ui = ui.copy(error = it.readableMessage()) }
        }
    }

    fun resetCalibration() {
        viewModelScope.launch {
            runCatching { repo.resetCalibration() }
                .onSuccess {
                    ui = ui.copy(
                        error = null,
                        chartSamples = repo.recentSamples(30)
                    )
                }
                .onFailure { ui = ui.copy(error = it.readableMessage()) }
        }
    }

    fun saveProtectionVoltage(value: String) {
        val parsed = value.toDoubleOrNull()
        if (parsed == null || parsed !in 45.0..70.0) {
            ui = ui.copy(error = "保护电压应在 45.0–70.0V 之间")
            return
        }
        viewModelScope.launch {
            repo.updateProtectionVoltage(parsed)
            ui = ui.copy(protectionVoltage = "%.1f".format(parsed), error = null)
        }
    }

    fun saveNickname(value: String) {
        viewModelScope.launch {
            repo.updateNickname(value)
            ui = ui.copy(nickname = value.trim(), error = null)
        }
    }

    fun openSettings() {
        ui = ui.copy(screen = AppScreen.SETTINGS)
    }

    fun openHome() {
        ui = ui.copy(screen = AppScreen.HOME)
    }

    fun openDiagnostics() {
        viewModelScope.launch {
            ui = ui.copy(
                screen = AppScreen.DIAGNOSTICS,
                diagnostics = repo.recentSamples(10),
                chartSamples = repo.recentSamples(30)
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
            ui = UiState()
        }
    }

    fun clearError() {
        ui = ui.copy(error = null)
    }

    private fun Throwable.readableMessage(): String =
        when (this) {
            is BiotApiException -> message
            else -> message ?: "操作失败"
        }
}
