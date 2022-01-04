package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "设备工单排产列表", description = "WorkbatchShiftVO排产对象")
public class WorkbatchShiftListVO {

    @ApiModelProperty(value = "工单工序唯一id")
    Integer sdId;
    @ApiModelProperty(value = "排产id")
    Integer wfId;
    @ApiModelProperty(value = "工序ID")
    Integer prId;
    @ApiModelProperty(value = "设备id；可以为空")
    Integer maId;
    @ApiModelProperty(value = "工单编号")
    String wbNo;
    @ApiModelProperty(value = "产品名称")
    String pdName;
    @ApiModelProperty(value = "设备名称")
    String maName;
    @ApiModelProperty(value = "客户名称缩写")
    String cmName;
    @ApiModelProperty(value = "截止日期")
    String endDate;
    @ApiModelProperty(value = "工序名称")
    String prName;
    @ApiModelProperty(value = "排产已完成数据；上报或者盒子数进行更新；采用bf表中的上报良品数")
    String completeNum;
    @ApiModelProperty(value = "计划排产数量")
    String planNum;
    @ApiModelProperty(value = "排产单状态:状态：-1未下发，0:待接单，1：生产中，2：生产完成（已上报）  3：未上报（结束生产） 4：未完成（已上报）")
    Integer shiftStatus;
    @ApiModelProperty(value = "是否自动接单0,否1,是")
    Integer isAuto = 0;
}
