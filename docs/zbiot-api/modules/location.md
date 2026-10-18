# 设备轨迹 (Location)

- 模块标识：`location`
- 接口数量：9
- 原始文档：`source/轨迹(location).txt`
- 正式环境：`http://cciot.cc/mapi`
- 测试环境：`http://csapp.cciot.cc/mapi`
- 开发环境：`http://192.168.1.105/mapi`
- 模块负责人：fushengyou,hexiaoqin,hangjl,fange

## 接口索引

| 接口 | 名称 | 请求字段数 | 返回字段数 |
| --- | --- | ---: | ---: |
| [`QueryLocation`](#location-QueryLocation) | 查询轨迹 | 8 | 17 |
| [`Tracking`](#location-Tracking) | 实时追踪 | 7 | 2 |
| [`QueryTrackDate`](#location-QueryTrackDate) | 获取有轨迹的日期 | 6 | 6 |
| [`Distance`](#location-Distance) | 里程统计 | 11 | 19 |
| [`PPoint`](#location-PPoint) | 停留点详情 | 10 | 23 |
| [`Speed`](#location-Speed) | 超速统计 | 10 | 21 |
| [`PDistance`](#location-PDistance) | 行程统计 | 9 | 26 |
| [`PPointSummary`](#location-PPointSummary) | 停留统计报表 | 6 | 27 |
| [`RegionSearch`](#location-RegionSearch) | 区域搜索 | 9 | 15 |

## 接口详情

<a id="location-QueryLocation"></a>
## 查询轨迹 (`QueryLocation`)

- 接口 ID：`location.QueryLocation`
- 模块：`location`
- 用途：查询轨迹
- 原始位置：`source/轨迹(location).txt` 第 29 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，必须要有开始时间和结束时间且大于0；
2，设备需要历史轨迹权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,QueryLocation |
| `module` | `string` | 模块名,location |
| `params` | `object` | 参数列表 |
| `params.last_time` | `number` | 上次查询的最后时间(用于分页，首次请求不存在分页不需要填写，分页详情请参考 &lt;&lt;前言&gt;&gt; 分页)（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `params.limit_size` | `number` | 限制获取数量,最多2000 |
| `params.simei` | `string` | 加密后设备号,设备号登录可以不用传，其他查询操作必须填 |
| `params.time_begin` | `number` | 查询范围中设定的轨迹开始时间,必填（开始时间必须小于结束时间 time_begin &lt; time_end） |
| `params.time_end` | `number` | 查询范围中设定的轨迹结束时间（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数），如果不填那么默认结束时间为当前的操作时间（结束时间必须大于开始时间 time_begin &lt; time_end） |

### 请求示例

```json
{
  "module": "location",
  "func": "QueryLocation",
  "params": {
    "last_time": 938957722,
    "limit_size": 938957722,
    "simei": "3cfea25e-8580-4771-91da-aba1392dbcd3",
    "time_begin": 938957722,
    "time_end": 938957722
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `data` | `array` | 轨迹信息 |
| `data.direction` | `number` | 方向 |
| `data.distance` | `number` | 分段里程(当前p点跟上一个p点之间的里程) |
| `data.duration` | `number` | 停留时长（停留结束时间 - 停留结束开始时间：end_time - start_time） |
| `data.end_time` | `number` | 设备结束停留时间点，（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `data.lat` | `number` | 纬度:火星坐标已弃用 |
| `data.lon` | `number` | 经度:火星坐标已弃用 |
| `data.ptype` | `number` | 是否为p点,0不是，1是 |
| `data.ptype_all` | `number` | 只有is_all参数为true时返回(is_all参数是只有拥有权限才可以设置)，返回点的过滤情况.1:p点,2为展示点,其他：被过滤的点 |
| `data.speed` | `number` | 速度 |
| `data.start_time` | `number` | 设备开始停留时间点,（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `data.time` | `number` | 时间戳，用于下次查询分页使用的时间戳（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `data.type` | `number` | 所用的定位方式,0基站上报,1动态gps上报,2wifi上报,3静态基站点上报,4静态gps点上报,5静态wifi上报 |
| `data.wgs` | `string` | 存储WGS84坐标系,例如:"22.550413,114.096219" |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `is_finish` | `boolean` | 是否加载完毕,true加载完毕,false未加载完 |

### 返回示例

```json
{
  "errcode": 0,
  "data": [
    {
      "direction": 29863228,
      "distance": 29863228,
      "duration": 29863228,
      "end_time": 29863228,
      "lat": 29863228,
      "lon": 29863228,
      "ptype": 29863228,
      "ptype_all": 29863228,
      "speed": 29863228,
      "start_time": 29863228,
      "time": 29863228,
      "type": 29863228,
      "wgs": "6b1b31a5-a4db-4ade-9bc7-4617622eb6ff"
    },
    {
      "direction": 29863228,
      "distance": 29863228,
      "duration": 29863228,
      "end_time": 29863228,
      "lat": 29863228,
      "lon": 29863228,
      "ptype": 29863228,
      "ptype_all": 29863228,
      "speed": 29863228,
      "start_time": 29863228,
      "time": 29863228,
      "type": 29863228,
      "wgs": "76f32a9a-9c02-4028-b9d8-98057b673d88"
    }
  ],
  "error_message": "4925ea85-2de7-4afa-ae1d-967c45ff53b1",
  "is_finish": true
}
```

<a id="location-Tracking"></a>
## 实时追踪 (`Tracking`)

- 接口 ID：`location.Tracking`
- 模块：`location`
- 用途：实时追踪
- 原始位置：`source/轨迹(location).txt` 第 143 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，时间间隔范围（0-65535）；
2，位置跟踪有效期（1-1440）；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Tracking |
| `module` | `string` | 模块名,location |
| `params` | `object` | 参数列表 |
| `params.effective_time` | `number` | 位置跟踪有效期，单位min |
| `params.interval_time` | `number` | 跟踪控制的设备上报时间间隔 0-取消追踪，单位s |
| `params.is_add_log` | `boolean` | 由于会循环调用，这里在首次进入页面的时候，设置为true，后面就设置为false，避免产生多条操作日志 |
| `params.simei` | `string` | 加密后设备号,设备号登录可以不用传 |

### 请求示例

```json
{
  "module": "location",
  "func": "Tracking",
  "params": {
    "effective_time": 1261922899,
    "interval_time": 1261922899,
    "is_add_log": false,
    "simei": "bb7a7977-eedb-4f80-86bc-dcb4ab91a7bb"
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
  "error_message": "9144d619-9dc1-4c19-b8e0-7fa9ebf25f4b"
}
```

<a id="location-QueryTrackDate"></a>
## 获取有轨迹的日期 (`QueryTrackDate`)

- 接口 ID：`location.QueryTrackDate`
- 模块：`location`
- 用途：获取有轨迹的日期
- 原始位置：`source/轨迹(location).txt` 第 191 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，如果有开始时间或结束时间需要大于0；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,QueryTrackDate |
| `module` | `string` | 模块名,location |
| `params` | `object` | 参数列表 |
| `params.date_begin` | `number` | 设定查询指定时间段的开始时间，不可以为0，（10位长的时间戳）开始时间必须小于结束时间（date_begin &lt; date_end） |
| `params.date_end` | `number` | 设定查询指定时间段的结束时间.不可以为0,（10位长的时间戳）开始时间必须小于结束时间（date_begin &lt; date_end） |
| `params.simei` | `string` | 加密后的设备号,设备号登录可以不用传，其他查询操作必填 |

### 请求示例

```json
{
  "module": "location",
  "func": "QueryTrackDate",
  "params": {
    "date_begin": 1118234208,
    "date_end": 1118234208,
    "simei": "2b3ce372-5bb5-4633-bb7d-8a8254caab43"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `date` | `array` | 设备有轨迹的日期集合,包括所有有日里程的日期 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` | 设备有日里程的数据集合 |
| `items.date` | `number` | 日期 |
| `items.distance` | `number` | 日里程数 |

### 返回示例

```json
{
  "errcode": 0,
  "date": [
    508422888
  ],
  "error_message": "d6f0c089-649d-48d4-a3b7-0e7449e0d717",
  "items": [
    {
      "date": 508422888,
      "distance": 508422888
    },
    {
      "date": 508422888,
      "distance": 508422888
    },
    {
      "date": 508422888,
      "distance": 508422888
    },
    {
      "date": 508422888,
      "distance": 508422888
    }
  ]
}
```

<a id="location-Distance"></a>
## 里程统计 (`Distance`)

- 接口 ID：`location.Distance`
- 模块：`location`
- 用途：里程统计
- 原始位置：`source/轨迹(location).txt` 第 264 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Distance |
| `module` | `string` | 模块名,location |
| `params` | `object` | 参数列表 |
| `params.last_sgid` | `string` | 上一页的最后一项数据的sgid(可用于分页，分页详情请参考 &lt;&lt;前言&gt;&gt; 分页) |
| `params.last_simei` | `string` | 上一页的最后一项数据的simei(可用于分页，分页详情请参考 &lt;&lt;前言&gt;&gt; 分页) |
| `params.limit_size` | `number` | 限制获取数量，默认50条,不能超过1000条 |
| `params.sfamily` | `string` | 需要查询的组织id,账号登录字段sgid,simei,sfamilyid三选一，设备号登录不用填写 |
| `params.sgid` | `string` | 需要获取统计的车组,账号登录字段sgid,simei,sfamilyid三选一，设备号登录不用填写 |
| `params.simei` | `array` | 需要查询统计的设备id,不能超过1000条,账号登录字段sgid,simei,sfamilyid三选一，设备号登录不用填写 |
| `params.time_begin` | `number` | 起始时间戳,（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数）开始时间必须小于结束时间（time_begin &lt; time_end） |
| `params.time_end` | `number` | 结束时间戳,（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数）如果为0，表示服务器当前的时间.开始时间必须小于结束时间（time_begin &lt; time_end） |

### 请求示例

```json
{
  "module": "location",
  "func": "Distance",
  "params": {
    "last_sgid": "5fc523d1-6456-4d7a-b204-6bd927a012c6",
    "last_simei": "2147c4de-59d0-476c-926f-1dcb79cdfe4f",
    "limit_size": 2049680079,
    "sfamily": "a6973d1e-bc6a-4bd5-9744-6cd938cb6393",
    "sgid": "4bcd05e3-466a-44be-a740-b0c5353e4913",
    "simei": [
      "d0bb6e22-3150-498a-9a74-57cf1e3a76b4",
      "b87ab105-6948-48f7-a85b-59fffb7a82a1",
      "0774dabf-cc3d-47a9-86c4-63e06f757aa5",
      "ba66a0c9-c5b6-499d-8c31-f0ffbe9a1618"
    ],
    "time_begin": 2049680079,
    "time_end": 2049680079
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.bind_familyname` | `string` | 绑定终端组织名称,设备号登录不返回 |
| `items.bind_phone` | `string` | 绑定手机号,设备号登录不返回 |
| `items.bind_sfamily` | `string` | 绑定终端组织id,设备号登录不返回 |
| `items.bind_time` | `number` | 绑定时间（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.car_number` | `string` | 车牌号,设备号登录不返回 |
| `items.distance` | `number` | 行驶里程，单位m |
| `items.familyname` | `string` | 组织名称,设备号登录不返回 |
| `items.groupname` | `string` | 分组名称,设备号登录不返回 |
| `items.imei` | `number` | 车载设备IMEI,仅用于展示 |
| `items.sfamily` | `string` | 组织id,设备号登录不返回 |
| `items.sgid` | `string` | 分组id,设备号登录不返回 |
| `items.simei` | `string` | 设备id,设备号登录不返回 |
| `items.time_begin` | `number` | 起始时间戳，（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.time_end` | `number` | 结束时间戳，（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.version` | `string` | 设备型号,设备号登录不返回 |
| `total` | `number` | 总数,只有在不翻页的时候返回 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "4298934e-06ab-4f03-922c-66ce03f5d8df",
  "items": [
    {
      "bind_familyname": "f756ae6a-476b-48f9-a13c-0fd543ad4391",
      "bind_phone": "c2981ec5-5407-40d1-b966-fc72566e9164",
      "bind_sfamily": "ef86993d-7038-4f94-aff2-f27861971f6a",
      "bind_time": 1918217191,
      "car_number": "1af74d57-8c57-46f1-8fd2-77c5ed6152e9",
      "distance": 1918217191,
      "familyname": "9e1e2cd4-dfa6-48a8-a8bd-d985e47768b3",
      "groupname": "4edad49c-53ac-4271-b2ae-ed01977a39cd",
      "imei": 1918217191,
      "sfamily": "aa8674bc-6d84-43d3-96f4-deb1dd9fd12f",
      "sgid": "c643cead-cc75-4105-a882-3ce31b70f28f",
      "simei": "93ca99b8-bbbc-485d-b231-004aa88a5191",
      "time_begin": 1918217191,
      "time_end": 1918217191,
      "version": "d74be312-f136-4518-89bc-f5deaa2b8905"
    },
    {
      "bind_familyname": "ba1126bb-a29b-4d78-b29d-db1ed3ceadf6",
      "bind_phone": "d9fcad67-a4a8-4215-8add-48af53889197",
      "bind_sfamily": "9d8fd791-fbda-4b3c-b872-3dd0acf1610a",
      "bind_time": 1918217191,
      "car_number": "10333721-d861-4d12-8556-071247cda619",
      "distance": 1918217191,
      "familyname": "7ed8280f-83de-4c01-9405-baebf6edecdf",
      "groupname": "6487ee5a-b4c7-4765-a9b1-ef36f7e5f4b2",
      "imei": 1918217191,
      "sfamily": "f387793d-1ac3-4748-8f4c-b53f2304dacd",
      "sgid": "732f1292-e590-4770-b229-4bbb4912b238",
      "simei": "da60c950-8153-492b-b849-6e527f085d48",
      "time_begin": 1918217191,
      "time_end": 1918217191,
      "version": "06439414-7b26-468d-a60f-6cd1d4a9eb94"
    },
    {
      "bind_familyname": "252e90ee-15a2-43a0-b536-3c8a3e805823",
      "bind_phone": "a78f08bb-9a4d-44cc-9394-79793624a291",
      "bind_sfamily": "5dc9f151-0a57-46a1-a764-046d42bde1f5",
      "bind_time": 1918217191,
      "car_number": "c084b67c-82ec-44bd-918f-f07e49823ff0",
      "distance": 1918217191,
      "familyname": "c7582e05-9fde-4289-99ab-eac609ca1a9c",
      "groupname": "5b86ac2a-39c5-413a-a4f2-0ce5b13bf6fe",
      "imei": 1918217191,
      "sfamily": "7e36d432-2f88-4358-874e-f634a8ffaf1d",
      "sgid": "47390b52-7c9d-4205-ac97-fee0bc6cfc4b",
      "simei": "9634325a-f2c8-4e8d-ad56-67ccf83a7f0f",
      "time_begin": 1918217191,
      "time_end": 1918217191,
      "version": "b19bdc80-4fe5-4e85-9ec9-1c7750b8b01b"
    }
  ],
  "total": 1918217191
}
```

<a id="location-PPoint"></a>
## 停留点详情 (`PPoint`)

- 接口 ID：`location.PPoint`
- 模块：`location`
- 用途：停留点详情
- 原始位置：`source/轨迹(location).txt` 第 413 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,PPoint |
| `module` | `string` | 模块名,location |
| `params` | `object` | 参数列表 |
| `params.last_simei` | `string` | 上一页的最后一项数据的simei(可用于分页，分页详情请参考 &lt;&lt;前言&gt;&gt; 分页) |
| `params.last_time` | `number` | 最后时间（可用于分页，分页详情请参考 &lt;&lt;前言&gt;&gt; 分页）（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `params.limit_size` | `number` | 限制获取数量，默认50条,不能超过1000条 |
| `params.simei` | `array` | 需要查询统计的设备id,不能超过1000条，必填 |
| `params.stop_time` | `number` | 停留时长的阀值 |
| `params.time_begin` | `number` | 起始时间戳,（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数）开始时间必须小于结束时间（time_begin &lt; time_end） |
| `params.time_end` | `number` | 结束时间戳,（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数）如果为0，表示服务器当前的时间.开始时间必须小于结束时间（time_begin &lt; time_end） |

### 请求示例

```json
{
  "module": "location",
  "func": "PPoint",
  "params": {
    "last_simei": "b90ec30e-72e1-49c3-b4db-0d92acb5db94",
    "last_time": 1470234873,
    "limit_size": 1470234873,
    "simei": [
      "5ce6b241-c972-4a0f-84cd-eb95df25d196",
      "360114c5-3037-4ca5-a3cd-9fff28de86a1"
    ],
    "stop_time": 1470234873,
    "time_begin": 1470234873,
    "time_end": 1470234873
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.bind_familyname` | `string` | 绑定终端组织名称,设备号登录不返回 |
| `items.bind_phone` | `string` | 绑定手机号,设备号登录不返回 |
| `items.bind_sfamily` | `string` | 绑定终端组织id,设备号登录不返回 |
| `items.bind_time` | `number` | 绑定时间（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.car_number` | `string` | 车牌号,设备号登录不返回 |
| `items.cos_time` | `number` | 停留时长，单位为秒 |
| `items.distance` | `number` | 分段里程,当前p点跟上一个p点 |
| `items.familyname` | `string` | 组织名称,设备号登录不返回 |
| `items.groupname` | `string` | 分组名称,设备号登录不返回 |
| `items.imei` | `number` | 车载设备IMEI,仅用于展示 |
| `items.lat` | `number` | 纬度:火星坐标已弃用 |
| `items.lon` | `number` | 经度:火星坐标已弃用 |
| `items.sfamily` | `string` | 组织id,设备号登录不返回 |
| `items.sgid` | `string` | 分组id,设备号登录不返回 |
| `items.simei` | `string` | 设备id,设备号登录不返回 |
| `items.start_time` | `number` | 开始停留时间（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.time` | `number` | 时间戳（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.version` | `string` | 设备型号,设备号登录不返回 |
| `items.wgs` | `string` | 轨迹84坐标系位置 |
| `total` | `number` | 总数,只有在不翻页的时候返回 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "48d2bf44-ad17-41f7-8e70-88201ecaa69a",
  "items": [
    {
      "bind_familyname": "672e6ea6-cacc-4633-b677-9c33b2fd455a",
      "bind_phone": "aaca4d93-e600-4c52-9616-25b5d3df429b",
      "bind_sfamily": "17807519-1183-4df5-92e3-7201d8934d59",
      "bind_time": 1319963688,
      "car_number": "ce1226ed-59a5-41b5-9fd1-46257addcdd3",
      "cos_time": 1319963688,
      "distance": 1319963688,
      "familyname": "08143cf7-e049-4a61-b33e-36e2f5f832df",
      "groupname": "61ec20c8-c32d-4d45-8508-27dd46acc750",
      "imei": 1319963688,
      "lat": 1319963688,
      "lon": 1319963688,
      "sfamily": "15c5d3e5-6123-4e34-9921-f49c82b4f42c",
      "sgid": "7333d69b-e208-4a9d-9049-e9720d0d8151",
      "simei": "16546f0d-6256-4da4-8541-8d43b586378e",
      "start_time": 1319963688,
      "time": 1319963688,
      "version": "48e3554d-800d-4108-900b-dc0fc0f082d8",
      "wgs": "bcfcc3e5-ab5d-4f20-bbde-e2d7a4540d2f"
    },
    {
      "bind_familyname": "f26b632f-a134-4f8c-bd06-f8f3e99d90e6",
      "bind_phone": "6bcc0a5a-b4cc-48a1-8d6a-0069979b8dd3",
      "bind_sfamily": "44bd4e7d-9fae-4a80-b5c1-614b7426f061",
      "bind_time": 1319963688,
      "car_number": "2e7e0ba1-e0ca-48e3-aaca-6332e4f31556",
      "cos_time": 1319963688,
      "distance": 1319963688,
      "familyname": "618831ba-3b14-486c-b4a0-405af567818b",
      "groupname": "cf37667b-e96b-405a-875d-da7904b186aa",
      "imei": 1319963688,
      "lat": 1319963688,
      "lon": 1319963688,
      "sfamily": "54ea176a-ad5c-4f4c-85e3-baf089f64af1",
      "sgid": "26d2e417-ef26-4e27-9d3f-c8a64dafdcd6",
      "simei": "b5219bf1-d338-41c3-a5d8-4b786211bbea",
      "start_time": 1319963688,
      "time": 1319963688,
      "version": "8608a605-775e-4d8a-a399-6de1b6f27fe5",
      "wgs": "1ba14c15-ead3-4603-b2f1-39591a12579a"
    },
    {
      "bind_familyname": "f6c3b520-8ff3-4133-9c37-a4ede12a7367",
      "bind_phone": "4ea358e0-8b88-4221-b41d-d2399cda0f8e",
      "bind_sfamily": "184cf0dd-f777-426a-816e-fff499900628",
      "bind_time": 1319963688,
      "car_number": "ff8783b8-d100-492b-8987-1b1bf41eacf8",
      "cos_time": 1319963688,
      "distance": 1319963688,
      "familyname": "4a3deb72-b73b-49ea-bbcb-12ba282f1b09",
      "groupname": "3c8a2758-a8aa-47a0-836d-9283f84676f6",
      "imei": 1319963688,
      "lat": 1319963688,
      "lon": 1319963688,
      "sfamily": "7e667c5a-8893-40b6-9546-1a44311b1f12",
      "sgid": "85941ea6-db4d-4675-a988-46bb18026ad8",
      "simei": "81410ac9-11bd-4b74-8566-013ffb863639",
      "start_time": 1319963688,
      "time": 1319963688,
      "version": "8b212bfc-8664-4f70-a9e6-9f3773cb7d2b",
      "wgs": "9ae8d757-b9bb-4f0c-a79a-9c12e07b56ea"
    },
    {
      "bind_familyname": "1820c85d-a7e2-4a0c-9b70-bd4210c2ebb4",
      "bind_phone": "ec3c4288-5806-4b78-9c5b-5c0964857342",
      "bind_sfamily": "fb94c825-0583-4779-b07b-a2e1238ac20b",
      "bind_time": 1319963688,
      "car_number": "792b0737-d6f0-4919-ac5c-0e6cceaef360",
      "cos_time": 1319963688,
      "distance": 1319963688,
      "familyname": "07e68243-9ddf-45ab-8366-cec5fa09cd73",
      "groupname": "2707c508-8c0b-4f18-8a63-3ecd052e6b8c",
      "imei": 1319963688,
      "lat": 1319963688,
      "lon": 1319963688,
      "sfamily": "a4cd135e-d712-4df5-aa5b-da1f447a49d3",
      "sgid": "5eedc219-2815-4397-acfc-fd7582474cd2",
      "simei": "357e91a6-dc9a-4b1a-abea-b10f275e5e10",
      "start_time": 1319963688,
      "time": 1319963688,
      "version": "eee3bbfb-05b3-41aa-af60-3d31eba351dd",
      "wgs": "59bed3b1-4629-4218-a139-60c4d8c01365"
    }
  ],
  "total": 1319963688
}
```

<a id="location-Speed"></a>
## 超速统计 (`Speed`)

- 接口 ID：`location.Speed`
- 模块：`location`
- 用途：超速统计
- 原始位置：`source/轨迹(location).txt` 第 598 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Speed |
| `module` | `string` | 模块名,location |
| `params` | `object` | 参数列表 |
| `params.last_simei` | `string` | 上一页的最后一项数据的simei（可用于分页，分页详情请参考 &lt;&lt;前言&gt;&gt; 分页） |
| `params.last_time` | `number` | 最后时间（可用于分页，分页详情请参考 &lt;&lt;前言&gt;&gt; 分页）（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `params.limit_size` | `number` | 限制获取数量，默认50条,不能超过1000条 |
| `params.simei` | `array` | 需要查询统计的设备id,不能超过1000条，必填 |
| `params.speed` | `number` | 超速的速度阀值,如果不填或填写0，则以设备自己设置的超速阈值为准 |
| `params.time_begin` | `number` | 起始时间戳,（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数）开始时间必须小于结束时间（time_begin &lt; time_end） |
| `params.time_end` | `number` | 结束时间戳,（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数）如果为0，表示服务器当前的时间。开始时间必须小于结束时间（time_begin &lt; time_end） |

### 请求示例

```json
{
  "module": "location",
  "func": "Speed",
  "params": {
    "last_simei": "2dce0da0-1fc0-423b-9175-ad7938b3bb55",
    "last_time": 561430627,
    "limit_size": 561430627,
    "simei": [
      "a646fb7e-436f-4c0c-8c90-5c1d0f00f10f",
      "129ae5bb-4776-4fb6-8bca-08f7ee628954"
    ],
    "speed": 561430627,
    "time_begin": 561430627,
    "time_end": 561430627
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.bind_familyname` | `string` | 绑定终端组织名称,设备号登录不返回 |
| `items.bind_phone` | `string` | 绑定手机号,设备号登录不返回 |
| `items.bind_sfamily` | `string` | 绑定终端组织id,设备号登录不返回 |
| `items.bind_time` | `number` | 绑定时间（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.car_number` | `string` | 车牌号,设备号登录不返回 |
| `items.familyname` | `string` | 组织名称,设备号登录不返回 |
| `items.groupname` | `string` | 分组名称,设备号登录不返回 |
| `items.imei` | `number` | 车载设备IMEI,仅用于展示 |
| `items.lat` | `number` | 纬度:火星坐标已弃用 |
| `items.lon` | `number` | 经度:火星坐标已弃用 |
| `items.sfamily` | `string` | 组织id,设备号登录不返回 |
| `items.sgid` | `string` | 分组id,设备号登录不返回 |
| `items.simei` | `string` | 设备id,设备号登录不返回 |
| `items.speed` | `number` | 速度 |
| `items.time` | `number` | 时间戳（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.version` | `string` | 设备型号,设备号登录不返回 |
| `items.wgs` | `string` | 轨迹84坐标系位置 |
| `total` | `number` | 总数,只有在不翻页的时候返回 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "ed407c4f-c94e-405e-9a4d-3d8646134eb5",
  "items": [
    {
      "bind_familyname": "140cea34-4002-4283-8737-c76bd2157ad9",
      "bind_phone": "835e3dcb-1a0b-4e42-a6b6-8fd02f1aaba3",
      "bind_sfamily": "330a2d90-2a8c-4156-88c9-70127029d154",
      "bind_time": 419826712,
      "car_number": "f3d17f48-3502-4d4d-b670-c827d87e5a6f",
      "familyname": "197da027-63fb-4104-8b2b-4d085cf5d963",
      "groupname": "b9afec17-6766-4aef-b276-d6e1a2b2948f",
      "imei": 419826712,
      "lat": 419826712,
      "lon": 419826712,
      "sfamily": "ed9570ff-e133-469a-8465-9a64639851f1",
      "sgid": "4eecba90-ec2b-46ed-a875-9a0930aaacdf",
      "simei": "b3d699a6-9d55-4f0b-b25b-61cfe9e14482",
      "speed": 419826712,
      "time": 419826712,
      "version": "2ed054ba-da74-469e-9f46-eff73dfacdf0",
      "wgs": "0995192a-0047-4113-940f-8a8438c6da08"
    },
    {
      "bind_familyname": "ae9b83f2-6ce7-45d9-9e45-ee9beb445243",
      "bind_phone": "eec3c69f-3faf-4c89-b104-150e4b40ff72",
      "bind_sfamily": "10f684c2-32ad-43e6-b573-7d28762338f7",
      "bind_time": 419826712,
      "car_number": "eab6f445-b1ad-4f30-b560-33a03a2379e2",
      "familyname": "dab896e2-dc7a-4958-8f14-b8a29c12dc14",
      "groupname": "da728912-9163-4169-b4ff-7570a1fb2c19",
      "imei": 419826712,
      "lat": 419826712,
      "lon": 419826712,
      "sfamily": "24fc78a2-0ab4-4855-a637-c92828fb7086",
      "sgid": "e3e4cccd-3c7f-46e0-a22a-7df8f2f9c0c5",
      "simei": "242f75e2-892b-46d2-a4f6-19ae9f7a355b",
      "speed": 419826712,
      "time": 419826712,
      "version": "ae56a75f-5e6b-4a4b-b118-1812e5889523",
      "wgs": "d062fb20-0ae6-49b3-8aac-06b2d2b3aec6"
    },
    {
      "bind_familyname": "e892e79e-fbba-4d28-a838-157a7081c303",
      "bind_phone": "d1784523-8447-48c6-bbc4-bc6045476ea0",
      "bind_sfamily": "1ca2ea52-9af2-45db-b556-2b6aee9abac3",
      "bind_time": 419826712,
      "car_number": "e8cd5bb2-3b49-4f54-91ec-f55f34e8ee06",
      "familyname": "7e457870-56ad-451f-9ad1-2483fac7f293",
      "groupname": "a2e3bd1e-ad3f-44ba-9de7-caaaa63f032f",
      "imei": 419826712,
      "lat": 419826712,
      "lon": 419826712,
      "sfamily": "6c98e410-2d98-49ad-9f35-f24edcafc44a",
      "sgid": "2e063078-ba51-4695-aa11-5470ce04846c",
      "simei": "23cbb79a-21e9-4420-9400-9278c677ec66",
      "speed": 419826712,
      "time": 419826712,
      "version": "541d74ef-630e-4bbc-b947-d5ca614b4742",
      "wgs": "baf23c6d-ed8b-439f-bce4-8e9b7edb1f1a"
    },
    {
      "bind_familyname": "107a36e1-768b-4824-a65a-68e25b201641",
      "bind_phone": "95cd3c79-2de6-4fa3-b91d-8b6fa939cc0e",
      "bind_sfamily": "22cfecbc-1158-48c4-9383-eb4f9d1b8547",
      "bind_time": 419826712,
      "car_number": "de4d2781-0b49-41ba-9165-ebbbb22d555f",
      "familyname": "1021c9f9-e5f0-4d86-96b2-025bfd1e77bf",
      "groupname": "1bd92148-d20b-471d-8151-2f58ffdb2745",
      "imei": 419826712,
      "lat": 419826712,
      "lon": 419826712,
      "sfamily": "69ad6d4f-a2f2-4fc1-ab09-941c0061e810",
      "sgid": "ef300d9a-7222-46d7-8634-29b1dd23eaf1",
      "simei": "2ef18233-0e52-4dad-8b2d-04219028ebdd",
      "speed": 419826712,
      "time": 419826712,
      "version": "fe674134-5fef-445b-80e3-7202971c3eb3",
      "wgs": "d8e91510-3c5d-4191-8d94-f7dce82b5e19"
    }
  ],
  "total": 419826712
}
```

<a id="location-PDistance"></a>
## 行程统计 (`PDistance`)

- 接口 ID：`location.PDistance`
- 模块：`location`
- 用途：行程统计
- 原始位置：`source/轨迹(location).txt` 第 771 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,PDistance |
| `module` | `string` | 模块名,location |
| `params` | `object` | 参数列表 |
| `params.last_simei` | `string` | 上一页的最后一项数据的simei（可用于分页，分页详情请参考 &lt;&lt;前言&gt;&gt; 分页） |
| `params.last_start_time` | `number` | 上一次开始时间（可用于分页，分页详情请参考 &lt;&lt;前言&gt;&gt; 分页）（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `params.limit_size` | `number` | 限制获取数量，默认50条,不能超过1000条 |
| `params.simei` | `array` | 需要查询统计的设备id,不能超过1000条，必填 |
| `params.time_begin` | `number` | 起始时间戳，（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数）开始时间必须小于结束时间（time_begin &lt; time_end） |
| `params.time_end` | `number` | 结束时间戳,如果为0，（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数）表示服务器当前的时间。开始时间必须小于结束时间（time_begin &lt; time_end） |

### 请求示例

```json
{
  "module": "location",
  "func": "PDistance",
  "params": {
    "last_simei": "ff0d8cf1-6a35-4bed-a542-945d8494e023",
    "last_start_time": 446801157,
    "limit_size": 446801157,
    "simei": [
      "531aa6ed-299f-44a9-9fd7-0cab7d0ea307"
    ],
    "time_begin": 446801157,
    "time_end": 446801157
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.bind_familyname` | `string` | 绑定终端组织名称,设备号登录不返回 |
| `items.bind_phone` | `string` | 绑定手机号,设备号登录不返回 |
| `items.bind_sfamily` | `string` | 绑定终端组织id,设备号登录不返回 |
| `items.bind_time` | `number` | 绑定时间（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.car_number` | `string` | 车牌号,设备号登录不返回 |
| `items.cos_time` | `number` | 运行时长，单位为秒 |
| `items.distance` | `number` | 分段里程,当前p点跟上一个p点 |
| `items.familyname` | `string` | 组织名称,设备号登录不返回 |
| `items.groupname` | `string` | 分组名称,设备号登录不返回 |
| `items.imei` | `number` | 车载设备IMEI,仅用于展示 |
| `items.sfamily` | `string` | 组织id,设备号登录不返回 |
| `items.sgid` | `string` | 分组id,设备号登录不返回 |
| `items.simei` | `string` | 设备id,设备号登录不返回 |
| `items.start_lat` | `number` | 起始纬度:火星坐标已弃用 |
| `items.start_lon` | `number` | 起始经度:火星坐标已弃用 |
| `items.start_time` | `number` | 开始停留时间（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.start_wgs` | `string` | 起始轨迹84坐标系位置 |
| `items.stop_lat` | `number` | 结束纬度:火星坐标已弃用 |
| `items.stop_lon` | `number` | 结束经度:火星坐标已弃用 |
| `items.stop_time` | `number` | 结束时间（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.stop_wgs` | `string` | 结束轨迹84坐标系位置 |
| `items.version` | `string` | 设备型号,设备号登录不返回 |
| `total` | `number` | 总数,只有在不翻页的时候返回 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "165c7b28-9595-4b59-b667-88b46e417fd7",
  "items": [
    {
      "bind_familyname": "96665659-d766-4122-b155-dd990282c923",
      "bind_phone": "f54d4ad9-1947-47ae-a2d8-0ec8df2ec146",
      "bind_sfamily": "999d14f1-6d47-4751-8a5c-8d7d6c61eeb8",
      "bind_time": 1687252988,
      "car_number": "3c71d854-0194-4129-bf88-6b4e7043ad84",
      "cos_time": 1687252988,
      "distance": 1687252988,
      "familyname": "d58ecd83-1161-44c4-80e7-d90d938fbda8",
      "groupname": "259da8d2-a604-41b3-92fd-dec413ef5541",
      "imei": 1687252988,
      "sfamily": "1ab47fc4-4cb8-49b1-bbf6-2332db2b3640",
      "sgid": "28f009f7-4d31-4ea3-9e78-35a333f1b22d",
      "simei": "2e6f4156-b9b9-4e3a-b18c-f97c49ce72ed",
      "start_lat": 1687252988,
      "start_lon": 1687252988,
      "start_time": 1687252988,
      "start_wgs": "1344e658-ff05-4c5a-b929-b75595b13337",
      "stop_lat": 1687252988,
      "stop_lon": 1687252988,
      "stop_time": 1687252988,
      "stop_wgs": "60043dfd-aff5-4582-bcc8-7706bed45974",
      "version": "be2928ff-a3f3-452a-9539-601abda7d7eb"
    }
  ],
  "total": 1687252988
}
```

<a id="location-PPointSummary"></a>
## 停留统计报表 (`PPointSummary`)

- 接口 ID：`location.PPointSummary`
- 模块：`location`
- 用途：停留统计报表
- 原始位置：`source/轨迹(location).txt` 第 898 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,PPointSummary |
| `module` | `string` | 模块名,location |
| `params` | `object` | 参数列表 |
| `params.simei` | `array` | 需要查询统计的设备id，最多允许10个 |
| `params.time_begin` | `number` | 起始时间戳。（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数）开始时间必须小于结束时间（time_begin &lt; time_end） |
| `params.time_end` | `number` | 结束时间戳,如果为0，（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数）表示服务器当前的时间。开始时间必须小于结束时间（time_begin &lt; time_end） |

### 请求示例

```json
{
  "module": "location",
  "func": "PPointSummary",
  "params": {
    "simei": [
      "58fcc0c6-4984-4793-af9d-232f62f322a3",
      "c6478215-b3ed-4669-ba63-f96aed6ae55f"
    ],
    "time_begin": 316103296,
    "time_end": 316103296
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.bind_familyname` | `string` | 绑定终端组织名称,设备号登录不返回 |
| `items.bind_phone` | `string` | 绑定手机号,设备号登录不返回 |
| `items.bind_sfamily` | `string` | 绑定终端组织id,设备号登录不返回 |
| `items.bind_time` | `number` | 绑定时间，（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.car_number` | `string` | 车牌号,设备号登录不返回 |
| `items.count` | `number` | 次数 |
| `items.familyname` | `string` | 组织名称,设备号登录不返回 |
| `items.groupname` | `string` | 分组名称,设备号登录不返回 |
| `items.imei` | `number` | 车载设备IMEI,仅用于展示 |
| `items.items` | `array` | 详情 |
| `items.items.cos_time` | `number` | 停留耗时长，单位为秒 |
| `items.items.lat` | `number` | 纬度:火星坐标已弃用 |
| `items.items.lon` | `number` | 经度:火星坐标已弃用 |
| `items.items.start_time` | `number` | 开始停留的时间（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.items.time` | `number` | 点上报的时间，（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `items.items.wgs` | `string` | 轨迹84坐标系位置 |
| `items.lat` | `number` | 纬度:火星坐标已弃用 |
| `items.lon` | `number` | 经度:火星坐标已弃用 |
| `items.sfamily` | `string` | 组织id,设备号登录不返回 |
| `items.sgid` | `string` | 分组id,设备号登录不返回 |
| `items.simei` | `string` | 设备id,设备号登录不返回 |
| `items.version` | `string` | 设备型号,设备号登录不返回 |
| `items.wgs` | `string` | 轨迹84坐标系位置 |
| `total` | `number` | 总数,只有在不翻页的时候返回 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "052f8dcc-a18a-4c46-96e7-daac5d41d8d0",
  "items": [
    {
      "bind_familyname": "9e5e6e20-305f-45dc-be36-daf768049349",
      "bind_phone": "9bdff87c-425f-4596-99fe-8a503645d840",
      "bind_sfamily": "e309aaf4-beef-400c-972a-e7030ed96504",
      "bind_time": 1254640364,
      "car_number": "e387b609-4af0-4553-b952-cb47385152c3",
      "count": 1254640364,
      "familyname": "f5ea7419-9208-45e6-9292-bb8d9a1446dc",
      "groupname": "11559ffa-7855-45a0-a8eb-78efb39bee76",
      "imei": 1254640364,
      "items": [
        {
          "cos_time": 1254640364,
          "lat": 1254640364,
          "lon": 1254640364,
          "start_time": 1254640364,
          "time": 1254640364,
          "wgs": "b0d09c63-b899-4c48-87f4-5f14cef4efc4"
        },
        {
          "cos_time": 1254640364,
          "lat": 1254640364,
          "lon": 1254640364,
          "start_time": 1254640364,
          "time": 1254640364,
          "wgs": "55958e6d-5bc6-430f-b326-defb6279944d"
        }
      ],
      "lat": 1254640364,
      "lon": 1254640364,
      "sfamily": "350c00c2-45e9-4b4a-aa2e-7c5c8ea80765",
      "sgid": "14641f4d-37f8-419e-875d-69d82b68a681",
      "simei": "c1228949-253f-4d52-b7fd-bfe521332e08",
      "version": "61c6bbe9-1e2f-49bb-9b55-9ab1e1d6a70e",
      "wgs": "5afdb201-5a05-47fe-9bee-e19c8f732abe"
    },
    {
      "bind_familyname": "54dcc0e6-4db8-4228-8370-b477c248124b",
      "bind_phone": "d0eed25a-8beb-40cf-8478-2dee644edf2a",
      "bind_sfamily": "cf614ed5-a798-44e1-aba4-c5144059a25e",
      "bind_time": 1254640364,
      "car_number": "615a80d4-05ed-4c8b-b1c2-1d03cd38b1da",
      "count": 1254640364,
      "familyname": "9c4952f1-1686-402d-8d28-88f691bab1a6",
      "groupname": "a53c64da-5790-4828-b0a0-e961f70a85a9",
      "imei": 1254640364,
      "items": [
        {
          "cos_time": 1254640364,
          "lat": 1254640364,
          "lon": 1254640364,
          "start_time": 1254640364,
          "time": 1254640364,
          "wgs": "6d291f36-23e4-4960-8a45-53ee3c1ab6a5"
        },
        {
          "cos_time": 1254640364,
          "lat": 1254640364,
          "lon": 1254640364,
          "start_time": 1254640364,
          "time": 1254640364,
          "wgs": "bb924c24-0c4c-4877-9c24-4bb2a038e8a7"
        }
      ],
      "lat": 1254640364,
      "lon": 1254640364,
      "sfamily": "6c61eb6a-3de8-473d-8191-19f812ef9004",
      "sgid": "f4bd0ed1-7ae7-4474-956e-996aa9801ce9",
      "simei": "b372db7d-6142-42f7-99cc-c675b6cd2d76",
      "version": "440a1c6d-1b50-4e21-b631-5aed50bf70bd",
      "wgs": "92c1eb65-1956-4c59-91ef-fce2c15f9b5e"
    },
    {
      "bind_familyname": "0396bdd1-e613-4711-80eb-f635648675f3",
      "bind_phone": "cccff82c-a571-406c-aa05-861731af38cd",
      "bind_sfamily": "db8dfed3-605f-4f18-bcbf-a4ecdbf61828",
      "bind_time": 1254640364,
      "car_number": "c045f671-8554-468b-b398-a5bce4328de6",
      "count": 1254640364,
      "familyname": "8ecb2249-adf9-4f84-bc73-43cb8523873a",
      "groupname": "305c7f56-97af-449a-89ea-e5cce1ccb274",
      "imei": 1254640364,
      "items": [
        {
          "cos_time": 1254640364,
          "lat": 1254640364,
          "lon": 1254640364,
          "start_time": 1254640364,
          "time": 1254640364,
          "wgs": "42d770bb-a0ed-43d8-8a2b-c6291a3e8f50"
        },
        {
          "cos_time": 1254640364,
          "lat": 1254640364,
          "lon": 1254640364,
          "start_time": 1254640364,
          "time": 1254640364,
          "wgs": "65375765-5ec1-4925-a2b9-cad9b2fe8191"
        },
        {
          "cos_time": 1254640364,
          "lat": 1254640364,
          "lon": 1254640364,
          "start_time": 1254640364,
          "time": 1254640364,
          "wgs": "541da7ea-6dd0-4f85-88c2-33b5a2e10242"
        }
      ],
      "lat": 1254640364,
      "lon": 1254640364,
      "sfamily": "22e69c4a-d8d2-4e81-80ff-5c07da56e3d6",
      "sgid": "f37b135a-3120-4ae9-a30b-5739d8ae56f7",
      "simei": "b3d8b793-1780-4a7d-850e-27d8ee3b8548",
      "version": "6a6d1581-6a25-479d-9416-38c616886d7f",
      "wgs": "2f0bc077-be36-41a9-8c77-340d29ee5d6f"
    }
  ],
  "total": 1254640364
}
```

<a id="location-RegionSearch"></a>
## 区域搜索 (`RegionSearch`)

- 接口 ID：`location.RegionSearch`
- 模块：`location`
- 用途：区域搜索
- 原始位置：`source/轨迹(location).txt` 第 1111 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,RegionSearch |
| `module` | `string` | 模块名,location |
| `params` | `object` | 参数列表 |
| `params.height` | `number` | 多边形搜索时填写，搜索高度（单位m） |
| `params.lat` | `number` | 中心经度:84坐标系 |
| `params.lng` | `number` | 中心纬度:84坐标系 |
| `params.radius` | `number` | 圆形搜索时填写，搜索半径（单位m） |
| `params.search_manner` | `string` | 区域搜索方式，圆形还是多边形<br>e_manner_circle<br>e_manner_box |
| `params.width` | `number` | 多边形搜索时填写，搜索长度（单位m） |

### 请求示例

```json
{
  "module": "location",
  "func": "RegionSearch",
  "params": {
    "height": 367282584,
    "lat": 367282584,
    "lng": 367282584,
    "radius": 367282584,
    "search_manner": "e_manner_circle|e_manner_box",
    "width": 367282584
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` | 符合条件设备的详情的字符串表示 |
| `key_desc` | `object` | 详情各列的描述 |
| `key_desc.car_number` | `number` | 车牌号 |
| `key_desc.imei` | `number` | 设备号 |
| `key_desc.lat` | `number` | 纬度:84坐标系 |
| `key_desc.lon` | `number` | 经度:84坐标系 |
| `key_desc.speed` | `number` | 设备的速度 |
| `key_desc.state` | `number` | 设备所处的状态 |
| `key_desc.time` | `number` | 时间（10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数） |
| `offline_cnt` | `number` | 离线总数 |
| `online_cnt` | `number` | 在线总数 |
| `sleep_cnt` | `number` | 睡眠总数 |
| `total_cnt` | `number` | 符合条件的设备总数 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "752d3239-3bac-4955-9e16-5c5812ef0c5d",
  "items": [
    "a9494efe-be87-48d3-a76e-d5630adf3aee",
    "500831b7-b34c-4607-b7d3-2fae7a42a6be",
    "d3ef77a4-33bb-4cf1-b485-c4e7de304941"
  ],
  "key_desc": {
    "car_number": 378473089,
    "imei": 378473089,
    "lat": 378473089,
    "lon": 378473089,
    "speed": 378473089,
    "state": 378473089,
    "time": 378473089
  },
  "offline_cnt": 378473089,
  "online_cnt": 378473089,
  "sleep_cnt": 378473089,
  "total_cnt": 378473089
}
```
