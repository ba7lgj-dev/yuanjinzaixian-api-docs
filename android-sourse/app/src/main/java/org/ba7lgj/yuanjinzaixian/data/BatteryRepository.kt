package org.ba7lgj.yuanjinzaixian.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.ba7lgj.yuanjinzaixian.domain.BatteryEstimator
import org.ba7lgj.yuanjinzaixian.domain.BatteryState
import org.ba7lgj.yuanjinzaixian.domain.BatteryStatus
import org.ba7lgj.yuanjinzaixian.domain.EstimatorAnchors
import org.ba7lgj.yuanjinzaixian.domain.SettingsSnapshot
import org.ba7lgj.yuanjinzaixian.domain.StableLock
import org.ba7lgj.yuanjinzaixian.notifications.NotificationHelper
import org.ba7lgj.yuanjinzaixian.widget.BatteryWidget
import org.ba7lgj.yuanjinzaixian.work.SamplingScheduler
import org.ba7lgj.yuanjinzaixian.work.HistorySyncScheduler
import androidx.glance.appwidget.updateAll
import kotlin.math.abs
import kotlin.math.max

class BatteryRepository(
    private val context: Context,
    private val database: AppDatabase,
    private val api: BiotApi,
    private val credentials: CredentialStore,
    private val trips: TripRepository
) {
    private val sampleMutex = Mutex()

    suspend fun hasCredentials(): Boolean = credentials.hasPassword()

    suspend fun getSettings(): SettingsSnapshot {
        val anchors = estimatorAnchors()
        return loadSettings().toSnapshot(anchors)
    }

    suspend fun bootstrap() {
        val settings = loadSettings()
        if (!credentials.hasPassword() || settings.deviceSimei.isNullOrBlank()) {
            BatteryStateStore.save(
                context,
                BatteryStateStore.state.value.copy(
                    loggedIn = false,
                    deviceLabel = settings.displayName(),
                    stale = true
                )
            )
            return
        }
        BatteryStateStore.save(
            context,
            BatteryStateStore.state.value.copy(
                loggedIn = true,
                deviceLabel = settings.displayName(),
                protectionVoltage = settings.protectionVoltage
            )
        )
    }

    suspend fun login(account: String, password: String): List<DeviceInfo> =
        withContext(Dispatchers.IO) {
            val result = api.login(account, password)
            credentials.savePassword(password)
            credentials.saveSid(result.sid)
            val settings = loadSettings()
            val family = result.families.firstOrNull()
                ?: throw BiotApiException(-1, "登录成功但没有返回组织")
            val devices = api.listDevices(family.sid, result.sid)
            saveSettings(
                settings.copy(
                    account = account,
                    deviceSimei = null,
                    deviceLabel = null,
                    nickname = null,
                    stationarySince = null,
                    lastDayDistanceKm = null,
                    protectionAlerted = false,
                    stableLockedVoltageV = null,
                    stableLockedPosTime = null,
                    stableLockedComTime = null,
                    stableMarkedAt = null,
                    stableCycleStartedAt = null
                )
            )
            SamplingScheduler.sync(context, enabled = false, stableMarkedAt = null)
            HistorySyncScheduler.sync(context, enabled = false, initialBackfill = false)
            trips.resetRangeState()
            if (devices.size == 1) {
                selectDevice(devices.first())
            } else {
                BatteryStateStore.save(
                    context,
                    BatteryStateStore.state.value.copy(
                        loggedIn = true,
                        deviceLabel = null,
                        stale = true
                    )
                )
            }
            devices
        }

    suspend fun selectDevice(device: DeviceInfo) {
        val settings = loadSettings()
        saveSettings(
            settings.copy(
                deviceSimei = device.simei,
                deviceLabel = device.label,
                nickname = null,
                lastDayDistanceKm = device.dayDistanceRaw
                    ?.let { if (it > 1000.0) it / 1000.0 else it },
                stationarySince = null,
                protectionAlerted = false,
                stableLockedVoltageV = null,
                stableLockedPosTime = null,
                stableLockedComTime = null,
                stableMarkedAt = null,
                stableCycleStartedAt = null
            )
        )
        SamplingScheduler.sync(
            context = context,
            enabled = true,
            stableMarkedAt = null
        )
        trips.resetRangeState()
        HistorySyncScheduler.sync(
            context = context,
            enabled = true,
            initialBackfill = !trips.historyInitialDone()
        )
        BatteryStateStore.save(
            context,
            BatteryStateStore.state.value.copy(
                loggedIn = true,
                deviceLabel = device.label,
                stale = true
            )
        )
    }

    suspend fun sample() = sampleMutex.withLock {
        sampleLocked()
    }

    private suspend fun sampleLocked() {
        val settings = loadSettings()
        val simei = settings.deviceSimei
        if (simei.isNullOrBlank() || !credentials.hasPassword()) {
            BatteryStateStore.save(context, BatteryStateStore.state.value.copy(loggedIn = false))
            return
        }
        try {
            val previousSample = database.voltageSampleDao().latest()
            val detail = fetchDetailWithRetry(simei)
            val address = fetchAddress(detail)
            val communicationTime = detail.lastComTime
            if (communicationTime == null || detail.voltageV == null) {
                refreshDisplayOnly(settings, address)
                return
            }
            if (!BatteryEstimator.isNewCommunication(
                    previousSample?.lastComTime,
                    communicationTime
                )
            ) {
                refreshDisplayOnly(settings, address)
                return
            }
            persistFreshDetail(detail, settings, address)
        } catch (error: Exception) {
            BatteryStateStore.save(
                context,
                BatteryStateStore.state.value.copy(
                    loggedIn = true,
                    deviceLabel = settings.displayName(),
                    status = BatteryStatus.ERROR,
                    stale = true,
                    lastError = error.readableMessage()
                )
            )
            throw error
        }
    }

    suspend fun refreshOnce() {
        sample()
    }

    private suspend fun refreshDisplayOnly(
        settings: AppSettingsEntity,
        address: String?
    ) {
        refreshFromDatabase(
            previous = BatteryStateStore.state.value.copy(
                loggedIn = true,
                deviceLabel = settings.displayName(),
                address = address ?: BatteryStateStore.state.value.address,
                protectionVoltage = settings.protectionVoltage
            ),
            currentSettings = settings
        )
    }

    private suspend fun persistFreshDetail(
        detail: DeviceDetail,
        settings: AppSettingsEntity,
        address: String?
    ) {
        val communicationTime = detail.lastComTime ?: return
        val voltage = detail.voltageV ?: return
        val nowSec = detail.curTime ?: (System.currentTimeMillis() / 1000)
        val dayKm = detail.dayDistanceKm
        val previousDayKm = settings.lastDayDistanceKm
        val moving = dayKm != null && previousDayKm != null &&
            abs(dayKm - previousDayKm) > 0.02
        val stationarySince = when {
            moving -> nowSec
            settings.stationarySince != null -> settings.stationarySince
            else -> nowSec
        }
        val localRest = (nowSec - stationarySince).coerceAtLeast(0)
        val stateRest = if (detail.state == "e_line_sleep") {
            detail.stateTime ?: 0
        } else {
            0
        }
        val restSeconds = if (moving) 0 else max(localRest, stateRest)
        val age = (nowSec - communicationTime).coerceAtLeast(0)
        val stableLock = settings.stableLock()
        val keepStableLock = stableLock != null &&
            BatteryEstimator.canKeepStableLock(
                lock = stableLock,
                communicationTime = communicationTime,
                voltage = voltage,
                mileageIncreased = moving
            )
        val unlockStable = stableLock != null && !keepStableLock
        val cycleStartedAt = when {
            moving -> communicationTime
            unlockStable -> communicationTime
            settings.stableCycleStartedAt == null -> communicationTime
            else -> settings.stableCycleStartedAt
        }
        val sampleEntity = VoltageSampleEntity(
                fetchedAt = System.currentTimeMillis(),
                voltageV = voltage,
                lastComTime = detail.lastComTime,
                lastPosTime = detail.lastPosTime,
                curTime = detail.curTime,
                state = detail.state,
                stateBeginTime = detail.stateBeginTime,
                stateTime = detail.stateTime,
                dayDistanceRaw = detail.dayDistanceRaw,
                dayDistanceKm = dayKm,
                oilState = detail.oilState,
                locatorPowerPct = detail.locatorPowerPct,
                rawAccState = detail.rawAccState,
                rawAccPower = detail.rawAccPower,
                observedAt = communicationTime,
                ageSeconds = age,
                restSeconds = restSeconds,
                cacheSuspect = false,
                timestampSource = "communication"
            )
        database.voltageSampleDao().insert(sampleEntity)
        val recent = database.voltageSampleDao().recent(240)
        val cycleStable = stableLock == null &&
            !moving &&
            BatteryEstimator.isStable(recent, restSeconds, cycleStartedAt)
        val updatedSettings = settings.copy(
            stationarySince = stationarySince,
            lastDayDistanceKm = dayKm ?: previousDayKm,
            stableLockedVoltageV = when {
                cycleStable -> voltage
                keepStableLock -> stableLock?.voltageV
                unlockStable -> null
                else -> settings.stableLockedVoltageV
            },
            stableLockedPosTime = when {
                cycleStable || keepStableLock -> detail.lastPosTime
                unlockStable -> null
                else -> settings.stableLockedPosTime
            },
            stableLockedComTime = when {
                cycleStable || keepStableLock -> communicationTime
                unlockStable -> null
                else -> settings.stableLockedComTime
            },
            stableMarkedAt = when {
                cycleStable -> nowSec
                unlockStable -> null
                else -> settings.stableMarkedAt
            },
            stableCycleStartedAt = cycleStartedAt
        )
        saveSettings(updatedSettings)
        trips.observeSample(
            sample = sampleEntity,
            stable = cycleStable || keepStableLock,
            trainingGrade = restSeconds >= 150L * 60L,
            chargePending = database.chargeEventDao().latestPending() != null,
            cycleStartedAt = cycleStartedAt
        )
        refreshFromDatabase(
            previous = BatteryStateStore.state.value.copy(
                loggedIn = true,
                deviceLabel = settings.displayName(),
                address = address ?: BatteryStateStore.state.value.address,
                protectionVoltage = settings.protectionVoltage
            ),
            currentSettings = updatedSettings
        )
        SamplingScheduler.requestSync(context)
    }

    suspend fun refreshFromDatabase() {
        val settings = loadSettings()
        val previous = BatteryStateStore.state.value.copy(
            loggedIn = !settings.deviceSimei.isNullOrBlank(),
            deviceLabel = settings.displayName(),
            protectionVoltage = settings.protectionVoltage
        )
        refreshFromDatabase(previous, settings)
    }

    private suspend fun refreshFromDatabase(
        previous: BatteryState,
        currentSettings: AppSettingsEntity
    ) {
        val samples = database.voltageSampleDao().recent(240)
        val anchors = estimatorAnchors()
        val latest = samples.maxByOrNull { it.fetchedAt }
        if (latest != null && !BatteryEstimator.isStable(samples, latest.restSeconds)) {
            val signal = BatteryEstimator.chargeSignal(samples)
            if (signal != null) {
                maybeCreateChargeEvent(signal, latest)
            }
        }
        val now = latest?.curTime ?: (System.currentTimeMillis() / 1000)
        val pending = database.chargeEventDao().latestPending()
        if (pending != null && now - pending.detectedAt > 12 * 60 * 60) {
            database.chargeEventDao().resolve(
                pending.id,
                "unknown",
                "unknown",
                now
            )
        }
        var state = BatteryEstimator.evaluate(
            samples = samples,
            anchors = anchors,
            protectionVoltage = currentSettings.protectionVoltage,
            previous = previous,
            chargePending = database.chargeEventDao().latestPending() != null,
            stableLock = currentSettings.stableLock(),
            stableCycleStartedAt = currentSettings.stableCycleStartedAt
        )
        state = processFullRequest(state, now)
        val live = runCatching { trips.currentRangeSnapshot() }.getOrNull()
        val nextState = state.copy(
            protectionVoltage = currentSettings.protectionVoltage,
            liveRangeKm = live?.takeIf { it.available }?.remainingKm,
            anchorRangeKm = live?.takeIf { it.available }?.anchorRangeKm,
            distanceSinceAnchorKm = live?.distanceSinceAnchorKm,
            anchorVoltageV = live?.takeIf { it.available }?.anchorVoltageV,
            anchorObservedAt = live?.takeIf { it.available }?.anchorObservedAt,
            rangeSource = live?.source,
            modelState = live?.modelState,
            currentPeopleCount = live?.currentPeople ?: state.currentPeopleCount
        )
        BatteryStateStore.save(context, nextState)
        updateWidget()
    }

    suspend fun markFull() {
        val now = System.currentTimeMillis() / 1000
        val expiresAt = now + 24 * 60 * 60
        database.anchorRequestDao().insert(
            AnchorRequestEntity(
                type = "full",
                requestedAt = now,
                expiresAt = expiresAt
            )
        )
        trips.clearActiveAnchor()
        BatteryStateStore.save(
            context,
            BatteryStateStore.state.value.copy(
                fullRequestPending = true,
                fullRequestExpiresAt = expiresAt,
                fullRequestExpired = false
            )
        )
        refreshFromDatabase()
    }

    suspend fun markEmpty() {
        val state = BatteryStateStore.state.value
        val voltage = state.voltageV ?: throw IllegalStateException("还没有电压")
        database.batteryAnchorDao().insert(
            BatteryAnchorEntity(
                type = "empty",
                voltageV = voltage,
                socPercent = 0,
                createdAt = System.currentTimeMillis(),
                method = "manual"
            )
        )
        trips.clearActiveAnchor()
        refreshFromDatabase()
    }

    suspend fun saveCalibrationAnchors(fullVoltage: Double, emptyVoltage: Double) {
        require(fullVoltage in 40.0..75.0 && emptyVoltage in 40.0..75.0) {
            "满/空电压应在 40.0–75.0V 之间"
        }
        require(fullVoltage >= emptyVoltage + 2.0) {
            "满电电压应至少比空电电压高 2.0V"
        }
        val now = System.currentTimeMillis()
        database.batteryAnchorDao().insert(
            BatteryAnchorEntity(
                type = "full",
                voltageV = fullVoltage,
                socPercent = 100,
                createdAt = now,
                method = "manual"
            )
        )
        database.batteryAnchorDao().insert(
            BatteryAnchorEntity(
                type = "empty",
                voltageV = emptyVoltage,
                socPercent = 0,
                createdAt = now,
                method = "manual"
            )
        )
        trips.clearActiveAnchor()
        refreshFromDatabase()
    }

    suspend fun resetCalibration() {
        database.batteryAnchorDao().deleteAll()
        database.anchorRequestDao().deleteAll()
        trips.clearActiveAnchor()
        BatteryStateStore.save(
            context,
            BatteryStateStore.state.value.copy(
                fullRequestPending = false,
                fullRequestExpiresAt = null,
                fullRequestExpired = false
            )
        )
        refreshFromDatabase()
    }

    suspend fun updateNickname(value: String?) {
        val settings = loadSettings()
        val nickname = value?.trim()?.takeIf { it.isNotBlank() }
        val updated = settings.copy(nickname = nickname)
        saveSettings(updated)
        BatteryStateStore.save(
            context,
            BatteryStateStore.state.value.copy(deviceLabel = updated.displayName())
        )
        updateWidget()
    }

    suspend fun resolveChargeEvent(eventId: Long, choice: String) {
        val status = when (choice) {
            "charged" -> "charged"
            "not_charged" -> "not_charged"
            else -> "unknown"
        }
        database.chargeEventDao().resolve(eventId, status, status, System.currentTimeMillis() / 1000)
        refreshFromDatabase()
        NotificationHelper.cancelCharge(context)
    }

    suspend fun logout() {
        credentials.clear()
        val settings = loadSettings()
        saveSettings(
            settings.copy(
                account = null,
                deviceSimei = null,
                deviceLabel = null,
                nickname = null,
                stationarySince = null,
                lastDayDistanceKm = null,
                protectionAlerted = false,
                stableLockedVoltageV = null,
                stableLockedPosTime = null,
                stableLockedComTime = null,
                stableMarkedAt = null,
                stableCycleStartedAt = null
            )
        )
        SamplingScheduler.sync(context, enabled = false, stableMarkedAt = null)
        BatteryStateStore.save(context, BatteryState().copy(protectionVoltage = settings.protectionVoltage))
        updateWidget()
    }

    suspend fun recentSamples(limit: Int = 20): List<VoltageSampleEntity> =
        database.voltageSampleDao().recent(limit)

    private suspend fun ensureSid(): String {
        val current = credentials.getSid()
        if (!current.isNullOrBlank()) return current
        return loginStored()
    }

    private fun AppSettingsEntity.stableLock(): StableLock? {
        val voltage = stableLockedVoltageV ?: return null
        val communicationTime = stableLockedComTime ?: return null
        return StableLock(
            voltageV = voltage,
            communicationTime = communicationTime,
            markedAt = stableMarkedAt ?: 0
        )
    }

    private suspend fun fetchDetailWithRetry(simei: String): DeviceDetail {
        var sid = ensureSid()
        return try {
            withContext(Dispatchers.IO) { api.getDetail(simei, sid) }
        } catch (error: BiotApiException) {
            if (error.code != BiotApi.SESSION_EXPIRED) throw error
            credentials.saveSid("")
            sid = loginStored()
            withContext(Dispatchers.IO) { api.getDetail(simei, sid) }
        }
    }

    private suspend fun fetchAddress(detail: DeviceDetail): String? {
        val lat = detail.lat ?: return null
        val lon = detail.lon ?: return null
        return runCatching {
            val sid = ensureSid()
            withContext(Dispatchers.IO) { api.batchGetAddress(lat, lon, sid) }
        }.getOrNull()
    }

    private suspend fun loginStored(): String {
        val password = credentials.getPassword()
            ?: throw BiotApiException(-1, "没有保存的密码")
        val settings = loadSettings()
        val account = settings.account
            ?: throw BiotApiException(-1, "没有保存的账号")
        val result = withContext(Dispatchers.IO) { api.login(account, password) }
        credentials.saveSid(result.sid)
        return result.sid
    }

    /**
     * 切换设备时重新拉取账号下的设备列表。
     * 只刷新 sid 与列表，不清空当前设备、历史锚点或本地模型。
     */
    suspend fun listAccountDevices(): List<DeviceInfo> = withContext(Dispatchers.IO) {
        val password = credentials.getPassword()
            ?: throw BiotApiException(-1, "没有保存的密码，请重新登录")
        val account = loadSettings().account
            ?: throw BiotApiException(-1, "没有保存的账号，请重新登录")
        val result = api.login(account, password)
        credentials.saveSid(result.sid)
        val family = result.families.firstOrNull()
            ?: throw BiotApiException(-1, "登录成功但没有返回组织")
        api.listDevices(family.sid, result.sid)
    }

    private suspend fun maybeCreateChargeEvent(
        signal: org.ba7lgj.yuanjinzaixian.domain.ChargeSignal,
        latest: VoltageSampleEntity
    ) {
        val pending = database.chargeEventDao().latestPending()
        if (pending != null) return
        val last = database.chargeEventDao().latest()
        val now = latest.observedAt
        if (last != null && now - last.detectedAt < 2 * 60 * 60) return
        val endVoltage = latest.voltageV ?: return
        val id = database.chargeEventDao().insert(
            ChargeEventEntity(
                detectedAt = now,
                startVoltage = endVoltage - signal.deltaV,
                endVoltage = endVoltage,
                deltaV = signal.deltaV,
                windowSeconds = signal.windowSeconds
            )
        )
        if (id > 0) {
            NotificationHelper.showChargeAmbiguity(context, id, signal.deltaV)
        }
    }

    private suspend fun processFullRequest(
        state: BatteryState,
        now: Long
    ): BatteryState {
        val request = database.anchorRequestDao().latestPending("full") ?: return state
        if (now > request.expiresAt) {
            database.anchorRequestDao().resolve(request.id, "expired")
            return state.copy(
                fullRequestPending = false,
                fullRequestExpiresAt = request.expiresAt,
                fullRequestExpired = true
            )
        }
        if (state.stableSocPercent != null && state.voltageV != null) {
            database.batteryAnchorDao().insert(
                BatteryAnchorEntity(
                    type = "full",
                    voltageV = state.voltageV,
                    socPercent = 100,
                    createdAt = now,
                    method = "pending_stable"
                )
            )
            database.anchorRequestDao().resolve(request.id, "completed")
            return state.copy(
                fullRequestPending = false,
                fullRequestExpiresAt = request.expiresAt,
                fullRequestExpired = false
            )
        }
        return state.copy(
            fullRequestPending = true,
            fullRequestExpiresAt = request.expiresAt,
            fullRequestExpired = false
        )
    }

    private suspend fun estimatorAnchors(): EstimatorAnchors =
        EstimatorAnchors(
            fullVoltage = database.batteryAnchorDao().latest("full")?.voltageV,
            emptyVoltage = database.batteryAnchorDao().latest("empty")?.voltageV
        )

    private suspend fun updateWidget() {
        runCatching { BatteryWidget().updateAll(context) }
    }

    private suspend fun loadSettings(): AppSettingsEntity =
        database.appSettingsDao().get() ?: AppSettingsEntity()

    private suspend fun saveSettings(settings: AppSettingsEntity) {
        database.appSettingsDao().upsert(settings)
    }

    private fun AppSettingsEntity.toSnapshot(anchors: EstimatorAnchors): SettingsSnapshot =
        SettingsSnapshot(
            account = account,
            deviceSimei = deviceSimei,
            deviceLabel = deviceLabel,
            nickname = nickname,
            fullAnchorVoltage = anchors.fullVoltage ?: BatteryEstimator.DEFAULT_FULL_VOLTAGE,
            emptyAnchorVoltage = anchors.emptyVoltage ?: BatteryEstimator.DEFAULT_EMPTY_VOLTAGE,
            protectionVoltage = protectionVoltage,
            stationarySince = stationarySince,
            lastDayDistanceKm = lastDayDistanceKm,
            protectionAlerted = protectionAlerted,
            stableLocked = stableLockedVoltageV != null && stableLockedComTime != null,
            stableMarkedAt = stableMarkedAt
        )

    private fun AppSettingsEntity.displayName(): String =
        nickname?.takeIf { it.isNotBlank() }
            ?: deviceLabel?.takeIf { it.isNotBlank() }
            ?: "我的车"

    private fun Throwable.readableMessage(): String =
        when (this) {
            is BiotApiException -> when (code) {
                BiotApi.SESSION_EXPIRED -> "登录已过期，请重新登录"
                BiotApi.RATE_LIMITED -> "操作频率过快，请稍后再试"
                else -> message
            }
            else -> message ?: "网络请求失败"
        }

}
