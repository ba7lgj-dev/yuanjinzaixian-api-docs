package org.ba7lgj.yuanjinzaixian.domain

import kotlin.math.max

object DistanceAccounting {
    /**
     * 计算锚点以来新增的日里程。
     *
     * - 同一天：使用正的日里程增量；单次超过 50km 视为平台跳变并丢弃。
     * - 跨天：把新一天已经发生的日里程计入，避免午夜的累计缺口。
     */
    fun addedKm(
        lastDayKey: String?,
        lastDayKm: Double?,
        dayKey: String,
        dayKm: Double
    ): Double = when {
        lastDayKey == null || lastDayKm == null -> 0.0
        lastDayKey == dayKey && dayKm >= lastDayKm ->
            (dayKm - lastDayKm).takeIf { it <= 50.0 } ?: 0.0
        lastDayKey != dayKey -> dayKm.coerceIn(0.0, 100.0)
        else -> 0.0
    }
}

object TrainingRules {
    data class Input(
        val startIsTrainingAnchor: Boolean,
        val endIsTrainingAnchor: Boolean,
        val chargingSuspected: Boolean,
        val segmentCount: Int,
        val unlabelledSegments: Int,
        val coveredKm: Double,
        val totalKm: Double,
        val startSoc: Int,
        val endSoc: Int,
        val durationSeconds: Long
    )

    const val ELIGIBLE = "可进入训练"

    fun reason(input: Input): String = when {
        !input.startIsTrainingAnchor -> "起点不是久静置训练锚点"
        !input.endIsTrainingAnchor -> "终点不是久静置训练锚点"
        input.chargingSuspected -> "区间内疑似充电"
        input.segmentCount == 0 -> "缺少骑行区间"
        input.unlabelledSegments > 0 -> "还有骑行区间未标注人数"
        input.coveredKm < input.totalKm * 0.95 -> "标注区间覆盖不足 95%"
        input.startSoc <= input.endSoc -> "起止 SOC 未下降"
        input.totalKm !in 0.1..200.0 -> "行程距离异常"
        input.durationSeconds > 48 * 60 * 60 -> "周期超过 48 小时"
        else -> ELIGIBLE
    }

    fun coveredRatio(coveredKm: Double, totalKm: Double): Double =
        if (totalKm <= 0.0) 0.0 else (coveredKm / totalKm).coerceIn(0.0, 1.0)

    fun rangeKm(anchorSoc: Double, socPerKm: Double): Double =
        if (socPerKm <= 0.0) 0.0 else max(0.0, anchorSoc / socPerKm)
}
