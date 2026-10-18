# 设备管理 (Device)

- 模块标识：`device`
- 接口数量：21
- 原始文档：`source/设备(device).txt`
- 正式环境：`http://cciot.cc/mapi`
- 测试环境：`http://cs.iotiot.cc/mapi`
- 开发环境：`http://192.168.1.20/mapi`
- 模块负责人：fushengyou

## 接口索引

| 接口 | 名称 | 请求字段数 | 返回字段数 |
| --- | --- | ---: | ---: |
| [`GetDetail`](#device-GetDetail) | 设备详情信息 | 9 | 84 |
| [`GetConfig`](#device-GetConfig) | 设备的配置开关信息 | 7 | 75 |
| [`Add`](#device-Add) | 添加设备 | 6 | 9 |
| [`SetDetail`](#device-SetDetail) | 修改设备的详细信息 | 22 | 2 |
| [`SetConfig`](#device-SetConfig) | 修改设备的配置信息 | 62 | 7 |
| [`Cmd`](#device-Cmd) | 给设备下发指令 | 6 | 2 |
| [`DelLog`](#device-DelLog) | 删除设备的操作日志 | 5 | 2 |
| [`GetLog`](#device-GetLog) | 获取设备的操作日志 | 13 | 21 |
| [`DelFromFamily`](#device-DelFromFamily) | 将设备从组织中删除 | 7 | 8 |
| [`SetRelaySwitch`](#device-SetRelaySwitch) | 设置继电器开关 | 7 | 2 |
| [`MoveGarage`](#device-MoveGarage) | 设备迁移车组 | 7 | 8 |
| [`DelToParent`](#device-DelToParent) | 删除设备回到上级组织 | 12 | 8 |
| [`Load`](#device-Load) | 导入设备 | 13 | 6 |
| [`ChangeHost`](#device-ChangeHost) | 修改设备的主从host | 7 | 7 |
| [`BatchCmd`](#device-BatchCmd) | 批量下发指令 | 7 | 7 |
| [`UnBindPhone`](#device-UnBindPhone) | 设备解绑电话号码 | 4 | 7 |
| [`FindDev`](#device-FindDev) | 查找设备 | 10 | 14 |
| [`DevVersion`](#device-DevVersion) | 系统支持的设备版本号 | 6 | 7 |
| [`VersionFun`](#device-VersionFun) | 版本号对应的功能 | 4 | 18 |
| [`AddForTerm`](#device-AddForTerm) | 经销商给终端组织添加设备 | 6 | 9 |
| [`BatchSetDetail`](#device-BatchSetDetail) | 批量设置设备信息 | 23 | 7 |

## 接口详情

<a id="device-GetDetail"></a>
## 设备详情信息 (`GetDetail`)

- 接口 ID：`device.GetDetail`
- 模块：`device`
- 用途：设备详情信息
- 原始位置：`source/设备(device).txt` 第 53 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

说明：最后一点的详情last_post是个json串组成的string类型。里面点类型type有如下类型
e_lbs 基站上报
e_gps 动态gps上报
e_wifi wifi上报
e_static_lbs 静态基站点上报
e_static_gps 静态gps点上报
e_static_wifi 静态wifi上报

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetDetail |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.get_additional` | `boolean` | 是否获取设备的附加信息，非必填，默认不获取。如：车主信息，车辆保险信息。 |
| `params.get_increment` | `boolean` | 是否获取该设备支持的全部增值服务，非必填。默认不获取。 |
| `params.get_latest_alarm` | `boolean` | 获取最近的一条告警信息 |
| `params.get_user` | `boolean` | 是否获取绑定用户信息,非必填，默认不获取 |
| `params.is_get_follow` | `boolean` | 是否获取该设备备注信息，非必填。默认不获取。 |
| `params.simei` | `string` | 设备imei,账号登录必填，设备登入可以不填，这是该接口的必要参数 |

### 请求示例

```json
{
  "module": "device",
  "func": "GetDetail",
  "params": {
    "get_additional": true,
    "get_increment": true,
    "get_latest_alarm": true,
    "get_user": true,
    "is_get_follow": true,
    "simei": "bee920fb-7f6d-41cc-bc70-7e5fb7603784"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `acc_power` | `string` | 电源状态acc，充电(e_acc_power_charge)或断电(e_acc_power_interuupt)<br>e_acc_power_charge 充电<br>e_acc_power_interuupt 断电 |
| `acc_state` | `string` | acc状态，<br>e_acc_close 关闭<br>e_acc_open 打开<br>e_acc_empty 为空 |
| `add_account` | `string` | 导入的账户（账号登录时，并且拥有查看的权限e_ua_show_phone才会返回） |
| `alarm_name` | `string` | 告警 |
| `alarm_time` | `number` | 告警时间, 秒 |
| `altitude` | `number` | 海拔，如果是基站和WiFi点上报，没有海拔。只有gps上报才有海拔 |
| `bd_satellite` | `number` | 北斗卫星个数 |
| `bind_account_phone` | `string` | 绑定的终端组织登录账号的电话 |
| `bind_phone` | `string` | 返回的设备的绑定的手机号该手机号和detail.bind_phone一样 |
| `bind_seller_gname` | `string` | 返回的设备绑定销售商账号分组名称，设置了get_user时返回 |
| `bind_seller_name` | `array` | 返回设备的销售商账号名称,设置get_user时返回 |
| `bind_sfamilyid` | `string` | 绑定的终端sfamilyid |
| `bind_time` | `number` | 返回设备的绑定时间(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数) |
| `bind_user_name` | `array` | 返回设备绑定账号的名称,设置get_user时返回 |
| `bluetooth_online` | `boolean` | 是否蓝牙在线 |
| `bt_beacon` | `string` | 蓝牙信标 |
| `center_phone` | `string` | 中心号码，下发指令和接受警告的号码，默认为空 |
| `create_time` | `number` | 注册时间(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数)（账号登录时，并且拥有查看的的权限e_ua_show_phone才会返回） |
| `cur_time` | `number` | 当前时间, 秒 |
| `day_distance` | `number` | 日里程。0表示没有日里程 |
| `detail` | `object` | 详细信息 |
| `detail.bck_phone` | `string` | 用户手机号码,备用手机号码，SetDetail接口user_phone可设置 |
| `detail.car_image` | `number` | 车辆图标 |
| `detail.car_number` | `string` | 车牌号 |
| `detail.car_type` | `string` | 车辆类型 |
| `detail.end_dev_time` | `number` | 设备到期时间,(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数) |
| `detail.engine_num` | `string` | 发动机号 |
| `detail.frame_num` | `string` | 车架号车辆品牌 |
| `detail.img_other` | `string` | 身份证反面 |
| `detail.img_pos` | `string` | 身份证正面 |
| `detail.mode_id` | `number` | 定位模式 |
| `detail.mode_name` | `string` | 定位模式名称 |
| `detail.scar_safe_time` | `string` | 车辆保险 格式如:2018-10-19 |
| `detail.scar_year_check` | `string` | 车辆年检 格式如:2018-10-19 |
| `detail.start_dev_time` | `number` | 设备开通时间,(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数) |
| `detail.user_addr` | `string` | 车主地址 |
| `detail.user_cer` | `string` | 车主证件号码 |
| `detail.user_image` | `number` | 车主形象（图标） |
| `detail.user_mail` | `string` | 车主e_mail |
| `detail.user_name` | `string` | 车主姓名 |
| `detail.user_phone` | `string` | 用户手机号码,将被弃用，请勿使用 |
| `detail.user_sex` | `string` | 车主性别<br>e_sex_man 男<br>e_sex_woman 女 |
| `detail.user_unit` | `string` | 车主工作单位 |
| `down_line_time` | `number` | 最近下线时间(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数)） |
| `enable_time` | `number` | 设备开通时间戳(第一次上线时间)(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数)这是Linux系统上的时间戳，可自行转化为正常时间表示 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `expire` | `boolean` | 设备是否过期，true过期，false没过期 |
| `external_voltage` | `string` | 外电电压 |
| `glonass_satellite` | `number` | 格洛纳斯卫星个数 |
| `gps_satellite` | `number` | gps卫星个数 |
| `history` | `number` | 是否有历史轨迹的能力, 0不支持,1支持 |
| `humility` | `string` | 冷链设备湿度 |
| `iccid` | `string` | 设备上报ICCID， |
| `imei` | `number` | 车载设备IMEI ,用于展示， |
| `increment` | `array` | 增值服务,默认不返回，开启了get_increment之后才返回 |
| `increment_alarm_day` | `number` | 增值服务告警预警天数,get_increment为true时返回，不支持的不返回 |
| `interval` | `number` | 下次调用间隔, 毫秒 |
| `is_human_dev` | `boolean` | true为人员款设备，false为车载款设备 |
| `last_com_time` | `number` | 最后通信时间，(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数) |
| `last_pos` | `string` | 位置信息json格式，火星坐标为(lat,lon)(已弃用),84坐标系为("22.641872,114.033431") 如:"{\\"addr\\":\\"广东省 深圳市 龙华区****\\",\\"lat\\":22641872,\\"lon\\":114033431,\\"ptype\\":\\"e_location_nomal\\",\\"time\\":1654660669,\\"type\\":\\"e_wifi\\",\\"wgs\\":\\"22.641872,114.033431\\"}" |
| `loc_timezone` | `number` | 时区，设备所在国家的时区, 东区为正数，西区为负数，如东八区8， 西八区-8 |
| `mode_fun` | `string` | 支持的模式，(如：e_mode_invalid)<br>e_mode_invalid 什么模式都不支持<br>e_mode_loc 支持多选的模式<br>e_mode_rtls 支持定时的模式 |
| `nfc_beacon` | `string` | nfc信标 |
| `oil_state` | `string` | 油电状态<br>e_oil_normal 油电正常<br>e_oil_off 油电断开 |
| `power` | `number` | 电量 |
| `priority` | `string` | 定位优先级，用于今后的定位操作 |
| `protocol` | `number` | 设备协议，默认808协议 |
| `protocol_name` | `string` | 设备协议的展示名称，默认808协议 |
| `remarks` | `string` | 设置了is_get_follow时返回 |
| `satellite_type` | `array` | 卫星定位类型<br>e_type_gps gps卫星<br>e_type_beidou 北斗卫星<br>e_type_glonass Glonass卫星 |
| `signa_val` | `number` | 卫星数量 |
| `signal_rate` | `number` | 信号强度百分比值 |
| `start_dev_time` | `number` | 设备开始使用的时间,(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数)大于0 就表示设备已激活,0表示设备没有激活（时间戳） |
| `state` | `string` | 设备状态<br>e_line_down 不在线<br>e_line_on 在线<br>e_line_sleep 睡眠 |
| `state_begin_time` | `number` | 当前状态的起始时间(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数) |
| `state_time` | `number` | 当前状态运行时间,单位位秒（当前时间 - 当前状态的起始时间） |
| `support_wxvoice` | `boolean` | 是否支持微信语音通话 |
| `temper` | `string` | 冷链设备温度 |
| `temperature` | `string` | 四路温度 |
| `up_line_time` | `number` | 最近上线时间(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数) |
| `ver` | `string` | 设备固件型号，(不带版本信息)如：SK3 |
| `version` | `string` | 设备固件版本，默认为1 |
| `wake_time` | `number` | 设备上报的下一次报警时间,有就展示，没有或者为0就不展示 |

### 返回示例

```json
{
  "errcode": 0,
  "acc_power": "e_acc_power_charge|e_acc_power_interuupt",
  "acc_state": "e_acc_close|e_acc_open|e_acc_empty",
  "add_account": "0a0e161f-d7d0-469c-b6f2-4af7767a1e72",
  "alarm_name": "719af3ec-35cf-476e-a47a-da4e72ff642d",
  "alarm_time": 908925107,
  "altitude": 908925107,
  "bd_satellite": 908925107,
  "bind_account_phone": "e970fac2-4f9d-40c7-b02a-6b3f3cc356a6",
  "bind_phone": "afd5f117-378e-44ec-86c1-e7f64b65ba15",
  "bind_seller_gname": "a630a4ed-948c-4fc3-bbfc-2504438cccf7",
  "bind_seller_name": [
    "bd7d4bd3-f016-412e-8b3b-a9986a4d38da",
    "b1284475-11a4-4fe5-9e5b-5877421a239d",
    "c5de0426-c0a1-44d5-9dc8-fa7990d5d748"
  ],
  "bind_sfamilyid": "84ef26ff-9220-4ada-aaa6-765fa6e13101",
  "bind_time": 908925107,
  "bind_user_name": [
    "969dfeec-cf25-498b-8a37-fd2aeb7b34a6"
  ],
  "bluetooth_online": false,
  "bt_beacon": "79fb4150-b3bf-40e4-8606-721ea706025d",
  "center_phone": "3e0ba7ac-b7d4-485e-b2cb-3c458e548458",
  "create_time": 908925107,
  "cur_time": 908925107,
  "day_distance": 908925107,
  "detail": {
    "bck_phone": "fe2ae537-2ddc-43df-bf1c-557fae3d4ffd",
    "car_image": 908925107,
    "car_number": "f64a9bc0-2f38-448a-9e9f-6dd3fd5d01fb",
    "car_type": "b3c1b2c7-7b9e-4e36-9898-81a1f235cc1b",
    "end_dev_time": 908925107,
    "engine_num": "63667eb5-8f54-4de3-a0c9-760e6c04a0d1",
    "frame_num": "c9f0d3d9-24e7-4cf5-8be4-07be0dd2c794",
    "img_other": "042471b7-d504-481d-8443-71cb1934d68b",
    "img_pos": "c5602565-e735-4192-9c08-698724bd7ac0",
    "mode_id": 908925107,
    "mode_name": "592a3e8a-09ae-41ff-a1e3-6a9d1afe0420",
    "scar_safe_time": "782423a3-1c55-406b-95d5-3d2005b4ec19",
    "scar_year_check": "269d430a-634d-4f1f-8e7f-e52d5d4b6263",
    "start_dev_time": 908925107,
    "user_addr": "63d48524-9f44-4b27-b6de-93bc6108049d",
    "user_cer": "acfa407f-467f-4d1e-a8be-2505214af837",
    "user_image": 908925107,
    "user_mail": "da7ec03b-cca8-4558-9d3a-9242f2180472",
    "user_name": "a060350f-9de7-4dfe-a506-1d8f73afd850",
    "user_phone": "23a13373-ea7c-4606-aded-508f7437d994",
    "user_sex": "e_sex_man|e_sex_woman",
    "user_unit": "f32d2de6-6893-42f6-bd74-05f28bdc517e"
  },
  "down_line_time": 908925107,
  "enable_time": 908925107,
  "error_message": "00af68ca-a5e5-416a-9004-5036f92d4ff6",
  "expire": false,
  "external_voltage": "39c49f2a-0103-477b-905f-a0d950bbc004",
  "glonass_satellite": 908925107,
  "gps_satellite": 908925107,
  "history": 908925107,
  "humility": "98c3e965-8584-46f6-a82e-a6cb9884e5c1",
  "iccid": "165fbad0-f3b7-46e3-9d5a-2fc8573ec4f3",
  "imei": 908925107,
  "increment": [
    908925107,
    908925107
  ],
  "increment_alarm_day": 908925107,
  "interval": 908925107,
  "is_human_dev": false,
  "last_com_time": 908925107,
  "last_pos": "14e3c93c-994a-4ea3-89e0-f98ef17a1704",
  "loc_timezone": 908925107,
  "mode_fun": "e_mode_invalid|e_mode_loc|e_mode_rtls",
  "nfc_beacon": "f2ed8b4e-f692-499f-b53b-0eb35b4aa66b",
  "oil_state": "e_oil_normal|e_oil_off",
  "power": 908925107,
  "priority": "9d68c84a-ede4-4267-aa51-b3a15fadc82f",
  "protocol": 908925107,
  "protocol_name": "a3f411c2-83be-4072-a475-74b0f7fb749f",
  "remarks": "c7befb98-6b03-45cb-a065-f56385d3b6b4",
  "satellite_type": [
    "e_type_gps|e_type_beidou|e_type_glonass",
    "e_type_gps|e_type_beidou|e_type_glonass",
    "e_type_gps|e_type_beidou|e_type_glonass"
  ],
  "signa_val": 908925107,
  "signal_rate": 908925107,
  "start_dev_time": 908925107,
  "state": "e_line_down|e_line_on|e_line_sleep",
  "state_begin_time": 908925107,
  "state_time": 908925107,
  "support_wxvoice": false,
  "temper": "83f27b78-e588-4132-ba70-76182ba9d42e",
  "temperature": "232fa955-6c00-43b2-a46e-b79f3db6a206",
  "up_line_time": 908925107,
  "ver": "cbf00c03-9413-4169-bbd1-bae94eae4195",
  "version": "1daf0622-197f-4b72-b647-b220fca3a43a",
  "wake_time": 908925107
}
```

<a id="device-GetConfig"></a>
## 设备的配置开关信息 (`GetConfig`)

- 接口 ID：`device.GetConfig`
- 模块：`device`
- 用途：设备的配置开关信息
- 原始位置：`source/设备(device).txt` 第 389 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，设备sim卡的App_flag需要和登录账号的App一致；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetConfig |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.check_func` | `array` | 功能标识, 当type为e_config_common或e_config_alarm时check_result将只返回check_func指定的功能检测结果 |
| `params.simei` | `string` | 设备imei,设备登入可以不用 |
| `params.type` | `string` | 拉取信息种类(必填)<br>e_config_config 报警相关<br>e_config_phone 电话相关<br>e_config_other 其它所有<br>e_config_all 获取全部配置<br>e_config_common 获取通用配置功能&lt;工具， 测试&gt;<br>e_config_alarm 获取报警功能列表 |
| `params.types` | `array` | 拉取信息种类，可选，可同时返回多个类别的功能<br>e_config_config 报警相关<br>e_config_phone 电话相关<br>e_config_other 其它所有<br>e_config_all 获取全部配置<br>e_config_common 获取通用配置功能&lt;工具， 测试&gt;<br>e_config_alarm 获取报警功能列表 |

### 请求示例

```json
{
  "module": "device",
  "func": "GetConfig",
  "params": {
    "check_func": [
      "081c02cb-9f8a-40d5-9979-8af0d3f63188",
      "ea177275-483d-45cd-9544-13b9551e408b",
      "830988d3-166c-4ff2-81d6-f87b325d2b58"
    ],
    "simei": "6064d414-93a1-4f2a-aa58-bb63dee60084",
    "type": "min|e_config_config|e_config_phone|e_config_other|e_config_all|e_config_common|e_config_alarm",
    "types": [
      "min|e_config_config|e_config_phone|e_config_other|e_config_all|e_config_common|e_config_alarm",
      "min|e_config_config|e_config_phone|e_config_other|e_config_all|e_config_common|e_config_alarm"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `attendance` | `number` | 是否支持打卡记录 0不支持 1支持 |
| `bluetooth_flag` | `number` | 猎狗蓝牙标识，0不支持蓝牙,1支持蓝牙 |
| `check_result` | `array` | 检测结果 |
| `check_result.func` | `string` | 功能标识 |
| `check_result.func_desc` | `string` | 功能参数描述 |
| `check_result.param` | `string` | 功能参数 |
| `check_result.support` | `number` | 是否支持, -1不支持，0支持 |
| `check_result.type` | `string` | 功能所属类别<br>e_config_config 报警相关<br>e_config_phone 电话相关<br>e_config_other 其它所有<br>e_config_all 获取全部配置<br>e_config_common 获取通用配置功能&lt;工具， 测试&gt;<br>e_config_alarm 获取报警功能列表 |
| `cmd_read` | `array` | 支持的指令相关,但是不可用<br>e_cmd_sleep 一键休眠<br>e_cmd_wakeup 一键唤醒<br>e_cmd_restart 一键重启<br>e_cmd_reset 恢复出厂设置<br>e_cmd_finddev 找寻设备<br>e_cmd_query_location 查询位置信息<br>e_cmd_alarm_ack 确认报警<br>e_cmd_force_shutdown 强制关机 |
| `cmd_type` | `array` | 支持的指令相关信息<br>e_cmd_sleep 一键休眠<br>e_cmd_wakeup 一键唤醒<br>e_cmd_restart 一键重启<br>e_cmd_reset 恢复出厂设置<br>e_cmd_finddev 找寻设备<br>e_cmd_query_location 查询位置信息<br>e_cmd_alarm_ack 确认报警<br>e_cmd_force_shutdown 强制关机 |
| `config` | `object` | 配置信息 |
| `config.after_sleep` | `number` | 设备休眠后的动作0发送心跳数据，1发送定位数据,2断网,默认0 |
| `config.alarm_space` | `number` | 电子牵绳告警阈值 |
| `config.car_switch` | `object` | 设备的开关 |
| `config.car_switch.accalarm` | `number` | acc上电报警开关,默认关闭,此开关不依赖设防状态,&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推 |
| `config.car_switch.alarmswitch` | `number` | 报警开关 0关闭，1开启，-1不支持 |
| `config.car_switch.auto_answer` | `number` | 自动应答；1开启，0关闭，-1不支持 |
| `config.car_switch.closeswitch` | `number` | 关机报警0关闭，-1不支持,&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推，范围在（0-16） |
| `config.car_switch.disconnswitch` | `number` | 休眠是否断网 0关闭，1开启，-1不支持 |
| `config.car_switch.dropalarm` | `number` | 脱落报警0关闭，-1不支持,&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推，范围在（0-16） |
| `config.car_switch.fenceswitch` | `number` | 围栏开关 0关闭,-1不支持,&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推，范围（0-16） |
| `config.car_switch.indicatorlight` | `number` | 指示灯 0关闭，1开启，-1不支持 |
| `config.car_switch.lbsswitch` | `number` | 基站开关 0关闭，1开启，-1不支持 |
| `config.car_switch.lowpower` | `number` | 低电报警,0关闭，-1不支持,&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推，范围在（0-16） |
| `config.car_switch.lowpowermodeswitch` | `number` | 低电模式开关 0关闭, 1开启, -1不支持 |
| `config.car_switch.offswitch` | `number` | 离线报警0关闭，-1不支持,&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推，范围（0-16） |
| `config.car_switch.openswitch` | `number` | 开机报警 启动报警 0关闭，1开启，-1不支持 |
| `config.car_switch.protect` | `number` | 设防开关,默认关闭 0关闭，1开启，-1不支持 |
| `config.car_switch.rechage_sim` | `number` | 是否可以充值sim卡,0关闭, 1开启 |
| `config.car_switch.replaystate` | `number` | 继电器开关状态 0关闭，1开启，-1不支持,在SetRelaySwitch设置 |
| `config.car_switch.reportloc` | `number` | 设备在唤醒状态下静止时是否上报定位数据,默认不上报 0关闭，1开启，-1不支持 |
| `config.car_switch.shakeswicth` | `number` | 震动报警开关，暂时不可设置,关闭请设置shake_value值, -1不支持，&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推 范围（0-16） |
| `config.car_switch.sleep_overtime` | `number` | 休眠超时报警;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推 范围（0-16） |
| `config.car_switch.sosswitch` | `number` | sos报警开关，暂时不可设置，-1不支持，&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推，范围（0-16） |
| `config.car_switch.soundswitch` | `number` | 声音提示开关 0关闭，1开启，-1不支持 |
| `config.car_switch.speedswitch` | `number` | 超速报警开关，暂时不可设置,关闭请设置speed_value值, -1不支持，&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推 范围（0-16） |
| `config.car_switch.statellite` | `number` | 设备上报的定位数据中，在定到位的状态下，是否带上基站及wifi等附属信息,默认不带上 0关闭，1开启，-1不支持 |
| `config.car_switch.stayalarm` | `number` | 停留报警；1开启，0关闭，-1不支持 |
| `config.car_switch.wifiswitch` | `number` | wifi开关 0关闭，1开启，-1不支持 |
| `config.dev_log` | `number` | 设备log上报等级0,关闭，1info,2error，3fatal默认1 |
| `config.language` | `string` | 设备当前语言 0：英文   1：中文，默认中文<br>e_language_en 0：英文<br>e_language_ch 1：中文，默认中文 |
| `config.locint_val` | `number` | 运动定位上报间隔 单位秒 默认30s,设备如果不支持返回-1，范围为（0-10000）秒 |
| `config.notice_phones` | `array` | 报警通知电话 |
| `config.phone_alarm` | `object` | 如果设备支持电话报警，设置参数时，哪个值改变了，设置哪个值 |
| `config.phone_alarm.acc` | `string` | acc非法启动报警的方式<br>e_pa_none 不通知<br>e_pa_sms 短信通知<br>e_pa_phone 电话通知<br>e_pa_all 短信+电话 |
| `config.phone_alarm.out` | `string` | 断电报警的方式<br>e_pa_none 不通知<br>e_pa_sms 短信通知<br>e_pa_phone 电话通知<br>e_pa_all 短信+电话 |
| `config.phone_alarm.total` | `number` | 电话通知次数 |
| `config.phone_alarm.vibration` | `string` | 震动报警的方式<br>e_pa_none 不通知<br>e_pa_sms 短信通知<br>e_pa_phone 电话通知<br>e_pa_all 短信+电话 |
| `config.phone_book` | `string` | 设备电话本（管理员号码集合 用来唤醒设备）json字符串数组. |
| `config.shake_value` | `number` | 震动报警程度0-关闭,(10-254)开启，-1不支持,如果支持设防震动，这个值0表示关闭，1表示开启，,注意，如果设置这个值，需要带上shakeswicth，范围为（10-254） |
| `config.sleep_intval` | `number` | 休眠定位上报间隔 单位秒 默认600s,设备如果不支持返回-1，范围为（0-10000）秒 |
| `config.sleep_overtime_threshold` | `number` | 休眠超时阈值(秒)，0关闭，&gt;0开启，还需设置car_switch.sleepovertime |
| `config.speed_value` | `number` | 超速阈值 0关闭，&gt;0开启，-1不支持,注意，如果设置这个值，需要带上speedswitch，范围在（10-190） |
| `config.vibration` | `object` | 如果设备支持带有设防震动报警功能，设置参数,注意每次设置的时候，需要将每个参数值都带上 |
| `config.vibration.fence_delay` | `number` | 自动设防延时时间 |
| `config.vibration.fence_shake_delay` | `number` | 自动设防震动报警延时 |
| `config.vibration.shake_alarm_interval` | `number` | 震动报警间隔 |
| `config.vibration.shake_check_time` | `number` | 震动检测时间 |
| `config.volume_value` | `number` | 喇叭音量值 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `force_replay` | `number` | 是否有强制断油电能力，-1不支持，1支持 |
| `force_shutdown` | `number` | 强制关机 0不支持，1支持 |
| `history` | `number` | 是否有历史轨迹的能力, 0不支持,1支持 |
| `mode_fun` | `string` | 支持的模式<br>e_mode_invalid 什么模式都不支持<br>e_mode_loc 支持多选的模式<br>e_mode_rtls 支持定时的模式 |
| `monitor` | `number` | 是否有远程听音功能 0不支持,1支持 |
| `ontime_switch` | `number` | 是否有定时开关机功能, 0不支持,1支持 |
| `phone_alarm` | `number` | 是否支持电话报警,0不支持,1支持 |
| `priority` | `string` | 定位优先级，如果没有该功能，将不返回该字段 |
| `protocol` | `number` | 设备协议，默认808协议 |
| `remoteswitch` | `number` | 是否可远程开关机, -1不支持,1支持,0支持，但是不可用 |
| `sms_msg` | `number` | 短信转发能力,-1不支持,1支持,2支持，但是sim卡无效不可用 |
| `trace` | `number` | 是否有追踪的能力 0不支持,1支持 |
| `vibration` | `number` | 是否支持震动0不支持，1支持老版本的,2支持带有设防震动 |
| `volume` | `number` | 是否支持喇叭音量 0不支持 1支持 |

### 返回示例

```json
{
  "errcode": 0,
  "attendance": 539284576,
  "bluetooth_flag": 539284576,
  "check_result": [
    {
      "func": "ece0c9e3-83a3-4628-b65d-b31c07f0bbe0",
      "func_desc": "0823f17b-b68f-418a-bc1f-f84a57ac1d25",
      "param": "07525e30-bf2f-4897-9689-09fee0d267d9",
      "support": 539284576,
      "type": "min|e_config_config|e_config_phone|e_config_other|e_config_all|e_config_common|e_config_alarm"
    },
    {
      "func": "0a47201c-b4e4-4256-b4e3-cc4685a19098",
      "func_desc": "bdcea4a0-2f43-43f2-9d4b-a0437ef52583",
      "param": "6e53c5b0-814b-4887-970b-04d86161f0eb",
      "support": 539284576,
      "type": "min|e_config_config|e_config_phone|e_config_other|e_config_all|e_config_common|e_config_alarm"
    },
    {
      "func": "3efc90e3-1d36-4242-8d3f-3a79c5938543",
      "func_desc": "f85038a6-709e-46a0-b8f2-a097ee9b2af3",
      "param": "3e92ac72-054f-4954-a0f7-47c5f640ff88",
      "support": 539284576,
      "type": "min|e_config_config|e_config_phone|e_config_other|e_config_all|e_config_common|e_config_alarm"
    },
    {
      "func": "48821149-fcb4-4f96-bd64-f9f3f9272b7b",
      "func_desc": "7c707c4f-c595-480c-bdf5-dde1e605646d",
      "param": "39afed55-e6ba-4f44-9f08-6ad5ecebed7c",
      "support": 539284576,
      "type": "min|e_config_config|e_config_phone|e_config_other|e_config_all|e_config_common|e_config_alarm"
    }
  ],
  "cmd_read": [
    "e_cmd_sleep|e_cmd_wakeup|e_cmd_restart|e_cmd_reset|e_cmd_finddev|e_cmd_query_location|e_cmd_alarm_ack|e_cmd_force_shutdown|xmg_track|e_cmd_dev_listen",
    "e_cmd_sleep|e_cmd_wakeup|e_cmd_restart|e_cmd_reset|e_cmd_finddev|e_cmd_query_location|e_cmd_alarm_ack|e_cmd_force_shutdown|xmg_track|e_cmd_dev_listen",
    "e_cmd_sleep|e_cmd_wakeup|e_cmd_restart|e_cmd_reset|e_cmd_finddev|e_cmd_query_location|e_cmd_alarm_ack|e_cmd_force_shutdown|xmg_track|e_cmd_dev_listen"
  ],
  "cmd_type": [
    "e_cmd_sleep|e_cmd_wakeup|e_cmd_restart|e_cmd_reset|e_cmd_finddev|e_cmd_query_location|e_cmd_alarm_ack|e_cmd_force_shutdown|xmg_track|e_cmd_dev_listen",
    "e_cmd_sleep|e_cmd_wakeup|e_cmd_restart|e_cmd_reset|e_cmd_finddev|e_cmd_query_location|e_cmd_alarm_ack|e_cmd_force_shutdown|xmg_track|e_cmd_dev_listen"
  ],
  "config": {
    "after_sleep": 539284576,
    "alarm_space": 539284576,
    "car_switch": {
      "accalarm": 539284576,
      "alarmswitch": 539284576,
      "auto_answer": 539284576,
      "closeswitch": 539284576,
      "disconnswitch": 539284576,
      "dropalarm": 539284576,
      "fenceswitch": 539284576,
      "indicatorlight": 539284576,
      "lbsswitch": 539284576,
      "lowpower": 539284576,
      "lowpowermodeswitch": 539284576,
      "offswitch": 539284576,
      "openswitch": 539284576,
      "protect": 539284576,
      "rechage_sim": 539284576,
      "replaystate": 539284576,
      "reportloc": 539284576,
      "shakeswicth": 539284576,
      "sleep_overtime": 539284576,
      "sosswitch": 539284576,
      "soundswitch": 539284576,
      "speedswitch": 539284576,
      "statellite": 539284576,
      "stayalarm": 539284576,
      "wifiswitch": 539284576
    },
    "dev_log": 539284576,
    "language": "e_language_en|e_language_ch",
    "locint_val": 539284576,
    "notice_phones": [
      "6f1554ce-39e1-4841-8db4-8e379798a500",
      "a707b09e-4425-45fb-93e7-0e52c2c35bb2",
      "fe03435f-a295-4920-ab05-0d02c454cfc0",
      "430f7ff5-1856-4068-a002-e0197006e9df"
    ],
    "phone_alarm": {
      "acc": "e_pa_none|e_pa_sms|e_pa_phone|e_pa_all",
      "out": "e_pa_none|e_pa_sms|e_pa_phone|e_pa_all",
      "total": 539284576,
      "vibration": "e_pa_none|e_pa_sms|e_pa_phone|e_pa_all"
    },
    "phone_book": "4397a420-323e-48cd-9eea-315deb12178a",
    "shake_value": 539284576,
    "sleep_intval": 539284576,
    "sleep_overtime_threshold": 539284576,
    "speed_value": 539284576,
    "vibration": {
      "fence_delay": 539284576,
      "fence_shake_delay": 539284576,
      "shake_alarm_interval": 539284576,
      "shake_check_time": 539284576
    },
    "volume_value": 539284576
  },
  "error_message": "8002c6db-40da-4033-88c4-4f22ea7a6f1a",
  "force_replay": 539284576,
  "force_shutdown": 539284576,
  "history": 539284576,
  "mode_fun": "e_mode_invalid|e_mode_loc|e_mode_rtls",
  "monitor": 539284576,
  "ontime_switch": 539284576,
  "phone_alarm": 539284576,
  "priority": "309788b5-02c1-430f-b6a4-4fffa37ad25a",
  "protocol": 539284576,
  "remoteswitch": 539284576,
  "sms_msg": 539284576,
  "trace": 539284576,
  "vibration": 539284576,
  "volume": 539284576
}
```

<a id="device-Add"></a>
## 添加设备 (`Add`)

- 接口 ID：`device.Add`
- 模块：`device`
- 用途：添加设备
- 原始位置：`source/设备(device).txt` 第 753 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1,最多一次允许添加1000个；
2，修改car_image,start_dev_time,car_number无效；
3，白名单最多10个且只准数字不可重复；
4，不可添加已有终端用户绑定的设备；
5，不可添加设备版本号不支持的功能；
6，超速阈值speed_value范围（10-190）；
7，灵敏度shake_value范围（10-254）；
8，休眠定位上报间隔sleep_value范围（1-10000）；
9，运动定位上报间隔locint_val范围（1-10000）；
10，终端用户添加设备，不影响销售商与该设备的挂在关系；
11，账号登录才可使用；
12，需要添加设备权限；
13，销售商只能添加自己上级及以上的组织中的设备,并且添加之后，设备将移动到当前销售商中
14，对应权限e_ua_add_dev

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Add |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.info` | `array` | 批量添加的设备信息，必填，最多一次允许添加1000个，超过1000会分批进行。添加的所有设备必须在同一个平台，不支持多平台同时添加。 |
| `params.info.imei` | `number` | 车载设备IMEI |
| `params.sgid` | `string` | 指定的车组，添加的设备将全部挂载在该车组下 |

### 请求示例

```json
{
  "module": "device",
  "func": "Add",
  "params": {
    "info": [
      {
        "imei": 941493121
      },
      {
        "imei": 941493121
      }
    ],
    "sgid": "543f36c0-3c67-4905-b0b4-716bf69efc6d"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 失败的imei信息，如果添加的设备中既有成功又有失败的设备，只会显示失败的设备信息。 |
| `fail_items.errcode` | `number` | 错误编码，账号登录部分设备号错误，错误码在这里返回。 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |
| `suc_items` | `array` | 添加成功上的imei信息，如果添加的设备中既有成功又有失败的设备。成功的设备不会显示。 |
| `suc_items.imei` | `number` | 设备的imei |
| `suc_items.simei` | `string` | 设备id |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "60b2fec5-0978-40d5-8927-5bf54fb29afb",
  "fail_items": [
    {
      "errcode": 801060491,
      "error_message": "7360ba53-d50e-4c30-b8df-6b5585ab3d12",
      "imei": 801060491
    }
  ],
  "suc_items": [
    {
      "imei": 801060491,
      "simei": "02c5e0ca-021b-4071-8651-6c6a92c91271"
    },
    {
      "imei": 801060491,
      "simei": "1e61c7e6-762b-4e95-9f74-85a8e4547df5"
    }
  ]
}
```

<a id="device-SetDetail"></a>
## 修改设备的详细信息 (`SetDetail`)

- 接口 ID：`device.SetDetail`
- 模块：`device`
- 用途：修改设备的详细信息
- 原始位置：`source/设备(device).txt` 第 847 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，需要设备修改权限；
2，白名单最多10个且只准数字不可重复；
3，对应权限e_ua_modify_dev

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SetDetail |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.car_image` | `number` | 车辆图标 |
| `params.car_number` | `string` | 车牌号 |
| `params.car_type` | `string` | 车辆类型 |
| `params.center_phone` | `string` | 中心号码 |
| `params.engine_num` | `string` | 发动机号 |
| `params.frame_num` | `string` | 车架号车辆品牌 |
| `params.img_other` | `string` | 身份证反面 |
| `params.img_pos` | `string` | 身份证正面 |
| `params.scar_safe_time` | `string` | 车辆保险 格式如:2018-10-19,清空为0000-00-00 |
| `params.scar_year_check` | `string` | 车辆年检 格式如:2018-10-19,清空为0000-00-00 |
| `params.simei` | `string` | 需要修改的imei,如果设备登入，不用填，账号登录需要填。 |
| `params.user_addr` | `string` | 车主地址 |
| `params.user_cer` | `string` | 车主证件号码 |
| `params.user_image` | `number` | 车主形象（图标） |
| `params.user_mail` | `string` | 车主e_mail |
| `params.user_name` | `string` | 车主姓名 |
| `params.user_phone` | `string` | 用户手机号码 |
| `params.user_sex` | `string` | 车主性别<br>e_sex_man 男<br>e_sex_woman 女 |
| `params.user_unit` | `string` | 车主工作单位 |

### 请求示例

```json
{
  "module": "device",
  "func": "SetDetail",
  "params": {
    "car_image": 1114632357,
    "car_number": "fe42c425-6a28-4b72-945d-a6aa35d5954f",
    "car_type": "76011006-b302-43b8-85f2-d2ebbaa07849",
    "center_phone": "75d6d82a-2af8-4324-888d-e3fd19360351",
    "engine_num": "9a871250-16bd-41cf-86e2-8c77025b56bf",
    "frame_num": "5fc238ce-c198-4874-9e1f-0d6ebdea484b",
    "img_other": "8e2241c4-f1fa-4705-a682-71069a8f1732",
    "img_pos": "5b9136b2-f6f6-410e-b6d2-d4106ddeeb21",
    "scar_safe_time": "aa5eab54-95f8-44fa-ba51-af0d514393ce",
    "scar_year_check": "ad273a28-fac1-4650-b55b-f1ce0207a587",
    "simei": "04463d5a-54dc-40f6-8426-e9b29a5dc36c",
    "user_addr": "b850ed9b-ec26-4425-b455-54b863e939f6",
    "user_cer": "f32d4879-1a63-4650-b673-af5137fc1548",
    "user_image": 1114632357,
    "user_mail": "c94e0741-1875-4c7c-a112-843b53fa4233",
    "user_name": "5b00edae-9fdd-46e4-8db5-b00cbc80f9af",
    "user_phone": "828f3423-bedb-4f81-82bf-c956f3aa49b3",
    "user_sex": "e_sex_man|e_sex_woman",
    "user_unit": "fa39e6ea-0147-408c-9a1b-90063851bbce"
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
  "error_message": "95325169-b559-4ba2-bb4a-a9144cbb8328"
}
```

<a id="device-SetConfig"></a>
## 修改设备的配置信息 (`SetConfig`)

- 接口 ID：`device.SetConfig`
- 模块：`device`
- 用途：修改设备的配置信息
- 原始位置：`source/设备(device).txt` 第 943 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1,需要设备修改权限；
2，修改car_image,car_number无效；
3，白名单最多10个且只准数字不可重复；
4，设备sim卡的App_flag需要和登录账号的App一致；
5，不可添加设备版本号不支持的功能；
6，超速阈值speed_value范围（10-190）；
7，灵敏度shake_value范围（10-254）；
8，休眠定位上报间隔sleep_value范围（1-10000）；
9，运动定位上报间隔locint_val范围（1-10000）；
10，对应权限e_ua_modify_dev

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SetConfig |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.config` | `object` | 需要修改的设备信息 |
| `params.config.after_sleep` | `number` | 设备休眠后的动作0发送心跳数据，1发送定位数据,2断网,默认0 |
| `params.config.alarm_space` | `number` | 电子牵绳告警阈值 |
| `params.config.car_switch` | `object` | 设备的开关 |
| `params.config.car_switch.accalarm` | `number` | acc上电报警开关,默认关闭,此开关不依赖设防状态,&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推 |
| `params.config.car_switch.alarmswitch` | `number` | 报警开关 0关闭，1开启，-1不支持 |
| `params.config.car_switch.auto_answer` | `number` | 自动应答；1开启，0关闭，-1不支持 |
| `params.config.car_switch.closeswitch` | `number` | 关机报警0关闭，-1不支持,&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推，范围在（0-16） |
| `params.config.car_switch.disconnswitch` | `number` | 休眠是否断网 0关闭，1开启，-1不支持 |
| `params.config.car_switch.dropalarm` | `number` | 脱落报警0关闭，-1不支持,&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推，范围在（0-16） |
| `params.config.car_switch.fenceswitch` | `number` | 围栏开关 0关闭,-1不支持,&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推，范围（0-16） |
| `params.config.car_switch.indicatorlight` | `number` | 指示灯 0关闭，1开启，-1不支持 |
| `params.config.car_switch.lbsswitch` | `number` | 基站开关 0关闭，1开启，-1不支持 |
| `params.config.car_switch.lowpower` | `number` | 低电报警,0关闭，-1不支持,&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推，范围在（0-16） |
| `params.config.car_switch.lowpowermodeswitch` | `number` | 低电模式开关 0关闭, 1开启, -1不支持 |
| `params.config.car_switch.offswitch` | `number` | 离线报警0关闭，-1不支持,&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推，范围（0-16） |
| `params.config.car_switch.openswitch` | `number` | 开机报警 启动报警 0关闭，1开启，-1不支持 |
| `params.config.car_switch.protect` | `number` | 设防开关,默认关闭 0关闭，1开启，-1不支持 |
| `params.config.car_switch.rechage_sim` | `number` | 是否可以充值sim卡,0关闭, 1开启 |
| `params.config.car_switch.replaystate` | `number` | 继电器开关状态 0关闭，1开启，-1不支持,在SetRelaySwitch设置 |
| `params.config.car_switch.reportloc` | `number` | 设备在唤醒状态下静止时是否上报定位数据,默认不上报 0关闭，1开启，-1不支持 |
| `params.config.car_switch.shakeswicth` | `number` | 震动报警开关，暂时不可设置,关闭请设置shake_value值, -1不支持，&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推 范围（0-16） |
| `params.config.car_switch.sleep_overtime` | `number` | 休眠超时报警;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推 范围（0-16） |
| `params.config.car_switch.sosswitch` | `number` | sos报警开关，暂时不可设置，-1不支持，&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推，范围（0-16） |
| `params.config.car_switch.soundswitch` | `number` | 声音提示开关 0关闭，1开启，-1不支持 |
| `params.config.car_switch.speedswitch` | `number` | 超速报警开关，暂时不可设置,关闭请设置speed_value值, -1不支持，&gt;0的时候，按位取值;1开启app推送，2开启短信推送,4开启电话推送，如果是3，也就是app推送以及短信推送，以此类推 范围（0-16） |
| `params.config.car_switch.statellite` | `number` | 设备上报的定位数据中，在定到位的状态下，是否带上基站及wifi等附属信息,默认不带上 0关闭，1开启，-1不支持 |
| `params.config.car_switch.stayalarm` | `number` | 停留报警；1开启，0关闭，-1不支持 |
| `params.config.car_switch.wifiswitch` | `number` | wifi开关 0关闭，1开启，-1不支持 |
| `params.config.dev_log` | `number` | 设备log上报等级0,关闭，1info,2error，3fatal默认1 |
| `params.config.language` | `string` | 设备当前语言 0：英文   1：中文，默认中文<br>e_language_en 0：英文<br>e_language_ch 1：中文，默认中文 |
| `params.config.locint_val` | `number` | 运动定位上报间隔 单位秒 默认30s,设备如果不支持返回-1，范围为（0-10000）秒 |
| `params.config.notice_phones` | `array` | 报警通知电话 |
| `params.config.phone_alarm` | `object` | 如果设备支持电话报警，设置参数时，哪个值改变了，设置哪个值 |
| `params.config.phone_alarm.acc` | `string` | acc非法启动报警的方式<br>e_pa_none 不通知<br>e_pa_sms 短信通知<br>e_pa_phone 电话通知<br>e_pa_all 短信+电话 |
| `params.config.phone_alarm.out` | `string` | 断电报警的方式<br>e_pa_none 不通知<br>e_pa_sms 短信通知<br>e_pa_phone 电话通知<br>e_pa_all 短信+电话 |
| `params.config.phone_alarm.total` | `number` | 电话通知次数 |
| `params.config.phone_alarm.vibration` | `string` | 震动报警的方式<br>e_pa_none 不通知<br>e_pa_sms 短信通知<br>e_pa_phone 电话通知<br>e_pa_all 短信+电话 |
| `params.config.phone_book` | `string` | 设备电话本（管理员号码集合 用来唤醒设备）json字符串数组. |
| `params.config.shake_value` | `number` | 震动报警程度0-关闭,(10-254)开启，-1不支持,如果支持设防震动，这个值0表示关闭，1表示开启，,注意，如果设置这个值，需要带上shakeswicth，范围为（10-254） |
| `params.config.sleep_intval` | `number` | 休眠定位上报间隔 单位秒 默认600s,设备如果不支持返回-1，范围为（0-10000）秒 |
| `params.config.sleep_overtime_threshold` | `number` | 休眠超时阈值(秒)，0关闭，&gt;0开启，还需设置car_switch.sleepovertime |
| `params.config.speed_value` | `number` | 超速阈值 0关闭，&gt;0开启，-1不支持,注意，如果设置这个值，需要带上speedswitch，范围在（10-190） |
| `params.config.vibration` | `object` | 如果设备支持带有设防震动报警功能，设置参数,注意每次设置的时候，需要将每个参数值都带上 |
| `params.config.vibration.fence_delay` | `number` | 自动设防延时时间 |
| `params.config.vibration.fence_shake_delay` | `number` | 自动设防震动报警延时 |
| `params.config.vibration.shake_alarm_interval` | `number` | 震动报警间隔 |
| `params.config.vibration.shake_check_time` | `number` | 震动检测时间 |
| `params.config.volume_value` | `number` | 喇叭音量值 |
| `params.func_data` | `array` |  |
| `params.func_data.func` | `string` | 功能标识 |
| `params.func_data.func_desc` | `string` | 功能参数描述 |
| `params.func_data.param` | `string` | 功能参数 |
| `params.func_data.support` | `number` | 是否支持, -1不支持，0支持 |
| `params.func_data.type` | `string` | 功能所属类别<br>e_config_config 报警相关<br>e_config_phone 电话相关<br>e_config_other 其它所有<br>e_config_all 获取全部配置<br>e_config_common 获取通用配置功能&lt;工具， 测试&gt;<br>e_config_alarm 获取报警功能列表 |
| `params.priority` | `string` | 定位优先级 |
| `params.sfamilyid` | `string` | 需要修改车组织下面全部设备，simei,sfamilyid,sgid参数三选一 |
| `params.sgid` | `string` | 需要修改指定分组下面全部设备，simei,sfamilyid,sgid参数三选一 |
| `params.simei` | `array` | 需要修改的simei列表,最多1000,如果设备登入，不用填 |

### 请求示例

```json
{
  "module": "device",
  "func": "SetConfig",
  "params": {
    "config": {
      "after_sleep": 2068822492,
      "alarm_space": 2068822492,
      "car_switch": {
        "accalarm": 2068822492,
        "alarmswitch": 2068822492,
        "auto_answer": 2068822492,
        "closeswitch": 2068822492,
        "disconnswitch": 2068822492,
        "dropalarm": 2068822492,
        "fenceswitch": 2068822492,
        "indicatorlight": 2068822492,
        "lbsswitch": 2068822492,
        "lowpower": 2068822492,
        "lowpowermodeswitch": 2068822492,
        "offswitch": 2068822492,
        "openswitch": 2068822492,
        "protect": 2068822492,
        "rechage_sim": 2068822492,
        "replaystate": 2068822492,
        "reportloc": 2068822492,
        "shakeswicth": 2068822492,
        "sleep_overtime": 2068822492,
        "sosswitch": 2068822492,
        "soundswitch": 2068822492,
        "speedswitch": 2068822492,
        "statellite": 2068822492,
        "stayalarm": 2068822492,
        "wifiswitch": 2068822492
      },
      "dev_log": 2068822492,
      "language": "e_language_en|e_language_ch",
      "locint_val": 2068822492,
      "notice_phones": [
        "5e19ebf4-13de-4498-b2e1-4cce93f04acc"
      ],
      "phone_alarm": {
        "acc": "e_pa_none|e_pa_sms|e_pa_phone|e_pa_all",
        "out": "e_pa_none|e_pa_sms|e_pa_phone|e_pa_all",
        "total": 2068822492,
        "vibration": "e_pa_none|e_pa_sms|e_pa_phone|e_pa_all"
      },
      "phone_book": "dbb565a9-d9d6-44ad-92a1-cdac439dc113",
      "shake_value": 2068822492,
      "sleep_intval": 2068822492,
      "sleep_overtime_threshold": 2068822492,
      "speed_value": 2068822492,
      "vibration": {
        "fence_delay": 2068822492,
        "fence_shake_delay": 2068822492,
        "shake_alarm_interval": 2068822492,
        "shake_check_time": 2068822492
      },
      "volume_value": 2068822492
    },
    "func_data": [
      {
        "func": "4f0409ca-1289-4615-9b18-b2f90edc59f9",
        "func_desc": "b7673ada-8d1b-4d53-a16e-2d1edc929e68",
        "param": "bb948b1d-61b3-4114-ad2e-67226615bc9a",
        "support": 2068822492,
        "type": "min|e_config_config|e_config_phone|e_config_other|e_config_all|e_config_common|e_config_alarm"
      }
    ],
    "priority": "3bdcf4f3-82e0-442b-be60-67f587145b76",
    "sfamilyid": "c6f6ba89-96ff-4a0c-9dcc-d346cee2d70b",
    "sgid": "fc162c6d-939d-40ec-81b4-d9967cb018c6",
    "simei": [
      "cfc775d7-11bd-487e-ba49-83d4be830ae8"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 当没有触发异步操作时，有操作成功的imei号，通过这个字段返回 |
| `fail_items.errcode` | `number` | 错误编码 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |
| `fail_items.simei` | `string` | 错误的设备号(加密) |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "c97074ad-a13b-448c-b8bb-f87713cb1c01",
  "fail_items": [
    {
      "errcode": 1528899251,
      "error_message": "5b60e050-1215-4e70-9aed-332f88072622",
      "imei": 1528899251,
      "simei": "4ba13794-6916-4ea4-b73d-2d735a0e086c"
    },
    {
      "errcode": 1528899251,
      "error_message": "f58149bb-df60-467c-924c-878d4d547eea",
      "imei": 1528899251,
      "simei": "903d6625-0243-4348-9c41-de7d1afe1562"
    }
  ]
}
```

<a id="device-Cmd"></a>
## 给设备下发指令 (`Cmd`)

- 接口 ID：`device.Cmd`
- 模块：`device`
- 用途：给设备下发指令
- 原始位置：`source/设备(device).txt` 第 1219 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1,部分指令需版本号支持（参照版本权限表）；
2，m2设备下发定位指令需间隔一分钟；
3，设备离线下不支持一键休眠，一键唤醒，一键重启，找寻设备指令；
4，需要设备修改权限；
5，对应权限e_ua_modify_dev

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Cmd |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.alarmtype` | `number` | 确认报警类型 |
| `params.simei` | `string` | 需要修改的imei,如果设备登入，不用填 |
| `params.type` | `string` | 下发指令类型<br>e_cmd_sleep 一键休眠<br>e_cmd_wakeup 一键唤醒<br>e_cmd_restart 一键重启<br>e_cmd_reset 恢复出厂设置<br>e_cmd_finddev 找寻设备<br>e_cmd_query_location 查询位置信息<br>e_cmd_alarm_ack 确认报警<br>e_cmd_force_shutdown 强制关机 |

### 请求示例

```json
{
  "module": "device",
  "func": "Cmd",
  "params": {
    "alarmtype": 778447475,
    "simei": "7e826c8c-f277-4fd5-b5f1-b49a8b8b46f5",
    "type": "e_cmd_sleep|e_cmd_wakeup|e_cmd_restart|e_cmd_reset|e_cmd_finddev|e_cmd_query_location|e_cmd_alarm_ack|e_cmd_force_shutdown|xmg_track|e_cmd_dev_listen"
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
  "error_message": "8bbd273d-9453-42ab-ac31-66ff756de519"
}
```

<a id="device-DelLog"></a>
## 删除设备的操作日志 (`DelLog`)

- 接口 ID：`device.DelLog`
- 模块：`device`
- 用途：删除设备的操作日志
- 原始位置：`source/设备(device).txt` 第 1275 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，需要删除操作log；
2，对应权限e_ua_del_opt_log

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,DelLog |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.simei` | `string` | 设备号，需要删除操作日志的设备号，必填 |
| `params.time` | `number` | 拉取日志获取到,必填， 精确到ms的时间戳(13位长的时间戳，表示从1970.1.1的00:00到该时刻经过的毫秒的总数) |

### 请求示例

```json
{
  "module": "device",
  "func": "DelLog",
  "params": {
    "simei": "0e1dcf35-c459-4ff4-9d5f-de12a672675d",
    "time": 191385300
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
  "error_message": "04fd3e5f-2d4c-46b0-ae65-17e464ab51ff"
}
```

<a id="device-GetLog"></a>
## 获取设备的操作日志 (`GetLog`)

- 接口 ID：`device.GetLog`
- 模块：`device`
- 用途：获取设备的操作日志
- 原始位置：`source/设备(device).txt` 第 1317 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetLog |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.begin_time` | `number` | 拉取日志的起始时间,精确到ms,(13位长的时间戳，表示从1970.1.1的00:00到该时刻经过的毫秒的总数)可以不用填写 |
| `params.end_time` | `number` | 拉取日志的结束时间,精确到ms,(13位长的时间戳，表示从1970.1.1的00:00到该时刻经过的毫秒的总数)可以不用填写,默认当前时间 |
| `params.is_summary` | `boolean` | 是否需要统计数据，默认不需要 |
| `params.last_imei` | `number` | 获取到上一页日志的最后一条信息的imei号 |
| `params.last_time` | `number` | 获取到上一页日志的最后一条信息的时间戳,(13位长的时间戳，表示从1970.1.1的00:00到该时刻经过的毫秒的总数) |
| `params.limit_size` | `number` | 限制条数，不填默认20条，范围为（0-1000） |
| `params.simei_all` | `string` | 针对单个设备查询他全部的操作记录，只有销售商有这个权限,将忽略参数uid，simeis |
| `params.simeis` | `array` | 如果查询特定账号的日志，可以对特定的设备号进行查询 |
| `params.type` | `string` | 操作说明,获取全部就不要填<br>e_oper_cmd_sleep 下发指令给设备-一键休眠<br>e_oper_cmd_wakeup 下发指令给设备-一键唤醒<br>e_oper_cmd_restart 下发指令给设备-一键重启<br>e_oper_resume_init 恢复出厂设置<br>e_oper_cmd_find 下发指令给设备-寻找设备<br>e_oper_cmd_current_voltage 下发指令给设备-查询外电电压<br>e_oper_set_log_switch 设置设备操作日志<br>e_oper_set_remote_upgrade 下发设置终端远程升级功能<br>e_oper_set_dev_type 设置设备型号<br>e_oper_immediately_loc 立即定位<br>e_oper_set_protocol 更换设备协议<br>e_oper_set_protect 一键设防<br>e_oper_set_location 设置设备定位优先级<br>e_oper_force_shutdown 强制关机<br>e_oper_set_timerswitch 设备定时开关机<br>e_oper_close_timerswitch 关闭定时开关机<br>e_oper_set_loc_mode 设置定位模式<br>e_oper_set_relay_switch 设置继电器打开<br>e_oper_sim_recharge 充值<br>e_oper_sim_remote_switch 设置远程开关机<br>e_oper_open_realtime_track 实时追踪<br>e_oper_close_realtime_track 取消追踪<br>e_oper_add_device 添加设备<br>e_oper_del_device 删除设备<br>e_oper_move_garage 转移设备分组<br>e_oper_move_family 转移设备组织<br>e_oper_set_delicacy 设置灵敏度<br>e_oper_set_report_intval 设置运动定位汇报<br>e_oper_del_device_to_parent 移除设备<br>e_oper_clear_device 清除设备<br>e_oper_set_rtls_mode 设置定位上传模式<br>e_oper_set_change_host 设置主副ip<br>e_oper_clean_pwd 重置密码<br>e_oper_open_increment 操作增值服务开关<br>e_oper_change_bindphone 修改绑定手机号<br>e_oper_findpwd 找回密码<br>e_oper_changepwd 修改密码<br>e_oper_bindwx 绑定微信<br>e_oper_unbindwx 解绑微信<br>e_oper_fence_modify 修改围栏<br>e_oper_prepay_increment 充值增值服务<br>e_oper_bind_wxpush  绑定微信推送<br>e_oper_unbind_wxpush  解绑微信推送<br>e_oper_set_speed_value 设置超速阈值<br>e_oper_set_shake_value 设置震动报警<br>e_oper_set_lowpower_alarm 设置低电报警开关状态<br>e_oper_set_out_alarm 设置断电报警（拆除报警）报警开关状态<br>e_oper_set_drop_alarm 设置脱落报警开关状态<br>e_oper_set_indicatorlight_alarm 设置指示灯报警开关状态<br>e_oper_set_close_alarm 设置关机报警开关状态<br>e_oper_set_open_alarm 设置开机报警开关状态<br>e_oper_set_off_alarm 设置离线报警开关状态<br>e_oper_set_replay_alarm 设置继电器报警开关状态<br>e_oper_set_sound_alarm 设置声音报警开关状态<br>e_oper_set_language 设置设备语言<br>e_oper_set_listen_mode 设备语音模式<br>e_oper_set_phone_book 设置设备电话本<br>e_oper_set_center_phone 设置中心号码<br>e_oper_set_fence_alarm 围栏报警<br>e_oper_set_lbs_switch 设置基站开关<br>e_oper_set_disconn_switch 设置休眠不断网/设置休眠断网<br>e_oper_set_wifi_switch 设置wifi开关<br>e_oper_set_sleep_intval 设置休眠定位汇报间隔<br>e_oper_set_rechage_sim 设置是否充值sim卡<br>e_oper_set_auto_answer 设置自动接听<br>e_oper_set_sos_alarm 设置sos报警<br>e_oper_set_dualoffline_alarm 双离线报警&lt;mode&gt;bool&lt;/mode&gt;<br>e_oper_set_sleep_overtime_alarm 休眠超时报警&lt;mode value=&gt;bool&lt;/mode&gt;<br>e_oper_voice_tts 语音TTS |
| `params.uid` | `string` | 筛选指定账号的操作日志，如果不填写，返回当前账号的操作日志 |

### 请求示例

```json
{
  "module": "device",
  "func": "GetLog",
  "params": {
    "begin_time": 982679096,
    "end_time": 982679096,
    "is_summary": true,
    "last_imei": 982679096,
    "last_time": 982679096,
    "limit_size": 982679096,
    "simei_all": "14632b24-ab62-4c60-9ed8-7411936b60df",
    "simeis": [
      "4437e218-b59f-42dd-a254-55050fbdc7b8",
      "6c744420-a85c-455d-9f98-3fb3d8e0dbbb",
      "af3f5eaf-887e-440d-81d7-e41971aab88a",
      "8f927709-838e-49fc-804e-3630f7d4e128"
    ],
    "type": "e_oper_cmd_sleep|e_oper_cmd_wakeup|e_oper_cmd_restart|e_oper_resume_init|e_oper_cmd_find|e_oper_cmd_current_voltage|e_oper_set_log_switch|e_oper_set_remote_upgrade|e_oper_set_dev_type|e_oper_immediately_loc|e_oper_set_protocol|e_oper_set_protect|e_oper_set_location|e_oper_force_shutdown|e_oper_set_timerswitch|e_oper_close_timerswitch|e_oper_set_loc_mode|e_oper_set_relay_switch|e_oper_sim_recharge|e_oper_sim_remote_switch|e_oper_remote_report|e_oper_open_realtime_track|e_oper_close_realtime_track|e_oper_add_device|e_oper_del_device|e_oper_move_garage|e_oper_move_family|e_oper_open_record|e_oper_set_delicacy|e_oper_set_report_intval|e_oper_del_device_to_parent|e_oper_clear_device|e_oper_set_rtls_mode|e_oper_set_change_host|e_oper_clean_pwd|e_oper_open_increment|e_oper_change_bindphone|e_oper_findpwd|e_oper_changepwd|e_oper_bindwx|e_oper_unbindwx|e_oper_fence_modify|e_oper_prepay_increment|e_oper_bind_wxpush|e_oper_unbind_wxpush|e_oper_set_speed_value|e_oper_set_shake_value|e_oper_set_lowpower_alarm|e_oper_set_out_alarm|e_oper_set_drop_alarm|e_oper_set_indicatorlight_alarm|e_oper_set_close_alarm|e_oper_set_open_alarm|e_oper_set_off_alarm|e_oper_set_replay_alarm|e_oper_set_sound_alarm|e_oper_set_language|e_oper_set_listen_mode|e_oper_set_phone_book|e_oper_set_center_phone|e_oper_set_fence_alarm|e_oper_set_lbs_switch|e_oper_set_disconn_switch|e_oper_set_wifi_switch|e_oper_set_sleep_intval|e_oper_set_vor_switch|e_oper_set_rechage_sim|e_oper_set_auto_answer|e_oper_set_sos_alarm|e_oper_set_dualoffline_alarm|e_oper_set_sleep_overtime_alarm|e_oper_voice_tts",
    "uid": "8d9c16e4-d900-4b06-a0a3-eada8e77a550"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.account` | `string` | 操作账号 |
| `items.bind_familyname` | `string` | is_summary为true时返回，绑定终端组织名称 |
| `items.bind_phone` | `string` | is_summary为true时返回，绑定手机号 |
| `items.bind_sfamily` | `string` | is_summary为true时返回，绑定终端组织id |
| `items.bind_time` | `number` | 绑定时间(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数) |
| `items.car_number` | `string` | is_summary为true时返回，车牌号 |
| `items.familyname` | `string` | is_summary为true时返回，组织名称 |
| `items.fence_name` | `string` | 围栏名称，操作类型为围栏时返回 |
| `items.groupname` | `string` | is_summary为true时返回，分组名称 |
| `items.host` | `string` | 操作账号所在的ip |
| `items.imei` | `number` | 车载设备IMEI,仅用于展示，可用于下次分页的设备号 |
| `items.remark` | `string` | 操作信息 |
| `items.sfenceid` | `string` | 操作的围栏id，操作类型为围栏时返回 |
| `items.time` | `number` | 操作时间，精确到ms,可用于下次分页的时间(13位长的时间戳，表示从1970.1.1的00:00到该时刻经过的毫秒的总数) |
| `items.version` | `string` | is_summary为true时返回，设备型号 |
| `last_imei` | `number` | 再次请求时，请带上这个imei最为最后一次请求参数。(13位长的时间戳，表示从1970.1.1的00:00到该时刻经过的毫秒的总数)(可用于分页,分页详情请参考 &lt;&lt;前言&gt;&gt; 分页) |
| `last_time` | `number` | 再次请求时，请带上这个时间戳最为最后一次请求参数，(13位长的时间戳，表示从1970.1.1的00:00到该时刻经过的毫秒的总数)(可用于分页,分页详情请参考 &lt;&lt;前言&gt;&gt; 分页) |
| `total` | `number` | 总数 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "b6190885-733d-429a-ab03-01755a58bb9a",
  "items": [
    {
      "account": "ac6a3c16-6011-4afa-994d-65352cc01916",
      "bind_familyname": "4758b771-d8bc-42ab-84d5-f979521d8c41",
      "bind_phone": "3e5ca604-75ef-471f-bd7d-9ab3cdf5f959",
      "bind_sfamily": "0620ee44-0bcc-49f4-bf37-a1f63eefd091",
      "bind_time": 1926467539,
      "car_number": "cb438498-c9d8-4cc1-9744-6ed0b289479c",
      "familyname": "456e4297-abf2-4270-a639-0d573b615d5a",
      "fence_name": "92ff2732-d9db-401e-bf56-b26fed270526",
      "groupname": "5ad6b8cf-46e5-42c7-82a0-7556ae939c8e",
      "host": "e3b67ce1-570e-4b3c-a379-8985cf8c5085",
      "imei": 1926467539,
      "remark": "8c94c276-9a8a-42e3-8098-08ee33be1fd3",
      "sfenceid": "d783baa7-9e7c-4f5d-a5d8-6873f87ee553",
      "time": 1926467539,
      "version": "a17d5ec4-5b80-4ca8-b734-7e1659af5d7f"
    },
    {
      "account": "da406e59-1f64-494f-9073-5b277bc4a02e",
      "bind_familyname": "1b0f05e4-af11-4a32-9fa1-57abc2940344",
      "bind_phone": "5142170e-9e3e-4438-9f0e-1c6f7d3c4ccd",
      "bind_sfamily": "6f445af0-cd23-4f28-8675-f400d2f1d70e",
      "bind_time": 1926467539,
      "car_number": "8b401dea-bfcc-4aaa-a92d-ecd4113315c1",
      "familyname": "365fca11-64d5-41f1-a75e-99f46885b2da",
      "fence_name": "377a5b71-b3aa-4334-ab37-a12726631566",
      "groupname": "77629ef2-e172-4b3d-bba4-07632a31a754",
      "host": "87ab818b-f2c9-4e6f-b8c4-daad99be0362",
      "imei": 1926467539,
      "remark": "69d7880d-375d-4d52-b12f-527ded36a510",
      "sfenceid": "f659435e-36ed-40a2-99b6-af89c819a453",
      "time": 1926467539,
      "version": "6c5aefdb-8ccc-46d7-ab33-11bd5e39d54e"
    },
    {
      "account": "4ccfeffc-7e1b-4931-a137-bec5d70e3013",
      "bind_familyname": "4388f9c2-ecaa-4a89-a2fd-be1587f5a97d",
      "bind_phone": "ab21ee3e-1383-42a7-920f-3f70df6526f2",
      "bind_sfamily": "f7ce7b0c-727d-4eea-b611-32f730f634ea",
      "bind_time": 1926467539,
      "car_number": "2a1e7e59-bb35-4b1a-95a6-9e0fe298362a",
      "familyname": "9566a3d1-25f7-4b9b-8240-538b26acd53e",
      "fence_name": "18c2de7a-3b90-4118-9d64-da6b18d99708",
      "groupname": "572d7bae-102a-43c2-af59-df5cee954429",
      "host": "fb99d82e-6da2-44bf-b011-8473169d4eb7",
      "imei": 1926467539,
      "remark": "4b6c6541-c4c5-45e3-a840-486b69b3589b",
      "sfenceid": "2b6efb35-6827-4258-ab45-728ec3bc7a2d",
      "time": 1926467539,
      "version": "4cfb5a41-ad1f-4890-864b-fa33b943a7b7"
    }
  ],
  "last_imei": 1926467539,
  "last_time": 1926467539,
  "total": 1926467539
}
```

<a id="device-DelFromFamily"></a>
## 将设备从组织中删除 (`DelFromFamily`)

- 接口 ID：`device.DelFromFamily`
- 模块：`device`
- 用途：将设备从组织中删除
- 原始位置：`source/设备(device).txt` 第 1547 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1,只能账号登录；
2，最多一次允许操作1000个；
3，需要从组织中删除的权限；
4，对应权限e_ua_del_from_family和e_ua_modify_dev

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,DelFromFamily |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.is_del_gid` | `boolean` | 如果用sgid删除设备，如果这里填写true，则最后会把gid也删除，默认为false，不删除gid |
| `params.sfamilyid` | `string` | 需要删除指定车组织下面全部设备，simei,sfamilyid,sgid三选一 |
| `params.sgid` | `string` | 需要删除指定分组下面全部设备，simei,sfamilyid,sgid三选一 |
| `params.simei` | `array` | 设备号,限制数量1000，simei,sfamilyid,sgid三选一 |

### 请求示例

```json
{
  "module": "device",
  "func": "DelFromFamily",
  "params": {
    "is_del_gid": true,
    "sfamilyid": "20420ca0-c610-4010-9bb9-fc3c74f13795",
    "sgid": "fe6c1742-7f92-454b-8525-8e29b6dac8ce",
    "simei": [
      "7e05fd68-3e8a-4ab9-9af8-1f823cde43d5",
      "bfa31c3e-2278-4bb5-bfed-da5ae09a70b2",
      "c293a1bd-9c11-4cb1-9e81-da94c2e97c20"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 当没有触发异步操作时，有操作成功的imei号，通过这个字段返回 |
| `fail_items.errcode` | `number` | 错误编码 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |
| `fail_items.simei` | `string` | 错误的设备号(加密) |
| `task_id` | `string` | 如果数据量太多，触发异步操作，这里会返回任务id，如果没有返回，表示同步操作 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "54a7beef-6dcc-4344-978b-032833c7db3e",
  "fail_items": [
    {
      "errcode": 127865290,
      "error_message": "d7539bc3-7e8f-41d3-9fcc-4ecfcc825a21",
      "imei": 127865290,
      "simei": "9b46d190-28b1-489a-b006-5d9f902c2ce2"
    }
  ],
  "task_id": "fa95019b-527d-43be-93f4-17b6967ac89d"
}
```

<a id="device-SetRelaySwitch"></a>
## 设置继电器开关 (`SetRelaySwitch`)

- 接口 ID：`device.SetRelaySwitch`
- 模块：`device`
- 用途：设置继电器开关
- 原始位置：`source/设备(device).txt` 第 1622 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，需要修改设备权限；
2,设备不能为离线状态；
3，对应权限e_ua_modify_relay

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SetRelaySwitch |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.is_force` | `boolean` | 是否强制断油电,需要权限e_ua_force_off_oil |
| `params.pwd_md5` | `string` | 登录密码md5 |
| `params.replay_switch` | `boolean` | 继电器开关(true开，false关) |
| `params.simei` | `string` | 设备号,设备登入，需要修改自己的继电器密码，请调用user模块下的SetAccount |

### 请求示例

```json
{
  "module": "device",
  "func": "SetRelaySwitch",
  "params": {
    "is_force": false,
    "pwd_md5": "46f72225-af19-42a3-bbcb-2e6ed041584d",
    "replay_switch": false,
    "simei": "0614b44c-43d3-4a7d-ba36-180fa7714403"
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
  "error_message": "5e743a63-f6cf-485e-893c-8239d40ef583"
}
```

<a id="device-MoveGarage"></a>
## 设备迁移车组 (`MoveGarage`)

- 接口 ID：`device.MoveGarage`
- 模块：`device`
- 用途：设备迁移车组
- 原始位置：`source/设备(device).txt` 第 1671 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1,只能账号登录；
2，最多一次允许操作1000个；
3，对应权限e_ua_group_opt和e_ua_move_dev

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,MoveGarage |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.sfamilyid` | `string` | 需要转移指定车组织下面全部设备，simei,sfamilyid,sgid三选一 |
| `params.sgid` | `string` | 需要转移指定分组下面全部设备，simei,sfamilyid,sgid三选一 |
| `params.sgid_new` | `string` | 指定的车组 |
| `params.simei` | `array` | 设备号 |

### 请求示例

```json
{
  "module": "device",
  "func": "MoveGarage",
  "params": {
    "sfamilyid": "812af523-4b03-45f0-9443-e65517a9daa4",
    "sgid": "b3ab8bac-56c4-4698-a608-ef9fabbe4c4c",
    "sgid_new": "e2bf5abe-aa3b-4d69-a9f2-592a7ba77874",
    "simei": [
      "1b8593d9-3034-446c-9a9c-2d20b63a2b53",
      "442e960d-6c5d-4248-8f19-0292354f456d"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 当没有触发异步操作时，有操作成功的imei号，通过这个字段返回 |
| `fail_items.errcode` | `number` | 错误编码 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |
| `fail_items.simei` | `string` | 错误的设备号(加密) |
| `task_id` | `string` | 如果数据量太多，触发异步操作，这里会返回任务id，如果没有返回，表示同步操作 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "8d7bad50-b054-4fde-b05f-4b7d35b4d7e4",
  "fail_items": [
    {
      "errcode": 1262410605,
      "error_message": "b1a59aba-8f12-4d23-8f9f-544436e15dd9",
      "imei": 1262410605,
      "simei": "cf1ae176-9ba2-4bcf-903f-6383d7b4c423"
    },
    {
      "errcode": 1262410605,
      "error_message": "2a34f5f4-c9ae-4131-8960-814be12c8524",
      "imei": 1262410605,
      "simei": "529acc03-997e-4410-a9c5-4c4122d28759"
    },
    {
      "errcode": 1262410605,
      "error_message": "f7855e3b-af35-48fe-83c8-0f8f21fe556e",
      "imei": 1262410605,
      "simei": "6d73c585-0aa4-4eb4-a7a5-a0ce1c9d76f6"
    },
    {
      "errcode": 1262410605,
      "error_message": "b999969d-8409-4c94-a3c8-205ae37546aa",
      "imei": 1262410605,
      "simei": "42492621-1312-42df-8581-1fad8a69513d"
    }
  ],
  "task_id": "d27d5a64-2359-4013-a291-f4fb0d21b2b5"
}
```

<a id="device-DelToParent"></a>
## 删除设备回到上级组织 (`DelToParent`)

- 接口 ID：`device.DelToParent`
- 模块：`device`
- 用途：删除设备回到上级组织
- 原始位置：`source/设备(device).txt` 第 1762 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

说明:
1，终端用户删除,不用选择del_type，同步操作，不返回taskid
2，e_del_no_move对应权限e_ua_del_move_family
3，e_del_to_myself对应权限e_ua_del_move_family和e_ua_group_opt
4，e_del_to_root对应的权限是e_ua_del_move_to_root和e_ua_group_opt
5，e_del_to_parent对应的权限是e_ua_del_move_to_parent和e_ua_group_opt

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,DelToParent |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.del_type` | `string` | 删除类型<br>e_del_no_move 清除设备，但是不移动<br>e_del_to_parent 销售商移动到设备上一级组织新建的分组中，终端用户直接删除与关系<br>e_del_to_myself 清除设备，并回到当前操作账号的组织下,将移入新建的分组中<br>e_del_to_root 清除设备，并回到入库状态<br>e_del_unbind_terminal 经销商账户解绑设备所绑定的终端账户 |
| `params.is_del_gid` | `boolean` | 如果用sgid删除设备，如果这里填写true，则最后会把gid也删除，默认为false，不删除gid |
| `params.is_del_increment` | `boolean` | 是否清除设备增值服务开关,需要对应的权限 |
| `params.is_del_start_time` | `boolean` | 是否清除设备激活时间,需要对应的权限 |
| `params.is_reset_platform` | `boolean` | 是否清除设备激活时间的同时,还原设备的有效时长 |
| `params.no_imei` | `array` | 设备号，配合sfamilyid, sgid使用，传入的设备号不删除 |
| `params.sfamilyid` | `string` | 需要删除指定车组织下面全部设备，simei,sfamilyid,sgid三选一 |
| `params.sgid` | `string` | 需要删除指定分组下面全部设备，simei,sfamilyid,sgid三选一 |
| `params.simei` | `array` | 设备号,限制数量1000，simei,sfamilyid,sgid三选一 |

### 请求示例

```json
{
  "module": "device",
  "func": "DelToParent",
  "params": {
    "del_type": "e_del_no_move|e_del_to_parent|e_del_to_myself|e_del_to_root|e_del_unbind_terminal",
    "is_del_gid": true,
    "is_del_increment": true,
    "is_del_start_time": true,
    "is_reset_platform": true,
    "no_imei": [
      205928236,
      205928236
    ],
    "sfamilyid": "7e57a9d1-da68-444c-89b1-feeaef897f82",
    "sgid": "1b221cfa-0e5a-4767-8f73-4f30814a2603",
    "simei": [
      "a3baff26-e9e1-492b-9f88-ff5b631a8cf6",
      "2c9b423b-4da0-4aed-9459-728237966f54"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 当没有触发异步操作时，有操作成功的imei号，通过这个字段返回 |
| `fail_items.errcode` | `number` | 错误编码 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |
| `fail_items.simei` | `string` | 错误的设备号(加密) |
| `task_id` | `string` | 如果数据量太多，触发异步操作，这里会返回任务id，如果没有返回，表示同步操作 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "79db204c-83bf-4181-a312-5572f1e8579f",
  "fail_items": [
    {
      "errcode": 1147170143,
      "error_message": "ada8489d-4ddd-4cb7-87e0-e5d883c621a0",
      "imei": 1147170143,
      "simei": "1a5fc6b8-8e15-4659-ba35-22cd5ae1c4c5"
    },
    {
      "errcode": 1147170143,
      "error_message": "35d33afd-4a15-4660-83c8-0beb8235232e",
      "imei": 1147170143,
      "simei": "bc5ecbbe-d2cf-45f2-947e-d0dff8aee54f"
    },
    {
      "errcode": 1147170143,
      "error_message": "a35bed75-70d1-4e2d-bd39-2f07c585ee8d",
      "imei": 1147170143,
      "simei": "be6bbc5b-83da-44a7-ba3b-c52fb2b95de5"
    },
    {
      "errcode": 1147170143,
      "error_message": "76e142dc-4923-48af-89f3-8a7820dff6e7",
      "imei": 1147170143,
      "simei": "cf1dbe5b-d536-4fe0-a400-ed9cf977f7f5"
    }
  ],
  "task_id": "881fed48-3813-4878-9f9d-82b7edb5cdea"
}
```

<a id="device-Load"></a>
## 导入设备 (`Load`)

- 接口 ID：`device.Load`
- 模块：`device`
- 用途：导入设备
- 原始位置：`source/设备(device).txt` 第 1878 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

说明:
1,对应的权限是e_ua_load_new_dev

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Load |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.device_list` | `array` | 需要导入的设备列表，和imeis 二选一 |
| `params.device_list.car_number` | `string` |  |
| `params.device_list.imei` | `string` |  |
| `params.imeis` | `array` | 需要导入的imei号列表 |
| `params.increment` | `array` | 需要支持的增值服务 |
| `params.platform_time` | `number` | 设备的平台使用时间，单位月 |
| `params.protocol` | `number` | 协议，不填或者填写0，表示支持808, 1：808协议；2：康凯斯 |
| `params.sgid` | `string` | 如果指明需要导入的分组，那么直接导入并且添加到对应的分组 |
| `params.use_default_pwd` | `boolean` | 使用默认密码 |
| `params.version` | `string` | 本批次设备号的默认版本号 |

### 请求示例

```json
{
  "module": "device",
  "func": "Load",
  "params": {
    "device_list": [
      {
        "car_number": "d67fe119-afa4-42d0-aa8a-5e1eab6206a2",
        "imei": "314dd49f-85d4-49a8-9218-6e57106cd08b"
      },
      {
        "car_number": "5b7450e6-d73f-4243-a03c-9e507dfc0f70",
        "imei": "a2762d4e-9550-4fcf-96c6-a30b45641227"
      },
      {
        "car_number": "c759d7fa-3236-4112-95aa-ed9e06959cf7",
        "imei": "aee98166-e008-4f8a-8a70-e04f512e9a1e"
      }
    ],
    "imeis": [
      "4e43e830-6977-45a9-ba70-a2c9a0443212"
    ],
    "increment": [
      1160506004
    ],
    "platform_time": 1160506004,
    "protocol": 1160506004,
    "sgid": "8d9e3311-7e03-489b-923e-87195eb6b264",
    "use_default_pwd": true,
    "version": "e381562d-549f-4b8e-94d0-1162b6e6257c"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 失败的信息 |
| `fail_items.errcode` | `number` | 错误编码 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "6a1b2ca7-aaae-4956-b6ed-35603a7b2a64",
  "fail_items": [
    {
      "errcode": 1184736308,
      "error_message": "c8dba812-e3a6-4d7c-965b-9ff9943f2bba",
      "imei": 1184736308
    },
    {
      "errcode": 1184736308,
      "error_message": "98e780dd-35d1-4c72-8e16-915ae73a2a48",
      "imei": 1184736308
    }
  ]
}
```

<a id="device-ChangeHost"></a>
## 修改设备的主从host (`ChangeHost`)

- 接口 ID：`device.ChangeHost`
- 模块：`device`
- 用途：修改设备的主从host
- 原始位置：`source/设备(device).txt` 第 1978 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

说明:
1,对应的权限是e_ua_change_host

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,ChangeHost |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.host` | `string` | ip地址 |
| `params.is_main` | `boolean` | 是否是主IP，如果false，表示修改从IP,默认true |
| `params.port` | `number` | 端口号 |
| `params.simei` | `array` | 需要设备的设备列表 |

### 请求示例

```json
{
  "module": "device",
  "func": "ChangeHost",
  "params": {
    "host": "27df8c4e-4cf2-4846-b3d2-1853a7a58a7f",
    "is_main": false,
    "port": 125198681,
    "simei": [
      "e4b2dbdf-89bb-4c34-94c0-b4dfaeb0c8e7",
      "cbde51e6-2ec0-426e-b1c2-97ca883084d2",
      "54a1c312-cfac-4ce0-a5f6-e62c0c35a776",
      "a768ef44-b00f-4724-af25-f4fd37a73d0b"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 当没有触发异步操作时，有操作成功的imei号，通过这个字段返回 |
| `fail_items.errcode` | `number` | 错误编码 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |
| `fail_items.simei` | `string` | 错误的设备号(加密) |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "7387c912-025c-4f10-9c19-c927cf0fe84e",
  "fail_items": [
    {
      "errcode": 455667517,
      "error_message": "2fe0170e-578d-4612-b36f-263ada1414b1",
      "imei": 455667517,
      "simei": "f1ae886a-3dc3-43ce-92fa-ee94a00ffc41"
    },
    {
      "errcode": 455667517,
      "error_message": "0e44fbc5-e6c3-4867-b6e3-5dd1eea62641",
      "imei": 455667517,
      "simei": "9bf5b33b-2f1f-4ef7-8035-14e5477ce60c"
    },
    {
      "errcode": 455667517,
      "error_message": "baef9b71-f6cb-458b-b2e0-47e526fcbb1c",
      "imei": 455667517,
      "simei": "20209f69-0e92-4364-9bd8-718d7a326620"
    },
    {
      "errcode": 455667517,
      "error_message": "a40858cf-3e6b-4d48-9dbe-d8b949467471",
      "imei": 455667517,
      "simei": "2f1e9e4a-9f61-4435-9abd-00f72313c3fd"
    }
  ]
}
```

<a id="device-BatchCmd"></a>
## 批量下发指令 (`BatchCmd`)

- 接口 ID：`device.BatchCmd`
- 模块：`device`
- 用途：批量下发指令
- 原始位置：`source/设备(device).txt` 第 2066 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

说明:
1,e_cmd_log_switch对应的权限是e_ua_modify_log_switch

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,BatchCmd |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.cmd` | `string` | 需要下发的指令<br>e_cmd_log_switch 设备日志开关，设置value附带值,如果0关,1开<br>e_cmd_set_remote_upgrade 下发设置终端远程升级功能<br>e_cmd_set_dev_type 设置设备型号，设置str_value附带值,例如M1<br>e_cmd_set_protocol 更换协议，设置value附带值,1：JT808/2011；2：JT808/2013；3：JT808/2019；100：康凯斯ET130版本；101：康凯斯GT06E版本；110：天琴协议；默认1<br>e_cmd_set_locat 设置设备定位优先级<br>e_cmd_close_imei |
| `params.simei` | `array` | 需要设备的设备列表 |
| `params.str_value` | `string` | 指令附带的值 |
| `params.value` | `number` | 指令附带的值 |

### 请求示例

```json
{
  "module": "device",
  "func": "BatchCmd",
  "params": {
    "cmd": "e_cmd_log_switch|e_cmd_set_remote_upgrade|e_cmd_set_dev_type|e_cmd_set_protocol|e_cmd_set_locat|e_cmd_close_imei",
    "simei": [
      "823520e5-cdaf-4df4-b124-b0bae5e331a4"
    ],
    "str_value": "fd845c8e-cd17-4d8a-ad6d-8791fd6aeaa8",
    "value": 1851910532
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 当没有触发异步操作时，有操作成功的imei号，通过这个字段返回 |
| `fail_items.errcode` | `number` | 错误编码 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |
| `fail_items.simei` | `string` | 错误的设备号(加密) |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "c0ab3121-c564-49d3-9861-ab83f3c272d7",
  "fail_items": [
    {
      "errcode": 1565970960,
      "error_message": "b029f19c-608d-48c0-824c-011bda01299e",
      "imei": 1565970960,
      "simei": "ec86462a-3afc-4a82-93f8-537bbcf5933b"
    },
    {
      "errcode": 1565970960,
      "error_message": "82c38d49-4a36-40d9-a32f-82f940b67191",
      "imei": 1565970960,
      "simei": "eb94b004-6352-46d1-b975-9fc431e01b06"
    },
    {
      "errcode": 1565970960,
      "error_message": "fdded532-090f-4cfc-b941-b7591426aace",
      "imei": 1565970960,
      "simei": "71e63dea-3c02-4f1f-afb0-61fe58e42909"
    },
    {
      "errcode": 1565970960,
      "error_message": "96be3f8e-3fb2-4865-9d82-706515406e2a",
      "imei": 1565970960,
      "simei": "e23a9267-d9d4-4893-8886-43d9aac7c8eb"
    }
  ]
}
```

<a id="device-UnBindPhone"></a>
## 设备解绑电话号码 (`UnBindPhone`)

- 接口 ID：`device.UnBindPhone`
- 模块：`device`
- 用途：设备解绑电话号码
- 原始位置：`source/设备(device).txt` 第 2157 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,UnBindPhone |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.simei` | `array` | 需要设备的设备列表 |

### 请求示例

```json
{
  "module": "device",
  "func": "UnBindPhone",
  "params": {
    "simei": [
      "ccdcb617-444b-41bc-8c93-141a830b271b",
      "3e2cf57b-da24-4db9-b224-b4f02e44d95d"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 当没有触发异步操作时，有操作成功的imei号，通过这个字段返回 |
| `fail_items.errcode` | `number` | 错误编码 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |
| `fail_items.simei` | `string` | 错误的设备号(加密) |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "b9c4b34c-eeca-41f9-8062-257d9fc6b525",
  "fail_items": [
    {
      "errcode": 1904600564,
      "error_message": "8e6d6adf-eca3-4d02-aa45-df7db7da7506",
      "imei": 1904600564,
      "simei": "3c0708d7-148d-4022-8c23-272b9baa1127"
    },
    {
      "errcode": 1904600564,
      "error_message": "e36aaa94-4992-4e0b-b2b3-9179e4b05c31",
      "imei": 1904600564,
      "simei": "2c41de28-52df-4b1a-a589-fd4d9adcbedb"
    },
    {
      "errcode": 1904600564,
      "error_message": "e3b6bf75-fe14-4bb7-a11d-29fb3eaf6d8a",
      "imei": 1904600564,
      "simei": "7e7bb85d-3953-4340-bc6f-a3feab0cbda6"
    },
    {
      "errcode": 1904600564,
      "error_message": "0ef4e2be-e039-47bb-b7a1-9d4d27159210",
      "imei": 1904600564,
      "simei": "714518a4-f183-4979-8e10-91259da0a95b"
    }
  ]
}
```

<a id="device-FindDev"></a>
## 查找设备 (`FindDev`)

- 接口 ID：`device.FindDev`
- 模块：`device`
- 用途：查找设备
- 原始位置：`source/设备(device).txt` 第 2231 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,FindDev |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.iccids` | `array` | 匹配的iccid |
| `params.imeis` | `array` | 指定查找的imei号，传入imei号列表进行精准搜索 |
| `params.is_get_child` | `boolean` | 查找范围是否包含指定组织的下级，需搭配sfid使用，当sfid不为空时生效 |
| `params.last_imei` | `number` | 上一次调用该接口返回的最后一条imei |
| `params.limit_size` | `number` | 限制个数，默认10个，越多，速度越慢 |
| `params.sfid` | `string` | 指定的组织id，也可以不指定，全范围搜索 |
| `params.sgid` | `string` | 指定的分组id，也可以不指定，全范围搜索 |

### 请求示例

```json
{
  "module": "device",
  "func": "FindDev",
  "params": {
    "iccids": [
      "5397803d-4adf-4048-87b3-16bb339d696c",
      "0a2cad0b-6c84-4051-a693-3d005a08bf9c"
    ],
    "imeis": [
      "0dec4b02-5441-4167-8de6-7972989f5518",
      "50765d8f-7c6b-4c57-bee7-a6c97ad82e3f",
      "b4a7b564-a24e-4a0b-a325-20d12679e6ce",
      "fdb21209-c8e2-48a5-95a7-29342aa3bf37"
    ],
    "is_get_child": false,
    "last_imei": 1917464199,
    "limit_size": 1917464199,
    "sfid": "265b6105-560c-4da2-b9a5-12b0d105d1eb",
    "sgid": "8bdde7da-5e64-4b1f-9f1a-c0a61e8b1b98"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` | 返回设备号信息 |
| `items.car_image` | `number` | 车图标 |
| `items.car_number` | `string` | 车牌号 |
| `items.expire` | `boolean` | 是否过期 |
| `items.imei` | `number` | 设备号 |
| `items.is_human_dev` | `boolean` | true为人员款设备，false为车载款设备 |
| `items.is_wireless_dev` | `boolean` | true为无线款设备, false为接线款设备 |
| `items.sfid` | `string` | 组织id |
| `items.sgid` | `string` | 分组id |
| `items.simei` | `string` | 加密设备号 |
| `items.state` | `string` | 在线状态 |
| `items.version` | `string` | 版本号 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "3b3e6fbe-4ae8-4ae1-bd4d-43f153542f82",
  "items": [
    {
      "car_image": 874244136,
      "car_number": "35fea597-a221-4273-9bfd-6efa0ce1cd8a",
      "expire": true,
      "imei": 874244136,
      "is_human_dev": true,
      "is_wireless_dev": true,
      "sfid": "b8c5408b-a662-4804-bbe2-1655e6e31d2c",
      "sgid": "38b86c5f-d1ae-4cff-b9dd-59fbd9bdd47a",
      "simei": "4a2a91f7-caa6-41a8-bce0-e870759133cd",
      "state": "e_line_down|e_line_on|e_line_sleep",
      "version": "26e8f0b5-eaea-4318-a170-b00ad9244879"
    }
  ]
}
```

<a id="device-DevVersion"></a>
## 系统支持的设备版本号 (`DevVersion`)

- 接口 ID：`device.DevVersion`
- 模块：`device`
- 用途：系统支持的设备版本号
- 原始位置：`source/设备(device).txt` 第 2337 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,DevVersion |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.all_protocol` | `boolean` | 是否获取所有的协议 |
| `params.filter_fun` | `string` | 按功能型号表支持的功能进行过滤<br>e_func_haodog  haodog款<br>e_func_expire_show_loc  过期设备展示定位<br>e_func_wx_voice  微信语音通话 |
| `params.get_protocol` | `boolean` | 设备所用协议 |

### 请求示例

```json
{
  "module": "device",
  "func": "DevVersion",
  "params": {
    "all_protocol": false,
    "filter_fun": "e_func_haodog|e_func_expire_show_loc|e_func_wx_voice",
    "get_protocol": false
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `protocol` | `array` | 协议信息 |
| `protocol.is_show` | `boolean` | 是否可操作性 |
| `protocol.name` | `string` | 名称 |
| `protocol.pid` | `number` | id |
| `vers` | `array` | 版本号列表 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "8405139b-17cd-4053-a0a1-b87f8015f67e",
  "protocol": [
    {
      "is_show": false,
      "name": "2be31921-ed19-44c2-8996-e3046f3de7f5",
      "pid": 1691628693
    },
    {
      "is_show": false,
      "name": "265db855-70ff-4c91-a57b-eb036a5f6d3b",
      "pid": 1691628693
    },
    {
      "is_show": false,
      "name": "2c1e92a5-ce0b-4037-b45b-f51257705e99",
      "pid": 1691628693
    }
  ],
  "vers": [
    "10be9af6-8234-433b-9937-40384f785bfb"
  ]
}
```

<a id="device-VersionFun"></a>
## 版本号对应的功能 (`VersionFun`)

- 接口 ID：`device.VersionFun`
- 模块：`device`
- 用途：版本号对应的功能
- 原始位置：`source/设备(device).txt` 第 2411 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,VersionFun |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.ver` | `string` | 需要查找的版本号 |

### 请求示例

```json
{
  "module": "device",
  "func": "VersionFun",
  "params": {
    "ver": "d289aab3-ad5a-44ac-b35b-7163d79b2b77"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `alarm_list` | `array` | 该型号支持的所有告警 |
| `alarm_list.func` | `string` | 功能标识 |
| `alarm_list.func_desc` | `string` | 功能参数描述 |
| `alarm_list.param` | `string` | 功能参数 |
| `alarm_list.support` | `number` | 是否支持, -1不支持，0支持 |
| `alarm_list.type` | `string` | 功能所属类别<br>e_config_config 报警相关<br>e_config_phone 电话相关<br>e_config_other 其它所有<br>e_config_all 获取全部配置<br>e_config_common 获取通用配置功能&lt;工具， 测试&gt;<br>e_config_alarm 获取报警功能列表 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fun` | `array` | 功能列表 |
| `mode_list` | `array` | 支持的定位模式 |
| `mode_list.mid` | `number` | 模式id |
| `mode_list.msg` | `string` | 被前端展示的说明 |
| `mode_list.name` | `string` | 名称 |
| `mode_list.param` | `string` | 需要的附加信息 |
| `mode_list.uid` | `number` | UI类型 |
| `oiloff` | `string` | 断油电的方式<br>e_oo_invalid 不支持<br>e_oo_old 老的断油电<br>e_oo_state 结合传感器状态断油电 |
| `one_restart` | `string` | 一键重启:0-不支持,1-支持(离线不支持),2-只支持在线<br>e_onerestart_invalid 不支持<br>e_onerestart_open 支持<br>e_onerestart_only_online 只支持在线 |
| `pid` | `array` | 支持的协议号 |

### 返回示例

```json
{
  "errcode": 0,
  "alarm_list": [
    {
      "func": "e6c0cfa4-8848-4dc6-88cb-da391eef59e4",
      "func_desc": "c7b77d27-4200-4bb7-be9f-0b33c71c013c",
      "param": "c166c3b6-675a-48a2-b5ab-344d4f49e705",
      "support": 961160395,
      "type": "min|e_config_config|e_config_phone|e_config_other|e_config_all|e_config_common|e_config_alarm"
    },
    {
      "func": "eca38068-d9c8-4fde-a993-ebfc10cf7ca4",
      "func_desc": "95c53a4c-423e-4ab1-ba77-68c709f0bf79",
      "param": "1263386f-8f7d-451c-9bb4-edc3a3ce7c61",
      "support": 961160395,
      "type": "min|e_config_config|e_config_phone|e_config_other|e_config_all|e_config_common|e_config_alarm"
    }
  ],
  "error_message": "38ad5d4b-28f9-4243-b79d-826e908d50dd",
  "fun": [
    "71ccffa3-f24a-47b3-96ae-fa2f0f6aec89",
    "a92c8452-c982-462a-aeed-f822bfa4f201",
    "908621a1-cca8-49a3-81cb-6ae3741549d6",
    "9f0e3f61-d873-4ad7-a387-ddbf6b7f2ef4"
  ],
  "mode_list": [
    {
      "mid": 961160395,
      "msg": "a615571c-3cfc-4c0f-b65e-250eb79ae6d7",
      "name": "52a30721-fa0c-4572-9020-7f7af5b1babf",
      "param": "cede0b46-dddc-405f-b6ed-30e5154c73fc",
      "uid": 961160395
    }
  ],
  "oiloff": "e_oo_invalid|e_oo_old|e_oo_state",
  "one_restart": "e_onerestart_invalid|e_onerestart_open|e_onerestart_only_online",
  "pid": [
    961160395,
    961160395,
    961160395,
    961160395
  ]
}
```

<a id="device-AddForTerm"></a>
## 经销商给终端组织添加设备 (`AddForTerm`)

- 接口 ID：`device.AddForTerm`
- 模块：`device`
- 用途：经销商给终端组织添加设备
- 原始位置：`source/设备(device).txt` 第 2529 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,AddForTerm |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.is_del_bind` | `boolean` | 是否删除存在的绑定关系，默认不删除 |
| `params.simei` | `array` | 需要添加的设备 |
| `params.suid` | `string` | 需要添加的终端对象,可通过GetTermUidByAccount接口获取 |

### 请求示例

```json
{
  "module": "device",
  "func": "AddForTerm",
  "params": {
    "is_del_bind": true,
    "simei": [
      "d33930ea-1d74-4495-a271-ee27cf2ff180",
      "d1914101-eb3c-4627-89f2-7e1dece57dd6"
    ],
    "suid": "e240ebef-ad6e-47a3-a3fe-7558c82cc178"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 失败的信息 |
| `fail_items.errcode` | `number` | 错误编码，账号登录部分设备号错误，错误码在这里返回。 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |
| `suc_items` | `array` | 已经添加上的imei信息 |
| `suc_items.imei` | `number` | 设备的imei |
| `suc_items.simei` | `string` | 设备id |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "c8a13c23-dd5f-4086-88ff-8558dc49c51e",
  "fail_items": [
    {
      "errcode": 1020820252,
      "error_message": "cbd40675-78f2-40fd-a918-26b35a084479",
      "imei": 1020820252
    },
    {
      "errcode": 1020820252,
      "error_message": "cd212afe-e197-4360-83bc-61c274fc0c90",
      "imei": 1020820252
    }
  ],
  "suc_items": [
    {
      "imei": 1020820252,
      "simei": "d74bcdae-91ba-4204-a183-f8802ff9e5a4"
    },
    {
      "imei": 1020820252,
      "simei": "c57775bb-0a1b-43fb-9ad9-0df155a67581"
    },
    {
      "imei": 1020820252,
      "simei": "523abd9d-65cb-49e9-988a-f98d32865e2a"
    }
  ]
}
```

<a id="device-BatchSetDetail"></a>
## 批量设置设备信息 (`BatchSetDetail`)

- 接口 ID：`device.BatchSetDetail`
- 模块：`device`
- 用途：批量设置设备信息
- 原始位置：`source/设备(device).txt` 第 2613 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,BatchSetDetail |
| `module` | `string` | 模块名,device |
| `params` | `object` | 参数列表 |
| `params.items` | `array` |  |
| `params.items.car_image` | `number` | 车辆图标 |
| `params.items.car_number` | `string` | 车牌号 |
| `params.items.car_type` | `string` | 车辆类型 |
| `params.items.center_phone` | `string` | 中心号码 |
| `params.items.engine_num` | `string` | 发动机号 |
| `params.items.frame_num` | `string` | 车架号车辆品牌 |
| `params.items.img_other` | `string` | 身份证反面 |
| `params.items.img_pos` | `string` | 身份证正面 |
| `params.items.scar_safe_time` | `string` | 车辆保险 格式如:2018-10-19,清空为0000-00-00 |
| `params.items.scar_year_check` | `string` | 车辆年检 格式如:2018-10-19,清空为0000-00-00 |
| `params.items.simei` | `string` | 需要修改的imei,如果设备登入，不用填，账号登录需要填。 |
| `params.items.user_addr` | `string` | 车主地址 |
| `params.items.user_cer` | `string` | 车主证件号码 |
| `params.items.user_image` | `number` | 车主形象（图标） |
| `params.items.user_mail` | `string` | 车主e_mail |
| `params.items.user_name` | `string` | 车主姓名 |
| `params.items.user_phone` | `string` | 用户手机号码 |
| `params.items.user_sex` | `string` | 车主性别<br>e_sex_man 男<br>e_sex_woman 女 |
| `params.items.user_unit` | `string` | 车主工作单位 |

### 请求示例

```json
{
  "module": "device",
  "func": "BatchSetDetail",
  "params": {
    "items": [
      {
        "car_image": 739012728,
        "car_number": "c405f932-b8e2-4579-a679-1244b5cc7384",
        "car_type": "3de2a89b-e77a-45db-bf47-4fc6f62a754b",
        "center_phone": "51d13e57-b3ef-4307-9914-fae1ac3345d4",
        "engine_num": "776dcac5-c59a-41e1-8c8b-bc80b0d5fa62",
        "frame_num": "056607a6-a849-4686-9731-596dce6284b2",
        "img_other": "dd2c88a5-b777-4ca7-aaa7-166fdf3c901c",
        "img_pos": "12570db0-9105-41ac-8ac7-6eb38979391d",
        "scar_safe_time": "c00c0e1f-748b-4670-a578-ff9e8f8374d9",
        "scar_year_check": "ebd35c79-0fa5-404c-97a3-bfb0319bc7f1",
        "simei": "a205d95c-b74c-44dd-9609-57fd1772f4e2",
        "user_addr": "2e2a6826-a834-41d3-a7bd-8882009b9890",
        "user_cer": "495e60dd-7817-474b-adbb-dcd4927518c2",
        "user_image": 739012728,
        "user_mail": "8663df96-831b-46a6-95eb-8f5cc5102386",
        "user_name": "480409b2-8b79-4a9a-a185-dcc01854e6ed",
        "user_phone": "0ff6a04c-57d2-4380-a41e-1b980df76ea7",
        "user_sex": "e_sex_man|e_sex_woman",
        "user_unit": "bef06852-27c8-4941-8432-18e57145f3f7"
      },
      {
        "car_image": 739012728,
        "car_number": "a581d343-a606-473b-8605-f080f6be4df5",
        "car_type": "a35cffee-117f-43a4-be57-7e3a03fc6cd6",
        "center_phone": "352e72ec-daa7-41fe-964b-e4401381c8fe",
        "engine_num": "c0ada155-65e3-4782-9a29-7a190c862577",
        "frame_num": "5245e208-6a30-49f3-bffa-532235165627",
        "img_other": "26347129-f9e5-4e6a-b871-a4d3b23a0325",
        "img_pos": "05ad1e73-0392-4bcc-82b2-7df173bcb4e9",
        "scar_safe_time": "9e271964-c139-41dd-b963-4d49bb9b0b20",
        "scar_year_check": "b8a5951a-e1b6-4992-b66c-cc0592eecce3",
        "simei": "49a779d8-21d6-4fb1-81d3-c53f18db37f0",
        "user_addr": "d4ebc310-a468-4829-b8e8-2e31c2ae5e95",
        "user_cer": "f401b6b8-541c-4bab-b5ba-489fdbce7e89",
        "user_image": 739012728,
        "user_mail": "7dd19b91-5783-4986-8e82-1d0bef740bcb",
        "user_name": "d863a345-4854-4a2a-9cea-3ee654222bd7",
        "user_phone": "7040a39b-a65f-4673-b809-12a034f28703",
        "user_sex": "e_sex_man|e_sex_woman",
        "user_unit": "c139a613-2d11-470f-934e-d286d86966d9"
      }
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 失败的设备信息 |
| `fail_items.errcode` | `number` | 错误编码 |
| `fail_items.error_message` | `string` | 错误信息 |
| `fail_items.imei` | `number` | 错误的imei号 |
| `fail_items.simei` | `string` | 错误的设备号(加密) |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "188979c9-5445-46c6-b81b-255c2071c6fc",
  "fail_items": [
    {
      "errcode": 224466,
      "error_message": "36554105-b99d-414e-88ba-38c738f04ee7",
      "imei": 224466,
      "simei": "01940537-b6b4-4760-ac48-5643b813116a"
    },
    {
      "errcode": 224466,
      "error_message": "301872bc-e69e-4e42-a322-7a0035f45e49",
      "imei": 224466,
      "simei": "b98b4fa9-44e3-4809-85ed-4bff10085ea2"
    },
    {
      "errcode": 224466,
      "error_message": "09bdb94e-739a-4ca7-a5bd-ee58445bed8a",
      "imei": 224466,
      "simei": "9ca9d8cf-ecd0-43a6-b466-3916c1853a75"
    }
  ]
}
```
