package com.yb.machine.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author my
 */
@Data
@ApiModel("今日产能VO")
public class TodayCapacityVO {

    @ApiModelProperty("设备名")
    private String name;

    @ApiModelProperty("产量")
    private Integer countNum;
}
