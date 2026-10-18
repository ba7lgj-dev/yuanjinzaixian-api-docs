package org.ba7lgj.yuanjinzaixian

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.ba7lgj.yuanjinzaixian.data.VoltageSampleEntity
import org.ba7lgj.yuanjinzaixian.domain.BatteryState
import org.ba7lgj.yuanjinzaixian.domain.BatteryStatus
import org.ba7lgj.yuanjinzaixian.domain.Confidence
import org.ba7lgj.yuanjinzaixian.ui.AppScreen
import org.ba7lgj.yuanjinzaixian.ui.MainViewModel
import org.ba7lgj.yuanjinzaixian.ui.UiState
import org.ba7lgj.yuanjinzaixian.ui.theme.YuanjinzaixianTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val notificationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(ui.screen) {
        if (
            ui.screen == AppScreen.HOME &&
            !askedNotification &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            askedNotification = true
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(ui.screen, ui.deviceLabel) {
        if (ui.screen == AppScreen.HOME && ui.deviceLabel != null) {
            viewModel.refresh(silent = true)
            while (isActive) {
                delay(60_000)
                viewModel.refresh(silent = true)
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (ui.screen) {
                AppScreen.LOGIN -> LoginScreen(ui, viewModel)
                AppScreen.DEVICES -> DeviceScreen(ui, viewModel)
                AppScreen.HOME -> HomeScreen(ui, state, viewModel)
                AppScreen.SETTINGS -> SettingsScreen(ui, viewModel, context)
                AppScreen.DIAGNOSTICS -> DiagnosticsScreen(ui, viewModel)
            }
        }
    }
}

@Composable
private fun LoginScreen(ui: UiState, viewModel: MainViewModel) {
    var account by rememberSaveable { mutableStateOf(ui.account) }
    var password by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "远近电量",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "读取外电电压，估算 60V 铅酸电池剩余电量",
            modifier = Modifier.padding(top = 8.dp, bottom = 28.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedTextField(
            value = account,
            onValueChange = { account = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("远近在线账号") },
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("密码") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { viewModel.login(account, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !ui.loading
        ) {
            if (ui.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("登录")
            }
        }
        ui.error?.let { ErrorText(it) }
        Text(
            text = "提示：平台显示的“正在充电”可能只是定位器在充电，车辆充电判断由电压趋势完成。",
            modifier = Modifier.padding(top = 24.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DeviceScreen(ui: UiState, viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("选择设备", style = MaterialTheme.typography.headlineSmall)
        Text(
            "账号下有多台设备，请选择要显示在小组件中的一台。",
            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ui.devices.forEach { device ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(device.label, fontWeight = FontWeight.Bold)
                        Text(
                            "设备尾号 ${device.imei?.toString()?.takeLast(4) ?: "--"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(onClick = { viewModel.selectDevice(device) }) {
                        Text("选择")
                    }
                }
            }
        }
        ui.error?.let { ErrorText(it) }
    }
}

@Composable
private fun HomeScreen(ui: UiState, state: BatteryState, viewModel: MainViewModel) {
    val displaySoc = state.displaySocPercent
        ?: state.socPercent
        ?: state.instantSocPercent
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "远近电量",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    ui.deviceLabel ?: "未选择设备",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            TextButton(onClick = { viewModel.openSettings() }) {
                Text("设置")
            }
        }
        Spacer(Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = displaySoc?.let { "$it%" } ?: "--",
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = state.rangeKm?.let { "预计还能跑 $it km" } ?: "预计续航等待电压稳定",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = statusText(state),
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        InfoCard("当前外电电压", state.voltageV?.let { "%.2f V".format(it) } ?: "--")
        InfoCard("当前显示", displaySoc?.let { "$it%" } ?: "--")
        InfoCard("瞬时估算", state.instantSocPercent?.let { "$it%" } ?: "--")
        InfoCard("15分钟临时", state.provisionalSocPercent?.let { "$it%" } ?: "--")
        InfoCard("稳定估算", state.stableSocPercent?.let { "$it%" } ?: "--")
        InfoCard("置信度", confidenceText(state.confidence))
        InfoCard("静止/睡眠时长", formatDuration(state.restSeconds))
        InfoCard(
            "最后数据时间",
            state.voltageObservedAt?.let { formatTime(it) } ?: "--"
        )
        InfoCard("数据年龄", state.ageSeconds?.let { formatDuration(it) } ?: "--")
        if (state.status != BatteryStatus.STABLE && ui.chartSamples.size >= 2) {
            VoltageChartCard(ui.chartSamples, state)
        }
        if (state.fullSuggestion) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("可能已充满", fontWeight = FontWeight.Bold)
                    Text(
                        "电压已稳定在高位。请确认这是静置后的满电电压，再写入满电锚点。",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = { viewModel.markFull() }) {
                        Text("确认标记满电")
                    }
                }
            }
        }
        ui.error?.let { ErrorText(it) }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.refresh() },
                modifier = Modifier.weight(1f),
                enabled = !ui.loading
            ) {
                Text("刷新")
            }
            OutlinedButton(
                onClick = { viewModel.markFull() },
                modifier = Modifier.weight(1f)
            ) {
                Text("标记满电")
            }
        }
        OutlinedButton(
            onClick = { viewModel.markEmpty() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("标记实际空电")
        }
        Text(
            "“标记实际空电”只应在车辆已经没动力时使用；57.8V 保护线不会自动改写空电锚点。",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun VoltageChartCard(samples: List<VoltageSampleEntity>, state: BatteryState) {
    val valid = samples
        .filter { it.voltageV != null }
        .sortedBy { it.observedAt }
    if (valid.size < 2) return
    val start = state.voltageObservedAt
        ?.minus(state.restSeconds)
        ?.takeIf { it > 0 }
        ?: valid.first().observedAt
    val visible = valid.filter { it.observedAt >= start }
        .ifEmpty { valid.takeLast(12) }
    if (visible.size < 2) return
    val lineColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val end = visible.last().observedAt
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("最近静止后的电压变化", fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text(
                    visible.last().voltageV?.let { "%.2fV".format(it) } ?: "--",
                    color = lineColor,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                "从 ${formatTime(start)} 到现在 · 自动刷新",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(top = 10.dp)
            ) {
                val left = 4.dp.toPx()
                val right = size.width - 4.dp.toPx()
                val top = 4.dp.toPx()
                val bottom = size.height - 4.dp.toPx()
                val minV = visible.minOf { it.voltageV!! }
                val maxV = visible.maxOf { it.voltageV!! }
                val low = minV - 0.02
                val high = maxV + 0.02
                for (i in 0..4) {
                    val y = top + (bottom - top) * i / 4f
                    drawLine(
                        color = gridColor.copy(alpha = 0.45f),
                        start = Offset(left, y),
                        end = Offset(right, y),
                        strokeWidth = 1f
                    )
                }
                val span = (end - start).coerceAtLeast(1)
                val points = visible.map { sample ->
                    val ratio = ((sample.observedAt - start).toFloat() / span)
                        .coerceIn(0f, 1f)
                    val x = left + (right - left) * ratio
                    val normalized = ((sample.voltageV!! - low) / (high - low))
                        .coerceIn(0.0, 1.0)
                    Offset(x, bottom - (bottom - top) * normalized.toFloat())
                }
                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    points.drop(1).forEach { lineTo(it.x, it.y) }
                }
                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(
                        width = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
                points.forEach {
                    drawCircle(color = lineColor, radius = 3.5.dp.toPx(), center = it)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    formatTime(start),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.weight(1f))
                Text(
                    formatTime(end),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SettingsScreen(ui: UiState, viewModel: MainViewModel, context: android.content.Context) {
    var protection by rememberSaveable(ui.protectionVoltage) {
        mutableStateOf(ui.protectionVoltage)
    }
    var nickname by rememberSaveable(ui.nickname) {
        mutableStateOf(ui.nickname)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(onClick = { viewModel.openHome() }) {
            Text("返回")
        }
        Text(
            "设置与标定",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))
        InfoCard("账号", ui.account.ifBlank { "--" })
        InfoCard("设备", ui.deviceLabel ?: "--")
        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("车辆昵称") },
            singleLine = true
        )
        Button(
            onClick = { viewModel.saveNickname(nickname) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("保存昵称")
        }
        OutlinedTextField(
            value = protection,
            onValueChange = { protection = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("保护电压（V）") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Button(
            onClick = { viewModel.saveProtectionVoltage(protection) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("保存保护电压")
        }
        Text(
            "默认 57.8V，约等于 20% 剩余电量保护线；可按实际电池调整。",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp)
        )
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(android.net.Uri.parse("package:${context.packageName}"))
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("打开应用设置 / 电池优化")
        }
        Text(
            "华为/荣耀建议：允许自启动、允许后台活动、关闭对本应用的电池优化。WorkManager 仍可能被系统延迟。",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
        )
        OutlinedButton(
            onClick = { viewModel.openDiagnostics() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("诊断与最近采样")
        }
        OutlinedButton(
            onClick = { viewModel.logout() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("退出登录并清除凭证")
        }
        ui.error?.let { ErrorText(it) }
    }
}

@Composable
private fun DiagnosticsScreen(ui: UiState, viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(onClick = { viewModel.openSettings() }) {
            Text("返回设置")
        }
        Text(
            "诊断与最近采样",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            "定位器电量（非电动车电池）和原始 ACC 字段仅用于排查，不参与 SOC。",
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (ui.diagnostics.isEmpty()) {
            Text("还没有采样记录。")
        } else {
            ui.diagnostics.forEach { sample ->
                DiagnosticRow(sample)
            }
        }
    }
}

@Composable
private fun DiagnosticRow(sample: VoltageSampleEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                "通讯 ${formatTime(sample.observedAt)}",
                fontWeight = FontWeight.Medium
            )
            Text(
                "外电 ${sample.voltageV?.let { "%.2fV".format(it) } ?: "--"}  " +
                    "年龄 ${formatDuration(sample.ageSeconds)}  " +
                    "静止 ${formatDuration(sample.restSeconds)}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "状态 ${sample.state ?: "--"}  cache=${sample.cacheSuspect}  " +
                    "source=${sample.timestampSource}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "定位器电量（非电动车电池） ${sample.locatorPowerPct?.let { "$it%" } ?: "--"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InfoCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.weight(1f))
            Text(value, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ErrorText(message: String) {
    Text(
        text = message,
        modifier = Modifier.padding(top = 10.dp),
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall
    )
}

private fun statusText(state: BatteryState): String = when (state.status) {
    BatteryStatus.STABLE -> "电压稳定"
    BatteryStatus.REBOUND -> "刚停车，电压反弹中"
    BatteryStatus.CHARGE_UNKNOWN -> "疑似充电/反弹，待确认"
    BatteryStatus.STALE -> "数据已过期"
    BatteryStatus.OFFLINE -> "设备离线"
    BatteryStatus.ERROR -> "请求失败"
    BatteryStatus.WAITING -> "等待可信电压"
}

private fun confidenceText(confidence: Confidence): String = when (confidence) {
    Confidence.HIGH -> "高"
    Confidence.MEDIUM -> "中"
    Confidence.LOW -> "低"
}

private fun formatTime(epochSeconds: Long): String =
    SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA).format(Date(epochSeconds * 1000))

private fun formatDuration(seconds: Long): String {
    if (seconds <= 0) return "0 分钟"
    val minutes = seconds / 60
    return when {
        minutes < 60 -> "$minutes 分钟"
        else -> "${minutes / 60} 小时 ${minutes % 60} 分钟"
    }
}
