package com.yb.supervise.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/7/31 11:01
 */
@ApiModel("印聊产能分析VO")
@Data
public class YieldStatisticsListVO {

    @ApiModelProperty("小时")
    private Integer hour;

    @ApiModelProperty("完成数")
    private Integer completNum;

    @ApiModelProperty("计划数")
    private Integer planNUm;

    @ApiModelProperty("设备类型")
    private String maType;

    @ApiModelProperty("设备id")
    private Integer maId;

    @ApiModelProperty("达成率")
    private BigDecimal rate;

    @ApiModelProperty("设备名称")
    private String maName;

}
