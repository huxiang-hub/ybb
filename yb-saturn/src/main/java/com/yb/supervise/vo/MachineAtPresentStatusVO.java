package com.yb.supervise.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 当前设备状态返回对象
 */
@Data
@ApiModel(value = "当前设备状态返回对象", description = "当前设备状态返回对象")
public class MachineAtPresentStatusVO implements Serializable {

    @ApiModelProperty(value = "设备编号")
    private String maNO;
    @ApiModelProperty(value = "设备名称")
    private String maName;
    @ApiModelProperty(value = "部门名称")
    private String deptName;
    @ApiModelProperty(value = "工序名称")
    private String prName;
    @ApiModelProperty(value = "工单编号")
    private String wbNo;
    @ApiModelProperty(value = "订单编号")
    private String odNo;
    @ApiModelProperty(value = "排产日期")
    private String sdDate;
    @ApiModelProperty(value = "班次id")
    private Integer wsId;
    @ApiModelProperty(value = "当前状态")
    private String status;
    @ApiModelProperty(value = "转速")
    private String dspeed;
    @ApiModelProperty(value = "当天(班)计数")
    private String numberOfDay;
    @ApiModelProperty(value = "当天(班)工单")
    private String sdNum;
    @ApiModelProperty(value = "开机人员")
    private String usIds;
    @ApiModelProperty(value = "开机人员")
    private List usNames;
    @ApiModelProperty(value = "当班能耗")
    private String wsEnergynum;
    @ApiModelProperty(value = "当天能耗")
    private String energyOfDay;
}
