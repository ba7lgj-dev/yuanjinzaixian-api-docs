package org.ba7lgj.yuanjinzaixian

import org.ba7lgj.yuanjinzaixian.data.VoltageSampleEntity
import org.ba7lgj.yuanjinzaixian.domain.BatteryEstimator
import org.ba7lgj.yuanjinzaixian.domain.BatteryState
import org.ba7lgj.yuanjinzaixian.domain.BatteryStatus
import org.ba7lgj.yuanjinzaixian.domain.EstimatorAnchors
import org.ba7lgj.yuanjinzaixian.domain.StableLock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BatteryEstimatorTest {
    @Test
    fun defaultLeadAcidCurveMapsExpectedVoltagePoints() {
        val anchors = EstimatorAnchors()
        assertEquals(100, BatteryEstimator.socFromVoltage(65.5, anchors))
        assertEquals(53, BatteryEstimator.socFromVoltage(63.0, anchors))
        assertEquals(19, BatteryEstimator.socFromVoltage(60.3, anchors))
        assertEquals(0, BatteryEstimator.socFromVoltage(55.0, anchors))
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
    fun longRestAndTwoCommunicationSamplesCanBecomeStableImmediately() {
        val samples = listOf(
            sample(time = 1_000, observed = 1_000, voltage = 60.00, rest = 30 * 60),
            sample(time = 1_060, observed = 1_060, voltage = 60.04, rest = 31 * 60)
        )
        assertTrue(BatteryEstimator.isStable(samples, restSeconds = 31 * 60))
        val changed = listOf(samples[0], samples[1].copy(voltageV = 60.06))
        assertEquals(false, BatteryEstimator.isStable(changed, restSeconds = 31 * 60))
    }

    @Test
    fun duplicateCommunicationTimeIsNotStable() {
        val samples = listOf(
            sample(time = 1_000, observed = 1_000, voltage = 60.00),
            sample(time = 1_000, observed = 1_000, voltage = 60.01, cache = true)
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
        assertEquals(
            100,
            BatteryEstimator.socFromVoltage(
                64.0,
                EstimatorAnchors(fullVoltage = 64.0, emptyVoltage = 53.0)
            )
        )
        assertEquals(
            0,
            BatteryEstimator.socFromVoltage(
                53.0,
                EstimatorAnchors(fullVoltage = 64.0, emptyVoltage = 53.0)
            )
        )
    }

    @Test
    fun duplicateCommunicationTimesDoNotCreateExtraStableSamples() {
        val samples = listOf(
            sample(time = 1_000, observed = 1_000, voltage = 60.00),
            sample(time = 1_000, observed = 1_500, voltage = 60.01, cache = true),
            sample(time = 1_000, observed = 2_400, voltage = 60.02, cache = true)
        )
        assertEquals(false, BatteryEstimator.isStable(samples, restSeconds = 30 * 60))
    }

    @Test
    fun threeFifteenMinuteSamplesCanBecomeStableWhenTheLastTwoMatch() {
        val samples = listOf(
            sample(time = 1_000, observed = 1_000, voltage = 60.00, rest = 30 * 60),
            sample(time = 1_900, observed = 1_900, voltage = 60.12, rest = 45 * 60),
            sample(time = 2_800, observed = 2_800, voltage = 60.16, rest = 60 * 60)
        )
        assertTrue(BatteryEstimator.isStable(samples, restSeconds = 60 * 60))
    }

    @Test
    fun slowRiseWaitsUntilTwoAdjacentSamplesAreClose() {
        val rising = listOf(
            sample(time = 1_000, observed = 1_000, voltage = 60.00, rest = 60 * 60),
            sample(time = 1_900, observed = 1_900, voltage = 60.10, rest = 75 * 60),
            sample(time = 2_800, observed = 2_800, voltage = 60.20, rest = 90 * 60)
        )
        assertEquals(false, BatteryEstimator.isStable(rising, restSeconds = 90 * 60))
        val settled = rising + sample(
            time = 3_700,
            observed = 3_700,
            voltage = 60.22,
            rest = 105 * 60
        )
        assertTrue(BatteryEstimator.isStable(settled, restSeconds = 105 * 60))
    }

    @Test
    fun stableCycleIgnoresSamplesBeforeItsStart() {
        val samples = listOf(
            sample(time = 1_000, observed = 1_000, voltage = 60.00, rest = 30 * 60),
            sample(time = 1_900, observed = 1_900, voltage = 60.00, rest = 60 * 60),
            sample(time = 2_800, observed = 2_800, voltage = 60.00, rest = 90 * 60)
        )
        assertEquals(
            false,
            BatteryEstimator.isStable(samples, restSeconds = 90 * 60, cycleStartedAt = 2_700)
        )
    }

    @Test
    fun persistedStableLockKeepsPassiveSampleStable() {
        val passive = sample(
            time = 2_800,
            observed = 2_800,
            voltage = 60.03,
            cache = true,
            rest = 90 * 60
        ).copy(ageSeconds = 7 * 60 * 60)
        val state = BatteryEstimator.evaluate(
            samples = listOf(passive),
            anchors = EstimatorAnchors(),
            protectionVoltage = 57.8,
            previous = BatteryState(),
            chargePending = false,
            stableLock = StableLock(
                voltageV = 60.01,
                communicationTime = 2_800,
                markedAt = 2_800
            ),
            stableCycleStartedAt = 1_000
        )
        assertEquals(BatteryStatus.STABLE, state.status)
        assertNotNull(state.stableSocPercent)
    }

    @Test
    fun stableLockEndsOnVoltageDifferenceOrMileageIncrease() {
        val lock = StableLock(
            voltageV = 60.00,
            communicationTime = 2_800,
            markedAt = 2_800
        )
        assertTrue(BatteryEstimator.canKeepStableLock(lock, 2_800, 60.05, false))
        assertTrue(BatteryEstimator.canKeepStableLock(lock, 2_801, 60.00, false))
        assertEquals(false, BatteryEstimator.canKeepStableLock(lock, 2_801, 60.06, false))
        assertEquals(false, BatteryEstimator.canKeepStableLock(lock, 2_801, 60.00, true))
    }

    @Test
    fun onlyAdvancedCommunicationTimeCreatesANewSample() {
        assertTrue(BatteryEstimator.isNewCommunication(null, 2_800))
        assertTrue(BatteryEstimator.isNewCommunication(2_800, 2_801))
        assertEquals(false, BatteryEstimator.isNewCommunication(2_800, 2_800))
        assertEquals(false, BatteryEstimator.isNewCommunication(2_800, 2_799))
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
        observedAt = time,
        ageSeconds = 10,
        restSeconds = rest,
        cacheSuspect = cache,
        timestampSource = "communication"
    )
}
