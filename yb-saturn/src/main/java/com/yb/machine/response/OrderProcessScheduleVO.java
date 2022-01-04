package com.yb.machine.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author my
 */
@Data
@ApiModel("工单工序进度VO")
public class OrderProcessScheduleVO {

    @ApiModelProperty("设备编号")
    private String mno;

    @ApiModelProperty("设备名称")
    private String maName;

    @ApiModelProperty("工序名称")
    private String prName;

    @ApiModelProperty("排产数")
    private Integer scheduledNum;

    @ApiModelProperty("上报数")
    private Integer productNum;

    @ApiModelProperty("废品数")
    private Integer wasteNum;

    @ApiModelProperty("达成率")
    private BigDecimal rate;

    @ApiModelProperty("生产状态：-1待排产ERP接入0手工导入2开始生产3已完成4已挂起5废弃6驳回7已排产8部分完成9未排完10强制结束" )
    private Integer wbStatus;

    @ApiModelProperty("wfStatus:-1未下发，0:待接单，1：生产中，2：生产完成（已上报）  3：未上报（结束生产） 4：未完成（已上报）")
    private Integer wfStatus;

    @ApiModelProperty("排产id")
    private Integer wfId;

    @ApiModelProperty("工单编号")
    private Integer wbNo;

    @ApiModelProperty("工单排产日期")
    private String sdDate;
}
