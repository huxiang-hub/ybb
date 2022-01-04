package com.yb.statis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/23 11:04
 */
@Data
@ApiModel("产能总进度vo")
public class DeviceCapacityProgressVO {

    @ApiModelProperty("各个设备的统计信息")
    private List<DeviceCapacityProgressListVO> deviceCapacityProgressListVOS;

    @ApiModelProperty("正在生产数")
    private Integer totalCurrentNum;

    @ApiModelProperty("总完成数")
    private Integer totalFinishNum;

    @ApiModelProperty("总计划完成数")
    private Integer totalPlanNum;

    @ApiModelProperty("待生产数")
    private Integer totalWaitNum;

    @ApiModelProperty("暂停未下发")
    private Integer stopNum;

    @ApiModelProperty("产能总进度")
    private Integer totalNowNum;
}
