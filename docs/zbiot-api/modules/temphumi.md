# 温湿度 (Temphumi)

- 模块标识：`temphumi`
- 接口数量：2
- 原始文档：`source/温度、湿度(temphumi).txt`
- 正式环境：`http://cciot.cc/mapi`
- 测试环境：`http://csapp.cciot.cc/mapi`
- 开发环境：`http://192.168.1.105/mapi`
- 模块负责人：hexq,renj,fushengyou

## 接口索引

| 接口 | 名称 | 请求字段数 | 返回字段数 |
| --- | --- | ---: | ---: |
| [`QueryTemperature`](#temphumi-QueryTemperature) | 查询温度信息 | 8 | 6 |
| [`QueryHumidity`](#temphumi-QueryHumidity) | 查询湿度信息 | 8 | 6 |

## 接口详情

<a id="temphumi-QueryTemperature"></a>
## 查询温度信息 (`QueryTemperature`)

- 接口 ID：`temphumi.QueryTemperature`
- 模块：`temphumi`
- 用途：查询温度信息
- 原始位置：`source/温度、湿度(temphumi).txt` 第 15 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,QueryTemperature |
| `module` | `string` | 模块名,temphumi |
| `params` | `object` | 参数列表 |
| `params.last_time` | `number` | 最后时间 |
| `params.limit_size` | `number` | 限制获取数量，最大限制2000 |
| `params.simei` | `string` | 加密后设备号,设备号登录可以不用传 |
| `params.time_begin` | `number` | 开始时间 |
| `params.time_end` | `number` | 结束时间 |

### 请求示例

```json
{
  "module": "temphumi",
  "func": "QueryTemperature",
  "params": {
    "last_time": 483878995,
    "limit_size": 483878995,
    "simei": "6f769c80-d1a0-4ae8-beff-b320e053e264",
    "time_begin": 483878995,
    "time_end": 483878995
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.imei` | `number` | primary key:1(fimei) |
| `items.temperature` | `number` |  |
| `items.time` | `number` | primary key:2(ftime) |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "b8d667f8-12df-4554-a1dc-d30e603e5d60",
  "items": [
    {
      "imei": 1714867017,
      "temperature": 1714867017,
      "time": 1714867017
    },
    {
      "imei": 1714867017,
      "temperature": 1714867017,
      "time": 1714867017
    }
  ]
}
```

<a id="temphumi-QueryHumidity"></a>
## 查询湿度信息 (`QueryHumidity`)

- 接口 ID：`temphumi.QueryHumidity`
- 模块：`temphumi`
- 用途：查询湿度信息
- 原始位置：`source/温度、湿度(temphumi).txt` 第 82 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,QueryHumidity |
| `module` | `string` | 模块名,temphumi |
| `params` | `object` | 参数列表 |
| `params.last_time` | `number` | 最后时间 |
| `params.limit_size` | `number` | 限制获取数量，最大限制2000 |
| `params.simei` | `string` | 加密后设备号,设备号登录可以不用传 |
| `params.time_begin` | `number` | 开始时间 |
| `params.time_end` | `number` | 结束时间 |

### 请求示例

```json
{
  "module": "temphumi",
  "func": "QueryHumidity",
  "params": {
    "last_time": 38520824,
    "limit_size": 38520824,
    "simei": "3be80f44-3f1d-421d-a1b8-e3fb19d85649",
    "time_begin": 38520824,
    "time_end": 38520824
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.humidity` | `number` |  |
| `items.imei` | `number` | primary key:1(fimei) |
| `items.time` | `number` | primary key:2(ftime) |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "4bf7ae7f-2340-41f4-aea6-562a5d304c94",
  "items": [
    {
      "humidity": 209860123,
      "imei": 209860123,
      "time": 209860123
    }
  ]
}
```
