package org.ba7lgj.yuanjinzaixian.ui.navigation

import org.ba7lgj.yuanjinzaixian.ui.SessionStage
import java.net.URLEncoder

object AppRoutes {
    const val LOGIN = "login"
    const val DEVICES = "devices"
    const val HOME = "home"
    const val HISTORY = "history"
    const val SETTINGS = "settings"
    const val DIAGNOSTICS = "diagnostics"
    const val CYCLE_ARG = "cycleKey"
    const val CYCLE_PATTERN = "cycle/{$CYCLE_ARG}"

    /** cycleKey 含冒号，路径参数必须编码。 */
    fun cycle(cycleKey: String): String =
        "cycle/" + URLEncoder.encode(cycleKey, "UTF-8")
}

enum class AppTab(val route: String, val label: String) {
    HOME(AppRoutes.HOME, "首页"),
    HISTORY(AppRoutes.HISTORY, "历史"),
    SETTINGS(AppRoutes.SETTINGS, "设置")
}

enum class BackAction {
    /** 首页双击退出，登录/设备选择直接退出。 */
    EXIT_APP,

    /** 历史或设置返回首页。 */
    GO_HOME,

    /** 详情页与诊断页弹出当前页。 */
    POP_BACK_STACK
}

object NavigationRules {
    fun isTab(route: String?): Boolean = AppTab.entries.any { it.route == route }

    fun tabFor(route: String?): AppTab? = AppTab.entries.firstOrNull { it.route == route }

    fun isCycleDetail(route: String?): Boolean =
        route != null && route.startsWith("cycle/")

    fun startRoute(stage: SessionStage): String = when (stage) {
        SessionStage.LOADING -> AppRoutes.LOGIN
        SessionStage.LOGIN -> AppRoutes.LOGIN
        SessionStage.CHOOSE_DEVICE -> AppRoutes.DEVICES
        SessionStage.READY -> AppRoutes.HOME
    }

    fun titleFor(route: String?): String = when {
        route == AppRoutes.HOME -> "远近电量"
        route == AppRoutes.HISTORY -> "历史记录"
        route == AppRoutes.SETTINGS -> "设置"
        route == AppRoutes.DIAGNOSTICS -> "诊断"
        route == AppRoutes.LOGIN -> "登录"
        route == AppRoutes.DEVICES -> "选择设备"
        isCycleDetail(route) -> "行程详情"
        else -> "远近电量"
    }

    fun backAction(route: String?): BackAction = when {
        route == AppRoutes.HOME -> BackAction.EXIT_APP
        route == AppRoutes.LOGIN || route == AppRoutes.DEVICES -> BackAction.EXIT_APP
        route == AppRoutes.HISTORY || route == AppRoutes.SETTINGS -> BackAction.GO_HOME
        isCycleDetail(route) || route == AppRoutes.DIAGNOSTICS -> BackAction.POP_BACK_STACK
        else -> BackAction.POP_BACK_STACK
    }
}
