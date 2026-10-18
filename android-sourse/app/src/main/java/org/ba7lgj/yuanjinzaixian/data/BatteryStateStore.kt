package org.ba7lgj.yuanjinzaixian.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import org.ba7lgj.yuanjinzaixian.domain.BatteryState
import org.ba7lgj.yuanjinzaixian.domain.BatteryStatus
import org.ba7lgj.yuanjinzaixian.domain.Confidence

object BatteryStateStore {
    private const val PREFS = "battery_state"
    private const val KEY_SOC = "soc_percent"
    private const val KEY_INSTANT_SOC = "instant_soc_percent"
    private const val KEY_PROVISIONAL_SOC = "provisional_soc_percent"
    private const val KEY_STABLE_SOC = "stable_soc_percent"
    private const val KEY_DISPLAY_SOC = "display_soc_percent"
    private const val KEY_RANGE = "range_km"
    private const val KEY_VOLTAGE = "voltage_v"
    private const val KEY_STATUS = "status"
    private const val KEY_CONFIDENCE = "confidence"
    private const val KEY_REST = "rest_seconds"
    private const val KEY_OBSERVED = "voltage_observed_at"
    private const val KEY_AGE = "age_seconds"
    private const val KEY_STALE = "stale"
    private const val KEY_ERROR = "last_error"
    private const val KEY_ADDRESS = "address"
    private const val KEY_FULL_PENDING = "full_request_pending"
    private const val KEY_FULL_EXPIRES = "full_request_expires"
    private const val KEY_FULL_EXPIRED = "full_request_expired"
    private const val KEY_PROTECTION = "protection_voltage"
    private const val KEY_FULL_SUGGESTION = "full_suggestion"
    private const val KEY_LOCATOR_POWER = "locator_power_pct"
    private const val KEY_ACC_STATE = "raw_acc_state"
    private const val KEY_ACC_POWER = "raw_acc_power"
    private const val KEY_FETCHED = "last_fetched_at"
    private const val KEY_LOGGED_IN = "logged_in"
    private const val KEY_DEVICE_LABEL = "device_label"

    val state = MutableStateFlow(BatteryState())

    fun load(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val loaded = BatteryState(
            loggedIn = prefs.getBoolean(KEY_LOGGED_IN, false),
            deviceLabel = prefs.getString(KEY_DEVICE_LABEL, null),
            socPercent = prefs.nullableInt(KEY_SOC),
            instantSocPercent = prefs.nullableInt(KEY_INSTANT_SOC),
            provisionalSocPercent = prefs.nullableInt(KEY_PROVISIONAL_SOC),
            stableSocPercent = prefs.nullableInt(KEY_STABLE_SOC),
            displaySocPercent = prefs.nullableInt(KEY_DISPLAY_SOC),
            rangeKm = prefs.nullableInt(KEY_RANGE),
            voltageV = prefs.nullableDouble(KEY_VOLTAGE),
            status = prefs.getString(KEY_STATUS, null)
                ?.let { runCatching { BatteryStatus.valueOf(it) }.getOrNull() }
                ?: BatteryStatus.WAITING,
            confidence = prefs.getString(KEY_CONFIDENCE, null)
                ?.let { runCatching { Confidence.valueOf(it) }.getOrNull() }
                ?: Confidence.LOW,
            restSeconds = prefs.getLong(KEY_REST, 0L),
            voltageObservedAt = prefs.nullableLong(KEY_OBSERVED),
            ageSeconds = prefs.nullableLong(KEY_AGE),
            stale = prefs.getBoolean(KEY_STALE, true),
            lastError = prefs.getString(KEY_ERROR, null),
            address = prefs.getString(KEY_ADDRESS, null),
            fullRequestPending = prefs.getBoolean(KEY_FULL_PENDING, false),
            fullRequestExpiresAt = prefs.nullableLong(KEY_FULL_EXPIRES),
            fullRequestExpired = prefs.getBoolean(KEY_FULL_EXPIRED, false),
            protectionVoltage = prefs.getFloat(KEY_PROTECTION, 57.8f).toDouble(),
            fullSuggestion = prefs.getBoolean(KEY_FULL_SUGGESTION, false),
            locatorPowerPct = prefs.nullableInt(KEY_LOCATOR_POWER),
            rawAccState = prefs.getString(KEY_ACC_STATE, null),
            rawAccPower = prefs.getString(KEY_ACC_POWER, null),
            lastFetchedAt = prefs.nullableLong(KEY_FETCHED)
        )
        state.value = loaded
    }

    fun save(context: Context, value: BatteryState) {
        val editor = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
        editor.putBoolean(KEY_LOGGED_IN, value.loggedIn)
        editor.putString(KEY_DEVICE_LABEL, value.deviceLabel)
        editor.putNullableInt(KEY_SOC, value.socPercent)
        editor.putNullableInt(KEY_INSTANT_SOC, value.instantSocPercent)
        editor.putNullableInt(KEY_PROVISIONAL_SOC, value.provisionalSocPercent)
        editor.putNullableInt(KEY_STABLE_SOC, value.stableSocPercent)
        editor.putNullableInt(KEY_DISPLAY_SOC, value.displaySocPercent)
        editor.putNullableInt(KEY_RANGE, value.rangeKm)
        editor.putNullableDouble(KEY_VOLTAGE, value.voltageV)
        editor.putString(KEY_STATUS, value.status.name)
        editor.putString(KEY_CONFIDENCE, value.confidence.name)
        editor.putLong(KEY_REST, value.restSeconds)
        editor.putNullableLong(KEY_OBSERVED, value.voltageObservedAt)
        editor.putNullableLong(KEY_AGE, value.ageSeconds)
        editor.putBoolean(KEY_STALE, value.stale)
        editor.putString(KEY_ERROR, value.lastError)
        editor.putString(KEY_ADDRESS, value.address)
        editor.putBoolean(KEY_FULL_PENDING, value.fullRequestPending)
        editor.putNullableLong(KEY_FULL_EXPIRES, value.fullRequestExpiresAt)
        editor.putBoolean(KEY_FULL_EXPIRED, value.fullRequestExpired)
        editor.putFloat(KEY_PROTECTION, value.protectionVoltage.toFloat())
        editor.putBoolean(KEY_FULL_SUGGESTION, value.fullSuggestion)
        editor.putNullableInt(KEY_LOCATOR_POWER, value.locatorPowerPct)
        editor.putString(KEY_ACC_STATE, value.rawAccState)
        editor.putString(KEY_ACC_POWER, value.rawAccPower)
        editor.putNullableLong(KEY_FETCHED, value.lastFetchedAt)
        editor.apply()
        state.value = value
    }

    fun read(context: Context): BatteryState {
        load(context)
        return state.value
    }

    private fun android.content.SharedPreferences.nullableInt(key: String): Int? =
        if (contains(key)) getInt(key, 0) else null

    private fun android.content.SharedPreferences.nullableLong(key: String): Long? =
        if (contains(key)) getLong(key, 0L) else null

    private fun android.content.SharedPreferences.nullableDouble(key: String): Double? =
        if (contains(key)) getFloat(key, 0f).toDouble() else null

    private fun android.content.SharedPreferences.Editor.putNullableInt(key: String, value: Int?) {
        if (value == null) remove(key) else putInt(key, value)
    }

    private fun android.content.SharedPreferences.Editor.putNullableLong(key: String, value: Long?) {
        if (value == null) remove(key) else putLong(key, value)
    }

    private fun android.content.SharedPreferences.Editor.putNullableDouble(
        key: String,
        value: Double?
    ) {
        if (value == null) remove(key) else putFloat(key, value.toFloat())
    }
}
