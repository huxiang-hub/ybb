package com.yb.socketio;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/31 9:40
 */
public interface Const {
    // ======================CMC Start =======================
    /**
     * COSMOSOURCE:（变量描述：cosmosource）
     */
    String COSMOSOURCE = "cosmosource";
    /**
     * CMC:（变量描述：cmc）
     */
    String CMC = "cmc";
    /**
     * UTF_8:（变量描述：UTF-8）
     */
    String UTF_8 = "UTF-8";
    /**
     * TOKEN:（变量描述：token）
     */
    String TOKEN = "token";
    /**
     * TOKEN:（变量描述：mene）
     */
    String MENE = "mene";
    /**
     * TOKEN:（变量描述：资源ID 1000）
     */
    String NID = "1000";
    /**
     * TOKEN:（变量描述：objType allC）
     */
    String OBJTYPE = "allC";

    /**
     * DOT：（变量描述：点）
     */
    String DOT = ".";
    /**
     * SLASH：（变量描述：斜杠）
     */
    String SLASH = "/";
    /**
     * BACK_SLASH：（变量描述：反斜杠）
     */
    String BACK_SLASH = "\\";
    /**
     * semicolon：（变量描述：分号）
     */
    String SEMICOLON = ";";
    /**
     * colon: （变量描述：冒号）
     */
    String COLON = ":";
    /**
     * COMMA：（变量描述：逗号）
     */
    String COMMA = ",";
    /**
     * SHIFT_15：（变量描述：<）
     */
    String LESS_THAN = "<";
    /**
     * SINGLE_QUOTE：（变量描述：单引号）
     */
    String SINGLE_QUOTE = "'";
    /**
     * PAH_E：（变量描述：%e）
     */
    String PAH_E = "%e";
    /**
     * SHIFT_10：（变量描述：`）
     */
    String SHIFT_10 = "`";
    /**
     * SHIFT_11：（变量描述：~）
     */
    String SHIFT_11 = "~";
    /**
     * SHIFT_1：（变量描述：!）
     */
    String SHIFT_1 = "!";
    /**
     * SHIFT_2：（变量描述：@）
     */
    String SHIFT_2 = "@";
    /**
     * SHIFT_3：（变量描述：#）
     */
    String SHIFT_3 = "#";
    /**
     * SHIFT_4：（变量描述：$）
     */
    String SHIFT_4 = "$";
    /**
     * SHIFT_5：（变量描述：%）
     */
    String SHIFT_5 = "%";
    /**
     * SHIFT_6：（变量描述：^）
     */
    String SHIFT_6 = "^";
    /**
     * SHIFT_7：（变量描述：&）
     */
    String SHIFT_7 = "&";
    /**
     * SHIFT_8：（变量描述：*）
     */
    String SHIFT_8 = "*";
    /**
     * SHIFT_9：（变量描述：(）
     */
    String SHIFT_9 = "(";
    /**
     * SHIFT_0：（变量描述：)）
     */
    String SHIFT_0 = ")";
    /**
     * SHIFT_12：（变量描述：-）
     */
    String SHIFT_12 = "-";
    /**
     * SHIFT_13：（变量描述：=）
     */
    String SHIFT_13 = "=";
    /**
     * SHIFT_14：（变量描述：_）
     */
    String SHIFT_14 = "_";
    /**
     * SHIFT_15：（变量描述：+）
     */
    String SHIFT_15 = "+";
    /**
     * NAME：（变量描述：NAME）
     */
    String NAME = "name";
    /**
     * FIELD：（变量描述：FIELD）
     */
    String FIELD = "field";
    /**
     * TABLE_NAME：（变量描述：TABLE_NAME）
     */
    String TABLE_NAME = "TABLE_NAME";
    /**
     * VIEW_NAME：（变量描述：VIEW_NAME）
     */
    String VIEW_NAME = "VIEW_NAME";
    /**
     * COLUMN_NAME：（变量描述：COLUMN_NAME）
     */
    String COLUMN_NAME = "COLUMN_NAME";

    /**
     * EMPTY:（变量描述：空）
     */
    String EMPTY = "";

    /**
     * COMMA：（变量描述：字符串0）
     */
    String ZERO = "0";
    /**
     * COMMA：（变量描述：字符串1）
     */
    String ONE = "1";
    /**
     * COMMA：（变量描述：字符串2）
     */
    String TWO = "2";
    /**
     * COMMA：（变量描述：字符串3）
     */
    String THREE = "3";
    /**
     * COMMA：（变量描述：字符串4）
     */
    String FOUR = "4";
    /**
     * COMMA：（变量描述：字符串5）
     */
    String FIVE = "5";
    /**
     * COMMA：（变量描述：字符串6）
     */
    String SIX = "6";
    /**
     * COMMA：（变量描述：字符串7）
     */
    String SEVEN = "7";
    /**
     * COMMA：（变量描述：字符串8）
     */
    String EIGHT = "8";
    /**
     * COMMA：（变量描述：字符串9）
     */
    String NINE = "9";
    /**
     * COMMA：（变量描述：字符串10）
     */
    String TEN = "10";
    /**
     * COMMA：（变量描述：字符串15）
     */
    String FIFTEEN = "15";
    /**
     * COMMA：（变量描述：字符串20）
     */
    String TWENTY = "20";
    /**
     * STATUS：（变量描述：status）
     */
    String STATUS = "status";
    /**
     * STATUS_UNDERWAY：（变量描述：正在进行）
     */
    String STATUS_UNDERWAY = "正在进行";
    /**
     * STATUS_OVER：（变量描述：结束）
     */
    String STATUS_OVER = "结束";
    /**
     * MSG：（变量描述：msg）
     */
    String MSG = "msg";
    /**
     * DATA：（变量描述：data）
     */
    String DATA = "data";
    /**
     * SUCCESS：（变量描述：success）
     */
    String SUCCESS = "success";
    /**
     * ERROR：（变量描述：error）
     */
    String ERROR = "error";
    /**
     * FAILED：（变量描述：failed）
     */
    String FAILED = "failed";
    /**
     * PROPERTY：（变量描述：property）
     */
    String PROPERTY = "property";
    /**
     * STYLE：（变量描述：style）
     */
    String STYLE = "style";
    /**
     * SYSTEM：（变量描述：system）
     */
    String SYSTEM = "system";
    /**
     * CUSTOM：（变量描述：custom）
     */
    String CUSTOM = "custom";
    /**
     * CSS：（变量描述：.css）
     */
    String CSS = ".css";
    /**
     * CSS：（变量描述：消息为空null_count）
     */
    String NULL_COUNT = "null_count";
    /**
     * CSS：（变量描述：.fail_count）
     */
    String FAIL_COUNT = "fail_count";
    /**
     * CSS：（变量描述：.success_count）
     */
    String SUCCESS_COUNT = "success_count";

// ======================CMC End =======================

// ====================== Session Start =======================
    /**
     * USER:（变量描述：User对象）
     */
    String USER = "user";
    /**
     * USERNAME:（变量描述：用户名）
     */
    String USERNAME = "username";

// ====================== Session End =======================

// ====================== Other Start =======================

    String DB_MYSQL = "mysql";
    String DB_ORACLE = "oracle";
    String DB_SQLSERVER = "sqlserver";

    /**
     * TIME_10000:（变量描述：防刷时间）
     */
    int TIME_10000 = 10000;

    /**
     * TIME_10000:（变量描述：防刷时间）
     */
    int TIME_5000 = 5000;
    /**
     * HTML_INDEX:（变量描述：欢迎页）
     */
    String HTML_INDEX = "index.html";
    /**
     * HTML_403:（变量描述：无权限访问页）
     */
    String HTML_403 = "/Unauthorized3.html";
    /**
     * HTML_404:（变量描述：路径错误页面）
     */
    String HTML_404 = "/NotFound.html";
    /**
     * HTML_500:（变量描述：服务器异常页面）
     */
    String HTML_500 = "/InternalServerError.html";
    /**
     * HTML_503:（变量描述：频繁刷新页面）
     */
    String HTML_503 = "/ServiceUnavailable.html";
    /**
     * HTML_404:（变量描述：路径错误页面）
     */
    String HTML_SESSIONTIMEOUT = "/SessionTimeout.html";
    /**
     * LAST_VISIT_TIME:（变量描述：最后访问时间）
     */
    String LAST_VISIT_TIME = "lastVisitTime";
    /**
     * CALLBACK:（变量描述：回调）
     */
    String CALLBACK = "callback";

/**
 * KEYPREFIX:（变量描述：Redis key前缀）
 */
//String KEYPREFIX = PropertyUtil.getStr("redis.keyPrefix");

    /**
     * FWATERM:（变量描述：文字水印）
     */
    String FWATERM = "宇动源";

    /**
     * IWATERM:（变量描述：图片水印）
     */
    String IWATERM = "";
    String RIGHT_EDIT = "fwp_edit";
// ====================== Other End =======================
}
