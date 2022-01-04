package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "在制品托盘信息")
public class ArticlesTraycardVO implements Serializable {

    @ApiModelProperty(value = "总台数")
    private Integer totalNum;
    @ApiModelProperty(value = "第几台（版号）")
    private Integer trayNo;
    @ApiModelProperty(value = "托盘容量")
    private Integer trayNum;
    @ApiModelProperty(value = "库位id")
    private Integer mpId;
    @ApiModelProperty(value = "库位名称")
    private Integer storePlace;
}
