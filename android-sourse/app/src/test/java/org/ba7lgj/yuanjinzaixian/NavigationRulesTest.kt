package org.ba7lgj.yuanjinzaixian

import org.ba7lgj.yuanjinzaixian.ui.SessionStage
import org.ba7lgj.yuanjinzaixian.ui.navigation.AppRoutes
import org.ba7lgj.yuanjinzaixian.ui.navigation.AppTab
import org.ba7lgj.yuanjinzaixian.ui.navigation.BackAction
import org.ba7lgj.yuanjinzaixian.ui.navigation.NavigationRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NavigationRulesTest {
    @Test
    fun startRouteFollowsSessionStage() {
        assertEquals(AppRoutes.LOGIN, NavigationRules.startRoute(SessionStage.LOADING))
        assertEquals(AppRoutes.LOGIN, NavigationRules.startRoute(SessionStage.LOGIN))
        assertEquals(AppRoutes.DEVICES, NavigationRules.startRoute(SessionStage.CHOOSE_DEVICE))
        assertEquals(AppRoutes.HOME, NavigationRules.startRoute(SessionStage.READY))
    }

    @Test
    fun onlyThreePrimaryRoutesShowBottomNavigation() {
        assertEquals(3, AppTab.entries.size)
        assertTrue(NavigationRules.isTab(AppRoutes.HOME))
        assertTrue(NavigationRules.isTab(AppRoutes.HISTORY))
        assertTrue(NavigationRules.isTab(AppRoutes.SETTINGS))
        assertFalse(NavigationRules.isTab(AppRoutes.DIAGNOSTICS))
        assertFalse(NavigationRules.isTab(AppRoutes.DEVICES))
        assertFalse(NavigationRules.isTab(AppRoutes.cycle("auto:1:2:3")))
    }

    @Test
    fun backActionsMatchHierarchy() {
        assertEquals(BackAction.EXIT_APP, NavigationRules.backAction(AppRoutes.HOME))
        assertEquals(BackAction.EXIT_APP, NavigationRules.backAction(AppRoutes.LOGIN))
        assertEquals(BackAction.GO_HOME, NavigationRules.backAction(AppRoutes.HISTORY))
        assertEquals(BackAction.GO_HOME, NavigationRules.backAction(AppRoutes.SETTINGS))
        assertEquals(BackAction.POP_BACK_STACK, NavigationRules.backAction(AppRoutes.DIAGNOSTICS))
        assertEquals(
            BackAction.POP_BACK_STACK,
            NavigationRules.backAction(AppRoutes.cycle("auto:1:2:3"))
        )
    }

    @Test
    fun cycleRouteEncodesColonInKeys() {
        val route = AppRoutes.cycle("auto:8986:live:1:live:2")
        assertTrue(route.startsWith("cycle/"))
        assertFalse(route.substringAfter("cycle/").contains(":"))
        assertTrue(route.contains("%3A"))
    }

    @Test
    fun titlesAreHumanReadable() {
        assertEquals("远近电量", NavigationRules.titleFor(AppRoutes.HOME))
        assertEquals("历史记录", NavigationRules.titleFor(AppRoutes.HISTORY))
        assertEquals("设置", NavigationRules.titleFor(AppRoutes.SETTINGS))
        assertEquals("诊断", NavigationRules.titleFor(AppRoutes.DIAGNOSTICS))
        assertEquals("行程详情", NavigationRules.titleFor(AppRoutes.cycle("auto:1")))
    }
}
