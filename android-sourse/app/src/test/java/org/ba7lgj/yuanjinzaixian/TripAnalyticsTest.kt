package org.ba7lgj.yuanjinzaixian

import org.ba7lgj.yuanjinzaixian.domain.PauseSpan
import org.ba7lgj.yuanjinzaixian.domain.TripAnalytics
import org.ba7lgj.yuanjinzaixian.domain.TripPoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TripAnalyticsTest {
    @Test
    fun haversineMatchesKnownShortDistance() {
        val meters = TripAnalytics.haversineMeters(30.0000, 120.0000, 30.0010, 120.0000)
        assertEquals(111.2, meters, 2.0)
    }

    @Test
    fun totalDistancePrefersPlatformSegmentDistance() {
        val points = listOf(
            point(0, 30.0000, 120.0000, segment = 120.0),
            point(600, 30.0100, 120.0000, segment = 300.0),
            point(1200, 30.0200, 120.0000, segment = 50.0)
        )
        // 第一个点的 segment 距离没有“上一个点”，因此只累计后两段 300m + 50m。
        assertEquals(0.35, TripAnalytics.totalDistanceKm(points), 0.02)
    }

    @Test
    fun pauseSplitsCycleIntoOrderedMotionIntervals() {
        val intervals = TripAnalytics.motionIntervals(
            startTime = 1_000,
            endTime = 7_600,
            pauses = listOf(
                PauseSpan("p1", 2_000, 3_000),
                PauseSpan("p2", 5_000, 5_600)
            )
        )
        assertEquals(3, intervals.size)
        assertEquals(1_000L, intervals[0].startTime)
        assertEquals(2_000L, intervals[0].endTime)
        assertEquals(3_000L, intervals[1].startTime)
        assertEquals(5_000L, intervals[1].endTime)
        assertEquals(5_600L, intervals[2].startTime)
        assertEquals(7_600L, intervals[2].endTime)
    }

    @Test
    fun metricsDeriveTerrainSpeedAndRegenProxy() {
        val points = listOf(
            point(0, 30.0000, 120.0000, speed = 20.0, altitude = 100.0, segment = 100.0),
            point(60, 30.0010, 120.0000, speed = 25.0, altitude = 112.0, segment = 140.0),
            point(120, 30.0020, 120.0000, speed = 18.0, altitude = 104.0, segment = 150.0)
        )
        val metrics = TripAnalytics.metrics(points, 0, 120, 0.0)
        assertTrue(metrics.distanceKm > 0.25)
        assertEquals(12.0, metrics.ascentM, 1.0)
        assertEquals(8.0, metrics.descentM, 1.0)
        assertTrue(metrics.avgSpeedKmh in 18.0..25.0)
        assertTrue(metrics.maxSpeedKmh >= 25.0)
        assertTrue(metrics.positiveAccel > 0.0)
        assertTrue(metrics.regenProxyPerKm > 0.0)
    }

    private fun point(
        time: Long,
        lat: Double,
        lon: Double,
        speed: Double? = null,
        altitude: Double? = null,
        segment: Double?
    ) = TripPoint(
        timeSec = time,
        lat = lat,
        lon = lon,
        altitudeM = altitude,
        altitudeUsable = altitude != null,
        speedKmh = speed,
        segmentDistanceM = segment
    )
}
