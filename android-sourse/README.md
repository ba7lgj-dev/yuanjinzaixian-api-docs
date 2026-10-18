# 远近电量 Android 核心版

首版目标：通过远近在线外电电压估算 60V 铅酸电池 SOC，并提供 4x1 桌面小组件、满/空标定、保护线提醒和疑似充电/反弹询问。

## 构建

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest
```

Debug APK：

`app/build/outputs/apk/debug/app-debug.apk`

## 当前能力

- 账号登录，密码和 `sid` 使用 Android Keystore 加密保存。
- 后台 WorkManager 每 15 分钟采样；App 前台每 1 分钟刷新。
- 小组件显示当前电压即时 SOC；静置 15 分钟后提供灰色指针临时估算，30 分钟且电压稳定后才建立可信稳定值，150 分钟后为高置信度。
- 只使用 `external_voltage` 作为电池 SOC 依据；`power` 是定位器电量，`acc_state`/`acc_power` 不参与车辆判断。
- `last_com_time` 是电压主时间，`last_pos.time` 仅在通讯时间缺失时兜底。
- 稳定电压使用 60V 铅酸 OCV 曲线；默认 57.8V 保护线，约 20% SOC。
- 4x1 小组件：半透明白色内容胶囊居中显示，淡灰色底槽上叠加自绘矩形电量条；SOC 低于 20% 为红色、20–80% 为浅绿、80% 以上为深绿。稳定后在电量条正下方显示小号预计续航；指针带白色外圈，绿色表示稳定，灰色表示临时/反弹/未知/过期。
- 小组件严格限制为 4 横向 × 1 纵向（`minHeight=40dp`），单行显示车辆昵称、电量条和外电电压；点击小组件会先下发一次 `e_cmd_query_location` 立即定位，再等待设备回报并刷新，不打开 App。
- App 在电压不稳定时显示“最近静止起点到现在”的电压折线。
- 疑似充电/反弹通知带“充过电 / 没充电 / 不确定”按钮。
- 设置页可保存车辆昵称；默认回退到平台备注、车牌号或“我的车”。

首版不包含行程统计、载人数量、海拔、车速和行程融合 SOC。
