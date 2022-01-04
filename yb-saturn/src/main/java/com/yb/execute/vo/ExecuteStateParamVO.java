package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "时序图参数对象")
public class ExecuteStateParamVO implements Serializable {
    @ApiModelProperty(value = "查询条件始时间,开始时间", required = true)
    private String startTime;//查询条件始时间,开
    @ApiModelProperty(value = "查询条件,结束时间", required = true)
    private String endTime;
    @ApiModelProperty(value = "工序id")
    private Integer prId;
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    @ApiModelProperty(value = "班次id")
    private Integer wsId;
    @ApiModelProperty(value = "设备类型")
    private Integer maType;
    @ApiModelProperty(value = "设备名称")
    private String machineName;
    @ApiModelProperty(value = "部门名称")
    private String dpName;

}
