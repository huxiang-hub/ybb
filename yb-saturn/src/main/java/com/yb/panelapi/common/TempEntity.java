package com.yb.panelapi.common;

import lombok.Data;

import java.util.Date;

/**
 *  机台操作发送临时小的实体
 * @author by SUMMER
 * @date 2020/3/29.
 */
@Data
public class TempEntity {

    //工单id
    private Integer sdId;
    //订单批次id
    private Integer wbId;
    //机台操作开始时间
    private Date startTime;
    //机台操作间隔时间
    private Double duration;
    //订单名称
    private String orderName;
}
