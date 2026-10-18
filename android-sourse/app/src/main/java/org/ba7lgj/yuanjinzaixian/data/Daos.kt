package org.ba7lgj.yuanjinzaixian.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VoltageSampleDao {
    @Insert
    suspend fun insert(sample: VoltageSampleEntity): Long

    @Query("SELECT * FROM voltage_samples ORDER BY fetchedAt DESC LIMIT :limit")
    suspend fun recent(limit: Int): List<VoltageSampleEntity>

    @Query("SELECT * FROM voltage_samples ORDER BY fetchedAt DESC LIMIT 1")
    suspend fun latest(): VoltageSampleEntity?

    @Query("DELETE FROM voltage_samples WHERE fetchedAt < :cutoff")
    suspend fun deleteOlderThan(cutoff: Long)
}

@Dao
interface BatteryAnchorDao {
    @Insert
    suspend fun insert(anchor: BatteryAnchorEntity): Long

    @Query("SELECT * FROM battery_anchors WHERE type = :type ORDER BY createdAt DESC LIMIT 1")
    suspend fun latest(type: String): BatteryAnchorEntity?

    @Query("SELECT * FROM battery_anchors ORDER BY createdAt DESC LIMIT :limit")
    suspend fun recent(limit: Int): List<BatteryAnchorEntity>

    @Query("DELETE FROM battery_anchors")
    suspend fun deleteAll()
}

@Dao
interface AnchorRequestDao {
    @Insert
    suspend fun insert(request: AnchorRequestEntity): Long

    @Query(
        "SELECT * FROM anchor_requests WHERE type = :type AND status = 'pending' " +
            "ORDER BY requestedAt DESC LIMIT 1"
    )
    suspend fun latestPending(type: String): AnchorRequestEntity?

    @Query("SELECT * FROM anchor_requests ORDER BY requestedAt DESC LIMIT 1")
    suspend fun latest(): AnchorRequestEntity?

    @Query(
        "UPDATE anchor_requests SET status = :status WHERE id = :id"
    )
    suspend fun resolve(id: Long, status: String)

    @Query("DELETE FROM anchor_requests")
    suspend fun deleteAll()
}

@Dao
interface ChargeEventDao {
    @Insert
    suspend fun insert(event: ChargeEventEntity): Long

    @Query("SELECT * FROM charge_events WHERE status = 'pending' ORDER BY detectedAt DESC LIMIT 1")
    suspend fun latestPending(): ChargeEventEntity?

    @Query("SELECT * FROM charge_events ORDER BY detectedAt DESC LIMIT 1")
    suspend fun latest(): ChargeEventEntity?

    @Query(
        "UPDATE charge_events SET status = :status, choice = :choice, resolvedAt = :resolvedAt " +
            "WHERE id = :id"
    )
    suspend fun resolve(id: Long, status: String, choice: String, resolvedAt: Long)
}

@Dao
interface AppSettingsDao {
    @Query("SELECT * FROM app_settings WHERE id = 1")
    suspend fun get(): AppSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(settings: AppSettingsEntity)
}
