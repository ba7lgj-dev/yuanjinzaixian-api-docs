package org.ba7lgj.yuanjinzaixian.data

data class FamilyInfo(
    val sid: String,
    val name: String
)

data class DeviceInfo(
    val familyId: String,
    val simei: String,
    val imei: Long?,
    val label: String,
    val dayDistanceRaw: Double?
)

data class LoginResult(
    val sid: String,
    val families: List<FamilyInfo>
)

data class DeviceDetail(
    val simei: String,
    val voltageV: Double?,
    val lastComTime: Long?,
    val lastPosTime: Long?,
    val lat: Double?,
    val lon: Double?,
    val wgs: String?,
    val curTime: Long?,
    val state: String?,
    val stateBeginTime: Long?,
    val stateTime: Long?,
    val dayDistanceRaw: Double?,
    val dayDistanceKm: Double?,
    val oilState: String?,
    val locatorPowerPct: Int?,
    val rawAccState: String?,
    val rawAccPower: String?
)

data class TrackDayInfo(
    val dateKey: String,
    val distanceKm: Double
)

data class TrackPointRecord(
    val timeSec: Long,
    val lat: Double,
    val lon: Double,
    val wgs: String?,
    val altitudeM: Double?,
    val altitudeUsable: Boolean,
    val speedRaw: Double?,
    val speedKmh: Double?,
    val direction: Double?,
    val segmentDistanceM: Double?,
    val ptype: String?,
    val externalVoltageV: Double?
)

data class PauseRecord(
    val pauseKey: String,
    val startTime: Long,
    val endTime: Long,
    val distanceM: Double
)

data class PlatformTripRecord(
    val tripKey: String,
    val startTime: Long,
    val endTime: Long,
    val distanceM: Double
)

class BiotApiException(
    val code: Long,
    override val message: String
) : Exception(message)
