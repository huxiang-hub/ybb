package com.yb.workbatch.vo;

import com.yb.workbatch.entity.WorkbatchProgress;
import lombok.Data;

import java.util.Date;

@Data
public class WorkbatchProgressVO extends WorkbatchProgress {
    private static final long serialVersionUID = 1L;
    private Double differenceTime;
    private Double differenceTimeStart;
    private Double differenceTimeEnd;
    private String startTime;//查询条件,开始时间
    private String endTime;//查询条件,结束时间

}
