package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("废品-数量vo")
public
class WastTypeSum {

    @ApiModelProperty(value = "工序id")
    private Integer prId;

    @ApiModelProperty(value = "废品类型id")
    private Integer wastClassId;

    @ApiModelProperty(value = "废品数量")
    private Integer waste;

}