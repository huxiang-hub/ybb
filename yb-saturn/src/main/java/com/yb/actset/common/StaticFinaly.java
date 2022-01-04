package com.yb.actset.common;

public  class  StaticFinaly {

    public final static int CHECK_ING = 1; //待审核
    public final static int CHECK_AGREE = 2; //已同意
    public final static int CHECK_DISAGREE = 3; //拒绝
    public final static int CHECK_TIMEOUT = 4;//审核超时
    public final static int IS_STATUS = 1;//状态启用
    public final static int NOTIS_STATUS = 0;//状态禁用
    public final static String AS_TYPE_ORDER = "A";//订单
    public final static String AS_TYPE_PRODUCT = "B";//产品
    public final static String SEND_TO_MACHINE = "1";//审核成功下发机台
    public final static String SEND_TO_MACHINE_FAI = "7";//审核失败

}
