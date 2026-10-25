package org.ba7lgj.yuanjinzaixian

import org.ba7lgj.yuanjinzaixian.domain.TimeKeys
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TimeKeysTest {
    private val now = 1_790_000_000L

    @Test
    fun sevenDayEditableWindow() {
        assertTrue(TimeKeys.isEditable(TimeKeys.dateKey(now), now, 7))
        assertTrue(TimeKeys.isEditable(TimeKeys.daysAgoKey(now, 7), now, 7))
        assertFalse(TimeKeys.isEditable(TimeKeys.daysAgoKey(now, 8), now, 7))
    }

    @Test
    fun monthKeyGroupsByCalendarMonth() {
        val dateKey = TimeKeys.dateKey(now)
        assertEquals(dateKey.take(7), TimeKeys.monthKey(dateKey))
        assertEquals(dateKey.take(7), TimeKeys.monthKey(now))
    }

    @Test
    fun dayLabelsTodayAndYesterday() {
        assertEquals("今天", TimeKeys.dayLabel(TimeKeys.dateKey(now), now))
        assertEquals(
            "昨天",
            TimeKeys.dayLabel(TimeKeys.daysAgoKey(now, 1), now)
        )
    }

    @Test
    fun crossMidnightIsDifferentDay() {
        val dayEnd = TimeKeys.endOfDay(TimeKeys.dateKey(now))
        assertFalse(TimeKeys.sameDay(dayEnd, dayEnd + 1))
        assertTrue(TimeKeys.sameDay(dayEnd - 60, dayEnd))
    }
}
