package com.yb.actset.vo;

import java.util.Date;

public class StoregeCheckModelVO{

    /***
     * yb_actset_checklog 表中的id
     */
    private Integer logId;
    /***
     * yb_actset_checklog 审核时间
     */
    private Date checkTime;
    /***
     * yb_actset_checklog 审核结果
     */
    private String result;
    /***
     * yb_actset_checklog 审核的状态
     */
    private Integer status;

}
