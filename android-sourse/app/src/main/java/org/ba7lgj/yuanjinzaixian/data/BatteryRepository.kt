package org.ba7lgj.yuanjinzaixian.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.ba7lgj.yuanjinzaixian.domain.BatteryEstimator
import org.ba7lgj.yuanjinzaixian.domain.BatteryState
import org.ba7lgj.yuanjinzaixian.domain.BatteryStatus
import org.ba7lgj.yuanjinzaixian.domain.EstimatorAnchors
import org.ba7lgj.yuanjinzaixian.domain.SettingsSnapshot
import org.ba7lgj.yuanjinzaixian.notifications.NotificationHelper
import org.ba7lgj.yuanjinzaixian.widget.BatteryWidget
import androidx.glance.appwidget.updateAll
import kotlin.math.abs
import kotlin.math.max

class BatteryRepository(
    private val context: Context,
    private val database: AppDatabase,
    private val api: BiotApi,
    private val credentials: CredentialStore
) {
    suspend fun hasCredentials(): Boolean = credentials.hasPassword()

    suspend fun getSettings(): SettingsSnapshot = loadSettings().toSnapshot()

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
        runCatching { sample() }
            .onFailure {
                BatteryStateStore.save(
                    context,
                    BatteryStateStore.state.value.copy(
                        loggedIn = true,
                        deviceLabel = settings.displayName(),
                        status = BatteryStatus.ERROR,
                        stale = true,
                        lastError = it.readableMessage()
                    )
                )
            }
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
                    protectionAlerted = false
                )
            )
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
                protectionAlerted = false
            )
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

    suspend fun sample() {
        val settings = loadSettings()
        val simei = settings.deviceSimei
        if (simei.isNullOrBlank() || !credentials.hasPassword()) {
            BatteryStateStore.save(context, BatteryStateStore.state.value.copy(loggedIn = false))
            return
        }
        try {
            val detail = fetchDetailWithRetry(simei)
            val address = fetchAddress(detail)
            val previousSample = database.voltageSampleDao().latest()
            val nowSec = detail.curTime ?: (System.currentTimeMillis() / 1000)
            val observedAt = detail.lastComTime
                ?: detail.lastPosTime
                ?: (System.currentTimeMillis() / 1000)
            val timestampSource = when {
                detail.lastComTime != null -> "communication"
                detail.lastPosTime != null -> "position_fallback"
                else -> "local_fallback"
            }
            val cacheSuspect = previousSample != null &&
                previousSample.lastComTime != null &&
                detail.lastComTime == previousSample.lastComTime
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
            val age = (nowSec - observedAt).coerceAtLeast(0)
            val sample = VoltageSampleEntity(
                fetchedAt = System.currentTimeMillis(),
                voltageV = detail.voltageV,
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
                observedAt = observedAt,
                ageSeconds = age,
                restSeconds = restSeconds,
                cacheSuspect = cacheSuspect,
                timestampSource = timestampSource
            )
            database.voltageSampleDao().insert(sample)
            saveSettings(
                settings.copy(
                    stationarySince = stationarySince,
                    lastDayDistanceKm = dayKm ?: previousDayKm
                )
            )
            refreshFromDatabase(
                previous = BatteryStateStore.state.value.copy(
                    loggedIn = true,
                    deviceLabel = settings.displayName(),
                    address = address ?: BatteryStateStore.state.value.address,
                    protectionVoltage = settings.protectionVoltage
                ),
                currentSettings = loadSettings()
            )
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

    suspend fun immediateRefresh() {
        val settings = loadSettings()
        val simei = settings.deviceSimei?.takeIf { it.isNotBlank() } ?: return
        val beforeDetail = fetchDetailWithRetry(simei)
        fetchAddress(beforeDetail)?.let { address ->
            BatteryStateStore.save(
                context,
                BatteryStateStore.state.value.copy(address = address)
            )
        }
        val sid = ensureSid()
        try {
            withContext(Dispatchers.IO) { api.requestImmediateLocation(simei, sid) }
        } catch (error: BiotApiException) {
            if (error.code != BiotApi.SESSION_EXPIRED) throw error
            credentials.saveSid("")
            val freshSid = loginStored()
            withContext(Dispatchers.IO) { api.requestImmediateLocation(simei, freshSid) }
        }
        val before = database.voltageSampleDao().latest()?.lastComTime
        var lastError: Throwable? = null
        repeat(4) { attempt ->
            delay(if (attempt == 0) 3_000L else 2_000L)
            try {
                sample()
                val after = database.voltageSampleDao().latest()?.lastComTime
                if (after != null && after != before) return
            } catch (error: Throwable) {
                lastError = error
            }
        }
        lastError?.let { throw it }
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
        val samples = database.voltageSampleDao().recent(12)
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
            chargePending = database.chargeEventDao().latestPending() != null
        )
        state = processFullRequest(state, now)
        val nextState = state.copy(protectionVoltage = currentSettings.protectionVoltage)
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
        refreshFromDatabase()
    }

    suspend fun resetCalibration() {
        database.batteryAnchorDao().deleteAll()
        database.anchorRequestDao().deleteAll()
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

    suspend fun updateProtectionVoltage(value: Double) {
        val settings = loadSettings()
        saveSettings(settings.copy(protectionVoltage = value, protectionAlerted = false))
        BatteryStateStore.save(
            context,
            BatteryStateStore.state.value.copy(protectionVoltage = value)
        )
        updateWidget()
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
                protectionAlerted = false
            )
        )
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

    private fun AppSettingsEntity.toSnapshot(): SettingsSnapshot =
        SettingsSnapshot(
            account = account,
            deviceSimei = deviceSimei,
            deviceLabel = deviceLabel,
            nickname = nickname,
            protectionVoltage = protectionVoltage,
            stationarySince = stationarySince,
            lastDayDistanceKm = lastDayDistanceKm,
            protectionAlerted = protectionAlerted
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
