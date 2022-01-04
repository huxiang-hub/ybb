package com.yb.execute.vo;

import lombok.Data;

import java.util.List;

@Data
public class TraycardDeleteListVO {
    /*需要修改的id*/
    private List<Integer> etIdList;
    /*本台计数*/
    private Integer trayNum;
    /*备注*/
    private String remark;
    /*库位名称*/
    private String storePlace;
    /*库位id*/
    private Integer mpId;
    /*操作人id*/
    private Integer usId;
}
