package org.ba7lgj.yuanjinzaixian

import org.ba7lgj.yuanjinzaixian.domain.PeopleRules
import org.ba7lgj.yuanjinzaixian.domain.RangeModel
import org.ba7lgj.yuanjinzaixian.domain.RangeProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PeopleRulesTest {
    @Test
    fun clampsAnyInputToRiderPlusOnePassenger() {
        assertEquals(1, PeopleRules.clamp(0))
        assertEquals(1, PeopleRules.clamp(1))
        assertEquals(2, PeopleRules.clamp(2))
        assertEquals(2, PeopleRules.clamp(3))
        assertEquals(2, PeopleRules.clamp(4))
    }

    @Test
    fun historicUnsupportedLablesAreRejectedAndNormalised() {
        assertFalse(PeopleRules.isSupported(0))
        assertFalse(PeopleRules.isSupported(3))
        assertFalse(PeopleRules.isSupported(4))
        assertTrue(PeopleRules.isSupported(1))
        assertTrue(PeopleRules.isSupported(2))
        assertEquals(2, PeopleRules.clampOrNull(3))
        assertNull(PeopleRules.clampOrNull(null))
    }

    @Test
    fun labelsExplainThatRiderIsIncluded() {
        assertTrue(PeopleRules.label(1).contains("仅骑行者"))
        assertTrue(PeopleRules.label(2).contains("乘客"))
        assertEquals("2 人", PeopleRules.shortLabel(4))
        assertEquals("未标注", PeopleRules.shortLabel(null))
    }

    @Test
    fun modelNeverUsesMoreThanTwoPeople() {
        val (twoPeople, _) = RangeModel.predictSocPerKm(
            model = null,
            peopleCount = 2,
            anchorSoc = 80.0,
            profile = RangeProfile()
        )
        val (fourPeople, _) = RangeModel.predictSocPerKm(
            model = null,
            peopleCount = 4,
            anchorSoc = 80.0,
            profile = RangeProfile()
        )
        assertEquals(twoPeople, fourPeople, 1e-9)
    }
}
