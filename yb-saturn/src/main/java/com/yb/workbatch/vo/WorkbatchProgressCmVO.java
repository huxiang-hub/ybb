package com.yb.workbatch.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WorkbatchProgressCmVO implements Serializable {
    /*批次id*/
    private Integer wbId;
    /*工单编号*/
    private String wbNo;
    /*客户名称*/
    private String cmName;
    /*产品Id*/
    private Integer pdId;
    /*产品名称*/
    private String pdName;
    /*部件信息*/
    private List<WorkbatchProgressPtVO> children;
}
