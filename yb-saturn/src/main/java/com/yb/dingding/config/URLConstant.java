package com.yb.dingding.config;

public class URLConstant {
    /**
     * 钉钉网关gettoken地址
     */
    public static final String URL_GET_TOKKEN = "https://oapi.dingtalk.com/gettoken";

    /**
     *获取用户在企业内userId的接口URL
     */
    public static final String URL_GET_USER_INFO = "https://oapi.dingtalk.com/user/getuserinfo";

    /**
     *获取用户姓名的接口url
     */
    public static final String URL_USER_GET = "https://oapi.dingtalk.com/user/get";
    /**
     *获取所有部门信息url
     */
    public static final String URL_DEPARTMENT_LIST = "https://oapi.dingtalk.com/department/list";
    /**
     *获取每个部门下的人员信息集url
     */
    public static final String URL_USER_LISTBYPAGE = "https://oapi.dingtalk.com/user/listbypage";
    /**
     *获取钉钉用户的用户名和userid的list的url
     */
    public static final String URL_USER_SIMPLELIAT = "https://oapi.dingtalk.com/user/simplelist";
    /**
     * 获取第三方应用凭证
     */
    public static final String URL_GET_SUITE_TOKEN = "https://oapi.dingtalk.com/service/get_suite_token";
    /**
     * 获取企业授权凭证(第三方企业应用)
     */
    public static final String URL_GET_CORP_TOKEN = "https://oapi.dingtalk.com/service/get_corp_token?suite_access_token=";
    /**
     * 推送通知消息url
     */
    public static final String ASYNCSEND_V2 = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";
    /**
     * 群消息发送url
     */
    public static final String CHAT_SEND = "https://oapi.dingtalk.com/chat/send";
    /**
     * 钉钉导入考勤打卡记录
     */
    public static final String ATTENDANCE_RECORD_UPLOAD = "https://oapi.dingtalk.com/topapi/attendance/record/upload";
    /**
     * 钉钉发起审核流程url
     */
    public static final String PROCESSINSTANCE_CREATE = "https://oapi.dingtalk.com/topapi/processinstance/create";
    /**
     * 钉钉根据流程模板获取模板code
     */
    public static final String GET_BY_NAME = "https://oapi.dingtalk.com/topapi/process/get_by_name";
    /**
     * 获取单个审批实例详情url
     */
    public static final String PROCESSINSTANCE_GET = "https://oapi.dingtalk.com/topapi/processinstance/get";
    /**
     * 执行审批操作带附件url
     */
    public static final String PROCESS_INSTANCE_EXECUTE = "https://oapi.dingtalk.com/topapi/process/instance/execute";
    /**
     * 执行审批操作带附件url
     */
    public static final String CHAT_CREATE = "https://oapi.dingtalk.com/chat/create";
}
