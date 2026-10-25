package org.ba7lgj.yuanjinzaixian.domain

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

object TripAnalytics {
    private const val EARTH_RADIUS_M = 6_371_000.0

    fun haversineMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        if (lat1 == 0.0 && lon1 == 0.0) return 0.0
        if (lat2 == 0.0 && lon2 == 0.0) return 0.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)
        return EARTH_RADIUS_M * 2 * atan2(sqrt(a), sqrt(1 - a))
    }

    fun totalDistanceKm(points: List<TripPoint>): Double {
        val ordered = points.sortedBy { it.timeSec }
        if (ordered.size < 2) return 0.0
        var totalM = 0.0
        var previous = ordered.first()
        for (point in ordered.drop(1)) {
            val explicit = point.segmentDistanceM?.takeIf { it in 0.5..20_000.0 }
            val distance = explicit ?: haversineMeters(
                previous.lat,
                previous.lon,
                point.lat,
                point.lon
            )
            if (distance in 0.5..20_000.0) totalM += distance
            previous = point
        }
        return totalM / 1000.0
    }

    fun motionIntervals(
        startTime: Long,
        endTime: Long,
        pauses: List<PauseSpan>
    ): List<MotionInterval> {
        if (endTime <= startTime) return emptyList()
        val clipped = pauses
            .filter { it.active && it.endTime > startTime && it.startTime < endTime }
            .map {
                PauseSpan(
                    pauseKey = it.pauseKey,
                    startTime = max(startTime, it.startTime),
                    endTime = minOf(endTime, it.endTime),
                    distanceM = it.distanceM,
                    source = it.source,
                    active = true
                )
            }
            .filter { it.durationSeconds >= 30 }
            .sortedBy { it.startTime }

        val result = mutableListOf<MotionInterval>()
        var cursor = startTime
        for (pause in clipped) {
            if (pause.startTime - cursor >= 30) {
                result += MotionInterval(cursor, pause.startTime)
            }
            cursor = max(cursor, pause.endTime)
        }
        if (endTime - cursor >= 30) result += MotionInterval(cursor, endTime)
        return result
    }

    fun metrics(
        points: List<TripPoint>,
        startTime: Long,
        endTime: Long,
        fallbackDistanceKm: Double = 0.0
    ): SegmentMetrics {
        val ordered = points
            .filter { it.timeSec in startTime..endTime }
            .sortedBy { it.timeSec }
        val duration = (endTime - startTime).coerceAtLeast(1)
        val calculatedDistanceKm = totalDistanceKm(ordered)
        val distanceKm = if (calculatedDistanceKm >= 0.01) {
            calculatedDistanceKm
        } else {
            fallbackDistanceKm.coerceAtLeast(0.0)
        }

        val speeds = ordered.mapNotNull { it.speedKmh?.takeIf { value -> value in 0.0..120.0 } }
        val averageSpeed = if (speeds.isNotEmpty()) {
            speeds.average()
        } else {
            distanceKm / (duration / 3600.0)
        }.coerceIn(0.0, 120.0)
        val maxSpeed = speeds.maxOrNull() ?: averageSpeed
        val variance = if (speeds.isEmpty()) {
            0.0
        } else {
            speeds.sumOf { (it - averageSpeed) * (it - averageSpeed) } / speeds.size
        }

        val altitude = ordered
            .filter { it.altitudeUsable }
            .mapNotNull { it.altitudeM?.takeIf { value -> value in -500.0..5000.0 } }
        var ascent = 0.0
        var descent = 0.0
        var previousAltitude: Double? = null
        altitude.forEach { current ->
            val previous = previousAltitude
            if (previous != null) {
                val delta = current - previous
                if (abs(delta) >= 2.0) {
                    if (delta > 0) ascent += delta else descent += -delta
                    previousAltitude = current
                }
            } else {
                previousAltitude = current
            }
        }

        var positiveAccel = 0.0
        var negativeAccel = 0.0
        var accelSamples = 0
        var previousSpeed: Pair<Long, Double>? = null
        ordered.forEach { point ->
            val speed = point.speedKmh?.takeIf { it in 0.0..120.0 }
            val previous = previousSpeed
            if (speed != null && previous != null) {
                val seconds = (point.timeSec - previous.first).coerceAtLeast(1)
                if (seconds <= 120) {
                    val delta = (speed - previous.second) / seconds
                    if (delta > 0) positiveAccel += delta else negativeAccel += -delta
                    accelSamples++
                }
            }
            if (speed != null) previousSpeed = point.timeSec to speed
        }
        if (accelSamples > 0) {
            positiveAccel /= accelSamples
            negativeAccel /= accelSamples
        }

        val distanceM = distanceKm * 1000.0
        val downhillRatio = if (distanceM > 1.0) (descent / distanceM).coerceIn(0.0, 1.0) else 0.0
        val regenProxy = if (distanceKm > 0.01) {
            (descent * averageSpeed / 25.0 + negativeAccel * 20.0) / distanceKm
        } else {
            0.0
        }

        return SegmentMetrics(
            distanceKm = distanceKm,
            durationSeconds = duration,
            avgSpeedKmh = averageSpeed,
            maxSpeedKmh = maxSpeed,
            speedStdKmh = sqrt(variance),
            ascentM = ascent,
            descentM = descent,
            downhillRatio = downhillRatio,
            positiveAccel = positiveAccel,
            negativeAccel = negativeAccel,
            regenProxyPerKm = regenProxy
        )
    }
}
