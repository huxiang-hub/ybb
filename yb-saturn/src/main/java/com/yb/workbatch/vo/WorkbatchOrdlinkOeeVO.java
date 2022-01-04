package com.yb.workbatch.vo;

import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchOrdoee;
import lombok.Data;

@Data
public class WorkbatchOrdlinkOeeVO extends WorkbatchOrdlink {
    /*排产oee*/
    private WorkbatchOrdoee workbatchOrdoee;

    private Integer speed;

    private Integer mouldStay;
}
