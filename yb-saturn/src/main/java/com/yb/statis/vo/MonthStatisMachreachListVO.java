package com.yb.statis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description: 月计划达成率vo
 * @Author my
 * @Date Created in 2020/8/1
 */
@Data
@ApiModel("月计划达成率vo")
public class MonthStatisMachreachListVO {

    @ApiModelProperty("时间")
    private String date;

    @ApiModelProperty("设备类型")
    private String maType;

    @ApiModelProperty("设备名字")
    private String maName;

    @ApiModelProperty("达成率")
    private BigDecimal rate;
}
