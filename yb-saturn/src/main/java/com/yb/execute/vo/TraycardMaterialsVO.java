package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "托盘及上料相关数据对象")
public class TraycardMaterialsVO implements Serializable {

    @ApiModelProperty(value = "托盘号")
    private String tdNo;
    @ApiModelProperty(value = "数量")
    private Integer trayNum;
    @ApiModelProperty(value = "库存id")
    private String stId;
    @ApiModelProperty(value = "库存位置")
    private String stNo;
    @ApiModelProperty(value = "打印时间")
    private Date printTime;
    @ApiModelProperty(value = "打印人")
    private String usName;
    @ApiModelProperty(value = "审核状态")
    private Integer isReview;
    @ApiModelProperty(value = "是否上料 为上料:0, 已上料: 1")
    private Integer isLoadMaterial;
    @ApiModelProperty(value = "上料设备")
    private String maName;
    @ApiModelProperty(value = "上料时间")
    private Date loadMaterialTime;

}
