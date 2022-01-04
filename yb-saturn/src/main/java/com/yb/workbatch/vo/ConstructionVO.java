package com.yb.workbatch.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConstructionVO implements Serializable {

    /*产品规格*/
    private String pdSize;
    /*产品编号*/
    private String pdNo;
    /*订单数量*/
    private Integer odCount;
    /*部件名称*/
    private String ptName;
    /*材料名称*/
    private String materialName;
    /*工序说明*/
    private String prDes;
    /*客户产品编号*/
    private String productNo;
    /*本工序*/
    private String prName;
    /*下工序*/
    private String downPorcess;
    /*下工序说明*/
    private String downPorcessDes;
    /*此板数量*/
    private Integer plateNumber;
    /*废品数量*/
    private Integer wasteNum;
    /*工序应交货数*/
    private Integer prPlanNum;
    /*版号*/
    private String plateNo;
    /*贴次*/
    private Integer onTime;
    /*客户产品批号*/
    private String productBatch;
    /*生产批号*/
    private String batchNumber;
    /*责任人*/
    private String functionary;
    /*质检员*/
    private String inspector;
    /*备注*/
    private String remark;
    /*施工单号*/
    private String wbNo;
    /*订单交期*/
    private String finalTime;
    /*客户名称*/
    private String cmName;
    /*订单编号*/
    private String odNo;
    /*产品名称*/
    private String pdName;
    /*工艺流程*/
    private String prRoute;
    /*-----------------*/
    /*操作人*/
    private String userName;
    /*设备名称*/
    private String maName;
    /*部门名称*/
    private String dpName;
    /*排产日期*/
    private String sdDate;
}
