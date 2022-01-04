package com.yb.panelapi.waste.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "上报数据参数信息")
public class QualityBfwasteVO {
    //工序id
    @ApiModelProperty(value = "上道工序ID；若为本道工序，该字段为空")
    private Integer prId;

    @ApiModelProperty(value = "执行单工序ID；为本道工序ID")
    private Integer exPrid;

    @ApiModelProperty(value = "废品类型")
    private Integer wasteType;

    @ApiModelProperty(value = "废品类型名称")
    private String wasteName;
    //上报数量
    @ApiModelProperty(value = "废品数量，包含过版纸等不良品")
    private Integer wasteNum;
}
