# 设备车组 (Family)

- 模块标识：`family`
- 接口数量：21
- 原始文档：`source/车组(family).txt`
- 正式环境：`http://cciot.cc/mapi`
- 测试环境：`http://cs.iotiot.cc/mapi`
- 开发环境：`http://192.168.1.20/mapi`
- 模块负责人：fushengyou,fange

## 接口索引

| 接口 | 名称 | 请求字段数 | 返回字段数 |
| --- | --- | ---: | ---: |
| [`GetDeviceList`](#family-GetDeviceList) | 获取设备列表 | 21 | 45 |
| [`AddDevGroup`](#family-AddDevGroup) | 添加车组 | 5 | 3 |
| [`SetDevGroupName`](#family-SetDevGroupName) | 设置车组名称 | 5 | 2 |
| [`DelDevGroup`](#family-DelDevGroup) | 删除车组 | 4 | 2 |
| [`UntieDevGroup`](#family-UntieDevGroup) | 解绑车组 | 4 | 3 |
| [`BindDevGroup`](#family-BindDevGroup) | 绑定车组 | 5 | 3 |
| [`GetTask`](#family-GetTask) | 查询任务详情 | 4 | 9 |
| [`GetRunInfo`](#family-GetRunInfo) | 实时刷新设备运行状态 | 12 | 20 |
| [`SetFamilyName`](#family-SetFamilyName) | 设置组织名称 | 5 | 2 |
| [`GetFamilyList`](#family-GetFamilyList) | 获取车组列表 | 12 | 21 |
| [`DelNextFamily`](#family-DelNextFamily) | 删除下级组织 | 4 | 3 |
| [`GetFamilyInfo`](#family-GetFamilyInfo) | 获取上级各层级的组织信息 | 7 | 8 |
| [`ModifyFamilyContact`](#family-ModifyFamilyContact) | 组织联系方式 | 5 | 2 |
| [`GetContact`](#family-GetContact) | 获取联系方式 | 5 | 4 |
| [`FindFamily`](#family-FindFamily) | 对组织进行模糊搜索 | 9 | 5 |
| [`SetFamilyConfig`](#family-SetFamilyConfig) | 设置组织配置信息 | 19 | 2 |
| [`QueryFamilyConfig`](#family-QueryFamilyConfig) | 获取组织配置信息 | 5 | 16 |
| [`QueryTermByBussness`](#family-QueryTermByBussness) | 通过经销商组织查询绑定终端组织列表 | 10 | 8 |
| [`MoveFamily`](#family-MoveFamily) | 迁移下级组织 | 5 | 2 |
| [`GetFamilyConfig`](#family-GetFamilyConfig) | 获取组织的配置信息 | 6 | 4 |
| [`SetFamilyCustomConfig`](#family-SetFamilyCustomConfig) | 设置组织个性化配置 | 6 | 2 |

## 接口详情

<a id="family-GetDeviceList"></a>
## 获取设备列表 (`GetDeviceList`)

- 接口 ID：`family.GetDeviceList`
- 模块：`family`
- 用途：获取设备列表
- 原始位置：`source/车组(family).txt` 第 53 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件:
1，账号登录情况下参数必须要有familyid或者groupid，设备登录可以不填；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetDeviceList |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.busness_familyid` | `string` | 如果是经销商拉取终端组织，传入对应的经销商id，将返回is_in作为判断是否是改经销商的设备 |
| `params.car_number` | `array` | 过筛条件:车牌号 |
| `params.end_dev_time` | `number` | 设置该值，就会只查询出过期时间在end_dev_time之前的设备,(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数) |
| `params.familyid` | `string` | 用户组织id--familyid,familyid和下面参数groupid二选一,如果设备登入，两个都不填 |
| `params.get_increment` | `boolean` | 是否获取该设备支持的全部增值服务，默认为false。 |
| `params.get_total` | `boolean` | 是否获取所有设备的总值。如果为true就获取在线，离线，下线统计总值,默认false |
| `params.groupid` | `string` | 车组id--=groupid,,groupid和上面参数familyid二选一,如果设备登入，两个都不填 |
| `params.is_get_follow` | `boolean` | 是否获取关注设备信息，默认不获取 |
| `params.is_get_sim` | `boolean` | 是否获取sim卡信息，默认不获取 |
| `params.last_sgid` | `string` | 上一次请求的最后一个sgid(用于分页，分页详情请参考 &lt;&lt;前言&gt;&gt; 分页) |
| `params.last_simei` | `string` | 上一次最后获取到的simei(用于分页,分页详情请参考 &lt;&lt;前言&gt;&gt; 分页) |
| `params.limit_size` | `number` | 限定获取在设备数量,默认100,最大1000 |
| `params.protocol` | `number` | 过筛条件:设备协议 |
| `params.simeis` | `array` | 过滤条件:多个加密的设备号 |
| `params.state` | `string` | 查询在线状态，不传入，表示查询全部(如果输入e_line_on，那么只会查询出在线状态下的设备)<br>e_line_down 不在线<br>e_line_on 在线<br>e_line_sleep 睡眠 |
| `params.states` | `array` | 多状态筛选，可传多个状态，如果state与本字段同时传入，状态将合并进行筛选<br>e_line_down 不在线<br>e_line_on 在线<br>e_line_sleep 睡眠 |
| `params.term_familyid` | `string` | 过筛条件:终端组织 |
| `params.version` | `array` | 过筛条件:设备固件版本 |

### 请求示例

```json
{
  "module": "family",
  "func": "GetDeviceList",
  "params": {
    "busness_familyid": "44b6860a-953f-461a-87e4-3864f342e094",
    "car_number": [
      "5910429c-030d-4413-8ec4-58a8ba4ffad4"
    ],
    "end_dev_time": 1406020526,
    "familyid": "0bcc4bc5-6c70-47fc-9477-46e7580cd3f7",
    "get_increment": true,
    "get_total": true,
    "groupid": "3c4f4174-c35f-465c-9ee2-ec731cccc262",
    "is_get_follow": true,
    "is_get_sim": true,
    "last_sgid": "efe59a65-39e9-4dab-b127-6d11e284d239",
    "last_simei": "1c370726-d225-4ff5-b8ae-77ddef27dfa1",
    "limit_size": 1406020526,
    "protocol": 1406020526,
    "simeis": [
      "415147ac-e108-4c5f-bc44-13960300c503",
      "4b388096-d03b-482a-bee0-20577ae9cd51",
      "797491e9-a8cb-4db0-8d65-384182261338"
    ],
    "state": "e_line_down|e_line_on|e_line_sleep",
    "states": [
      "e_line_down|e_line_on|e_line_sleep",
      "e_line_down|e_line_on|e_line_sleep",
      "e_line_down|e_line_on|e_line_sleep"
    ],
    "term_familyid": "44580348-c069-4a18-9e6e-1ba39c3baf50",
    "version": [
      "323608d5-d965-45de-ac22-4ed8ec903026"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `all_total` | `number` | 设备总数,get_total值为true的时候返回 |
| `down_total` | `number` | 下线数,get_total值为true的时候返回 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` | 车组信息（如果是传入组织id，那么返回该组织下的所有车组和车组下挂载的所有设备，如果是传入车组id只会返回当前车组下挂载的所有设备，如果是设备登录返回该设备的信息） |
| `items.acc_power` | `string` | 电源状态acc<br>e_acc_power_charge 充电<br>e_acc_power_interuupt 断电 |
| `items.bind_phone` | `string` | 绑定手机号 |
| `items.bind_time` | `number` | 绑定时间，(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数) |
| `items.bluetooth_flag` | `number` | 猎狗蓝牙标识，0不支持蓝牙,1支持蓝牙 |
| `items.car_image` | `number` | 车辆图标 |
| `items.car_number` | `string` | 车牌号 |
| `items.create_time` | `number` | 入库时间 |
| `items.day_distance` | `number` | 日里程，如果没有日里程，那么该值为0 |
| `items.end_dev_time` | `number` | 设备到期时间 |
| `items.expire` | `boolean` | 设备是否过期，true过期，false没过期 |
| `items.familyname` | `string` | 所属组织名称 |
| `items.groupname` | `string` | 车组名称 |
| `items.iccid` | `string` | 设备绑定iccid |
| `items.imei` | `number` |  |
| `items.increment` | `array` | 增值服务,默认不返回，开启了get_increment之后才返回 |
| `items.increment_alarm_day` | `number` | 增值服务告警预警天数,get_increment为true时返回，不支持的不返回 |
| `items.is_in` | `boolean` | 如果传入busness_familyid,在对应的组织中，返回true |
| `items.is_wireless` | `boolean` | true-无线，false-有线 |
| `items.last_com_time` | `number` | 最后一次通讯时间,(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数) |
| `items.last_pos` | `string` | 位置信息,json格式，火星坐标为(lat,lon)(已弃用),84坐标系为wgs("22.641872,114.033431")如:"{\\"addr\\":\\"龙华区****\\",\\"lat\\":22641872,\\"lon\\":114033431,\\"wgs\\":\\"22.641872,114.033431\\",\\"ptype\\":\\"e_location_nomal\\",\\"time\\":1654660669,\\"type\\":\\"e_wifi\\",}" |
| `items.last_sgid` | `string` | 分页参数 |
| `items.last_simei` | `string` | 分页参数 |
| `items.phone` | `string` | 设备卡号,默认不返回，只有is_get_sim为true时返回 |
| `items.power` | `number` | 设备电量,范围为(0-100) |
| `items.pro_name` | `string` | 协议名称 |
| `items.protocol` | `number` | 协议 |
| `items.remark` | `string` | 设备备注 |
| `items.sgid` | `string` | 车组id |
| `items.signal_rate` | `number` | 信号强度 |
| `items.simei` | `string` | 设备的加密id，如果查看设备为自己的上级所归属的设备那么该simei会被隐藏一些（7281****347834） |
| `items.start_dev_time` | `number` | 设备开始使用时间,(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数) |
| `items.state` | `string` | 设备状态<br>e_line_down 不在线<br>e_line_on 在线<br>e_line_sleep 睡眠 |
| `items.state_begin_time` | `number` | 当前状态的起始时间（例如当前状态为在线，那么该字段就是表示在线状态的起始时间可能不等于设备的开始使用时间start_dev_time）(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数) |
| `items.state_time` | `number` | 截至当前的状态运行时间,单位为秒(当前状态所处的时间与该状态开始时间的时间差) |
| `items.support_wx_voice` | `boolean` | true-支持微信语音，false或不返回-不支持微信语音 |
| `items.ver` | `string` | 设备型号, 不携带版本信息 |
| `items.version` | `string` | 设备类型 |
| `line_total` | `number` | 在线数,get_total值为true的时候返回 |
| `sleep_total` | `number` | 睡眠数,get_total值为true的时候返回 |
| `total` | `number` | 当前查询条件满足的全部设备总数，每次都会返回 |

### 返回示例

```json
{
  "errcode": 0,
  "all_total": 662716129,
  "down_total": 662716129,
  "error_message": "7911d3dd-d27b-44d0-884d-cdd08778374d",
  "items": [
    {
      "acc_power": "e_acc_power_charge|e_acc_power_interuupt",
      "bind_phone": "e3504082-d93e-4991-8a17-94b3f1bedd2e",
      "bind_time": 662716129,
      "bluetooth_flag": 662716129,
      "car_image": 662716129,
      "car_number": "48e8415d-09e7-4f77-bd59-d21663aa8ea8",
      "create_time": 662716129,
      "day_distance": 662716129,
      "end_dev_time": 662716129,
      "expire": false,
      "familyname": "e5bdd692-8c50-4325-ad18-6a2a19089e4b",
      "groupname": "71154bc2-24e7-4682-b9e1-35f4f8e88f62",
      "iccid": "9cbeed00-71c4-4e1f-9a47-1b16ec970c38",
      "imei": 662716129,
      "increment": [
        662716129,
        662716129,
        662716129,
        662716129
      ],
      "increment_alarm_day": 662716129,
      "is_in": false,
      "is_wireless": false,
      "last_com_time": 662716129,
      "last_pos": "3acaf6dc-b52f-4727-bdef-87a0d35ee808",
      "last_sgid": "04583e62-7cae-433a-916b-12aaf8ac41e1",
      "last_simei": "7ea41969-9bff-44a6-a60e-c53f1f924788",
      "phone": "44120cb7-0c6c-45f1-ae61-ac0ac63e8d5c",
      "power": 662716129,
      "pro_name": "2e47418d-7e9d-4f74-8f27-7e1b9923306c",
      "protocol": 662716129,
      "remark": "55e7bf5c-2fdc-40c4-9889-36885d0991b7",
      "sgid": "e153b96e-f14d-400e-b352-65a2c4729388",
      "signal_rate": 662716129,
      "simei": "784ac552-4515-4078-8476-0d4f3bdf9657",
      "start_dev_time": 662716129,
      "state": "e_line_down|e_line_on|e_line_sleep",
      "state_begin_time": 662716129,
      "state_time": 662716129,
      "support_wx_voice": false,
      "ver": "c22487ce-1dda-4ff4-a855-8cbcb67f1300",
      "version": "4f2118e9-4cb0-454f-b2eb-90c44c8263df"
    },
    {
      "acc_power": "e_acc_power_charge|e_acc_power_interuupt",
      "bind_phone": "646ba6e8-63a1-494a-a05b-c94028193023",
      "bind_time": 662716129,
      "bluetooth_flag": 662716129,
      "car_image": 662716129,
      "car_number": "d2d0ddae-b44d-435a-8d63-62758c7f777e",
      "create_time": 662716129,
      "day_distance": 662716129,
      "end_dev_time": 662716129,
      "expire": false,
      "familyname": "d3685999-72e6-4169-9d31-72f0131e469f",
      "groupname": "81896514-8263-47f9-b7eb-9caa343531d0",
      "iccid": "847d09fc-c167-40d9-9bc9-a44af8e7f79e",
      "imei": 662716129,
      "increment": [
        662716129
      ],
      "increment_alarm_day": 662716129,
      "is_in": false,
      "is_wireless": false,
      "last_com_time": 662716129,
      "last_pos": "2a0dce68-84e2-459a-8494-bac085cc0147",
      "last_sgid": "5e1ce925-3f42-4a13-b652-a2f797c829b0",
      "last_simei": "9be4985c-76b3-4e99-9d58-459eef175999",
      "phone": "49adb489-ebe9-4a64-b668-fdfaa7605a01",
      "power": 662716129,
      "pro_name": "9d30afe4-2a0e-4d6a-af43-8256665c3b61",
      "protocol": 662716129,
      "remark": "e8481598-3292-4298-9ca6-320816aa0be4",
      "sgid": "d0a97f88-a680-485b-91b5-f9c6ac17941a",
      "signal_rate": 662716129,
      "simei": "ecc11eed-5d38-482d-aad1-70ebb1fc5e60",
      "start_dev_time": 662716129,
      "state": "e_line_down|e_line_on|e_line_sleep",
      "state_begin_time": 662716129,
      "state_time": 662716129,
      "support_wx_voice": false,
      "ver": "e851388b-022c-4cc6-9053-0cd188fcc972",
      "version": "34a61d68-929f-4b22-966c-96b4c888a697"
    }
  ],
  "line_total": 662716129,
  "sleep_total": 662716129,
  "total": 662716129
}
```

<a id="family-AddDevGroup"></a>
## 添加车组 (`AddDevGroup`)

- 接口 ID：`family.AddDevGroup`
- 模块：`family`
- 用途：添加车组
- 原始位置：`source/车组(family).txt` 第 343 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件:
1，车组名只能用中文数字字母且长度不超过20个字节；
2，只能账号登录；
3，需要分组管理权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,AddDevGroup |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.familyid` | `string` | 新添加车组将要挂载的组织id--familyid（该id不是车组将要生成的车组id--gid） |
| `params.group_name` | `string` | 车组名称 |

### 请求示例

```json
{
  "module": "family",
  "func": "AddDevGroup",
  "params": {
    "familyid": "011b46f3-d8ce-4b7d-96e3-a4481b330e00",
    "group_name": "9a032306-7b81-4ffd-99fb-84b83b141641"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `gid` | `string` | 添加成功后的车组id |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "0a6c859c-4d19-4fba-90ef-d3896165fb7e",
  "gid": "899c5812-c42b-4fa6-ac3d-e0888d7973d5"
}
```

<a id="family-SetDevGroupName"></a>
## 设置车组名称 (`SetDevGroupName`)

- 接口 ID：`family.SetDevGroupName`
- 模块：`family`
- 用途：设置车组名称
- 原始位置：`source/车组(family).txt` 第 389 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件:
1，车组名只能用中文数字字母且长度不超过20个字节；
2，只能账号登录；
3，需要分组管理权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SetDevGroupName |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.gid` | `string` | 车组id |
| `params.group_name` | `string` | 待修改车组的新名称 |

### 请求示例

```json
{
  "module": "family",
  "func": "SetDevGroupName",
  "params": {
    "gid": "24709b34-d009-458b-bc65-65a0f3c77b58",
    "group_name": "a05bea66-8787-4618-9df5-e1219a506ead"
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
  "error_message": "54c84a37-13c8-47b9-96f6-fe8e6f54d67a"
}
```

<a id="family-DelDevGroup"></a>
## 删除车组 (`DelDevGroup`)

- 接口 ID：`family.DelDevGroup`
- 模块：`family`
- 用途：删除车组
- 原始位置：`source/车组(family).txt` 第 432 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件:
1，车组下有设备时无法删除；
2，只剩一个车组时无法删除；
3，只能账号登录，
4，需要分组管理权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,DelDevGroup |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.gid` | `string` | 要被删除的车组id |

### 请求示例

```json
{
  "module": "family",
  "func": "DelDevGroup",
  "params": {
    "gid": "0c7e57ee-b78d-4685-b7d1-0dd8a81bb526"
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
  "error_message": "82926a52-e764-4bc4-aba0-02cb68811888"
}
```

<a id="family-UntieDevGroup"></a>
## 解绑车组 (`UntieDevGroup`)

- 接口 ID：`family.UntieDevGroup`
- 模块：`family`
- 用途：解绑车组
- 原始位置：`source/车组(family).txt` 第 473 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件:
1，顶级组织无法解绑；
2，组织下只剩下最后一个车组无法解绑；
3，只能账号登录；
4，需要分组管理权限；
5，需要转移设备权限；
6，分组名不可重复；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,UntieDevGroup |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.gid` | `string` | 要解绑的车组id,解绑之后回到上一级 |

### 请求示例

```json
{
  "module": "family",
  "func": "UntieDevGroup",
  "params": {
    "gid": "f563e24d-0645-47ce-9099-0a2fb007c6c3"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `task_id` | `string` | 迁移family是异步完成的，会返回一个任务id，下次可以用这个id来查询任务状态 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "b83df4ee-f361-4dd7-ba94-7d001d6022f6",
  "task_id": "dd60ce64-6083-48be-8f14-4dc415f04422"
}
```

<a id="family-BindDevGroup"></a>
## 绑定车组 (`BindDevGroup`)

- 接口 ID：`family.BindDevGroup`
- 模块：`family`
- 用途：绑定车组
- 原始位置：`source/车组(family).txt` 第 519 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件:
1，只能账号登录；
2，组织下最后一个车组无法绑定新车组；
3，需要分组管理权限；
4，需要转移设备权限；
5，分组名不可重复；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,BindDevGroup |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.familyid` | `string` | 绑定的车组织 |
| `params.gid` | `string` | 车组id |

### 请求示例

```json
{
  "module": "family",
  "func": "BindDevGroup",
  "params": {
    "familyid": "8ec9d7aa-64f9-43e4-9a5b-a5c64104b0e6",
    "gid": "1174ff16-ac5b-456f-8aa5-a2e9481312cd"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `task_id` | `string` | 迁移family是异步完成的，会返回一个任务id，下次可以用这个id来查询任务状态 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "c01fa622-18a2-4d88-a614-7405bc836ce3",
  "task_id": "d2505074-1edf-47d4-aa85-2326583b8a78"
}
```

<a id="family-GetTask"></a>
## 查询任务详情 (`GetTask`)

- 接口 ID：`family.GetTask`
- 模块：`family`
- 用途：查询任务详情
- 原始位置：`source/车组(family).txt` 第 567 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetTask |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.task_id` | `string` | 任务id |

### 请求示例

```json
{
  "module": "family",
  "func": "GetTask",
  "params": {
    "task_id": "87a33475-a5ac-4b8b-b31f-3c5665ec14ee"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `addtional` | `string` | 任务执行结果附加信息 |
| `current_count` | `number` | 当前进度 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `is_begin` | `boolean` | 任务是否已经开始 |
| `is_finish` | `boolean` | 是否结束了 |
| `return_code` | `number` | 当前完成之后的返回值 |
| `return_msg` | `string` | 返回的提示信息，如果出错，返回错误信息 |
| `total` | `number` | 待完成的总数 |

### 返回示例

```json
{
  "errcode": 0,
  "addtional": "5678f2f7-dc94-4113-88e1-655ef5dbf75f",
  "current_count": 1822182676,
  "error_message": "125cca1d-8120-4406-b2b1-99ea511414d6",
  "is_begin": true,
  "is_finish": true,
  "return_code": 1822182676,
  "return_msg": "71cacebe-b39a-4967-9173-adae62dc39f5",
  "total": 1822182676
}
```

<a id="family-GetRunInfo"></a>
## 实时刷新设备运行状态 (`GetRunInfo`)

- 接口 ID：`family.GetRunInfo`
- 模块：`family`
- 用途：实时刷新设备运行状态
- 原始位置：`source/车组(family).txt` 第 623 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetRunInfo |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.call_all_dev` | `boolean` | 如果设备处于点名模式，是否针对返回的设备集体下发一次位置查询，默认false，不下发;设置true，下发 |
| `params.call_simei` | `array` | 需要下发位置查询的设备列表，如果非点名模式，将忽略这个参数 |
| `params.is_get_follow` | `boolean` | 是否获取关注设备信息，默认不获取 |
| `params.limit_size` | `number` | 限制条数，默认200台,不能超过3000。 |
| `params.sfamily` | `string` | 需要查询的车组织id--familyid,如果设备号登入，可以不用传 |
| `params.sgid` | `array` | 当前请求的分组id列表 |
| `params.simei` | `array` | 需要查询的实时数据的设备列表,可以传入特别关心的设备号，否者服务器将返回family下的小于200台设备信息,如果设备号登入，可以不用传 |
| `params.term_familyid` | `array` | 当前请求的终端组织id列表 |
| `params.update_time` | `number` | 更新时间，如果传入，则仅返回更新时间之后的设备信息 |

### 请求示例

```json
{
  "module": "family",
  "func": "GetRunInfo",
  "params": {
    "call_all_dev": false,
    "call_simei": [
      "41adda25-fc18-454f-8848-00f2e2ee9159"
    ],
    "is_get_follow": false,
    "limit_size": 1983089453,
    "sfamily": "c8582b2b-7851-484b-becd-978d89ca72e4",
    "sgid": [
      "9cd2c441-1543-497e-8d8f-b105e73909d5",
      "75191b94-dada-47b6-b5b7-c60895201049",
      "32425c7f-87db-4bdd-8a0d-0723b3662f3a"
    ],
    "simei": [
      "df92bd93-3b04-47da-90c4-6efdbed478f6"
    ],
    "term_familyid": [
      "ce504414-263b-4aee-ad9c-a621977ae1f4"
    ],
    "update_time": 1983089453
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `cur_time` | `number` | 当前时间 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `is_all` | `boolean` | 是否拉全了组织下的全部设备 |
| `items` | `array` |  |
| `items.bluetooth_flag` | `number` | 猎狗蓝牙标识，0不支持蓝牙,1支持蓝牙 |
| `items.car_image` | `number` | 车辆图标 |
| `items.car_number` | `string` | 车牌号 |
| `items.day_distance` | `number` | 日里程，如果没有日里程，那么该值为0,仅增量拉取时返回 |
| `items.expire` | `boolean` | 设备是否过期，true过期，false没过期 |
| `items.imei` | `number` |  |
| `items.is_human_dev` | `boolean` | true,人员款设备，false或不存在则为车载款设备 |
| `items.last_com_time` | `number` | 最后通讯时间，(10位长的时间戳，表示从1970.1.1的00:00到该时刻经过的秒的总数) |
| `items.last_pos` | `string` | 位置信息json格式，火星坐标为(lat,lon)(已弃用),84坐标系为("22.641872,114.033431") 如:"{\\"addr\\":\\"广东省 深圳市 龙华区****\\",\\"lat\\":22641872,\\"lon\\":114033431,\\"ptype\\":\\"e_location_nomal\\",\\"time\\":1654660669,\\"type\\":\\"e_wifi\\",\\"wgs\\":\\"22.641872,114.033431\\"}" |
| `items.power` | `number` | 电量 |
| `items.remark` | `string` | 设备备注 |
| `items.simei` | `string` | 设备imei,如果传入的是family就会返回这个字段 |
| `items.state` | `string` | 设备状态<br>e_line_down 不在线<br>e_line_on 在线<br>e_line_sleep 睡眠 |
| `items.state_time` | `number` | 截至当前的状态运行时间,单位为秒(当前状态所处的时间与该状态开始时间的时间差),仅增量拉取时返回 |
| `items.version` | `string` | 设备固件版本 |

### 返回示例

```json
{
  "errcode": 0,
  "cur_time": 768320743,
  "error_message": "c615a900-f3dc-4a74-893b-cbc1dda0b1d8",
  "is_all": false,
  "items": [
    {
      "bluetooth_flag": 768320743,
      "car_image": 768320743,
      "car_number": "ac1b98ad-8fe9-4bf7-8f20-1f5ab5659f4f",
      "day_distance": 768320743,
      "expire": false,
      "imei": 768320743,
      "is_human_dev": false,
      "last_com_time": 768320743,
      "last_pos": "711976e3-f635-4e4d-a7cd-cb62a35450b1",
      "power": 768320743,
      "remark": "079afcec-3f95-471e-97cb-e56e258cdc33",
      "simei": "4717cd75-b500-4b2c-ae4b-8f50f35a1a7b",
      "state": "e_line_down|e_line_on|e_line_sleep",
      "state_time": 768320743,
      "version": "4b0675c4-8a31-497f-b415-2e3092e503de"
    },
    {
      "bluetooth_flag": 768320743,
      "car_image": 768320743,
      "car_number": "16afb9ba-e91f-4bba-91d9-17bd9b5868f4",
      "day_distance": 768320743,
      "expire": false,
      "imei": 768320743,
      "is_human_dev": false,
      "last_com_time": 768320743,
      "last_pos": "05cf9945-1ecd-4bda-9d22-7c9f6b19446f",
      "power": 768320743,
      "remark": "6ee89ab8-36e0-494d-89b9-276e36da967a",
      "simei": "af5a75db-31d2-4bab-8d70-819bc0af9e1d",
      "state": "e_line_down|e_line_on|e_line_sleep",
      "state_time": 768320743,
      "version": "f4058d50-3b62-4afa-8af2-a3adf2c501c7"
    },
    {
      "bluetooth_flag": 768320743,
      "car_image": 768320743,
      "car_number": "9561a4cb-863a-4967-a8a3-d096cbf9c8d6",
      "day_distance": 768320743,
      "expire": false,
      "imei": 768320743,
      "is_human_dev": false,
      "last_com_time": 768320743,
      "last_pos": "09efd62c-cec3-4aa8-8907-cdfb0c62f1b4",
      "power": 768320743,
      "remark": "9e11dfcc-3220-4c49-8667-96d9b31b1486",
      "simei": "ddc5a217-960d-479f-8482-32372dbad5ac",
      "state": "e_line_down|e_line_on|e_line_sleep",
      "state_time": 768320743,
      "version": "5105baad-b204-4ba8-9433-86888bb0d238"
    },
    {
      "bluetooth_flag": 768320743,
      "car_image": 768320743,
      "car_number": "38d95b19-da0c-47e2-a938-63b23af0ba24",
      "day_distance": 768320743,
      "expire": false,
      "imei": 768320743,
      "is_human_dev": false,
      "last_com_time": 768320743,
      "last_pos": "e36e0ac6-2c23-45e1-9a50-a0651daa6eee",
      "power": 768320743,
      "remark": "30f56b37-55bd-4efa-a970-8077c7038da1",
      "simei": "7256a950-fec8-4b6a-a8bb-8b965bb3fad2",
      "state": "e_line_down|e_line_on|e_line_sleep",
      "state_time": 768320743,
      "version": "5de3c5ff-f6b0-434b-9e2b-de8d397a6211"
    }
  ]
}
```

<a id="family-SetFamilyName"></a>
## 设置组织名称 (`SetFamilyName`)

- 接口 ID：`family.SetFamilyName`
- 模块：`family`
- 用途：设置组织名称
- 原始位置：`source/车组(family).txt` 第 803 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件:
1，只能账号登录；
2，组织名只能用中文数字字母且长度不超过15个字节；
3，需要组织管理权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SetFamilyName |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.familyid` | `string` | 修改的组织id |
| `params.name` | `string` | 修改的组织新名称 |

### 请求示例

```json
{
  "module": "family",
  "func": "SetFamilyName",
  "params": {
    "familyid": "895826b3-3612-4c67-bce0-de3afc56e215",
    "name": "4529c131-3a01-4b70-84bf-939d4f037011"
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
  "error_message": "c62137c3-477c-47c6-8737-195de628652c"
}
```

<a id="family-GetFamilyList"></a>
## 获取车组列表 (`GetFamilyList`)

- 接口 ID：`family.GetFamilyList`
- 模块：`family`
- 用途：获取车组列表
- 原始位置：`source/车组(family).txt` 第 846 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件:
1，只能账号登录；
2，g_limit_size和f_limit_size范围（0-100）；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetFamilyList |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.f_limit_size` | `number` | 限制组织获取数量，默认20，最大1000 |
| `params.familyid` | `string` | 用户组织id,为空表示最上一级 |
| `params.g_limit_size` | `number` | 限制分组获取数量,默认20，最大100 |
| `params.get_all` | `boolean` | 返回当前组织的全部设备总数，默认false，不返回 |
| `params.get_current` | `boolean` | 仅获取传入组织的信息 |
| `params.get_garage_sum` | `boolean` | 返回分组的统计数据，默认返回 |
| `params.get_increment_switch` | `boolean` | 获取组织打开的增值服务开关 |
| `params.last_fid` | `string` | 最后获取到的组织id--fid，没有可以为空 |
| `params.last_gid` | `string` | 最后获取到的车组id--gid，没有可以为空 |

### 请求示例

```json
{
  "module": "family",
  "func": "GetFamilyList",
  "params": {
    "f_limit_size": 1110648628,
    "familyid": "cc4b484e-8593-42f7-bf60-6b29fb381487",
    "g_limit_size": 1110648628,
    "get_all": true,
    "get_current": true,
    "get_garage_sum": true,
    "get_increment_switch": true,
    "last_fid": "403ba038-9d0e-4eb4-abe9-0a3d6b9402ce",
    "last_gid": "c6bc263b-6520-4c03-bafb-0cd2346047ec"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `familyid` | `string` | 当前请求的组织id,为空表示最上一级 |
| `familys` | `array` | 车组织信息 |
| `familys.imei_count` | `number` | 设备数(可按照参数名来区别具体的imei_cout，如：familys.imei_cout表示组织下挂载的设备数；garages.smei_cout，该smei_cout表示车组下挂载的设备数) |
| `familys.increment_types` | `array` | 组织打开的增值服务 |
| `familys.sid` | `string` | id(可按照参数名来区别具体的id，如：familys.sid该id表示组织id；garages.sid，该id表示车组id) |
| `familys.sname` | `string` | 名称(可按照参数名来区别具体的sname，如：familys.sname该id表示组织name；garages.sname，该id表示车组name) |
| `familys.sub_count` | `number` | 下级个数(可按照参数名来区别具体的sub_count，如：familys.sub_count该sub_count表示组织的下级个数；garages.sub_count，该sub_count表示车组下级个数) |
| `familys.type` | `string` | 属性<br>e_type_user 终端组织<br>e_type_busness 销售商 |
| `familys_total` | `number` | 符合条件的车组织总数 |
| `garages` | `array` | 车组信息 |
| `garages.imei_count` | `number` | 设备数(可按照参数名来区别具体的imei_cout，如：familys.imei_cout表示组织下挂载的设备数；garages.smei_cout，该smei_cout表示车组下挂载的设备数) |
| `garages.increment_types` | `array` | 组织打开的增值服务 |
| `garages.sid` | `string` | id(可按照参数名来区别具体的id，如：familys.sid该id表示组织id；garages.sid，该id表示车组id) |
| `garages.sname` | `string` | 名称(可按照参数名来区别具体的sname，如：familys.sname该id表示组织name；garages.sname，该id表示车组name) |
| `garages.sub_count` | `number` | 下级个数(可按照参数名来区别具体的sub_count，如：familys.sub_count该sub_count表示组织的下级个数；garages.sub_count，该sub_count表示车组下级个数) |
| `garages.type` | `string` | 属性<br>e_type_user 终端组织<br>e_type_busness 销售商 |
| `garages_total` | `number` | 符合条件的车组总数 |
| `imei_count` | `number` | get_all为true时返回,当前车组的设备总数,不包含冻结设备 |
| `run_limit` | `number` | app级别拉取设备限制 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "2cb9d5d8-2ef9-4554-a683-f83d4c8e6254",
  "familyid": "aaba6d6f-a124-470e-8077-bc823ceb7d5f",
  "familys": [
    {
      "imei_count": 1729736404,
      "increment_types": [
        1729736404
      ],
      "sid": "24614454-d783-4855-b5b1-f2789d8b17fb",
      "sname": "95b2779a-7b04-4b86-917c-a1fb79ac521d",
      "sub_count": 1729736404,
      "type": "e_type_user|e_type_busness"
    },
    {
      "imei_count": 1729736404,
      "increment_types": [
        1729736404,
        1729736404,
        1729736404
      ],
      "sid": "e5633e8c-8aa3-4a4c-b9c6-f87c2980169a",
      "sname": "b02cf996-cc75-4577-ad1d-8519efaba202",
      "sub_count": 1729736404,
      "type": "e_type_user|e_type_busness"
    }
  ],
  "familys_total": 1729736404,
  "garages": [
    {
      "imei_count": 1729736404,
      "increment_types": [
        1729736404,
        1729736404
      ],
      "sid": "e50dc28b-3ec4-4699-9563-18ef8f1be248",
      "sname": "865d9784-0260-457c-b0fe-2e9c238b121f",
      "sub_count": 1729736404,
      "type": "e_type_user|e_type_busness"
    },
    {
      "imei_count": 1729736404,
      "increment_types": [
        1729736404,
        1729736404
      ],
      "sid": "08941840-e57b-4772-8964-0e7f5adcde0e",
      "sname": "8eda3eb1-7be9-4acb-bd77-a456462f1bdd",
      "sub_count": 1729736404,
      "type": "e_type_user|e_type_busness"
    }
  ],
  "garages_total": 1729736404,
  "imei_count": 1729736404,
  "run_limit": 1729736404
}
```

<a id="family-DelNextFamily"></a>
## 删除下级组织 (`DelNextFamily`)

- 接口 ID：`family.DelNextFamily`
- 模块：`family`
- 用途：删除下级组织
- 原始位置：`source/车组(family).txt` 第 1004 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件:
1，只能账号登录；
2，需要组织管理权限；
3，当前账号最后一个组织无权删除；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,DelNextFamily |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.familyid` | `string` | 当前请求的组织id,不能删掉自己唯一的组织 |

### 请求示例

```json
{
  "module": "family",
  "func": "DelNextFamily",
  "params": {
    "familyid": "2c1147c0-e9aa-48c5-9972-6f256adc06ac"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `task_id` | `string` | 删除family是异步完成的，会返回一个任务id，下次可以用这个id来查询任务状态 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "851b0d15-b0d3-4a0f-8b2f-a2c397da504f",
  "task_id": "fcc0da40-891e-4ee2-876b-ae5dbd7198e9"
}
```

<a id="family-GetFamilyInfo"></a>
## 获取上级各层级的组织信息 (`GetFamilyInfo`)

- 接口 ID：`family.GetFamilyInfo`
- 模块：`family`
- 用途：获取上级各层级的组织信息
- 原始位置：`source/车组(family).txt` 第 1047 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetFamilyInfo |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.familyid` | `string` | 当前请求的组织id，与字段sgid，二选一 |
| `params.get_own_account` | `boolean` | 获取组织的登录账号 |
| `params.sgid` | `string` | 当前请求的分组id,与字段familyid，二选一 |
| `params.top_familyid` | `string` |  |

### 请求示例

```json
{
  "module": "family",
  "func": "GetFamilyInfo",
  "params": {
    "familyid": "faaa3927-fc8d-48e0-b78f-82f05941d147",
    "get_own_account": false,
    "sgid": "898051f7-5bc9-4711-9ebe-12756ac4c648",
    "top_familyid": "5225bf07-aa44-4839-8ef2-3127ad560040"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `gname` | `string` | 如果传入了sgid，那么这里将给gid对应的名称 |
| `items` | `array` | 返回对应的全部层级组织信息,列表第一个是当前登录账号的第一级组织，列表最后一个是请求参数的组织 |
| `items.contact` | `string` | 组织联系方式 |
| `items.familyid` | `string` | 组织id |
| `items.sname` | `string` | 组织名称 |
| `items.user_account` | `string` | 登录账号 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "a3c76fde-5906-4fac-853b-e82dda603338",
  "gname": "af110de9-c265-479d-bdee-663aa5765250",
  "items": [
    {
      "contact": "1e986bde-2e58-4d94-b2a0-b69fcba179ec",
      "familyid": "a24ab5a3-af25-4c48-ba9d-4b550a554ccf",
      "sname": "b19fd134-2211-4e7b-9d8b-48aa41480af6",
      "user_account": "2d4d7e70-9a9d-4dc8-8350-7b8376dce492"
    },
    {
      "contact": "f88d4773-0820-4594-ae12-89201f0ba72c",
      "familyid": "b1c4379b-bd36-4d8b-9754-10e1e8e143e0",
      "sname": "7e8fe8f5-388d-4fbf-848d-f02115f561c0",
      "user_account": "229a12c7-6853-4c9b-8d26-6b056796364b"
    },
    {
      "contact": "df4994c2-10e3-48a6-85e3-df0b53e2b327",
      "familyid": "f3445727-ba28-4ea5-90d2-1c4bb524e363",
      "sname": "c11beb7c-84ef-43d0-aeba-bff0483ebaca",
      "user_account": "baabab4f-0a31-4910-bb1e-31d1a3ed0914"
    }
  ]
}
```

<a id="family-ModifyFamilyContact"></a>
## 组织联系方式 (`ModifyFamilyContact`)

- 接口 ID：`family.ModifyFamilyContact`
- 模块：`family`
- 用途：组织联系方式
- 原始位置：`source/车组(family).txt` 第 1124 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,ModifyFamilyContact |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.customer_phone` | `string` | 组织对应的新客服电话( |
| `params.familyid` | `string` | 修改的组织id |

### 请求示例

```json
{
  "module": "family",
  "func": "ModifyFamilyContact",
  "params": {
    "customer_phone": "51507d08-925f-48e0-b221-d2ee8631614e",
    "familyid": "1b6bcf72-f286-487e-85fd-a2d2165421e4"
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
  "error_message": "7c7b4364-0eab-40b3-9af5-dcba9af81935"
}
```

<a id="family-GetContact"></a>
## 获取联系方式 (`GetContact`)

- 接口 ID：`family.GetContact`
- 模块：`family`
- 用途：获取联系方式
- 原始位置：`source/车组(family).txt` 第 1162 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetContact |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.familyid` | `string` | 获取组织id对应的组织客服联系方式,参数familyid与simei二选一，设备号登录可以不用填写 |
| `params.simei` | `string` | 获取设备对应的组织客服联系方式,参数familyid与simei二选一，设备号登录可以不用填写 |

### 请求示例

```json
{
  "module": "family",
  "func": "GetContact",
  "params": {
    "familyid": "57810c1d-b5a2-4be3-9515-11e1ab4837d9",
    "simei": "a45e08fe-b0ad-423b-a4ce-81fc911080c2"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `customer_phone` | `string` | 组织对应的客服电话 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `sname` | `string` | 组织名称 |

### 返回示例

```json
{
  "errcode": 0,
  "customer_phone": "e9d2deb6-badf-4b59-8be3-66746fe3d1cb",
  "error_message": "92fbef37-9e93-4612-836a-da9636400ebc",
  "sname": "1ecc5901-ceec-4b1f-aeba-4a73cb75ea09"
}
```

<a id="family-FindFamily"></a>
## 对组织进行模糊搜索 (`FindFamily`)

- 接口 ID：`family.FindFamily`
- 模块：`family`
- 用途：对组织进行模糊搜索
- 原始位置：`source/车组(family).txt` 第 1206 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,FindFamily |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.find_terminal` | `boolean` | 是否查找终端 |
| `params.last_sfamilyid` | `string` | 最后一个familyid(分页功能,分页详情请参考 &lt;&lt;前言&gt;&gt; 分页) |
| `params.limit_size` | `number` | 搜索限制，默认10，不可超过10 |
| `params.name` | `string` | 模糊搜索值 |
| `params.only_find_next` | `boolean` | 只搜索下级 |
| `params.sfamily` | `string` | 在指定的组织id下查找 |

### 请求示例

```json
{
  "module": "family",
  "func": "FindFamily",
  "params": {
    "find_terminal": true,
    "last_sfamilyid": "d12e087f-832f-42ee-b0cd-396eb1cf01bd",
    "limit_size": 1993378160,
    "name": "ea08708f-cf7f-4db0-9c6d-c1611de67aeb",
    "only_find_next": true,
    "sfamily": "2f96c62a-5279-4079-8f8e-be943e514661"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `infos` | `array` |  |
| `infos.familyid` | `string` | 组织id |
| `infos.sname` | `string` | 组织名称 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "8ced3cc4-9449-4f3d-a6a5-92cbd220864c",
  "infos": [
    {
      "familyid": "3278383d-f8ba-4576-97c2-58808ff934ad",
      "sname": "312523bf-eb77-4c13-bbdd-12c2956d8d70"
    },
    {
      "familyid": "49896adb-7017-415d-89ad-e860b3edab71",
      "sname": "a9848ba9-cd63-48a3-8c14-866bd1cad3cc"
    },
    {
      "familyid": "b005d924-2331-4d12-845b-30dd8e432ba5",
      "sname": "dacad716-dc6f-466e-b558-e341cbfdcc4b"
    }
  ]
}
```

<a id="family-SetFamilyConfig"></a>
## 设置组织配置信息 (`SetFamilyConfig`)

- 接口 ID：`family.SetFamilyConfig`
- 模块：`family`
- 用途：设置组织配置信息
- 原始位置：`source/车组(family).txt` 第 1276 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SetFamilyConfig |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.auth_info` | `string` | 认证信息, 默认为空, 仅使用tcp连接方式时生效 |
| `params.buss_wx_key` | `array` | 企业微信推送配置 |
| `params.conn_type` | `string` | 连接方式, 默认http<br>e_type_http<br>e_type_tcp |
| `params.familyid` | `string` | 修改的组织id,不传表示当前组织的 |
| `params.forward_addr` | `string` | 转发信息的地址,需要设置才填写，如果不设置，就不需要填写。置空表示删除该项 |
| `params.forward_type` | `string` | 转发类型, 默认单连接单条<br>e_single  单连接单条<br>e_batch  单连接批量, 将多个单条消息合并成json数组格式一次性发送<br>e_multi  多连接, 每个连接发送消息格式同单连接单条 |
| `params.fun` | `object` | 功能位配置 |
| `params.fun.b_alarm` | `boolean` | 上报告警信息 |
| `params.fun.b_allsub` | `boolean` | 包含下级组织设备 |
| `params.fun.b_devinfo` | `boolean` | 上报设备信息 |
| `params.fun.b_lbs_result` | `boolean` | 基站wifi异步解析结果, b_location为true时有效 |
| `params.fun.b_location` | `boolean` | 上报轨迹点 |
| `params.fun.b_original` | `boolean` | 原始信息 |
| `params.fun.b_state` | `boolean` | 上报更新状态 |
| `params.service_url` | `string` |  |
| `params.update_busswx` | `boolean` | 是否更新企业微信配置,默认为false |

### 请求示例

```json
{
  "module": "family",
  "func": "SetFamilyConfig",
  "params": {
    "auth_info": "ab8034df-62f2-413a-9561-d418eed8ea1e",
    "buss_wx_key": [
      "ae95c24b-cc28-4de8-a1ff-8b5ed4b2713f",
      "8be046a6-b901-4cd7-a586-6e48075c9830",
      "d786b443-98c5-456e-93ca-a089b7667baa",
      "78a7d47a-62ac-4612-b0b0-547751efc8dd"
    ],
    "conn_type": "e_type_http|e_type_tcp",
    "familyid": "6287e5e4-3026-4a49-85cf-501e892b034e",
    "forward_addr": "41840866-5916-4da6-8664-8dada389aaaf",
    "forward_type": "e_single|e_batch|e_multi",
    "fun": {
      "b_alarm": false,
      "b_allsub": false,
      "b_devinfo": false,
      "b_lbs_result": false,
      "b_location": false,
      "b_original": false,
      "b_state": false
    },
    "service_url": "c366590a-5ad7-4d8d-aad9-4f7843a316b5",
    "update_busswx": false
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
  "error_message": "b1e540be-221f-4bfd-80f9-a94e57995cb8"
}
```

<a id="family-QueryFamilyConfig"></a>
## 获取组织配置信息 (`QueryFamilyConfig`)

- 接口 ID：`family.QueryFamilyConfig`
- 模块：`family`
- 用途：获取组织配置信息
- 原始位置：`source/车组(family).txt` 第 1367 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,QueryFamilyConfig |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.familyid` | `string` | 获取的组织id,不传表示当前组织的 |
| `params.need_forward` | `boolean` | 获取转发信息，默认为true,会鉴权，如没必要建议传入false |

### 请求示例

```json
{
  "module": "family",
  "func": "QueryFamilyConfig",
  "params": {
    "familyid": "82986f37-6c5d-44fa-b6f0-7114536c1c76",
    "need_forward": true
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `auth_info` | `string` | 认证信息, 默认为空, 仅使用tcp连接方式时生效, 最长128个字符 |
| `buss_wx_key` | `array` | 企业微信推送配置 |
| `conn_type` | `string` | 连接方式, 默认http<br>e_type_http<br>e_type_tcp |
| `dashboard_title` | `string` |  |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `forward_addr` | `string` | 转发信息的地址,需要设置才填写，如果不设置，就不需要填写。置空表示删除该项 |
| `forward_type` | `string` | 转发类型, 默认单连接单条<br>e_single  单连接单条<br>e_batch  单连接批量, 将多个单条消息合并成json数组格式一次性发送<br>e_multi  多连接, 每个连接发送消息格式同单连接单条 |
| `fun` | `object` | 功能位配置 |
| `fun.b_alarm` | `boolean` | 上报告警信息 |
| `fun.b_allsub` | `boolean` | 包含下级组织设备 |
| `fun.b_devinfo` | `boolean` | 上报设备信息 |
| `fun.b_lbs_result` | `boolean` | 基站wifi异步解析结果, b_location为true时有效 |
| `fun.b_location` | `boolean` | 上报轨迹点 |
| `fun.b_original` | `boolean` | 原始信息 |
| `fun.b_state` | `boolean` | 上报更新状态 |

### 返回示例

```json
{
  "errcode": 0,
  "auth_info": "902d9b75-daea-4111-b5ba-ab75d6be662f",
  "buss_wx_key": [
    "0cebd45f-2434-4c9c-922d-1833718bc1d4",
    "c4bacb68-1ed0-4b4a-ae22-0f08460a8bc8",
    "90964c7c-1dc2-47bc-a9f6-a884af4fcdb7",
    "404e35e3-6936-47b4-8be5-324debac4875"
  ],
  "conn_type": "e_type_http|e_type_tcp",
  "dashboard_title": "6e282d9e-a99e-4d9b-84bc-25011e744fe9",
  "error_message": "e436b598-e4eb-4a6d-9337-ffb684befc98",
  "forward_addr": "0a032ee5-9edb-4d6e-a99d-ca7e36d2960e",
  "forward_type": "e_single|e_batch|e_multi",
  "fun": {
    "b_alarm": true,
    "b_allsub": true,
    "b_devinfo": true,
    "b_lbs_result": true,
    "b_location": true,
    "b_original": true,
    "b_state": true
  }
}
```

<a id="family-QueryTermByBussness"></a>
## 通过经销商组织查询绑定终端组织列表 (`QueryTermByBussness`)

- 接口 ID：`family.QueryTermByBussness`
- 模块：`family`
- 用途：通过经销商组织查询绑定终端组织列表
- 原始位置：`source/车组(family).txt` 第 1458 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,QueryTermByBussness |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.familyid` | `string` | 获取的组织id,不传表示当前组织的 |
| `params.get_bind_dev` | `boolean` | 查询未绑定终端组织，仅设备号登录的设备总数。包括仅绑定手机号登录和什么也没有绑定，直接设备号登录。与get_bind_phone同时传入时优先get_bind_phone |
| `params.get_bind_phone` | `boolean` | 查询未绑定终端组织，仅绑定手机号登录的设备总数 |
| `params.last_sfamilyid` | `string` | 最后一个familyid(分页功能,分页详情请参考 &lt;&lt;前言&gt;&gt; 分页) |
| `params.limit_size` | `number` | 搜索限制，默认10 |
| `params.stat_scale` | `string` | 过滤时间范围<br>e_scale_1_hour  1小时内<br>e_scale_1_hour_to_day  1小时到1天<br>e_scale_1_day  1天内<br>e_scale_1_day_to_7  1-7天<br>e_scale_7_day  7天内<br>e_scale_7_day_to_30  7-30天<br>e_scale_30_day  30天内<br>e_scale_30_day_to_60  30-60天内<br>e_scale_60_day  60天内<br>e_scale_over_60  超过60天 |
| `params.stat_type` | `string` | 过滤类型<br>e_stat_dev  获取组织设备及包含下级设备数<br>e_stat_expired  获取过期设备数<br>e_stat_expiring  即将过期<br>e_stat_down  获取离线设备数<br>e_stat_sleep  获取静止设备数<br>e_stat_inactive  获取未启用设备数 |

### 请求示例

```json
{
  "module": "family",
  "func": "QueryTermByBussness",
  "params": {
    "familyid": "c9ea9253-6343-4d62-b799-05e81f635d42",
    "get_bind_dev": false,
    "get_bind_phone": false,
    "last_sfamilyid": "12d8e2d9-339d-49e6-9947-d04357dd9dd8",
    "limit_size": 1490929137,
    "stat_scale": "e_scale_1_hour|e_scale_1_hour_to_day|e_scale_1_day|e_scale_1_day_to_7|e_scale_7_day|e_scale_7_day_to_30|e_scale_30_day|e_scale_30_day_to_60|e_scale_60_day|e_scale_over_60",
    "stat_type": "e_stat_dev|e_stat_expired|e_stat_expiring|e_stat_down|e_stat_sleep|e_stat_inactive"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.imei_count` | `number` | 该终端组织设备总数 |
| `items.imei_current` | `number` | 该经销商名下的设备总数 |
| `items.name` | `string` | 终端组织名称 |
| `items.sfamilyid` | `string` | 终端组织id,如果为空，表示未绑定终端组织的信息 |
| `total` | `number` |  |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "b0ce9658-43af-4446-bb27-6bc7b2ec10b0",
  "items": [
    {
      "imei_count": 762707656,
      "imei_current": 762707656,
      "name": "740feb1c-7ee8-442a-8673-471b26371f23",
      "sfamilyid": "d7b955c9-8371-4c6f-bb21-32d090c1b774"
    },
    {
      "imei_count": 762707656,
      "imei_current": 762707656,
      "name": "288db47a-e3c5-4e97-9b5c-8b5d42b19d85",
      "sfamilyid": "a8b0aea4-f753-4f14-8f61-caf59dc21e66"
    },
    {
      "imei_count": 762707656,
      "imei_current": 762707656,
      "name": "b16a0d45-e605-4ca3-bb2c-c721319a2846",
      "sfamilyid": "4c36123e-f385-4384-9459-f118c7f3e6a8"
    },
    {
      "imei_count": 762707656,
      "imei_current": 762707656,
      "name": "8c38b190-1196-4712-a7dc-8de11a8ce7ce",
      "sfamilyid": "400e27f6-7806-4c1f-8f5e-ede19cfe0961"
    }
  ],
  "total": 762707656
}
```

<a id="family-MoveFamily"></a>
## 迁移下级组织 (`MoveFamily`)

- 接口 ID：`family.MoveFamily`
- 模块：`family`
- 用途：迁移下级组织
- 原始位置：`source/车组(family).txt` 第 1566 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,MoveFamily |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.sfamilyid` | `string` | 要迁移的组织id |
| `params.targetid` | `string` | 目标的组织id |

### 请求示例

```json
{
  "module": "family",
  "func": "MoveFamily",
  "params": {
    "sfamilyid": "17d7a3f4-eb5c-4e55-aad5-5c7213896fbb",
    "targetid": "6e2c7d42-cd0a-4a96-8315-67ce309c3d36"
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
  "error_message": "1dd4042b-589e-4660-a1a6-7083dfbfee39"
}
```

<a id="family-GetFamilyConfig"></a>
## 获取组织的配置信息 (`GetFamilyConfig`)

- 接口 ID：`family.GetFamilyConfig`
- 模块：`family`
- 用途：获取组织的配置信息
- 原始位置：`source/车组(family).txt` 第 1604 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetFamilyConfig |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.service_url_type` | `string` | e_service_normal<br>e_service_pay |
| `params.sfamilyid` | `string` |  |
| `params.simei` | `string` | 加密的设备号，如果不传，默认获取当前账号所在组织的 |

### 请求示例

```json
{
  "module": "family",
  "func": "GetFamilyConfig",
  "params": {
    "service_url_type": "e_service_normal|e_service_pay",
    "sfamilyid": "d8cefda6-affc-4d51-b99f-bad40f319969",
    "simei": "2436ed17-7e82-4270-a1b6-e05fa03dafe7"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `customer_config` | `boolean` | 是否客户配置 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `service_url` | `string` |  |

### 返回示例

```json
{
  "errcode": 0,
  "customer_config": true,
  "error_message": "f2f805ad-0772-4015-b69b-b0e98b96d8e2",
  "service_url": "a6a9da1d-4f88-4c4b-84c1-f178718105f1"
}
```

<a id="family-SetFamilyCustomConfig"></a>
## 设置组织个性化配置 (`SetFamilyCustomConfig`)

- 接口 ID：`family.SetFamilyCustomConfig`
- 模块：`family`
- 用途：设置组织个性化配置
- 原始位置：`source/车组(family).txt` 第 1653 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SetFamilyCustomConfig |
| `module` | `string` | 模块名,family |
| `params` | `object` | 参数列表 |
| `params.dashboard_title` | `string` | 风控中心标题 |
| `params.dashboard_title_child` | `boolean` | 风控中心标题 同步至下级 |
| `params.familyid` | `string` |  |

### 请求示例

```json
{
  "module": "family",
  "func": "SetFamilyCustomConfig",
  "params": {
    "dashboard_title": "a6bbfeea-1bdb-47fe-acdd-41f31f3a86eb",
    "dashboard_title_child": false,
    "familyid": "3e363ca6-56f5-4c50-ace7-68aef62b7319"
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
  "error_message": "0bcba1e7-8ef4-4dcb-abf1-48465dfc2aa3"
}
```
