package org.ba7lgj.yuanjinzaixian.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.ba7lgj.yuanjinzaixian.ui.UiState
import org.ba7lgj.yuanjinzaixian.ui.components.Caption
import org.ba7lgj.yuanjinzaixian.ui.components.ErrorText

@Composable
fun LoginScreen(ui: UiState, onLogin: (String, String) -> Unit) {
    var account by rememberSaveable { mutableStateOf(ui.account) }
    var password by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "远近电量",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "读取外电电压，估算 60V 铅酸电池剩余电量与续航",
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
            onClick = { onLogin(account, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !ui.loading
        ) {
            if (ui.loading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
                Text("登录")
            }
        }
        Caption(
            text = "登录后读取账号下的车辆列表；账号密码只加密保存在本机，不会上传到其他服务。",
            modifier = Modifier.padding(top = 8.dp)
        )
        ui.error?.let { ErrorText(it) }
        Spacer(Modifier.height(28.dp))
        Caption(
            "提示：平台显示的“正在充电”可能只是定位器在充电，车辆充电判断由电压趋势完成；" +
                "平台电量百分比是定位器电量，不是电动车电池电量。"
        )
    }
}
