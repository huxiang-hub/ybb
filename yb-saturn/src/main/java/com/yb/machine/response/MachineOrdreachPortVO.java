package com.yb.machine.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 设备小时达成率报表VO
 * @Author my
 */
@ApiModel("设备小时达成率报表VO")
@Data
public class MachineOrdreachPortVO {

    @ApiModelProperty("小时")
    private Integer targetHour;

    @ApiModelProperty("日期")
    private String targetDay;

    @ApiModelProperty("计划数")
    private Integer planCount;

    @ApiModelProperty("实际数")
    private Integer realCount;

}
