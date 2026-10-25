package org.ba7lgj.yuanjinzaixian

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.ba7lgj.yuanjinzaixian.ui.MainViewModel
import org.ba7lgj.yuanjinzaixian.ui.NavEvent
import org.ba7lgj.yuanjinzaixian.ui.SessionStage
import org.ba7lgj.yuanjinzaixian.ui.components.AppBottomBar
import org.ba7lgj.yuanjinzaixian.ui.components.AppTopBar
import org.ba7lgj.yuanjinzaixian.ui.navigation.AppRoutes
import org.ba7lgj.yuanjinzaixian.ui.navigation.AppTab
import org.ba7lgj.yuanjinzaixian.ui.navigation.BackAction
import org.ba7lgj.yuanjinzaixian.ui.navigation.NavigationRules
import org.ba7lgj.yuanjinzaixian.ui.screens.DeviceScreen
import org.ba7lgj.yuanjinzaixian.ui.screens.DiagnosticsScreen
import org.ba7lgj.yuanjinzaixian.ui.screens.HistoryScreen
import org.ba7lgj.yuanjinzaixian.ui.screens.HomeScreen
import org.ba7lgj.yuanjinzaixian.ui.screens.LoginScreen
import org.ba7lgj.yuanjinzaixian.ui.screens.SettingsScreen
import org.ba7lgj.yuanjinzaixian.ui.screens.TripDetailScreen
import org.ba7lgj.yuanjinzaixian.ui.theme.YuanjinzaixianTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YuanjinzaixianTheme {
                BatteryApp()
            }
        }
    }
}

@Composable
private fun BatteryApp(viewModel: MainViewModel = viewModel()) {
    val ui = viewModel.ui
    val state by viewModel.battery.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var askedNotification by rememberSaveable { mutableStateOf(false) }
    var lastBackAt by remember { mutableStateOf(0L) }
    val notificationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { }

    if (ui.sessionStage == SessionStage.LOADING) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        return
    }

    val navController = rememberNavController()
    val startRoute = remember { NavigationRules.startRoute(ui.sessionStage) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    LaunchedEffect(ui.navEvent) {
        when (ui.navEvent) {
            NavEvent.TO_LOGIN -> {
                navController.navigate(AppRoutes.LOGIN) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
                viewModel.consumeNavEvent()
            }

            NavEvent.TO_DEVICES_CLEAR -> {
                navController.navigate(AppRoutes.DEVICES) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
                viewModel.consumeNavEvent()
            }

            NavEvent.TO_DEVICES_PUSH -> {
                navController.navigate(AppRoutes.DEVICES)
                viewModel.consumeNavEvent()
            }

            NavEvent.TO_HOME -> {
                navController.navigate(AppRoutes.HOME) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
                viewModel.consumeNavEvent()
            }

            null -> Unit
        }
    }

    LaunchedEffect(currentRoute) {
        if (
            currentRoute == AppRoutes.HOME &&
            !askedNotification &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askedNotification = true
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val interceptBack = currentRoute in setOf(
        AppRoutes.HOME,
        AppRoutes.LOGIN,
        AppRoutes.HISTORY,
        AppRoutes.SETTINGS
    )
    BackHandler(enabled = interceptBack) {
        when (NavigationRules.backAction(currentRoute)) {
            BackAction.EXIT_APP -> {
                if (currentRoute == AppRoutes.HOME) {
                    val now = System.currentTimeMillis()
                    if (now - lastBackAt < 2_000L) {
                        (context as? Activity)?.finish()
                    } else {
                        lastBackAt = now
                        Toast.makeText(context, "再按一次返回退出", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    (context as? Activity)?.finish()
                }
            }

            BackAction.GO_HOME -> {
                navController.navigate(AppRoutes.HOME) {
                    popUpTo(AppRoutes.HOME) { inclusive = false }
                    launchSingleTop = true
                }
            }

            BackAction.POP_BACK_STACK -> navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = NavigationRules.titleFor(currentRoute),
                onBack = when {
                    currentRoute == AppRoutes.DIAGNOSTICS ||
                        NavigationRules.isCycleDetail(currentRoute) ->
                        ({ navController.popBackStack() })

                    currentRoute == AppRoutes.DEVICES &&
                        navController.previousBackStackEntry != null ->
                        ({ navController.popBackStack() })

                    else -> null
                }
            )
        },
        bottomBar = {
            if (NavigationRules.isTab(currentRoute)) {
                AppBottomBar(currentRoute) { tab -> navigateTab(navController, currentRoute, tab) }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoutes.LOGIN) {
                LoginScreen(ui = ui, onLogin = viewModel::login)
            }
            composable(AppRoutes.DEVICES) {
                DeviceScreen(
                    ui = ui,
                    onSelect = viewModel::selectDevice,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(AppRoutes.HOME) {
                HomeScreen(
                    ui = ui,
                    state = state,
                    onPeopleSelected = viewModel::setPeople
                )
            }
            composable(AppRoutes.HISTORY) {
                HistoryScreen(
                    ui = ui,
                    onOpenCycle = { key -> navController.navigate(AppRoutes.cycle(key)) },
                    onRefresh = viewModel::refreshHistory
                )
            }
            composable(AppRoutes.SETTINGS) {
                SettingsScreen(
                    ui = ui,
                    state = state,
                    viewModel = viewModel,
                    onOpenDiagnostics = { navController.navigate(AppRoutes.DIAGNOSTICS) }
                )
            }
            composable(AppRoutes.DIAGNOSTICS) {
                DiagnosticsScreen(ui = ui, onLoad = viewModel::loadDiagnostics)
            }
            composable(
                route = AppRoutes.CYCLE_PATTERN,
                arguments = listOf(navArgument(AppRoutes.CYCLE_ARG) { type = NavType.StringType })
            ) { entry ->
                val rawKey = entry.arguments?.getString(AppRoutes.CYCLE_ARG)
                val decodedKey = rawKey?.let { runCatching { Uri.decode(it) }.getOrNull() } ?: rawKey
                val cycle = ui.history?.recentDays
                    ?.asSequence()
                    ?.flatMap { it.cycles.asSequence() }
                    ?.firstOrNull { it.cycleKey == rawKey || it.cycleKey == decodedKey }
                TripDetailScreen(
                    cycle = cycle,
                    onPeopleSelected = viewModel::setSegmentPeople
                )
            }
        }
    }
}

private fun navigateTab(
    navController: androidx.navigation.NavHostController,
    currentRoute: String?,
    tab: AppTab
) {
    if (currentRoute == tab.route) return
    navController.navigate(tab.route) {
        popUpTo(AppRoutes.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
