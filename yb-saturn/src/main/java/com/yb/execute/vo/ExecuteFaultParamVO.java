package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ExecuteFault参数对象")
public class ExecuteFaultParamVO {
    @ApiModelProperty(value = "设备id", required = true)
    private Integer maId;
    @ApiModelProperty(value = "班次id", required = true)
    private Integer wsId;
    @ApiModelProperty(value = "日期")
    private String targetDay;
}
