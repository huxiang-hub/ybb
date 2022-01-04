package com.yb.panelapi.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * #Description
 */
@ApiModel("上报请求类")
@Data
public class OrderReportRequest {

    @ApiModelProperty(value = "上报id")
    private Integer bId;

    @ApiModelProperty(value = "工单id")
    private Integer sdId;

    @ApiModelProperty(value = "工单id")
    private Integer wfId;

    @ApiModelProperty(value = "设备id")
    private Integer maId;

    @ApiModelProperty("标志位")
    private Integer isNext;

    /**
     * 上报作业数量
     */
    @ApiModelProperty(value = "上报作业数量")
    private Integer productNum;
    /**
     * 上报正品数量
     */
    @ApiModelProperty(value = "上报正品数量")
    private Integer countNum;
    /**
     * 废品数量
     */
    @ApiModelProperty(value = "废品数量")
    private Integer wasteNum;


    /**
     * 上工序废品数量
     */
    @ApiModelProperty(value = "上工序废品数量")
    private Integer upWasteNum;

    /**
     * 上上工序废品数量
     */
    @ApiModelProperty(value = "上上工序废品数量")
    private Integer uppWasteNum;

    /**
     * 上报人id
     */
    @ApiModelProperty(value = "上报人id")
    private Integer reportUserId;

    @ApiModelProperty(value = "生产状态：1已完成0未完成")
    private Integer status;

    @ApiModelProperty(value = "用户id")
    private String usIds;
}
