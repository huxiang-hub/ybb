package org.springblade.common.constant;

public class FeiShuURLConstant {
    /**
     * TENANT_ACCESS_TOKEN
     */
    public static final String TENANT_ACCESS_TOKEN = "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal/";
    /**
     * APP_ACCESS_TOKEN
     */
    public static final String APP_ACCESS_TOKEN = "https://open.feishu.cn/open-apis/auth/v3/app_access_token/internal/";
    /**
     * 飞书获取通讯录授权范围
     */
    public static final String SCOPE_GET = "https://open.feishu.cn/open-apis/contact/v1/scope/get";
    /**
     * 飞书批量获取部门详情
     */
    public static final String DEPARTMENT_DETAIL_BATCH_GET = "https://open.feishu.cn/open-apis/contact/v1/department/detail/batch_get";
    /**
     * 飞书获取部门下的用户详情
     */
    public static final String USER_DETAIL_LIST = "https://open.feishu.cn/open-apis/contact/v1/department/user/detail/list";
    /**
     * 飞书登录
     */
    public static final String ACCESS_TOKEN = "https://open.feishu.cn/open-apis/authen/v1/access_token";
    /**
     * 获取飞书打卡流水url
     */
    public static final String GET_USERS_CHECK_LIST_RECORDS  = "https://time.clockin.biz/attendance/open-api/getUsersCheckListRecords";
    /**
     * 导入飞书打卡流水url
     */
    public static final String IMPORT_USER_CHECK_RECORDS  = "https://time.clockin.biz/attendance/open-api/importUserCheckRecords";
    /**
     * 导入飞书打卡流水url
     */
    public static final String SEND  = "https://open.feishu.cn/open-apis/message/v4/send/";

}
