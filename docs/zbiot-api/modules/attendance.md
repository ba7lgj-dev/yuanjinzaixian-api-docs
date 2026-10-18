# 打卡 (Attendance)

- 模块标识：`attendance`
- 接口数量：4
- 原始文档：`source/打卡(attendance).txt`
- 正式环境：`http://cciot.cc/mapi`
- 测试环境：`http://cs.iotiot.cc/mapi`
- 开发环境：`http://192.168.1.105/mapi`
- 模块负责人：renj,hexq

## 接口索引

| 接口 | 名称 | 请求字段数 | 返回字段数 |
| --- | --- | ---: | ---: |
| [`BatchModifyClassPeriod`](#attendance-BatchModifyClassPeriod) | 批量修改上课时间段 | 7 | 2 |
| [`QueryAttendanceRecord`](#attendance-QueryAttendanceRecord) | 查询打卡记录 | 13 | 14 |
| [`QueryAttendanceDate`](#attendance-QueryAttendanceDate) | 查询有打卡记录的日期 | 6 | 4 |
| [`QueryClassPeriod`](#attendance-QueryClassPeriod) | 查询上课时间段 | 4 | 5 |

## 接口详情

<a id="attendance-BatchModifyClassPeriod"></a>
## 批量修改上课时间段 (`BatchModifyClassPeriod`)

- 接口 ID：`attendance.BatchModifyClassPeriod`
- 模块：`attendance`
- 用途：批量修改上课时间段
- 原始位置：`source/打卡(attendance).txt` 第 19 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,BatchModifyClassPeriod |
| `module` | `string` | 模块名,attendance |
| `params` | `object` | 参数列表 |
| `params.class_period` | `array` |  |
| `params.class_period.end` | `string` |  |
| `params.class_period.start` | `string` |  |
| `params.simeis` | `array` |  |

### 请求示例

```json
{
  "module": "attendance",
  "func": "BatchModifyClassPeriod",
  "params": {
    "class_period": [
      {
        "end": "17f786ee-b4ab-4361-8ff4-645247cec222",
        "start": "c3a9c578-850a-4e16-b839-dee0a09cc961"
      }
    ],
    "simeis": [
      "cd426515-41ff-4845-b555-13dd9342ea82",
      "4158dd81-3589-4035-a8b9-d5ade84fd094",
      "ad60899c-7113-4a0b-849d-8885ee73498a"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "bb4e61cb-f9ba-46e2-a619-22ab8274d19c"
}
```

<a id="attendance-QueryAttendanceRecord"></a>
## 查询打卡记录 (`QueryAttendanceRecord`)

- 接口 ID：`attendance.QueryAttendanceRecord`
- 模块：`attendance`
- 用途：查询打卡记录
- 原始位置：`source/打卡(attendance).txt` 第 70 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,QueryAttendanceRecord |
| `module` | `string` | 模块名,attendance |
| `params` | `object` | 参数列表 |
| `params.filter` | `number` | 是否过滤，0：不过滤，1：过滤 |
| `params.flag` | `number` | 1:上班打卡 2：下班打卡 |
| `params.last_sortvalue` | `string` | 用于报表查询时的排序值 |
| `params.last_time` | `number` |  |
| `params.limit` | `number` |  |
| `params.sfamilyid` | `string` | 组织 |
| `params.simei` | `string` |  |
| `params.status` | `number` | 打卡状态，0：正常，1：迟到 2：早退 3:缺卡 |
| `params.time_begin` | `number` | 开始时间 |
| `params.time_end` | `number` | 结束时间 |

### 请求示例

```json
{
  "module": "attendance",
  "func": "QueryAttendanceRecord",
  "params": {
    "filter": 486228668,
    "flag": 486228668,
    "last_sortvalue": "d4a1eb34-4844-4cae-aacc-36cff37c993c",
    "last_time": 486228668,
    "limit": 486228668,
    "sfamilyid": "69299968-82c1-454a-9182-616562e45dd8",
    "simei": "04587af9-3b96-442d-8402-f3101f5cd68c",
    "status": 486228668,
    "time_begin": 486228668,
    "time_end": 486228668
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.addr` | `string` | 打卡位置 |
| `items.car_number` | `string` | 车辆名称 |
| `items.class_period` | `string` | 该打卡记录所属的时间段 |
| `items.create_time` | `number` | default:0 |
| `items.filter` | `number` | default:0 |
| `items.flag` | `number` | default:0 |
| `items.id` | `number` | 消息ID |
| `items.imei` | `number` | default:0 |
| `items.sortvalue` | `string` | 用于报表排序值 |
| `items.status` | `number` | default:0 |
| `total` | `number` |  |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "9a4eb17b-50ac-4cce-91b7-e67eac57da9e",
  "items": [
    {
      "addr": "01dfd6de-45db-42fe-b55f-6abf319e9b76",
      "car_number": "2fb4489f-3997-4697-8cdf-b79b76055698",
      "class_period": "d7014860-c8f3-48d7-abca-04dfd5a13cbf",
      "create_time": 362815296,
      "filter": 362815296,
      "flag": 362815296,
      "id": 362815296,
      "imei": 362815296,
      "sortvalue": "2bc6a162-99d6-4b8b-abed-207d1e5b6cca",
      "status": 362815296
    },
    {
      "addr": "727155c3-f3f2-4050-b6ca-10b3211a4009",
      "car_number": "6c76758e-219e-4e6c-adf8-e7a98f8d29fd",
      "class_period": "490c2a57-bcfe-4cc4-861e-45a88437b7d5",
      "create_time": 362815296,
      "filter": 362815296,
      "flag": 362815296,
      "id": 362815296,
      "imei": 362815296,
      "sortvalue": "e0f5fc50-fafa-4cd6-b83c-f4ea3df6378a",
      "status": 362815296
    },
    {
      "addr": "bbda62c4-557d-4890-9852-8abe2a0e140d",
      "car_number": "dba92496-4fb5-417a-9daa-11be4da3a488",
      "class_period": "3bacba81-f248-4ddf-bfa8-558ab6d7b202",
      "create_time": 362815296,
      "filter": 362815296,
      "flag": 362815296,
      "id": 362815296,
      "imei": 362815296,
      "sortvalue": "d8f354af-005d-4323-8faf-19303f69661b",
      "status": 362815296
    }
  ],
  "total": 362815296
}
```

<a id="attendance-QueryAttendanceDate"></a>
## 查询有打卡记录的日期 (`QueryAttendanceDate`)

- 接口 ID：`attendance.QueryAttendanceDate`
- 模块：`attendance`
- 用途：查询有打卡记录的日期
- 原始位置：`source/打卡(attendance).txt` 第 195 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,QueryAttendanceDate |
| `module` | `string` | 模块名,attendance |
| `params` | `object` | 参数列表 |
| `params.simei` | `string` |  |
| `params.time_begin` | `number` | 开始时间戳 |
| `params.time_end` | `number` | 结束时间戳 |

### 请求示例

```json
{
  "module": "attendance",
  "func": "QueryAttendanceDate",
  "params": {
    "simei": "7e00abd5-b6aa-46b4-9596-6a44c6d658d5",
    "time_begin": 2048572338,
    "time_end": 2048572338
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `invalid_date` | `array` | 打卡异常的日期 |
| `normal_date` | `array` | 打卡正常的日期 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "62bd5699-7e9d-4445-94b7-68c30605cf83",
  "invalid_date": [
    1448496030,
    1448496030
  ],
  "normal_date": [
    1448496030,
    1448496030
  ]
}
```

<a id="attendance-QueryClassPeriod"></a>
## 查询上课时间段 (`QueryClassPeriod`)

- 接口 ID：`attendance.QueryClassPeriod`
- 模块：`attendance`
- 用途：查询上课时间段
- 原始位置：`source/打卡(attendance).txt` 第 248 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,QueryClassPeriod |
| `module` | `string` | 模块名,attendance |
| `params` | `object` | 参数列表 |
| `params.simei` | `string` |  |

### 请求示例

```json
{
  "module": "attendance",
  "func": "QueryClassPeriod",
  "params": {
    "simei": "c70afecb-f2d0-49f9-8df4-89ad26269902"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `class_period` | `array` | 上课时间段 |
| `class_period.end` | `string` |  |
| `class_period.start` | `string` |  |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |

### 返回示例

```json
{
  "errcode": 0,
  "class_period": [
    {
      "end": "746bb897-1a58-46fe-809c-23860f8fd845",
      "start": "e8cb91cc-a444-4bd6-bed9-6a0cd3fefe80"
    },
    {
      "end": "12aa8116-6fd9-445c-897a-cea88e9d8852",
      "start": "2904a880-21ca-41ac-974c-1c38876fbec0"
    }
  ],
  "error_message": "56e051a6-e6ad-4b4f-9233-b6d0988bb180"
}
```
