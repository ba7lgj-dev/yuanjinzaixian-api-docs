package org.ba7lgj.yuanjinzaixian.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "voltage_samples",
    indices = [Index(value = ["fetchedAt"])]
)
data class VoltageSampleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fetchedAt: Long,
    val voltageV: Double?,
    val lastComTime: Long?,
    val lastPosTime: Long?,
    val curTime: Long?,
    val state: String?,
    val stateBeginTime: Long?,
    val stateTime: Long?,
    val dayDistanceRaw: Double?,
    val dayDistanceKm: Double?,
    val oilState: String?,
    val locatorPowerPct: Int?,
    val rawAccState: String?,
    val rawAccPower: String?,
    val observedAt: Long,
    val ageSeconds: Long,
    val restSeconds: Long,
    val cacheSuspect: Boolean,
    val timestampSource: String
)

@Entity(tableName = "battery_anchors")
data class BatteryAnchorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val voltageV: Double,
    val socPercent: Int,
    val createdAt: Long,
    val method: String
)

@Entity(tableName = "charge_events")
data class ChargeEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val detectedAt: Long,
    val startVoltage: Double,
    val endVoltage: Double,
    val deltaV: Double,
    val windowSeconds: Long,
    val status: String = "pending",
    val choice: String? = null,
    val resolvedAt: Long? = null
)

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val account: String? = null,
    val deviceSimei: String? = null,
    val deviceLabel: String? = null,
    val nickname: String? = null,
    val protectionVoltage: Double = 57.8,
    val stationarySince: Long? = null,
    val lastDayDistanceKm: Double? = null,
    val protectionAlerted: Boolean = false
)
