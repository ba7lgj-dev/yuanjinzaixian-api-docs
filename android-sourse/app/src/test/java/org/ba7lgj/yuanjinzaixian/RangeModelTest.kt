package org.ba7lgj.yuanjinzaixian

import org.ba7lgj.yuanjinzaixian.domain.RangeModel
import org.ba7lgj.yuanjinzaixian.domain.RangeProfile
import org.ba7lgj.yuanjinzaixian.domain.RangeTrainingSample
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RangeModelTest {
    @Test
    fun fewerThanFiveCyclesKeepsPhysicalBaseline() {
        assertNull(RangeModel.train(samples(4)))
        val (perKm, source) = RangeModel.predictSocPerKm(
            model = null,
            peopleCount = 2,
            anchorSoc = 80.0,
            profile = RangeProfile()
        )
        assertEquals("物理基准", source)
        assertEquals(2.5 / 0.85, perKm, 0.01)
    }

    @Test
    fun fiveToNineCyclesUsesPeopleMedian() {
        val model = RangeModel.train(samples(7))
        assertNotNull(model)
        assertEquals("median", model!!.kind)
        assertEquals(7, model.sampleCount)
        val (perKm, source) = RangeModel.predictSocPerKm(
            model,
            peopleCount = 1,
            anchorSoc = 70.0,
            profile = RangeProfile()
        )
        assertEquals("本地中位数", source)
        assertTrue(perKm > 0.0)
    }

    @Test
    fun tenToTwentyNineCyclesUsesRegularizedRegression() {
        val model = RangeModel.train(samples(14))
        assertNotNull(model)
        assertEquals("regression", model!!.kind)
        assertEquals(8, model.coefficients.size)
    }

    @Test
    fun thirtyCyclesProducesSocBandModels() {
        val model = RangeModel.train(samples(32))
        assertNotNull(model)
        assertEquals("band_regression", model!!.kind)
        assertTrue(model.bandModels.isNotEmpty())
    }

    @Test
    fun encodeDecodeRoundTripKeepsNegativeRegenCoefficient() {
        val model = RangeModel.train(samples(32))!!.copy(
            coefficients = doubleArrayOf(1.0, -0.5, 0.25, 0.0, 0.1, 0.2, -0.3, 0.4)
        )
        val decoded = RangeModel.decode(RangeModel.encode(model))!!
        assertEquals(model.kind, decoded.kind)
        assertEquals(model.sampleCount, decoded.sampleCount)
        assertEquals(-0.5, decoded.coefficients[1], 1e-6)
        assertEquals(model.maeSocPerKm, decoded.maeSocPerKm, 1e-6)
    }

    @Test
    fun newModelIsRejectedWhenErrorIsClearlyWorse() {
        val good = RangeModel.train(samples(14))!!.copy(maeSocPerKm = 0.20)
        val bad = good.copy(maeSocPerKm = 0.40)
        assertTrue(RangeModel.betterThan(good, null))
        assertTrue(!RangeModel.betterThan(bad, good))
        assertTrue(RangeModel.betterThan(good, good))
    }

    private fun samples(count: Int): List<RangeTrainingSample> {
        val bands = listOf(20.0, 50.0, 70.0, 90.0)
        return (0 until count).map { index ->
            val startSoc = bands[index % bands.size]
            val people = index % 2 + 1
            RangeTrainingSample(
                socDropPerKm = 2.0 + people * 0.3 + (90.0 - startSoc) / 100.0 +
                    index % 3 * 0.02,
                peopleCount = people,
                startSoc = startSoc,
                profile = RangeProfile(
                    avgSpeedKmh = 15.0 + index % 10,
                    ascentPerKm = (index % 5).toDouble(),
                    descentPerKm = (index % 4).toDouble(),
                    downhillRatio = (index % 5) / 10.0,
                    positiveAccel = 0.3 + index % 3 * 0.1,
                    negativeAccel = 0.3 + index % 2 * 0.1,
                    regenProxyPerKm = (index % 4) * 0.2
                )
            )
        }
    }
}
