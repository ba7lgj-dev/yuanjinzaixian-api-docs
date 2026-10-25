package org.ba7lgj.yuanjinzaixian.domain

data class TripPoint(
    val timeSec: Long,
    val lat: Double,
    val lon: Double,
    val altitudeM: Double?,
    val altitudeUsable: Boolean,
    val speedKmh: Double?,
    val segmentDistanceM: Double?
)

data class PauseSpan(
    val pauseKey: String,
    val startTime: Long,
    val endTime: Long,
    val distanceM: Double = 0.0,
    val source: String = "platform",
    val active: Boolean = true
) {
    val durationSeconds: Long get() = (endTime - startTime).coerceAtLeast(0)
}

data class MotionInterval(
    val startTime: Long,
    val endTime: Long
) {
    val durationSeconds: Long get() = (endTime - startTime).coerceAtLeast(0)
}

data class SegmentMetrics(
    val distanceKm: Double,
    val durationSeconds: Long,
    val avgSpeedKmh: Double,
    val maxSpeedKmh: Double,
    val speedStdKmh: Double,
    val ascentM: Double,
    val descentM: Double,
    val downhillRatio: Double,
    val positiveAccel: Double,
    val negativeAccel: Double,
    val regenProxyPerKm: Double
)

data class RangeProfile(
    val avgSpeedKmh: Double = 20.0,
    val ascentPerKm: Double = 5.0,
    val descentPerKm: Double = 5.0,
    val downhillRatio: Double = 0.2,
    val positiveAccel: Double = 0.4,
    val negativeAccel: Double = 0.4,
    val regenProxyPerKm: Double = 0.5
)

data class RangeTrainingSample(
    val socDropPerKm: Double,
    val peopleCount: Int,
    val startSoc: Double,
    val profile: RangeProfile
)

data class RangePrediction(
    val rangeKm: Double,
    val socPerKm: Double,
    val source: String,
    val modelId: Long? = null
)

data class TrainedRangeModel(
    val kind: String,
    val sampleCount: Int,
    val maeSocPerKm: Double,
    val intercept: Double = 0.0,
    val coefficients: DoubleArray = DoubleArray(0),
    val medians: Map<String, Double> = emptyMap(),
    val bandModels: Map<String, BandLinearModel> = emptyMap(),
    val featureVersion: Int = 1
)

data class BandLinearModel(
    val intercept: Double,
    val coefficients: DoubleArray
)

data class LiveRangeSnapshot(
    val available: Boolean,
    val remainingKm: Int?,
    val anchorRangeKm: Int?,
    val distanceSinceAnchorKm: Double,
    val anchorVoltageV: Double?,
    val anchorObservedAt: Long?,
    val source: String,
    val currentPeople: Int,
    val modelState: String
)

data class HistorySegmentView(
    val segmentKey: String,
    val cycleKey: String,
    val sequence: Int,
    val startTime: Long,
    val endTime: Long,
    val distanceKm: Double,
    val avgSpeedKmh: Double,
    val ascentM: Double,
    val descentM: Double,
    val peopleCount: Int?
)

data class HistoryPauseView(
    val pauseKey: String,
    val cycleKey: String,
    val startTime: Long,
    val endTime: Long,
    val source: String
)

data class HistoryCycleView(
    val cycleKey: String,
    val startTime: Long,
    val endTime: Long,
    val startVoltageV: Double,
    val endVoltageV: Double,
    val distanceKm: Double,
    val socDrop: Int?,
    val trainingEligible: Boolean,
    val trainingReason: String,
    val dateKey: String,
    val editable: Boolean,
    val segments: List<HistorySegmentView>,
    val pauses: List<HistoryPauseView>
)

data class HistoryDayView(
    val dateKey: String,
    val cycles: List<HistoryCycleView>
)

data class HistoryMonthView(
    val monthKey: String,
    val cycleCount: Int,
    val distanceKm: Double
)

data class HistoryOverview(
    val recentDays: List<HistoryDayView>,
    val olderMonths: List<HistoryMonthView>,
    val importedDays: Int,
    val lastImportAt: Long?
)

data class HistorySyncResult(
    val importedDays: Int,
    val cycles: Int,
    val trainingCycles: Int,
    val lastError: String? = null,
    val morePending: Boolean = false
)

data class TrainingStatus(
    val cycleCount: Int = 0,
    val trainingCycleCount: Int = 0,
    val modelKind: String? = null,
    val sampleCount: Int = 0,
    val maeSocPerKm: Double? = null,
    val historyInitialDone: Boolean = false,
    val lastSyncAt: Long? = null
) {
    val modelLabel: String
        get() = when (modelKind) {
            "median" -> "本地中位数"
            "regression" -> "本地回归"
            "band_regression" -> "分区间回归"
            else -> "物理基准"
        }
}
