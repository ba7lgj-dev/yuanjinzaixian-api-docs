# 远近在线电动车电量与续航助手（Android 本地版）

> 状态：核心版已实现，后续能力待补充  
> 保存日期：2026-10-18  
> 目标设备：60V 20Ah，5×12V 铅酸电池
> 代码位置：`android-sourse/`（首版核心版）
> 当前实现范围：电压 SOC、15 分钟临时估算、30 分钟稳定值、满/空锚点、57.8V 保护线、半透明 4x1 仪表条、不稳定电压折线、疑似充电/反弹通知；融合 SOC、行程、载人、海拔和车速暂未启用。

## 摘要

- 做成一个 **Android 单机 App + 4x1 桌面小组件**，不依赖云服务器；账号密码只保存在手机 Keystore 加密存储中。
- 同时提供 **电压预估 SOC** 和 **行程融合预估 SOC**；小组件主数字显示融合预估，同时保留电压预估作为对照。20% 作为可调保护线，续航只计算保护线以上的可用部分。
- 铅酸电池不直接用骑行中或刚停车时的电压换算 SOC。文献中 12V VRLA 在撤掉 10W 负载后进行了 150 分钟静置，另有研究使用 240 分钟，因此采用“静置稳定后再锚定 SOC”的策略。
- 参考依据：Qurthobi 等 2018 年 VRLA 负载电压/SOC 论文（DOI `10.11591/ijpeds.v9.i1.pp357-364`）、2019 年 OCV 估算论文（DOI `10.1088/1742-6596/1367/1/012077`）以及 2007 年铅酸 OCV-SOC 研究（DOI `10.1109/pccon.2007.373052`）。

## 实现方案

### Android 应用与采集

- 使用现有 `android-sourse/` Kotlin 工程，采用 Jetpack Compose、Glance、WorkManager、Room、OkHttp 和 Android Keystore；当前 `compileSdk/targetSdk 34`、`minSdk 24`。
- 后台每 15 分钟用 WorkManager 采集一次，App 前台时每 1 分钟采集一次；不常驻前台服务。
- 采集接口使用 `https://app.cciot.cc/mapi`，平台 `type=2`，请求体仍按实际可用格式发送 JSON，但 `Content-Type` 使用 `application/x-www-form-urlencoded`。
- 登录后保存 `sid`，后续调用 `family/GetDeviceList` 和 `device/GetDetail`；`sid` 过期错误 `268697631` 时自动重新登录，频率限制错误 `268697629` 时指数退避。
- 记录 `external_voltage`、`state`、`last_com_time`、`last_pos.time`、`cur_time`、`state_begin_time`、`state_time`、`day_distance`，以及仅用于诊断的 `oil_state`。
- 行程结束后分页调用 `location/QueryLocation`，读取每个轨迹点的 `time`、经纬度、`altitude`、`speed`、`direction`。实际响应包含这些字段，但接口文档没有列出 `altitude`，实现必须做字段探测和缺失降级。
- 轨迹中的 `external_voltage` 只作诊断，不作为 SOC 时间源；电压时间继续以 `last_com_time` 为准。速度按前端逻辑将原始值除以 10 转为 km/h，同时保留原始值，首次实车校验后再固定单位。
- 对海拔序列做中值平滑，忽略小于约 2m 的抖动，再计算累计爬升、累计下降、净爬升和每公里坡度；缺少 GPS 海拔的点标记为空，不用基站/WiFi 点伪造海拔。
- **不把 `power` 当作电动车电池 SOC**：该字段是定位器自身电量百分比，估算、续航、充电判断和 UI 主数值都不得使用；如保留原始值，仅命名为 `locator_power_pct` 放在诊断区。
- **不使用 `acc_state` 或 `acc_power` 判断车辆状态**：车辆是否移动、是否停车、是否充电都由电压趋势、`day_distance`、`state_time` 和 `PPoint` 推导。
- 原始响应中的 `acc_state`、`acc_power` 如保留，只能以 `raw_acc_state`、`raw_acc_power` 名称保存到诊断记录，不参与任何业务判断。
- 首次启动引导用户登录、选择设备、确认电池配置，并引导华为/荣耀开启自启动、后台活动和忽略电池优化；仍明确说明系统可能延迟后台任务。

### 电池状态机

- **疑似充电/反弹**：根据 `external_voltage` 连续上升或停留在电压平台判断，不依赖 `acc_power`；平台提供的“正在充电”只表示定位器在充电，不能据此认定车辆电池正在充电。
- **骑行中**：根据两次采样之间的 `day_distance` 增加或轨迹点变化判断，不依赖 `acc_state`；骑行期间不把电压当 OCV，显示上次稳定 SOC，并按近期电耗做临时估算。
- **反弹期**：停车或停止充电后的前 30 分钟不更新 SOC，显示“反弹中/低置信”。
- **稳定判据**：至少 30 分钟静置，最近 3 个有效样本跨度不少于 30 分钟，电压极差不超过 `0.05V`，且没有持续上升趋势；静置时长优先由文档中的 `state_time`/`state_begin_time` 推导，并用 `day_distance` 未增加交叉验证。
- 最多等待 4 小时；4 小时后仍未稳定时，只有 60 分钟内变化不超过 `0.15V` 才允许以中等置信度更新，否则继续显示旧值。
- 若 `last_com_time` 距服务器 `cur_time` 超过 30 分钟，或连续采集中 `last_com_time` 没有推进，小组件显示“数据过期”并保留最后时间和数值。

### 电压时间戳与缓存防护

- 文档只定义了“最后通信时间”`last_com_time` 和 `last_pos` JSON 内的定位时间 `time`，没有明确说明 `external_voltage` 是随通信更新还是随定位更新，也没有承诺缓存策略。
- **`last_com_time` 是外电电压的主时间戳**，直接作为 `voltage_observed_at`；不比较它和 `last_pos.time` 的先后、差值或哪个更新。
- `last_pos.time` 只作为 `last_com_time` 缺失或无效时的兜底时间，并在记录中标记 `timestamp_source=position_fallback`；有通讯时间时完全忽略定位时间用于电压新鲜度。
- 使用服务器 `cur_time` 计算 `age_seconds = max(0, cur_time - last_com_time)`，不使用手机本地时钟做新鲜度判断。
- 如果连续采样中 `last_com_time` 没有推进，视为重复/缓存候选：可以记录原始值，但不能计入稳定窗口、不能更新 SOC、不能用于满电/空电校准。
- 如果时间推进但电压完全不变，静止场景下可以视为稳定；移动中 `day_distance` 增加却仍返回同一电压，则标记 `cache_suspect=true` 并降低该段数据置信度。
- 历史停留用 `PPoint` 的 `start_time`/`cos_time` 判断静止区间；电压时间始终以该样本的 `last_com_time` 为准。

### 静止时长与电压可信度

- `device.GetDetail` 已提供 `state`、`state_begin_time` 和 `state_time`。当 `state=e_line_sleep` 时，把 `state_time` 作为“当前静止/睡眠持续秒数”的初值，并用 `cur_time - state_begin_time` 交叉校验；两者相差超过 2 分钟时降低可信度。
- `state` 只代表设备在线/睡眠状态，不等同于车辆物理静止；因此必须同时满足 `day_distance` 没有增加，才把这段时间作为静止候选。`acc_state` 不参与判断。
- App 自身记录第一次观察到睡眠状态且 `day_distance` 不再增加的时间，作为 `state_time` 缺失时的补充 `stationary_since`；如果 `day_distance` 增加，立即重置静止计时。
- 静止时长与电压稳定性共同决定 SOC 可信度：

  | 静止/睡眠持续 | 默认处理 |
  | --- | --- |
  | `<15 分钟` | 不使用该电压更新 SOC，显示“刚停车，电压反弹中” |
  | `15-30 分钟` | 只记录趋势，不建立稳定锚点 |
  | `30-150 分钟` | 电压满足 30 分钟稳定窗口时可建立中等置信度锚点 |
  | `>=150 分钟` | 电压稳定时可建立高置信度锚点，符合公开 VRLA 实验的静置量级 |
  | `>=240 分钟` | 最高可信度；若电压仍持续变化，则仍降级处理 |

- 电压稳定性和静止时长是两个独立门槛：时间够但电压仍在变化，只能算中等或低置信；电压暂时平稳但只静置了 10 分钟，也不能建立稳定锚点。
- 历史停留可用 `location.PPoint` 的 `start_time`、`cos_time`、`distance` 事后校验；`cos_time` 的单位为秒，`stop_time` 作为查询阈值。`location.PPointSummary` 的 `items.items.cos_time` 可用于按天查看停留统计。
- 若实时 `state_time` 缺失，App 先用自己的采样时间推算静止时长；等下一次拿到 `PPoint` 后再补全并重新计算该段数据的置信度。

### 双预估与短暂充电/反弹

- 同时维护两个电量结果：
  - `voltageSoc`：只由稳定外电电压和 OCV-SOC 曲线得到的“电压预估”，不稳定时保持上次可信值并标记不可校准。
  - `fusedSoc`：从上一个稳定锚点出发，结合距离、载人、海拔、车速和 SOC 区间消耗模型推算的“行程融合预估”。
- 刚停车、充电后短时间、电压仍在上升或数据时间未推进时，`voltageSoc` 不作为新的 SOC 锚点；`fusedSoc` 可继续按行程模型推进，但置信度降低。
- 如果静止期间电压明显上升但还没达到稳定，随后又开始骑行，标记为 `charge_or_rebound_unknown`：不判断是否真的充过电，也不把这段上升当满电；下一次稳定静止后统一校正两个预估值。
- 如果充电后电压稳定在高平台，App 只提示“可能已满”，必须等用户确认后才写入满电锚点。
- 稳定锚点出现时：
  - 高置信度电压：将 `fusedSoc` 校正到 `voltageSoc` 附近。
  - 中等置信度电压：`fusedSoc` 与 `voltageSoc` 各占约一半权重。
  - 低置信度电压：保留 `fusedSoc`，只把电压作为参考。
- 两个预估值相差超过 10 个百分点时，界面明确显示“电压预估/行程预估差异较大”，不得静默选择一个数值。
- 行程结束并得到高置信度终点后，才生成可供载人标记和电耗校准的行程。

### SOC、满电与保护线

- 默认使用 25°C、5×12V 铅酸包的 OCV-SOC 分段表：

  `100/90/80/70/60/50/40/30/20/10/0% → 63.0/62.5/62.1/61.6/61.0/60.3/59.5/58.75/57.9/56.55/52.5V`

- 稳定电压通过线性插值得到实际 SOC；确认过满电/空电锚点后，用锚点替换默认的 100%/0% 端点并重新缩放曲线。
- 默认保护线为 `57.8V`，对应约 20% 实际 SOC，用户可以在设置中调整；续航计算使用 `max(0, SOC - 20)`。
- 充电时若电压在 30 分钟内变化小于 `0.10V`，App 提示“可能已充满”；用户拔电并静置稳定后再确认，确认样本写入满电锚点。确认前必须至少满足 30 分钟静止候选窗口。
- 自动判空只在非充电、静止候选成立、连续 2 个间隔至少 15 分钟的样本都不高于保护线时触发；触发后通知一次并将可用续航置 0。正在充电或 `day_distance` 仍在增加时绝不自动判空。`acc_state`/`acc_power` 不参与判断。
- 用户手动“标记满电”和“标记空电”始终可用；空电锚点会覆盖默认的 52.5V 实际 0% 端点。

### 续航估算

- 默认电耗为 `2.5%/km`。按“满电到没动力 40 公里以上”的描述，100% 到 20% 保护线按约 32 公里保守显示，标注“未校准”。
- 同时显示 `voltageRange`（电压预估续航）和 `fusedRange`（行程融合续航）：前者只在稳定电压锚点有效，后者用于两次稳定锚点之间；小组件以 `fusedRange` 为主，状态中附带电压预估。
- 每次得到两个高置信度稳定锚点，且期间没有已确认充电、没有 `charge_or_rebound_unknown`、距离不少于 1 公里、SOC 下降不少于 5 个百分点时，生成一个可供校准的行程段；带未知充电/反弹标记的行程只保留原始记录，不进入 `%/km` 校准。
- 行程记录保存起点/终点稳定电压、SOC、时间、距离、电压变化、载人数量、海拔增益/损失和速度；`%/km = SOC 下降值 ÷ 距离`。
- 行程结束锚点稳定后，App 在行程记录中提示一次“这次坐了几个人？”，选项为 `1人/2人/3人/不标记`；用户未标记时不重复提醒，也不使用该行程做续航校准。
- 载人数量按“总人数（含骑行者）”记录，每人按约 `70kg` 估算相对负载；不记录或估算车辆自重。
- 已标记的行程才参与校准：先按 SOC 区间（100-80/80-60/60-40/40-20）分箱，避免用一条线性曲线描述整段电压变化；每个箱内优先使用相同载人数量、相近海拔和速度区间的行程中位数。
- 数据足够时再做稳健回归，特征为 `载人数量`、`每公里爬升`、`每公里下降`、`平均车速`；数据不足时退回同载人/同 SOC 区间中位数，再退回全部已标记行程中位数，禁止用未经验证的理论系数硬套。
- 爬坡按下坡/平路/爬坡分箱：`net_elevation_per_km` 明显为正时提高 `%/km`，明显为负时降低；下坡状态结合海拔下降和速度判断。高速区间单独统计，避免把高速耗电混入低速市区行程。
- 当前预计载人数量作为首页设置，默认 `1人`；预计续航使用对应人数下的 `%/km`，并在小组件状态中标明“按1人”或对应人数。
- 预计续航 `= 可用 SOC ÷ 当前 SOC 区间及路况下的 %/km`，结果四舍五入到公里，并明确标注“至20%保护线”。
- 距离优先使用 `day_distance` 的增量；跨天重置或数据缺失时回退到 `location/QueryLocation` 的轨迹距离。
- SOC 和续航分别显示置信度：稳定锚点且近期校准为高；稳定但缺少校准为中；充电、反弹、骑行中、数据过期或温度异常为低。

### 界面与小组件

- App 包含首页、历史曲线、行程记录、设置/校准四部分。
- 首页显示大号 SOC、预计续航、电压、状态、置信度、最后更新时间、“当前载人数量”和“标记满电/标记空电”按钮。
- 历史页显示电压与 SOC 曲线、充电/骑行区间、满/空锚点、行程距离、起止电压变化、载人数量和电耗校准结果；未标记行程显示“未标记，未参与校准”。
- 行程结束后在行程详情中显示一次载人数量选择；用户不选择则保持“未标记”，不弹出系统通知，也不反复追问。
- 支持 CSV 导出。
- 4x1 小组件使用居中的半透明白色内容胶囊、淡灰底槽和自绘矩形填充条：0–20% 红色、20–80% 浅绿、80–100% 深绿；20% 保留无文字刻度，右侧显示外电电压，稳定后在电量条正下方显示小号预计续航。指针绿色表示稳定、灰色表示反弹/未知/过期。点击只后台采样并刷新，不打开 App；每次采样后刷新，并保留 30 分钟系统刷新兜底。
- 核心版首页显示电压 SOC、预计续航、外电电压、可信度和最后更新时间；电压不稳定时显示从最近静止起点到当前时刻的电压折线。融合 SOC 暂不显示。
- 4x1 小组件主数字显示融合 SOC、融合续航、电压和状态；状态中附带“V估xx%”，不显示定位器自身电量；若诊断页保留定位器电量，必须标注为“定位器电量（非电动车电池）”。
- 只发送“到达保护线/自动判空”通知；充满建议仅在 App 内提示，不额外发通知。
- 原始 15 分钟样本保留 2 年；数据量很小，不做自动清理，用户可手动导出或清空。

## 接口与数据模型

- `BiotApi`：`login()`、`listDevices()`、`getDeviceDetail(simei)`、`queryLocation(range)`、`queryStopPoints(simei, begin, end, stopSeconds)`、`queryStopSummary(simei, begin, end)`；统一处理登录、`sid`、重试和限频。
- `queryLocation()` 返回 `QueryPoint`：`time`、`lat`、`lon`、`altitude?`、`speedRaw?`、`direction?`、`ptype?`；缺失字段必须允许为空，并记录 `feature_flags`，不能因未文档化字段缺失而丢弃整段行程。
- `BatteryEstimator`：纯函数实现稳定判据、OCV 插值、满/空锚点校准、自动判空和 `%/km` 续航计算，方便单元测试。
- Room 表：`voltage_samples`、`battery_anchors`、`trip_segments`、`stop_intervals`、`app_settings`。
- `trip_segments` 保存 `start_anchor_id`、`end_anchor_id`、起止时间、起止电压、起止 SOC、距离、`delta_voltage`、`people_count`、`payload_kg`、`soc_per_km`、`soc_band`、`elevation_gain_m`、`elevation_loss_m`、`net_elevation_per_km`、`avg_speed_kmh`、`max_speed_kmh`、`downhill_ratio` 和 `calibration_status`；未标记行程的 `people_count` 为 `null`。
- `voltage_samples` 额外保存 `state`、`state_begin_time`、`state_time`、`cur_time`、`last_com_time`、`last_pos_time`、`voltage_observed_at`、`timestamp_source`、`age_seconds`、`cache_suspect`、`day_distance`、`rest_seconds`、`rest_source` 和 `rest_confidence`；诊断字段 `locator_power_pct`、`raw_acc_state`、`raw_acc_power` 单独标记为不可用于估算。
- `stop_intervals` 保存 `PPoint` 返回的 `start_time`、`cos_time`、`distance`，用于事后校正静止时长。
- `BatteryState`：`voltageSocPercent`、`fusedSocPercent`、`voltageRangeKm`、`fusedRangeKm`、`modelDifference`、`voltageV`、`status`、`confidence`、`restSeconds`、`restConfidence`、`voltageObservedAt`、`ageSeconds`、`stale`、`currentPeopleCount`、`rangeBasis`、`chargeUncertainty`。
- 小组件只读取本地生成的 `BatteryState`，不直接持有账号或密码。
- 新增 `docs/battery-estimation.md`，记录论文依据、默认曲线、锚点校准公式和已知误差边界；现有 API 文档不改。

## 测试计划

- 单元测试：OCV 表插值、满/空锚点缩放、30 分钟稳定判据、`state_time`/`state_begin_time` 一致性、日里程不变才算静止、停车反弹排除、电压上升/平台判定充电、57.8V 双侧滞回、跨午夜日里程重置、续航中位数和无效行程过滤。
- 时间测试：`last_com_time` 始终优先、通讯时间缺失时才用 `last_pos.time`、通讯时间未推进、服务器时间倒退、手机时钟偏差、移动中电压不变导致 `cache_suspect`。
- 行程测试：稳定起点/终点才生成行程；骑行距离不足不生成；行程结束后只提示一次载人数量；未标记行程不进入校准；1人=70kg；不同载人数量下的回归/中位数/默认值回退正确；海拔爬升/下降、速度和下坡分箱正确。
- 双预估测试：短暂充电后立刻骑行不产生电压锚点；`fusedSoc` 可继续推进但降低置信度；下一次稳定锚点按高/中/低置信度分别重置或混合；两模型差异超过 10 点时界面明确提示。
- 轨迹兼容测试：`QueryLocation` 缺少 `altitude` 或 `speed` 时降级；基站/WiFi 点没有海拔时不用 0 伪造；速度原始值/10 的换算与前端一致。
- 回归测试：任意修改 `power`、`acc_state`、`acc_power` 的原始值，都不得改变 SOC、续航、充电状态或自动判空结果；UI 不得把 `power` 显示成电动车电池电量。
- API 测试：登录成功、`sid` 过期自动重登、频率限制退避、网络中断、电压字段缺失或格式异常、`PPoint`/`PPointSummary` 返回空数据和分页。
- 手工验收：安装 APK、登录、添加 4x1 小组件；后台等待 15 分钟确认采样；重启手机确认 WorkManager 和小组件恢复；完整充电并确认满电锚点；骑行后等待稳定 SOC；完成至少两次有效行程后确认续航从默认值切换为校准值；触发保护线确认只通知一次。
- 验收标准：充电和骑行中不产生新的稳定 SOC；数据过期有明确标记；满电必须由用户确认；57.8V 自动保护线不会被负载瞬间电压误触发；未校准时续航按保守 32 公里显示。

## 假设与默认值

- 电池按 **5×12V 铅酸、60V 20Ah、30 个 2V 单体**处理；没有单体 BMS 电压、电流传感器和电池温度数据。
- 57.8V 是默认保护线，约等于 20% 实际 SOC；用户可改，手动空电锚点优先于默认值。
- `power` 是定位器自身电量百分比，不是电动车电池 SOC；任何情况下都不能用它替代或修正 `external_voltage` 估算。
- 当前平台没有可回填的历史电压接口；数据从 App 安装并开始采样后积累。
- API 文档没有说明 `external_voltage` 的独立采集时间，也没有承诺缓存语义；按用户确认的口径，一律以 `last_com_time` 作为电压主时间，`last_pos.time` 仅在通讯时间缺失时兜底。
- `state_time` 是设备状态持续时间的代理值，不保证等同于车辆完全物理静止；必须与 `day_distance` 和 `PPoint` 交叉验证，不能使用 ACC 开关作为判据。
- 载人数量是相对负载标签，按总人数（含骑行者）和每人约 70kg 估算；不建模车辆自重，也不声称得到真实绝对重量。
- `QueryLocation` 的真实响应包含文档未列出的 `altitude`、`external_voltage` 等字段；海拔和速度必须做字段探测。轨迹中的电压只作诊断，不改变 `last_com_time` 主时间口径。
- 平台无法提供车辆充电器电流或“车辆正在充电”状态；短时充电、停车反弹和电压回弹在数据上可能不可区分，必须用“未知充电/反弹”状态和降低置信度处理。
- `PPoint`/`PPointSummary` 是事后停留记录，适合校验静止时长，不保证能实时反映当前这一次停车。
- HarmonyOS/荣耀可能延迟 WorkManager；App 前台 1 分钟采样、手动刷新和小组件点击可作为补偿，但不承诺严格 15 分钟实时性。
- 第一版以个人侧载 APK 为目标，不发布应用商店；应用暂定名“远近电量”，包名 `com.yuanjin.batterymonitor`。

## 待补充

后续补充内容在确认后追加到本文件，并在定稿时更新文档状态。
