package com.yb.machine.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: 设备监控报表VO
 * @Author my
 */
@Data
@ApiModel("设备监控报表VO")
public class MachineReportVO {

    @ApiModelProperty("运行时长")
    private Integer runTime;

    @ApiModelProperty("停机时长")
    private Integer stopTime;

    @ApiModelProperty("离线时长")
    private Integer offTime;

    @ApiModelProperty("设备状态变化报表信息")
    List<MachineStatusChangePortVO> machineStatusChangePort;

    @ApiModelProperty("小时达成率报表信息")
    List<MachineOrdreachPortVO> MachineOrdreachPort;

    @ApiModelProperty("速度变化报表信息")
    List<MachineSpeedChangePortVO> machineSpeedChangePort;

}
