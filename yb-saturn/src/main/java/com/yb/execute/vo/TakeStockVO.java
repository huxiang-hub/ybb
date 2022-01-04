package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lzb
 * @Date 2021/2/25
 **/
@Data
@ApiModel(value = "盘点审核列表对象")
public class TakeStockVO {

    @ApiModelProperty(value = "工单ID")
    private Integer sdId;

    @ApiModelProperty(value = "工单计划总数")
    private Integer sdPlanNumber;

    @ApiModelProperty(value = "工单完成总数")
    private Integer sdCompleteNumber;

    @ApiModelProperty(value = "上报表id")
    private Integer bfId;

    @ApiModelProperty(value = "排产单ID")
    private Integer wfId;

    @ApiModelProperty(value = "执行单ID")
    private Integer exId;

    @ApiModelProperty("工单号")
    private String wbNo;

    @ApiModelProperty("设备名")
    private String machineName;

    @ApiModelProperty("工序名")
    private String prName;

    @ApiModelProperty("产品名")
    private String productName;

    @ApiModelProperty("总计划数")
    private String totalNumber;

    @ApiModelProperty("完成数")
    private String accomplishNumber;

    @ApiModelProperty("托盘数")
    private Integer trayNumber;

    @ApiModelProperty("托盘产品总张数")
    private Integer trayProdNumber;

    @ApiModelProperty("审核状态")
    private Integer exStatus;

    @ApiModelProperty("班次时间")
    private String sdDate;

    @ApiModelProperty("白班夜班")
    private String ckName;

    @ApiModelProperty("上报人")
    private String realName;
}
