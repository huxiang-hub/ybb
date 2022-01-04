package com.yb.panelapi.order.entity;

import com.yb.execute.entity.ExecuteFault;
import com.yb.execute.entity.ExecuteState;
import lombok.Data;

/**
 * 停机上报实体类
 * @author by SUMMER
 * @date 2020/3/20.
 */
@Data
public class DownFaultReportDto {
    //停机上报表
    private ExecuteFault executeFault;
    //执行状态实体
    private ExecuteState executeState;
}
