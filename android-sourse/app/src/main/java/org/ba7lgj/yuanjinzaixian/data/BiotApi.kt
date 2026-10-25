package org.ba7lgj.yuanjinzaixian.data

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONArray
import org.ba7lgj.yuanjinzaixian.domain.TimeKeys
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

class BiotApi {
    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val formType = "application/x-www-form-urlencoded".toMediaType()

    fun login(account: String, password: String): LoginResult {
        val params = JSONObject()
            .put("account", account)
            .put("pwd_md5", md5(password))
            .put("lang", "ch")
            .put("platform", "web")
            .put("type", 2)
            .put(
                "info",
                ";projectName= ;system=windows(Win32);cookieEnabled=true;" +
                    "navigator=chrome;version=155.0.0.0;lang:ch"
            )
        val json = post("user", "Login", params, null)
        val sid = json.optString("sid")
        if (sid.isBlank()) {
            throw BiotApiException(json.optLong("errcode", -1), "登录未返回 sid")
        }
        val families = mutableListOf<FamilyInfo>()
        val array = json.optJSONArray("familys")
        if (array != null) {
            for (i in 0 until array.length()) {
                val item = array.optJSONObject(i) ?: continue
                val familySid = item.optString("sid")
                if (familySid.isNotBlank()) {
                    families += FamilyInfo(
                        sid = familySid,
                        name = item.optString("sname", "组织")
                    )
                }
            }
        }
        return LoginResult(sid, families)
    }

    fun listDevices(familyId: String, sid: String): List<DeviceInfo> {
        val json = post(
            "family",
            "GetDeviceList",
            JSONObject().put("familyid", familyId).put("limit_size", 1000),
            sid
        )
        val result = mutableListOf<DeviceInfo>()
        val items = json.optJSONArray("items") ?: return result
        for (i in 0 until items.length()) {
            val item = items.optJSONObject(i) ?: continue
            val simei = item.optString("simei")
            if (simei.isBlank()) continue
            val imei = item.optLong("imei").takeIf { it > 0 }
            val carNumber = item.optString("car_number")
            val remark = item.optString("remark")
            val label = remark.ifBlank {
                carNumber.ifBlank {
                    imei?.toString() ?: "设备 ${result.size + 1}"
                }
            }
            result += DeviceInfo(
                familyId = familyId,
                simei = simei,
                imei = imei,
                label = label,
                dayDistanceRaw = item.optDouble("day_distance", Double.NaN)
                    .takeUnless { it.isNaN() }
            )
        }
        return result
    }

    fun getDetail(simei: String, sid: String): DeviceDetail {
        val json = post("device", "GetDetail", JSONObject().put("simei", simei), sid)
        val lastPos = json.optString("last_pos")
        val position = parseLastPos(lastPos)
        val rawDayDistance = json.optDouble("day_distance", Double.NaN)
            .takeUnless { it.isNaN() }
        return DeviceDetail(
            simei = simei,
            voltageV = parseVoltage(json.optString("external_voltage")),
            lastComTime = json.optLong("last_com_time").takeIf { it > 0 },
            lastPosTime = position?.optLong("time")?.takeIf { it > 0 },
            lat = position?.optDouble("lat", Double.NaN)
                ?.takeUnless { it.isNaN() }
                ?.let { if (kotlin.math.abs(it) > 1000) it / 1_000_000 else it },
            lon = position?.optDouble("lon", Double.NaN)
                ?.takeUnless { it.isNaN() }
                ?.let { if (kotlin.math.abs(it) > 1000) it / 1_000_000 else it },
            wgs = position?.optString("wgs")?.takeIf { it.isNotBlank() },
            curTime = json.optLong("cur_time").takeIf { it > 0 },
            state = json.optString("state").takeIf { it.isNotBlank() },
            stateBeginTime = json.optLong("state_begin_time").takeIf { it > 0 },
            stateTime = json.optLong("state_time").takeIf { it > 0 },
            dayDistanceRaw = rawDayDistance,
            dayDistanceKm = rawDayDistance?.let(::normalizeDayDistance),
            oilState = json.optString("oil_state").takeIf { it.isNotBlank() },
            locatorPowerPct = json.optInt("power", -1).takeIf { it in 0..100 },
            rawAccState = json.optString("acc_state").takeIf { it.isNotBlank() },
            rawAccPower = json.optString("acc_power").takeIf { it.isNotBlank() }
        )
    }

    fun batchGetAddress(lat: Double, lon: Double, sid: String): String? {
        if (lat !in -90.0..90.0 || lon !in -180.0..180.0) return null
        val json = post(
            "location",
            "BatchGetAddr",
            JSONObject()
                .put("coordtype", "gcj02ll")
                .put("language", "cn")
                .put(
                    "latlng",
                    org.json.JSONArray().put("%.6f,%.6f".format(lon, lat))
                ),
            sid
        )
        val item = json.optJSONArray("items")?.optJSONObject(0) ?: return null
        val address = item.optString("address")
        val unit = item.optString("unit")
        return (address + unit).takeIf { it.isNotBlank() }
    }

    fun queryTrackDates(
        simei: String,
        dateBegin: Long,
        dateEnd: Long,
        sid: String
    ): List<TrackDayInfo> {
        val json = post(
            "location",
            "QueryTrackDate",
            JSONObject()
                .put("date_begin", dateBegin)
                .put("date_end", dateEnd)
                .put("simei", simei),
            sid
        )
        val result = linkedMapOf<String, TrackDayInfo>()
        val dates = json.optJSONArray("date")
        if (dates != null) {
            for (index in 0 until dates.length()) {
                val dateKey = parseDayKey(dates.opt(index)) ?: continue
                result[dateKey] = TrackDayInfo(dateKey, 0.0)
            }
        }
        val items = json.optJSONArray("items")
        if (items != null) {
            for (index in 0 until items.length()) {
                val item = items.optJSONObject(index) ?: continue
                val dateKey = parseDayKey(item.opt("date")) ?: continue
                val distanceKm = distanceMeters(item.opt("distance")) / 1000.0
                result[dateKey] = TrackDayInfo(dateKey, distanceKm.coerceAtLeast(0.0))
            }
        }
        return result.values.sortedBy { it.dateKey }
    }

    fun queryTrackPoints(
        simei: String,
        timeBegin: Long,
        timeEnd: Long,
        sid: String,
        limitSize: Int = 2000
    ): List<TrackPointRecord> {
        val result = mutableListOf<TrackPointRecord>()
        var cursor: Long? = null
        repeat(MAX_PAGES) {
            val params = JSONObject()
                .put("simei", simei)
                .put("time_begin", timeBegin)
                .put("time_end", timeEnd)
                .put("limit_size", limitSize.coerceIn(1, 2000))
            cursor?.let { params.put("last_time", it) }
            val json = post("location", "QueryLocation", params, sid)
            val items = json.optJSONArray("items") ?: return result
            var lastCursor: Long? = null
            for (index in 0 until items.length()) {
                val item = items.optJSONObject(index) ?: continue
                val time = longValue(item.opt("time")) ?: continue
                if (time <= 0) continue
                val lat = coordinate(item.opt("lat"))
                val lon = coordinate(item.opt("lon"))
                val rawSpeed = doubleValue(item.opt("speed"))
                val ptype = item.optString("ptype").takeIf { it.isNotBlank() }
                val altitude = doubleValue(item.opt("altitude"))
                    ?.takeIf { it in -500.0..5000.0 }
                result += TrackPointRecord(
                    timeSec = time,
                    lat = lat ?: 0.0,
                    lon = lon ?: 0.0,
                    wgs = item.optString("wgs").takeIf { it.isNotBlank() },
                    altitudeM = altitude,
                    altitudeUsable = altitude != null && gpsAltitudeUsable(ptype),
                    speedRaw = rawSpeed,
                    speedKmh = rawSpeed?.let { if (it >= 0.0) it / 10.0 else null },
                    direction = doubleValue(item.opt("direction")),
                    segmentDistanceM = distanceMeters(item.opt("distance"))
                        .takeIf { it > 0.0 },
                    ptype = ptype,
                    externalVoltageV = parseVoltage(item.optString("external_voltage"))
                        ?: normalizeVoltage(doubleValue(item.opt("external_voltage")))
                )
                lastCursor = time
            }
            if (items.length() < limitSize || lastCursor == null || lastCursor == cursor) {
                return result
            }
            cursor = lastCursor
        }
        return result
    }

    fun queryPauses(
        simei: String,
        timeBegin: Long,
        timeEnd: Long,
        sid: String,
        stopTimeSeconds: Int = 180
    ): List<PauseRecord> {
        val result = mutableListOf<PauseRecord>()
        var cursor: Long? = null
        repeat(MAX_PAGES) {
            val params = JSONObject()
                .put("simei", JSONArray().put(simei))
                .put("time_begin", timeBegin)
                .put("time_end", timeEnd)
                .put("stop_time", stopTimeSeconds)
                .put("limit_size", 1000)
            cursor?.let {
                params.put("last_simei", simei)
                params.put("last_time", it)
            }
            val json = post("location", "PPoint", params, sid)
            val items = json.optJSONArray("items") ?: return result
            var lastCursor: Long? = null
            for (index in 0 until items.length()) {
                val item = items.optJSONObject(index) ?: continue
                val start = longValue(item.opt("start_time")) ?: continue
                val duration = longValue(item.opt("cos_time")) ?: 0L
                val end = start + duration.coerceAtLeast(0)
                result += PauseRecord(
                    pauseKey = "platform:$simei:$start:$end",
                    startTime = start,
                    endTime = end,
                    distanceM = distanceMeters(item.opt("distance"))
                )
                lastCursor = longValue(item.opt("time")) ?: start
            }
            if (items.length() < 1000 || lastCursor == null || lastCursor == cursor) {
                return result.distinctBy { it.pauseKey }
            }
            cursor = lastCursor
        }
        return result.distinctBy { it.pauseKey }
    }

    fun queryPlatformTrips(
        simei: String,
        timeBegin: Long,
        timeEnd: Long,
        sid: String
    ): List<PlatformTripRecord> {
        val result = mutableListOf<PlatformTripRecord>()
        var cursor: Long? = null
        repeat(MAX_PAGES) {
            val params = JSONObject()
                .put("simei", JSONArray().put(simei))
                .put("time_begin", timeBegin)
                .put("time_end", timeEnd)
                .put("limit_size", 1000)
            cursor?.let {
                params.put("last_simei", simei)
                params.put("last_start_time", it)
            }
            val json = post("location", "PDistance", params, sid)
            val items = json.optJSONArray("items") ?: return result
            var lastCursor: Long? = null
            for (index in 0 until items.length()) {
                val item = items.optJSONObject(index) ?: continue
                val start = longValue(item.opt("start_time")) ?: continue
                val duration = longValue(item.opt("cos_time")) ?: 0L
                val stop = longValue(item.opt("stop_time"))
                    ?.takeIf { it >= start }
                    ?: (start + duration.coerceAtLeast(0))
                if (stop <= start) continue
                result += PlatformTripRecord(
                    tripKey = "platform:$simei:$start:$stop",
                    startTime = start,
                    endTime = stop,
                    distanceM = distanceMeters(item.opt("distance"))
                )
                lastCursor = start
            }
            if (items.length() < 1000 || lastCursor == null || lastCursor == cursor) {
                return result.distinctBy { it.tripKey }
            }
            cursor = lastCursor
        }
        return result.distinctBy { it.tripKey }
    }

    private fun post(
        module: String,
        func: String,
        params: JSONObject,
        sid: String?
    ): JSONObject {
        val body = JSONObject()
            .put("module", module)
            .put("func", func)
            .put("params", params)
            .toString()
        val urlBuilder = BASE_URL.toHttpUrl().newBuilder()
        if (!sid.isNullOrBlank()) urlBuilder.addQueryParameter("sid", sid)
        val request = Request.Builder()
            .url(urlBuilder.build())
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36"
            )
            .header("Accept", "application/json, text/plain, */*")
            .header("Referer", "https://app.cciot.cc/")
            .post(body.toRequestBody(formType))
            .build()
        client.newCall(request).execute().use { response ->
            val text = response.body?.string().orEmpty()
            if (!response.isSuccessful) {
                throw BiotApiException(response.code.toLong(), "HTTP ${response.code}")
            }
            val json = runCatching { JSONObject(text) }.getOrElse {
                throw BiotApiException(-1, "接口返回不是 JSON")
            }
            val code = json.optLong("errcode", -1)
            if (code != 0L) {
                throw BiotApiException(code, json.optString("error_message", "接口错误"))
            }
            return json
        }
    }

    private fun parseVoltage(value: String): Double? {
        if (value.isBlank()) return null
        return Regex("""[-+]?\d+(?:\.\d+)?""")
            .find(value)
            ?.value
            ?.toDoubleOrNull()
            ?.let(::normalizeVoltage)
    }

    /** 兼容 60.04、6004、600 三种平台上报格式。 */
    private fun normalizeVoltage(raw: Double?): Double? = when {
        raw == null -> null
        raw in 10.0..120.0 -> raw
        raw in 100.0..1_200.0 -> raw / 10.0
        raw in 1_000.0..12_000.0 -> raw / 100.0
        else -> null
    }

    private fun parseLastPos(value: String): JSONObject? =
        if (value.isBlank()) null else runCatching { JSONObject(value) }.getOrNull()

    private fun normalizeDayDistance(raw: Double): Double =
        if (raw > 1000.0) raw / 1000.0 else raw

    private fun parseDayKey(value: Any?): String? = when (value) {
        is Number -> value.toLong().takeIf { it > 0 }?.let(TimeKeys::dateKey)
        is String -> {
            val text = value.trim()
            when {
                text.matches(Regex("""\d{4}-\d{2}-\d{2}""")) -> text
                text.matches(Regex("""\d{8}""")) ->
                    "${text.substring(0, 4)}-${text.substring(4, 6)}-${text.substring(6, 8)}"
                text.toLongOrNull() != null -> TimeKeys.dateKey(text.toLong())
                else -> null
            }
        }
        else -> null
    }

    private fun coordinate(value: Any?): Double? {
        val raw = doubleValue(value) ?: return null
        val normalized = if (kotlin.math.abs(raw) > 1000.0) raw / 1_000_000.0 else raw
        return normalized.takeIf { it != 0.0 }
    }

    private fun doubleValue(value: Any?): Double? = when (value) {
        is Number -> value.toDouble()
        is String -> value.trim().toDoubleOrNull()
        else -> null
    }

    private fun longValue(value: Any?): Long? = when (value) {
        is Number -> value.toLong()
        is String -> value.trim().toLongOrNull()
        else -> null
    }

    private fun distanceMeters(value: Any?): Double {
        val raw = doubleValue(value) ?: return 0.0
        return raw.coerceIn(0.0, 2_000_000.0)
    }

    private fun gpsAltitudeUsable(ptype: String?): Boolean {
        val type = ptype?.lowercase().orEmpty()
        if (type.isBlank()) return true
        return listOf("gps", "gnss", "beidou", "location").any { type.contains(it) } &&
            listOf("base", "lbs", "wifi", "cell").none { type.contains(it) }
    }

    private fun md5(value: String): String =
        MessageDigest.getInstance("MD5")
            .digest(value.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }

    companion object {
        const val BASE_URL = "https://app.cciot.cc/mapi"
        const val SESSION_EXPIRED = 268697631L
        const val RATE_LIMITED = 268697629L
        private const val MAX_PAGES = 50
    }
}
