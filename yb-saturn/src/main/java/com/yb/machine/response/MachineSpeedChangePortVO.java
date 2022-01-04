package com.yb.machine.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author my
 * @Date Created in 2021/1/9 11:24
 */
@Data
@ApiModel("设备速度变化报表")
public class MachineSpeedChangePortVO {

    @ApiModelProperty("时间")
    private Date startTime;

    @ApiModelProperty("速度")
    private BigDecimal speed;


}
