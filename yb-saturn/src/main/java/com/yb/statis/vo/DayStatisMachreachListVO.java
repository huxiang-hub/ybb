package com.yb.statis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description: 天计划达成率vo
 * @Author my
 * @Date Created in 2020/8/1 19:32
 */
@Data
@ApiModel("天计划达成率vo")
public class DayStatisMachreachListVO {

    @ApiModelProperty("小时")
    private Integer hour;

    @ApiModelProperty("设备类型")
    private String maType;

    @ApiModelProperty("设备名称")
    private String maName;

    @ApiModelProperty("达成率")
    private BigDecimal rate;

}
