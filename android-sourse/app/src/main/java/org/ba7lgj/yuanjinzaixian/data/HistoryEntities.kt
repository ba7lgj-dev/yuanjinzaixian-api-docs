package org.ba7lgj.yuanjinzaixian.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "track_points",
    primaryKeys = ["simei", "timeSec", "latE6", "lonE6"]
)
data class TrackPointEntity(
    val simei: String,
    val timeSec: Long,
    val latE6: Int,
    val lonE6: Int,
    val wgs: String?,
    val altitudeM: Double?,
    val altitudeUsable: Boolean,
    val speedRaw: Double?,
    val speedKmh: Double?,
    val direction: Double?,
    val segmentDistanceM: Double?,
    val ptype: String?,
    val externalVoltageV: Double?,
    val importedAt: Long
)

@Entity(tableName = "rest_anchors")
data class RestAnchorEntity(
    @PrimaryKey val anchorKey: String,
    val simei: String,
    val observedAt: Long,
    val voltageV: Double,
    val socPercent: Int,
    val restSeconds: Long,
    val grade: String,
    val source: String,
    val dayKey: String,
    val dayDistanceKm: Double?,
    val cycleStartedAt: Long?,
    val createdAt: Long
)

@Entity(tableName = "pause_intervals")
data class PauseIntervalEntity(
    @PrimaryKey val pauseKey: String,
    val simei: String,
    val startTime: Long,
    val endTime: Long,
    val distanceM: Double,
    val source: String,
    val active: Boolean,
    val cycleKey: String?,
    val updatedAt: Long
)

@Entity(tableName = "platform_trips")
data class PlatformTripEntity(
    @PrimaryKey val tripKey: String,
    val simei: String,
    val startTime: Long,
    val endTime: Long,
    val distanceM: Double,
    val importedAt: Long
)

@Entity(tableName = "trip_cycles")
data class TripCycleEntity(
    @PrimaryKey val cycleKey: String,
    val simei: String,
    val startAnchorKey: String,
    val endAnchorKey: String,
    val startTime: Long,
    val endTime: Long,
    val startVoltageV: Double,
    val endVoltageV: Double,
    val startSoc: Int,
    val endSoc: Int,
    val distanceKm: Double,
    val dateKey: String,
    val trainingEligible: Boolean,
    val trainingReason: String,
    val chargingSuspected: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "trip_segments")
data class TripSegmentEntity(
    @PrimaryKey val segmentKey: String,
    val cycleKey: String,
    val sequence: Int,
    val startTime: Long,
    val endTime: Long,
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
    val regenProxyPerKm: Double,
    val peopleCount: Int?,
    val updatedAt: Long
)

@Entity(tableName = "history_import_days")
data class HistoryImportDayEntity(
    @PrimaryKey val dateKey: String,
    val status: String,
    val pointCount: Int,
    val tripCount: Int,
    val pauseCount: Int,
    val lastError: String?,
    val importedAt: Long
)

@Entity(tableName = "model_versions")
data class ModelVersionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val kind: String,
    val sampleCount: Int,
    val maeSocPerKm: Double,
    val coefficients: String,
    val trainedAt: Long,
    val active: Boolean
)

@Entity(tableName = "monthly_summaries")
data class MonthlySummaryEntity(
    @PrimaryKey val monthKey: String,
    val cycleCount: Int,
    val distanceKm: Double,
    val avgSocPerKm: Double?,
    val updatedAt: Long
)

@Entity(tableName = "range_state")
data class RangeStateEntity(
    @PrimaryKey val id: Int = 1,
    val activeAnchorKey: String? = null,
    val anchorInvalidated: Boolean = false,
    val distanceSinceAnchorKm: Double = 0.0,
    val lastDayKey: String? = null,
    val lastDayDistanceKm: Double? = null,
    val currentPeopleCount: Int = 1,
    val modelVersionId: Long? = null,
    val historyInitialDone: Boolean = false,
    val lastHistorySyncAt: Long? = null
)
