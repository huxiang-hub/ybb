package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "在制品订单相关信息")
public class ArticlesOrderVO implements Serializable {
    @ApiModelProperty(value = "客户名称")
    private String cmName;
    @ApiModelProperty(value = "订单编号")
    private String odNo;
    @ApiModelProperty(value = "订单计划数")
    private Integer odCount;
}
