package com.yb.workbatch.vo;

import com.yb.workbatch.entity.WorkbatchOrdlink;
import lombok.Data;

@Data
public class PlanProduceModelVO extends WorkbatchOrdlink {
    /**
     * 批次编号
     */
    private String batchNo;
    /**
     * 订单名称
     */
    private String odName;
    /**
     * 班次
     */
    private String ckName;
    /**
     * 设备名称
     */
    private String maName;
    /**
     * 审核记录表中的Id
     */
   private Integer logId;
}
