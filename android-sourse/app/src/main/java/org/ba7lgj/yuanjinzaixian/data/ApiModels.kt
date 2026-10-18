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

class BiotApiException(
    val code: Long,
    override val message: String
) : Exception(message)
