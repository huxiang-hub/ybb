package com.yb.statis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/7/31 16:46
 */
@Data
@ApiModel("计划达成率listvo")
public class StatisMachreachListVO {

    @ApiModelProperty("工序名称")
    private String prName;

    @ApiModelProperty("计划产量")
    private String planNum;

    @ApiModelProperty("完成数")
    private String countNum;

    @ApiModelProperty("设备名称")
    private String maName;

    @ApiModelProperty("结束时间")
    private Date finishTime;

    @ApiModelProperty("上报时间")
    private Date reportTime;

    @ApiModelProperty("良品率")
    private BigDecimal yieldRate;

    @ApiModelProperty("达成率")
    private BigDecimal rate;
}
