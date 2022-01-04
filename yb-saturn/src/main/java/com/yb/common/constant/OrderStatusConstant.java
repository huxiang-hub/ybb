package com.yb.common.constant;

/**
 * @author by SUMMER
 * @date 2020/3/17.
 */
public class OrderStatusConstant {
    //待接单
    public static final String STATUS_WAITING = "1";
    //正在生产
    public static final String STATUS_PRODUCTION = "2";
    //已完成
    public static final String STATUS_COMPLETE = "3";

    //待接单
    public static final Integer RUN_STATUS_WAITING = 0;
    //生产中
    public static final Integer RUN_STATUS_PRODUCTION = 1;
    //已完成
    public static final Integer RUN_STATUS_COMPLETE = 2;
    //结束未上报
    public static final Integer RUN_STATUS_NOREPORT = 3;
    //未完成，已上报
    public static final Integer RUN_STATUS_REPORT = 4;
}
