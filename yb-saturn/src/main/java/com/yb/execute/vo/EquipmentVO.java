package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "时序图对象")
public class EquipmentVO implements Serializable {
    /*------------------------设备时序图------------------*/
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    @ApiModelProperty(value = "设备名")
    private String machineName;
    @ApiModelProperty(value = "准备时间段")
    private List<PrepareVO> prepareVOList;//准备时间段
    @ApiModelProperty(value = "生产时间段")
    private List<ProductionVO> productionVOList;//生产时间段
    @ApiModelProperty(value = "停机时间段")
    private List<DowntimeVO> downtimeVOList;//停机时间段
    @ApiModelProperty(value = "间隔时间")
    private List<IntervalVO> intervalVOList;//间隔时间
    @ApiModelProperty(value = "保养时间段")
    private List<MaintainVO> maintainVOList;//保养时间段
    @ApiModelProperty(value = "计划生产")
    private PlanMachineVO planMachineVO;//计划生产
}
