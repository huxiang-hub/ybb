package com.yb.statis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/23 11:18
 */
@ApiModel("班次排产单个数统计VO")
@Data
public class DeviceOrderNumProgressListVO {

    @ApiModelProperty("设备名称")
    private String maName;

    @ApiModelProperty("设备类型")
    private String maType;

    @ApiModelProperty("班次排产单个数")
    private Integer wfNum;

    @ApiModelProperty("完成的班次排产单个数")
    private Integer finishNum;

    @ApiModelProperty("完成率")
    private BigDecimal rate;

}
