package org.ba7lgj.yuanjinzaixian.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ba7lgj.yuanjinzaixian.domain.HistoryCycleView
import org.ba7lgj.yuanjinzaixian.domain.HistorySegmentView
import org.ba7lgj.yuanjinzaixian.domain.PeopleRules
import org.ba7lgj.yuanjinzaixian.ui.components.Caption
import org.ba7lgj.yuanjinzaixian.ui.components.SegmentedChoice
import org.ba7lgj.yuanjinzaixian.ui.components.formatClock

@Composable
fun TripDetailScreen(
    cycle: HistoryCycleView?,
    onPeopleSelected: (String, Int?) -> Unit
) {
    if (cycle == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = "找不到这段行程",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Caption(
                text = "记录可能已被清理，或已超过可编辑期限。返回历史列表查看其他周期。",
                modifier = Modifier.padding(top = 6.dp)
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "${formatClock(cycle.startTime)} → ${formatClock(cycle.endTime)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Caption("${cycle.dateKey} · %.1f km".format(cycle.distanceKm))
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "%.1fV → %.1fV".format(cycle.startVoltageV, cycle.endVoltageV),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = cycle.socDrop?.let { "掉电 $it%" } ?: "掉电 --",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = (if (cycle.trainingEligible) "✓ " else "") + cycle.trainingReason,
                    modifier = Modifier.padding(top = 6.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (cycle.trainingEligible) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                if (cycle.pauses.isNotEmpty()) {
                    Caption(
                        text = "本周期有 ${cycle.pauses.size} 个自动识别的暂停点，暂停边界只读。",
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Text(
            text = "骑行区间与人数",
            modifier = Modifier.padding(top = 20.dp, bottom = 4.dp),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Caption("人数包含骑行者，每人约 70 kg；全部区间标注完成后，本周期才计入模型训练。")

        if (cycle.segments.isEmpty()) {
            Caption(
                text = "这段周期没有可标注的骑行区间。",
                modifier = Modifier.padding(top = 12.dp)
            )
        }
        cycle.segments.forEach { segment ->
            SegmentCard(
                segment = segment,
                editable = cycle.editable,
                onPeopleSelected = onPeopleSelected
            )
        }

        Caption(
            text = "训练门槛：起止锚点都是训练锚点、标注距离覆盖 ≥95% 且没有充电事件。",
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
    }
}

@Composable
private fun SegmentCard(
    segment: HistorySegmentView,
    editable: Boolean,
    onPeopleSelected: (String, Int?) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "第 ${segment.sequence + 1} 段",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "%.1f km".format(segment.distanceKm),
                    fontWeight = FontWeight.Medium
                )
            }
            Caption(
                text = "${formatClock(segment.startTime)} → ${formatClock(segment.endTime)} · " +
                    "均速 %.0f km/h · 爬升 %.0f m".format(
                        segment.avgSpeedKmh,
                        segment.ascentM
                    )
            )
            Spacer(Modifier.height(10.dp))
            if (!editable) {
                Caption("人数：" + PeopleRules.shortLabel(segment.peopleCount) + "（只读）")
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SegmentedChoice(
                        options = PeopleRules.options(),
                        selected = segment.peopleCount ?: 0,
                        label = { count -> if (count == 1) "1 人" else "2 人" },
                        onSelect = { onPeopleSelected(segment.segmentKey, it) },
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { onPeopleSelected(segment.segmentKey, null) },
                        enabled = segment.peopleCount != null
                    ) {
                        Text("清除")
                    }
                }
            }
        }
    }
}
