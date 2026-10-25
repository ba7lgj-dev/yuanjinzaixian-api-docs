package org.ba7lgj.yuanjinzaixian.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackPointDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(points: List<TrackPointEntity>)

    @Query(
        "SELECT * FROM track_points WHERE simei = :simei " +
            "AND timeSec BETWEEN :startTime AND :endTime ORDER BY timeSec ASC"
    )
    suspend fun between(simei: String, startTime: Long, endTime: Long): List<TrackPointEntity>

    @Query("SELECT COUNT(*) FROM track_points WHERE simei = :simei")
    suspend fun count(simei: String): Int

    @Query("DELETE FROM track_points WHERE timeSec < :cutoff")
    suspend fun deleteOlderThan(cutoff: Long)
}

@Dao
interface RestAnchorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anchor: RestAnchorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(anchors: List<RestAnchorEntity>)

    @Query("SELECT * FROM rest_anchors WHERE anchorKey = :anchorKey LIMIT 1")
    suspend fun byKey(anchorKey: String): RestAnchorEntity?

    @Query("SELECT * FROM rest_anchors WHERE simei = :simei ORDER BY observedAt ASC")
    suspend fun all(simei: String): List<RestAnchorEntity>

    @Query(
        "SELECT * FROM rest_anchors WHERE simei = :simei AND observedAt BETWEEN :startTime AND :endTime " +
            "ORDER BY observedAt ASC"
    )
    suspend fun between(simei: String, startTime: Long, endTime: Long): List<RestAnchorEntity>

    @Query(
        "UPDATE rest_anchors SET grade = :grade, restSeconds = :restSeconds, voltageV = :voltage, " +
            "socPercent = :soc WHERE anchorKey = :anchorKey"
    )
    suspend fun upgrade(
        anchorKey: String,
        grade: String,
        restSeconds: Long,
        voltage: Double,
        soc: Int
    )

    @Query("DELETE FROM rest_anchors")
    suspend fun deleteAll()

    @Query("DELETE FROM rest_anchors WHERE anchorKey LIKE 'history:%' AND observedAt < :cutoff")
    suspend fun deleteHistoryOlderThan(cutoff: Long)
}

@Dao
interface PauseIntervalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pauses: List<PauseIntervalEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pause: PauseIntervalEntity)

    @Query(
        "SELECT * FROM pause_intervals WHERE simei = :simei " +
            "AND endTime > :startTime AND startTime < :endTime ORDER BY startTime ASC"
    )
    suspend fun between(
        simei: String,
        startTime: Long,
        endTime: Long
    ): List<PauseIntervalEntity>

    @Query("SELECT * FROM pause_intervals WHERE cycleKey = :cycleKey ORDER BY startTime ASC")
    suspend fun forCycle(cycleKey: String): List<PauseIntervalEntity>

    @Query("SELECT * FROM pause_intervals WHERE pauseKey = :pauseKey LIMIT 1")
    suspend fun byKey(pauseKey: String): PauseIntervalEntity?

    @Query("UPDATE pause_intervals SET active = :active, updatedAt = :updatedAt WHERE pauseKey = :pauseKey")
    suspend fun setActive(pauseKey: String, active: Boolean, updatedAt: Long)

    @Query(
        "UPDATE pause_intervals SET startTime = :startTime, endTime = :endTime, " +
            "cycleKey = NULL, updatedAt = :updatedAt WHERE pauseKey = :pauseKey"
    )
    suspend fun move(pauseKey: String, startTime: Long, endTime: Long, updatedAt: Long)

    @Query("UPDATE pause_intervals SET cycleKey = :cycleKey WHERE pauseKey = :pauseKey")
    suspend fun setCycle(pauseKey: String, cycleKey: String)

    @Query("DELETE FROM pause_intervals WHERE endTime < :cutoff AND cycleKey IS NULL")
    suspend fun deleteOrphanOlderThan(cutoff: Long)
}

@Dao
interface PlatformTripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trips: List<PlatformTripEntity>)

    @Query(
        "SELECT * FROM platform_trips WHERE simei = :simei " +
            "AND endTime > :startTime AND startTime < :endTime ORDER BY startTime ASC"
    )
    suspend fun between(simei: String, startTime: Long, endTime: Long): List<PlatformTripEntity>

    @Query("DELETE FROM platform_trips WHERE endTime < :cutoff")
    suspend fun deleteOlderThan(cutoff: Long)
}

@Dao
interface TripCycleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cycle: TripCycleEntity)

    @Query("SELECT * FROM trip_cycles WHERE cycleKey = :cycleKey LIMIT 1")
    suspend fun byKey(cycleKey: String): TripCycleEntity?

    @Query("SELECT * FROM trip_cycles ORDER BY startTime DESC")
    suspend fun all(): List<TripCycleEntity>

    @Query(
        "SELECT * FROM trip_cycles WHERE startTime BETWEEN :startTime AND :endTime " +
            "ORDER BY startTime DESC"
    )
    suspend fun between(startTime: Long, endTime: Long): List<TripCycleEntity>

    @Query(
        "UPDATE trip_cycles SET trainingEligible = :eligible, trainingReason = :reason, " +
            "updatedAt = :updatedAt WHERE cycleKey = :cycleKey"
    )
    suspend fun setTraining(
        cycleKey: String,
        eligible: Boolean,
        reason: String,
        updatedAt: Long
    )

    @Query("SELECT COUNT(*) FROM trip_cycles WHERE trainingEligible = 1")
    suspend fun trainingCount(): Int

    @Query("DELETE FROM trip_cycles WHERE startTime < :cutoff")
    suspend fun deleteOlderThan(cutoff: Long)
}

@Dao
interface TripSegmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(segments: List<TripSegmentEntity>)

    @Query("SELECT * FROM trip_segments WHERE cycleKey = :cycleKey ORDER BY sequence ASC")
    suspend fun forCycle(cycleKey: String): List<TripSegmentEntity>

    @Query("SELECT * FROM trip_segments WHERE segmentKey = :segmentKey LIMIT 1")
    suspend fun byKey(segmentKey: String): TripSegmentEntity?

    @Query("SELECT * FROM trip_segments ORDER BY startTime ASC")
    suspend fun all(): List<TripSegmentEntity>

    @Query("UPDATE trip_segments SET peopleCount = :people, updatedAt = :updatedAt WHERE segmentKey = :segmentKey")
    suspend fun setPeople(segmentKey: String, people: Int?, updatedAt: Long)

    @Query("DELETE FROM trip_segments WHERE cycleKey = :cycleKey")
    suspend fun deleteByCycle(cycleKey: String)

    @Query("DELETE FROM trip_segments WHERE cycleKey NOT IN (SELECT cycleKey FROM trip_cycles)")
    suspend fun deleteOrphans()
}

@Dao
interface HistoryImportDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(day: HistoryImportDayEntity)

    @Query("SELECT * FROM history_import_days WHERE dateKey = :dateKey LIMIT 1")
    suspend fun get(dateKey: String): HistoryImportDayEntity?

    @Query("SELECT * FROM history_import_days ORDER BY dateKey DESC")
    suspend fun all(): List<HistoryImportDayEntity>

    @Query("SELECT COUNT(*) FROM history_import_days WHERE status = 'complete'")
    suspend fun completedCount(): Int

    @Query("SELECT MAX(importedAt) FROM history_import_days")
    suspend fun lastImportAt(): Long?
}

@Dao
interface ModelVersionDao {
    @Insert
    suspend fun insert(model: ModelVersionEntity): Long

    @Query("SELECT * FROM model_versions WHERE active = 1 ORDER BY trainedAt DESC LIMIT 1")
    suspend fun active(): ModelVersionEntity?

    @Query("SELECT * FROM model_versions WHERE id = :id LIMIT 1")
    suspend fun byId(id: Long): ModelVersionEntity?

    @Query("SELECT * FROM model_versions ORDER BY trainedAt DESC")
    suspend fun all(): List<ModelVersionEntity>

    @Query("UPDATE model_versions SET active = 0")
    suspend fun deactivateAll()

    @Query("UPDATE model_versions SET active = 1 WHERE id = :id")
    suspend fun activate(id: Long)

    @Query("SELECT * FROM model_versions WHERE trainedAt < :cutoff ORDER BY trainedAt DESC")
    suspend fun olderThan(cutoff: Long): List<ModelVersionEntity>
}

@Dao
interface MonthlySummaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(summaries: List<MonthlySummaryEntity>)

    @Query("SELECT * FROM monthly_summaries ORDER BY monthKey DESC")
    suspend fun all(): List<MonthlySummaryEntity>
}

@Dao
interface RangeStateDao {
    @Query("SELECT * FROM range_state WHERE id = 1")
    suspend fun get(): RangeStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(state: RangeStateEntity)
}
