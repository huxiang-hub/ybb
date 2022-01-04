package com.yb.common.constant;

/**
 * 订单的生产状态
 * @author by SUMMER
 * @date 2020/4/15.
 */
public enum OrderRunStatus {

    RUN_STATUS_WAITING(1, "待接单"),
    RUN_STATUS_PRODUCTION(2, "生产中"),
    RUN_STATUS_COMPLETE(3, "已完成"),
    RUN_STATUS_NOREPORT(4, "结束未上报"),
    RUN_STATUS_REPORT(5, "未完成，已上报");

    Integer type;
    String desc;

    OrderRunStatus(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
