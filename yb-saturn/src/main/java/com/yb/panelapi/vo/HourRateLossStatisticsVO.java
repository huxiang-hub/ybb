package com.yb.panelapi.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 小时达成率总体损耗统计分析vo
 * @Author my
 * @Date Created in 2020/8/16 15:33
 */

@ApiModel("小时达成率总体损耗统计分析vo")
@Data
public class HourRateLossStatisticsVO {

    @ApiModelProperty("设备名称")
    private String name;

    @ApiModelProperty("设备类型名称")
    private String maType;

    /**
     * 生产准备时间
     */
    @ApiModelProperty("生产准备时间")
    private Integer proReadyTime;

    /**
     * 设备故障时间
     */
    @ApiModelProperty("设备故障时间")
    private Integer deviceFaultTime;

    /**
     * 品质实验时间
     */
    @ApiModelProperty("品质实验时间")
    private Integer qualityTestTime;

    /**
     * 品种切换时间
     */
    @ApiModelProperty("品种切换时间")
    private Integer typeSwitchTime;

    /**
     * 管理停止时间
     */
    @ApiModelProperty("管理停止时间")
    private Integer manageStopTime;

    /**
     * 其他损失时间(分钟)
     */
    @ApiModelProperty("其他损失时间(分钟)")
    private Integer otherLossTime;

}
