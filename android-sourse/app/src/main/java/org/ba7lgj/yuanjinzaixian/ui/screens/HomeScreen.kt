package org.ba7lgj.yuanjinzaixian.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ba7lgj.yuanjinzaixian.R
import org.ba7lgj.yuanjinzaixian.domain.BatteryState
import org.ba7lgj.yuanjinzaixian.domain.BatteryStatus
import org.ba7lgj.yuanjinzaixian.domain.PeopleRules
import org.ba7lgj.yuanjinzaixian.ui.UiState
import org.ba7lgj.yuanjinzaixian.ui.components.AppBatteryBar
import org.ba7lgj.yuanjinzaixian.ui.components.Caption
import org.ba7lgj.yuanjinzaixian.ui.components.ErrorText
import org.ba7lgj.yuanjinzaixian.ui.components.PeopleChooser
import org.ba7lgj.yuanjinzaixian.ui.components.VoltageChartCard
import org.ba7lgj.yuanjinzaixian.ui.components.formatTime
import org.ba7lgj.yuanjinzaixian.ui.components.statusText
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    ui: UiState,
    state: BatteryState,
    onPeopleSelected: (Int) -> Unit
) {
    val displaySoc = state.displaySocPercent
        ?: state.socPercent
        ?: state.instantSocPercent
    val people = PeopleRules.clamp(state.currentPeopleCount)
    val stable = state.status == BatteryStatus.STABLE && !state.stale
    val remainingKm = ui.liveRange?.remainingKm
        ?: state.rangeKm
            ?.takeIf { stable }
            ?.let { (it * 0.85.pow(people - 1)).roundToInt() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_aima),
                contentDescription = "爱玛",
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.size(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ui.deviceLabel ?: "我的车",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = state.address ?: "地址 --",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (!stable && ui.chartSamples.size >= 2) {
            VoltageChartCard(ui.chartSamples, state)
        }

        Spacer(Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = remainingKm?.toString()
                                    ?: (displaySoc?.toString() ?: "--"),
                                fontSize = 44.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (remainingKm != null) " km" else " %",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 7.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Caption(if (remainingKm != null) "预计剩余里程" else "电量预估（等待稳定）")
                    }
                    Text(
                        text = state.voltageV?.let { "%.2f V".format(it) } ?: "--",
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(12.dp))
                AppBatteryBar(displaySoc ?: 0)
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Caption("SOC ${displaySoc?.let { "$it%" } ?: "--"}")
                    Spacer(Modifier.weight(1f))
                    val anchor = ui.liveRange
                    Caption(
                        if (anchor?.available == true) {
                            "锚点 %.1fV · 已行 %.1fkm".format(
                                anchor.anchorVoltageV ?: 0.0,
                                anchor.distanceSinceAnchorKm
                            )
                        } else {
                            "尚无稳定锚点"
                        }
                    )
                }
                Spacer(Modifier.height(4.dp))
                Caption(statusText(state))
            }
        }

        Spacer(Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "载人人数",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                PeopleChooser(
                    current = people,
                    onSelect = onPeopleSelected
                )
            }
        }

        if (state.fullRequestPending) {
            Caption(
                text = "满电标定：等待电压稳定后写入满电锚点。",
                modifier = Modifier.padding(top = 10.dp)
            )
        } else if (state.fullRequestExpired) {
            Text(
                text = "满电标定：已失效，可到设置页重新标记。",
                modifier = Modifier.padding(top = 10.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        if (state.fullSuggestion) {
            Caption(
                text = "电压已稳定在高位，如确为充满静置，可到设置页标记充满。",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Caption(
            text = "最后数据 ${state.voltageObservedAt?.let(::formatTime) ?: "--"}",
            modifier = Modifier.padding(top = 10.dp)
        )
        ui.error?.let { ErrorText(it) }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Caption("后台每 15 分钟校准，稳定后改为每 60 分钟监测")
        }
    }
}
