package com.yb.supervise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备设置页面当前状态参数
 */
@Data
public class MachineParmVO {
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    @ApiModelProperty(value = "班次id")
    private Integer wsId;
    @ApiModelProperty(value = "当前日期")
    private Integer targetDay;
}
