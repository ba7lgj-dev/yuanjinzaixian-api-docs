package org.ba7lgj.yuanjinzaixian

import org.ba7lgj.yuanjinzaixian.work.SamplingScheduler
import org.junit.Assert.assertEquals
import org.junit.Test

class SamplingSchedulerTest {
    private val hourMs = 60 * 60 * 1000L

    @Test
    fun firstMonitorRunsOneHourAfterStableMark() {
        val markedAtSec = 1_000L
        val now = markedAtSec * 1000L
        assertEquals(hourMs, SamplingScheduler.monitorInitialDelayMs(now, markedAtSec))
    }

    @Test
    fun monitorDelayKeepsHourlyCadenceAfterRestart() {
        val markedAtSec = 1_000L
        val tenMinutesLater = markedAtSec * 1000L + 10 * 60 * 1000L
        assertEquals(
            50 * 60 * 1000L,
            SamplingScheduler.monitorInitialDelayMs(tenMinutesLater, markedAtSec)
        )
        assertEquals(
            0L,
            SamplingScheduler.monitorInitialDelayMs(
                markedAtSec * 1000L + hourMs,
                markedAtSec
            )
        )
    }
}
