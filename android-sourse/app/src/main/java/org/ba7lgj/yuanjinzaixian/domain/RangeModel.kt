package org.ba7lgj.yuanjinzaixian.domain

import java.util.Locale
import kotlin.math.abs
import kotlin.math.pow

object RangeModel {
    private const val FEATURE_COUNT = 8
    private const val RIDGE_LAMBDA = 6.0
    private const val MIN_GLOBAL_SAMPLES = 10
    private const val MIN_BAND_SAMPLES = 30

    fun bandForSoc(soc: Double): String = when {
        soc >= 80.0 -> "80-100"
        soc >= 60.0 -> "60-80"
        soc >= 40.0 -> "40-60"
        else -> "0-40"
    }

    fun train(samples: List<RangeTrainingSample>): TrainedRangeModel? {
        if (samples.size < 5) return null
        if (samples.size < MIN_GLOBAL_SAMPLES) {
            val medians = mutableMapOf<String, Double>()
            groupMedians(samples).forEach { (key, values) ->
                median(values)?.let { medians[key] = it }
            }
            return TrainedRangeModel(
                kind = "median",
                sampleCount = samples.size,
                maeSocPerKm = medianAbsoluteError(samples, medians),
                medians = medians
            )
        }

        val global = ridgeFit(samples)
        val mae = crossValidatedMae(samples) { subset -> ridgeFit(subset) }
        if (samples.size < MIN_BAND_SAMPLES) {
            return TrainedRangeModel(
                kind = "regression",
                sampleCount = samples.size,
                maeSocPerKm = mae,
                intercept = global.first,
                coefficients = global.second
            )
        }

        val bandModels = mutableMapOf<String, BandLinearModel>()
        for (band in listOf("0-40", "40-60", "60-80", "80-100")) {
            val subset = samples.filter { bandForSoc(it.startSoc) == band }
            if (subset.size < 4) continue
            val fitted = ridgeFit(subset)
            bandModels[band] = BandLinearModel(fitted.first, fitted.second)
        }
        return TrainedRangeModel(
            kind = "band_regression",
            sampleCount = samples.size,
            maeSocPerKm = mae,
            intercept = global.first,
            coefficients = global.second,
            bandModels = bandModels
        )
    }

    fun predictSocPerKm(
        model: TrainedRangeModel?,
        peopleCount: Int,
        anchorSoc: Double,
        profile: RangeProfile
    ): Pair<Double, String> {
        val people = PeopleRules.clamp(peopleCount)
        if (model == null) {
            val baseline = 2.5 / (0.85.pow((people - 1).coerceAtLeast(0)))
            return baseline.coerceIn(0.5, 8.0) to "物理基准"
        }
        if (model.kind == "median") {
            val band = bandForSoc(anchorSoc)
            val value = model.medians["$people:$band"]
                ?: model.medians["1:$band"]
                ?: model.medians["all"]
                ?: 2.5
            return value.coerceIn(0.5, 8.0) to "本地中位数"
        }
        val vector = featureVector(people, anchorSoc, profile)
        val bandModel = model.bandModels[bandForSoc(anchorSoc)]
        val intercept = bandModel?.intercept ?: model.intercept
        val coefficients = bandModel?.coefficients ?: model.coefficients
        val predicted = if (coefficients.isEmpty()) {
            2.5
        } else {
            intercept + vector.indices.sumOf { index ->
                coefficients.getOrElse(index) { 0.0 } * vector[index]
            }
        }
        return predicted.coerceIn(0.5, 8.0) to
            if (model.kind == "band_regression") "本地分区间回归" else "本地回归"
    }

    fun predictRange(
        model: TrainedRangeModel?,
        anchorSoc: Double,
        peopleCount: Int,
        profile: RangeProfile,
        modelId: Long? = null
    ): RangePrediction {
        val (socPerKm, source) = predictSocPerKm(model, peopleCount, anchorSoc, profile)
        val range = (anchorSoc / socPerKm).coerceIn(0.0, 200.0)
        return RangePrediction(range, socPerKm, source, modelId)
    }

    fun encode(model: TrainedRangeModel): String {
        val coefficients = model.coefficients.joinToString(",") { number(it) }
        val medians = model.medians.entries.joinToString("|") { "${it.key}=${number(it.value)}" }
        val bands = model.bandModels.entries.joinToString("|") {
            "${it.key}#${number(it.value.intercept)}#" +
                it.value.coefficients.joinToString(",") { value -> number(value) }
        }
        return listOf(
            "v1",
            model.kind,
            model.sampleCount.toString(),
            number(model.maeSocPerKm),
            number(model.intercept),
            coefficients,
            medians,
            bands,
            model.featureVersion.toString()
        ).joinToString(";")
    }

    fun decode(value: String?): TrainedRangeModel? {
        if (value.isNullOrBlank()) return null
        val parts = value.split(';')
        if (parts.size < 9 || parts[0] != "v1") return null
        return runCatching {
            TrainedRangeModel(
                kind = parts[1],
                sampleCount = parts[2].toInt(),
                maeSocPerKm = parts[3].toDouble(),
                intercept = parts[4].toDouble(),
                coefficients = numbers(parts[5]),
                medians = parts[6].split('|')
                    .filter { it.isNotBlank() }
                    .associate {
                        val pair = it.split('=', limit = 2)
                        pair[0] to pair[1].toDouble()
                    },
                bandModels = parts[7].split('|')
                    .filter { it.isNotBlank() }
                    .associate {
                        val values = it.split('#')
                        values[0] to BandLinearModel(values[1].toDouble(), numbers(values[2]))
                    },
                featureVersion = parts[8].toInt()
            )
        }.getOrNull()
    }

    fun betterThan(candidate: TrainedRangeModel, current: TrainedRangeModel?): Boolean =
        current == null || candidate.maeSocPerKm <= current.maeSocPerKm * 1.02 + 0.001

    private fun featureVector(
        peopleCount: Int,
        anchorSoc: Double,
        profile: RangeProfile
    ): DoubleArray = doubleArrayOf(
        PeopleRules.clamp(peopleCount).toDouble(),
        profile.avgSpeedKmh,
        profile.ascentPerKm,
        profile.descentPerKm,
        profile.downhillRatio,
        profile.positiveAccel,
        profile.negativeAccel,
        profile.regenProxyPerKm
    )

    private fun trainingVector(sample: RangeTrainingSample): DoubleArray = featureVector(
        sample.peopleCount,
        sample.startSoc,
        sample.profile
    )

    private fun ridgeFit(samples: List<RangeTrainingSample>): Pair<Double, DoubleArray> {
        if (samples.isEmpty()) return 0.0 to DoubleArray(FEATURE_COUNT)
        val x = samples.map(::trainingVector)
        val y = samples.map { it.socDropPerKm }
        val means = DoubleArray(FEATURE_COUNT) { index -> x.map { it[index] }.average() }
        val stds = DoubleArray(FEATURE_COUNT) { index ->
            val variance = x.sumOf { (it[index] - means[index]).pow(2) } / x.size
            sqrtSafe(variance).coerceAtLeast(1e-6)
        }
        val design = Array(x.size) { row ->
            DoubleArray(FEATURE_COUNT + 1) { column ->
                if (column == 0) 1.0 else (x[row][column - 1] - means[column - 1]) / stds[column - 1]
            }
        }
        val xtx = Array(FEATURE_COUNT + 1) { DoubleArray(FEATURE_COUNT + 1) }
        val xty = DoubleArray(FEATURE_COUNT + 1)
        for (row in design.indices) {
            for (i in 0..FEATURE_COUNT) {
                xty[i] += design[row][i] * y[row]
                for (j in 0..FEATURE_COUNT) xtx[i][j] += design[row][i] * design[row][j]
            }
        }
        for (i in 1..FEATURE_COUNT) xtx[i][i] += RIDGE_LAMBDA
        val standardized = solve(xtx, xty)
            ?: return y.average() to DoubleArray(FEATURE_COUNT)
        val raw = DoubleArray(FEATURE_COUNT) {
            standardized[it + 1] / stds[it]
        }
        val intercept = standardized[0] - means.indices.sumOf { means[it] * raw[it] }
        return intercept to raw
    }

    private fun solve(matrix: Array<DoubleArray>, vector: DoubleArray): DoubleArray? {
        val n = vector.size
        val a = Array(n) { matrix[it].copyOf() }
        val b = vector.copyOf()
        for (column in 0 until n) {
            var pivot = column
            for (row in column + 1 until n) {
                if (abs(a[row][column]) > abs(a[pivot][column])) pivot = row
            }
            if (abs(a[pivot][column]) < 1e-9) return null
            if (pivot != column) {
                val tmp = a[pivot]
                a[pivot] = a[column]
                a[column] = tmp
                val value = b[pivot]
                b[pivot] = b[column]
                b[column] = value
            }
            val divisor = a[column][column]
            for (j in column until n) a[column][j] /= divisor
            b[column] /= divisor
            for (row in 0 until n) {
                if (row == column) continue
                val factor = a[row][column]
                if (abs(factor) < 1e-12) continue
                for (j in column until n) a[row][j] -= factor * a[column][j]
                b[row] -= factor * b[column]
            }
        }
        return b
    }

    private fun crossValidatedMae(
        samples: List<RangeTrainingSample>,
        fit: (List<RangeTrainingSample>) -> Pair<Double, DoubleArray>
    ): Double {
        if (samples.size < 4) return Double.MAX_VALUE
        val errors = mutableListOf<Double>()
        for (index in samples.indices) {
            val training = samples.filterIndexed { itemIndex, _ -> itemIndex != index }
            val (intercept, coefficients) = fit(training)
            val vector = trainingVector(samples[index])
            val predicted = intercept + vector.indices.sumOf {
                coefficients.getOrElse(it) { 0.0 } * vector[it]
            }
            errors += abs(predicted.coerceIn(0.5, 8.0) - samples[index].socDropPerKm)
        }
        return errors.average()
    }

    private fun groupMedians(samples: List<RangeTrainingSample>): Map<String, List<Double>> {
        val result = mutableMapOf<String, MutableList<Double>>()
        samples.forEach { sample ->
            val band = bandForSoc(sample.startSoc)
            result.getOrPut("${sample.peopleCount}:$band") { mutableListOf() }
                .add(sample.socDropPerKm)
            result.getOrPut("1:$band") { mutableListOf() }.add(sample.socDropPerKm)
            result.getOrPut("${sample.peopleCount}:all") { mutableListOf() }
                .add(sample.socDropPerKm)
            result.getOrPut("all") { mutableListOf() }.add(sample.socDropPerKm)
        }
        return result
    }

    private fun medianAbsoluteError(
        samples: List<RangeTrainingSample>,
        medians: Map<String, Double>
    ): Double = samples.map { sample ->
        val predicted = medians["${sample.peopleCount}:${bandForSoc(sample.startSoc)}"]
            ?: medians["1:${bandForSoc(sample.startSoc)}"]
            ?: medians["all"]
            ?: 2.5
        abs(predicted - sample.socDropPerKm)
    }.average()

    private fun median(values: List<Double>): Double? {
        if (values.isEmpty()) return null
        val sorted = values.sorted()
        val middle = sorted.size / 2
        return if (sorted.size % 2 == 1) sorted[middle]
        else (sorted[middle - 1] + sorted[middle]) / 2.0
    }

    private fun sqrtSafe(value: Double): Double =
        if (value <= 0.0) 0.0 else kotlin.math.sqrt(value)

    private fun number(value: Double): String = String.format(Locale.US, "%.8f", value)

    private fun numbers(value: String): DoubleArray =
        value.split(',').filter { it.isNotBlank() }.map { it.toDouble() }.toDoubleArray()
}
