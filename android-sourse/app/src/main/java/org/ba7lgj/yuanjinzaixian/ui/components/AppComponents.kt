package org.ba7lgj.yuanjinzaixian.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ba7lgj.yuanjinzaixian.data.VoltageSampleEntity
import org.ba7lgj.yuanjinzaixian.domain.BatteryState
import org.ba7lgj.yuanjinzaixian.domain.BatteryStatus
import org.ba7lgj.yuanjinzaixian.domain.Confidence
import org.ba7lgj.yuanjinzaixian.domain.HistorySegmentView
import org.ba7lgj.yuanjinzaixian.domain.PeopleRules
import org.ba7lgj.yuanjinzaixian.domain.TimeKeys
import org.ba7lgj.yuanjinzaixian.ui.navigation.AppTab
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, onBack: (() -> Unit)? = null) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "返回")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun AppBottomBar(currentRoute: String?, onSelect: (AppTab) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        AppTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = currentRoute == tab.route,
                onClick = { onSelect(tab) },
                icon = { Icon(tabIcon(tab), contentDescription = null) },
                label = { Text(tab.label) }
            )
        }
    }
}

private fun tabIcon(tab: AppTab): ImageVector = when (tab) {
    AppTab.HOME -> Icons.Filled.Home
    AppTab.HISTORY -> Icons.Filled.DateRange
    AppTab.SETTINGS -> Icons.Filled.Settings
}

@Composable
fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(content = content)
        }
    }
}

enum class ActionTone { NORMAL, DANGER }

@Composable
fun ActionRow(
    title: String,
    description: String,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    trailing: String? = null,
    showChevron: Boolean = true,
    tone: ActionTone = ActionTone.NORMAL
) {
    val titleColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        tone == ActionTone.DANGER -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 2.dp),
                tint = if (enabled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
            Spacer(Modifier.size(12.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = titleColor
            )
            Text(
                text = description,
                modifier = Modifier.padding(top = 2.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (trailing != null) {
            Text(
                text = trailing,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (showChevron) {
            Icon(
                Icons.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DangerActionRow(
    title: String,
    description: String,
    confirmTitle: String,
    confirmMessage: String,
    confirmLabel: String = "确认",
    onConfirm: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    ActionRow(
        title = title,
        description = description,
        onClick = { showDialog = true },
        tone = ActionTone.DANGER,
        showChevron = false
    )
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(confirmTitle) },
            text = { Text(confirmMessage) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onConfirm()
                    }
                ) {
                    Text(confirmLabel, color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("取消") }
            }
        )
    }
}

@Composable
fun InfoRow(label: String, value: String, description: String? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
        if (description != null) {
            Text(
                text = description,
                modifier = Modifier.padding(top = 2.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun Caption(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun ErrorText(message: String) {
    Text(
        text = message,
        modifier = Modifier.padding(top = 10.dp),
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun <T> SegmentedChoice(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val buttonModifier = Modifier
                .weight(1f)
                .height(40.dp)
            if (option == selected) {
                Button(
                    onClick = { onSelect(option) },
                    modifier = buttonModifier,
                    enabled = enabled,
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(label(option), maxLines = 1)
                }
            } else {
                OutlinedButton(
                    onClick = { onSelect(option) },
                    modifier = buttonModifier,
                    enabled = enabled,
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(label(option), maxLines = 1)
                }
            }
        }
    }
}

@Composable
fun PeopleChooser(
    current: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showCaption: Boolean = true
) {
    Column(modifier = modifier) {
        SegmentedChoice(
            options = PeopleRules.options(),
            selected = PeopleRules.clamp(current),
            label = { count -> if (count == 1) "1 人" else "2 人" },
            onSelect = onSelect,
            enabled = enabled
        )
        if (showCaption) {
            Caption(
                text = "含骑行者，每人约 70 kg；人数会影响预计续航。",
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
fun AppBatteryBar(socPercent: Int) {
    val soc = socPercent.coerceIn(0, 100)
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Color(0xFFF2F2EE))
        ) {
            if (soc > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(soc / 100f)
                        .background(
                            Color(0xFF4CAF50),
                            RoundedCornerShape(topStart = 13.dp, bottomStart = 13.dp)
                        )
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("E", fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.weight(1f))
                Text("F", fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.weight(0.2f))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(2.dp))
                )
                Spacer(Modifier.weight(0.8f))
            }
        }
    }
}

@Composable
fun VoltageChartCard(samples: List<VoltageSampleEntity>, state: BatteryState) {
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
            .padding(top = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
            Caption("从 ${formatTime(start)} 到现在 · 随后台采样更新")
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
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
                    val ratio = ((sample.observedAt - start).toFloat() / span).coerceIn(0f, 1f)
                    val x = left + (right - left) * ratio
                    val normalized = ((sample.voltageV!! - low) / (high - low)).coerceIn(0.0, 1.0)
                    Offset(x, bottom - (bottom - top) * normalized.toFloat())
                }
                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    points.drop(1).forEach { lineTo(it.x, it.y) }
                }
                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
                points.forEach {
                    drawCircle(color = lineColor, radius = 3.5.dp.toPx(), center = it)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Caption(formatTime(start))
                Spacer(Modifier.weight(1f))
                Caption(formatTime(end))
            }
        }
    }
}

fun statusText(state: BatteryState): String = when (state.status) {
    BatteryStatus.STABLE -> "电压稳定"
    BatteryStatus.REBOUND -> "刚停车，电压反弹中"
    BatteryStatus.CHARGE_UNKNOWN -> "疑似充电/反弹，待确认"
    BatteryStatus.STALE -> "数据已过期"
    BatteryStatus.OFFLINE -> "设备离线"
    BatteryStatus.ERROR -> "请求失败"
    BatteryStatus.WAITING -> "等待可信电压"
}

fun confidenceText(confidence: Confidence): String = when (confidence) {
    Confidence.HIGH -> "高"
    Confidence.MEDIUM -> "中"
    Confidence.LOW -> "低"
}

fun peopleSummary(segments: List<HistorySegmentView>): String {
    if (segments.isEmpty()) return "未标注"
    val values = segments.map { it.peopleCount }
    if (values.all { it == null }) return "未标注"
    if (values.any { it == null }) return "部分标注"
    val distinct = values.filterNotNull().toSet()
    return if (distinct.size == 1) {
        "${distinct.first()} 人"
    } else {
        "混合 " + distinct.sorted().joinToString("/") + " 人"
    }
}

fun formatDayLabel(dateKey: String): String {
    val now = System.currentTimeMillis() / 1000
    val label = TimeKeys.dayLabel(dateKey, now)
    return if (label == dateKey.substringAfter('-')) dateKey else "$label · $dateKey"
}

fun formatTime(epochSeconds: Long): String =
    SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA).format(Date(epochSeconds * 1000))

fun formatClock(epochSeconds: Long): String =
    SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(Date(epochSeconds * 1000))

fun formatDuration(seconds: Long): String {
    if (seconds <= 0) return "0 分钟"
    val minutes = seconds / 60
    return when {
        minutes < 60 -> "$minutes 分钟"
        else -> "${minutes / 60} 小时 ${minutes % 60} 分钟"
    }
}
