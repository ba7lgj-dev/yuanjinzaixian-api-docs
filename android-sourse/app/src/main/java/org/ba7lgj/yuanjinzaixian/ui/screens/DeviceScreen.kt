package org.ba7lgj.yuanjinzaixian.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ba7lgj.yuanjinzaixian.data.DeviceInfo
import org.ba7lgj.yuanjinzaixian.ui.UiState
import org.ba7lgj.yuanjinzaixian.ui.components.Caption
import org.ba7lgj.yuanjinzaixian.ui.components.ErrorText

@Composable
fun DeviceScreen(
    ui: UiState,
    onSelect: (DeviceInfo) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "选择要显示的车辆",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Caption(
            text = "首页和桌面小组件只显示所选车辆；切换后历史标注仍按车辆分别保存。",
            modifier = Modifier.padding(top = 6.dp, bottom = 16.dp)
        )
        if (ui.loading) {
            CircularProgressIndicator(modifier = Modifier.padding(vertical = 12.dp))
        }
        if (ui.devices.isEmpty()) {
            Text(
                text = "没有获取到可选设备。",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text("返回上一页")
            }
        }
        ui.devices.forEach { device ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                            "设备尾号 " + (device.imei?.toString()?.takeLast(4) ?: "--"),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(
                        onClick = { onSelect(device) },
                        enabled = !ui.loading
                    ) {
                        Text("选择设备")
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        ui.error?.let { ErrorText(it) }
    }
}
