package org.ba7lgj.yuanjinzaixian

import org.ba7lgj.yuanjinzaixian.domain.DistanceAccounting
import org.ba7lgj.yuanjinzaixian.domain.TrainingRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class DistanceAndTrainingRulesTest {
    @Test
    fun sameDayUsesPositiveIncrement() {
        assertEquals(
            2.5,
            DistanceAccounting.addedKm("2026-10-25", 10.0, "2026-10-25", 12.5),
            1e-6
        )
    }

    @Test
    fun sameDayResetOrJumpIsIgnored() {
        assertEquals(
            0.0,
            DistanceAccounting.addedKm("2026-10-25", 12.5, "2026-10-25", 3.0),
            1e-6
        )
        assertEquals(
            0.0,
            DistanceAccounting.addedKm("2026-10-25", 12.5, "2026-10-25", 80.0),
            1e-6
        )
    }

    @Test
    fun crossMidnightAddsNewDayDistance() {
        assertEquals(
            4.2,
            DistanceAccounting.addedKm("2026-10-24", 18.0, "2026-10-25", 4.2),
            1e-6
        )
    }

    @Test
    fun firstSampleOnlyEstablishesBaseline() {
        assertEquals(0.0, DistanceAccounting.addedKm(null, null, "2026-10-25", 8.0), 1e-6)
    }

    @Test
    fun eligibleCyclePassesAllRules() {
        assertEquals(TrainingRules.ELIGIBLE, TrainingRules.reason(validInput()))
    }

    @Test
    fun eachMissingPrerequisiteRejectsTraining() {
        assertNotEquals(
            TrainingRules.ELIGIBLE,
            TrainingRules.reason(validInput().copy(startIsTrainingAnchor = false))
        )
        assertNotEquals(
            TrainingRules.ELIGIBLE,
            TrainingRules.reason(validInput().copy(endIsTrainingAnchor = false))
        )
        assertNotEquals(
            TrainingRules.ELIGIBLE,
            TrainingRules.reason(validInput().copy(chargingSuspected = true))
        )
        assertNotEquals(
            TrainingRules.ELIGIBLE,
            TrainingRules.reason(validInput().copy(unlabelledSegments = 1))
        )
        assertNotEquals(
            TrainingRules.ELIGIBLE,
            TrainingRules.reason(validInput().copy(coveredKm = 5.0))
        )
        assertNotEquals(
            TrainingRules.ELIGIBLE,
            TrainingRules.reason(validInput().copy(startSoc = 50, endSoc = 50))
        )
        assertNotEquals(
            TrainingRules.ELIGIBLE,
            TrainingRules.reason(validInput().copy(totalKm = 0.05))
        )
    }

    private fun validInput() = TrainingRules.Input(
        startIsTrainingAnchor = true,
        endIsTrainingAnchor = true,
        chargingSuspected = false,
        segmentCount = 2,
        unlabelledSegments = 0,
        coveredKm = 9.6,
        totalKm = 10.0,
        startSoc = 80,
        endSoc = 55,
        durationSeconds = 3_600
    )
}
