package com.yb.workbatch.vo;

import com.yb.workbatch.entity.WorkbatchProgress;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WorkbatchProgressPtVO implements Serializable {
    /*部件id*/
    private Integer ptId;
    /*部件名称*/
    private String ptName;
    /*主计划信息*/
    private List<WorkbatchProgress> children;
}
