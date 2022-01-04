package com.yb.panelapi.order.entity;

import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteExamine;
import com.yb.execute.vo.ExecuteExamineIpcVO;
import com.yb.panelapi.waste.entity.QualityBfwaste;
import com.yb.workbatch.vo.WorkbatchOrdlinkVO;
import lombok.Data;

import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/15.
 */
@Data
public class OrderReportVo {
    //执行上报表信息
    private ExecuteBriefer briefer;
    //执行上报表信息
    private WorkbatchOrdlinkVO ordlinkVO;
    //上报审核表
    private ExecuteExamineIpcVO examine;
    //废品种类
    private List<QualityBfwaste> wastes;

    //需要上报的上报表的id
    private Integer bid;
    //标志位
    private Integer isNext;

}
