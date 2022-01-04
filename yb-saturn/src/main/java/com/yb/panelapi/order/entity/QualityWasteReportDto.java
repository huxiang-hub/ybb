package com.yb.panelapi.order.entity;

import com.yb.execute.entity.ExecuteState;
import com.yb.execute.entity.ExecuteWaste;
import lombok.Data;

/**
 *
 * 质量检测上报
 * @author by SUMMER
 * @date 2020/3/20.
 */
@Data
public class QualityWasteReportDto {
    //质量检测表
    private ExecuteWaste executeWaste;
    //执行状态实体
    private ExecuteState executeState;
}
