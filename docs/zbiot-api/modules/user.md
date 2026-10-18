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
