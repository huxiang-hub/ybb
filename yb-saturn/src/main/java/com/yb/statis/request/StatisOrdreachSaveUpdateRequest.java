package com.yb.statis.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Description: 小时达成率新增修改请求
 * @Author my
 * @Date Created in 2020/07/18
 */
@Data
@ApiModel("小时达成率新增修改请求")
public class StatisOrdreachSaveUpdateRequest {

//    @ApiModelProperty("修改达成率信息")
//    List<StatisOrdreachDTO> statisOrdreachDTOS;

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
