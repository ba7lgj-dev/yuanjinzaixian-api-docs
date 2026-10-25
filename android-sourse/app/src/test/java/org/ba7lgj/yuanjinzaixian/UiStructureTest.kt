package org.ba7lgj.yuanjinzaixian

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 无设备环境下的结构回归：确认页面层级与按钮文案没有被改回旧的平铺形式。
 * 这些断言只读取源码，不依赖 Android 运行时。
 */
class UiStructureTest {
    private val screens = "src/main/java/org/ba7lgj/yuanjinzaixian/ui/screens"

    @Test
    fun historyListKeepsModelActionsInSettings() {
        val history = source("$screens/HistoryScreen.kt")
        assertFalse(history.contains("retrainModel"))
        assertFalse(history.contains("rollbackModel"))
        assertFalse(history.contains("resetModelToBaseline"))
        assertFalse(history.contains("syncHistory"))
        assertTrue(history.contains("点击查看详情并标注人数"))
        assertTrue(history.contains("超过 7 天只保留月汇总"))
    }

    @Test
    fun settingsRowsAlwaysCarryDescriptions() {
        val settings = source("$screens/SettingsScreen.kt")
        val actionRows = Regex("ActionRow\\(").findAll(settings).count()
        val dangerRows = Regex("DangerActionRow\\(").findAll(settings).count()
        val descriptions = Regex("description =").findAll(settings).count()
        assertTrue(actionRows >= 8)
        assertTrue(dangerRows >= 3)
        assertTrue(descriptions >= actionRows + dangerRows)
    }

    @Test
    fun settingsNoLongerExposesProtectionVoltage() {
        val settings = source("$screens/SettingsScreen.kt")
        assertFalse(settings.contains("保护电压"))
    }

    @Test
    fun tripDetailOnlyEditsPeopleCount() {
        val detail = source("$screens/TripDetailScreen.kt")
        assertTrue(detail.contains("PeopleRules.options()"))
        assertFalse(detail.contains("拆分"))
        assertFalse(detail.contains("合并"))
        assertFalse(detail.contains("+5"))
    }

    @Test
    fun mainActivityUsesNavigationComposeInsteadOfScreenEnum() {
        val activity = source("src/main/java/org/ba7lgj/yuanjinzaixian/MainActivity.kt")
        assertTrue(activity.contains("NavHost"))
        assertTrue(activity.contains("AppRoutes.CYCLE_PATTERN"))
        assertFalse(activity.contains("AppScreen"))
    }

    private fun source(relative: String): String {
        val candidates = listOf(
            File(relative),
            File("app/$relative"),
            File("../app/$relative")
        )
        return candidates.firstOrNull { it.isFile }?.readText()
            ?: error("找不到源码文件: $relative")
    }
}
