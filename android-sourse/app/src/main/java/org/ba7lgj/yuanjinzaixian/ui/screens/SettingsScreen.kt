package org.ba7lgj.yuanjinzaixian.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import org.ba7lgj.yuanjinzaixian.domain.BatteryState
import org.ba7lgj.yuanjinzaixian.domain.PeopleRules
import org.ba7lgj.yuanjinzaixian.ui.MainViewModel
import org.ba7lgj.yuanjinzaixian.ui.UiState
import org.ba7lgj.yuanjinzaixian.ui.components.ActionRow
import org.ba7lgj.yuanjinzaixian.ui.components.Caption
import org.ba7lgj.yuanjinzaixian.ui.components.DangerActionRow
import org.ba7lgj.yuanjinzaixian.ui.components.ErrorText
import org.ba7lgj.yuanjinzaixian.ui.components.InfoRow
import org.ba7lgj.yuanjinzaixian.ui.components.PeopleChooser
import org.ba7lgj.yuanjinzaixian.ui.components.SettingsGroup

@Composable
fun SettingsScreen(
    ui: UiState,
    state: BatteryState,
    viewModel: MainViewModel,
    onOpenDiagnostics: () -> Unit
) {
    val context = LocalContext.current
    var nickname by rememberSaveable(ui.nickname) { mutableStateOf(ui.nickname) }
    var fullVoltage by rememberSaveable(ui.fullVoltage) { mutableStateOf(ui.fullVoltage) }
    var emptyVoltage by rememberSaveable(ui.emptyVoltage) { mutableStateOf(ui.emptyVoltage) }
    var notificationsGranted by remember { mutableStateOf(context.hasNotificationPermission()) }
    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> notificationsGranted = granted }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        SettingsGroup(title = "账号与车辆") {
            InfoRow(label = "账号", value = ui.account.ifBlank { "--" })
            InfoRow(
                label = "当前车辆",
                value = ui.deviceLabel ?: "--",
                description = "首页、桌面小组件与历史记录都按这台车显示。"
            )
            ActionRow(
                title = "切换设备",
                description = "重新读取账号下的车辆列表，选择另一台车。",
                onClick = { viewModel.requestDeviceSwitch() },
                enabled = !ui.loading
            )
            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                label = { Text("车辆昵称") },
                singleLine = true
            )
            Button(
                onClick = { viewModel.saveNickname(nickname) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("保存昵称")
            }
            Caption(
                text = "昵称显示在首页和桌面小组件；留空时回退到平台备注或“我的车”。",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        SettingsGroup(title = "电量标定") {
            Caption(
                text = "默认静置满电 65.5V、实际空电 55.0V。保存后会立即重算电量与续航。",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = fullVoltage,
                    onValueChange = { fullVoltage = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("满电电压 V") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                OutlinedTextField(
                    value = emptyVoltage,
                    onValueChange = { emptyVoltage = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("空电电压 V") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
            }
            Button(
                onClick = { viewModel.saveCalibrationAnchors(fullVoltage, emptyVoltage) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("保存满/空电压")
            }
            ActionRow(
                title = "标记充满",
                description = "记录一次充满；需要等待电压稳定后才写入满电锚点。",
                onClick = { viewModel.markFull() },
                trailing = when {
                    state.fullRequestPending -> "等待稳定"
                    state.fullRequestExpired -> "已失效"
                    else -> null
                },
                showChevron = false
            )
            ActionRow(
                title = "标记实际空电",
                description = "车辆已经没动力时使用；当前电压立即作为 0% 锚点。",
                onClick = { viewModel.markEmpty() },
                showChevron = false
            )
            DangerActionRow(
                title = "重置标定",
                description = "恢复默认满电 65.5V / 空电 55.0V，并清除待定的满电请求。",
                confirmTitle = "重置满/空标定？",
                confirmMessage = "会清除手动与自动标定，恢复默认 65.5V / 55.0V。历史行程和电压记录不受影响。",
                confirmLabel = "重置",
                onConfirm = { viewModel.resetCalibration() }
            )
        }

        SettingsGroup(title = "载人设置") {
            Column(Modifier.padding(16.dp)) {
                PeopleChooser(
                    current = PeopleRules.clamp(state.currentPeopleCount),
                    onSelect = { viewModel.setPeople(it) }
                )
            }
            Caption(
                text = "最多两人（骑行者 + 1 名乘客）。首页与设置页共用同一人数，切换后立即重算续航。",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        SettingsGroup(title = "历史与模型") {
            ActionRow(
                title = "同步历史",
                description = if (ui.historySyncing) {
                    "正在同步，请保持网络连接。"
                } else {
                    "首次同步最近两年，之后只增量同步最近两天。"
                },
                onClick = { viewModel.syncHistory() },
                enabled = !ui.historySyncing,
                showChevron = false
            )
            InfoRow(
                label = "训练状态",
                value = ui.training.modelLabel,
                description = "周期 ${ui.training.cycleCount} 个 · 可训练 ${ui.training.trainingCycleCount} 个 · " +
                    "样本 ${ui.training.sampleCount} 个" +
                    (ui.training.maeSocPerKm?.let { " · 误差 %.2f%%/km".format(it) } ?: "")
            )
            ActionRow(
                title = "重新训练",
                description = "用已标注完人数的周期重新训练本机模型。",
                onClick = { viewModel.retrainModel() },
                showChevron = false
            )
            ActionRow(
                title = "回滚上一版",
                description = "恢复上一个模型版本，用于对比训练效果。",
                onClick = { viewModel.rollbackModel() },
                showChevron = false
            )
            DangerActionRow(
                title = "恢复物理基准",
                description = "清除学习结果，改用 2.5%/km 的保守默认电耗。",
                confirmTitle = "恢复物理基准？",
                confirmMessage = "本地学习模型会被停用，续航回到保守默认估算。历史标注会保留，可随时重新训练。",
                confirmLabel = "恢复基准",
                onConfirm = { viewModel.resetModelToBaseline() }
            )
            Caption(
                text = "只有最近 7 天的周期可以标注；所有骑行区间都标注人数后才会进入训练。",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        ui.historyMessage?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = 10.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        SettingsGroup(title = "应用与后台") {
            ActionRow(
                title = "通知权限",
                description = if (notificationsGranted) {
                    "已允许，用于充电/反弹提醒与保护线通知。"
                } else {
                    "未允许，点击后由系统弹窗确认；拒绝也不影响电量显示。"
                },
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        notificationsGranted = true
                    }
                },
                trailing = if (notificationsGranted) "已允许" else "未允许",
                showChevron = false
            )
            ActionRow(
                title = "后台与电池优化",
                description = "打开系统应用设置；华为/荣耀建议允许自启动、后台活动并关闭电池优化。",
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.parse("package:${context.packageName}"))
                    context.startActivity(intent)
                }
            )
            InfoRow(
                label = "采样策略",
                value = "15 / 60 分钟",
                description = "未稳定时每 15 分钟校准，稳定后每 60 分钟普通监测；受系统省电策略影响。"
            )
            InfoRow(label = "版本", value = "1.1 (2)")
        }

        SettingsGroup(title = "支持与诊断") {
            ActionRow(
                title = "诊断信息",
                description = "查看原始接口字段与最近采样；不影响电量计算。",
                onClick = onOpenDiagnostics
            )
        }

        SettingsGroup(title = "账号操作") {
            DangerActionRow(
                title = "退出登录",
                description = "清除本机保存的登录凭证与稳定锁，历史数据保留。",
                confirmTitle = "退出登录？",
                confirmMessage = "会清除加密保存的账号密码与当前会话，需要重新登录才能继续采样。",
                confirmLabel = "退出登录",
                onConfirm = { viewModel.logout() }
            )
        }

        ui.error?.let { ErrorText(it) }
        Spacer(Modifier.height(24.dp))
    }
}

private fun Context.hasNotificationPermission(): Boolean =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
