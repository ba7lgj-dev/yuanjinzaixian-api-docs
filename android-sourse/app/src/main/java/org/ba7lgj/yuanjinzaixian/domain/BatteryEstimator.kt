package org.ba7lgj.yuanjinzaixian.domain

import org.ba7lgj.yuanjinzaixian.data.VoltageSampleEntity
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

object BatteryEstimator {
    const val DEFAULT_FULL_VOLTAGE = 65.5
    const val DEFAULT_EMPTY_VOLTAGE = 55.0
    const val STABLE_VOLTAGE_TOLERANCE = 0.05
    private const val OCV_FULL_VOLTAGE = 63.0
    private const val OCV_EMPTY_VOLTAGE = 52.5
    private const val PROTECTION_SOC = 20.0
    private const val DEFAULT_SOC_PER_KM = 2.5

    private val voltageTable = listOf(
        100.0 to 63.0,
        90.0 to 62.5,
        80.0 to 62.1,
        70.0 to 61.6,
        60.0 to 61.0,
        50.0 to 60.3,
        40.0 to 59.5,
        30.0 to 58.75,
        20.0 to 57.9,
        10.0 to 56.55,
        0.0 to 52.5
    )

    fun socFromVoltage(voltage: Double?, anchors: EstimatorAnchors): Int? {
        val v = voltage ?: return null
        val full = anchors.fullVoltage ?: DEFAULT_FULL_VOLTAGE
        val empty = anchors.emptyVoltage ?: DEFAULT_EMPTY_VOLTAGE
        if (full <= empty + 1.0) return null
        val reference = OCV_EMPTY_VOLTAGE + (v - empty) *
            (OCV_FULL_VOLTAGE - OCV_EMPTY_VOLTAGE) / (full - empty)
        val clamped = reference.coerceIn(OCV_EMPTY_VOLTAGE, OCV_FULL_VOLTAGE)
        val upper = voltageTable.firstOrNull { clamped >= it.second }
            ?: voltageTable.last()
        val index = voltageTable.indexOf(upper)
        if (index == 0) return 100
        val lower = voltageTable[index - 1]
        val upperPoint = voltageTable[index]
        if (upperPoint.second == lower.second) return upperPoint.first.roundToInt()
        val ratio = (clamped - upperPoint.second) / (lower.second - upperPoint.second)
        return (upperPoint.first + ratio * (lower.first - upperPoint.first))
            .roundToInt()
            .coerceIn(0, 100)
    }

    fun rangeKm(
        socPercent: Int?,
        protectionSoc: Double = 0.0,
        socPerKm: Double = DEFAULT_SOC_PER_KM
    ): Int? {
        val soc = socPercent ?: return null
        return max(0.0, soc - protectionSoc).div(socPerKm).roundToInt()
    }

    fun confidenceForRest(restSeconds: Long): Confidence = when {
        restSeconds >= 240 * 60 -> Confidence.HIGH
        restSeconds >= 150 * 60 -> Confidence.HIGH
        restSeconds >= 30 * 60 -> Confidence.MEDIUM
        else -> Confidence.LOW
    }

    fun isStable(
        samples: List<VoltageSampleEntity>,
        restSeconds: Long,
        cycleStartedAt: Long? = null
    ): Boolean {
        if (restSeconds < 30 * 60) return false
        val valid = samples
            .filter {
                it.voltageV != null &&
                    it.lastComTime != null &&
                    it.lastComTime > 0 &&
                    it.observedAt == it.lastComTime &&
                    (cycleStartedAt == null || it.observedAt >= cycleStartedAt) &&
                    !it.cacheSuspect
            }
            .distinctBy { it.lastComTime }
            .sortedBy { it.observedAt }
        if (valid.isEmpty()) return false
        val restStart = valid.last().observedAt - restSeconds
        val currentRest = valid
            .filter { it.observedAt >= restStart }
            .distinctBy { it.lastComTime }
        if (currentRest.size < 2) return false
        val last = currentRest.last()
        val previous = currentRest[currentRest.lastIndex - 1]
        return abs(last.voltageV!! - previous.voltageV!!) <= 0.05
    }

    fun chargeSignal(samples: List<VoltageSampleEntity>): ChargeSignal? {
        val valid = samples
            .filter {
                it.voltageV != null &&
                    it.lastComTime != null &&
                    it.lastComTime > 0 &&
                    it.observedAt == it.lastComTime &&
                    !it.cacheSuspect
            }
            .distinctBy { it.lastComTime }
            .sortedBy { it.observedAt }
        if (valid.size < 2) return null
        val latest = valid.last()
        val latestDistance = latest.dayDistanceKm
        val sameDistance = valid.filter {
            latestDistance != null &&
                it.dayDistanceKm != null &&
                abs(it.dayDistanceKm - latestDistance) < 0.05
        }
        if (sameDistance.size < 2) return null
        val end = sameDistance.last()
        val candidates = sameDistance.dropLast(1)
        var best: ChargeSignal? = null
        for (start in candidates.reversed()) {
            val window = end.observedAt - start.observedAt
            if (window <= 0) continue
            if (window > 30 * 60) break
            val delta = end.voltageV!! - start.voltageV!!
            val qualifies = (window >= 15 * 60 && delta >= 0.10) ||
                (window >= 30 * 60 && delta >= 0.20)
            if (qualifies && (best == null || delta > best.deltaV)) {
                best = ChargeSignal(delta, window)
            }
        }
        return best
    }

    fun evaluate(
        samples: List<VoltageSampleEntity>,
        anchors: EstimatorAnchors,
        protectionVoltage: Double,
        previous: BatteryState,
        chargePending: Boolean,
        stableLock: StableLock? = null,
        stableCycleStartedAt: Long? = null
    ): BatteryState {
        val latest = samples.maxByOrNull { it.fetchedAt }
            ?: return previous.copy(status = BatteryStatus.WAITING, stale = true)
        val observedAt = latest.lastComTime ?: latest.lastPosTime
        val age = latest.ageSeconds
        val lockStable = stableLock != null &&
            latest.lastComTime == stableLock.communicationTime &&
            latest.voltageV != null &&
            abs(latest.voltageV - stableLock.voltageV) <= STABLE_VOLTAGE_TOLERANCE
        val stable = lockStable ||
            isStable(samples, latest.restSeconds, stableCycleStartedAt)
        val instantSoc = socFromVoltage(latest.voltageV, anchors)
        val provisionalSoc = instantSoc
            ?.takeIf { latest.restSeconds >= 15 * 60 }
        val stableSoc = instantSoc.takeIf { stable }
        val displaySoc = stableSoc
            ?: provisionalSoc
            ?: instantSoc
            ?: previous.displaySocPercent
            ?: previous.socPercent
        val status = when {
            latest.state == "e_line_down" -> BatteryStatus.OFFLINE
            chargePending -> BatteryStatus.CHARGE_UNKNOWN
            lockStable -> BatteryStatus.STABLE
            age > 30 * 60 || observedAt == null -> BatteryStatus.STALE
            stable -> BatteryStatus.STABLE
            latest.restSeconds < 30 * 60 -> BatteryStatus.REBOUND
            else -> BatteryStatus.WAITING
        }
        val confidence = if (stable && stableSoc != null) {
            confidenceForRest(latest.restSeconds)
        } else {
            Confidence.LOW
        }
        val fullSuggestion = stable &&
            (latest.voltageV ?: 0.0) >=
            ((anchors.fullVoltage ?: DEFAULT_FULL_VOLTAGE) - 0.3)
        return previous.copy(
            socPercent = displaySoc,
            instantSocPercent = instantSoc,
            provisionalSocPercent = provisionalSoc,
            stableSocPercent = stableSoc,
            displaySocPercent = displaySoc,
            rangeKm = rangeKm(displaySoc),
            voltageV = latest.voltageV ?: previous.voltageV,
            status = status,
            confidence = confidence,
            restSeconds = latest.restSeconds,
            voltageObservedAt = observedAt,
            ageSeconds = age,
            stale = status == BatteryStatus.STALE || status == BatteryStatus.OFFLINE,
            protectionVoltage = protectionVoltage,
            fullSuggestion = fullSuggestion,
            locatorPowerPct = latest.locatorPowerPct,
            rawAccState = latest.rawAccState,
            rawAccPower = latest.rawAccPower,
            lastFetchedAt = latest.fetchedAt
        )
    }

    fun canKeepStableLock(
        lock: StableLock,
        communicationTime: Long?,
        voltage: Double?,
        mileageIncreased: Boolean
    ): Boolean {
        if (mileageIncreased) return false
        val communicationAdvanced = communicationTime != null &&
            communicationTime >= lock.communicationTime
        val voltageWithinTolerance = voltage == null ||
            abs(voltage - lock.voltageV) <= STABLE_VOLTAGE_TOLERANCE
        return communicationAdvanced && voltageWithinTolerance
    }

    fun isNewCommunication(previousTime: Long?, currentTime: Long?): Boolean =
        currentTime != null &&
            currentTime > 0 &&
            (previousTime == null || currentTime > previousTime)
}
