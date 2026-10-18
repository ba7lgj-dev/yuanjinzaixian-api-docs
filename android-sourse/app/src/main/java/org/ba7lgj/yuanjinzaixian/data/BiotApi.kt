package org.ba7lgj.yuanjinzaixian.data

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
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
        val lastPosTime = parseLastPosTime(lastPos)
        val rawDayDistance = json.optDouble("day_distance", Double.NaN)
            .takeUnless { it.isNaN() }
        return DeviceDetail(
            simei = simei,
            voltageV = parseVoltage(json.optString("external_voltage")),
            lastComTime = json.optLong("last_com_time").takeIf { it > 0 },
            lastPosTime = lastPosTime,
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

    fun requestImmediateLocation(simei: String, sid: String) {
        post(
            "device",
            "Cmd",
            JSONObject()
                .put("simei", simei)
                .put("type", "e_cmd_query_location"),
            sid
        )
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
    }

    private fun parseLastPosTime(value: String): Long? {
        if (value.isBlank()) return null
        return runCatching {
            JSONObject(value).optLong("time").takeIf { it > 0 }
        }.getOrNull()
    }

    private fun normalizeDayDistance(raw: Double): Double =
        if (raw > 1000.0) raw / 1000.0 else raw

    private fun md5(value: String): String =
        MessageDigest.getInstance("MD5")
            .digest(value.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }

    companion object {
        const val BASE_URL = "https://app.cciot.cc/mapi"
        const val SESSION_EXPIRED = 268697631L
        const val RATE_LIMITED = 268697629L
    }
}
