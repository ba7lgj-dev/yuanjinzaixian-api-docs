# 设备模式 (Loc)

- 模块标识：`loc`
- 接口数量：12
- 原始文档：`source/模式(loc).txt`
- 正式环境：`http://cciot.cc/mapi`
- 测试环境：`http://csapp.cciot.cc/mapi`
- 开发环境：`http://192.168.1.105/mapi`
- 模块负责人：hexiaoqin,fushengyou

## 接口索引

| 接口 | 名称 | 请求字段数 | 返回字段数 |
| --- | --- | ---: | ---: |
| [`SetLocMode`](#loc-SetLocMode) | 设置定位模式 | 19 | 2 |
| [`GetLocMode`](#loc-GetLocMode) | 获取定位模式 | 4 | 23 |
| [`SetRtlsMode`](#loc-SetRtlsMode) | 设置定位上传模式 | 6 | 2 |
| [`AddLoopLoc`](#loc-AddLoopLoc) | 添加周期定位 | 9 | 3 |
| [`UpdateLoopLoc`](#loc-UpdateLoopLoc) | 修改周期定位 | 9 | 2 |
| [`GetLoopLoc`](#loc-GetLoopLoc) | 获取周期定位 | 6 | 10 |
| [`DelLoopLoc`](#loc-DelLoopLoc) | 删除周期定位 | 4 | 2 |
| [`GetRtlsMode`](#loc-GetRtlsMode) | 获取定位上传模式 | 4 | 6 |
| [`SetPosInt`](#loc-SetPosInt) | 设置定位时间间隔 | 5 | 2 |
| [`BatchModifyLocMode`](#loc-BatchModifyLocMode) | 批量修改多选模式 | 8 | 6 |
| [`BatchModifyRtlsMode`](#loc-BatchModifyRtlsMode) | 批量修改定时定距模式 | 7 | 6 |
| [`BatchModifyAlarmMode`](#loc-BatchModifyAlarmMode) | 批量修改闹钟模式 | 10 | 6 |

## 接口详情

<a id="loc-SetLocMode"></a>
## 设置定位模式 (`SetLocMode`)

- 接口 ID：`loc.SetLocMode`
- 模块：`loc`
- 用途：设置定位模式
- 原始位置：`source/模式(loc).txt` 第 35 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，设置的模式需要设备版本支持；
2，设置飞行模式的定位时间间隔范围（10-1440）且需要设置开关；
3，设置睡眠模式时需要设置时间；
4，需要修改设备信息权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SetLocMode |
| `module` | `string` | 模块名,loc |
| `params` | `object` | 参数列表 |
| `params.m7_config` | `object` | M7设备模式参数 |
| `params.m7_config.enable_mute_period` | `boolean` | 是否启用上课模式 |
| `params.m7_config.enable_work_period` | `boolean` | 是否启用工作时间段 |
| `params.m7_config.mute_period` | `array` | 静音时段 |
| `params.m7_config.mute_period.end` | `string` |  |
| `params.m7_config.mute_period.start` | `string` |  |
| `params.m7_config.rtmode_interval` | `number` | 实时模式运动上报间隔 |
| `params.m7_config.rtmode_sleep_interval` | `number` | 实时模式休眠上报间隔 |
| `params.m7_config.spmode_interval` | `number` | 省电模式上报间隔 |
| `params.m7_config.work_period` | `object` | 工作时段 |
| `params.m7_config.work_period.end` | `string` |  |
| `params.m7_config.work_period.start` | `string` |  |
| `params.mode_id` | `number` | 定位模式 |
| `params.mode_type` | `number` | 数值,比如省电模式下的时间,飞行模式下的开关：0-关闭，1-开启 |
| `params.simei` | `string` | 设备号,如果设备号登入，可以不用填写 |
| `params.smode_value` | `string` | 时间 格式 HH:MM 飞行模式定位间隔 |

### 请求示例

```json
{
  "module": "loc",
  "func": "SetLocMode",
  "params": {
    "m7_config": {
      "enable_mute_period": true,
      "enable_work_period": true,
      "mute_period": [
        {
          "end": "97be51de-932a-4ac1-ba75-4c5f5a4c5975",
          "start": "30b4a627-febb-4528-97e0-975b6adca5a8"
        },
        {
          "end": "c70c7911-71cc-4837-a239-6adbb06f299b",
          "start": "d39a2371-1df7-42bc-a5a5-38ea2b2f1b33"
        }
      ],
      "rtmode_interval": 2021623166,
      "rtmode_sleep_interval": 2021623166,
      "spmode_interval": 2021623166,
      "work_period": {
        "end": "884dd009-097f-4ac2-95b8-e1aabfc4b871",
        "start": "4f41600e-144f-4058-8a6e-912af826015d"
      }
    },
    "mode_id": 2021623166,
    "mode_type": 2021623166,
    "simei": "8dec6e23-c796-49a3-8e7a-6e6f3b2c88dd",
    "smode_value": "413932bd-849e-4d3e-ac3a-35796286263e"
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
  "error_message": "2f5b51b3-6604-4eb7-8552-29903ff4ef16"
}
```

<a id="loc-GetLocMode"></a>
## 获取定位模式 (`GetLocMode`)

- 接口 ID：`loc.GetLocMode`
- 模块：`loc`
- 用途：获取定位模式
- 原始位置：`source/模式(loc).txt` 第 130 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

uid的说明:
1:界面没有任何处理
2:弹出周期定位设置界面
3:弹出飞行模式设置界面
4:弹出提示框
5:单下拉框
6:弹出时间选择框
7:多项下拉框

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetLocMode |
| `module` | `string` | 模块名,loc |
| `params` | `object` | 参数列表 |
| `params.simei` | `string` | 设备号,如果设备号登入，可以不用填写 |

### 请求示例

```json
{
  "module": "loc",
  "func": "GetLocMode",
  "params": {
    "simei": "a48df257-1360-416e-8401-9ca608e302fd"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `m7_config` | `object` | M7设备模式参数 |
| `m7_config.enable_mute_period` | `boolean` | 是否启用上课模式 |
| `m7_config.enable_work_period` | `boolean` | 是否启用工作时间段 |
| `m7_config.mute_period` | `array` | 静音时段 |
| `m7_config.mute_period.end` | `string` |  |
| `m7_config.mute_period.start` | `string` |  |
| `m7_config.rtmode_interval` | `number` | 实时模式运动上报间隔 |
| `m7_config.rtmode_sleep_interval` | `number` | 实时模式休眠上报间隔 |
| `m7_config.spmode_interval` | `number` | 省电模式上报间隔 |
| `m7_config.work_period` | `object` | 工作时段 |
| `m7_config.work_period.end` | `string` |  |
| `m7_config.work_period.start` | `string` |  |
| `mode_id` | `number` | 定位模式 |
| `mode_list` | `array` | 支持的定位模式 |
| `mode_list.mid` | `number` | 模式id |
| `mode_list.msg` | `string` | 被前端展示的说明 |
| `mode_list.name` | `string` | 名称 |
| `mode_list.param` | `string` | 需要的附加信息 |
| `mode_list.uid` | `number` | UI类型，前端展示的类型 |
| `mode_type` | `number` | 值,例如点名模式 定位间隔 飞行模式开关 0-关闭 1-打开 |
| `smode_value` | `string` | 待机模式设置的时间 格式 HH:MM 飞行模式定位间隔 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "45c24de4-78ed-4d77-81d7-bc7dc9c5992e",
  "m7_config": {
    "enable_mute_period": true,
    "enable_work_period": true,
    "mute_period": [
      {
        "end": "7abc7a93-38a7-44de-afe4-801dce56c02e",
        "start": "1055a9ad-8f67-45a0-9dc1-ff60e252f256"
      },
      {
        "end": "908b3087-a986-4bd2-af97-5a08b8a75ae6",
        "start": "aae66fb6-26b3-4efb-8b91-b888d2e86ca7"
      },
      {
        "end": "880972da-9141-4e39-b3ac-efc934090e11",
        "start": "28cbbc94-b601-4597-89cb-6ed021497a55"
      },
      {
        "end": "8c33f15c-88d2-486f-9796-0a8de01cb15e",
        "start": "417a8df5-2d1f-4cb5-b560-fe538be86ff7"
      }
    ],
    "rtmode_interval": 51014168,
    "rtmode_sleep_interval": 51014168,
    "spmode_interval": 51014168,
    "work_period": {
      "end": "7974ae09-5c7e-4b54-a8ae-9455254d66e1",
      "start": "2be201a0-831d-42f4-a5f0-9a9e6f7a1912"
    }
  },
  "mode_id": 51014168,
  "mode_list": [
    {
      "mid": 51014168,
      "msg": "1bc0cb89-2ac8-481e-9a36-a5832dec608f",
      "name": "ecdbf663-cd95-428c-8fd4-93eef9bea0f7",
      "param": "72cfd068-851f-4078-817d-0efe891d79a9",
      "uid": 51014168
    },
    {
      "mid": 51014168,
      "msg": "68be2dfa-6cf4-4222-9fef-ec348724cbe2",
      "name": "8ae3be7d-bffc-43f1-9b41-4bfd302a9b9c",
      "param": "dd23beda-cfbe-4fcc-b81e-990432410b1c",
      "uid": 51014168
    },
    {
      "mid": 51014168,
      "msg": "e0174937-930f-480a-a3a2-df9829966244",
      "name": "b825967b-feea-4955-bc78-8fb5b4cc20db",
      "param": "321c0253-31f7-4692-a535-8c281252e90c",
      "uid": 51014168
    },
    {
      "mid": 51014168,
      "msg": "61eaeb3b-08a7-4a87-b6cb-1b0f9ad81ba6",
      "name": "6c618c96-938c-4404-b0dc-d9eef4b381bc",
      "param": "d0d265df-5e38-4d12-a059-64fce17b0385",
      "uid": 51014168
    }
  ],
  "mode_type": 51014168,
  "smode_value": "cd150cee-2284-40d0-90cd-73334b899167"
}
```

<a id="loc-SetRtlsMode"></a>
## 设置定位上传模式 (`SetRtlsMode`)

- 接口 ID：`loc.SetRtlsMode`
- 模块：`loc`
- 用途：设置定位上传模式
- 原始位置：`source/模式(loc).txt` 第 278 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，设置的模式需要设备版本支持；
2，设置上传模式定时范围（10~120）， 定距（200，1000）；
3，需要修改设备信息权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SetRtlsMode |
| `module` | `string` | 模块名,loc |
| `params` | `object` | 参数列表 |
| `params.rtls_mode` | `string` | 定位模式<br>e_rtls_time 定时<br>e_rtls_space 定距 |
| `params.rtls_value` | `number` | 定时-10s~120s 定距-200m~1000m |
| `params.simei` | `string` | 设备号,如果设备号登入，可以不用填写 |

### 请求示例

```json
{
  "module": "loc",
  "func": "SetRtlsMode",
  "params": {
    "rtls_mode": "e_rtls_time|e_rtls_space",
    "rtls_value": 1607277190,
    "simei": "55028c6d-b345-4c19-8e6d-0b4858443d71"
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
  "error_message": "45a2abf1-6a64-46f3-837b-5ea4071a9e02"
}
```

<a id="loc-AddLoopLoc"></a>
## 添加周期定位 (`AddLoopLoc`)

- 接口 ID：`loc.AddLoopLoc`
- 模块：`loc`
- 用途：添加周期定位
- 原始位置：`source/模式(loc).txt` 第 326 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，设置该模式需要设备版本支持；
2，添加的周期定位不能超过4个；
3，周期定位名称只支持数字字母中文且长度不超过20；
4，周期定位星期不能为负数；
5，需要修改设备信息权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,AddLoopLoc |
| `module` | `string` | 模块名,loc |
| `params` | `object` | 参数列表 |
| `params.item` | `object` | 周期定位信息列表 |
| `params.item.loc_switch` | `boolean` | 周期定位开关 |
| `params.item.loc_value` | `string` | 周期定位数值 |
| `params.item.loop_loc_name` | `string` | 周期定位名称 |
| `params.item.rp_loop_loc` | `array` | 周期定位星期 |
| `params.simei` | `string` | 设备号,如果设备号登入，可以不用填写 |

### 请求示例

```json
{
  "module": "loc",
  "func": "AddLoopLoc",
  "params": {
    "item": {
      "loc_switch": false,
      "loc_value": "54c5530b-4b07-4c86-b57d-fd5e6fe502dd",
      "loop_loc_name": "cdd3ea61-de3e-455b-a406-9f8b023def24",
      "rp_loop_loc": [
        849345171
      ]
    },
    "simei": "ee4555ff-a287-4bd2-b404-e20dca771998"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `lid` | `string` | 成功添加之后，返回周期定位id |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "e0880cb5-97e0-4b84-a444-e325e4266536",
  "lid": "ccd13b1d-ef5c-4b24-a00c-41497acdce1f"
}
```

<a id="loc-UpdateLoopLoc"></a>
## 修改周期定位 (`UpdateLoopLoc`)

- 接口 ID：`loc.UpdateLoopLoc`
- 模块：`loc`
- 用途：修改周期定位
- 原始位置：`source/模式(loc).txt` 第 389 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，设置该模式需要设备版本支持；
2，周期定位名称只支持数字字母中文且长度不超过20；
3,周期定位星期不能为负数；
4，设备在周期定位模式下会保留一个开启的周期定位会导致无法修改；
5，需要修改设备信息权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,UpdateLoopLoc |
| `module` | `string` | 模块名,loc |
| `params` | `object` | 参数列表 |
| `params.item` | `object` | 周期定位信息列表 |
| `params.item.loc_switch` | `boolean` | 周期定位开关 |
| `params.item.loc_value` | `string` | 周期定位数值 |
| `params.item.loop_loc_name` | `string` | 周期定位名称 |
| `params.item.rp_loop_loc` | `array` | 周期定位星期 |
| `params.lid` | `string` | 周期定位id |

### 请求示例

```json
{
  "module": "loc",
  "func": "UpdateLoopLoc",
  "params": {
    "item": {
      "loc_switch": true,
      "loc_value": "17974f6a-9d5a-4967-ab66-eadad78adff1",
      "loop_loc_name": "9f27d56d-28f1-46f6-a19a-7eac5f696994",
      "rp_loop_loc": [
        1948469914,
        1948469914,
        1948469914
      ]
    },
    "lid": "4d1a9ce3-edcd-493b-8c57-f2c776c2710f"
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
  "error_message": "151c694f-0052-4473-bc4f-887ef0b4d942"
}
```

<a id="loc-GetLoopLoc"></a>
## 获取周期定位 (`GetLoopLoc`)

- 接口 ID：`loc.GetLoopLoc`
- 模块：`loc`
- 用途：获取周期定位
- 原始位置：`source/模式(loc).txt` 第 451 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetLoopLoc |
| `module` | `string` | 模块名,loc |
| `params` | `object` | 参数列表 |
| `params.last_slid` | `string` | 瀑布流上一条数据id,非必填,默认从第一条数据开始拉取 |
| `params.limit_size` | `number` | 显示条数,非必填，默认20,最大1000 |
| `params.simei` | `string` | 设备号,如果设备号登入，可以不用填写 |

### 请求示例

```json
{
  "module": "loc",
  "func": "GetLoopLoc",
  "params": {
    "last_slid": "2b96e70c-069a-45ea-8ba8-39a31d653214",
    "limit_size": 1663941945,
    "simei": "59417b45-a5e9-45ec-bea4-9d04f0b0ca62"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` | 周期定位信息列表 |
| `items.info` | `object` | 周期定位信息 |
| `items.info.loc_switch` | `boolean` | 周期定位开关 |
| `items.info.loc_value` | `string` | 周期定位数值 |
| `items.info.loop_loc_name` | `string` | 周期定位名称 |
| `items.info.rp_loop_loc` | `array` | 周期定位星期 |
| `items.lid` | `string` | 周期定位id |
| `total` | `number` | 符合条件的总条数 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "7af95d50-ab3c-4a0f-bd72-0fb8e6692a5e",
  "items": [
    {
      "info": {
        "loc_switch": true,
        "loc_value": "982da7f6-b017-4683-8e1a-ec05872b916c",
        "loop_loc_name": "afcc64cc-b753-4291-8b16-13c7002ce3c2",
        "rp_loop_loc": [
          753269006,
          753269006,
          753269006
        ]
      },
      "lid": "10a0c768-bf38-43a2-a8e3-a3631da3a768"
    },
    {
      "info": {
        "loc_switch": true,
        "loc_value": "0f27bb36-2886-47d5-a732-80ef90ed3fd6",
        "loop_loc_name": "0a601cad-313c-4ad8-9b98-cd3b8baee64e",
        "rp_loop_loc": [
          753269006,
          753269006
        ]
      },
      "lid": "29f3ab2d-b828-4e41-90b3-d5d64443c03a"
    },
    {
      "info": {
        "loc_switch": true,
        "loc_value": "3e907e56-5fb2-46c3-8f35-cad27069296a",
        "loop_loc_name": "54206b38-6d55-4420-8c8c-243608a0aa2f",
        "rp_loop_loc": [
          753269006,
          753269006,
          753269006,
          753269006
        ]
      },
      "lid": "187aeafc-8afc-4d13-88a8-0e818f7b179a"
    }
  ],
  "total": 753269006
}
```

<a id="loc-DelLoopLoc"></a>
## 删除周期定位 (`DelLoopLoc`)

- 接口 ID：`loc.DelLoopLoc`
- 模块：`loc`
- 用途：删除周期定位
- 原始位置：`source/模式(loc).txt` 第 550 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，设置该模式需要设备版本支持；
2，设备在周期定位模式下会保留一个开启的周期定位会导致无法删除；
3，需要修改设备信息权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,DelLoopLoc |
| `module` | `string` | 模块名,loc |
| `params` | `object` | 参数列表 |
| `params.lid` | `string` | 周期定位id |

### 请求示例

```json
{
  "module": "loc",
  "func": "DelLoopLoc",
  "params": {
    "lid": "eecd6423-d919-4407-aca4-6bcc64827771"
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
  "error_message": "9ec8e254-30da-401a-8f2a-bca9ab065e00"
}
```

<a id="loc-GetRtlsMode"></a>
## 获取定位上传模式 (`GetRtlsMode`)

- 接口 ID：`loc.GetRtlsMode`
- 模块：`loc`
- 用途：获取定位上传模式
- 原始位置：`source/模式(loc).txt` 第 590 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetRtlsMode |
| `module` | `string` | 模块名,loc |
| `params` | `object` | 参数列表 |
| `params.simei` | `string` | 设备号,如果设备号登入，可以不用填写 |

### 请求示例

```json
{
  "module": "loc",
  "func": "GetRtlsMode",
  "params": {
    "simei": "0c6282dd-aea4-4e13-bd89-be8c1ee15c63"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `is_locspace` | `number` | 是否是实时定位的定距模式, 0不支持,1支持，2只读 |
| `is_realtimeposition` | `number` | 是否是实时定位的定时模式, 0不支持,1支持，2只读 |
| `rtls_mode` | `string` | 定位模式<br>e_rtls_time 定时<br>e_rtls_space 定距 |
| `rtls_value` | `number` | 0-10s~120s 1-200m~1000m |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "7a2504d2-c7ad-46aa-91d7-dd2056fa270b",
  "is_locspace": 1867736378,
  "is_realtimeposition": 1867736378,
  "rtls_mode": "e_rtls_time|e_rtls_space",
  "rtls_value": 1867736378
}
```

<a id="loc-SetPosInt"></a>
## 设置定位时间间隔 (`SetPosInt`)

- 接口 ID：`loc.SetPosInt`
- 模块：`loc`
- 用途：设置定位时间间隔
- 原始位置：`source/模式(loc).txt` 第 639 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SetPosInt |
| `module` | `string` | 模块名,loc |
| `params` | `object` | 参数列表 |
| `params.pos_interval` | `number` | 定位时间间隔 |
| `params.simei` | `string` | 设备号,如果设备号登入，可以不用填写 |

### 请求示例

```json
{
  "module": "loc",
  "func": "SetPosInt",
  "params": {
    "pos_interval": 1412178901,
    "simei": "82c3ee32-dce7-4033-babb-5060821a5b78"
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
  "error_message": "0e6ff158-26ce-4843-8a92-8f8ea49069a2"
}
```

<a id="loc-BatchModifyLocMode"></a>
## 批量修改多选模式 (`BatchModifyLocMode`)

- 接口 ID：`loc.BatchModifyLocMode`
- 模块：`loc`
- 用途：批量修改多选模式
- 原始位置：`source/模式(loc).txt` 第 677 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,BatchModifyLocMode |
| `module` | `string` | 模块名,loc |
| `params` | `object` | 参数列表 |
| `params.mode_id` | `number` | 定位模式 |
| `params.mode_type` | `number` | 数值,比如省电模式下的时间,飞行模式下的开关：0-关闭，1-开启 |
| `params.simei` | `array` | 设备号 |
| `params.smode_value` | `string` | 时间 格式 HH:MM 飞行模式定位间隔 |
| `params.version` | `string` | 版本号 |

### 请求示例

```json
{
  "module": "loc",
  "func": "BatchModifyLocMode",
  "params": {
    "mode_id": 667155840,
    "mode_type": 667155840,
    "simei": [
      "700c0dc4-d993-4018-981d-6c31e13bbe40",
      "7640b1d7-3c82-48a1-ad26-e06a4f04786f"
    ],
    "smode_value": "cdb4aeac-c929-4d05-89b5-b8db26f084a4",
    "version": "4f230c4e-17cc-48da-a724-a4e7f5eae850"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 有操作失败的imei号，通过这个字段返回 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |
| `fail_items.simei` | `string` | 错误的设备号 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "c73e4563-1348-4e89-bd60-2d51229ad153",
  "fail_items": [
    {
      "error_message": "42fa5c3c-c18a-492d-a6f4-99a41a18643b",
      "imei": 214053313,
      "simei": "d05ba1fd-1a53-4d3e-8ca9-2bc5cb40ebec"
    },
    {
      "error_message": "9384650b-b66e-4608-8e39-b1342f2bdfc6",
      "imei": 214053313,
      "simei": "a9ca6bed-2893-4da6-83f9-f74740112f69"
    },
    {
      "error_message": "3a509f79-5271-4b27-99ef-57f901a7b496",
      "imei": 214053313,
      "simei": "3ef809b2-6c44-433c-95fa-49ba241bbe85"
    },
    {
      "error_message": "362f9c8e-e149-4e04-af46-09d3e5d16d56",
      "imei": 214053313,
      "simei": "8fbf3c69-0c31-467e-92a3-0b968ec7273a"
    }
  ]
}
```

<a id="loc-BatchModifyRtlsMode"></a>
## 批量修改定时定距模式 (`BatchModifyRtlsMode`)

- 接口 ID：`loc.BatchModifyRtlsMode`
- 模块：`loc`
- 用途：批量修改定时定距模式
- 原始位置：`source/模式(loc).txt` 第 757 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,BatchModifyRtlsMode |
| `module` | `string` | 模块名,loc |
| `params` | `object` | 参数列表 |
| `params.rtls_mode` | `string` | 定位模式<br>e_rtls_time 定时<br>e_rtls_space 定距 |
| `params.rtls_value` | `number` | 定时-10s~120s 定距-200m~1000m |
| `params.simei` | `array` | 设备号 |
| `params.version` | `string` | 版本号 |

### 请求示例

```json
{
  "module": "loc",
  "func": "BatchModifyRtlsMode",
  "params": {
    "rtls_mode": "e_rtls_time|e_rtls_space",
    "rtls_value": 382153472,
    "simei": [
      "31a1ae80-5a19-43c6-9665-ace4d1dea0a8"
    ],
    "version": "1f16e80e-55cc-466b-89cb-bae015882e51"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 有操作失败的imei号，通过这个字段返回 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |
| `fail_items.simei` | `string` | 错误的设备号 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "9ad5c540-9738-4fe3-a3b5-a43a89cf9369",
  "fail_items": [
    {
      "error_message": "87d93d30-c1bb-4156-b274-8781538d3445",
      "imei": 1016819287,
      "simei": "0d63279b-b7cc-45ce-b5ac-111ae4f17735"
    },
    {
      "error_message": "6cd2bf70-a76c-4394-be5c-079aa56e7f0a",
      "imei": 1016819287,
      "simei": "93ea4f3e-0e4d-4ec6-9193-e60fef0cf9e2"
    },
    {
      "error_message": "f57146a6-5518-4233-9d08-46c6eb84f80f",
      "imei": 1016819287,
      "simei": "784f323e-3ec3-4ae5-9d11-c212cd52b876"
    }
  ]
}
```

<a id="loc-BatchModifyAlarmMode"></a>
## 批量修改闹钟模式 (`BatchModifyAlarmMode`)

- 接口 ID：`loc.BatchModifyAlarmMode`
- 模块：`loc`
- 用途：批量修改闹钟模式
- 原始位置：`source/模式(loc).txt` 第 830 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,BatchModifyAlarmMode |
| `module` | `string` | 模块名,loc |
| `params` | `object` | 参数列表 |
| `params.items` | `array` | 闹钟信息列表 |
| `params.items.loc_switch` | `boolean` | 周期定位开关 |
| `params.items.loc_value` | `string` | 周期定位数值 |
| `params.items.loop_loc_name` | `string` | 周期定位名称 |
| `params.items.rp_loop_loc` | `array` | 周期定位星期 |
| `params.simei` | `array` | 设备号 |
| `params.version` | `string` | 版本号 |

### 请求示例

```json
{
  "module": "loc",
  "func": "BatchModifyAlarmMode",
  "params": {
    "items": [
      {
        "loc_switch": true,
        "loc_value": "3f5977a8-1ae6-419c-8f91-a31794e79205",
        "loop_loc_name": "dca0eccc-2ec8-4de8-aab1-2b81ef18ff00",
        "rp_loop_loc": [
          1627353906,
          1627353906,
          1627353906,
          1627353906
        ]
      },
      {
        "loc_switch": true,
        "loc_value": "42f11431-31c8-45a0-b695-913240b71a62",
        "loop_loc_name": "d59e7c96-aeba-4054-835e-3f045ba24b93",
        "rp_loop_loc": [
          1627353906,
          1627353906,
          1627353906,
          1627353906
        ]
      },
      {
        "loc_switch": true,
        "loc_value": "4849c8b5-d533-4eec-b49b-709a6d3079f3",
        "loop_loc_name": "b1342ba8-b63a-49ad-be0e-efa70b799ed4",
        "rp_loop_loc": [
          1627353906
        ]
      },
      {
        "loc_switch": true,
        "loc_value": "ef6ca38a-8a38-44e2-925e-251154a60552",
        "loop_loc_name": "94eaa348-9278-4700-af34-7bcdf558b6a7",
        "rp_loop_loc": [
          1627353906,
          1627353906,
          1627353906,
          1627353906
        ]
      }
    ],
    "simei": [
      "fdb910ac-6734-487d-ad7f-c4af97136028",
      "4501d00b-db3c-4d99-9991-73957bd01262",
      "3402c683-afc6-4336-9105-f4e53e50d0bc"
    ],
    "version": "893ea17b-600a-421f-89b6-b81dfa81ed72"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 有操作失败的imei号，通过这个字段返回 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |
| `fail_items.simei` | `string` | 错误的设备号 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "f22e45d8-e5dd-4a14-a3f0-1e6ed4e1f857",
  "fail_items": [
    {
      "error_message": "518ea6be-f8db-4cc8-b73f-df3cc29f4d1f",
      "imei": 875962884,
      "simei": "48456c16-dffd-48c2-83ca-05b860d89b3d"
    },
    {
      "error_message": "06c87c2f-4de9-43f8-9850-f4a72fca043a",
      "imei": 875962884,
      "simei": "2c437099-a4c1-4c87-b4ea-9852c939aacf"
    },
    {
      "error_message": "600a3c4f-48d7-48bb-8b40-2d0a68fae42b",
      "imei": 875962884,
      "simei": "2e2afb08-e175-4c6a-9b7d-fd833803e149"
    }
  ]
}
```
