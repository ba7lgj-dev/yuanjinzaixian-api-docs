# 远近在线开放云服务 API 文档快照

本目录完整保存了 [https://api.zbiot.cc/](https://api.zbiot.cc/) 当前公开的接口文档，并转换为适合 AI 检索的结构化资料。

- 抓取时间：2026-10-18T13:12:05+08:00
- 覆盖范围：12 个业务模块，135 个接口
- 原始来源：站点 `public/txt/*.txt` 文档数据
- 原始快照：[`source/`](source/)

## AI 读取顺序

1. 先读 [`llms.txt`](llms.txt) 了解文档入口。
2. 查找接口时优先读 [`endpoint-index.json`](endpoint-index.json)，它包含全部接口的 ID、名称、用途和字段名。
3. 需要完整字段说明与示例时读 [`api-index.json`](api-index.json) 或对应模块 Markdown。
4. 需要一次性全文检索时读 [`llms-full.txt`](llms-full.txt) 或 [`all-api.md`](all-api.md)。

## 调用模型

```http
POST http://cciot.cc/mapi?sid=<登录返回的sid>
Content-Type: application/json

{
  "module": "user",
  "func": "Login",
  "params": {}
}
```

- 接口入口固定，`module` 选择模块，`func` 选择函数，`params` 承载业务参数。
- `sid` 通过 URL 查询参数传递。每个模块文档开头也保留了该模块列出的开发、测试和正式地址。
- 返回值均为 JSON，成功时通常包含 `errcode: 0`。
- 本项目只保存文档快照，不包含可用账号、密码、`sid` 或真实设备数据。

## 关键接入规则

| 主题 | 规则 |
| --- | --- |
| 登录凭证 | `user.Login` 返回 `sid`；连续 2 天未使用会过期 |
| sid 上限 | 同一账号最多 100 个有效 `sid` |
| 限频 | 单模块单接口 100ms 一次，1 分钟最多 500 次 |
| 超限错误 | `268697629`，操作频率过快 |
| 分页 | 瀑布流分页，下一页使用响应给出的最后一条记录参数 |
| 示例数据 | 示例值随机生成，只表示结构和类型 |
| 设备标识 | 多数接口使用加密设备号 `simei`，不是展示用 IMEI |
| 列表建议 | App 约 50 条/页，Web 约 100 条/页，或使用接口默认值 |

## 模块目录

| 模块标识 | 模块名称 | 接口数 | 文档 |
| --- | --- | ---: | --- |
| `user` | 用户管理 (User) | 20 | [打开](modules/user.md) |
| `family` | 设备车组 (Family) | 21 | [打开](modules/family.md) |
| `device` | 设备管理 (Device) | 21 | [打开](modules/device.md) |
| `location` | 设备轨迹 (Location) | 9 | [打开](modules/location.md) |
| `alarm` | 设备报警 (Alarm) | 17 | [打开](modules/alarm.md) |
| `fence` | 电子围栏 (Fence) | 5 | [打开](modules/fence.md) |
| `sim` | SIM卡 (SIM) | 14 | [打开](modules/sim.md) |
| `loc` | 设备模式 (Loc) | 12 | [打开](modules/loc.md) |
| `timerswitch` | 定时开关机（timerswitch） | 3 | [打开](modules/timerswitch.md) |
| `summary` | 统计报表 (Summary) | 7 | [打开](modules/summary.md) |
| `temphumi` | 温湿度 (Temphumi) | 2 | [打开](modules/temphumi.md) |
| `attendance` | 打卡 (Attendance) | 4 | [打开](modules/attendance.md) |

## 指南与索引

- [前言与接入规则](guides/overview.md)：认证、分页、限频、组织结构和常见错误。
- [坐标系转换代码](guides/coordinate-conversion.md)：WGS-84 / GCJ-02 的 C++ 与 JavaScript 实现。
- [完整接口 JSON](api-index.json)：包含每个接口的参数、返回字段和请求/返回示例。
- [接口速查 JSON](endpoint-index.json)：轻量索引，适合搜索后定位模块。
- [LLM 索引](llms.txt) / [LLM 全文](llms-full.txt)。
- [采集清单](manifest.json)：源文件、生成文件、哈希与覆盖统计。

## 已知文档特点

- 部分字段表把枚举值写在说明列，已按多行合并保留。
- 文档示例中的字符串和数值是随机占位值，不能直接作为业务参数使用。
- 源站点所有未知路径都会回退到同一 HTML 页面，因此本快照直接采集该页面实际加载的 `public/txt/*.txt` 文件。
- 前言中的模块数量与当前索引存在差异，正文和 JSON 均以实际接口详情为准。
