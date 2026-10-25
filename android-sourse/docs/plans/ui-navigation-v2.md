# 远近电量 1.1：导航层级与操作规范

## 页面结构

```
login
devices
home ────────────────┬── history ── cycle/{cycleKey}
                     └── settings ── diagnostics
```

- 底部导航只有三个一级页：首页、历史、设置。
- 登录页、设备选择页不显示底部导航；设备选择从设置进入时以二级页方式压栈。
- 行程详情只从历史页进入；超过 7 天的月汇总不可进入详情。
- 诊断只从设置页进入。

## 路由

| 路由 | 页面 | 底部导航 | 返回行为 |
| --- | --- | --- | --- |
| `login` | 登录 | 无 | 直接退出应用 |
| `devices` | 选择设备 | 无 | 从设置压栈时返回设置；作为启动页时直接退出 |
| `home` | 首页 | 有 | 2 秒内再按一次退出应用 |
| `history` | 历史列表 | 有 | 返回首页 |
| `settings` | 设置 | 有 | 返回首页 |
| `cycle/{cycleKey}` | 行程详情 | 无 | 返回历史列表 |
| `diagnostics` | 诊断 | 无 | 返回设置 |

`cycleKey` 含冒号，跳转前使用 `URLEncoder` 编码，页面内同时按原始值与解码值查找周期。

## 操作说明规范

- 所有可点击行使用 `ActionRow`：标题 + 一行作用说明，右侧可选状态文字与箭头。
- 危险操作用 `DangerActionRow`：错误色标题 + 说明 + 二次确认对话框（取消 / 确认）。
- 分段选择（1 人 / 2 人）下方必须有一行说明，解释“人数含骑行者、每人约 70kg、影响续航”。
- 只读信息使用 `InfoRow`，不显示箭头，避免被误认为可点击。

## 设置页分组

1. 账号与车辆：账号、当前车辆、切换设备、车辆昵称。
2. 电量标定：满/空电压输入与保存、标记充满、标记实际空电、重置标定（危险）。
3. 载人设置：1 人 / 2 人默认值。
4. 历史与模型：同步历史、训练状态、重新训练、回滚上一版、恢复物理基准（危险）。
5. 应用与后台：通知权限、后台与电池优化、采样策略、版本。
6. 支持与诊断：诊断信息。
7. 账号操作：退出登录（危险）。

## 人数规则

- 总人数范围固定为 1–2 人，包含骑行者：1 人 = 仅骑行者，2 人 = 骑行者 + 1 名乘客。
- `PeopleRules` 是唯一收敛点：`clamp(0)=1`、`clamp(2)=2`、`clamp(3)=2`、`clamp(4)=2`。
- UI、缓存读取、训练样本聚合和模型预测都经过 `PeopleRules`；模型不会接收到 3 人或 4 人特征。

## 数据库升级（v7 → v8）

```sql
ALTER TABLE app_settings ADD COLUMN modelNeedsRetrain INTEGER NOT NULL DEFAULT 0;
UPDATE app_settings SET modelNeedsRetrain = 1;
UPDATE trip_segments SET peopleCount = 2 WHERE peopleCount IS NOT NULL AND peopleCount > 2;
UPDATE trip_segments SET peopleCount = 1 WHERE peopleCount IS NOT NULL AND peopleCount < 1;
UPDATE range_state SET currentPeopleCount =
  CASE WHEN currentPeopleCount < 1 THEN 1
       WHEN currentPeopleCount > 2 THEN 2
       ELSE currentPeopleCount END;
UPDATE model_versions SET active = 0;
```

- 不删除轨迹、电压历史、周期、标定、账号或昵称。
- 升级后首次启动若存在 ≥5 个完整标注周期，自动按 1–2 人规则重训一次；不足 5 个则使用物理基准。
- 用户手动“恢复物理基准”会清除待重训标记，不会被下次启动再次覆盖。

## 主题

- 关闭系统动态取色，固定品牌绿：主色 `#2E7D32`、容器 `#C8E6C9`、错误 `#C62828`、浅底 `#FAFAF7`。
- 深色模式使用同一绿色的低亮度变体，与电量条、爱玛灰图标保持一致。

## 验收

- `testDebugUnitTest` 通过（共 50 个测试，含新增 `PeopleRulesTest` 与 `NavigationRulesTest`）。
- `assembleDebug` 通过。
- 人数 0、3、4 会被裁剪到 1–2，模型预测对 2 人与 4 人给出相同基线。
- 历史页无训练、回滚、同步按钮；这些操作只在设置页。
- 小组件点击行为未改动，仍只执行一次 `GetDetail` 后台刷新。
