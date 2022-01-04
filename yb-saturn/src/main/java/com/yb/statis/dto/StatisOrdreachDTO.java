package com.yb.statis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;
import org.springframework.validation.annotation.Validated;


/**
 * @Description: 小时达成率 修改DTO
 * @Author my
 * @Date Created in 2020/7/18 14:47
 */
@Data
@ApiModel("小时达成率 修改DTO")
public class StatisOrdreachDTO {

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
    private Integer deviceFaultTime;

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
}
