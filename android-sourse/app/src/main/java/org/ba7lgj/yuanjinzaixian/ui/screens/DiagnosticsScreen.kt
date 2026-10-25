package org.ba7lgj.yuanjinzaixian.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ba7lgj.yuanjinzaixian.data.VoltageSampleEntity
import org.ba7lgj.yuanjinzaixian.ui.UiState
import org.ba7lgj.yuanjinzaixian.ui.components.Caption
import org.ba7lgj.yuanjinzaixian.ui.components.formatDuration
import org.ba7lgj.yuanjinzaixian.ui.components.formatTime

@Composable
fun DiagnosticsScreen(ui: UiState, onLoad: () -> Unit) {
    LaunchedEffect(Unit) { onLoad() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "最近采样",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Caption(
            text = "定位器电量（非电动车电池）和原始 ACC 字段只用于排查，不参与 SOC 或续航计算。",
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )
        if (ui.diagnostics.isEmpty()) {
            Text(
                text = "还没有采样记录。",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        ui.diagnostics.forEach { sample -> DiagnosticCard(sample) }
    }
}

@Composable
private fun DiagnosticCard(sample: VoltageSampleEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                text = "通讯 ${formatTime(sample.observedAt)}",
                fontWeight = FontWeight.Medium
            )
            Caption(
                text = "外电 " + (sample.voltageV?.let { "%.2fV".format(it) } ?: "--") +
                    " · 年龄 ${formatDuration(sample.ageSeconds)}" +
                    " · 静止 ${formatDuration(sample.restSeconds)}"
            )
            Caption(
                text = "状态 " + (sample.state ?: "--") +
                    " · cache=${sample.cacheSuspect}" +
                    " · source=${sample.timestampSource}"
            )
            Caption(
                text = "定位器电量（非电动车电池） " +
                    (sample.locatorPowerPct?.let { "$it%" } ?: "--")
            )
        }
    }
}
