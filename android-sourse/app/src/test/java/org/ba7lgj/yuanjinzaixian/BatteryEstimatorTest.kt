package org.ba7lgj.yuanjinzaixian

import org.ba7lgj.yuanjinzaixian.data.VoltageSampleEntity
import org.ba7lgj.yuanjinzaixian.domain.BatteryEstimator
import org.ba7lgj.yuanjinzaixian.domain.BatteryState
import org.ba7lgj.yuanjinzaixian.domain.EstimatorAnchors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BatteryEstimatorTest {
    @Test
    fun defaultLeadAcidCurveMapsExpectedVoltagePoints() {
        val anchors = EstimatorAnchors()
        assertEquals(100, BatteryEstimator.socFromVoltage(63.0, anchors))
        assertEquals(50, BatteryEstimator.socFromVoltage(60.3, anchors))
        assertEquals(20, BatteryEstimator.socFromVoltage(57.9, anchors))
        assertEquals(0, BatteryEstimator.socFromVoltage(52.5, anchors))
    }

    @Test
    fun rangeUsesActualEmptyInsteadOfTwentyPercentReserve() {
        assertEquals(40, BatteryEstimator.rangeKm(100))
        assertEquals(20, BatteryEstimator.rangeKm(50))
        assertEquals(8, BatteryEstimator.rangeKm(20))
        assertEquals(4, BatteryEstimator.rangeKm(10))
        assertEquals(0, BatteryEstimator.rangeKm(0))
    }

    @Test
    fun stableWindowRequiresTimeAndLowVoltageSpread() {
        val samples = listOf(
            sample(time = 1_000, observed = 1_000, voltage = 60.00),
            sample(time = 1_900, observed = 1_900, voltage = 60.01),
            sample(time = 2_800, observed = 2_800, voltage = 60.02)
        )
        assertTrue(BatteryEstimator.isStable(samples, restSeconds = 30 * 60))
        assertEquals(false, BatteryEstimator.isStable(samples, restSeconds = 29 * 60))
        val noisy = samples.toMutableList().also { it[2] = it[2].copy(voltageV = 60.10) }
        assertEquals(false, BatteryEstimator.isStable(noisy, restSeconds = 30 * 60))
    }

    @Test
    fun duplicateCommunicationTimeIsNotStable() {
        val samples = listOf(
            sample(time = 1_000, observed = 1_000, voltage = 60.00),
            sample(time = 1_000, observed = 1_000, voltage = 60.01, cache = true),
            sample(time = 1_900, observed = 1_900, voltage = 60.01)
        )
        assertEquals(false, BatteryEstimator.isStable(samples, restSeconds = 30 * 60))
    }

    @Test
    fun chargeSignalDetectsRiseAtSameMileage() {
        val samples = listOf(
            sample(time = 1_000, observed = 1_000, voltage = 60.00),
            sample(time = 1_900, observed = 1_900, voltage = 60.16),
            sample(time = 2_800, observed = 2_800, voltage = 60.25)
        )
        val signal = BatteryEstimator.chargeSignal(samples)
        assertNotNull(signal)
        assertTrue(signal!!.deltaV >= 0.20)
    }

    @Test
    fun missingVoltageDoesNotProduceSoc() {
        assertNull(BatteryEstimator.socFromVoltage(null, EstimatorAnchors()))
        assertNull(BatteryEstimator.rangeKm(null))
    }

    @Test
    fun instantSocIsAvailableBeforeProvisionalWindow() {
        val state = BatteryEstimator.evaluate(
            samples = listOf(
                sample(
                    time = 1_000,
                    observed = 1_000,
                    voltage = 60.04,
                    rest = 10 * 60
                )
            ),
            anchors = EstimatorAnchors(),
            protectionVoltage = 57.8,
            previous = BatteryState(),
            chargePending = false
        )
        assertNotNull(state.instantSocPercent)
        assertNull(state.provisionalSocPercent)
        assertEquals(state.instantSocPercent, state.displaySocPercent)
    }

    @Test
    fun fifteenMinutesProducesProvisionalButNotStableSoc() {
        val state = BatteryEstimator.evaluate(
            samples = listOf(
                sample(
                    time = 1_000,
                    observed = 1_000,
                    voltage = 60.04,
                    rest = 15 * 60
                )
            ),
            anchors = EstimatorAnchors(),
            protectionVoltage = 57.8,
            previous = BatteryState(),
            chargePending = false
        )
        assertNotNull(state.provisionalSocPercent)
        assertNull(state.stableSocPercent)
    }

    @Test
    fun thirtyMinutesAndStableWindowProducesStableSoc() {
        val samples = listOf(
            sample(time = 1_000, observed = 1_000, voltage = 60.00, rest = 30 * 60),
            sample(time = 1_900, observed = 1_900, voltage = 60.01, rest = 30 * 60),
            sample(time = 2_800, observed = 2_800, voltage = 60.02, rest = 30 * 60)
        )
        val state = BatteryEstimator.evaluate(
            samples = samples,
            anchors = EstimatorAnchors(),
            protectionVoltage = 57.8,
            previous = BatteryState(),
            chargePending = false
        )
        assertNotNull(state.stableSocPercent)
        assertEquals(state.stableSocPercent, state.displaySocPercent)
    }

    @Test
    fun calibratedAnchorsShiftCurve() {
        val defaultSoc = BatteryEstimator.socFromVoltage(60.0, EstimatorAnchors())
        val calibratedSoc = BatteryEstimator.socFromVoltage(
            60.0,
            EstimatorAnchors(fullVoltage = 64.0, emptyVoltage = 53.0)
        )
        assertNotNull(defaultSoc)
        assertNotNull(calibratedSoc)
        assertTrue(defaultSoc != calibratedSoc)
    }

    private fun sample(
        time: Long,
        observed: Long,
        voltage: Double,
        cache: Boolean = false,
        rest: Long = 30 * 60
    ) = VoltageSampleEntity(
        fetchedAt = observed,
        voltageV = voltage,
        lastComTime = time,
        lastPosTime = time,
        curTime = observed + 10,
        state = "e_line_sleep",
        stateBeginTime = observed - 60,
        stateTime = 60,
        dayDistanceRaw = 0.0,
        dayDistanceKm = 0.0,
        oilState = "e_oil_normal",
        locatorPowerPct = 100,
        rawAccState = "e_acc_close",
        rawAccPower = "e_acc_power_charge",
        observedAt = observed,
        ageSeconds = 10,
        restSeconds = rest,
        cacheSuspect = cache,
        timestampSource = "communication"
    )
}
