package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "机台上报详情,托盘清单对象")
public class TraycardDetailedVO implements Serializable {

    @ApiModelProperty(value = "工单号")
    private String wbNo;
    @ApiModelProperty(value = "条码编号")
    private String tdNo;
    @ApiModelProperty(value = "托号")
    private String trayNo;
    @ApiModelProperty(value = "数量")
    private Integer trayNum;
    @ApiModelProperty(value = "库位位置")
    private String stNo;
    @ApiModelProperty(value = "打印时间")
    private String printTime;
    @ApiModelProperty(value = "操作人")
    private String usName;
    @ApiModelProperty(value = "审核人")
    private String examineName;
    @ApiModelProperty(value = "状态")
    private Integer exStatus;
    @ApiModelProperty(value = "类型:1、盘点 2、报废、3机长")
    private Integer exMold;
    @ApiModelProperty(value = "审核前数据")
    private Integer dataBefore;
    @ApiModelProperty(value = "审核后数据")
    private Integer dataAfter;
    @ApiModelProperty(value = "照片")
    private String exPics;
//    @ApiModelProperty(value = "审核进度")
//    private String reviewProgress;
    @ApiModelProperty(value = "审核进度id")
    private String processInstanceId;

}
