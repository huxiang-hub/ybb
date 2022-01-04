package com.yb.common.constant;

/**
 * @author :   my
 * date:     2020-07-25
 * description: 排产单按钮枚举
 */
public class LocalEnum {

    public enum WorkbatchOrdlinkStatus {
        //下发
        ISSUED("1", "下发"),
        //挂起
        HANG("4", "挂起"),
        //废弃
        DISCARD("5", "废弃"),
        //驳回
        DOWN("6", "驳回"),
        //排产
        SCHEDULING("7", "排产"),

        NOSCHEDULING("9", "未排完");

        WorkbatchOrdlinkStatus(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        String type;
        String desc;

        public String getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum RuleSettingCondition {
        LIKE(" like ", "包含"),
        NOT_LIKE(" not like ", "不包含"),
        Right_LIKE(" % ", "以开头"),
        EQUAL(" = ", "等于"),
        GREATER_THAN(" > ", "大于"),
        LESS_THAN(" < ", "小于"),
        GREATER_THAN_OR_EQUAL_TO(" >= ", "大于等于"),
        LESS_THAN_OR_EQUAL_TO(" <= ", "小于等于"),
        BETWEEN(" between ", "范围");

        RuleSettingCondition(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        String type;
        String desc;

        public String getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }
    }


    public enum RuleSettingType {
        AND(" and ", "并集"),
        OR(" or ", "或"),
        OTHER(" ", "其他"),
        STOP("  ", "停止");

        RuleSettingType(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        String type;
        String desc;

        public String getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }
    }


    public enum StopListType {
        GREATER_THAN(">", "大于"),
        LESS_THAN("<", "小于");

        StopListType(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        String type;
        String desc;

        public String getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }
    }
}

