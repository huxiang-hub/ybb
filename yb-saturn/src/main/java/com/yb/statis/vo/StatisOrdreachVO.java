package com.yb.statis.vo;

import com.yb.statis.entity.StatisOrdreach;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 小时达成率详情信息
 */
@Data
@ApiModel("小时达成率详情信息")
public class StatisOrdreachVO extends StatisOrdreach {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 生产准备时间
     */
    @ApiModelProperty(value = "生产准备时间")
    private Integer proReadyTime;

    /**
     * 设备故障时间
     */
    @ApiModelProperty(value = "设备故障时间")
    private Integer DeviceFaultTime;

    /**
     * 品质实验时间
     */
    @ApiModelProperty(value = "品质实验时间")
    private Integer qualityTestTime;

    /**
     * 品种切换时间
     */
    @ApiModelProperty(value = "品种切换时间")
    private Integer typeSwitchTime;

    /**
     * 管理停止时间
     */
    @ApiModelProperty(value = "管理停止时间")
    private Integer manageStopTime;

    /**
     * 其他损失时间
     */
    @ApiModelProperty(value = "其他损失时间")
    private Integer otherLossTime;

    /**
     * 其他损失事由
     */
    @ApiModelProperty(value = "其他损失事由")
    private String otherLossCause;
    /**
     * 时间区间
     */
    @ApiModelProperty(value = "时间区间")
    private String timeInterval;

}
