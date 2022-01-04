package com.yb.panelapi.waste.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ReportWaste {

    private Integer id;
    //上报id
    private Integer bfIid;
    //订单id
    private Integer orderId;
    //设备id
    private Integer maId;
    //废品类型名称
    private String wasteType;
    //废品类型id
    private Integer wasteTypeId;
    //数量
    private Integer wasteNum;
    //工序id
    private Integer processId;
    //上报时间
    private Date reportTime;


}