package com.yb.execute.dto;

import com.yb.execute.entity.ExecuteFault;
import com.yb.execute.entity.ExecuteState;
import lombok.Data;

/**
 * @author by SUMMER
 * @date 2020/3/15.
 */
@Data
public class ExecuteStateFaultDto {

    //执行表状态实体
    private ExecuteState state;
    //设备停机实体
    private ExecuteFault fault;
}
