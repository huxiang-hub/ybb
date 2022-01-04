package com.yb.common.constant;

/**
 *  订单状态
 * @author by SUMMER
 * @date 2020/4/15.
 */
public enum OrderStatus {

    STATUS_WAITING("1","待接单"),
    STATUS_PRODUCTION("2","正在生产"),
    STATUS_COMPLETE("3","已完成");

    String type;
    String desc;

    OrderStatus(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

}
