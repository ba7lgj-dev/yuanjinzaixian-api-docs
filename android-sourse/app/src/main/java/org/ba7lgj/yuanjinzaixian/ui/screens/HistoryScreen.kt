package org.ba7lgj.yuanjinzaixian.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ba7lgj.yuanjinzaixian.domain.HistoryCycleView
import org.ba7lgj.yuanjinzaixian.ui.UiState
import org.ba7lgj.yuanjinzaixian.ui.components.Caption
import org.ba7lgj.yuanjinzaixian.ui.components.ErrorText
import org.ba7lgj.yuanjinzaixian.ui.components.formatClock
import org.ba7lgj.yuanjinzaixian.ui.components.formatDayLabel
import org.ba7lgj.yuanjinzaixian.ui.components.peopleSummary

@Composable
fun HistoryScreen(
    ui: UiState,
    onOpenCycle: (String) -> Unit,
    onRefresh: () -> Unit
) {
    var showOlder by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) { onRefresh() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        HistorySummaryCard(ui)
        if (ui.historySyncing) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
        ui.historyMessage?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        ui.error?.let { ErrorText(it) }

        val history = ui.history
        if (history == null) {
            Text(
                text = "还没有历史数据。首次登录后会自动回填最近两年，也可以到设置页手动同步。",
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            return@Column
        }
        if (history.recentDays.isEmpty()) {
            Text(
                text = "最近 7 天没有可标注的完整周期。完整周期需要两次稳定静置和中间的实际行驶。",
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        history.recentDays.forEach { day ->
            Text(
                text = formatDayLabel(day.dateKey),
                modifier = Modifier.padding(top = 18.dp, bottom = 6.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            day.cycles.forEach { cycle ->
                HistoryCycleCard(cycle, onOpenCycle)
            }
        }
        if (history.olderMonths.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            TextButton(
                onClick = { showOlder = !showOlder },
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
            ) {
                Text(
                    if (showOlder) "收起更早记录"
                    else "更早记录 ${history.olderMonths.size} 个月（只读汇总）"
                )
            }
            Caption("超过 7 天只保留月汇总，不能继续编辑人数。")
            if (showOlder) {
                history.olderMonths.forEach { month ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(month.monthKey, fontWeight = FontWeight.Medium)
                            Spacer(Modifier.weight(1f))
                            Text(
                                "${month.cycleCount} 段 · %.1f km".format(month.distanceKm),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistorySummaryCard(ui: UiState) {
    val training = ui.training
    val history = ui.history
    val recentKm = history?.recentDays?.sumOf { day ->
        day.cycles.sumOf { it.distanceKm }
    } ?: 0.0
    val olderKm = history?.olderMonths?.sumOf { it.distanceKm } ?: 0.0
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "行程记录",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(training.modelLabel, fontWeight = FontWeight.Medium)
            }
            Caption(
                text = "周期 ${training.cycleCount} 个 · 可训练 ${training.trainingCycleCount} 个 · " +
                    "累计 %.1f km".format(recentKm + olderKm),
                modifier = Modifier.padding(top = 4.dp)
            )
            val trainingHint = when {
                training.sampleCount < 5 -> "少于 5 个完整周期，继续使用物理基准"
                training.sampleCount < 10 -> "5–9 个周期，使用人数中位数"
                else -> "已启用本地学习模型"
            }
            Caption(text = trainingHint, modifier = Modifier.padding(top = 2.dp))
            Caption(
                text = "模型训练、回滚和恢复基准都在设置页操作。",
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun HistoryCycleCard(cycle: HistoryCycleView, onOpenCycle: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(enabled = cycle.editable) { onOpenCycle(cycle.cycleKey) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${formatClock(cycle.startTime)} → ${formatClock(cycle.endTime)}",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "%.1f km".format(cycle.distanceKm),
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "%.1fV → %.1fV".format(cycle.startVoltageV, cycle.endVoltageV) +
                        (cycle.socDrop?.let { " · 掉电 $it%" } ?: ""),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "人数：" + peopleSummary(cycle.segments) +
                        " · " + (if (cycle.trainingEligible) "✓ " else "") + cycle.trainingReason,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (cycle.trainingEligible) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                Caption(
                    text = if (cycle.editable) "点击查看详情并标注人数" else "超过 7 天，只读",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            if (cycle.editable) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
