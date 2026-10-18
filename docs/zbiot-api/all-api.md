# 远近在线开放云服务 API 完整接口文档

> 来源：https://api.zbiot.cc/  
> 抓取时间：2026-10-18T13:12:05+08:00  
> 覆盖：12 个模块，135 个接口

## 模块目录

| 模块 | 接口数 | 文档 |
| --- | ---: | --- |
| `user` | 20 | [modules/user.md](modules/user.md) |
| `family` | 21 | [modules/family.md](modules/family.md) |
| `device` | 21 | [modules/device.md](modules/device.md) |
| `location` | 9 | [modules/location.md](modules/location.md) |
| `alarm` | 17 | [modules/alarm.md](modules/alarm.md) |
| `fence` | 5 | [modules/fence.md](modules/fence.md) |
| `sim` | 14 | [modules/sim.md](modules/sim.md) |
| `loc` | 12 | [modules/loc.md](modules/loc.md) |
| `timerswitch` | 3 | [modules/timerswitch.md](modules/timerswitch.md) |
| `summary` | 7 | [modules/summary.md](modules/summary.md) |
| `temphumi` | 2 | [modules/temphumi.md](modules/temphumi.md) |
| `attendance` | 4 | [modules/attendance.md](modules/attendance.md) |

# 用户管理 (User)

- 模块标识：`user`
- 接口数量：20
- 原始文档：`source/账户(user).txt`
- 正式环境：`http://cciot.cc/mapi`
- 测试环境：`http://cs.iotiot.cc/mapi`
- 开发环境：`http://192.168.1.20/mapi`
- 模块负责人：fange,fushengyou

## 接口索引

| 接口 | 名称 | 请求字段数 | 返回字段数 |
| --- | --- | ---: | ---: |
| [`Login`](#user-Login) | 登录 | 15 | 19 |
| [`Register`](#user-Register) | 注册 | 22 | 3 |
| [`AddAccount`](#user-AddAccount) | 添加帐号 | 11 | 3 |
| [`DelAccount`](#user-DelAccount) | 删除同级帐号 | 4 | 2 |
| [`SetAccount`](#user-SetAccount) | 修改用户信息 | 28 | 3 |
| [`AddNextAccount`](#user-AddNextAccount) | 添加下级用户(组织) | 12 | 4 |
| [`DelNextAccount`](#user-DelNextAccount) | 删除下级用户 | 4 | 3 |
| [`GetAccountList`](#user-GetAccountList) | 获取用户列表 | 8 | 17 |
| [`LoginOut`](#user-LoginOut) | 退出登录 | 3 | 2 |
| [`WXBindUser`](#user-WXBindUser) | 微信绑定用户 | 8 | 2 |
| [`IsBindWX`](#user-IsBindWX) | 登录绑定微信账号 | 16 | 15 |
| [`UnBindWX`](#user-UnBindWX) | 解除绑定微信 | 4 | 2 |
| [`GetUidInfo`](#user-GetUidInfo) | 获取单用户信息 | 6 | 20 |
| [`GetPhoneArea`](#user-GetPhoneArea) | 获取手机号码区号 | 5 | 5 |
| [`CheckVersion`](#user-CheckVersion) | 检查版本 | 6 | 9 |
| [`FindPass`](#user-FindPass) | 找回密码 | 19 | 5 |
| [`Cancel`](#user-Cancel) | 注销 | 3 | 4 |
| [`SwitchAuth`](#user-SwitchAuth) | 交换权限 | 5 | 2 |
| [`UnBindPhone`](#user-UnBindPhone) | 账号解绑电话号码 | 4 | 2 |
| [`AddBindPhoneDev`](#user-AddBindPhoneDev) | 添加绑定本账号手机号码的设备 | 6 | 8 |

## 接口详情

<a id="user-Login"></a>
## 登录 (`Login`)

- 接口 ID：`user.Login`
- 模块：`user`
- 用途：登录
- 原始位置：`source/账户(user).txt` 第 51 行
- URL 参数：源文档未列出

### 说明

限制条件：
1，limit_size不能超过100；
2，必要参数type，flag，account，platform和pwd_md5；
3,account字节范围为（0-120，platform字节范围（0-64））

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Login |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.account` | `string` | 帐号 ，必填,可以是设备号，绑定手机号，账号 |
| `params.flag` | `string` | 登录标识，建议填写，如果不填，在account有冲突的情况下，不会返回sid，会返回对应的flag让用户选择<br>e_account_login 用户登录<br>e_device_login 设备号登录<br>e_phone_device_login 手机号绑定的设备号登录<br>e_phone_account_login 手机号绑定的账号登录 |
| `params.get_is_bind_wx` | `boolean` | 获取是否绑定了wx的type |
| `params.info` | `string` | 手机登录信息:IP;IMEI;CCID;ADDR等,便于追踪用户登入情况 |
| `params.lang` | `string` | 登入之后的语言，非必填，默认中文, 例如:ch 中文; en 英语 |
| `params.limit_size` | `number` | 限制获取组织信息数量，默认20,最大100 |
| `params.mpm` | `string` | 推送通道，目前默认激光推送<br>e_jpush 极光推送<br>e_hpush 华为推送<br>e_xmpush 小米推送<br>e_oppopush oppo推送<br>e_vivopush vivo推送<br>e_xmiospush 小米ios推送<br>e_iospush IOS原生推送<br>e_googlepush google原生推送<br>e_webhwpush web端推送<br>e_honorpush 荣耀推送<br>e_iosp12 ios P12推送<br>e_wx_official 微信公众号推送 |
| `params.mpm_identify` | `string` | 推送标志 |
| `params.platform` | `string` | 必填，登录平台,ios,android,winphone,web，wx,便于追踪用户登入情况 |
| `params.pwd_md5` | `string` | 用户md5加密密码，必填 |
| `params.type` | `number` | 必填，账户挂靠的APP类型，可以向服务商索取， |
| `params.web_push` | `boolean` | 是否启动web端推送，注意填了这个参数将覆盖app端推送 |

### 请求示例

```json
{
  "module": "user",
  "func": "Login",
  "params": {
    "account": "61b4489d-21d3-4235-86b6-13ac2bedc5f6",
    "flag": "e_account_login|e_device_login|e_phone_device_login|e_phone_account_login",
    "get_is_bind_wx": true,
    "info": "8750adad-9505-4088-8c41-cad204f51d23",
    "lang": "70b66d09-f536-48dd-b6ea-63a599ac8f63",
    "limit_size": 1676984888,
    "mpm": "e_jpush|e_hpush|e_xmpush|e_oppopush|e_vivopush|e_xmiospush|e_iospush|e_googlepush|e_webhwpush|e_honorpush|e_iosp12|e_wx_official",
    "mpm_identify": "a26f9b74-a7a7-4cf0-9150-80dcbcd22a2d",
    "platform": "9871c25a-238b-45ae-b605-b4be5a1180a4",
    "pwd_md5": "100d610a-60b4-41bf-8dc5-f552611e119e",
    "type": 1676984888,
    "web_push": true
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `auth` | `array` | 用户的权限列表（只有账号登录才返回） |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `familys` | `array` | 组织信息（只有账号登录才返回） |
| `familys.auth` | `array` | 该组织的权限列表 |
| `familys.imei_count` | `number` | 该组织所挂载的设备总数（包含其下级组织的设备） |
| `familys.sid` | `string` | 组织id(今后可以当作familyid使用)，注意不是登录的会话sid。该组织id只在该会话sid范围内有效。 |
| `familys.sname` | `string` | 组织名称 |
| `familys.type` | `string` | 该组织的类型<br>e_type_user 终端组织<br>e_type_busness 销售商 |
| `flag` | `array` | 如果账号有冲突的情况下，将返回多个值，可以在调用本接口时选择其中一个传入flag<br>e_account_login 用户登录<br>e_device_login 设备号登录<br>e_phone_device_login 手机号绑定的设备号登录<br>e_phone_account_login 手机号绑定的账号登录 |
| `is_bind_wx_type` | `string` | 是否返回绑定wx的type |
| `is_check_phone` | `boolean` | 是否绑定手机号的账户,可以用来判断是否是海外账号 |
| `is_need_check` | `boolean` | 是否需要检查资料.true需要调用SetAccount补全手机号码和密码，false不需要 |
| `is_register_phone` | `boolean` | 是否是手机号码注册的账户 |
| `jpush` | `string` | 用于账户绑定推送消息 |
| `lack` | `array` | 如果is_need_check返回true,这里将返回缺少的信息<br>e_lack_pass 缺少密码<br>e_lack_phone 缺少绑定电话 |
| `push_switch` | `boolean` | 此用户是否需要推送用户推送标志,true代表开 false代表关，打开了就可以将告警信息推送给app |
| `sid` | `string` | 会话id 后续请求必带,如果账号有冲突的情况下,将不返回 |
| `zone` | `number` | 如果是手机号绑定登录，返回手机号的区号 |

### 返回示例

```json
{
  "errcode": 0,
  "auth": [
    "e_ua_show_family|e_ua_show_summary|e_ua_show_tool|e_ua_show_devops|e_ua_show_oss|e_ua_modify_oss|e_ua_modify_appflag|e_ua_modify_appversion|e_ua_modify_model|e_ua_show_model|e_ua_modify_auth|e_ua_load_new_dev|e_ua_load_iccid|e_ua_no_check|e_ua_add_message|e_ua_modify_file|e_ua_show_login|e_ua_test_pay|e_ua_batch_load|e_ua_show_phone|e_ua_show_orderlist|e_ua_show_iccid_log|e_ua_show_pwd|e_ua_show_sub_package|e_ua_check_draw_package|e_ua_modify_devicepackage|e_ua_add_devicepackage|e_ua_check_wxuser|e_ua_modify_increment|e_ua_modify_increment_platform|e_ua_show_homepage|e_ua_show_increment|e_ua_check_message|e_ua_show_order_complaintlist|e_ua_modify_order_complaintlist|e_ua_refund_order|e_ua_show_order_refundlist|e_ua_modify_order_complaint_notify|e_ua_transfer_account|e_ua_show_transfer_accountlist|e_ua_websocket|e_ua_websocket_gchild|e_ua_websocket_gterm|e_ua_show_sim|e_ua_beacon|e_ua_modify_beacon|e_ua_modify_wxpay_config|e_ua_work_bench|e_ua_limit_login|e_ua_show_host|e_ua_add_host|e_ua_modify_host|e_ua_del_host|e_ua_car_show_carreport|e_ua_share_risk_area|e_ua_is_teacher"
  ],
  "error_message": "f62eedd7-3d40-480c-aec1-8d0790758172",
  "familys": [
    {
      "auth": [
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily",
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily",
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily"
      ],
      "imei_count": 146093963,
      "sid": "2520cb90-2b8a-4c9a-a906-3bbc80effdb7",
      "sname": "e478fba8-6f57-4f03-9e8b-24dabdddf899",
      "type": "e_type_user|e_type_busness"
    },
    {
      "auth": [
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily",
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily"
      ],
      "imei_count": 146093963,
      "sid": "f7bb22ae-574a-41a8-8719-52b53b86f0fd",
      "sname": "dca9c39a-8016-465b-9d5d-37279820c926",
      "type": "e_type_user|e_type_busness"
    }
  ],
  "flag": [
    "e_account_login|e_device_login|e_phone_device_login|e_phone_account_login",
    "e_account_login|e_device_login|e_phone_device_login|e_phone_account_login",
    "e_account_login|e_device_login|e_phone_device_login|e_phone_account_login",
    "e_account_login|e_device_login|e_phone_device_login|e_phone_account_login"
  ],
  "is_bind_wx_type": "7ae68faa-1f90-4c32-9a0b-7b417e391a31",
  "is_check_phone": false,
  "is_need_check": false,
  "is_register_phone": false,
  "jpush": "0b91fc7b-fcaf-4a5a-8212-a8db57988dff",
  "lack": [
    "e_lack_pass|e_lack_phone",
    "e_lack_pass|e_lack_phone",
    "e_lack_pass|e_lack_phone"
  ],
  "push_switch": false,
  "sid": "d1a99743-d603-436f-9b84-3aaeceb3d5c9",
  "zone": 146093963
}
```

<a id="user-Register"></a>
## 注册 (`Register`)

- 接口 ID：`user.Register`
- 模块：`user`
- 用途：注册
- 原始位置：`source/账户(user).txt` 第 389 行
- URL 参数：源文档未列出

### 说明

限制条件：
1，账户名只能为中文字母数字(不可以为纯数字)且不超过20个字节；
2，必要参数account，phone，pwd和type；
3，nick_name字节范围（0-20），email字节长度范围（0-100）；
4，设备号不需要注册；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Register |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.auto_md5` | `string` | 秒验参数 |
| `params.auto_operator` | `string` | 秒验参数 |
| `params.auto_optoken` | `string` | 秒验参数 |
| `params.auto_phone_operator` | `string` | 秒验参数 |
| `params.auto_token` | `string` | 秒验参数 |
| `params.code` | `string` | 验证码，必填,用于短信校验手机号码 |
| `params.family_name` | `string` | 组织名称,如果不填，那么默认创建的family与account同名（注册账号会默认会创建一个组织和一个默认车组，该组织为平台APP的下级组织。）， |
| `params.info` | `object` | 用户信息 |
| `params.info.account` | `string` | 账号,如果不填，将随机创建一个 |
| `params.info.email` | `string` | 邮箱,选填,如果是国外注册，那么需要使用邮箱进行验证。 |
| `params.info.nick_name` | `string` | 昵称 |
| `params.info.phone` | `string` | 手机号，建议填，不填下次登录的时候需要再次绑定（如果配置了check_phone 为false。那么可以不填） |
| `params.info.pwd` | `string` | 必填，密码 |
| `params.key` | `string` | appkey,这个是由平台自己所拥有的，事先可先告知服务商，用于短信验证 |
| `params.lang` | `string` | 登入之后的语言，非必填，默认中文, 例如:ch 中文; en 英语 |
| `params.phone_code` | `string` | 微信小程序获取手机号的code |
| `params.type` | `number` | 必填，账户挂靠的APP类型，可以向服务商索取（由服务商分配app类型） |
| `params.wx_appid` | `string` | 微信的appid |
| `params.zone` | `string` | 区号,默认86，用于短信校验手机号码 |

### 请求示例

```json
{
  "module": "user",
  "func": "Register",
  "params": {
    "auto_md5": "a4c7f7ad-109f-4e5f-8625-c19d40b88291",
    "auto_operator": "91e4c30c-ac21-4b32-91ef-2aa67f1f9d11",
    "auto_optoken": "6ce5eb88-510c-4f97-b869-c41d9d3268ae",
    "auto_phone_operator": "6fdf9cea-c5d3-44cc-a829-bac1224f64e7",
    "auto_token": "34d01516-e502-4f2f-9238-3607ee02088e",
    "code": "240f0532-7b11-41d8-b0ff-cd75a74e32ec",
    "family_name": "615fc731-d980-41b6-895d-14097139fdfb",
    "info": {
      "account": "3685b361-8b41-45c4-8cac-c0ca1eb0edd7",
      "email": "a2fc91fa-e6c9-4af5-aed8-a55c6ae61d78",
      "nick_name": "4e40ec73-6b1e-4961-b31a-ec4a2ecb8c79",
      "phone": "080e406e-73d5-4a46-8e0f-7669c1ef68cc",
      "pwd": "8125f1e4-9fe4-4aa3-966b-0adbc64fe8b4"
    },
    "key": "6b092245-877c-4386-9e2f-6334277c280d",
    "lang": "e48058fb-5a90-42eb-b65f-e533f4ed51da",
    "phone_code": "dfd29f90-b0c5-436e-8cbe-5f053f04c703",
    "type": 2040654628,
    "wx_appid": "43f99d31-52ee-44d9-8b6b-197bfa54bdb2",
    "zone": "b8065937-14bb-4d7b-89b6-e15b54961f30"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `phone` | `string` | 如果是通过秒验，返回手机号码 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "7033d957-91f6-4203-94ec-bd0e88323b49",
  "phone": "9f8f3c55-a8f0-43e9-9a7b-39c6d194d748"
}
```

<a id="user-AddAccount"></a>
## 添加帐号 (`AddAccount`)

- 接口 ID：`user.AddAccount`
- 模块：`user`
- 用途：添加帐号
- 原始位置：`source/账户(user).txt` 第 486 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，需要添加账户权限；
2，必要参数account，pwd和familyid；
3，nick_name和email字节长度范围（0-100）；
4，只能账户登录；
3，对应权限e_ua_add_user

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,AddAccount |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.check_phone` | `boolean` | 是否必须校验手机号码，默认是，如果全都不需要校验，那么平台应该配置该flag的属性，在发送给服务器端请求当中默认将该字段全部设为false。 |
| `params.familyid` | `string` | 必填，添加账号的管理员所属的组织id（familyid，这用于检查是否具有权限添加下级该组织或该组织的下属组织的账号） |
| `params.info` | `object` | 用户信息 |
| `params.info.account` | `string` | 账号,如果不填，将随机创建一个 |
| `params.info.email` | `string` | 邮箱,选填 |
| `params.info.nick_name` | `string` | 昵称,选填 |
| `params.info.pwd` | `string` | 密码 |
| `params.info.role` | `string` | 用户类型<br>e_role_user 普通用户权限<br>e_role_manager 管理员权限<br>e_role_tool 售后人员权限<br>e_role_tooluser 无权限的售后人员 |

### 请求示例

```json
{
  "module": "user",
  "func": "AddAccount",
  "params": {
    "check_phone": true,
    "familyid": "5af34771-0df6-4054-8bd3-e236bf0cd687",
    "info": {
      "account": "7167a5ce-5b5e-4cfe-b904-8878a554f611",
      "email": "cd02d4b9-0c28-473a-9427-e9dc837b2f1d",
      "nick_name": "ab95e293-375e-4b2d-9d12-f2ed8fad60b8",
      "pwd": "2ad78604-1ec1-412e-b427-277a4cf8dd89",
      "role": "e_role_user|e_role_manager|e_role_tool|e_role_tooluser|e_role_multi"
    }
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `uid` | `string` | 添加用户所生成的用户id |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "bd9c6df9-c343-4c8d-bc86-39dcb32f0e11",
  "uid": "a21cb26c-8391-4b3d-81df-a01c12d12c17"
}
```

<a id="user-DelAccount"></a>
## 删除同级帐号 (`DelAccount`)

- 接口 ID：`user.DelAccount`
- 模块：`user`
- 用途：删除同级帐号
- 原始位置：`source/账户(user).txt` 第 557 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，需要删除账户权限；
2，只能账户登录；
3，不能删除自己；
4，对应权限e_ua_del_user

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,DelAccount |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.uid` | `string` | 要删除的用户id,不能删除自己 |

### 请求示例

```json
{
  "module": "user",
  "func": "DelAccount",
  "params": {
    "uid": "525d64f6-0435-4c5d-85d7-f59da122f568"
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
  "error_message": "734d2a17-e4f8-4bb6-b81f-6d6873f337bb"
}
```

<a id="user-SetAccount"></a>
## 修改用户信息 (`SetAccount`)

- 接口 ID：`user.SetAccount`
- 模块：`user`
- 用途：修改用户信息
- 原始位置：`source/账户(user).txt` 第 598 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，需要修改账户信息权限；
2，对应权限e_ua_modify_user_info
3，不能自己修改自己的角色

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SetAccount |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.auto_md5` | `string` | 秒验参数 |
| `params.auto_operator` | `string` | 秒验参数 |
| `params.auto_optoken` | `string` | 秒验参数 |
| `params.auto_phone_operator` | `string` | 秒验参数 |
| `params.auto_token` | `string` | 秒验参数（用于手机验证的信息） |
| `params.change_bind` | `boolean` | 是否更换手机号码绑定的imei号为当前imei号,默认false（设备号登录才有效的字段） |
| `params.check_phone` | `boolean` | 是否必须校验手机号码，默认是，如果全都不需要校验，那么需要通知平台配置该flag的属性,只有在设备号登录，并且置空了密码的情况下，设置该参数才生效 |
| `params.code` | `string` | 验证码,（如果平台设定的check_phone属性为true，那么就需要填写该字段，该字段与phone字段一起使用使用）用于短信校验手机号码 |
| `params.info` | `object` | 待修改用户信息 |
| `params.info.email` | `string` | 邮箱,选填（在check_phone为false那么可以通过设置该值进行验证） |
| `params.info.nick_name` | `string` | 昵称,选填，如果填了字节范围为（1-20） |
| `params.info.phone` | `string` | 手机号（设备修改手机号需要的字段）,选填 |
| `params.info.push_sound` | `string` | 推送提示音, 内容为声音文件名称(不带后缀)，传空串则为跟随系统 |
| `params.info.pwd` | `string` | 密码,选填（修改密码时需要的字段），如果填写字节范围必须为(6-20) |
| `params.info.role` | `string` | 用户类型,选填（修改用户类别时需要的字段，修改该字段需要拥有相对应的权限，可在用户权限中查看）<br>e_role_user 普通用户权限<br>e_role_manager 管理员权限<br>e_role_tool 售后人员权限<br>e_role_tooluser 无权限的售后人员 |
| `params.info.send_time` | `string` | 报警时间段结束时间，选填 00:00:00 |
| `params.info.sstart_time` | `string` | 报警时间段开始时间，选填 00:00:00 |
| `params.info.uid` | `string` | 用户加密后的uid,设备号登录可以不用填写；账号修改自己可以不用填； |
| `params.is_limit_login` | `boolean` | 是否禁止登录 |
| `params.key` | `string` | appkey这个是由平台自己所拥有的，事先可先告知服务商，用于短信验证 |
| `params.phone_code` | `string` | 微信小程序获取手机号的code |
| `params.push_switch` | `boolean` | 用户推送标志，此用户是否需要推送用户的推送标志,true代表开 false代表关。打开了就可以将告警信息推送给app。 |
| `params.pwd_md5` | `string` | md5加密密码 |
| `params.wx_appid` | `string` | 微信的appid |
| `params.zone` | `string` | 区号,默认86，用于短信校验手机号码 |

### 请求示例

```json
{
  "module": "user",
  "func": "SetAccount",
  "params": {
    "auto_md5": "8fce7291-693c-47d3-b293-dcae603f2faf",
    "auto_operator": "d52115b0-b943-407b-86ab-b80dfd627b4f",
    "auto_optoken": "a170a9fa-bd71-4a83-b03c-98df972c7926",
    "auto_phone_operator": "6b81f65a-52e2-4ba3-84e1-4b72f5c1f400",
    "auto_token": "c7816ae2-b0a8-4416-a9b1-360f9ee0d354",
    "change_bind": true,
    "check_phone": true,
    "code": "1d48b52b-fe2c-4d17-a5ec-1365b09b8cfe",
    "info": {
      "email": "8ac3b5ee-538b-4ae5-a032-21d278a5df47",
      "nick_name": "ec3cda1a-ddd2-40df-903f-b8430a675361",
      "phone": "4175e36e-7589-4e21-b643-961a89227eba",
      "push_sound": "b4702141-e7c4-438b-8033-d34cea96589d",
      "pwd": "09089765-eebe-49a2-b085-c3d42d2bfdd4",
      "role": "e_role_user|e_role_manager|e_role_tool|e_role_tooluser|e_role_multi",
      "send_time": "c3fcd07d-794a-4e20-a1c5-79bafd1a60ca",
      "sstart_time": "2c124f45-b2d2-4c46-a5d9-d95e97a90f11",
      "uid": "4699716c-eeee-4092-85dd-2f8c87b82947"
    },
    "is_limit_login": true,
    "key": "4f80e962-d45a-469e-b0d4-9b915b1d647b",
    "phone_code": "019bf495-1b4f-478b-b7ea-d1585629dad0",
    "push_switch": true,
    "pwd_md5": "115b3855-19fe-4d21-a66d-6c6cb95f53a7",
    "wx_appid": "12914ba9-1043-4861-94b7-7436a48e47c6",
    "zone": "c99e2633-96df-4acf-8ac9-8d51458d5963"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `phone` | `string` | 所绑定的手机号，秒验返回 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "acde70f4-34b6-471d-b69f-d838894c0554",
  "phone": "97bd2b04-4230-46ef-9e2d-64a6340d3cce"
}
```

<a id="user-AddNextAccount"></a>
## 添加下级用户(组织) (`AddNextAccount`)

- 接口 ID：`user.AddNextAccount`
- 模块：`user`
- 用途：添加下级用户(组织)
- 原始位置：`source/账户(user).txt` 第 718 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，需要添加下级权限；
2，对应权限e_ua_add_next_user
3，创建了一个下级用户会自动创建一个组织

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,AddNextAccount |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.check_phone` | `boolean` | 是否必须校验手机号码，默认是，如果全都不需要校验，那么需要通知平台配置该flag的属性。向服务器请求参数的时候都发送为false。 |
| `params.contact` | `string` | 组织联系人,选填 |
| `params.family_name` | `string` | 组织名称,如果不填，那么默认创建的family与account同名（在创建下级用户的时候会默认创建一个组织，该组织也会拥有一个默认车组，该账号就是该组织的默认账号，该组织的名字就为该字段） |
| `params.info` | `object` | 用户信息 |
| `params.info.account` | `string` | 账号 |
| `params.info.email` | `string` | 邮箱,选填 |
| `params.info.nick_name` | `string` | 昵称，选填字节范围（0-20） |
| `params.info.pwd` | `string` | 密码 |
| `params.parent_id` | `string` | 添加账号的所属父组织id,即上级组织的组织idfamilyid（就创建账号和组织之后所属的一个父组织id） |

### 请求示例

```json
{
  "module": "user",
  "func": "AddNextAccount",
  "params": {
    "check_phone": false,
    "contact": "ac9fb68a-c1cf-42f2-a2f1-fccaecb4086b",
    "family_name": "e36586f7-46ec-475c-99cf-24f2fe70957c",
    "info": {
      "account": "96907dec-1606-45bb-a7a9-77e0fadc5d36",
      "email": "7eaddb03-dc29-4350-9716-f62000da7940",
      "nick_name": "b30fb6c1-a6c5-479b-8b83-d3bcb640e8fe",
      "pwd": "33153b87-4ea3-4abc-a7a8-10a6d1c2359e"
    },
    "parent_id": "bd639879-49c1-4e2f-85b7-0b6f15cc12c5"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fid` | `string` | 添加后生成的加密组织id（就是默认创建的组织id---familyid） |
| `uid` | `string` | 添加后生成的加密uid |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "a542136b-1117-4c79-89c3-9c3bc7e4b6a1",
  "fid": "4b22ae22-f229-4ebd-b0d4-21c4eccd8ffb",
  "uid": "53eda658-234b-4b20-b3cc-f5256d1b9fc3"
}
```

<a id="user-DelNextAccount"></a>
## 删除下级用户 (`DelNextAccount`)

- 接口 ID：`user.DelNextAccount`
- 模块：`user`
- 用途：删除下级用户
- 原始位置：`source/账户(user).txt` 第 789 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，需要删除下级权限；
2，不能删除自己；
3，对应权限e_ua_del_next_user
4，如果该账号挂载在几个组织下，那么只会删除该组织的信息而不会删除账号信息

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,DelNextAccount |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.uid` | `string` | 待删除用户uid,不能自己删自己 |

### 请求示例

```json
{
  "module": "user",
  "func": "DelNextAccount",
  "params": {
    "uid": "468d2617-a9be-4507-8bce-f230b1d7217b"
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
  "error_message": "a1dfba1e-fc63-4061-9b54-31b5dc8e8195",
  "task_id": "0fabc81d-4cbf-40af-b582-4fcabbc6ab7b"
}
```

<a id="user-GetAccountList"></a>
## 获取用户列表 (`GetAccountList`)

- 接口 ID：`user.GetAccountList`
- 模块：`user`
- 用途：获取用户列表
- 原始位置：`source/账户(user).txt` 第 833 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetAccountList |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.familyid` | `string` | 用户组织id，将获取该组织下的所有用户（不包含下级的用户） |
| `params.get_manager` | `boolean` | 过滤条件，仅查询拥有管理权限的用户（e_ua_user_own_dev） |
| `params.last_uid` | `string` | 上一次获取到的最后一个用户uid，用于分页 |
| `params.limit_size` | `number` | 限制获取数量,最大100 |
| `params.role` | `string` | 用户类型，设置该字段之后只会获取该类型的用户<br>e_role_user 普通用户权限<br>e_role_manager 管理员权限<br>e_role_tool 售后人员权限<br>e_role_tooluser 无权限的售后人员 |

### 请求示例

```json
{
  "module": "user",
  "func": "GetAccountList",
  "params": {
    "familyid": "b68f2339-46c5-4ad4-94ae-faa9bc75cd35",
    "get_manager": true,
    "last_uid": "ba916be3-5b0d-4dc8-909e-c507fdf9bce7",
    "limit_size": 759848650,
    "role": "e_role_user|e_role_manager|e_role_tool|e_role_tooluser|e_role_multi"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `contact` | `string` | 组织联系人 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `info` | `array` | 用户信息 |
| `info.account` | `string` | 用户的账号 |
| `info.email` | `string` | 邮箱 |
| `info.email_modify` | `boolean` | 更改邮箱的时候，是否不需要校验邮箱验证码，默认是，不需要 |
| `info.is_bind_wx` | `boolean` | 是否绑定微信 |
| `info.is_check_user` | `boolean` | 是否是拥有审核权限(默认不返回) |
| `info.is_limit_login` | `boolean` | 是否禁止登录 |
| `info.is_mine` | `boolean` | 是否是当前用户，默认不是 |
| `info.nick_name` | `string` | 昵称 |
| `info.phone` | `string` | 用户所绑定的手机号 |
| `info.role` | `string` | 用户类型<br>e_role_user 普通用户权限<br>e_role_manager 管理员权限<br>e_role_tool 售后人员权限<br>e_role_tooluser 无权限的售后人员 |
| `info.role_name` | `string` | 角色名称 |
| `info.uid` | `string` | 用户加密后的uid, |
| `total` | `number` | 该组织下的用户总量，不包含该组织的下级组织的用户 |

### 返回示例

```json
{
  "errcode": 0,
  "contact": "7f8cf9d7-d774-4690-8d0a-09b8a749baae",
  "error_message": "5f4d255d-dc5f-447e-96ed-2cb2b40da632",
  "info": [
    {
      "account": "630ac160-3cfe-4170-b110-7ae88e05492b",
      "email": "400c85e7-ad3a-4944-9515-c203e24c393a",
      "email_modify": true,
      "is_bind_wx": true,
      "is_check_user": true,
      "is_limit_login": true,
      "is_mine": true,
      "nick_name": "06e65f91-abe0-4be1-913e-766291dd3012",
      "phone": "c1c96723-a5e0-4b65-99f7-39ec4699abf3",
      "role": "e_role_user|e_role_manager|e_role_tool|e_role_tooluser|e_role_multi",
      "role_name": "b7bdcdd6-b80b-4fe7-93df-38b6ca215806",
      "uid": "2639d379-7e36-4974-9b5f-6ef8262ab841"
    },
    {
      "account": "659338e0-a224-475a-a92b-138b81a83a9d",
      "email": "32103466-5d83-4c3e-ba70-f0ba0bfc3f80",
      "email_modify": true,
      "is_bind_wx": true,
      "is_check_user": true,
      "is_limit_login": true,
      "is_mine": true,
      "nick_name": "5f8c0a69-3228-4920-90cb-d3b76d823e70",
      "phone": "866c3fd1-3613-4898-ab66-681c669e7d77",
      "role": "e_role_user|e_role_manager|e_role_tool|e_role_tooluser|e_role_multi",
      "role_name": "b4c590d4-7f8b-4969-bd9f-18161b41f007",
      "uid": "b1c5615c-d73c-45aa-9e53-9cc704173234"
    },
    {
      "account": "9a7a2eb3-bfc6-4bbc-abf8-ef38660ce365",
      "email": "1e8c27f8-b5f5-46f7-8e9d-2c8d08ab4018",
      "email_modify": true,
      "is_bind_wx": true,
      "is_check_user": true,
      "is_limit_login": true,
      "is_mine": true,
      "nick_name": "f96d9d27-5ab3-4ed7-92dd-d1865df3d357",
      "phone": "6f5c77e7-fa23-443c-8311-ef4f040e7895",
      "role": "e_role_user|e_role_manager|e_role_tool|e_role_tooluser|e_role_multi",
      "role_name": "f35d0cd1-35e3-4a0f-b7e4-b7ec97075a7c",
      "uid": "0fd868c8-753c-49ec-bc21-53e5d357d263"
    },
    {
      "account": "fa2bbfb2-1772-4a41-a31e-43a76d72d59c",
      "email": "c17f2675-828e-4012-b934-fbdbfef74eb6",
      "email_modify": true,
      "is_bind_wx": true,
      "is_check_user": true,
      "is_limit_login": true,
      "is_mine": true,
      "nick_name": "75da2b90-28a1-4e40-b80a-19a647d9c6aa",
      "phone": "df4dad98-a492-449b-aac8-082efce4d232",
      "role": "e_role_user|e_role_manager|e_role_tool|e_role_tooluser|e_role_multi",
      "role_name": "5933cde1-638b-4b04-a74f-696e21f4faab",
      "uid": "fd3c3e50-11d0-4a41-8859-d3a70a823bda"
    }
  ],
  "total": 325670538
}
```

<a id="user-LoginOut"></a>
## 退出登录 (`LoginOut`)

- 接口 ID：`user.LoginOut`
- 模块：`user`
- 用途：退出登录
- 原始位置：`source/账户(user).txt` 第 978 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,LoginOut |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |

### 请求示例

```json
{
  "module": "user",
  "func": "LoginOut",
  "params": {}
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
  "error_message": "5c3cfbaf-b0c2-4c55-a726-8e99ae881975"
}
```

<a id="user-WXBindUser"></a>
## 微信绑定用户 (`WXBindUser`)

- 接口 ID：`user.WXBindUser`
- 模块：`user`
- 用途：微信绑定用户
- 原始位置：`source/账户(user).txt` 第 1009 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，参数都为必填；
2，一个账户可以绑定多个微信号，但是一个微信号只能绑定一个账号；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,WXBindUser |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.keyid` | `string` | 微信公众号appid对应的key,必填， 1零距离，2卓比汽车在线 |
| `params.source_app` | `boolean` | 来源 |
| `params.touser` | `string` | 微信touken，必填 |
| `params.wx_appid` | `string` | 微信的appid |
| `params.wx_code` | `string` | 微信code这个和openid二选一 |

### 请求示例

```json
{
  "module": "user",
  "func": "WXBindUser",
  "params": {
    "keyid": "28b78738-11b3-4ccd-afc3-0f2453af20b3",
    "source_app": true,
    "touser": "84a08292-3294-4784-b14c-b5ef8ff01d37",
    "wx_appid": "96df9e70-6fc1-4afa-aaf1-6d3cf5d5b5c6",
    "wx_code": "f27eb0ff-9cde-4c21-9f28-b64951b0f897"
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
  "error_message": "98713c29-e8d6-4518-8cbc-77634eb54d60"
}
```

<a id="user-IsBindWX"></a>
## 登录绑定微信账号 (`IsBindWX`)

- 接口 ID：`user.IsBindWX`
- 模块：`user`
- 用途：登录绑定微信账号
- 原始位置：`source/账户(user).txt` 第 1060 行
- URL 参数：源文档未列出

### 说明

限制条件：
1，参数都为必填；
2，请先登录后绑定；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,IsBindWX |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.info` | `string` | 手机登录信息:IP;IMEI;CCID;ADDR等 |
| `params.lang` | `string` | 登入之后的语言，非必填，默认中文, 例如:ch 中文; en 英语 |
| `params.limit_size` | `number` | 限制获取组织信息数量，默认20 |
| `params.mpm` | `string` | 推送通道，暂时不用，目前默认激光推送<br>e_jpush 极光推送<br>e_hpush 华为推送<br>e_xmpush 小米推送<br>e_oppopush oppo推送<br>e_vivopush vivo推送<br>e_xmiospush 小米ios推送<br>e_iospush IOS原生推送<br>e_googlepush google原生推送<br>e_webhwpush web端推送<br>e_honorpush 荣耀推送<br>e_iosp12 ios P12推送<br>e_wx_official 微信公众号推送 |
| `params.mpm_identify` | `string` | 推送标志，暂时不用 |
| `params.openid` | `string` | 微信openid,与wx_code二选一 |
| `params.platform` | `string` | 必填，登录平台,ios,android,winphone,web，wx,便于追踪用户登入情况 |
| `params.source_app` | `boolean` | 来源 |
| `params.str_type` | `string` | 账户挂靠的组织类型 |
| `params.type` | `number` | 账户挂靠的APP类型，必填，可以向服务商索取 |
| `params.web_push` | `boolean` | 是否启动web端推送，注意填了这个参数将覆盖app端推送 |
| `params.wx_appid` | `string` | 微信的appid |
| `params.wx_code` | `string` | 微信code这个和openid二选一 |

### 请求示例

```json
{
  "module": "user",
  "func": "IsBindWX",
  "params": {
    "info": "21cb1538-53e2-441b-9481-19903ebdbf87",
    "lang": "0f2aff25-6397-49d9-8361-c3977603a9a7",
    "limit_size": 987397099,
    "mpm": "e_jpush|e_hpush|e_xmpush|e_oppopush|e_vivopush|e_xmiospush|e_iospush|e_googlepush|e_webhwpush|e_honorpush|e_iosp12|e_wx_official",
    "mpm_identify": "3018aa2c-514c-4cd8-afb2-3b33c9302bb2",
    "openid": "c8cf744e-6795-4f1b-af93-710c2d2823cd",
    "platform": "63e90bf4-050b-4325-a637-1ecc668cdd7e",
    "source_app": false,
    "str_type": "5ff912fb-e55e-4752-837f-cd42995ea73b",
    "type": 987397099,
    "web_push": false,
    "wx_appid": "e06dabe1-81d7-463f-8c5e-be28fa18605b",
    "wx_code": "de3a9b48-5b73-4a2c-835f-ba3583626050"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `account` | `string` | 自己的账号 |
| `auth` | `array` | 权限列表 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `familys` | `array` | 组织信息 |
| `familys.auth` | `array` | 权限列表（账号登录返回） |
| `familys.imei_count` | `number` | 设备总数包含下级组织的设备数 |
| `familys.sid` | `string` | 组织id，注意不是登录的sid。这是一个组织id--familyid，这个只在会话sid的登录状态下有效 |
| `familys.sname` | `string` | 组织名称 |
| `familys.type` | `string` | e_type_user 终端组织<br>e_type_busness 销售商 |
| `flag` | `array` | 如果账号有冲突的情况下，将返回多个值，可以在调用本接口时选择其中一个传入flag<br>e_account_login 用户登录<br>e_device_login 设备号登录<br>e_phone_device_login 手机号绑定的设备号登录<br>e_phone_account_login 手机号绑定的账号登录 |
| `jpush` | `string` | 用于账户绑定推送消息 |
| `openid` | `string` | 如果填写wx_code,正常的话的话返回 |
| `push_switch` | `boolean` | 此用户是否需要推送用户推送标志,ture代表开 false代表关。设置开之后，可以推送设定的警告信息推送给app |
| `sid` | `string` | 会话id 后续请求必带 |

### 返回示例

```json
{
  "errcode": 0,
  "account": "96456457-f291-4ad7-a42a-5501a3f5ab51",
  "auth": [
    "e_ua_show_family|e_ua_show_summary|e_ua_show_tool|e_ua_show_devops|e_ua_show_oss|e_ua_modify_oss|e_ua_modify_appflag|e_ua_modify_appversion|e_ua_modify_model|e_ua_show_model|e_ua_modify_auth|e_ua_load_new_dev|e_ua_load_iccid|e_ua_no_check|e_ua_add_message|e_ua_modify_file|e_ua_show_login|e_ua_test_pay|e_ua_batch_load|e_ua_show_phone|e_ua_show_orderlist|e_ua_show_iccid_log|e_ua_show_pwd|e_ua_show_sub_package|e_ua_check_draw_package|e_ua_modify_devicepackage|e_ua_add_devicepackage|e_ua_check_wxuser|e_ua_modify_increment|e_ua_modify_increment_platform|e_ua_show_homepage|e_ua_show_increment|e_ua_check_message|e_ua_show_order_complaintlist|e_ua_modify_order_complaintlist|e_ua_refund_order|e_ua_show_order_refundlist|e_ua_modify_order_complaint_notify|e_ua_transfer_account|e_ua_show_transfer_accountlist|e_ua_websocket|e_ua_websocket_gchild|e_ua_websocket_gterm|e_ua_show_sim|e_ua_beacon|e_ua_modify_beacon|e_ua_modify_wxpay_config|e_ua_work_bench|e_ua_limit_login|e_ua_show_host|e_ua_add_host|e_ua_modify_host|e_ua_del_host|e_ua_car_show_carreport|e_ua_share_risk_area|e_ua_is_teacher"
  ],
  "error_message": "0f5f921c-e46a-414a-8f16-8a8035f50153",
  "familys": [
    {
      "auth": [
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily",
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily"
      ],
      "imei_count": 243177919,
      "sid": "83853136-c282-4abe-858a-26870b33a7ef",
      "sname": "afeed45c-a60e-4056-8c77-b4ab5d9e50c8",
      "type": "e_type_user|e_type_busness"
    },
    {
      "auth": [
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily",
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily",
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily"
      ],
      "imei_count": 243177919,
      "sid": "ac83cbb4-4f41-4dd5-932d-23716651ac91",
      "sname": "88d894d3-151a-44d0-b941-6ea03e4d4ab5",
      "type": "e_type_user|e_type_busness"
    },
    {
      "auth": [
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily",
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily",
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily"
      ],
      "imei_count": 243177919,
      "sid": "1563a1a9-70c6-40f6-b3a4-9db918a37bfe",
      "sname": "fda75ab5-6236-4fef-a4eb-7fb890380363",
      "type": "e_type_user|e_type_busness"
    },
    {
      "auth": [
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily",
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily"
      ],
      "imei_count": 243177919,
      "sid": "61237812-224c-421d-8bb1-ff94178c7e50",
      "sname": "2e83dac5-38e7-42e0-9181-659eb87ddf2f",
      "type": "e_type_user|e_type_busness"
    }
  ],
  "flag": [
    "e_account_login|e_device_login|e_phone_device_login|e_phone_account_login",
    "e_account_login|e_device_login|e_phone_device_login|e_phone_account_login",
    "e_account_login|e_device_login|e_phone_device_login|e_phone_account_login"
  ],
  "jpush": "8ed550ed-115f-4996-88b9-d20d61d413d0",
  "openid": "5a64eb87-0be2-458f-8dea-4abc79b84c27",
  "push_switch": false,
  "sid": "b890c762-5b4f-4654-ae03-dc0f46ce0c44"
}
```

<a id="user-UnBindWX"></a>
## 解除绑定微信 (`UnBindWX`)

- 接口 ID：`user.UnBindWX`
- 模块：`user`
- 用途：解除绑定微信
- 原始位置：`source/账户(user).txt` 第 1398 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,UnBindWX |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.uid` | `string` | 要解除的用户id,自己就不用填写 |

### 请求示例

```json
{
  "module": "user",
  "func": "UnBindWX",
  "params": {
    "uid": "180aad80-c782-4804-93ea-de76c81cc613"
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
  "error_message": "90d6f426-e840-4a63-8fe8-fb03dde27253"
}
```

<a id="user-GetUidInfo"></a>
## 获取单用户信息 (`GetUidInfo`)

- 接口 ID：`user.GetUidInfo`
- 模块：`user`
- 用途：获取单用户信息
- 原始位置：`source/账户(user).txt` 第 1433 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetUidInfo |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.get_is_bind_wx` | `boolean` | 获取是否绑定了wx的type |
| `params.limit_size` | `number` | 限制获取的组织数量，默认0，不获取（一个账号可能挂载多个组织） |
| `params.uid` | `string` | 需要获取信息的账号id，可以为账号，设备号， |

### 请求示例

```json
{
  "module": "user",
  "func": "GetUidInfo",
  "params": {
    "get_is_bind_wx": false,
    "limit_size": 2142241347,
    "uid": "d01ec1fe-ae9d-40be-95ae-569498abb25e"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `account` | `string` | 账号 |
| `app_name` | `string` | 账号所属的平台名称 |
| `auth` | `array` | 权限列表,limit_size传入大于0并且账号登录才返回 |
| `bind_time` | `number` | 绑定手机号的时间 |
| `email` | `string` | 邮箱 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `familys` | `array` | 组织信息,请求参数limit_size传入大于0并且账号登录才返回 |
| `familys.auth` | `array` | 权限列表 |
| `familys.imei_count` | `number` | 设备数 |
| `familys.sid` | `string` | 组织id，同familyid一样表示组织id，注意不是登录的会话sid |
| `familys.sname` | `string` | 名称 |
| `familys.type` | `string` | 组织类型<br>e_type_user 终端组织<br>e_type_busness 销售商 |
| `is_bind_wx_type` | `string` | 是否返回绑定wx的type |
| `nick_name` | `string` | 昵称 |
| `phone` | `string` | 手机号,修改时可以不用填写，添加注册必填 |
| `push_sound` | `string` | 推送提示音, 内容为声音文件名称(不带后缀)，空串或不存在则为跟随系统 |
| `push_switch` | `boolean` | 用户推送标志。如果为true就是开，false为关。如果开着，那么就拥有推送功能。 |
| `send_time` | `string` | 报警时间段的结束时间 00:00:00 |
| `sstart_time` | `string` | 报警时间段的开始时间 00:00:00 |

### 返回示例

```json
{
  "errcode": 0,
  "account": "3e7482ba-6bbd-4fcf-aef1-ac5a63882fc2",
  "app_name": "e94260c0-a284-4449-96ab-5a2637ca62e6",
  "auth": [
    "e_ua_show_family|e_ua_show_summary|e_ua_show_tool|e_ua_show_devops|e_ua_show_oss|e_ua_modify_oss|e_ua_modify_appflag|e_ua_modify_appversion|e_ua_modify_model|e_ua_show_model|e_ua_modify_auth|e_ua_load_new_dev|e_ua_load_iccid|e_ua_no_check|e_ua_add_message|e_ua_modify_file|e_ua_show_login|e_ua_test_pay|e_ua_batch_load|e_ua_show_phone|e_ua_show_orderlist|e_ua_show_iccid_log|e_ua_show_pwd|e_ua_show_sub_package|e_ua_check_draw_package|e_ua_modify_devicepackage|e_ua_add_devicepackage|e_ua_check_wxuser|e_ua_modify_increment|e_ua_modify_increment_platform|e_ua_show_homepage|e_ua_show_increment|e_ua_check_message|e_ua_show_order_complaintlist|e_ua_modify_order_complaintlist|e_ua_refund_order|e_ua_show_order_refundlist|e_ua_modify_order_complaint_notify|e_ua_transfer_account|e_ua_show_transfer_accountlist|e_ua_websocket|e_ua_websocket_gchild|e_ua_websocket_gterm|e_ua_show_sim|e_ua_beacon|e_ua_modify_beacon|e_ua_modify_wxpay_config|e_ua_work_bench|e_ua_limit_login|e_ua_show_host|e_ua_add_host|e_ua_modify_host|e_ua_del_host|e_ua_car_show_carreport|e_ua_share_risk_area|e_ua_is_teacher",
    "e_ua_show_family|e_ua_show_summary|e_ua_show_tool|e_ua_show_devops|e_ua_show_oss|e_ua_modify_oss|e_ua_modify_appflag|e_ua_modify_appversion|e_ua_modify_model|e_ua_show_model|e_ua_modify_auth|e_ua_load_new_dev|e_ua_load_iccid|e_ua_no_check|e_ua_add_message|e_ua_modify_file|e_ua_show_login|e_ua_test_pay|e_ua_batch_load|e_ua_show_phone|e_ua_show_orderlist|e_ua_show_iccid_log|e_ua_show_pwd|e_ua_show_sub_package|e_ua_check_draw_package|e_ua_modify_devicepackage|e_ua_add_devicepackage|e_ua_check_wxuser|e_ua_modify_increment|e_ua_modify_increment_platform|e_ua_show_homepage|e_ua_show_increment|e_ua_check_message|e_ua_show_order_complaintlist|e_ua_modify_order_complaintlist|e_ua_refund_order|e_ua_show_order_refundlist|e_ua_modify_order_complaint_notify|e_ua_transfer_account|e_ua_show_transfer_accountlist|e_ua_websocket|e_ua_websocket_gchild|e_ua_websocket_gterm|e_ua_show_sim|e_ua_beacon|e_ua_modify_beacon|e_ua_modify_wxpay_config|e_ua_work_bench|e_ua_limit_login|e_ua_show_host|e_ua_add_host|e_ua_modify_host|e_ua_del_host|e_ua_car_show_carreport|e_ua_share_risk_area|e_ua_is_teacher",
    "e_ua_show_family|e_ua_show_summary|e_ua_show_tool|e_ua_show_devops|e_ua_show_oss|e_ua_modify_oss|e_ua_modify_appflag|e_ua_modify_appversion|e_ua_modify_model|e_ua_show_model|e_ua_modify_auth|e_ua_load_new_dev|e_ua_load_iccid|e_ua_no_check|e_ua_add_message|e_ua_modify_file|e_ua_show_login|e_ua_test_pay|e_ua_batch_load|e_ua_show_phone|e_ua_show_orderlist|e_ua_show_iccid_log|e_ua_show_pwd|e_ua_show_sub_package|e_ua_check_draw_package|e_ua_modify_devicepackage|e_ua_add_devicepackage|e_ua_check_wxuser|e_ua_modify_increment|e_ua_modify_increment_platform|e_ua_show_homepage|e_ua_show_increment|e_ua_check_message|e_ua_show_order_complaintlist|e_ua_modify_order_complaintlist|e_ua_refund_order|e_ua_show_order_refundlist|e_ua_modify_order_complaint_notify|e_ua_transfer_account|e_ua_show_transfer_accountlist|e_ua_websocket|e_ua_websocket_gchild|e_ua_websocket_gterm|e_ua_show_sim|e_ua_beacon|e_ua_modify_beacon|e_ua_modify_wxpay_config|e_ua_work_bench|e_ua_limit_login|e_ua_show_host|e_ua_add_host|e_ua_modify_host|e_ua_del_host|e_ua_car_show_carreport|e_ua_share_risk_area|e_ua_is_teacher",
    "e_ua_show_family|e_ua_show_summary|e_ua_show_tool|e_ua_show_devops|e_ua_show_oss|e_ua_modify_oss|e_ua_modify_appflag|e_ua_modify_appversion|e_ua_modify_model|e_ua_show_model|e_ua_modify_auth|e_ua_load_new_dev|e_ua_load_iccid|e_ua_no_check|e_ua_add_message|e_ua_modify_file|e_ua_show_login|e_ua_test_pay|e_ua_batch_load|e_ua_show_phone|e_ua_show_orderlist|e_ua_show_iccid_log|e_ua_show_pwd|e_ua_show_sub_package|e_ua_check_draw_package|e_ua_modify_devicepackage|e_ua_add_devicepackage|e_ua_check_wxuser|e_ua_modify_increment|e_ua_modify_increment_platform|e_ua_show_homepage|e_ua_show_increment|e_ua_check_message|e_ua_show_order_complaintlist|e_ua_modify_order_complaintlist|e_ua_refund_order|e_ua_show_order_refundlist|e_ua_modify_order_complaint_notify|e_ua_transfer_account|e_ua_show_transfer_accountlist|e_ua_websocket|e_ua_websocket_gchild|e_ua_websocket_gterm|e_ua_show_sim|e_ua_beacon|e_ua_modify_beacon|e_ua_modify_wxpay_config|e_ua_work_bench|e_ua_limit_login|e_ua_show_host|e_ua_add_host|e_ua_modify_host|e_ua_del_host|e_ua_car_show_carreport|e_ua_share_risk_area|e_ua_is_teacher"
  ],
  "bind_time": 1988686805,
  "email": "cc0f48cf-3b0c-48c6-963c-4cb239fc683f",
  "error_message": "6e95d125-e249-462a-adad-266c31488a6c",
  "familys": [
    {
      "auth": [
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily",
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily",
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily",
        "e_ua_move_dev|e_ua_del_dev|e_ua_add_dev|e_ua_modify_dev|e_ua_del_from_family|e_ua_modify_relay|e_ua_del_move_family|e_ua_del_move_to_root|e_ua_del_move_to_parent|e_ua_user_own_dev|e_ua_trace_dev|e_ua_querylocation_all|e_ua_group_opt|e_ua_modify_family|e_ua_group_del|e_ua_batch_modify|e_ua_add_fence|e_ua_del_fence|e_ua_modify_fence|e_ua_add_user|e_ua_del_user|e_ua_add_next_user|e_ua_del_next_user|e_ua_modify_role|e_ua_modify_user_info|e_ua_del_alarm|e_ua_del_opt_log|e_ua_show_log|e_ua_change_host|e_ua_modify_log_switch|e_ua_force_off_oil|e_ua_record_del|e_ua_record_open|e_ua_record_start|e_ua_record_get|e_ua_car_add|e_ua_car_modify|e_ua_car_modify_overdue|e_ua_car_modify_loans|e_ua_car_modify_payoff|e_ua_car_del_noinsert|e_ua_car_unbind|e_ua_car_bind|e_ua_stu_modify_school|e_ua_stu_modify_grade|e_ua_stu_add|e_ua_stu_modify|e_ua_stu_add_school|e_ua_stu_add_grade|e_ua_stu_add_class|e_ua_stu_modify_school_name|e_ua_stu_unbind|e_ua_stu_bind|e_ua_stu_modify_class|e_ua_stu_check_application|e_ua_stu_set_course|e_ua_stu_show_course|e_ua_stu_modify_attendance|e_ua_stu_show_attendance|e_ua_stu_add_manage|e_ua_stu_modify_manage|e_ua_modify_package|e_ua_modify_family_increment|e_ua_modify_forward_dev|e_ua_move_family|e_ua_set_multiuser|e_ua_show_multiuser|e_ua_bind_multifamily"
      ],
      "imei_count": 1988686805,
      "sid": "6f3ba70f-a70e-42ce-8990-ab93a232f26c",
      "sname": "7c96233e-ceec-40ef-a09a-35a54a4b3f0a",
      "type": "e_type_user|e_type_busness"
    }
  ],
  "is_bind_wx_type": "0b593573-0634-4beb-afe1-2a14b2a6078a",
  "nick_name": "5234634f-de54-474e-8372-e3c55f3b78ec",
  "phone": "8d5f0ede-dae6-4453-8b0d-a53d6d85ffe2",
  "push_sound": "94a895a6-d210-481f-8995-e053df5ade9a",
  "push_switch": false,
  "send_time": "b9512915-71cf-416c-9634-1e6c9fbdf838",
  "sstart_time": "c2a35606-5ea6-4ef5-87d1-e8021ecbbd5f"
}
```

<a id="user-GetPhoneArea"></a>
## 获取手机号码区号 (`GetPhoneArea`)

- 接口 ID：`user.GetPhoneArea`
- 模块：`user`
- 用途：获取手机号码区号
- 原始位置：`source/账户(user).txt` 第 1707 行
- URL 参数：源文档未列出

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetPhoneArea |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.lang` | `string` | 登入之后的语言，非必填，默认中文, 例如:ch 中文; en 英语 |
| `params.last_country` | `string` | 上次获取到的最后的last_country |

### 请求示例

```json
{
  "module": "user",
  "func": "GetPhoneArea",
  "params": {
    "lang": "f946a9d2-d79c-4a4b-b964-ac74fd8f6d87",
    "last_country": "b4d29b0a-bed9-47da-8d5a-88d08fdcb8e5"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` | 手机配置信息 |
| `items.country` | `string` | 国家 |
| `items.zone` | `number` | 区号 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "529d4489-e1de-4741-8f3f-9b1e7f1f2b67",
  "items": [
    {
      "country": "36ef8b79-9499-40e5-8f9b-e656405f2e45",
      "zone": 1578747687
    },
    {
      "country": "770c8779-e4e9-457b-b308-8f4ed73c2ffa",
      "zone": 1578747687
    }
  ]
}
```

<a id="user-CheckVersion"></a>
## 检查版本 (`CheckVersion`)

- 接口 ID：`user.CheckVersion`
- 模块：`user`
- 用途：检查版本
- 原始位置：`source/账户(user).txt` 第 1759 行
- URL 参数：源文档未列出

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,CheckVersion |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.app_type` | `string` | app类型，该字段的使用，请联系服务器 |
| `params.lang` | `string` | 登入之后的语言，非必填，默认中文, 例如:ch 中文; en 英语 |
| `params.version` | `number` | 当前app版本号 |

### 请求示例

```json
{
  "module": "user",
  "func": "CheckVersion",
  "params": {
    "app_type": "1050bd58-a476-46d9-99a7-151db03f86f7",
    "lang": "4fdb9fe6-cc8b-44a4-a533-e6ebdd3b4708",
    "version": 1438380836
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `info` | `string` | 需要展示的新版本信息 |
| `is_force` | `boolean` | false-不强制更新 true-强制更新 |
| `lang` | `string` | 登入之后的语言，非必填，默认中文, 例如:ch 中文; en 英语 |
| `name` | `string` | 新版本名称 |
| `type` | `string` | 下载方式<br>e_dt_interior 内部下载<br>e_dt_th3 第三方 |
| `url` | `string` | 如果需要升级，这里展示需要升级的url |
| `version` | `number` | 新版本号 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "dcde8b46-a5f7-4e0a-beac-23a1cee963ea",
  "info": "451f2afb-c26b-4eab-9b58-14a3a9c96b12",
  "is_force": true,
  "lang": "ea4d3445-3fc5-4093-a093-c12e5652c39f",
  "name": "7122898d-62d3-4ed1-a742-cfaf19e926e0",
  "type": "e_dt_interior|e_dt_th3",
  "url": "d99a52a2-3c59-42c9-b474-6c4bd340d66e",
  "version": 2075279280
}
```

<a id="user-FindPass"></a>
## 找回密码 (`FindPass`)

- 接口 ID：`user.FindPass`
- 模块：`user`
- 用途：找回密码
- 原始位置：`source/账户(user).txt` 第 1821 行
- URL 参数：源文档未列出

### 说明

限制条件：
1，account和phone都为必填且已注册,设备号登录填写的phone要与入库的phone一致；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,FindPass |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.account` | `string` | 账号或者设备号,必填，如果不填写产生冲突之后，将返回flag列表 |
| `params.auto_md5` | `string` | 秒验参数 |
| `params.auto_operator` | `string` | 秒验参数 |
| `params.auto_optoken` | `string` | 秒验参数 |
| `params.auto_phone_operator` | `string` | 秒验参数 |
| `params.auto_token` | `string` | 秒验参数 |
| `params.code` | `string` | 验证码，用于手机验证，如果需要手机验证就需要该字段 |
| `params.email` | `string` | 邮箱，选填 |
| `params.flag` | `string` | 如果不填写account,产生冲突的情况下，返回的flag选择一个值填进来<br>e_account_login 用户登录<br>e_device_login 设备号登录<br>e_phone_device_login 手机号绑定的设备号登录<br>e_phone_account_login 手机号绑定的账号登录 |
| `params.key` | `string` | appkey，这个是由平台自己所拥有的，事先可先告知服务商，用于短信验证 |
| `params.lang` | `string` | 登入之后的语言，非必填，默认中文, 例如:ch 中文; en 英语 |
| `params.new_pwd` | `string` | 新密码，必填 |
| `params.only_check` | `boolean` | 仅检测是否可以找回密码，如果可以找回，将返回s_key字段 |
| `params.phone` | `string` | 绑定的手机号码 |
| `params.type` | `number` | 账户挂靠的APP类型，可以向服务商索取 |
| `params.zone` | `string` | 区号 |

### 请求示例

```json
{
  "module": "user",
  "func": "FindPass",
  "params": {
    "account": "4e44fb90-0bb3-4c1c-98d1-c0d3b45253de",
    "auto_md5": "53b26cd2-dab5-441d-a5ae-a0b3d68a1c4e",
    "auto_operator": "210364d4-64bd-426c-9dd6-cb9e4fe0d1e5",
    "auto_optoken": "63ecc257-74f5-462d-a03c-1b6f37d540eb",
    "auto_phone_operator": "8d849b8e-809e-4e1c-a093-dd820840ac87",
    "auto_token": "a6a00579-5de2-42f0-a078-9043122d1fdb",
    "code": "5ab37883-669e-45a3-b626-836ce4bb77bc",
    "email": "b7efae40-255b-4622-b0ba-531fe622864e",
    "flag": "e_account_login|e_device_login|e_phone_device_login|e_phone_account_login",
    "key": "f1abd7a3-71b3-4a12-988e-2958a08d98cd",
    "lang": "1e3e944c-a886-4c2d-9b42-8eb6d4c67e0a",
    "new_pwd": "78dd77e1-8406-41e0-971e-f1e2aaffdda6",
    "only_check": true,
    "phone": "b3930050-f54e-4ac0-9a91-1709414df515",
    "type": 1613148050,
    "zone": "e87b718f-d065-4bc3-88a0-faa4befdc015"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `flag` | `array` | 如果账号有冲突的情况下，将返回多个值，可以在调用本接口时选择其中一个传入flag<br>e_account_login 用户登录<br>e_device_login 设备号登录<br>e_phone_device_login 手机号绑定的设备号登录<br>e_phone_account_login 手机号绑定的账号登录 |
| `phone` | `string` | 秒验返回，绑定的手机号码 |
| `s_key` | `string` | 返回该字段，则表示可以找回密码 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "5a050808-7379-4e22-af07-5450c7064ead",
  "flag": [
    "e_account_login|e_device_login|e_phone_device_login|e_phone_account_login",
    "e_account_login|e_device_login|e_phone_device_login|e_phone_account_login",
    "e_account_login|e_device_login|e_phone_device_login|e_phone_account_login",
    "e_account_login|e_device_login|e_phone_device_login|e_phone_account_login"
  ],
  "phone": "62d87de5-fa35-491f-bca3-bd9a39814459",
  "s_key": "4a9c9a78-eb9d-45bd-9c3e-aaa98b67519c"
}
```

<a id="user-Cancel"></a>
## 注销 (`Cancel`)

- 接口 ID：`user.Cancel`
- 模块：`user`
- 用途：注销
- 原始位置：`source/账户(user).txt` 第 1924 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，最多处理10000条；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Cancel |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |

### 请求示例

```json
{
  "module": "user",
  "func": "Cancel",
  "params": {}
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `sfamily` | `array` | 对应着任务id的组织id |
| `task_id` | `array` | 注销账号是异步完成的，返回任务id列表，对应着账号挂载1级组织的任务，下次可以用这个id来查询任务状态,如果没有返回，所以直接删除成功了 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "a5ec4ac6-099d-42fe-aec1-1b839daac49c",
  "sfamily": [
    "73036111-aa47-416c-ac61-09d8be993911"
  ],
  "task_id": [
    "fd428fdc-3ad3-43ed-b88b-3de3835660b0",
    "05441870-89f0-4725-a3da-a5b77ea18202",
    "66155b25-3570-40c6-9725-65686132fd4d"
  ]
}
```

<a id="user-SwitchAuth"></a>
## 交换权限 (`SwitchAuth`)

- 接口 ID：`user.SwitchAuth`
- 模块：`user`
- 用途：交换权限
- 原始位置：`source/账户(user).txt` 第 1970 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，需要设置权限；
2，只能账户登录；
3，不能删除自己；
4，对应权限e_ua_modify_role

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SwitchAuth |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.old_uid` | `string` | 同级的源账户uid,如果不填写，就是转移自己的 |
| `params.uid` | `string` | 同级的目标账户uid |

### 请求示例

```json
{
  "module": "user",
  "func": "SwitchAuth",
  "params": {
    "old_uid": "6b23709f-62e3-4e17-922f-6375b71533fc",
    "uid": "8810ce5e-c8d8-4baa-ad97-2ed14486b70b"
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
  "error_message": "43963702-4256-4aef-b088-90b382714764"
}
```

<a id="user-UnBindPhone"></a>
## 账号解绑电话号码 (`UnBindPhone`)

- 接口 ID：`user.UnBindPhone`
- 模块：`user`
- 用途：账号解绑电话号码
- 原始位置：`source/账户(user).txt` 第 2014 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,UnBindPhone |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.uid` | `string` | 用户加密后的uid,设备号登录可以不用填写,自己修改自己可以不用填 |

### 请求示例

```json
{
  "module": "user",
  "func": "UnBindPhone",
  "params": {
    "uid": "099fb3bf-ef22-4aaf-a2e0-22e427722c61"
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
  "error_message": "608d7ab1-e330-481e-8ddf-1aa83ddddcd7"
}
```

<a id="user-AddBindPhoneDev"></a>
## 添加绑定本账号手机号码的设备 (`AddBindPhoneDev`)

- 接口 ID：`user.AddBindPhoneDev`
- 模块：`user`
- 用途：添加绑定本账号手机号码的设备
- 原始位置：`source/账户(user).txt` 第 2049 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,AddBindPhoneDev |
| `module` | `string` | 模块名,user |
| `params` | `object` | 参数列表 |
| `params.code` | `string` | 验证码模板 |
| `params.key` | `string` | appkey，这个是由平台自己所拥有的，事先可先告知服务商，用于短信验证 |
| `params.sgid` | `string` | 需要合并的当前组织的分组id |

### 请求示例

```json
{
  "module": "user",
  "func": "AddBindPhoneDev",
  "params": {
    "code": "b99a7a4f-1eaf-4d4c-aa1e-d5451601ee82",
    "key": "c406dc14-1183-4e1c-a26b-1f6aa09f9e87",
    "sgid": "a6e150c7-1516-4306-a6d7-fb2ea8bd5550"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_item` | `array` | 失败的 |
| `fail_item.error_message` | `string` | 失败原因 |
| `fail_item.imei` | `number` | 用于展示的imei号 |
| `suc_item` | `array` | 成功的 |
| `suc_item.imei` | `number` | 用于展示的imei号 |
| `suc_item.simei` | `string` | 加密之后的设备 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "8e1cfc2a-68de-4d6c-a823-9eb0f59c5383",
  "fail_item": [
    {
      "error_message": "625606cc-24a5-41b0-8bb3-99feacc19098",
      "imei": 60096017
    },
    {
      "error_message": "ccf643b4-ce69-4bfd-b2cb-de24d3408fc6",
      "imei": 60096017
    },
    {
      "error_message": "a9ed092b-11f9-4277-978f-8faff9cfbc08",
      "imei": 60096017
    },
    {
      "error_message": "167c67b4-6764-43cb-8e95-c85c4d97a796",
      "imei": 60096017
    }
  ],
  "suc_item": [
    {
      "imei": 60096017,
      "simei": "e123f6c6-7049-4e95-b3d3-7b95952712a9"
    },
    {
      "imei": 60096017,
      "simei": "d24e93ed-a745-47d4-9bf7-f94237dc7fb7"
    },
    {
      "imei": 60096017,
      "simei": "a70f78fb-ea4b-4716-8360-fae1cec9258a"
    },
    {
      "imei": 60096017,
      "simei": "eca47808-c122-41df-b5f9-e16f3dc7d88e"
    }
  ]
}
```


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


# 设备报警 (Alarm)

- 模块标识：`alarm`
- 接口数量：17
- 原始文档：`source/报警(alarm).txt`
- 正式环境：`http://cciot.cc/mapi`
- 测试环境：`http://cs.iotiot.cc/mapi`
- 开发环境：`http://192.168.1.20/mapi`
- 模块负责人：hangjl,fushengyou,guohaoqin,fange

## 接口索引

| 接口 | 名称 | 请求字段数 | 返回字段数 |
| --- | --- | ---: | ---: |
| [`Get`](#alarm-Get) | 获取报警信息 | 21 | 64 |
| [`Del`](#alarm-Del) | 删除报警信息 | 8 | 2 |
| [`GetStat`](#alarm-GetStat) | 获取设备报警统计 | 14 | 44 |
| [`BatchDel`](#alarm-BatchDel) | 批量删除告警 | 15 | 2 |
| [`GetAlarmTypeList`](#alarm-GetAlarmTypeList) | 获取可以筛选的告警类型列表 | 6 | 8 |
| [`GetFamilyDaySummary`](#alarm-GetFamilyDaySummary) | 获取组织日统计 | 7 | 19 |
| [`GetFamilyAlarmByType`](#alarm-GetFamilyAlarmByType) | 通过报警类型统计 | 16 | 17 |
| [`GetSummaryByDate`](#alarm-GetSummaryByDate) | 通过日期获取统计 | 12 | 10 |
| [`GetAlarmTypeSummary`](#alarm-GetAlarmTypeSummary) | 告警类型统计 | 16 | 23 |
| [`AlarmAck`](#alarm-AlarmAck) | 告警确认 | 11 | 2 |
| [`GetAlarmStatusSummary`](#alarm-GetAlarmStatusSummary) | 获取告警确认状态统计 | 5 | 5 |
| [`GetAlarmAck`](#alarm-GetAlarmAck) | 获取告警确认详情 | 4 | 5 |
| [`GetLastAlarm`](#alarm-GetLastAlarm) | 获取最新的告警 | 11 | 11 |
| [`SetFamilyAlarm`](#alarm-SetFamilyAlarm) | 设置组织告警 | 34 | 2 |
| [`GetFamilyAlarm`](#alarm-GetFamilyAlarm) | 获取组织告警 | 4 | 32 |
| [`GetImeiDailyAlarmRank`](#alarm-GetImeiDailyAlarmRank) | 获取设备日告警数量排行 | 7 | 7 |
| [`SendCarPush`](#alarm-SendCarPush) | 车辆报警推送 | 25 | 2 |

## 接口详情

<a id="alarm-Get"></a>
## 获取报警信息 (`Get`)

- 接口 ID：`alarm.Get`
- 模块：`alarm`
- 用途：获取报警信息
- 原始位置：`source/报警(alarm).txt` 第 45 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1,报警消息最高不超过100；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Get |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.duration_begin` | `number` | 筛选超速报警持续时长大于该时间 |
| `params.end_time` | `number` | 结束时间，如果有，必须&gt;start_time，如果没有，默认从此刻时间拉取 |
| `params.get_child` | `boolean` | 是否包含下级 |
| `params.get_status_total` | `boolean` | 获取状态统计 |
| `params.imei` | `number` | 设备号，传了该参数，组织id必填 |
| `params.is_desc` | `boolean` | 按照时间的降序排序 |
| `params.is_summary` | `boolean` | 是否需要统计数据，默认不需要 |
| `params.last_fencename` | `string` | 上次请求返回的最后一个的围栏名称 |
| `params.last_imei` | `number` | 上次请求返回的最后一个的imei，首次请求可以不填 |
| `params.last_time` | `number` | 上次请求返回的最后一个的时间，首次请求可以不填 |
| `params.last_type` | `string` | 上次请求返回的最后一个的类型，首次请求可以不填<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |
| `params.limit_size` | `number` | 数据的限制条数，默认20条,最高不超过100 |
| `params.not_type` | `array` | 不需要的告警类型<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |
| `params.sfamilyid` | `string` | 需要拉取的组织id,如果设备登入，不填 |
| `params.simei` | `array` | 设备列表,选填，如果设备登入不填,注意只能筛选同一个sfamilyid下的设备 |
| `params.start_time` | `number` | 开始的时间戳，可以不用填写 |
| `params.status` | `string` | 告警状态<br>e_alarm_status_unack 待处理<br>e_alarm_status_ack 已处理 |
| `params.type` | `array` | 需要过滤的类型，可以不用填写<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "Get",
  "params": {
    "duration_begin": 1490899539,
    "end_time": 1490899539,
    "get_child": false,
    "get_status_total": false,
    "imei": 1490899539,
    "is_desc": false,
    "is_summary": false,
    "last_fencename": "f775f0aa-89f8-4272-bbb1-48af19f9b911",
    "last_imei": 1490899539,
    "last_time": 1490899539,
    "last_type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
    "limit_size": 1490899539,
    "not_type": [
      "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
    ],
    "sfamilyid": "44242ade-39de-4828-9e3d-a193a7b98ab8",
    "simei": [
      "bcbcfb8c-051b-4dde-ac86-cc1f25e6efd3"
    ],
    "start_time": 1490899539,
    "status": "e_alarm_status_unack|e_alarm_status_ack",
    "type": [
      "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
      "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
      "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `ack_total` | `number` | 已处理告警数量，get_status_total为true时返回 |
| `dev_ack_list` | `array` | 需要下发指令给设备确认的告警列表， 传入simei或者设备号登录时返回 |
| `dev_ack_list.alarm_id` | `number` | 告警类型对应的id |
| `dev_ack_list.alarm_name` | `string` | 报警名称 |
| `dev_ack_list.imei` | `number` | 设备号 |
| `dev_ack_list.time` | `number` | 触发报警的时间 |
| `dev_ack_list.type` | `string` | 报警类型<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` | 报警列表 |
| `items.acc_power` | `string` | 电源状态accele<br>e_acc_power_charge 充电<br>e_acc_power_interuupt 断电 |
| `items.acc_state` | `string` | acc状态<br>e_acc_close 关闭<br>e_acc_open 打开<br>e_acc_empty 为空 |
| `items.addr` | `string` | 高德解析地址，格式：lat,lon |
| `items.alarm_id` | `number` | 告警类型对应的id |
| `items.alarm_name` | `string` | 告警名称 |
| `items.allow_delete` | `boolean` | 允许删除 |
| `items.altitude` | `number` | 海拔 |
| `items.bd_satellite` | `number` | 北斗卫星个数 |
| `items.bind_familyname` | `string` | is_summary为true时返回，绑定终端组织名称 |
| `items.bind_phone` | `string` | is_summary为true时返回，绑定手机号 |
| `items.bind_sfamily` | `string` | is_summary为true时返回，绑定终端组织id |
| `items.bind_time` | `number` | 绑定时间 |
| `items.car_number` | `string` | 车牌号 |
| `items.check` | `number` | 该报警是否需要确认,1需要确认，2已确认 |
| `items.day_distance` | `number` | 日里程，精确到米，如“367” |
| `items.devspeed` | `number` | 速度 |
| `items.duration` | `number` | 超速告警持续时间 |
| `items.external_voltage` | `string` | 外电电压 |
| `items.extra_info` | `object` | 附加信息 |
| `items.extra_info.contact_name` | `string` | 联系人姓名 |
| `items.extra_info.contact_phone` | `string` | 联系人电话 |
| `items.extra_info.contact_remark` | `string` | 联系人备注 |
| `items.familyname` | `string` | is_summary为true时返回，组织名称 |
| `items.fencename` | `string` | 围栏名称 |
| `items.finish_time` | `number` | 超速告警持续结束时间 |
| `items.frame_num` | `string` | 车架号, 使用setdetail设置车架号需添加additional字段，additional为字典类型 |
| `items.gen_family_name` | `string` | 告警产生时的组织名称 |
| `items.gen_sfamilyid` | `string` | 告警产生时的组织的id |
| `items.glonass_satellite` | `number` | 格洛纳斯卫星个数 |
| `items.gps_satellite` | `number` | gps卫星个数 |
| `items.groupname` | `string` | is_summary为true时返回，分组名称 |
| `items.imei` | `number` | 发生报警的imei号,仅用作展示，后面的请求需要用到imei的请用simei |
| `items.is_defend` | `boolean` | 是否设防 |
| `items.loc_type` | `number` | 定位类型 |
| `items.oil_state` | `string` | 油电状态<br>e_oil_normal 油电正常<br>e_oil_off 油电断开 |
| `items.power` | `number` | 电量 |
| `items.sackid` | `string` | 告警确认id |
| `items.satellite_type` | `array` | 卫星定位类型<br>e_type_gps gps卫星<br>e_type_beidou 北斗卫星<br>e_type_glonass Glonass卫星 |
| `items.sfamily` | `string` | is_summary为true时返回，组织id |
| `items.sgid` | `string` | is_summary为true时返回，分组id |
| `items.signa_type` | `number` | 信号类型,2G信号0，4G信号1 |
| `items.signa_val` | `number` | 信号强度,卫星数量 |
| `items.signal_rate` | `number` | 信号强度百分比值 |
| `items.simei` | `string` | 发生报警的imei号,后续的请求，可能会用到这个simei, 该设备号仅可用于本模块的删除相关接口 |
| `items.status` | `string` | 告警状态<br>e_alarm_status_unack 待处理<br>e_alarm_status_ack 已处理 |
| `items.time` | `number` | 发生报警的时间戳 |
| `items.type` | `string` | 报警类型<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |
| `items.version` | `string` | is_summary为true时返回，设备型号 |
| `last_fencename` | `string` | 上次请求返回的最后一个的围栏名称 |
| `last_imei` | `number` | 本次请求返回的最后一个的imei，注意可能与items返回的最后一条数据不一样 |
| `last_time` | `number` | 本次请求返回的最后一个的时间，注意可能与items返回的最后一条数据不一样 |
| `last_type` | `string` | 本次请求返回的最后一个的类型，注意可能与items返回的最后一条数据不一样<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |
| `total` | `number` | 总数,只有在不翻页的时候返回 |
| `unack_total` | `number` | 未处理告警数量，get_status_total为true时返回 |

### 返回示例

```json
{
  "errcode": 0,
  "ack_total": 1225512833,
  "dev_ack_list": [
    {
      "alarm_id": 1225512833,
      "alarm_name": "10db4d31-42a8-4a64-a919-d9d3f5ff523b",
      "imei": 1225512833,
      "time": 1225512833,
      "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
    },
    {
      "alarm_id": 1225512833,
      "alarm_name": "3b953dcf-1fb8-4454-b50d-a0c46c4d03f6",
      "imei": 1225512833,
      "time": 1225512833,
      "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
    },
    {
      "alarm_id": 1225512833,
      "alarm_name": "26bdd1fd-4899-4e0b-9c3d-0f0c3de06d6a",
      "imei": 1225512833,
      "time": 1225512833,
      "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
    },
    {
      "alarm_id": 1225512833,
      "alarm_name": "2d019a27-959a-468c-a7e1-232f8aedbcee",
      "imei": 1225512833,
      "time": 1225512833,
      "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
    }
  ],
  "error_message": "6e952178-3ae0-4676-a3bf-e889b2cfeeb9",
  "items": [
    {
      "acc_power": "e_acc_power_charge|e_acc_power_interuupt",
      "acc_state": "e_acc_close|e_acc_open|e_acc_empty",
      "addr": "c5d43c87-5289-42db-872f-307bf3df9359",
      "alarm_id": 1225512833,
      "alarm_name": "b5cd4490-e05f-4932-8d9d-2650a9fc50bf",
      "allow_delete": false,
      "altitude": 1225512833,
      "bd_satellite": 1225512833,
      "bind_familyname": "66879b6b-e61c-49db-85c0-eac138ec2792",
      "bind_phone": "7f37e3f3-6bf2-4d24-b79c-09640e16817b",
      "bind_sfamily": "dd8acae2-82ed-4757-8c71-cdd9e9a73727",
      "bind_time": 1225512833,
      "car_number": "fcdeb9b1-fe19-42a1-9fbe-81b762f41c23",
      "check": 1225512833,
      "day_distance": 1225512833,
      "devspeed": 1225512833,
      "duration": 1225512833,
      "external_voltage": "a61f8afd-e57b-42a5-a5e7-c0e073cc2258",
      "extra_info": {
        "contact_name": "a172a610-07eb-404b-a97f-d88cce2d3a39",
        "contact_phone": "3e1ebd76-d6ad-42ca-84ce-22f67d11ee6e",
        "contact_remark": "9028176a-1ba6-4ec3-a1fe-9dd12dd5b4fb"
      },
      "familyname": "cfe3b216-d7e8-400d-8511-3b63d9a7c3a3",
      "fencename": "75dde394-a513-42dd-b82d-cae4c6982d20",
      "finish_time": 1225512833,
      "frame_num": "41b02090-dbc6-45b6-8375-315944cfab91",
      "gen_family_name": "4324f2b4-50b0-43e7-976b-26a2be835557",
      "gen_sfamilyid": "8f9eb957-0132-40ee-bd07-3400f56b6076",
      "glonass_satellite": 1225512833,
      "gps_satellite": 1225512833,
      "groupname": "c4aa8e39-5217-49af-8d5f-4607f712b5fa",
      "imei": 1225512833,
      "is_defend": false,
      "loc_type": 1225512833,
      "oil_state": "e_oil_normal|e_oil_off",
      "power": 1225512833,
      "sackid": "fe9bc596-601c-4f07-863c-a46d6fb91260",
      "satellite_type": [
        "e_type_gps|e_type_beidou|e_type_glonass",
        "e_type_gps|e_type_beidou|e_type_glonass",
        "e_type_gps|e_type_beidou|e_type_glonass"
      ],
      "sfamily": "3810f876-9041-4322-9386-b181ee85544d",
      "sgid": "ef8ada2a-2a4c-44aa-9b57-dead545280c9",
      "signa_type": 1225512833,
      "signa_val": 1225512833,
      "signal_rate": 1225512833,
      "simei": "fc251874-8ace-4aad-810d-f993f0feb45c",
      "status": "e_alarm_status_unack|e_alarm_status_ack",
      "time": 1225512833,
      "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
      "version": "3b125b07-5f04-41c6-a979-8cdd518db833"
    },
    {
      "acc_power": "e_acc_power_charge|e_acc_power_interuupt",
      "acc_state": "e_acc_close|e_acc_open|e_acc_empty",
      "addr": "a22d4d28-56d9-4655-89b9-d89d5090e1fb",
      "alarm_id": 1225512833,
      "alarm_name": "571fc521-12e2-4f50-8981-1a5cd1ce706d",
      "allow_delete": false,
      "altitude": 1225512833,
      "bd_satellite": 1225512833,
      "bind_familyname": "814527fd-badc-4a07-8a0d-1a8d258a705d",
      "bind_phone": "59816bd1-3609-4189-a639-b312fc0d6db6",
      "bind_sfamily": "fd95e83f-4c5c-4121-9459-987ad889cfc1",
      "bind_time": 1225512833,
      "car_number": "03b177a3-8b78-4e62-be34-dc4f482b02d6",
      "check": 1225512833,
      "day_distance": 1225512833,
      "devspeed": 1225512833,
      "duration": 1225512833,
      "external_voltage": "0184d4c2-ca99-4bd5-a6c7-8ef14b292a98",
      "extra_info": {
        "contact_name": "9206f329-ecd1-4386-a618-d36122980a9c",
        "contact_phone": "f9e6fa83-186f-46c4-99cf-af5c862ca218",
        "contact_remark": "35cf9c89-4034-4375-be4e-f1113c91ba7f"
      },
      "familyname": "6838bfec-8ad8-4be8-81b2-c86326dd3737",
      "fencename": "17a79347-32d5-49e6-a831-966bc0011ea9",
      "finish_time": 1225512833,
      "frame_num": "69a3b5b2-6711-44d2-ac17-c7aa42114274",
      "gen_family_name": "a65c0cca-275b-427c-b62f-63e0dc90e1f3",
      "gen_sfamilyid": "7bc0d9a6-8fe1-48d1-befb-20acc6df419c",
      "glonass_satellite": 1225512833,
      "gps_satellite": 1225512833,
      "groupname": "31b3de3b-73b1-4a99-865f-ef4d7763bafc",
      "imei": 1225512833,
      "is_defend": false,
      "loc_type": 1225512833,
      "oil_state": "e_oil_normal|e_oil_off",
      "power": 1225512833,
      "sackid": "34a9e8a1-39d5-46f8-aace-500a7d1e3b27",
      "satellite_type": [
        "e_type_gps|e_type_beidou|e_type_glonass",
        "e_type_gps|e_type_beidou|e_type_glonass"
      ],
      "sfamily": "06c59363-4767-4cca-aa2a-cb77590b412c",
      "sgid": "c07bb97b-8a24-4c38-8860-7ba265b8211c",
      "signa_type": 1225512833,
      "signa_val": 1225512833,
      "signal_rate": 1225512833,
      "simei": "1e022867-45c5-4fbc-a084-62982d72c99b",
      "status": "e_alarm_status_unack|e_alarm_status_ack",
      "time": 1225512833,
      "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
      "version": "0c7f747e-1e1c-4431-96ab-3b8a079890b7"
    },
    {
      "acc_power": "e_acc_power_charge|e_acc_power_interuupt",
      "acc_state": "e_acc_close|e_acc_open|e_acc_empty",
      "addr": "dc549541-8312-4af5-8006-7ca292b2c26a",
      "alarm_id": 1225512833,
      "alarm_name": "a134f0b7-324a-4948-8d86-a768a73c1a42",
      "allow_delete": false,
      "altitude": 1225512833,
      "bd_satellite": 1225512833,
      "bind_familyname": "1af69a00-5129-416c-aa29-d40211aa7938",
      "bind_phone": "c7d2e577-c31b-42ba-873c-a9dadcb9fda0",
      "bind_sfamily": "9ddd04e8-bb7e-4fe1-a58b-84229ed97d7d",
      "bind_time": 1225512833,
      "car_number": "703d406d-646e-4fcb-a9b3-2d5fa898fbfa",
      "check": 1225512833,
      "day_distance": 1225512833,
      "devspeed": 1225512833,
      "duration": 1225512833,
      "external_voltage": "3011e413-c2af-4337-8b1d-6ffe2521ae28",
      "extra_info": {
        "contact_name": "7627bb7d-6baf-4530-ac7c-268163426dfd",
        "contact_phone": "327cd6d5-9d4c-4b99-b66c-0d19dcf76b8d",
        "contact_remark": "08b98136-89d9-4819-bc70-b8226727bf1d"
      },
      "familyname": "fa4dd58e-c568-41f9-906c-49e502140f70",
      "fencename": "6c94e67e-1e26-473c-ae88-842259de9e6d",
      "finish_time": 1225512833,
      "frame_num": "0fea28bb-0cdc-4372-9bb5-e62c9a4ddb56",
      "gen_family_name": "8db75193-c649-40cd-a9fe-b4133ec7355c",
      "gen_sfamilyid": "22170b4c-8dcf-416d-870b-efa8c1f9c43b",
      "glonass_satellite": 1225512833,
      "gps_satellite": 1225512833,
      "groupname": "4271a127-671d-460d-b9e0-c9384c9d83e7",
      "imei": 1225512833,
      "is_defend": false,
      "loc_type": 1225512833,
      "oil_state": "e_oil_normal|e_oil_off",
      "power": 1225512833,
      "sackid": "3510f94e-820e-4af4-b7ba-1415e398857e",
      "satellite_type": [
        "e_type_gps|e_type_beidou|e_type_glonass"
      ],
      "sfamily": "c579ce5f-a77c-4df8-b7a7-afeedc408a82",
      "sgid": "fbbebbdc-39f5-4850-a2a4-a2f17c1e1b7d",
      "signa_type": 1225512833,
      "signa_val": 1225512833,
      "signal_rate": 1225512833,
      "simei": "239d0193-f40e-41f2-9af1-049555dd5b36",
      "status": "e_alarm_status_unack|e_alarm_status_ack",
      "time": 1225512833,
      "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
      "version": "74defe9f-8fc8-4b33-b027-1c0442e0fabb"
    }
  ],
  "last_fencename": "54295078-813c-4c55-8f35-8b19e2d62de3",
  "last_imei": 1225512833,
  "last_time": 1225512833,
  "last_type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
  "total": 1225512833,
  "unack_total": 1225512833
}
```

<a id="alarm-Del"></a>
## 删除报警信息 (`Del`)

- 接口 ID：`alarm.Del`
- 模块：`alarm`
- 用途：删除报警信息
- 原始位置：`source/报警(alarm).txt` 第 1039 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，需要删除报警权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Del |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.fence` | `string` | 需要删除的日志的围栏名称，必填 |
| `params.gen_sfamilyid` | `string` | 该告警所属的组织id |
| `params.simei` | `string` | 如果设备登入，不填 |
| `params.time` | `number` | 需要删除的时间 |
| `params.type` | `string` | 报警类型,注意，必填<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "Del",
  "params": {
    "fence": "11c3e2e3-5042-4563-bd8f-654d6629c535",
    "gen_sfamilyid": "50d6f053-f8e2-4d58-85d0-e4534a55b481",
    "simei": "69fa5643-4261-4912-8d7b-51643dbe338c",
    "time": 253312338,
    "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
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
  "error_message": "dc9d0109-ff2e-4388-85a8-4309a327fd4c"
}
```

<a id="alarm-GetStat"></a>
## 获取设备报警统计 (`GetStat`)

- 接口 ID：`alarm.GetStat`
- 模块：`alarm`
- 用途：获取设备报警统计
- 原始位置：`source/报警(alarm).txt` 第 1182 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1,报警消息最高不超过100；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetStat |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.is_get_child` | `boolean` | 是否包含下级的组织数据，默认包含 |
| `params.last_sgid` | `string` | 上一页获取返回最后一条数据的分组id |
| `params.last_simei` | `string` | 上一页获取返回最后一条数据的设备id |
| `params.limit_size` | `number` | 数据的限制条数，默认20条,最高不超过100 |
| `params.sfamily` | `string` | 需要拉取的组织id,如果设备登入，不填,字段sgid,simei,sfamilyid三选一 |
| `params.sgid` | `string` | 需要获取统计的车组,如果设备登入，不填,字段sgid,simei,sfamilyid三选一 |
| `params.simei` | `array` | 统计的设备id，如果设备登入，不填,必须是同个组织下的设备.字段sgid,simei,sfamilyid三选一 |
| `params.time_begin` | `number` | 起始时间戳,为0或者不传,即以服务器的起始时间算起 |
| `params.time_end` | `number` | 结束时间戳,为0或者不传,即以服务器的当前时间算起 |
| `params.type` | `array` | 类型,没有表示全部<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |
| `params.version` | `array` | 按设备型号过滤, 使用sfamily时生效 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "GetStat",
  "params": {
    "is_get_child": true,
    "last_sgid": "27abba07-f401-4ed9-88f0-191d7970ebd9",
    "last_simei": "0beb5b08-b156-4962-b0ff-f63a5d988d88",
    "limit_size": 1205193600,
    "sfamily": "66014822-145c-49f8-9de0-d87148d50cd5",
    "sgid": "f9e2272b-a3d9-4479-b68e-694b0caf32ca",
    "simei": [
      "cc3bae02-e972-41d7-ad89-c0486d62424c",
      "c78db4fa-9bb2-4eb1-b11d-6f4f64ad5d31"
    ],
    "time_begin": 1205193600,
    "time_end": 1205193600,
    "type": [
      "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
      "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
      "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
    ],
    "version": [
      "ace8c8df-50a4-4dc0-a23d-dc856e7acc58"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` | 报警列表 |
| `items.acc_off` | `number` | ACC关闭报警 |
| `items.bind_familyname` | `string` | 绑定终端组织名称 |
| `items.bind_phone` | `string` | 绑定手机号 |
| `items.bind_sfamily` | `string` | 绑定终端组织id |
| `items.bind_time` | `number` | 绑定时间 |
| `items.car_number` | `string` | 车牌号 |
| `items.close_down` | `number` | 关机报警 |
| `items.dismantle` | `number` | 防拆报警 |
| `items.displacement` | `number` | 位移报警 |
| `items.familyname` | `string` | 组织名称 |
| `items.frame_num` | `string` | 车架号, 使用setdetail设置车架号需添加additional字段，additional为字典类型 |
| `items.groupname` | `string` | 分组名称 |
| `items.illegal_open` | `number` | 非法开门 |
| `items.illegal_start` | `number` | 非法启动 |
| `items.imei` | `number` | 设备号 |
| `items.in_fence` | `number` | 进围栏 |
| `items.light_off` | `number` | 设备脱落报警 |
| `items.line_dismantle` | `number` | 有线的断电报警,防拆报警 |
| `items.lowpower` | `number` | 设备低电量报警 |
| `items.off_line` | `number` | 离线报警 |
| `items.out_fence` | `number` | 出围栏 |
| `items.power_cut_off` | `number` | 断油断电报警 |
| `items.pseudo_base_station` | `number` | 伪基站报警 |
| `items.recovery_light` | `number` | 恢复光感脱落,已合并至解除防拆报警 |
| `items.remove_lowpower` | `number` | 解除低电量报警 |
| `items.remove_out` | `number` | 解除防拆报警 |
| `items.satellite` | `number` | 卫星盲区报警 |
| `items.sensitivity` | `number` | 灵敏度报警 |
| `items.sfamily` | `string` | 组织id |
| `items.sgid` | `string` | 分组id |
| `items.shake` | `number` | 震动报警 |
| `items.simei` | `string` | 设备id |
| `items.sos` | `number` | SOS报警 |
| `items.speed` | `number` | 超速报警 |
| `items.speeding_end` | `number` | 解除超速 |
| `items.start_dev` | `number` | 终端启动报警 |
| `items.start_up` | `number` | 开机报警 |
| `items.stop_dev` | `number` | 停车报警 |
| `items.total` | `number` | 震动报警 |
| `items.version` | `string` | 设备型号 |
| `total` | `number` | 总数,只有在不翻页的时候返回 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "61d11ff0-eb50-46ea-aa3f-ec3062b5e802",
  "items": [
    {
      "acc_off": 1384814228,
      "bind_familyname": "a40292e7-7c04-4476-84ec-414d2eabf244",
      "bind_phone": "742c0fbc-516e-4b3c-8aed-04070725a31b",
      "bind_sfamily": "b9c5a41b-5ba5-41d1-b18a-d8ed7924f484",
      "bind_time": 1384814228,
      "car_number": "304225e3-4c7e-40f2-adcb-57b9dc58297b",
      "close_down": 1384814228,
      "dismantle": 1384814228,
      "displacement": 1384814228,
      "familyname": "eb68e3f1-a577-4eb5-965c-a3876be6a752",
      "frame_num": "bcfc61c3-13ed-44b8-b251-f80639bea2a8",
      "groupname": "d8a930a7-becf-4506-ae95-d4d50b7fb6fd",
      "illegal_open": 1384814228,
      "illegal_start": 1384814228,
      "imei": 1384814228,
      "in_fence": 1384814228,
      "light_off": 1384814228,
      "line_dismantle": 1384814228,
      "lowpower": 1384814228,
      "off_line": 1384814228,
      "out_fence": 1384814228,
      "power_cut_off": 1384814228,
      "pseudo_base_station": 1384814228,
      "recovery_light": 1384814228,
      "remove_lowpower": 1384814228,
      "remove_out": 1384814228,
      "satellite": 1384814228,
      "sensitivity": 1384814228,
      "sfamily": "d1146282-101a-4804-b605-7baf5457ff37",
      "sgid": "bf43348b-58c2-4dcb-98a4-45b5d32575f3",
      "shake": 1384814228,
      "simei": "02b296d2-854c-498e-85d5-fa24993dea9e",
      "sos": 1384814228,
      "speed": 1384814228,
      "speeding_end": 1384814228,
      "start_dev": 1384814228,
      "start_up": 1384814228,
      "stop_dev": 1384814228,
      "total": 1384814228,
      "version": "55d5ab64-e95c-48d1-bbcf-345cf6c456e8"
    },
    {
      "acc_off": 1384814228,
      "bind_familyname": "1eb1094c-c056-4db8-b55b-e7104b9a5547",
      "bind_phone": "02bfc866-5c0a-4a48-8149-856f1b00582b",
      "bind_sfamily": "55e94c79-3fd2-4f32-b8fd-ecf7b32e8b4a",
      "bind_time": 1384814228,
      "car_number": "cf746435-1653-434f-b589-1688b810646a",
      "close_down": 1384814228,
      "dismantle": 1384814228,
      "displacement": 1384814228,
      "familyname": "4e3d9f76-c38c-4dcd-9259-5c31c570458c",
      "frame_num": "906605df-b6a5-42e4-8ff6-00de604a5d68",
      "groupname": "86acff9e-9346-4a30-ad96-e6f0bf5c51ce",
      "illegal_open": 1384814228,
      "illegal_start": 1384814228,
      "imei": 1384814228,
      "in_fence": 1384814228,
      "light_off": 1384814228,
      "line_dismantle": 1384814228,
      "lowpower": 1384814228,
      "off_line": 1384814228,
      "out_fence": 1384814228,
      "power_cut_off": 1384814228,
      "pseudo_base_station": 1384814228,
      "recovery_light": 1384814228,
      "remove_lowpower": 1384814228,
      "remove_out": 1384814228,
      "satellite": 1384814228,
      "sensitivity": 1384814228,
      "sfamily": "988bfa72-aae7-4a8c-8ff0-e00f186b3d8f",
      "sgid": "4a5f95d3-e55d-4611-82bc-59d6f6ce3360",
      "shake": 1384814228,
      "simei": "3fe9aada-7d58-4164-bb26-cb3ea2beb3c8",
      "sos": 1384814228,
      "speed": 1384814228,
      "speeding_end": 1384814228,
      "start_dev": 1384814228,
      "start_up": 1384814228,
      "stop_dev": 1384814228,
      "total": 1384814228,
      "version": "58eb795f-6517-41da-ac3f-ae87ceab8750"
    }
  ],
  "total": 1384814228
}
```

<a id="alarm-BatchDel"></a>
## 批量删除告警 (`BatchDel`)

- 接口 ID：`alarm.BatchDel`
- 模块：`alarm`
- 用途：批量删除告警
- 原始位置：`source/报警(alarm).txt` 第 1523 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,BatchDel |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.begin_time` | `number` | 开始的时间戳(1635302765)，填写了simei有效，且必须为当天00:00:00点 |
| `params.end_time` | `number` | 结束时间戳(1635302765)，填写了simei有效，且必须为当天23:59:59点 |
| `params.include_child` | `boolean` | 结合sfamily使用，是否删除包括下级组织的告警 |
| `params.items` | `array` | 需要删除的告警列表,最多1000，参数items，sfamily,simei三选一填写，设备号登入可以不用 |
| `params.items.fencename` | `string` | 触发报警的围栏名称 |
| `params.items.gen_sfamilyid` | `string` | 该告警所属的组织id |
| `params.items.simei` | `string` | 设备id,设备号登录可以不用传 |
| `params.items.time` | `number` | 告警时间 |
| `params.items.type` | `string` | 报警类型<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |
| `params.sfamily` | `string` | 需要删除的全部的组织告警，参数items，sfamily,simei三选一填写，设备号登入可以不用 |
| `params.simei` | `array` | 需要删除指定设备的全部告警，注意，这些设备号必须是同一个组织下.参数items，sfamily,simei三选一填写，设备号登入可以不用 |
| `params.type` | `array` | 如果填写了字段sfamily或simei，那么可以筛选其中几种类型批量删除<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "BatchDel",
  "params": {
    "begin_time": 194102610,
    "end_time": 194102610,
    "include_child": true,
    "items": [
      {
        "fencename": "0fd66d5f-6889-48d6-8c54-4aa1cfd0298a",
        "gen_sfamilyid": "9bbd0182-96a3-4ed2-9ed0-4a949312c3b9",
        "simei": "f2f46a4f-a2fd-48a4-aad4-5ac816918ce0",
        "time": 194102610,
        "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
      },
      {
        "fencename": "8eb16f27-920e-4a3c-8337-b645432e4efa",
        "gen_sfamilyid": "34ac46a6-5d25-4442-be7d-4b882c5deedc",
        "simei": "4c28c67c-f2cd-4fe5-b825-56a73adb4583",
        "time": 194102610,
        "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
      }
    ],
    "sfamily": "62216902-3877-49f3-a9f8-1d0a4f30752f",
    "simei": [
      "a9010aef-81b7-4c7d-9835-8d6f44b9e903",
      "20e9fa3b-e9a4-474f-8674-517d27c5074a",
      "192f2169-ff7b-4208-ae29-351ffc258bff",
      "22e29c76-ce3f-4572-b7dd-d3c440a304d5"
    ],
    "type": [
      "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
      "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
      "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
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
  "error_message": "abbb9f77-a169-47bd-91cc-c7118c908e16"
}
```

<a id="alarm-GetAlarmTypeList"></a>
## 获取可以筛选的告警类型列表 (`GetAlarmTypeList`)

- 接口 ID：`alarm.GetAlarmTypeList`
- 模块：`alarm`
- 用途：获取可以筛选的告警类型列表
- 原始位置：`source/报警(alarm).txt` 第 1796 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetAlarmTypeList |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.alarm_class` | `string` | 告警分类, 默认获取设备相关报警<br>e_alarm_dev  获取设备相关告警<br>e_alarm_car  获取车辆、学生等业务相关告警<br>e_alarm_all  获取全部告警 |
| `params.check_func` | `boolean` | 用于功能配置表 |
| `params.simei` | `string` | 需要查询的设备对应的告警列表 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "GetAlarmTypeList",
  "params": {
    "alarm_class": "e_alarm_dev|e_alarm_car|e_alarm_all",
    "check_func": false,
    "simei": "60dc31fd-854f-4326-a112-eaadced88696"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` | 可以支持的全部的告警列表 |
| `items.alarm_id` | `number` | 告警id |
| `items.alarm_name` | `string` | 名称 |
| `items.alarm_type` | `string` | 类型<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |
| `items.func_desc` | `string` | 告警格式 |
| `items.weight` | `number` |  |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "f81169c5-ee2a-4015-822a-a293caeb6bdd",
  "items": [
    {
      "alarm_id": 1643138572,
      "alarm_name": "f83f11f0-42b8-4f67-bce4-5219c977f9b9",
      "alarm_type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
      "func_desc": "5e82269d-efc0-45fc-adf5-3f407203e3ba",
      "weight": 1643138572
    },
    {
      "alarm_id": 1643138572,
      "alarm_name": "f40bf1dc-52bd-44bd-8a3d-5ce15b277c17",
      "alarm_type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
      "func_desc": "c4426f49-a837-45e1-817d-198312d0cb22",
      "weight": 1643138572
    },
    {
      "alarm_id": 1643138572,
      "alarm_name": "86d7328e-4e92-458d-8353-c8bbbff35914",
      "alarm_type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
      "func_desc": "e597279b-68bf-4ad5-897c-00d36c74571e",
      "weight": 1643138572
    }
  ]
}
```

<a id="alarm-GetFamilyDaySummary"></a>
## 获取组织日统计 (`GetFamilyDaySummary`)

- 接口 ID：`alarm.GetFamilyDaySummary`
- 模块：`alarm`
- 用途：获取组织日统计
- 原始位置：`source/报警(alarm).txt` 第 1968 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetFamilyDaySummary |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.date` | `number` | 日期,格式20220531,不填写就是当日 |
| `params.get_car_stat` | `boolean` | 是否获取车辆相关报警 |
| `params.get_child` | `boolean` | 是否包含下级，默认false |
| `params.sfamily` | `string` | 需要拉取的组织id,如果设备登入，不填 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "GetFamilyDaySummary",
  "params": {
    "date": 746512768,
    "get_car_stat": true,
    "get_child": true,
    "sfamily": "d5d2ca71-33bb-4c83-a62d-a4c02256d17c"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `acc_off` | `number` | acc关闭报警 |
| `close_down` | `number` | 关机报警 |
| `dismantle` | `number` | 防拆报警 |
| `distance` | `number` | 分离报警 |
| `dual_offline` | `number` | 双离线报警 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `gather` | `number` | 聚集报警 |
| `in_fence` | `number` | 进围栏报警 |
| `line_dismantle` | `number` | 断电报警(有线) |
| `loan_expired` | `number` | 到期报警 |
| `lowpower` | `number` | 设备低电量报警 |
| `off_line` | `number` | 离线报警 |
| `out_fence` | `number` | 出围栏报警 |
| `shake` | `number` | 震动报警 |
| `sos` | `number` | SOS报警 |
| `speed` | `number` | 超速报警 |
| `stop_dev` | `number` | 停车报警 |
| `total` | `number` | 总数 |

### 返回示例

```json
{
  "errcode": 0,
  "acc_off": 615268877,
  "close_down": 615268877,
  "dismantle": 615268877,
  "distance": 615268877,
  "dual_offline": 615268877,
  "error_message": "6bcf76c2-b604-44cd-94bc-dd28ed08a6b0",
  "gather": 615268877,
  "in_fence": 615268877,
  "line_dismantle": 615268877,
  "loan_expired": 615268877,
  "lowpower": 615268877,
  "off_line": 615268877,
  "out_fence": 615268877,
  "shake": 615268877,
  "sos": 615268877,
  "speed": 615268877,
  "stop_dev": 615268877,
  "total": 615268877
}
```

<a id="alarm-GetFamilyAlarmByType"></a>
## 通过报警类型统计 (`GetFamilyAlarmByType`)

- 接口 ID：`alarm.GetFamilyAlarmByType`
- 模块：`alarm`
- 用途：通过报警类型统计
- 原始位置：`source/报警(alarm).txt` 第 2063 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetFamilyAlarmByType |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.car_number` | `string` | 车牌号 |
| `params.car_type` | `string` | 车型 |
| `params.get_child` | `boolean` | 是否包含下级，默认不包含 |
| `params.last_imei` | `number` | 分页参数，上一次请求返回的最后一条imei |
| `params.last_time` | `number` | 分页参数，上一次请求返回的最后一条time |
| `params.last_type` | `string` | 分页参数，上一次请求返回的最后一条type<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |
| `params.limit_size` | `number` | 请求条数，最大100，默认100 |
| `params.sfamilyid` | `string` | 要查询的组织，组织、分组、设备号 三选一 |
| `params.sgid` | `string` | 要查询的分组，组织、分组、设备号 三选一 |
| `params.simei` | `string` | 要查询的设备号,组织、分组、设备号 三选一 |
| `params.time_begin` | `number` | 报警开始时间 |
| `params.time_end` | `number` | 报警结束时间 |
| `params.type` | `string` | 告警类型,目前只支持双离线<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "GetFamilyAlarmByType",
  "params": {
    "car_number": "66822f84-e8ea-4149-a796-65c55bf07542",
    "car_type": "fbc55fe0-43d9-4da5-98a9-724aad11852f",
    "get_child": false,
    "last_imei": 1998520195,
    "last_time": 1998520195,
    "last_type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
    "limit_size": 1998520195,
    "sfamilyid": "65eb49b1-a012-443b-bf84-014004e74ba0",
    "sgid": "6abdee5d-128b-4212-b12f-8a91e1e53257",
    "simei": "fd1bd8d3-0cbf-48ed-88b1-f36909ec0d38",
    "time_begin": 1998520195,
    "time_end": 1998520195,
    "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.addr` | `string` | 报警位置 |
| `items.car_number` | `string` | 车牌号 |
| `items.car_type` | `string` | 车型 |
| `items.detail` | `array` |  |
| `items.detail.imei` | `number` | 设备号 |
| `items.detail.type` | `number` | 设备类型，0有线款，1无线款 |
| `items.family_name` | `string` | 组织名称 |
| `items.group_name` | `string` | 所属分组 |
| `items.imei` | `number` | 触发报警的设备号 |
| `items.last_pos` | `string` | 最后定位信息 |
| `items.time` | `number` | 报警时间 |
| `items.wire_imei` | `number` | 接线款设备号 |
| `items.wireless_imei` | `number` | 无线款设备号 |
| `total` | `number` | 总条数，第一次请求返回 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "93a1191f-96cc-4ae8-90ad-5afadf69650f",
  "items": [
    {
      "addr": "73e2fef1-f0cd-458c-9009-bcb372c13e1f",
      "car_number": "0c9874ab-ed99-4d85-8399-62d4f400c84d",
      "car_type": "357f95fb-d9f0-46a6-9b43-f13ab79e23b3",
      "detail": [
        {
          "imei": 1905900858,
          "type": 1905900858
        },
        {
          "imei": 1905900858,
          "type": 1905900858
        },
        {
          "imei": 1905900858,
          "type": 1905900858
        }
      ],
      "family_name": "24cd0876-9042-4f3e-a24e-d25516bf3468",
      "group_name": "f6531572-74d5-4069-b3f7-98371dff5f0b",
      "imei": 1905900858,
      "last_pos": "04e0837f-4130-4c48-aa89-92aba6d48e40",
      "time": 1905900858,
      "wire_imei": 1905900858,
      "wireless_imei": 1905900858
    },
    {
      "addr": "34f52b4b-95cf-4acf-93ea-841e49a49761",
      "car_number": "0d8f2bc8-54d3-4415-9b17-36857099ddcb",
      "car_type": "049be3af-e5f9-4c98-8502-511c0c0b0e5b",
      "detail": [
        {
          "imei": 1905900858,
          "type": 1905900858
        }
      ],
      "family_name": "61bcf550-a49c-4d9a-ad56-6f1c18733bec",
      "group_name": "8860f675-c0c6-479b-a6fc-d9af4047d4d0",
      "imei": 1905900858,
      "last_pos": "7afb78a8-3725-4580-8ba9-d6b8ed14af53",
      "time": 1905900858,
      "wire_imei": 1905900858,
      "wireless_imei": 1905900858
    }
  ],
  "total": 1905900858
}
```

<a id="alarm-GetSummaryByDate"></a>
## 通过日期获取统计 (`GetSummaryByDate`)

- 接口 ID：`alarm.GetSummaryByDate`
- 模块：`alarm`
- 用途：通过日期获取统计
- 原始位置：`source/报警(alarm).txt` 第 2397 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetSummaryByDate |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.date` | `array` | 日统计(20231019) |
| `params.date_begin` | `number` | 报警开始时间(20231018) |
| `params.date_end` | `number` | 报警结束时间(20231019) |
| `params.get_child` | `boolean` | 是否包含下级，默认false |
| `params.month` | `array` | 月统计(202310) |
| `params.month_begin` | `number` | 月统计(202310) |
| `params.month_end` | `number` | 月统计(202310) |
| `params.sfamilyid` | `string` | 获取指定组织，必填 |
| `params.type` | `string` | 报警类型<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "GetSummaryByDate",
  "params": {
    "date": [
      998217650
    ],
    "date_begin": 998217650,
    "date_end": 998217650,
    "get_child": true,
    "month": [
      998217650,
      998217650,
      998217650,
      998217650
    ],
    "month_begin": 998217650,
    "month_end": 998217650,
    "sfamilyid": "00aa3f2a-848b-4574-9e9a-5540505d4faa",
    "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `days` | `array` | 日统计 |
| `days.date` | `number` | 报警日期 |
| `days.off_line` | `number` | 离线报警 |
| `days.total_imei` | `number` | 报警设备总数 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `months` | `array` | 月统计 |
| `months.date` | `number` | 报警日期 |
| `months.off_line` | `number` | 离线报警 |
| `months.total_imei` | `number` | 报警设备总数 |

### 返回示例

```json
{
  "errcode": 0,
  "days": [
    {
      "date": 570719448,
      "off_line": 570719448,
      "total_imei": 570719448
    },
    {
      "date": 570719448,
      "off_line": 570719448,
      "total_imei": 570719448
    },
    {
      "date": 570719448,
      "off_line": 570719448,
      "total_imei": 570719448
    },
    {
      "date": 570719448,
      "off_line": 570719448,
      "total_imei": 570719448
    }
  ],
  "error_message": "5824142d-df71-4773-b294-f7ff17bdd419",
  "months": [
    {
      "date": 570719448,
      "off_line": 570719448,
      "total_imei": 570719448
    },
    {
      "date": 570719448,
      "off_line": 570719448,
      "total_imei": 570719448
    },
    {
      "date": 570719448,
      "off_line": 570719448,
      "total_imei": 570719448
    }
  ]
}
```

<a id="alarm-GetAlarmTypeSummary"></a>
## 告警类型统计 (`GetAlarmTypeSummary`)

- 接口 ID：`alarm.GetAlarmTypeSummary`
- 模块：`alarm`
- 用途：告警类型统计
- 原始位置：`source/报警(alarm).txt` 第 2611 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetAlarmTypeSummary |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.car_number` | `string` | 车牌号 |
| `params.date_begin` | `number` | 报警开始时间 |
| `params.date_end` | `number` | 报警结束时间 |
| `params.dev_type` | `number` | 设备类型 |
| `params.get_child` | `boolean` | 是否包含下级，默认否 |
| `params.get_detail` | `boolean` | 获取详情 |
| `params.last_imei` | `number` | 分页参数，上一次请求返回的最后一条imei |
| `params.limit_size` | `number` | 请求条数，获取详情时填写，最大100 |
| `params.protocol` | `number` | 协议 |
| `params.sfamilyid` | `string` | 要查询的组织id,必填 |
| `params.simei` | `string` | 加密的设备号 |
| `params.simeis` | `array` | 查询条件，加密的设备号 |
| `params.type` | `array` | 如果获取详情，必填<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "GetAlarmTypeSummary",
  "params": {
    "car_number": "f3d3560f-b87b-4648-a581-1b83cdf6e63f",
    "date_begin": 1974158696,
    "date_end": 1974158696,
    "dev_type": 1974158696,
    "get_child": true,
    "get_detail": true,
    "last_imei": 1974158696,
    "limit_size": 1974158696,
    "protocol": 1974158696,
    "sfamilyid": "0a5c7032-63d5-45ad-9d5a-f4f1acbf1de3",
    "simei": "f679700a-8ebd-469f-a90d-d0d42ebc50ed",
    "simeis": [
      "247d9f8e-fbb1-40c5-b630-fcc8a8528919",
      "84297047-2c31-40b1-88e0-2a4090c61a15",
      "7edbdf7a-102e-4581-951f-5c14cc467c59"
    ],
    "type": [
      "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
      "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.detail` | `array` | 报警详情 |
| `items.detail.bind_familyname` | `string` |  |
| `items.detail.bind_phone` | `string` |  |
| `items.detail.bind_sfamily` | `string` |  |
| `items.detail.bind_time` | `number` |  |
| `items.detail.car_number` | `string` |  |
| `items.detail.dev_type` | `number` |  |
| `items.detail.familyname` | `string` |  |
| `items.detail.groupname` | `string` |  |
| `items.detail.imei` | `number` | 设备号 |
| `items.detail.protocol` | `number` |  |
| `items.detail.sfamily` | `string` |  |
| `items.detail.sgid` | `string` |  |
| `items.detail.simei` | `string` |  |
| `items.detail.today` | `number` |  |
| `items.detail.total` | `number` |  |
| `items.detail.version` | `string` |  |
| `items.today` | `number` | 今日报警次数 |
| `items.total` | `number` | 总报警次数 |
| `items.type` | `string` | 报警类型<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "11627248-6df9-42e2-993c-e21cedf66b43",
  "items": [
    {
      "detail": [
        {
          "bind_familyname": "7ea0c373-10a3-4ca2-a8d4-1e95ac104a64",
          "bind_phone": "928ebb3a-8441-4015-9b0f-c8b0fba2fd63",
          "bind_sfamily": "ffa777b1-5901-4f4f-b666-5ec53c3f6863",
          "bind_time": 465401911,
          "car_number": "f6062323-8ee9-43aa-99d7-ff1c025831ed",
          "dev_type": 465401911,
          "familyname": "91381b41-3f1f-464e-8092-3d5b662b3b29",
          "groupname": "935fc115-b3d3-42c7-a6e2-37e42eae2d9c",
          "imei": 465401911,
          "protocol": 465401911,
          "sfamily": "d2b330fd-7bed-4844-925f-9b38f62b9635",
          "sgid": "9334188b-97d9-45ad-8656-679beae7b158",
          "simei": "66afd799-a3ba-44f2-85d1-0e4f8e65f50e",
          "today": 465401911,
          "total": 465401911,
          "version": "7c2da4ae-b3f1-4940-8745-c2972c7327ee"
        },
        {
          "bind_familyname": "5b25eede-17d5-47dd-88f6-28ec0a634fec",
          "bind_phone": "837cd337-dedc-433b-81ea-6a63453fe8bd",
          "bind_sfamily": "1d4c8199-1602-4c69-9648-16e12414f78d",
          "bind_time": 465401911,
          "car_number": "21ad1547-dc25-4302-8613-771430ce4d92",
          "dev_type": 465401911,
          "familyname": "4c667d08-27c3-4e14-b59f-5b3f13ebf4cd",
          "groupname": "0e201954-c4e0-405a-971b-982130778555",
          "imei": 465401911,
          "protocol": 465401911,
          "sfamily": "8b070916-6a91-49c6-befa-9ab555ec6b2a",
          "sgid": "b44c8d0a-b2ac-4d57-9a96-e07bedf34064",
          "simei": "6eb76235-bedd-4148-8951-a6d66cc31dcb",
          "today": 465401911,
          "total": 465401911,
          "version": "cbead775-b3e9-40fa-9cf3-75bece04f1de"
        },
        {
          "bind_familyname": "80f50ec3-0808-47db-9c8e-c3f004fc4bb2",
          "bind_phone": "ef227021-f91a-43e4-b4b0-346a2b2a3d9d",
          "bind_sfamily": "512ae241-9d67-4b2d-84c0-cece7bc694db",
          "bind_time": 465401911,
          "car_number": "ccff22d1-5865-4ff4-b341-da7d9bbfc6ab",
          "dev_type": 465401911,
          "familyname": "030f8ec4-353a-46bd-bce1-5166a22eece9",
          "groupname": "11c12a7b-4f29-44f6-943d-987a8c76a06b",
          "imei": 465401911,
          "protocol": 465401911,
          "sfamily": "bf5575ca-c223-4d40-ad91-adfe2a041de8",
          "sgid": "fe35b7f3-b94c-4a81-8435-28e4f92623c5",
          "simei": "b808cba9-c98b-4d5a-9802-9d28ab2010b5",
          "today": 465401911,
          "total": 465401911,
          "version": "9f5aa86f-474f-470d-8abc-4d49d75e48db"
        },
        {
          "bind_familyname": "bba2a9a3-08a1-436d-99d3-d71fe8578c4d",
          "bind_phone": "c1ccc1f9-bb35-4c5c-bee0-93ff4cef1884",
          "bind_sfamily": "46d0c569-762f-4e92-84fd-f072b12125a4",
          "bind_time": 465401911,
          "car_number": "c4e077c2-2583-4ab3-8a5b-b1d9cbcd6a0b",
          "dev_type": 465401911,
          "familyname": "0231fab8-0bb8-48a1-a441-269e4ccdeba9",
          "groupname": "4f42e295-4885-4708-8fc4-5f26baf013ad",
          "imei": 465401911,
          "protocol": 465401911,
          "sfamily": "12512b1d-6422-4cc5-8165-1fffc888b970",
          "sgid": "947862f7-506e-4cd4-8405-c64421b9daa5",
          "simei": "b02b90e5-13bf-451e-84a9-15c56aaea3ac",
          "today": 465401911,
          "total": 465401911,
          "version": "0ccc3019-2f85-48f2-9035-ec3be9352844"
        }
      ],
      "today": 465401911,
      "total": 465401911,
      "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
    }
  ]
}
```

<a id="alarm-AlarmAck"></a>
## 告警确认 (`AlarmAck`)

- 接口 ID：`alarm.AlarmAck`
- 模块：`alarm`
- 用途：告警确认
- 原始位置：`source/报警(alarm).txt` 第 2998 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,AlarmAck |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.items` | `array` | 要操作的项 |
| `params.items.fencename` | `string` | 围栏名称 |
| `params.items.imei` | `number` | 设备号 |
| `params.items.time` | `number` | 告警时间 |
| `params.items.type` | `string` | 告警类型<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |
| `params.remark` | `string` | 备注 |
| `params.sfamilyid` | `string` | 筛选的告警组织 |
| `params.status` | `string` | 告警状态<br>e_alarm_status_unack 待处理<br>e_alarm_status_ack 已处理 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "AlarmAck",
  "params": {
    "items": [
      {
        "fencename": "d3ac89ec-a664-43b5-bb87-88ea77a7098e",
        "imei": 363997761,
        "time": 363997761,
        "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
      }
    ],
    "remark": "20e4f638-6800-4afb-a396-0a7d183d4a79",
    "sfamilyid": "02b7ce61-7004-46e4-8241-a44ba97df5a7",
    "status": "e_alarm_status_unack|e_alarm_status_ack"
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
  "error_message": "c22e8c2a-7688-4554-b1ab-210e5ca93832"
}
```

<a id="alarm-GetAlarmStatusSummary"></a>
## 获取告警确认状态统计 (`GetAlarmStatusSummary`)

- 接口 ID：`alarm.GetAlarmStatusSummary`
- 模块：`alarm`
- 用途：获取告警确认状态统计
- 原始位置：`source/报警(alarm).txt` 第 3152 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetAlarmStatusSummary |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.sfamilyid` | `string` | 加密的组织id，账号所在组织id |
| `params.status` | `string` | 状态<br>e_alarm_status_unack 待处理<br>e_alarm_status_ack 已处理 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "GetAlarmStatusSummary",
  "params": {
    "sfamilyid": "b5e3c5c1-6670-4d14-823c-6e664da0bad7",
    "status": "e_alarm_status_unack|e_alarm_status_ack"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `ack_total` | `number` | 已确认总条数 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `total` | `number` | 总条数 |
| `unack_total` | `number` | 未确认总条数 |

### 返回示例

```json
{
  "errcode": 0,
  "ack_total": 1350535480,
  "error_message": "e0ba09a3-9155-4e78-88a1-df6ff4a163b6",
  "total": 1350535480,
  "unack_total": 1350535480
}
```

<a id="alarm-GetAlarmAck"></a>
## 获取告警确认详情 (`GetAlarmAck`)

- 接口 ID：`alarm.GetAlarmAck`
- 模块：`alarm`
- 用途：获取告警确认详情
- 原始位置：`source/报警(alarm).txt` 第 3201 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetAlarmAck |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.sackid` | `string` | 告警确认id |

### 请求示例

```json
{
  "module": "alarm",
  "func": "GetAlarmAck",
  "params": {
    "sackid": "e64a24e8-6e03-4664-95a2-1b88a74ca490"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `create_time` | `number` | 操作时间 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `opt_account` | `string` | 操作人 |
| `remark` | `string` | 操作说明 |

### 返回示例

```json
{
  "errcode": 0,
  "create_time": 1529272688,
  "error_message": "79dbb34c-7265-4470-8c85-90895ebab19e",
  "opt_account": "bdf92a96-cba8-48f6-9121-4285533c3f62",
  "remark": "afdec760-b4dc-4c80-8c69-456f1255eab6"
}
```

<a id="alarm-GetLastAlarm"></a>
## 获取最新的告警 (`GetLastAlarm`)

- 接口 ID：`alarm.GetLastAlarm`
- 模块：`alarm`
- 用途：获取最新的告警
- 原始位置：`source/报警(alarm).txt` 第 3245 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetLastAlarm |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.begin_time` | `number` | 要拉取的开始时间，取上一次拉取接口返回的next_begin_time，不传则默认当前时间 |
| `params.is_get_child` | `boolean` | 是否包含下级，仅传入sfamilyid有效，root不能包含下级 |
| `params.last_fencename` | `string` | 上次请求的最后一条的fencename |
| `params.last_imei` | `number` | 上次请求的最后一条的imei |
| `params.last_time` | `number` | 分页参数，上次请求的最后一条的time |
| `params.last_type` | `string` | 上次请求的最后一条的type<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |
| `params.limit_size` | `number` | 请求条数 |
| `params.sfamilyid` | `string` | 要拉取的告警组织,设备号登录不用传 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "GetLastAlarm",
  "params": {
    "begin_time": 1077480430,
    "is_get_child": true,
    "last_fencename": "5738c504-7e69-4725-aa19-b44fd6aa83d2",
    "last_imei": 1077480430,
    "last_time": 1077480430,
    "last_type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result",
    "limit_size": 1077480430,
    "sfamilyid": "37354bac-d7a1-4dcb-a878-70864cae69f3"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.car_number` | `string` | 车牌号 |
| `items.content` | `string` | 告警内容 |
| `items.fencename` | `string` | 告警触发的围栏 |
| `items.imei` | `number` | 告警的设备号 |
| `items.speed` | `number` | 告警速度 |
| `items.time` | `number` | 告警时间 |
| `items.type` | `string` | 告警类型<br>e_alarm_shake 震动报警 0x03<br>e_alarm_speed 超速报警 0x06<br>e_alarm_out_fence 出围栏 0x05<br>e_alarm_in_fence 进围栏 0x04<br>e_alarm_lowpower 设备低电量报警 0x11<br>e_alarm_acc_off ACC关闭报警 0xff<br>e_alarm_dismantle  防拆报警 0x13<br>e_alarm_line_dismantle 有线的断电报警(防拆报警)<br>e_alarm_recovery_light 恢复光感脱落(已合并至解除防拆报警)<br>e_alarm_off_line 离线报警<br>e_alarm_fence_overspeed 围栏超速报警<br>e_alarm_remove_fence_overspeed 解除围栏超速报警<br>e_alarm_displacement 位移报警0x09<br>e_alarm_speeding_end 解除超速<br>e_alarm_remove_lowpower 解除低电量报警<br>e_alarm_remove_out 解除防拆报警 0x25<br>e_alarm_sos SOS报警 0x01<br>e_alarm_change_sim 换卡报警 0x10<br>e_alarm_sleep 进入休眠模式报警 0x20<br>e_alarm_acc_on ACC开启报警 0xfe<br>e_alarm_sleep_overtime m7设备检测休眠超时，由服务器判断<br>e_alarm_reach_addr 到达地点报警<br>e_alarm_out_addr 离开地点报警<br>e_alarm_sim_overflow 流量超额报警<br>e_alarm_dual_offline 双离线报警<br>e_alarm_config_begin 大于这个值后面的告警属于行业告警<br>e_alarm_illegal_open 非法开门 3<br>e_alarm_illegal_start 非法启动 4<br>e_alarm_sensitivity 灵敏度报警 9<br>e_alarm_power_cut_off 断油断电报警 10 0x02<br>e_alarm_satellite 卫星盲区报警 16<br>e_alarm_close_down 关机报警 18 0x15<br>e_alarm_pseudo_base_station 伪基站报警 20 0x17<br>e_alarm_start_dev 终端启动报警 22<br>e_alarm_stop_dev 停车报警 23<br>e_alarm_satellite_in 进卫星盲区报警 27 0x0a<br>e_alarm_current_low 外电低电报警 30 0x0e<br>e_alarm_current_protection 外电低电保护报警 31 0x0f<br>e_alarm_current_protection_fly 外电低电保护飞行模式报警 33 0x12<br>e_alarm_vor 声控报警 34 0x16<br>e_alarm_door 门报警 35 0x14<br>e_alarm_open_cover 开盖报警 36 0x18<br>e_alarm_low_battery 内部电池低电报警 37 0x19 (没用)<br>e_alarm_harsh_acceletation 剧烈加速报警 39 0x29<br>e_alarm_harsh_braking 紧急制动报警 40 0x30<br>e_alarm_sharp_left 紧急左转报警 41 0x2a<br>e_alarm_sharp_right 紧急右转报警 42 0x2b<br>e_alarm_sharp_crash 剧烈撞击报警 43 0x2c<br>e_alarm_vehicle_rolling 车辆滚动报警 44 0x2d<br>e_alarm_tilting 车辆倾斜报警 45 0x4b<br>e_alarm_sharp_turn 急转弯报警 46 0x4c<br>e_alarm_lane_switching 突然变道报警 47 0x4d<br>e_alarm_vehicle_stability 稳定性报警 48 0x4e<br>e_alarm_vehicle_angle 角度异常报警 49 0x4f<br>e_alarm_ldlespeed 怠速告警 51<br>e_alarm_elecrope_out 电子牵绳远离报警 52<br>e_alarm_fall 跌倒报警 56<br>e_alarm_heart_hign 心率过高报警 57<br>e_alarm_heart_low 心率过低报警 58<br>e_alarm_remove_tilting 解除车辆倾斜报警 54<br>e_alarm_satellite_out 出卫星盲区报警 28 0x0b<br>e_alarm_satellite_first 卫星第一次定位报警29 0x0d<br>e_alarm_start_up 开机报警 17 0x0c<br>e_alarm_fatigue 疲劳驾驶告警<br>e_alarm_fatigue_remove 解除疲劳驾驶告警<br>e_alarm_fatigue_warn 疲劳驾驶预警<br>e_alarm_bark 犬吠报警<br>e_alarm_share_risk_area_in 进共享风险点告警<br>e_alarm_share_risk_area_out 出共享风险点告警<br>e_alarm_risk_area_in 进风险点告警<br>e_alarm_risk_area_out  出风险点告警<br>e_alarm_home 离家报警(天)<br>e_alarm_company 离司报警<br>e_alarm_night 夜不归宿报警<br>e_alarm_gather 聚集报警<br>e_alarm_distance 分离报警<br>e_alarm_score 超分报警<br>e_alarm_loan_expired 贷款到期报警<br>e_alarm_loan_expired_unbind  贷款到期设备解绑<br>e_alarm_loan_expired_bind  贷款到期设备未解绑<br>e_alarm_loan_unbind  贷款未到期设备解绑<br>e_alarm_road_out 出路线报警<br>e_alarm_road_in 进路线报警<br>e_alarm_school_out 出学校报警<br>e_alarm_school_in 进学校报警<br>e_alarm_home_out 离家报警<br>e_alarm_home_in 到家报警<br>e_alarm_stu_lost 走失报警<br>e_alarm_stu_late 迟到通知<br>e_alarm_stu_early 早退通知<br>e_alarm_stu_lack 缺卡通知<br>e_alarm_stu_leave 请假申请通知<br>e_alarm_stu_leave_result 请假结果通知 |
| `next_begin_time` | `number` | 下次请求的开始时间 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "76277e28-a20a-49cb-9465-64d18dcddde9",
  "items": [
    {
      "car_number": "31b2f5fd-f5e9-4427-92fb-ff84bbee8307",
      "content": "c40c55b7-2f56-44f3-89ed-2f4406782950",
      "fencename": "a9195272-15f2-45de-a179-6d9564fff61f",
      "imei": 648742570,
      "speed": 648742570,
      "time": 648742570,
      "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
    },
    {
      "car_number": "d079131b-17e7-4bcf-a3ff-3a7af95fdec7",
      "content": "abf56614-008c-4b82-ae1a-7fc63c44dc66",
      "fencename": "8a5a52e4-7990-4bb3-a4ff-fce880392217",
      "imei": 648742570,
      "speed": 648742570,
      "time": 648742570,
      "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
    },
    {
      "car_number": "3e4187e2-85c7-4d95-9f67-303b7e122f22",
      "content": "c5c2166c-950d-4597-adfd-095b6b31d8c0",
      "fencename": "9539a55e-7361-4cae-a933-356805f123e7",
      "imei": 648742570,
      "speed": 648742570,
      "time": 648742570,
      "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
    },
    {
      "car_number": "0a2eab5d-34e2-4c9e-935e-703ae84eb6b8",
      "content": "72f1cbe2-df6e-4b5f-84aa-3d9795b35db3",
      "fencename": "43263e7f-efdc-44cc-957c-061892a5cf39",
      "imei": 648742570,
      "speed": 648742570,
      "time": 648742570,
      "type": "e_alarm_min|e_alarm_shake|e_alarm_speed|e_alarm_out_fence|e_alarm_in_fence|e_alarm_lowpower|e_alarm_acc_off|e_alarm_dismantle|e_alarm_line_dismantle|e_alarm_recovery_light|e_alarm_off_line|e_alarm_fence_overspeed|e_alarm_remove_fence_overspeed|e_alarm_displacement|e_alarm_speeding_end|e_alarm_remove_lowpower|e_alarm_remove_out|e_alarm_sos|e_alarm_change_sim|e_alarm_sleep|e_alarm_acc_on|e_alarm_sleep_overtime|e_alarm_reach_addr|e_alarm_out_addr|e_alarm_sim_overflow|e_alarm_dual_offline|e_alarm_config_begin|e_alarm_illegal_open|e_alarm_illegal_start|e_alarm_sensitivity|e_alarm_power_cut_off|e_alarm_satellite|e_alarm_close_down|e_alarm_pseudo_base_station|e_alarm_start_dev|e_alarm_stop_dev|e_alarm_satellite_in|e_alarm_current_low|e_alarm_current_protection|e_alarm_current_protection_fly|e_alarm_vor|e_alarm_door|e_alarm_open_cover|e_alarm_low_battery|e_alarm_harsh_acceletation|e_alarm_harsh_braking|e_alarm_sharp_left|e_alarm_sharp_right|e_alarm_sharp_crash|e_alarm_vehicle_rolling|e_alarm_tilting|e_alarm_sharp_turn|e_alarm_lane_switching|e_alarm_vehicle_stability|e_alarm_vehicle_angle|e_alarm_ldlespeed|e_alarm_elecrope_out|e_alarm_fall|e_alarm_heart_hign|e_alarm_heart_low|e_alarm_remove_tilting|e_alarm_satellite_out|e_alarm_satellite_first|e_alarm_start_up|e_alarm_fatigue|e_alarm_fatigue_remove|e_alarm_fatigue_warn|e_alarm_bark|e_alarm_share_risk_area_in|e_alarm_share_risk_area_out|e_alarm_risk_area_in|e_alarm_risk_area_out|e_alarm_home|e_alarm_company|e_alarm_night|e_alarm_gather|e_alarm_distance|e_alarm_score|e_alarm_loan_expired|e_alarm_loan_expired_unbind|e_alarm_loan_expired_bind|e_alarm_loan_unbind|e_alarm_road_out|e_alarm_road_in|e_alarm_school_out|e_alarm_school_in|e_alarm_home_out|e_alarm_home_in|e_alarm_stu_lost|e_alarm_stu_late|e_alarm_stu_early|e_alarm_stu_lack|e_alarm_stu_leave|e_alarm_stu_leave_result"
    }
  ],
  "next_begin_time": 648742570
}
```

<a id="alarm-SetFamilyAlarm"></a>
## 设置组织告警 (`SetFamilyAlarm`)

- 接口 ID：`alarm.SetFamilyAlarm`
- 模块：`alarm`
- 用途：设置组织告警
- 原始位置：`source/报警(alarm).txt` 第 3544 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：1. 某个告警的参数字段修改了，则属于同一个告警的其他参数字段也需要一起下发, 其他未修改的告警不必下发

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SetFamilyAlarm |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.company_alarm` | `number` | 是否开启离司告警，1=开，0=关 |
| `params.company_alarm_child` | `number` | 离司告警是否包含下级组织，1=开，0=关 |
| `params.company_alarm_days` | `number` | 离司告警天数 |
| `params.distance_alarm` | `number` | 是否开启分离告警，1=开，0=关 |
| `params.distance_alarm_child` | `number` | 分离告警是否包含下级组织，1=开，0=关 |
| `params.distance_alarm_distance` | `number` | 分离报警距离（公里） |
| `params.familyid` | `string` | 组织id |
| `params.gather_alarm` | `number` | 是否开启聚集告警，1=开，0=关 |
| `params.gather_alarm_child` | `number` | 聚集告警是否包含下级组织，1=开，0=关 |
| `params.gather_alarm_count` | `number` | 聚集报警数量 |
| `params.gather_alarm_distance` | `number` | 聚集报警距离（米） |
| `params.home_alarm` | `number` | 是否开启离家告警，1=开，0=关 |
| `params.home_alarm_child` | `number` | 离家告警是否包含下级组织，1=开，0=关 |
| `params.home_alarm_days` | `number` | 离家告警天数 |
| `params.loan_expired_alarm` | `number` | 是否开启贷款到期告警 |
| `params.loan_expired_alarm_child` | `number` | 贷款到期告警是否包含下级组织 |
| `params.loan_expired_bind_alarm` | `number` | 是否开启贷款到期设备未解绑报警 |
| `params.loan_expired_bind_alarm_child` | `number` | 贷款到期设备未解绑告警是否包含下级组织 |
| `params.loan_expired_unbind_alarm` | `number` | 贷款到期设备解绑报警 |
| `params.loan_expired_unbind_alarm_child` | `number` | 贷款到期设备解绑告警是否包含下级组织 |
| `params.loan_unbind_alarm` | `number` | 是否开启贷款未到期设备解绑报警 |
| `params.loan_unbind_alarm_child` | `number` | 贷款未到期设备解绑告警是否包含下级组织 |
| `params.night_alarm` | `number` | 是否开启夜不归宿告警，1=开，0=关 |
| `params.night_alarm_child` | `number` | 夜不归宿告警是否包含下级组织，1=开，0=关 |
| `params.night_alarm_days` | `number` | 夜不归宿天数 |
| `params.risk_area_alarm` | `number` | 是否开启共享风险点，1=开，0=关 |
| `params.risk_area_alarm_child` | `number` | 共享风险点是否包含下级组织，1=开，0=关 |
| `params.risk_area_alarm_duration` | `number` | 风险区域停留时间（秒） |
| `params.score_alarm` | `number` | 是否开启超分告警，1=开，0=关 |
| `params.score_alarm_child` | `number` | 超分告警是否包含下级组织，1=开，0=关 |
| `params.score_alarm_score` | `number` | 超分告警分数 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "SetFamilyAlarm",
  "params": {
    "company_alarm": 990784230,
    "company_alarm_child": 990784230,
    "company_alarm_days": 990784230,
    "distance_alarm": 990784230,
    "distance_alarm_child": 990784230,
    "distance_alarm_distance": 990784230,
    "familyid": "f8b60c12-2414-4a38-b24f-1ed53796ebb0",
    "gather_alarm": 990784230,
    "gather_alarm_child": 990784230,
    "gather_alarm_count": 990784230,
    "gather_alarm_distance": 990784230,
    "home_alarm": 990784230,
    "home_alarm_child": 990784230,
    "home_alarm_days": 990784230,
    "loan_expired_alarm": 990784230,
    "loan_expired_alarm_child": 990784230,
    "loan_expired_bind_alarm": 990784230,
    "loan_expired_bind_alarm_child": 990784230,
    "loan_expired_unbind_alarm": 990784230,
    "loan_expired_unbind_alarm_child": 990784230,
    "loan_unbind_alarm": 990784230,
    "loan_unbind_alarm_child": 990784230,
    "night_alarm": 990784230,
    "night_alarm_child": 990784230,
    "night_alarm_days": 990784230,
    "risk_area_alarm": 990784230,
    "risk_area_alarm_child": 990784230,
    "risk_area_alarm_duration": 990784230,
    "score_alarm": 990784230,
    "score_alarm_child": 990784230,
    "score_alarm_score": 990784230
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
  "error_message": "992697cd-d7c2-4440-aadc-f98f0a862420"
}
```

<a id="alarm-GetFamilyAlarm"></a>
## 获取组织告警 (`GetFamilyAlarm`)

- 接口 ID：`alarm.GetFamilyAlarm`
- 模块：`alarm`
- 用途：获取组织告警
- 原始位置：`source/报警(alarm).txt` 第 3671 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetFamilyAlarm |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.familyid` | `string` | 组织id |

### 请求示例

```json
{
  "module": "alarm",
  "func": "GetFamilyAlarm",
  "params": {
    "familyid": "9e410dad-3b82-4c54-80f8-e62fa345293a"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `company_alarm` | `number` | 是否开启离司告警，1=开，0=关 |
| `company_alarm_child` | `number` | 离司告警是否包含下级组织，1=开，0=关 |
| `company_alarm_days` | `number` | 离司告警天数 |
| `distance_alarm` | `number` | 是否开启分离告警，1=开，0=关 |
| `distance_alarm_child` | `number` | 分离告警是否包含下级组织，1=开，0=关 |
| `distance_alarm_distance` | `number` | 分离报警距离（公里） |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `gather_alarm` | `number` | 是否开启聚集告警，1=开，0=关 |
| `gather_alarm_child` | `number` | 聚集告警是否包含下级组织，1=开，0=关 |
| `gather_alarm_count` | `number` | 聚集报警数量 |
| `gather_alarm_distance` | `number` | 聚集报警距离（米） |
| `home_alarm` | `number` | 是否开启离家告警，1=开，0=关 |
| `home_alarm_child` | `number` | 离家告警是否包含下级组织，1=开，0=关 |
| `home_alarm_days` | `number` | 离家告警天数 |
| `loan_expired_alarm` | `number` | 是否开启贷款到期告警 |
| `loan_expired_alarm_child` | `number` | 贷款到期告警是否包含下级组织 |
| `loan_expired_bind_alarm` | `number` | 是否开启贷款到期设备未解绑报警 |
| `loan_expired_bind_alarm_child` | `number` | 贷款到期设备未解绑告警是否包含下级组织 |
| `loan_expired_unbind_alarm` | `number` | 贷款到期设备解绑报警 |
| `loan_expired_unbind_alarm_child` | `number` | 贷款到期设备解绑告警是否包含下级组织 |
| `loan_unbind_alarm` | `number` | 是否开启贷款未到期设备解绑报警 |
| `loan_unbind_alarm_child` | `number` | 贷款未到期设备解绑告警是否包含下级组织 |
| `night_alarm` | `number` | 是否开启夜不归宿告警，1=开，0=关 |
| `night_alarm_child` | `number` | 夜不归宿告警是否包含下级组织，1=开，0=关 |
| `night_alarm_days` | `number` | 夜不归宿天数 |
| `risk_area_alarm` | `number` | 是否开启共享风险点，1=开，0=关 |
| `risk_area_alarm_child` | `number` | 共享风险点是否包含下级组织，1=开，0=关 |
| `risk_area_alarm_duration` | `number` | 风险区域停留时间（秒） |
| `score_alarm` | `number` | 是否开启超分告警，1=开，0=关 |
| `score_alarm_child` | `number` | 超分告警是否包含下级组织，1=开，0=关 |
| `score_alarm_score` | `number` | 超分告警分数 |

### 返回示例

```json
{
  "errcode": 0,
  "company_alarm": 1050630065,
  "company_alarm_child": 1050630065,
  "company_alarm_days": 1050630065,
  "distance_alarm": 1050630065,
  "distance_alarm_child": 1050630065,
  "distance_alarm_distance": 1050630065,
  "error_message": "c688171a-8cc9-45ae-a5d0-d0b180fd9b11",
  "gather_alarm": 1050630065,
  "gather_alarm_child": 1050630065,
  "gather_alarm_count": 1050630065,
  "gather_alarm_distance": 1050630065,
  "home_alarm": 1050630065,
  "home_alarm_child": 1050630065,
  "home_alarm_days": 1050630065,
  "loan_expired_alarm": 1050630065,
  "loan_expired_alarm_child": 1050630065,
  "loan_expired_bind_alarm": 1050630065,
  "loan_expired_bind_alarm_child": 1050630065,
  "loan_expired_unbind_alarm": 1050630065,
  "loan_expired_unbind_alarm_child": 1050630065,
  "loan_unbind_alarm": 1050630065,
  "loan_unbind_alarm_child": 1050630065,
  "night_alarm": 1050630065,
  "night_alarm_child": 1050630065,
  "night_alarm_days": 1050630065,
  "risk_area_alarm": 1050630065,
  "risk_area_alarm_child": 1050630065,
  "risk_area_alarm_duration": 1050630065,
  "score_alarm": 1050630065,
  "score_alarm_child": 1050630065,
  "score_alarm_score": 1050630065
}
```

<a id="alarm-GetImeiDailyAlarmRank"></a>
## 获取设备日告警数量排行 (`GetImeiDailyAlarmRank`)

- 接口 ID：`alarm.GetImeiDailyAlarmRank`
- 模块：`alarm`
- 用途：获取设备日告警数量排行
- 原始位置：`source/报警(alarm).txt` 第 3796 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetImeiDailyAlarmRank |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.date` | `number` | 日期, 格式如20250101, 不传则默认当天 |
| `params.is_get_child` | `boolean` | 是否包含下级 |
| `params.limit_size` | `number` | 返回数量，默认10, 最大100 |
| `params.sfamilyid` | `string` | 组织id |

### 请求示例

```json
{
  "module": "alarm",
  "func": "GetImeiDailyAlarmRank",
  "params": {
    "date": 456650315,
    "is_get_child": false,
    "limit_size": 456650315,
    "sfamilyid": "264a2552-bbdf-4aee-8ea1-6b1f78ed882c"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.car_number` | `string` | 车牌号 |
| `items.car_owner_name` | `string` | 车主姓名 |
| `items.imei` | `number` | 设备号 |
| `items.total` | `number` | 数量 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "d958ecc1-0460-4916-9649-4472c74b9f6d",
  "items": [
    {
      "car_number": "8d0e9ced-19e4-4847-b90d-f6920614f4a1",
      "car_owner_name": "ad659f87-672c-47a3-ae15-f56be7b12591",
      "imei": 1557100359,
      "total": 1557100359
    },
    {
      "car_number": "eaea0088-eb3e-4d88-9fba-0cdc0dc5b021",
      "car_owner_name": "24b0fbb5-a0a4-44ec-93e0-8f08414f7935",
      "imei": 1557100359,
      "total": 1557100359
    }
  ]
}
```

<a id="alarm-SendCarPush"></a>
## 车辆报警推送 (`SendCarPush`)

- 接口 ID：`alarm.SendCarPush`
- 模块：`alarm`
- 用途：车辆报警推送
- 原始位置：`source/报警(alarm).txt` 第 3864 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SendCarPush |
| `module` | `string` | 模块名,alarm |
| `params` | `object` | 参数列表 |
| `params.items` | `array` |  |
| `params.items.alarm_name` | `string` | 告警名称 |
| `params.items.car_alarm_log` | `object` | 车辆告警日志 |
| `params.items.car_alarm_log.ack_account` | `string` | 经销商告警确认账号 |
| `params.items.car_alarm_log.ack_time` | `number` | 经销商告警确认时间 |
| `params.items.car_alarm_log.addr` | `string` | 地址 |
| `params.items.car_alarm_log.app_flag` | `number` | default:0 |
| `params.items.car_alarm_log.car_id` | `number` | 车辆Id |
| `params.items.car_alarm_log.car_owner_name` | `string` | 车主姓名 |
| `params.items.car_alarm_log.car_owner_phone` | `string` | 车主手机 |
| `params.items.car_alarm_log.car_plate_number` | `string` | 车牌号 |
| `params.items.car_alarm_log.car_status` | `number` | default:0 |
| `params.items.car_alarm_log.extra` | `string` | 其他信息 |
| `params.items.car_alarm_log.familyid` | `number` | 车组织ID |
| `params.items.car_alarm_log.imei` | `number` | 设备号 |
| `params.items.car_alarm_log.root_familyid` | `number` | 根组织ID, 用于分表。龙环: appflag, 校徽: schoolid |
| `params.items.car_alarm_log.time` | `number` | default:0 |
| `params.items.car_alarm_log.type` | `number` | default:0 |
| `params.items.car_alarm_log.user_ack_account` | `string` | 终端用户告警确认账号 |
| `params.items.car_alarm_log.user_ack_time` | `number` | 终端用户告警确认时间 |
| `params.items.push_targets` | `array` | 推送目标<br>e_push_all  推送所有<br>e_push_user  推送终端组织<br>e_push_teacher  推送班主任<br>e_push_business  推送经销商组织 |
| `params.items.wx_official_push_applet_path` | `string` | 微信服务号推送小程序路径 |

### 请求示例

```json
{
  "module": "alarm",
  "func": "SendCarPush",
  "params": {
    "items": [
      {
        "alarm_name": "9b85fab0-1d5f-46c0-b9f3-d7a98654fcf5",
        "car_alarm_log": {
          "ack_account": "aef58cc3-d525-4b63-8325-8db90efcd073",
          "ack_time": 1574760275,
          "addr": "888d8c50-97ec-40c8-86d5-aea2a4f9331e",
          "app_flag": 1574760275,
          "car_id": 1574760275,
          "car_owner_name": "deb6cb7a-3d4a-4da8-955f-15940eed8b9f",
          "car_owner_phone": "710cd58c-d33f-4fa2-93f1-0405425c4f79",
          "car_plate_number": "67d01c94-28d3-4c63-8a7d-c1e2d5d09f9a",
          "car_status": 1574760275,
          "extra": "20ab2b25-5d64-417b-a7bb-08d81da230ef",
          "familyid": 1574760275,
          "imei": 1574760275,
          "root_familyid": 1574760275,
          "time": 1574760275,
          "type": 1574760275,
          "user_ack_account": "5d995d3d-5988-42d2-ae24-a96fef7d6317",
          "user_ack_time": 1574760275
        },
        "push_targets": [
          "e_push_all|e_push_user|e_push_teacher|e_push_business",
          "e_push_all|e_push_user|e_push_teacher|e_push_business",
          "e_push_all|e_push_user|e_push_teacher|e_push_business"
        ],
        "wx_official_push_applet_path": "658c4595-e478-4c52-8220-dbb51a494deb"
      },
      {
        "alarm_name": "58a9db4d-041f-4aa3-9341-117f99bd97c7",
        "car_alarm_log": {
          "ack_account": "2fcd8592-deca-4479-8fb9-9f079e4476d5",
          "ack_time": 1574760275,
          "addr": "5053bc18-d528-4b3f-9673-c40791c85033",
          "app_flag": 1574760275,
          "car_id": 1574760275,
          "car_owner_name": "4fbf24c9-4830-4bdf-98a4-a60ee40f626b",
          "car_owner_phone": "9a286173-9e3a-4cda-b80a-31a390bb0fe7",
          "car_plate_number": "5e666ae3-ca4f-4707-a925-6503fbf68bf6",
          "car_status": 1574760275,
          "extra": "6eab472f-c167-4479-ba5a-5bad5b8fda3b",
          "familyid": 1574760275,
          "imei": 1574760275,
          "root_familyid": 1574760275,
          "time": 1574760275,
          "type": 1574760275,
          "user_ack_account": "40cb5594-a3df-4a84-b38f-c3e3dc30ff7b",
          "user_ack_time": 1574760275
        },
        "push_targets": [
          "e_push_all|e_push_user|e_push_teacher|e_push_business",
          "e_push_all|e_push_user|e_push_teacher|e_push_business"
        ],
        "wx_official_push_applet_path": "e867a0dc-deff-469f-8d1d-7928267ac18a"
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

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "17f27fdc-04d2-4d8d-b188-e273e4408bd3"
}
```


# 电子围栏 (Fence)

- 模块标识：`fence`
- 接口数量：5
- 原始文档：`source/围栏(fence).txt`
- 正式环境：`http://cciot.cc/mapi`
- 测试环境：`http://csapp.cciot.cc/mapi`
- 开发环境：`http://192.168.1.105/mapi`
- 模块负责人：hexiaoqin,fushengyou,hangjl

## 接口索引

| 接口 | 名称 | 请求字段数 | 返回字段数 |
| --- | --- | ---: | ---: |
| [`Add`](#fence-Add) | 添加围栏 | 20 | 9 |
| [`Modify`](#fence-Modify) | 修改围栏 | 20 | 2 |
| [`Get`](#fence-Get) | 获取围栏 | 6 | 19 |
| [`Del`](#fence-Del) | 删除围栏 | 4 | 2 |
| [`BatchDel`](#fence-BatchDel) | 批量删除围栏 | 4 | 2 |

## 接口详情

<a id="fence-Add"></a>
## 添加围栏 (`Add`)

- 接口 ID：`fence.Add`
- 模块：`fence`
- 用途：添加围栏
- 原始位置：`source/围栏(fence).txt` 第 21 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，围栏名称只支持中文字母数字且长度不超过20个字符；
2，一次只能添加一种类型的围栏；
3，多边形围栏至少3个点最多50个点；
4，ofence设置的围栏类型需要和type一致；
5，各围栏不能重名；
6，不支持浮点数；
7，最多添加10个围栏；
8，需要添加围栏权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Add |
| `module` | `string` | 模块名,fence |
| `params` | `object` | 参数列表 |
| `params.fence_switch` | `string` | 围栏报警<br>e_fence_in 进围栏<br>e_fence_out 出围栏<br>e_fence_in_out 进出围栏<br>e_fence_close 关闭 |
| `params.name` | `string` | 围栏名称 |
| `params.ofence` | `object` | 围栏的具体信息 |
| `params.ofence.circle` | `object` | 当围栏为圆形时设置 |
| `params.ofence.circle.lat` | `number` | 圆心的维度，原始坐标系，原值*1000000 |
| `params.ofence.circle.lon` | `number` | 圆心的经度，原始坐标系，原值*1000000 |
| `params.ofence.circle.radius` | `number` | 半径，原值 |
| `params.ofence.city` | `object` | 当围栏为地级市形时设置 |
| `params.ofence.city.district` | `string` | 区名字 |
| `params.ofence.city.name` | `string` | 城市名称 |
| `params.ofence.polygon` | `object` | 当围栏为多边形时设置 |
| `params.ofence.polygon.poit` | `array` | 城市名称 |
| `params.ofence.polygon.poit.lat` | `number` | 维度，原始坐标系，原值*1000000 |
| `params.ofence.polygon.poit.lon` | `number` | 经度，原始坐标系，原值*1000000 |
| `params.sgid` | `string` | 设置一个车组里所有的设备围栏，账号登入可选填，设备登入无效 |
| `params.simei` | `array` | 设备号列表，最多1000个,如果设备号登入或者设置sgid，可以不用填写 |
| `params.type` | `string` | 围栏形状<br>e_type_circle 圆形围栏<br>e_type_city 地市级围栏<br>e_type_polygon 多边形围栏 |

### 请求示例

```json
{
  "module": "fence",
  "func": "Add",
  "params": {
    "fence_switch": "e_fence_in|e_fence_out|e_fence_in_out|e_fence_close",
    "name": "32ec7b22-3543-4245-a1b5-44e5fcfdccfd",
    "ofence": {
      "circle": {
        "lat": 882308369,
        "lon": 882308369,
        "radius": 882308369
      },
      "city": {
        "district": "01c30d59-d0a4-40a7-9093-922ef0c84a50",
        "name": "07853dfd-4c66-4b61-8304-59b18c4acb21"
      },
      "polygon": {
        "poit": [
          {
            "lat": 882308369,
            "lon": 882308369
          },
          {
            "lat": 882308369,
            "lon": 882308369
          },
          {
            "lat": 882308369,
            "lon": 882308369
          }
        ]
      }
    },
    "sgid": "528d29a1-cf0d-4f70-86ff-8e73194bffc9",
    "simei": [
      "ca2b8064-e0f1-4114-8f7a-1c550d9060d8",
      "f884abac-5dd6-4ee2-aa72-428d1ff08045",
      "7c9258ff-fa72-4c1e-9dfe-fd56c2cf92f3"
    ],
    "type": "e_type_circle|e_type_city|e_type_polygon"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_imei` | `array` | 失败列表 |
| `fail_imei.errcode` | `number` | 错误编码 |
| `fail_imei.imei` | `number` | 用于前端展示 |
| `fail_imei.message` | `string` | 失败原因 |
| `suc_items` | `array` | 添加成功的围栏列表 |
| `suc_items.imei` | `number` | 用于前端展示 |
| `suc_items.sfid` | `string` | 新增围栏id |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "2d654f12-8fdc-4726-917c-3577ded99e24",
  "fail_imei": [
    {
      "errcode": 282467839,
      "imei": 282467839,
      "message": "5e698bea-8808-4564-836e-67705afc2c5b"
    },
    {
      "errcode": 282467839,
      "imei": 282467839,
      "message": "cec3325f-249f-4e0a-b712-87d489a09990"
    },
    {
      "errcode": 282467839,
      "imei": 282467839,
      "message": "32362cd1-4483-4d93-a298-844775cff19f"
    }
  ],
  "suc_items": [
    {
      "imei": 282467839,
      "sfid": "55baf0d2-fa49-401c-84ff-48ba43080e16"
    }
  ]
}
```

<a id="fence-Modify"></a>
## 修改围栏 (`Modify`)

- 接口 ID：`fence.Modify`
- 模块：`fence`
- 用途：修改围栏
- 原始位置：`source/围栏(fence).txt` 第 177 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，围栏名称只支持中文字母数字且长度不超过20个字符；
2，一次只能添加一种类型的围栏；
3，多边形围栏至少3个点最多50个点；
4，ofence设置的围栏类型需要和type一致；
5，各围栏不能重名；
6，不支持浮点数；
7，不可修改围栏类型；
8，需要修改围栏权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Modify |
| `module` | `string` | 模块名,fence |
| `params` | `object` | 参数列表 |
| `params.item` | `object` | 需要修改的围栏信息: |
| `params.item.fence_switch` | `string` | 围栏报警,修改是可选填<br>e_fence_in 进围栏<br>e_fence_out 出围栏<br>e_fence_in_out 进出围栏<br>e_fence_close 关闭 |
| `params.item.name` | `string` | 围栏名称,修改是可选填 |
| `params.item.ofence` | `object` | 围栏信息,修改是可选填，根据具体的围栏类型，数据不同:（type为e_type_circle，有半径，经度 ，维度；type为e_type_city有地级市名称 ；e_type_polygon有地址列表',） |
| `params.item.ofence.circle` | `object` | 当围栏为圆形时设置 |
| `params.item.ofence.circle.lat` | `number` | 圆心的维度，原始坐标系，原值*1000000 |
| `params.item.ofence.circle.lon` | `number` | 圆心的经度，原始坐标系，原值*1000000 |
| `params.item.ofence.circle.radius` | `number` | 半径，原值 |
| `params.item.ofence.city` | `object` | 当围栏为地级市形时设置 |
| `params.item.ofence.city.district` | `string` | 区名字 |
| `params.item.ofence.city.name` | `string` | 城市名称 |
| `params.item.ofence.polygon` | `object` | 当围栏为多边形时设置 |
| `params.item.ofence.polygon.poit` | `array` | 城市名称 |
| `params.item.ofence.polygon.poit.lat` | `number` | 维度，原始坐标系，原值*1000000 |
| `params.item.ofence.polygon.poit.lon` | `number` | 经度，原始坐标系，原值*1000000 |
| `params.item.sfid` | `string` | 围栏id |
| `params.item.type` | `string` | 围栏类型,修改是可选填<br>e_type_circle 圆形围栏<br>e_type_city 地市级围栏<br>e_type_polygon 多边形围栏 |

### 请求示例

```json
{
  "module": "fence",
  "func": "Modify",
  "params": {
    "item": {
      "fence_switch": "e_fence_in|e_fence_out|e_fence_in_out|e_fence_close",
      "name": "86fc136b-e010-4558-b66a-849e0c139e93",
      "ofence": {
        "circle": {
          "lat": 1221354766,
          "lon": 1221354766,
          "radius": 1221354766
        },
        "city": {
          "district": "375de0f9-a0b9-45ea-8071-6570bed7aa8e",
          "name": "3d2684c0-b225-4245-8482-8db90fb09c0e"
        },
        "polygon": {
          "poit": [
            {
              "lat": 1221354766,
              "lon": 1221354766
            }
          ]
        }
      },
      "sfid": "bce17398-5357-4eee-8927-15472c4358f1",
      "type": "e_type_circle|e_type_city|e_type_polygon"
    }
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
  "error_message": "0109c6e7-ff0c-4cd6-8037-27bf423ea828"
}
```

<a id="fence-Get"></a>
## 获取围栏 (`Get`)

- 接口 ID：`fence.Get`
- 模块：`fence`
- 用途：获取围栏
- 原始位置：`source/围栏(fence).txt` 第 285 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Get |
| `module` | `string` | 模块名,fence |
| `params` | `object` | 参数列表 |
| `params.last_sfid` | `string` | 瀑布流上一条数据id,非必填,默认从第一条数据开始拉取 |
| `params.limit_size` | `number` | 显示条数,非必填，默认20 |
| `params.simei` | `string` | 设备号,如果设备号登入，可以不用填写 |

### 请求示例

```json
{
  "module": "fence",
  "func": "Get",
  "params": {
    "last_sfid": "027e24d8-7f81-48ae-9bc9-c18b223d38e4",
    "limit_size": 1411736310,
    "simei": "8602399d-199b-4366-a7db-a63e2d97b194"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` | 围栏信息列表 |
| `items.fence_switch` | `string` | 围栏报警,修改是可选填<br>e_fence_in 进围栏<br>e_fence_out 出围栏<br>e_fence_in_out 进出围栏<br>e_fence_close 关闭 |
| `items.name` | `string` | 围栏名称,修改是可选填 |
| `items.ofence` | `object` | 围栏信息,修改是可选填，根据具体的围栏类型，数据不同:（type为e_type_circle，有半径，经度 ，维度；type为e_type_city有地级市名称 ；e_type_polygon有地址列表',） |
| `items.ofence.circle` | `object` | 当围栏为圆形时设置 |
| `items.ofence.circle.lat` | `number` | 圆心的维度，原始坐标系，原值*1000000 |
| `items.ofence.circle.lon` | `number` | 圆心的经度，原始坐标系，原值*1000000 |
| `items.ofence.circle.radius` | `number` | 半径，原值 |
| `items.ofence.city` | `object` | 当围栏为地级市形时设置 |
| `items.ofence.city.district` | `string` | 区名字 |
| `items.ofence.city.name` | `string` | 城市名称 |
| `items.ofence.polygon` | `object` | 当围栏为多边形时设置 |
| `items.ofence.polygon.poit` | `array` | 城市名称 |
| `items.ofence.polygon.poit.lat` | `number` | 维度，原始坐标系，原值*1000000 |
| `items.ofence.polygon.poit.lon` | `number` | 经度，原始坐标系，原值*1000000 |
| `items.sfid` | `string` | 围栏id |
| `items.type` | `string` | 围栏类型,修改是可选填<br>e_type_circle 圆形围栏<br>e_type_city 地市级围栏<br>e_type_polygon 多边形围栏 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "07358553-6686-48ce-a3d8-e09d6d674a8d",
  "items": [
    {
      "fence_switch": "e_fence_in|e_fence_out|e_fence_in_out|e_fence_close",
      "name": "678ac5ca-1bcc-4eb0-a2d8-a8cedf829074",
      "ofence": {
        "circle": {
          "lat": 1868857212,
          "lon": 1868857212,
          "radius": 1868857212
        },
        "city": {
          "district": "ceca5056-2599-4363-8021-aa82f3fb1426",
          "name": "6039909c-e983-4b82-905b-73b180dfd954"
        },
        "polygon": {
          "poit": [
            {
              "lat": 1868857212,
              "lon": 1868857212
            },
            {
              "lat": 1868857212,
              "lon": 1868857212
            },
            {
              "lat": 1868857212,
              "lon": 1868857212
            }
          ]
        }
      },
      "sfid": "d79bb391-8ea1-4a18-bc08-8cb7d82b7afb",
      "type": "e_type_circle|e_type_city|e_type_polygon"
    }
  ]
}
```

<a id="fence-Del"></a>
## 删除围栏 (`Del`)

- 接口 ID：`fence.Del`
- 模块：`fence`
- 用途：删除围栏
- 原始位置：`source/围栏(fence).txt` 第 402 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，需要删除围栏权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Del |
| `module` | `string` | 模块名,fence |
| `params` | `object` | 参数列表 |
| `params.sfid` | `string` | 需要删除的围栏id |

### 请求示例

```json
{
  "module": "fence",
  "func": "Del",
  "params": {
    "sfid": "b5e87fee-51b8-4ecc-8070-794e44bde411"
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
  "error_message": "20f2d8c5-f954-4605-aaee-57b72ca6a163"
}
```

<a id="fence-BatchDel"></a>
## 批量删除围栏 (`BatchDel`)

- 接口 ID：`fence.BatchDel`
- 模块：`fence`
- 用途：批量删除围栏
- 原始位置：`source/围栏(fence).txt` 第 440 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,BatchDel |
| `module` | `string` | 模块名,fence |
| `params` | `object` | 参数列表 |
| `params.sfid` | `array` | 需要删除的围栏id |

### 请求示例

```json
{
  "module": "fence",
  "func": "BatchDel",
  "params": {
    "sfid": [
      "a3eb79eb-2c80-411e-bb13-d4c4094df578",
      "f3ab2aa2-ee9d-4272-a101-2982a8f89f7a",
      "df827f1f-05b2-4e8d-b9a9-b00d2b06ba21",
      "071f8bd2-ba92-47c7-b3aa-ea9785ede3dc"
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
  "error_message": "9ab8d890-4f1f-4eed-a1f4-3158f5ef9a6d"
}
```


# SIM卡 (SIM)

- 模块标识：`sim`
- 接口数量：14
- 原始文档：`source/sim卡(sim).txt`
- 正式环境：`http://cciot.cc/mapi`
- 测试环境：`http://csapp.cciot.cc/mapi`
- 开发环境：`http://192.168.1.105/mapi`
- 模块负责人：hexiaoqin,fushengyou

## 接口索引

| 接口 | 名称 | 请求字段数 | 返回字段数 |
| --- | --- | ---: | ---: |
| [`Get`](#sim-Get) | 获取sim卡信息 | 5 | 22 |
| [`Recharge`](#sim-Recharge) | sim充值 | 11 | 8 |
| [`RemoteSwitch`](#sim-RemoteSwitch) | 设置远程开关机 | 5 | 3 |
| [`QueryPackages`](#sim-QueryPackages) | sim可购套餐查询 | 5 | 71 |
| [`Load`](#sim-Load) | 导入sim卡 | 8 | 5 |
| [`CmdMsg`](#sim-CmdMsg) | 短信透传 | 6 | 2 |
| [`Platform`](#sim-Platform) | 设备流量卡平台信息 | 11 | 21 |
| [`SimCmd`](#sim-SimCmd) | 短信指令 | 9 | 4 |
| [`GetSimCmd`](#sim-GetSimCmd) | 获取通过SimCmd结果 | 5 | 5 |
| [`SimCallBack`](#sim-SimCallBack) | sim卡信息回调 | 6 | 2 |
| [`GetSimAppList`](#sim-GetSimAppList) | 获取sim卡可分配平台列表 | 3 | 5 |
| [`QuerySimType`](#sim-QuerySimType) | 根据平台查询支持的卡类型 | 4 | 5 |
| [`CancelRecharge`](#sim-CancelRecharge) | 撤回充值 | 4 | 2 |
| [`BatchSimCmd`](#sim-BatchSimCmd) | 批量短信指令 | 9 | 6 |

## 接口详情

<a id="sim-Get"></a>
## 获取sim卡信息 (`Get`)

- 接口 ID：`sim.Get`
- 模块：`sim`
- 用途：获取sim卡信息
- 原始位置：`source/sim卡(sim).txt` 第 39 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，设备有sim卡且在sim表中数据完整且正确（iccid,phone,appflag,type都有）；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Get |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |
| `params.iccid` | `string` | 用户输入的iccid或者卡号，iccid与字段simei二选一输入 |
| `params.simei` | `string` | 设备号，如果是设备登入，也可以不用传 |

### 请求示例

```json
{
  "module": "sim",
  "func": "Get",
  "params": {
    "iccid": "c6b332b3-a222-4d2a-84fa-3545cca91e71",
    "simei": "0758d889-1fc7-475e-aa39-64fd3e43bc31"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `balance` | `number` | 余额，单位是M*100 |
| `bstate` | `string` | 操作状态：无、到期停机、到期断网、达量停机、达量断网、套餐已到期 |
| `dstate` | `string` | 在线、离线、关机 |
| `eperiod` | `string` | 服务结束时间 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `iccid` | `string` | ICCID |
| `imei` | `number` | 设备imei号，如果存在绑定设备则返回,否者不返回 |
| `is_recharge` | `boolean` | 是否可充值 |
| `left_flow` | `number` | 剩余流量 |
| `phone` | `string` | 电话号码，对应老版本的cno卡号字段 |
| `rflow` | `number` | 本月实时使用流量，单位是M*100 |
| `rsms` | `number` | 已用短信条数 |
| `rtflow` | `number` | 实际使用的总流量，单位是M*100 |
| `show_flow` | `number` | 展示的使用流量 |
| `sim_platform` | `string` | 卡平台，eg:llink，珍匠 |
| `speriod` | `string` | 服务开始时间 |
| `ssms` | `number` | 总短信条数 |
| `status` | `string` | 卡状态 |
| `tflow` | `number` | 总流量，单位是M*100 |
| `type` | `string` | 卡类型，对应老版本的card_type字段，fxft福信富通,gzwl公众物联,lm龙猫卡，yd移动，zdst中点尚通,ykpt优卡平台 |
| `type_name` | `string` | 卡类型名称 |

### 返回示例

```json
{
  "errcode": 0,
  "balance": 820154995,
  "bstate": "608104e1-6071-4ed9-82cc-75683cfb9abd",
  "dstate": "ddd69173-d64b-4231-885b-9e5ee51a0228",
  "eperiod": "77a11fbf-90fc-4711-951a-2eda59c446c3",
  "error_message": "7a4ba743-aabe-4c16-92c5-176790cfcf50",
  "iccid": "b88d75a5-88fa-46b6-897b-d0386fb70b8d",
  "imei": 820154995,
  "is_recharge": false,
  "left_flow": 820154995,
  "phone": "2d47ce07-2326-4b5a-9fde-6a44b1c4cfe2",
  "rflow": 820154995,
  "rsms": 820154995,
  "rtflow": 820154995,
  "show_flow": 820154995,
  "sim_platform": "99e924da-547d-4a98-830a-cd81e74ad8bd",
  "speriod": "b0476067-eff9-452d-804a-ea23714a2936",
  "ssms": 820154995,
  "status": "0201d9ff-044c-4b7a-a223-27d806708aca",
  "tflow": 820154995,
  "type": "eaf151eb-8f45-4d46-8f46-55b22ddde783",
  "type_name": "5e9df419-d420-4e7b-97aa-55772df6d8f3"
}
```

<a id="sim-Recharge"></a>
## sim充值 (`Recharge`)

- 接口 ID：`sim.Recharge`
- 模块：`sim`
- 用途：sim充值
- 原始位置：`source/sim卡(sim).txt` 第 140 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

仅供内部使用，url无法使用
限制条件：
1，设备有sim卡且在sim表中数据完整且正确（iccid,phone,appflag,type都有）；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Recharge |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |
| `params.iccid` | `string` | 用户输入的iccid，iccid与字段simei二选一输入 |
| `params.is_prepare` | `boolean` | 只做操作前的判断，不实际影响数据库 |
| `params.order` | `string` | 支付订单,公众物联必填 |
| `params.package_no` | `string` | 套餐编码，如果是短信套餐,不填, eg(福信富通):KX00000000312 |
| `params.package_sign` | `string` | 套餐的加密信息,拉取套餐列表时返回该值, |
| `params.package_type` | `string` | 套餐类型,福信富通，中点尚通充值必填，其他非必填<br>e_pt_base 基础套餐<br>e_pt_renewal 续费套餐，zdst里平台返回的续费套餐<br>e_pt_comeon 加油包，zdst里的流量套餐<br>e_pt_choice 可选包<br>e_pt_attach 附加包,zdts里的短信套餐<br>e_pt_package 套餐包,zdst里平台返回的叠加包 |
| `params.simei` | `string` | 设备号，如果是设备登入，也可以不用传 |
| `params.sms_num` | `string` | 充值短信套餐时填写,公众物联，中点尚通支持<br>e_sm_10 10<br>e_sm_50 50<br>e_sm_100 100 |

### 请求示例

```json
{
  "module": "sim",
  "func": "Recharge",
  "params": {
    "iccid": "eb06c81b-8dea-4420-9f4a-9afadb3b2366",
    "is_prepare": false,
    "order": "e50de9f8-abe2-43f5-9dd5-dc409e4d223c",
    "package_no": "f7999eac-c327-456c-8e60-021b082108e6",
    "package_sign": "2451882e-bf7d-4723-ab36-51f57fc202f0",
    "package_type": "e_pt_base|e_pt_renewal|e_pt_comeon|e_pt_choice|e_pt_attach|e_pt_package",
    "simei": "6cbdb996-65b7-4cfb-a4d9-5e52ca1d385b",
    "sms_num": "e_sm_10|e_sm_50|e_sm_100"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `account` | `string` | 当前充值套餐所需要的金额 |
| `balance` | `number` | 尚通续费或者补缴欠费后，账户的余额,单位:分 |
| `cost` | `number` | 尚通续费或者欠费所扣的金额,单位:分 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `familyid` | `number` | 仅供pay端使用 |
| `imei` | `number` | 仅供pay使用 |
| `order_id` | `string` | 卡商充值单号 |

### 返回示例

```json
{
  "errcode": 0,
  "account": "9532fa23-03cb-4170-aec9-1ce746d382a7",
  "balance": 1002263888,
  "cost": 1002263888,
  "error_message": "fd17372f-146a-4296-99c0-e85047ca995d",
  "familyid": 1002263888,
  "imei": 1002263888,
  "order_id": "e61befce-1550-45ba-aca0-875c43b60657"
}
```

<a id="sim-RemoteSwitch"></a>
## 设置远程开关机 (`RemoteSwitch`)

- 接口 ID：`sim.RemoteSwitch`
- 模块：`sim`
- 用途：设置远程开关机
- 原始位置：`source/sim卡(sim).txt` 第 227 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，设备有sim卡且在sim表中数据完整且正确（iccid,phone,appflag,type都有）；
2，设备版本号支持该操作；
3，需要修改设备信息权限；
4，一次操作后要隔一分钟才能再次操作；
5，如果已经有定时开关机，那么远程开关机就不能下发；
6，短信数量充足；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,RemoteSwitch |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |
| `params.simei` | `string` | 设备号，如果是设备登入，也可以不用传 |
| `params.state` | `string` | e_remote_close关机 e_remote_open开机<br>e_remote_close 关机<br>e_remote_open 开机 |

### 请求示例

```json
{
  "module": "sim",
  "func": "RemoteSwitch",
  "params": {
    "simei": "af513064-dcaf-420b-be32-1bc2f6f127a2",
    "state": "e_remote_close|e_remote_open"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `batch_no` | `string` | 发送短信的批次号,公众物联不返回 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |

### 返回示例

```json
{
  "errcode": 0,
  "batch_no": "1c2fa450-a447-46ee-ad69-f29a6a9f5ce3",
  "error_message": "9a129a98-feac-433a-afef-21c8390e2243"
}
```

<a id="sim-QueryPackages"></a>
## sim可购套餐查询 (`QueryPackages`)

- 接口 ID：`sim.QueryPackages`
- 模块：`sim`
- 用途：sim可购套餐查询
- 原始位置：`source/sim卡(sim).txt` 第 278 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，设备有sim卡且在sim表中数据完整且正确（iccid,phone,appflag,type都有）；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,QueryPackages |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |
| `params.iccid` | `string` | 用户输入的iccid，iccid与字段simei二选一输入 |
| `params.simei` | `string` | 设备号，如果是设备登入，也可以不用传 |

### 请求示例

```json
{
  "module": "sim",
  "func": "QueryPackages",
  "params": {
    "iccid": "bc669364-0322-4a9a-9fdd-7f9ec1e53d18",
    "simei": "c27bb2e8-08da-4f58-82f8-b788be20c7c2"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `addoptional` | `array` | 附加套餐信息,zdst的短信套餐 |
| `addoptional.card_no` | `string` | 卡号 |
| `addoptional.name` | `string` | 套餐名称 |
| `addoptional.package_info` | `object` | 套餐内容 |
| `addoptional.package_info.bag_number` | `string` | 附加包编号-附加套餐字段 |
| `addoptional.package_info.base_service_cycle_extend` | `string` | 基础套餐服务期延长（是，否）-可选包 |
| `addoptional.package_info.content_count` | `string` | 附加包内容数量-附加套餐字段 |
| `addoptional.package_info.content_unit` | `string` | 附加包单位(如：条/次) -附加套餐字段 |
| `addoptional.package_info.effect_immediate` | `string` | 立即生效（是，否） |
| `addoptional.package_info.flows` | `string` | 套餐流量，单位：MB |
| `addoptional.package_info.flows_cycle` | `string` | 流量周期，单位：月 |
| `addoptional.package_info.guide_price` | `string` | 套餐市场价格，单位：元 |
| `addoptional.package_info.name` | `string` | 套餐名称 |
| `addoptional.package_info.package_cycle` | `string` | 套餐周期，单位：月-基础套餐 |
| `addoptional.package_info.remark` | `string` | 套餐说明 |
| `addoptional.package_info.reset_cycle` | `string` | 清零周期(与流量周期同值)以月为单位-附加套餐字段 |
| `addoptional.package_info.service_cycle` | `string` | 服务周期以月为单位-非基础包 |
| `addoptional.package_info.type_content` | `string` | 附加包类型说明-附加套餐字段(附加包类型说明-附加套餐字段)) |
| `addoptional.package_info.voices` | `string` | 套餐语音，单位：分钟 |
| `addoptional.package_no` | `string` | 套餐编码 |
| `addoptional.package_sign` | `string` | 套餐的加密信息 |
| `addoptional.package_type` | `string` | 套餐类型<br>e_pt_base 基础套餐<br>e_pt_renewal 续费套餐，zdst里平台返回的续费套餐<br>e_pt_comeon 加油包，zdst里的流量套餐<br>e_pt_choice 可选包<br>e_pt_attach 附加包,zdts里的短信套餐<br>e_pt_package 套餐包,zdst里平台返回的叠加包 |
| `addoptional.renewals_price` | `string` | 套餐价格,单位元 |
| `comeon` | `array` | 加油套餐信息,zdst的流量套餐 |
| `comeon.card_no` | `string` | 卡号 |
| `comeon.name` | `string` | 套餐名称 |
| `comeon.package_info` | `object` | 套餐内容 |
| `comeon.package_info.bag_number` | `string` | 附加包编号-附加套餐字段 |
| `comeon.package_info.base_service_cycle_extend` | `string` | 基础套餐服务期延长（是，否）-可选包 |
| `comeon.package_info.content_count` | `string` | 附加包内容数量-附加套餐字段 |
| `comeon.package_info.content_unit` | `string` | 附加包单位(如：条/次) -附加套餐字段 |
| `comeon.package_info.effect_immediate` | `string` | 立即生效（是，否） |
| `comeon.package_info.flows` | `string` | 套餐流量，单位：MB |
| `comeon.package_info.flows_cycle` | `string` | 流量周期，单位：月 |
| `comeon.package_info.guide_price` | `string` | 套餐市场价格，单位：元 |
| `comeon.package_info.name` | `string` | 套餐名称 |
| `comeon.package_info.package_cycle` | `string` | 套餐周期，单位：月-基础套餐 |
| `comeon.package_info.remark` | `string` | 套餐说明 |
| `comeon.package_info.reset_cycle` | `string` | 清零周期(与流量周期同值)以月为单位-附加套餐字段 |
| `comeon.package_info.service_cycle` | `string` | 服务周期以月为单位-非基础包 |
| `comeon.package_info.type_content` | `string` | 附加包类型说明-附加套餐字段(附加包类型说明-附加套餐字段)) |
| `comeon.package_info.voices` | `string` | 套餐语音，单位：分钟 |
| `comeon.package_no` | `string` | 套餐编码 |
| `comeon.package_sign` | `string` | 套餐的加密信息 |
| `comeon.package_type` | `string` | 套餐类型<br>e_pt_base 基础套餐<br>e_pt_renewal 续费套餐，zdst里平台返回的续费套餐<br>e_pt_comeon 加油包，zdst里的流量套餐<br>e_pt_choice 可选包<br>e_pt_attach 附加包,zdts里的短信套餐<br>e_pt_package 套餐包,zdst里平台返回的叠加包 |
| `comeon.renewals_price` | `string` | 套餐价格,单位元 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `renewal` | `array` | 续费套餐信息,zdst的基础套餐 |
| `renewal.card_no` | `string` | 卡号 |
| `renewal.name` | `string` | 套餐名称 |
| `renewal.package_info` | `object` | 套餐内容 |
| `renewal.package_info.bag_number` | `string` | 附加包编号-附加套餐字段 |
| `renewal.package_info.base_service_cycle_extend` | `string` | 基础套餐服务期延长（是，否）-可选包 |
| `renewal.package_info.content_count` | `string` | 附加包内容数量-附加套餐字段 |
| `renewal.package_info.content_unit` | `string` | 附加包单位(如：条/次) -附加套餐字段 |
| `renewal.package_info.effect_immediate` | `string` | 立即生效（是，否） |
| `renewal.package_info.flows` | `string` | 套餐流量，单位：MB |
| `renewal.package_info.flows_cycle` | `string` | 流量周期，单位：月 |
| `renewal.package_info.guide_price` | `string` | 套餐市场价格，单位：元 |
| `renewal.package_info.name` | `string` | 套餐名称 |
| `renewal.package_info.package_cycle` | `string` | 套餐周期，单位：月-基础套餐 |
| `renewal.package_info.remark` | `string` | 套餐说明 |
| `renewal.package_info.reset_cycle` | `string` | 清零周期(与流量周期同值)以月为单位-附加套餐字段 |
| `renewal.package_info.service_cycle` | `string` | 服务周期以月为单位-非基础包 |
| `renewal.package_info.type_content` | `string` | 附加包类型说明-附加套餐字段(附加包类型说明-附加套餐字段)) |
| `renewal.package_info.voices` | `string` | 套餐语音，单位：分钟 |
| `renewal.package_no` | `string` | 套餐编码 |
| `renewal.package_sign` | `string` | 套餐的加密信息 |
| `renewal.package_type` | `string` | 套餐类型<br>e_pt_base 基础套餐<br>e_pt_renewal 续费套餐，zdst里平台返回的续费套餐<br>e_pt_comeon 加油包，zdst里的流量套餐<br>e_pt_choice 可选包<br>e_pt_attach 附加包,zdts里的短信套餐<br>e_pt_package 套餐包,zdst里平台返回的叠加包 |
| `renewal.renewals_price` | `string` | 套餐价格,单位元 |

### 返回示例

```json
{
  "errcode": 0,
  "addoptional": [
    {
      "card_no": "84003881-20de-4ff5-a382-fbf80d9c5ecb",
      "name": "1895f641-04dc-49e4-9c9d-cafa568160e5",
      "package_info": {
        "bag_number": "762c453a-022e-4470-a8a8-035e4bee765b",
        "base_service_cycle_extend": "80c7f890-7d69-41b6-8297-f90a7881ef04",
        "content_count": "2294e403-ab85-4ee5-8ac7-f0dd257fcc72",
        "content_unit": "4221927f-d3d0-4a9e-9012-6b512283606c",
        "effect_immediate": "fc3ee0cb-6b6f-4abf-a9f6-81e55ea24e63",
        "flows": "9e0da4f1-c238-44e4-9d69-bcf4c70b5dc6",
        "flows_cycle": "fc1a0ed3-94d8-4285-b8a2-79fead6a482a",
        "guide_price": "99f1f65a-b125-461c-9648-1ca1b11741e8",
        "name": "b0d47da3-5bf2-4a8f-97c7-ce1413f4da1f",
        "package_cycle": "0311bf95-0fe4-40fc-b1cc-422b30cbfced",
        "remark": "3982451a-5761-4716-902f-15fecc0bac1f",
        "reset_cycle": "151076d2-93b4-4bb2-a7de-da846e9a27a3",
        "service_cycle": "9c72abe7-2e54-4160-8d41-24f1055744ed",
        "type_content": "5dd7c6d5-8324-4181-b020-0c6b0c672382",
        "voices": "eb2d463a-f5ab-43af-82ee-ff2379e086fd"
      },
      "package_no": "1d29ec98-88ba-4e0e-8d04-855e58cbb638",
      "package_sign": "d46fb330-2c5d-4cdf-b490-074e54d21cf3",
      "package_type": "e_pt_base|e_pt_renewal|e_pt_comeon|e_pt_choice|e_pt_attach|e_pt_package",
      "renewals_price": "1325c070-2f5d-45aa-8eec-160990a48208"
    },
    {
      "card_no": "207ca76b-ef81-4a36-a3f2-17394cd484f4",
      "name": "307ada96-a41a-4b53-94e9-3939ec88cb5b",
      "package_info": {
        "bag_number": "c0a9a38a-00d9-4ad9-ab5a-af84507c2a72",
        "base_service_cycle_extend": "8ab1b6a6-4b6e-4846-9c60-514e2b782d18",
        "content_count": "6b7e7700-2b1d-4837-8423-ae8dadf49140",
        "content_unit": "afdd4d47-bc90-4794-b0f8-722d47ec8d54",
        "effect_immediate": "73b7d552-185d-45ee-a87b-1e37ad6f3a82",
        "flows": "f69c42f3-41be-4139-99eb-8f3515b0c9b4",
        "flows_cycle": "2d572982-acf9-4c02-9c3d-0267c8bd2d21",
        "guide_price": "5ce215a6-ffb2-4148-a91d-325e3604d755",
        "name": "98ead19f-f55e-4536-8d51-c8db81857c89",
        "package_cycle": "68cd6e43-cb8a-4e68-995c-79bfc4773b82",
        "remark": "777be246-337f-4fcd-8e07-db29d49133c3",
        "reset_cycle": "05d6826a-d61f-4509-a84d-11b4fa110136",
        "service_cycle": "2fc2fe6b-789a-41f0-a091-a9585b0e80cd",
        "type_content": "374613e5-8490-4c33-84dd-70e5efba9d0f",
        "voices": "6b14c370-f53f-4886-a2aa-84f40929c4c0"
      },
      "package_no": "55667fd6-79f7-4c78-8e89-5e39403feeb0",
      "package_sign": "7a33a147-ab8a-4d95-b4de-d665c01cd4f7",
      "package_type": "e_pt_base|e_pt_renewal|e_pt_comeon|e_pt_choice|e_pt_attach|e_pt_package",
      "renewals_price": "fff69730-5287-45c4-8c9a-f95d902fd52b"
    }
  ],
  "comeon": [
    {
      "card_no": "c4ec6f3f-9488-43c4-b3b0-aaa4483e5668",
      "name": "606cb163-80cf-42fa-82fb-7a509bf8b122",
      "package_info": {
        "bag_number": "70f983cf-0c61-43a4-8b3c-be1c14177291",
        "base_service_cycle_extend": "bc705dd1-f980-4fa5-9a5d-da0dbf880537",
        "content_count": "3b6508db-5dce-42a1-85b8-bf56eb572a75",
        "content_unit": "d76e2267-d066-47dd-a5ab-49c777ba8db7",
        "effect_immediate": "0cf926bb-caae-49fd-92b6-f4a9295dae0c",
        "flows": "d5aefaae-520f-4fac-a114-ffd0bc9ebd1f",
        "flows_cycle": "70e252cd-4a0b-478b-9226-6a5726629e83",
        "guide_price": "f95caf31-1d06-475b-b73e-4d6857586549",
        "name": "f974a92c-b8fb-4a5f-af4b-e4a5ed871943",
        "package_cycle": "ece06010-09b8-4e27-8cc4-443eb3999346",
        "remark": "77c31d68-0008-4ccd-9e36-29c07c0a0c30",
        "reset_cycle": "84b971ef-938f-4651-b05d-2aee5009eb44",
        "service_cycle": "bbe84a5c-c930-47b2-8e3f-511aa0863a40",
        "type_content": "3c9a2a13-11e5-41f1-8101-5ae08906249c",
        "voices": "9bdd10c1-4f56-423c-b451-f98cf2365258"
      },
      "package_no": "12aad2ce-ad8b-453f-a480-2159c3ed36ec",
      "package_sign": "06004b6b-e020-453a-9bbf-b948e3ba640b",
      "package_type": "e_pt_base|e_pt_renewal|e_pt_comeon|e_pt_choice|e_pt_attach|e_pt_package",
      "renewals_price": "a75c0013-c570-4d65-9f15-d47793d5b6d7"
    }
  ],
  "error_message": "48ec57cf-667b-4ee7-a65f-619e396a5ca7",
  "renewal": [
    {
      "card_no": "d4f72014-cb46-455c-b820-9ab526f90b2d",
      "name": "df5b2627-9a4c-4137-b87e-6ad72be6a91b",
      "package_info": {
        "bag_number": "d8006f8a-c531-422f-ab9c-0046f673c8d0",
        "base_service_cycle_extend": "1e90b40b-8ea3-4554-97dd-b716b4652a94",
        "content_count": "02750c6a-8649-479f-a15a-57ceeafe8a39",
        "content_unit": "5563c87a-5839-4fcc-aa7a-4bbe40df42b7",
        "effect_immediate": "0f4b10a2-685c-4a30-b4d0-9e95034649d6",
        "flows": "cff68657-0b29-411d-badd-ea00b1e18058",
        "flows_cycle": "deb063c6-b8d9-4d0f-969a-1bc439a9667c",
        "guide_price": "aaac3c9c-7c80-4afa-b2b0-b53e1fe4c193",
        "name": "86bc83ff-e981-4a04-b5a5-feea266a4505",
        "package_cycle": "192df62d-ad6e-4cb0-bf2e-a3efb44537ee",
        "remark": "eb9b5871-a839-405a-b173-5608bb46c7df",
        "reset_cycle": "a59e021b-b63d-4de9-9888-96ee1809c7c6",
        "service_cycle": "d3faa0df-5e21-470d-8d54-bbcd2a819f23",
        "type_content": "0aee18c4-30c4-4662-b197-84106ff4fb6e",
        "voices": "2a090b88-5de2-4137-b490-96bdc55f239f"
      },
      "package_no": "470aaf1d-c775-464c-968a-7f66ad6ddd4e",
      "package_sign": "3fcdbc01-4e87-4277-ba39-4ee94007dc4e",
      "package_type": "e_pt_base|e_pt_renewal|e_pt_comeon|e_pt_choice|e_pt_attach|e_pt_package",
      "renewals_price": "7143ee08-0e20-4823-a713-2d9facdad7c9"
    },
    {
      "card_no": "33cb89aa-44ab-42e1-908e-172fe3a6b83a",
      "name": "fd3adbd6-4e9d-4814-8173-97908915e303",
      "package_info": {
        "bag_number": "4d45e714-f4e9-41d0-afb6-82cf2b8f10bd",
        "base_service_cycle_extend": "dbf03417-b97c-4629-8353-3fe0d9ae147c",
        "content_count": "6d06ebe1-5a1c-4d0c-9e6d-91fd14d4d330",
        "content_unit": "45e9dff2-ab48-4926-ad28-38efa0a17e27",
        "effect_immediate": "064964fe-16eb-4747-ba8c-48889d4d6a21",
        "flows": "8f08b3eb-5bab-44ff-866b-112e9e4e5438",
        "flows_cycle": "c4408307-4ddf-477b-939f-3626b413f62b",
        "guide_price": "7c6b4b3c-24b0-435f-8dde-3f663a1fd921",
        "name": "e63307ac-b55c-472c-81f6-75f9d97f0d93",
        "package_cycle": "d863affb-2e45-4411-ba0b-5a3fb4594b40",
        "remark": "012e68e1-835e-4f3d-a792-6795ec342553",
        "reset_cycle": "d8bbf9ae-bae8-421a-b5d1-08391af2025c",
        "service_cycle": "29f57f2c-d745-4d42-b452-0d0ec346f83b",
        "type_content": "cfa3837f-98d2-4eb0-88ff-dc442876be65",
        "voices": "1c659316-1431-43aa-a6f4-ff880824d0ca"
      },
      "package_no": "ac62afcd-f26a-4c65-bf1e-f2dda0a93f47",
      "package_sign": "ca6b7de5-ed5a-496c-b6bf-4502e22e18be",
      "package_type": "e_pt_base|e_pt_renewal|e_pt_comeon|e_pt_choice|e_pt_attach|e_pt_package",
      "renewals_price": "ac544d30-132a-4458-b21c-46da82517e83"
    },
    {
      "card_no": "e208f45f-4d6e-40b1-9e93-f81d7e896bbf",
      "name": "53ce1f8b-847f-40c7-bf18-8213bec2217f",
      "package_info": {
        "bag_number": "178f290b-ea9e-4752-a47e-832ca2a613e7",
        "base_service_cycle_extend": "c374d1b2-08ca-4a07-a020-802790ebb619",
        "content_count": "eab7c051-a522-4bfe-8485-a36524d0436a",
        "content_unit": "d922509a-ac62-48ab-8799-0634e3e71161",
        "effect_immediate": "66480f18-6570-4fef-8a54-51ef587f3473",
        "flows": "c818fae1-fc75-4aa0-9794-72239f8a47c8",
        "flows_cycle": "891bc840-9b32-48bb-94a5-31235558fad6",
        "guide_price": "751af97f-f3b3-402f-a354-0eb6524d5c97",
        "name": "48f35c9d-3209-4edc-91bc-ca5da38d81a3",
        "package_cycle": "95dfa6ad-4c86-4239-94ed-504ee87e2f3f",
        "remark": "3cdf538b-ff7d-41b8-a810-c27fbd9c9150",
        "reset_cycle": "22fe772d-4149-4b9f-a04e-6a07a6f099d1",
        "service_cycle": "c3e62da8-3d94-4cbc-9da1-046e2edcd9e1",
        "type_content": "11722546-3fa9-4a63-9fb0-6a11f39821aa",
        "voices": "cc86543b-af70-4190-a6ad-cff98f595dd5"
      },
      "package_no": "528b5bbf-618a-4cba-b723-937cae5d467a",
      "package_sign": "c1f767d2-29f6-453d-8dd1-07fa11f24c17",
      "package_type": "e_pt_base|e_pt_renewal|e_pt_comeon|e_pt_choice|e_pt_attach|e_pt_package",
      "renewals_price": "5c0f2714-e387-42e1-8990-9d84d416e5fe"
    },
    {
      "card_no": "c70f912c-4df5-4472-98ca-697573e0bcf6",
      "name": "5ff9e5c7-4ea4-4997-b3a5-aa36efab9d07",
      "package_info": {
        "bag_number": "ae2023b8-8d70-46b5-851b-2277a288a155",
        "base_service_cycle_extend": "f88bc98c-b769-4d96-a73f-95d8d6bbd8f5",
        "content_count": "0f0f4b24-3ff9-49b9-9bcc-da5e855d0265",
        "content_unit": "73dfc533-83b1-4b3d-89d6-dc00947abadf",
        "effect_immediate": "b7d33018-3b8c-4f96-a5b1-e4e05ec4f980",
        "flows": "541d57bc-14cf-46a9-b10e-0854d711ee48",
        "flows_cycle": "66b4eabf-1ae6-425d-97fe-11bc09cfde17",
        "guide_price": "ea9c58eb-bd72-4301-b5bf-42a0e46da0d0",
        "name": "f52856b7-95a3-43c4-93cb-3dc5880884ed",
        "package_cycle": "9f381fb0-43ca-49f5-9254-0aaea21c608a",
        "remark": "c8bc6e1f-633f-4e27-a40a-e2eb7eb32e08",
        "reset_cycle": "7ebf49c0-ee3f-4e98-8a3e-035055b65c9d",
        "service_cycle": "b278728d-73eb-4805-8a80-483933a2327e",
        "type_content": "39f56235-e1d7-4b73-9611-aa37a502fe1d",
        "voices": "77f682e3-9513-43dc-9059-9cf4c6df3744"
      },
      "package_no": "d5277ede-2d33-411f-8542-8db269213d7c",
      "package_sign": "7628fbfa-0d09-457d-a241-b1cda4e03c70",
      "package_type": "e_pt_base|e_pt_renewal|e_pt_comeon|e_pt_choice|e_pt_attach|e_pt_package",
      "renewals_price": "3e411286-2bbd-4ee6-b013-198fd33f34dc"
    }
  ]
}
```

<a id="sim-Load"></a>
## 导入sim卡 (`Load`)

- 接口 ID：`sim.Load`
- 模块：`sim`
- 用途：导入sim卡
- 原始位置：`source/sim卡(sim).txt` 第 656 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Load |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |
| `params.app_flag` | `string` | 绑定的app类型，例如2是Llink,如果不填写，以当前登录账号的类型为准 |
| `params.items` | `array` | 需要绑定的类型 |
| `params.items.iccid` | `string` | 需要导入的iccid号 |
| `params.items.phone` | `string` | 需要导入的电话号码 |
| `params.type` | `string` | 绑定的卡类型，例如fxft |

### 请求示例

```json
{
  "module": "sim",
  "func": "Load",
  "params": {
    "app_flag": "63a4be31-a6cb-4837-8991-baade1eeb5de",
    "items": [
      {
        "iccid": "f2515f1a-5a18-471f-85f4-661715006d2d",
        "phone": "461cb2cc-bcef-4e89-a15a-5574277cde86"
      },
      {
        "iccid": "34644736-8e9b-475b-aa37-08e2e7fa460d",
        "phone": "c524f258-2979-4c07-b629-8ae092b6d5a4"
      },
      {
        "iccid": "55200ba1-482d-4470-a1f8-ece7fad7b97f",
        "phone": "943dfbeb-7bd9-4057-b911-9293d87a11a6"
      },
      {
        "iccid": "52a7dab8-08e1-47a4-a96d-70125945bae3",
        "phone": "85d9e39b-2b08-4ae3-85b6-441af912aa1c"
      }
    ],
    "type": "ca6326ea-1987-4b5b-8362-a3c22d1ca3ae"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `fail_items` | `array` | 失败的 |
| `fail_items.error_message` | `string` | 失败信息 |
| `fail_items.iccid` | `string` | 失败的iccid |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "3475f072-3c31-4249-9f1e-56a297e9304c",
  "fail_items": [
    {
      "error_message": "7d1bae3f-5171-45bf-87b8-cb002d0130fe",
      "iccid": "5c62fa3c-4718-4fc0-b824-935d1bbbec5c"
    },
    {
      "error_message": "d61c3ac0-ff99-43f0-838d-26e65fcb8c30",
      "iccid": "8ccd0c51-0923-4b1e-9d4b-cc8e7961381e"
    },
    {
      "error_message": "e390dc7d-bb87-4e90-a47a-eb537d0a66f2",
      "iccid": "86aa3f9e-a61a-4eea-8348-f76dc2540db4"
    }
  ]
}
```

<a id="sim-CmdMsg"></a>
## 短信透传 (`CmdMsg`)

- 接口 ID：`sim.CmdMsg`
- 模块：`sim`
- 用途：短信透传
- 原始位置：`source/sim卡(sim).txt` 第 738 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

说明：通过服务器下发指令给设备，使得设备发送短信给指定的号码

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,CmdMsg |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |
| `params.content` | `string` | 内容信息 |
| `params.phone` | `string` | 电话 |
| `params.simei` | `string` | 设备号，如果是设备登入，也可以不用传 |

### 请求示例

```json
{
  "module": "sim",
  "func": "CmdMsg",
  "params": {
    "content": "fc1b8054-ab10-466c-ae77-799ac2e2b477",
    "phone": "3d297825-3736-4d79-b549-5c317fe64085",
    "simei": "9db38106-2d8a-4a0f-bee2-cdbdf8825620"
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
  "error_message": "970c5c1e-e9e5-4997-8f48-4f7aab30f2c9"
}
```

<a id="sim-Platform"></a>
## 设备流量卡平台信息 (`Platform`)

- 接口 ID：`sim.Platform`
- 模块：`sim`
- 用途：设备流量卡平台信息
- 原始位置：`source/sim卡(sim).txt` 第 781 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Platform |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |
| `params.expire` | `number` | 过期时间，默认0，查询全部;设置-1，已经过期;设置7，查询的是7天内过期的设备列表 |
| `params.is_get_child` | `boolean` | 是否包含下级的组织数据，默认包含 |
| `params.last_sgid` | `string` | 上一页获取返回最后一条数据的分组id |
| `params.last_simei` | `string` | 上一页获取返回最后一条数据的设备id |
| `params.limit_size` | `number` | 数据的限制条数，默认20条,最高不超过100 |
| `params.sfamily` | `string` | 需要拉取的组织id,如果设备登入，不填,字段sgid,simei,sfamilyid三选一 |
| `params.sgid` | `string` | 需要获取统计的车组,如果设备登入，不填,字段sgid,simei,sfamilyid三选一 |
| `params.simei` | `array` | 统计的设备id，如果设备登入，不填,必须是同个组织下的设备.字段sgid,simei,sfamilyid三选一 |

### 请求示例

```json
{
  "module": "sim",
  "func": "Platform",
  "params": {
    "expire": 363189291,
    "is_get_child": false,
    "last_sgid": "f863626e-213e-4e7c-9a44-623e6bcf3a7e",
    "last_simei": "23e07205-4e0a-4117-930e-362964c2a1af",
    "limit_size": 363189291,
    "sfamily": "b2c6732a-be50-42d0-a108-f4c39f699b50",
    "sgid": "9e4a253a-7434-4157-98b9-87704704e79b",
    "simei": [
      "07c8ceb1-aa83-4afe-91dd-58347ed3ded2",
      "0c2568b7-72db-4676-8e1a-7b0ed0489547",
      "fadffa84-06e5-4af2-b139-e02c434f0462",
      "6398bbca-24b6-4d50-aa00-ce199459907e"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.bind_familyname` | `string` | 绑定终端组织名称 |
| `items.bind_phone` | `string` | 绑定手机号 |
| `items.bind_sfamily` | `string` | 绑定终端组织id |
| `items.bind_time` | `number` | 绑定时间 |
| `items.car_number` | `string` | 车牌号 |
| `items.familyname` | `string` | 组织名称 |
| `items.groupname` | `string` | 分组名称 |
| `items.iccid` | `string` | 绑定的ICCID |
| `items.imei` | `number` | 设备号 |
| `items.last_com_time` | `number` | 最后通信的时间，也就是最后在线时间 |
| `items.phone` | `string` | 绑定的卡号 |
| `items.sfamily` | `string` | 组织id |
| `items.sgid` | `string` | 分组id |
| `items.sim_end_time` | `number` | sim卡到期时间(1699302765) |
| `items.sim_start_time` | `number` | sim卡激活时间 |
| `items.simei` | `string` | 设备id |
| `items.version` | `string` | 设备型号 |
| `total` | `number` | 总数,只有在不翻页的时候返回 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "f5b80d42-e3dd-4a75-94ab-e43608bbac4a",
  "items": [
    {
      "bind_familyname": "8b7b2383-caad-4e69-aa86-529ed214ce77",
      "bind_phone": "188a343d-6e0e-45ed-92c1-3db2bf3b0fc7",
      "bind_sfamily": "5473e343-1c28-4012-9428-f9e43adb73f4",
      "bind_time": 219719241,
      "car_number": "46eeffd6-1066-4046-91fd-ccf39d1419f1",
      "familyname": "f5d0d5ac-1c1e-40c7-8aff-d8105e38fe61",
      "groupname": "170454ef-1344-4dd1-816f-91e045e4f965",
      "iccid": "514c5932-b7ff-47ee-a404-34e606bcfc4a",
      "imei": 219719241,
      "last_com_time": 219719241,
      "phone": "9face7da-fd4c-45fc-9158-3610067d0e62",
      "sfamily": "e6a83472-8b28-4aea-93ab-8533e8b25fcd",
      "sgid": "5bc143a0-13f1-4cd8-b61b-4f5a545a1acd",
      "sim_end_time": 219719241,
      "sim_start_time": 219719241,
      "simei": "33f7b801-d09c-4d9d-adc4-9ac0a3182ae8",
      "version": "edf60b79-9ec6-45e4-9286-fd789aa4bee7"
    },
    {
      "bind_familyname": "514ad710-c21a-41d3-8ef2-a5197f6f634d",
      "bind_phone": "ef816ff6-b6bd-4024-8ea2-7a3854de230f",
      "bind_sfamily": "5d067b04-c718-4992-a7ad-d5374178cd2a",
      "bind_time": 219719241,
      "car_number": "a0d44740-a764-4eb9-afd6-b39eee31b1bc",
      "familyname": "40c29080-41e9-4c8b-bdaa-074325f76213",
      "groupname": "287bd77b-5889-4f66-b235-126e021dc901",
      "iccid": "9c3ce5c4-eb0d-4526-9f61-7271e05a4abf",
      "imei": 219719241,
      "last_com_time": 219719241,
      "phone": "2aa076ed-bdc5-45af-9dab-cdbd701a8c20",
      "sfamily": "083c37dc-a225-4cae-960d-b27e412bb165",
      "sgid": "49beab1b-0f3f-4ca1-90f0-1d0d3a762494",
      "sim_end_time": 219719241,
      "sim_start_time": 219719241,
      "simei": "8754136e-c031-4285-be77-866e445bd518",
      "version": "a7cf3905-751d-45a0-ac20-a3eadfe43bdb"
    }
  ],
  "total": 219719241
}
```

<a id="sim-SimCmd"></a>
## 短信指令 (`SimCmd`)

- 接口 ID：`sim.SimCmd`
- 模块：`sim`
- 用途：短信指令
- 原始位置：`source/sim卡(sim).txt` 第 921 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

说明：通过短信内容下发指令给设备，如果设备不在线，将自动下发短信给设备；
如果设备在线，那么先下发指令，如果设备一段时间没有回复，那么自动下发短信给设备

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SimCmd |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |
| `params.content` | `string` | 短信内容 |
| `params.flags` | `array` | 短信展示标志<br>e_urgent 紧急<br>e_display 终端显示器显示<br>e_tts 终端tts播读<br>e_adscreen 广告屏显示 |
| `params.only_cmd` | `boolean` | 是否只通过给sim卡下发协议的方式，默认false， |
| `params.only_sim` | `boolean` | 是否只通过给sim卡下发短信的方式，默认false， |
| `params.protocol` | `number` | 协议 |
| `params.simei` | `string` | 设备号，如果是设备登入，也可以不用传 |

### 请求示例

```json
{
  "module": "sim",
  "func": "SimCmd",
  "params": {
    "content": "60c67fb4-b08c-4a4d-9c3d-9b25a04422ee",
    "flags": [
      "e_urgent|e_display|e_tts|e_adscreen",
      "e_urgent|e_display|e_tts|e_adscreen",
      "e_urgent|e_display|e_tts|e_adscreen",
      "e_urgent|e_display|e_tts|e_adscreen"
    ],
    "only_cmd": true,
    "only_sim": true,
    "protocol": 1308295614,
    "simei": "00818ad6-fdb9-44f9-ad7b-26f962eab506"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `cid` | `string` | 返回指令id，可通过GetSimCmd查询返回的结果 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `type` | `string` | 指令下发方式<br>e_sc_cmd 下发了指令模式<br>e_sc_sim 下发了短信模式 |

### 返回示例

```json
{
  "errcode": 0,
  "cid": "d589d4bb-5205-4b67-ba3e-0fd623979d7b",
  "error_message": "dad06416-d56d-4be7-a2c7-fa6022877746",
  "type": "e_sc_cmd|e_sc_sim"
}
```

<a id="sim-GetSimCmd"></a>
## 获取通过SimCmd结果 (`GetSimCmd`)

- 接口 ID：`sim.GetSimCmd`
- 模块：`sim`
- 用途：获取通过SimCmd结果
- 原始位置：`source/sim卡(sim).txt` 第 991 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetSimCmd |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |
| `params.cid` | `string` | 通过接口SimCmd返回的指令id |
| `params.simei` | `string` | 设备号，如果是设备登入，也可以不用传 |

### 请求示例

```json
{
  "module": "sim",
  "func": "GetSimCmd",
  "params": {
    "cid": "a6597bb1-93f2-427a-9e91-dcd203292d5b",
    "simei": "9d564061-6ba0-4d37-a953-e4892310b10a"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `content` | `string` | 返回内容 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `has_result` | `boolean` | 是否已经获取到返回了 |
| `type` | `string` | 指令下发方式<br>e_sc_cmd 下发了指令模式<br>e_sc_sim 下发了短信模式 |

### 返回示例

```json
{
  "errcode": 0,
  "content": "1f8fb55d-1060-4f62-b90d-4236c9bb05c1",
  "error_message": "2e8ddfa8-340e-42b2-af28-964e9fba52c3",
  "has_result": true,
  "type": "e_sc_cmd|e_sc_sim"
}
```

<a id="sim-SimCallBack"></a>
## sim卡信息回调 (`SimCallBack`)

- 接口 ID：`sim.SimCallBack`
- 模块：`sim`
- 用途：sim卡信息回调
- 原始位置：`source/sim卡(sim).txt` 第 1040 行
- URL 参数：源文档未列出

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,SimCallBack |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |
| `params.content` | `string` | 回调信息 |
| `params.platform` | `string` | 当前运行平台 |
| `params.simtype` | `string` | 卡类型,fxft福信富通,gzwl公众物联,lm龙猫卡，yd移动，zdst中点尚通 |

### 请求示例

```json
{
  "module": "sim",
  "func": "SimCallBack",
  "params": {
    "content": "810baf2e-9c52-42d6-86b3-afd847dfe06f",
    "platform": "a22fbb31-271a-4c9f-a07f-93ffa130bac6",
    "simtype": "882f8439-7dd6-47b6-9f96-b500dab5291f"
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
  "error_message": "a8c084b6-020b-466f-aed0-6b3b8bfa9794"
}
```

<a id="sim-GetSimAppList"></a>
## 获取sim卡可分配平台列表 (`GetSimAppList`)

- 接口 ID：`sim.GetSimAppList`
- 模块：`sim`
- 用途：获取sim卡可分配平台列表
- 原始位置：`source/sim卡(sim).txt` 第 1079 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetSimAppList |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |

### 请求示例

```json
{
  "module": "sim",
  "func": "GetSimAppList",
  "params": {}
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.app_id` | `string` | 可以在Load接口中传入到app_flag中 |
| `items.app_name` | `string` | 平台组织名称 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "03eeed02-e394-4031-93ba-25d8571a246d",
  "items": [
    {
      "app_id": "4692bdc0-fc0c-4667-b5ff-e54fe6c66c10",
      "app_name": "fdfc1f1e-fab3-4707-a4b7-3ed35aba537e"
    }
  ]
}
```

<a id="sim-QuerySimType"></a>
## 根据平台查询支持的卡类型 (`QuerySimType`)

- 接口 ID：`sim.QuerySimType`
- 模块：`sim`
- 用途：根据平台查询支持的卡类型
- 原始位置：`source/sim卡(sim).txt` 第 1122 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,QuerySimType |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |
| `params.app_id` | `string` | 可以在Load接口中传入到app_flag中 |

### 请求示例

```json
{
  "module": "sim",
  "func": "QuerySimType",
  "params": {
    "app_id": "0b68152c-d0ec-4d09-8354-80991874564c"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.type` | `string` | 卡平台 |
| `items.type_name` | `string` | 卡平台展示名称 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "b6ba888f-102f-4a43-bd65-72e8e1e77445",
  "items": [
    {
      "type": "cde22dae-82f1-4ce2-b942-1508ee6209aa",
      "type_name": "403e7b0b-bd0e-4765-9b0b-20db4ec29bba"
    },
    {
      "type": "8bcff80d-424d-4bf6-8c4a-c2b22f126dfd",
      "type_name": "af71ab97-e4f0-4824-8801-0628329f61d8"
    },
    {
      "type": "01a8bf43-eb9f-4be5-8c3e-6603ce1de82e",
      "type_name": "949341da-6998-4d7d-a99b-f830443f90ae"
    }
  ]
}
```

<a id="sim-CancelRecharge"></a>
## 撤回充值 (`CancelRecharge`)

- 接口 ID：`sim.CancelRecharge`
- 模块：`sim`
- 用途：撤回充值
- 原始位置：`source/sim卡(sim).txt` 第 1177 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,CancelRecharge |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |
| `params.order_id` | `string` | 充值单号 |

### 请求示例

```json
{
  "module": "sim",
  "func": "CancelRecharge",
  "params": {
    "order_id": "b4578541-d3e4-4332-a8cb-6635a9cd63e1"
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
  "error_message": "0760aa7f-2877-4641-8415-14fdd72b1678"
}
```

<a id="sim-BatchSimCmd"></a>
## 批量短信指令 (`BatchSimCmd`)

- 接口 ID：`sim.BatchSimCmd`
- 模块：`sim`
- 用途：批量短信指令
- 原始位置：`source/sim卡(sim).txt` 第 1212 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

批量通过sim卡下发短信指令

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,BatchSimCmd |
| `module` | `string` | 模块名,sim |
| `params` | `object` | 参数列表 |
| `params.content` | `string` | 短信内容，必填 |
| `params.flags` | `array` | 短信展示标志<br>e_urgent 紧急<br>e_display 终端显示器显示<br>e_tts 终端tts播读<br>e_adscreen 广告屏显示 |
| `params.protocol` | `number` | 下发使用的协议，必填 |
| `params.sfamilyid` | `string` | 选择某个组织 |
| `params.sgid` | `string` | 选择某个分组, 分组所在的familyid也要传递 |
| `params.simei` | `array` | 设备号 |

### 请求示例

```json
{
  "module": "sim",
  "func": "BatchSimCmd",
  "params": {
    "content": "759a9142-7c62-469e-96b8-700f121291ff",
    "flags": [
      "e_urgent|e_display|e_tts|e_adscreen"
    ],
    "protocol": 1907947712,
    "sfamilyid": "6a1e9d9f-4d62-45ae-ac75-eb2ceb78f1d5",
    "sgid": "fad9be67-5d46-4f37-93ab-c1515e636349",
    "simei": [
      "b10e100a-cb55-4b9b-b995-19cbecdd60b0"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `cids` | `array` |  |
| `cids.cid` | `string` | 返回指令id，可通过GetSimCmd查询返回的结果 |
| `cids.imei` | `number` | 设备号 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `type` | `string` | 指令下发方式<br>e_sc_cmd 下发了指令模式<br>e_sc_sim 下发了短信模式 |

### 返回示例

```json
{
  "errcode": 0,
  "cids": [
    {
      "cid": "aec030c1-6488-43fc-94af-45e287562555",
      "imei": 542328501
    },
    {
      "cid": "d09641db-10b7-4829-9d5b-ac7a7850d0ef",
      "imei": 542328501
    },
    {
      "cid": "922b31ab-86c0-4142-9ede-715ea67217ec",
      "imei": 542328501
    },
    {
      "cid": "caee0449-0235-4ba1-8923-dc34e5aa6dc3",
      "imei": 542328501
    }
  ],
  "error_message": "19d29fe5-ed9e-47e4-a541-19b498d50caf",
  "type": "e_sc_cmd|e_sc_sim"
}
```


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


# 定时开关机（timerswitch）

- 模块标识：`timerswitch`
- 接口数量：3
- 原始文档：`source/定时开关机(timerswitch).txt`
- 正式环境：`http://cciot.cc/mapi`
- 测试环境：`http://csapp.cciot.cc/mapi`
- 开发环境：`http://192.168.1.105/mapi`
- 模块负责人：hexiaoqin,fushengyou

## 接口索引

| 接口 | 名称 | 请求字段数 | 返回字段数 |
| --- | --- | ---: | ---: |
| [`Set`](#timerswitch-Set) | 设置定时开关 | 9 | 2 |
| [`Get`](#timerswitch-Get) | 获取定时开关 | 4 | 8 |
| [`Close`](#timerswitch-Close) | 关闭定时开关 | 4 | 2 |

## 接口详情

<a id="timerswitch-Set"></a>
## 设置定时开关 (`Set`)

- 接口 ID：`timerswitch.Set`
- 模块：`timerswitch`
- 用途：设置定时开关
- 原始位置：`source/定时开关机(timerswitch).txt` 第 17 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，设备版本需要支持定时开关机；
2，最多设置3个时间段；
3，时间段可以跨天但不可重叠；
4，睡眠模式下不允许操作；
5，需要修改设备信息权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Set |
| `module` | `string` | 模块名,timerswitch |
| `params` | `object` | 参数列表 |
| `params.loc_type` | `string` | 定时模式,如果是设置，就选填，如果是添加，就必填<br>e_loctype_eachday<br>e_loctype_currentmonth<br>e_loctype_eachmonth |
| `params.loop_days` | `array` | 模式,如果是设置，就选填，如果是添加，就必填,如果模式为e_loctype_eachmonth或e_loctype_currentmonth时,上传该参数 例如:1 2 13 25 31 范围(1-31) |
| `params.simei` | `string` | 设备号,如果设备号登入，可以不用填写 |
| `params.timer` | `array` | 定时开关时间对,如果是设置，就选填，如果是添加，就必填 |
| `params.timer.close` | `string` | 关机时间，格式: HH:MM |
| `params.timer.start` | `string` | 开机时间，格式为: HH:MM |

### 请求示例

```json
{
  "module": "timerswitch",
  "func": "Set",
  "params": {
    "loc_type": "e_loctype_eachday|e_loctype_currentmonth|e_loctype_eachmonth",
    "loop_days": [
      1165374751,
      1165374751,
      1165374751
    ],
    "simei": "717d65ea-d396-41a9-a49f-ffc26d00240b",
    "timer": [
      {
        "close": "88cbdd96-1755-4ea6-b1d1-01a2ad7f15af",
        "start": "2569ea70-65d4-4648-ade2-1b43bf9e0d84"
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

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "679e585b-4fb8-4ec7-872e-4a633cafc5ab"
}
```

<a id="timerswitch-Get"></a>
## 获取定时开关 (`Get`)

- 接口 ID：`timerswitch.Get`
- 模块：`timerswitch`
- 用途：获取定时开关
- 原始位置：`source/定时开关机(timerswitch).txt` 第 84 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Get |
| `module` | `string` | 模块名,timerswitch |
| `params` | `object` | 参数列表 |
| `params.simei` | `string` | 设备号,如果设备号登入，可以不用填写 |

### 请求示例

```json
{
  "module": "timerswitch",
  "func": "Get",
  "params": {
    "simei": "a2bf1b2c-476b-48e0-9514-c48c074c87e3"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `current_month` | `number` | 选择当月时的年月份,例如：202106, 默认系统时间 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `loc_type` | `string` | 定时模式<br>e_loctype_eachday<br>e_loctype_currentmonth<br>e_loctype_eachmonth<br>e_loctype_close |
| `loop_days` | `array` | 周期定位日期 |
| `timer` | `array` | 周期定位每日的时间 |
| `timer.close` | `string` | 关机时间，格式: HH:MM |
| `timer.start` | `string` | 开机时间，格式为: HH:MM |

### 返回示例

```json
{
  "errcode": 0,
  "current_month": 112075901,
  "error_message": "2e53ea3f-f2de-4887-84e3-e547209ccd62",
  "loc_type": "e_loctype_eachday|e_loctype_currentmonth|e_loctype_eachmonth|e_loctype_close",
  "loop_days": [
    112075901
  ],
  "timer": [
    {
      "close": "b800ae93-7e5b-454a-9076-d5d23dbf3c0c",
      "start": "7323ca32-f6dc-4613-aa42-736b7852f38a"
    },
    {
      "close": "1e687f24-260b-445c-9bd2-0d2b2348b0a3",
      "start": "46a9aaf5-a641-44ba-872a-b8d79e79e75e"
    },
    {
      "close": "45042cf8-d968-4259-a061-386235500403",
      "start": "a39e62a5-2203-4732-8f91-b43309dcf4c2"
    },
    {
      "close": "3fd5ee86-7ecf-46cc-b237-3a081fbbe842",
      "start": "47db3a56-216c-406a-8e54-6db4fb4993dd"
    }
  ]
}
```

<a id="timerswitch-Close"></a>
## 关闭定时开关 (`Close`)

- 接口 ID：`timerswitch.Close`
- 模块：`timerswitch`
- 用途：关闭定时开关
- 原始位置：`source/定时开关机(timerswitch).txt` 第 158 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 说明

限制条件：
1，设备版本需要支持定时开关机；
2，睡眠模式下不允许操作；
3，需要修改设备信息权限；

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,Close |
| `module` | `string` | 模块名,timerswitch |
| `params` | `object` | 参数列表 |
| `params.simei` | `string` | 设备号,如果设备号登入，可以不用填写 |

### 请求示例

```json
{
  "module": "timerswitch",
  "func": "Close",
  "params": {
    "simei": "899b020d-649c-4171-adf9-49d4022a1d95"
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
  "error_message": "aa27efc0-3228-429d-a4ef-2edbed5a9f36"
}
```


# 统计报表 (Summary)

- 模块标识：`summary`
- 接口数量：7
- 原始文档：`source/统计(summary).txt`
- 正式环境：`http://cciot.cc/mapi`
- 测试环境：`http://csapp.cciot.cc/mapi`
- 开发环境：`http://192.168.1.105/mapi`
- 模块负责人：hexq,fushengyou

## 接口索引

| 接口 | 名称 | 请求字段数 | 返回字段数 |
| --- | --- | ---: | ---: |
| [`DeviceSimple`](#summary-DeviceSimple) | 设备简单统计 | 6 | 5 |
| [`RunStat`](#summary-RunStat) | 运行总览 | 12 | 22 |
| [`OnLineState`](#summary-OnLineState) | 在线状态统计 | 11 | 18 |
| [`PlatformVip`](#summary-PlatformVip) | 平台服务 | 12 | 19 |
| [`GetSimPayDay`](#summary-GetSimPayDay) | 获取sim支付日统计 | 6 | 12 |
| [`GetSimPayMonth`](#summary-GetSimPayMonth) | 获取sim支付月统计 | 6 | 6 |
| [`FamilyDevState`](#summary-FamilyDevState) | 组织下设备状态统计 | 5 | 22 |

## 接口详情

<a id="summary-DeviceSimple"></a>
## 设备简单统计 (`DeviceSimple`)

- 接口 ID：`summary.DeviceSimple`
- 模块：`summary`
- 用途：设备简单统计
- 原始位置：`source/统计(summary).txt` 第 25 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,DeviceSimple |
| `module` | `string` | 模块名,summary |
| `params` | `object` | 参数列表 |
| `params.simei` | `string` | 需要查询统计的设备号，如果设备号登入，可以不用传 |
| `params.time_begin` | `number` | 起始时间戳 |
| `params.time_end` | `number` | 结束时间戳 |

### 请求示例

```json
{
  "module": "summary",
  "func": "DeviceSimple",
  "params": {
    "simei": "74d9ca43-de66-4aca-8c3a-8d71cd406c9e",
    "time_begin": 2078273896,
    "time_end": 2078273896
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `distance` | `number` | 里程,单位m |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `speed_alarm` | `number` | 超速告警次数,如果没有，则不返回 |
| `total_alarm` | `number` | 告警总数,如果没有，则不返回 |

### 返回示例

```json
{
  "errcode": 0,
  "distance": 1475490403,
  "error_message": "2c672cf2-9462-467b-bd33-29d290b5eb44",
  "speed_alarm": 1475490403,
  "total_alarm": 1475490403
}
```

<a id="summary-RunStat"></a>
## 运行总览 (`RunStat`)

- 接口 ID：`summary.RunStat`
- 模块：`summary`
- 用途：运行总览
- 原始位置：`source/统计(summary).txt` 第 75 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,RunStat |
| `module` | `string` | 模块名,summary |
| `params` | `object` | 参数列表 |
| `params.is_get_child` | `boolean` | 是否包含下级的组织数据，默认包含 |
| `params.last_sgid` | `string` | 上一页获取返回最后一条数据的分组id |
| `params.last_simei` | `string` | 上一页获取返回最后一条数据的设备id |
| `params.limit_size` | `number` | 数据的限制条数，默认20条,最高不超过100 |
| `params.sfamily` | `string` | 需要拉取的组织id,如果设备登入，不填,字段sgid,simei,sfamilyid三选一 |
| `params.sgid` | `string` | 需要获取统计的车组,如果设备登入，不填,字段sgid,simei,sfamilyid三选一 |
| `params.simei` | `array` | 统计的设备id，如果设备登入，不填,必须是同个组织下的设备.字段sgid,simei,sfamilyid三选一 |
| `params.time_begin` | `number` | 起始时间戳,为0或者不传,即以服务器的起始时间算起 |
| `params.time_end` | `number` | 结束时间戳,为0或者不传,即以服务器的当前时间算起 |

### 请求示例

```json
{
  "module": "summary",
  "func": "RunStat",
  "params": {
    "is_get_child": true,
    "last_sgid": "b6bf80d9-2b31-4a93-b6c2-665866fbd5dc",
    "last_simei": "9090b357-2abb-4184-99fd-f7eaa86157af",
    "limit_size": 1935017354,
    "sfamily": "67e0c7d9-6236-4cda-ba10-78aaf0a16519",
    "sgid": "cc1cdd99-d28b-4c46-b36c-b1405638b168",
    "simei": [
      "22d0023b-fa4a-4011-a73b-07934082abf4",
      "c3526274-ed31-4a93-b732-0e6caac523c9",
      "9b678c66-5554-48ff-84d5-beb06fd7aa2e"
    ],
    "time_begin": 1935017354,
    "time_end": 1935017354
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.bind_familyname` | `string` | 绑定终端组织名称 |
| `items.bind_phone` | `string` | 绑定手机号 |
| `items.bind_sfamily` | `string` | 绑定终端组织id |
| `items.bind_time` | `number` | 绑定时间 |
| `items.car_number` | `string` | 车牌号 |
| `items.distance` | `number` | 里程,单位m |
| `items.familyname` | `string` | 组织名称 |
| `items.groupname` | `string` | 分组名称 |
| `items.imei` | `number` | 设备号 |
| `items.p_distance` | `number` | 行程次数 |
| `items.p_point` | `number` | 停留次数 |
| `items.sfamily` | `string` | 组织id |
| `items.sgid` | `string` | 分组id |
| `items.simei` | `string` | 设备id |
| `items.speed` | `number` | 超速告警次数 |
| `items.time_begin` | `number` | 起始时间戳 |
| `items.time_end` | `number` | 结束时间戳 |
| `items.version` | `string` | 设备型号 |
| `total` | `number` | 总数,只有在不翻页的时候返回 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "589e9d99-94bb-4ac7-bce5-29bc68dce745",
  "items": [
    {
      "bind_familyname": "0465bcdb-20ff-4745-95ca-c3a3fa5e0005",
      "bind_phone": "9b71fa7e-fa98-4870-80d0-806c46a314db",
      "bind_sfamily": "3d6f5f6b-7e1b-46a1-90cf-58836565d5b9",
      "bind_time": 720278422,
      "car_number": "95cbb81f-8ca6-4e25-b2a7-1c49624da4fa",
      "distance": 720278422,
      "familyname": "dd252241-1a2c-4d1d-97a8-312b567372ee",
      "groupname": "a134d420-668c-4cf8-bbc6-7fd0b8d8cc86",
      "imei": 720278422,
      "p_distance": 720278422,
      "p_point": 720278422,
      "sfamily": "885dc017-e168-44cb-9e5a-5ce97e4cdd02",
      "sgid": "5cd6128d-0657-45fb-87ec-94bcef8ecd85",
      "simei": "1d52d888-26f6-488d-8d9c-ba4ff7a4f522",
      "speed": 720278422,
      "time_begin": 720278422,
      "time_end": 720278422,
      "version": "d584692a-9aac-4891-a06d-8889ea339209"
    },
    {
      "bind_familyname": "c7e86d50-d03d-4163-b8f2-2f00c941cadd",
      "bind_phone": "f045dd50-9452-4a98-a58a-2f0ae0eeb512",
      "bind_sfamily": "4c673558-8132-43c5-a29d-d7ad3bcb47ae",
      "bind_time": 720278422,
      "car_number": "5a011830-4898-4957-b2f5-03968fe3e1ef",
      "distance": 720278422,
      "familyname": "d729fea0-9fb2-4eed-b475-24ba2a434763",
      "groupname": "ed5d7f81-dca4-4f10-b38a-270e79a3b9e3",
      "imei": 720278422,
      "p_distance": 720278422,
      "p_point": 720278422,
      "sfamily": "4cda448a-79d1-4a22-9c8b-20f7d8ef3438",
      "sgid": "67ce4514-5b57-41ae-b210-8afb11f3bbd9",
      "simei": "bcff1dc6-15d0-4840-bb3d-96c5c96dab31",
      "speed": 720278422,
      "time_begin": 720278422,
      "time_end": 720278422,
      "version": "81b45d7d-244d-497a-8ff8-62814a865c5c"
    },
    {
      "bind_familyname": "709014b2-6d5f-47a2-9d82-76d80ac703f9",
      "bind_phone": "de66db7a-59be-43ac-b8e0-9a564238e121",
      "bind_sfamily": "5347bd7e-ab6a-4739-a3f8-46ee390d6378",
      "bind_time": 720278422,
      "car_number": "128feffb-e16c-4eed-806a-edc18c3a3a4c",
      "distance": 720278422,
      "familyname": "88bfcd10-d4a7-46aa-82b6-75c2231634b4",
      "groupname": "5880cba7-f629-438c-90ed-a432ef5155fe",
      "imei": 720278422,
      "p_distance": 720278422,
      "p_point": 720278422,
      "sfamily": "2452592b-e879-4ea0-badb-c3bc77ebbc73",
      "sgid": "68314de0-d20d-4beb-8e6f-350adb7ad47a",
      "simei": "de5b6783-b888-4657-9f90-647372405b75",
      "speed": 720278422,
      "time_begin": 720278422,
      "time_end": 720278422,
      "version": "f3be786f-0b2c-4674-90d1-e2cfc6ef0e0e"
    },
    {
      "bind_familyname": "6f641dc2-a440-4b89-854f-ac282fad07e6",
      "bind_phone": "f35357b2-0546-47df-9d76-c814c2ca446f",
      "bind_sfamily": "a2fe1a06-d68c-4824-bda9-a181dee49ca2",
      "bind_time": 720278422,
      "car_number": "e5fe1efe-0735-4bc8-9028-566d45dc7055",
      "distance": 720278422,
      "familyname": "e150c989-7474-4f04-9401-c54373a9900c",
      "groupname": "4ca72910-80da-4fa5-bf7b-c55bc5288ac7",
      "imei": 720278422,
      "p_distance": 720278422,
      "p_point": 720278422,
      "sfamily": "d89bb37d-eb39-4759-8919-6f2ca35c32e0",
      "sgid": "b0fbcad0-f7b4-4b44-9a79-b418aa75763c",
      "simei": "004ab899-6f02-499b-b412-29759ecd490e",
      "speed": 720278422,
      "time_begin": 720278422,
      "time_end": 720278422,
      "version": "f756138c-dd89-44c7-ae49-39accb9cc0df"
    }
  ],
  "total": 720278422
}
```

<a id="summary-OnLineState"></a>
## 在线状态统计 (`OnLineState`)

- 接口 ID：`summary.OnLineState`
- 模块：`summary`
- 用途：在线状态统计
- 原始位置：`source/统计(summary).txt` 第 261 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,OnLineState |
| `module` | `string` | 模块名,summary |
| `params` | `object` | 参数列表 |
| `params.is_get_child` | `boolean` | 是否包含下级的组织数据，默认包含 |
| `params.last_sgid` | `string` | 上一页获取返回最后一条数据的分组id |
| `params.last_simei` | `string` | 上一页获取返回最后一条数据的设备id |
| `params.limit_size` | `number` | 数据的限制条数，默认20条,最高不超过100 |
| `params.sfamily` | `string` | 需要拉取的组织id,如果设备登入，不填,字段sgid,simei,sfamilyid三选一 |
| `params.sgid` | `string` | 需要获取统计的车组,如果设备登入，不填,字段sgid,simei,sfamilyid三选一 |
| `params.simei` | `array` | 统计的设备id，如果设备登入，不填,必须是同个组织下的设备.字段sgid,simei,sfamilyid三选一 |
| `params.state` | `string` | 在线状态,默认不传，拉取全部<br>e_line_down 不在线<br>e_line_on 在线<br>e_line_sleep 睡眠 |

### 请求示例

```json
{
  "module": "summary",
  "func": "OnLineState",
  "params": {
    "is_get_child": false,
    "last_sgid": "35c534a3-dd48-4c74-8cf0-380942d1a045",
    "last_simei": "06bf6e8d-c66c-47e7-9c8f-c03d9e2e2bff",
    "limit_size": 2107067229,
    "sfamily": "fbd43308-c41d-49c1-9a6b-3ec97c399142",
    "sgid": "e62416f8-09e1-439f-8bf9-4c19d28e74c9",
    "simei": [
      "b667e0ed-45a5-4068-8284-0ad6aab5ef00",
      "a2c64aae-a3e8-4f49-a271-de95931f5f90"
    ],
    "state": "e_line_down|e_line_on|e_line_sleep"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.bind_familyname` | `string` | 绑定终端组织名称 |
| `items.bind_phone` | `string` | 绑定手机号 |
| `items.bind_sfamily` | `string` | 绑定终端组织id |
| `items.bind_time` | `number` | 绑定时间 |
| `items.car_number` | `string` | 车牌号 |
| `items.familyname` | `string` | 组织名称 |
| `items.groupname` | `string` | 分组名称 |
| `items.imei` | `number` | 设备号 |
| `items.sfamily` | `string` | 组织id |
| `items.sgid` | `string` | 分组id |
| `items.simei` | `string` | 设备id |
| `items.state` | `string` | 在线状态<br>e_line_down 不在线<br>e_line_on 在线<br>e_line_sleep 睡眠 |
| `items.state_begin_time` | `number` | 当前状态的起始时间 |
| `items.version` | `string` | 设备型号 |
| `total` | `number` | 总数,只有在不翻页的时候返回 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "0089686c-444e-45d2-b9ef-f575487dc3ea",
  "items": [
    {
      "bind_familyname": "b66a01ef-5a2d-400f-9116-20615a8c9dbe",
      "bind_phone": "5d27a421-c404-4946-8e69-be7a9f7b9b2f",
      "bind_sfamily": "2ccb0e47-38fd-4095-b4d0-91ad29558507",
      "bind_time": 1370122621,
      "car_number": "2168d776-0c3a-440e-bc3e-f65230c9f114",
      "familyname": "ccf82d75-f4c4-42bc-8c65-e009a4f603a5",
      "groupname": "a38fe605-a087-4b5a-8bb5-121f48bdac0a",
      "imei": 1370122621,
      "sfamily": "47a8df35-8652-4692-bee5-fe1476d9a5dc",
      "sgid": "f5fd61f5-c814-45d0-94b3-ad713f3d75a1",
      "simei": "9d6acdff-5c9e-4ff2-84bb-4231808dbafa",
      "state": "e_line_down|e_line_on|e_line_sleep",
      "state_begin_time": 1370122621,
      "version": "71b5b1f2-4757-4eb3-a5f0-1e3bc8acc3f0"
    },
    {
      "bind_familyname": "53f7be50-5479-4d55-b957-3f37b841341b",
      "bind_phone": "0adc1271-f782-423a-8174-74ecdf2e267e",
      "bind_sfamily": "67be8938-5947-4cee-99bf-0317f5c009e2",
      "bind_time": 1370122621,
      "car_number": "a2d752a5-fe1f-442a-99df-86792026ca68",
      "familyname": "d914460d-de76-48d9-810b-f543b517090b",
      "groupname": "8221b8ef-91dd-4137-a5e3-9bf0d21b6ec7",
      "imei": 1370122621,
      "sfamily": "e3730928-2202-4d90-8796-1feb20884cbb",
      "sgid": "3b98c36a-b5f9-46bb-a1c5-cbb7af15916b",
      "simei": "43e5000f-51fe-472c-9da2-b245e8699c30",
      "state": "e_line_down|e_line_on|e_line_sleep",
      "state_begin_time": 1370122621,
      "version": "68b33851-4611-47b4-9bf7-da883b2cd5ab"
    },
    {
      "bind_familyname": "80479df1-eb03-4690-85dc-ed62dc8729a2",
      "bind_phone": "17075527-5ba4-4ae8-a9ad-ba7aeb683291",
      "bind_sfamily": "5c0b04e1-e2f6-4416-bcfe-4733f02ee0dc",
      "bind_time": 1370122621,
      "car_number": "ecc5bd71-7928-4250-bdcf-98a048c83f88",
      "familyname": "aa0b0461-ab65-4b5f-ad3b-4573563c2147",
      "groupname": "1122ee6e-6d02-4f8d-bf96-8002bb5eba10",
      "imei": 1370122621,
      "sfamily": "085ae4f0-2813-4c29-83f7-b134c578b621",
      "sgid": "8cae1936-41af-4326-8888-80917a4efad8",
      "simei": "b5551126-2b42-4ad5-8e33-f653e533f755",
      "state": "e_line_down|e_line_on|e_line_sleep",
      "state_begin_time": 1370122621,
      "version": "f9292319-22cb-4fa5-9815-b3d2c9234355"
    }
  ],
  "total": 1370122621
}
```

<a id="summary-PlatformVip"></a>
## 平台服务 (`PlatformVip`)

- 接口 ID：`summary.PlatformVip`
- 模块：`summary`
- 用途：平台服务
- 原始位置：`source/统计(summary).txt` 第 409 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,PlatformVip |
| `module` | `string` | 模块名,summary |
| `params` | `object` | 参数列表 |
| `params.begin_use` | `boolean` | 是否激活，默认不传表示全部，false表示未激活 |
| `params.expire` | `number` | 过期时间，默认0，查询全部;设置-1，已经过期;设置7，查询的是7天内过期的设备列表 |
| `params.is_get_child` | `boolean` | 是否包含下级的组织数据，默认包含 |
| `params.last_sgid` | `string` | 上一页获取返回最后一条数据的分组id |
| `params.last_simei` | `string` | 上一页获取返回最后一条数据的设备id |
| `params.limit_size` | `number` | 数据的限制条数，默认20条,最高不超过100 |
| `params.sfamily` | `string` | 需要拉取的组织id,如果设备登入，不填,字段sgid,simei,sfamilyid三选一 |
| `params.sgid` | `string` | 需要获取统计的车组,如果设备登入，不填,字段sgid,simei,sfamilyid三选一 |
| `params.simei` | `array` | 统计的设备id，如果设备登入，不填,必须是同个组织下的设备.字段sgid,simei,sfamilyid三选一 |

### 请求示例

```json
{
  "module": "summary",
  "func": "PlatformVip",
  "params": {
    "begin_use": true,
    "expire": 1380734284,
    "is_get_child": true,
    "last_sgid": "5a0da670-21be-48ca-8f39-73c74f8a142a",
    "last_simei": "1de9b7f8-975e-430f-8333-ec9f70c97d3a",
    "limit_size": 1380734284,
    "sfamily": "b8179017-c012-447f-b0f5-5f774da0ef51",
    "sgid": "c93f5667-ca58-48d9-bd61-ff76f6582562",
    "simei": [
      "8f17d06a-19ce-4290-824f-2866faafda5f",
      "dc7ba537-55dd-46e2-a3fd-19cb02fc8cd9",
      "ed62267d-0cb0-4370-89bf-f73195e9e4cb"
    ]
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.bind_familyname` | `string` | 绑定终端组织名称 |
| `items.bind_phone` | `string` | 绑定手机号 |
| `items.bind_sfamily` | `string` | 绑定终端组织id |
| `items.bind_time` | `number` | 绑定时间 |
| `items.car_number` | `string` | 车牌号 |
| `items.end_dev_time` | `number` | 到期时间 |
| `items.familyname` | `string` | 组织名称 |
| `items.groupname` | `string` | 分组名称 |
| `items.imei` | `number` | 设备号 |
| `items.last_on_line` | `number` | 最后在线时间,0表示从没在线过 |
| `items.sfamily` | `string` | 组织id |
| `items.sgid` | `string` | 分组id |
| `items.simei` | `string` | 设备id |
| `items.start_dev_time` | `number` | 激活时间 |
| `items.version` | `string` | 设备型号 |
| `total` | `number` | 总数,只有在不翻页的时候返回 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "e0421b0c-fb63-46b3-896c-e511beb419cd",
  "items": [
    {
      "bind_familyname": "8c40c1c1-d1b9-4742-be99-7a405513dcc6",
      "bind_phone": "c88d2fd8-6c02-4368-b960-9b2962e1259c",
      "bind_sfamily": "d14fe9cf-2cb7-4da5-8491-78ad8898c0fb",
      "bind_time": 163420778,
      "car_number": "4beb074f-fac8-4c57-b959-4cc68315a3aa",
      "end_dev_time": 163420778,
      "familyname": "500cfde7-8fde-4367-852f-a47a82ad41a2",
      "groupname": "04497357-13f3-45d4-b902-6aa0099e6787",
      "imei": 163420778,
      "last_on_line": 163420778,
      "sfamily": "01dd11f7-3820-4888-b99f-4a63dded8747",
      "sgid": "0adf7240-aa27-4200-b53c-6b0ba6100904",
      "simei": "37604a7b-b633-4a29-8967-42140775b47f",
      "start_dev_time": 163420778,
      "version": "dfe59cd0-aa92-4eab-ad75-02893413b707"
    },
    {
      "bind_familyname": "530b9390-3f75-4d7f-af20-a52a55e35ef7",
      "bind_phone": "a1d35d22-33c6-46fd-b5d7-9ffc797da70f",
      "bind_sfamily": "388624dd-4dd7-4867-91e3-82f1b80791cb",
      "bind_time": 163420778,
      "car_number": "8df5ae53-aad2-4b6e-b80f-2c62d373cf4a",
      "end_dev_time": 163420778,
      "familyname": "a40bf438-271f-4445-9e46-9d32bd5ce4b0",
      "groupname": "b91f6722-0e88-4b3a-89ca-c93f7dcb7888",
      "imei": 163420778,
      "last_on_line": 163420778,
      "sfamily": "4707ece8-9041-4f3f-bda2-ad508e3316aa",
      "sgid": "7b2ad2b1-dd14-47e2-a241-56a7c0743c90",
      "simei": "079125f7-393e-46ff-84d0-714c8a78cc84",
      "start_dev_time": 163420778,
      "version": "ff5c8d23-b4eb-4138-9d9b-e4add042f931"
    }
  ],
  "total": 163420778
}
```

<a id="summary-GetSimPayDay"></a>
## 获取sim支付日统计 (`GetSimPayDay`)

- 接口 ID：`summary.GetSimPayDay`
- 模块：`summary`
- 用途：获取sim支付日统计
- 原始位置：`source/统计(summary).txt` 第 543 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetSimPayDay |
| `module` | `string` | 模块名,summary |
| `params` | `object` | 参数列表 |
| `params.date` | `array` | 日期,格式20220531,限制7个以内 |
| `params.is_get_child` | `boolean` | 是否包含下级的组织数据，默认包含 |
| `params.sfamily` | `string` | 需要拉取的组织id,如果设备登入，不填 |

### 请求示例

```json
{
  "module": "summary",
  "func": "GetSimPayDay",
  "params": {
    "date": [
      493661516,
      493661516,
      493661516
    ],
    "is_get_child": true,
    "sfamily": "ba638ce1-a929-49b0-a019-e79a28246dc0"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.att_account` | `number` | 日充值叠加包套餐金额 |
| `items.att_count` | `number` | 日充值叠加包套餐笔数 |
| `items.base_account` | `number` | 日充值基础套餐金额 |
| `items.base_count` | `number` | 日充值基础套餐笔数 |
| `items.date` | `number` | 日期 |
| `items.msg_account` | `number` | 日充值短信套餐金额 |
| `items.msg_count` | `number` | 日充值短信套餐笔数 |
| `items.total_account` | `number` | 日充值金额 |
| `items.total_count` | `number` | 日充值笔数 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "dd789f92-5890-44d9-90c6-186b314b72b8",
  "items": [
    {
      "att_account": 1720707906,
      "att_count": 1720707906,
      "base_account": 1720707906,
      "base_count": 1720707906,
      "date": 1720707906,
      "msg_account": 1720707906,
      "msg_count": 1720707906,
      "total_account": 1720707906,
      "total_count": 1720707906
    },
    {
      "att_account": 1720707906,
      "att_count": 1720707906,
      "base_account": 1720707906,
      "base_count": 1720707906,
      "date": 1720707906,
      "msg_account": 1720707906,
      "msg_count": 1720707906,
      "total_account": 1720707906,
      "total_count": 1720707906
    },
    {
      "att_account": 1720707906,
      "att_count": 1720707906,
      "base_account": 1720707906,
      "base_count": 1720707906,
      "date": 1720707906,
      "msg_account": 1720707906,
      "msg_count": 1720707906,
      "total_account": 1720707906,
      "total_count": 1720707906
    },
    {
      "att_account": 1720707906,
      "att_count": 1720707906,
      "base_account": 1720707906,
      "base_count": 1720707906,
      "date": 1720707906,
      "msg_account": 1720707906,
      "msg_count": 1720707906,
      "total_account": 1720707906,
      "total_count": 1720707906
    }
  ]
}
```

<a id="summary-GetSimPayMonth"></a>
## 获取sim支付月统计 (`GetSimPayMonth`)

- 接口 ID：`summary.GetSimPayMonth`
- 模块：`summary`
- 用途：获取sim支付月统计
- 原始位置：`source/统计(summary).txt` 第 654 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,GetSimPayMonth |
| `module` | `string` | 模块名,summary |
| `params` | `object` | 参数列表 |
| `params.date` | `array` | 日期,格式202205,目前限制1个以内 |
| `params.is_get_child` | `boolean` | 是否包含下级的组织数据，默认包含 |
| `params.sfamily` | `string` | 需要拉取的组织id,如果设备登入，不填 |

### 请求示例

```json
{
  "module": "summary",
  "func": "GetSimPayMonth",
  "params": {
    "date": [
      511590628,
      511590628,
      511590628,
      511590628
    ],
    "is_get_child": true,
    "sfamily": "dc5b32e8-02e6-44e6-b67b-3239c2851dda"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `items` | `array` |  |
| `items.date` | `number` | 日期 |
| `items.total_account` | `number` | 月充值金额 |
| `items.total_count` | `number` | 月充值笔数 |

### 返回示例

```json
{
  "errcode": 0,
  "error_message": "6b5a9d7d-0a39-478c-abec-dcd25f809f32",
  "items": [
    {
      "date": 658376210,
      "total_account": 658376210,
      "total_count": 658376210
    }
  ]
}
```

<a id="summary-FamilyDevState"></a>
## 组织下设备状态统计 (`FamilyDevState`)

- 接口 ID：`summary.FamilyDevState`
- 模块：`summary`
- 用途：组织下设备状态统计
- 原始位置：`source/统计(summary).txt` 第 715 行
- URL 参数：sid(会话登入id，登入成功之后由服务器返回)

### 请求字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `func` | `string` | 接口名称,FamilyDevState |
| `module` | `string` | 模块名,summary |
| `params` | `object` | 参数列表 |
| `params.is_get_child` | `boolean` | 是否包含下级的组织数据，默认包含 |
| `params.sfamily` | `string` | 需要拉取的组织id |

### 请求示例

```json
{
  "module": "summary",
  "func": "FamilyDevState",
  "params": {
    "is_get_child": true,
    "sfamily": "dd3e71fb-ee23-443a-9604-8df1acb80f77"
  }
}
```

### 返回字段

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `down` | `object` | 离线设备数 |
| `down.more_month` | `number` | 超过1个月 |
| `down.one_day` | `number` | 1天内 |
| `down.one_month` | `number` | 7天到1个月 |
| `down.seven_day` | `number` | 3-7天内 |
| `down.three_day` | `number` | 1-3天内 |
| `errcode` | `uint64` | 错误码，成功返回0 |
| `error_message` | `string` | 错误提示语（前端可直接展示） |
| `expire` | `object` | 即将过期的设备 |
| `expire.expire` | `number` | 已过期的设备 |
| `expire.seven_day` | `number` | 7天后过期的设备 |
| `expire.sixty_day` | `number` | 60天后过期的设备 |
| `online` | `object` | 在线设备数 |
| `online.run` | `number` | 运动设备的总数 |
| `online.sleep` | `number` | 静止设备的总数 |
| `total` | `number` | 总数 |
| `unexcited` | `object` | 未激活 |
| `unexcited.more_year` | `number` | 超过2年 |
| `unexcited.one_year` | `number` | 6-12个月 |
| `unexcited.six_month` | `number` | 3-6个月 |
| `unexcited.three_month` | `number` | 三个月内 |
| `unexcited.two_year` | `number` | 1-2年 |

### 返回示例

```json
{
  "errcode": 0,
  "down": {
    "more_month": 1144898686,
    "one_day": 1144898686,
    "one_month": 1144898686,
    "seven_day": 1144898686,
    "three_day": 1144898686
  },
  "error_message": "2e8fb287-32b4-4a59-ac2f-8ada10d4e673",
  "expire": {
    "expire": 1144898686,
    "seven_day": 1144898686,
    "sixty_day": 1144898686
  },
  "online": {
    "run": 1144898686,
    "sleep": 1144898686
  },
  "total": 1144898686,
  "unexcited": {
    "more_year": 1144898686,
    "one_year": 1144898686,
    "six_month": 1144898686,
    "three_month": 1144898686,
    "two_year": 1144898686
  }
}
```


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
