package org.ba7lgj.yuanjinzaixian.domain

enum class BatteryStatus {
    WAITING,
    STABLE,
    REBOUND,
    CHARGE_UNKNOWN,
    STALE,
    OFFLINE,
    ERROR
}

enum class Confidence {
    LOW,
    MEDIUM,
    HIGH
}

data class BatteryState(
    val loggedIn: Boolean = false,
    val deviceLabel: String? = null,
    val socPercent: Int? = null,
    val instantSocPercent: Int? = null,
    val provisionalSocPercent: Int? = null,
    val stableSocPercent: Int? = null,
    val displaySocPercent: Int? = null,
    val rangeKm: Int? = null,
    val liveRangeKm: Int? = null,
    val anchorRangeKm: Int? = null,
    val distanceSinceAnchorKm: Double? = null,
    val anchorVoltageV: Double? = null,
    val anchorObservedAt: Long? = null,
    val rangeSource: String? = null,
    val modelState: String? = null,
    val currentPeopleCount: Int = 1,
    val voltageV: Double? = null,
    val status: BatteryStatus = BatteryStatus.WAITING,
    val confidence: Confidence = Confidence.LOW,
    val restSeconds: Long = 0,
    val voltageObservedAt: Long? = null,
    val ageSeconds: Long? = null,
    val stale: Boolean = true,
    val lastError: String? = null,
    val address: String? = null,
    val fullRequestPending: Boolean = false,
    val fullRequestExpiresAt: Long? = null,
    val fullRequestExpired: Boolean = false,
    val protectionVoltage: Double = 57.8,
    val fullSuggestion: Boolean = false,
    val locatorPowerPct: Int? = null,
    val rawAccState: String? = null,
    val rawAccPower: String? = null,
    val lastFetchedAt: Long? = null
)

data class SettingsSnapshot(
    val account: String? = null,
    val deviceSimei: String? = null,
    val deviceLabel: String? = null,
    val nickname: String? = null,
    val fullAnchorVoltage: Double = BatteryEstimator.DEFAULT_FULL_VOLTAGE,
    val emptyAnchorVoltage: Double = BatteryEstimator.DEFAULT_EMPTY_VOLTAGE,
    val protectionVoltage: Double = 57.8,
    val stationarySince: Long? = null,
    val lastDayDistanceKm: Double? = null,
    val protectionAlerted: Boolean = false,
    val stableLocked: Boolean = false,
    val stableMarkedAt: Long? = null
)

data class EstimatorAnchors(
    val fullVoltage: Double? = null,
    val emptyVoltage: Double? = null
)

data class StableLock(
    val voltageV: Double,
    val communicationTime: Long,
    val markedAt: Long
)

data class ChargeSignal(
    val deltaV: Double,
    val windowSeconds: Long
)
