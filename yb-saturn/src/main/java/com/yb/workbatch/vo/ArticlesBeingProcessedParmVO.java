package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "在制品参数")
public class ArticlesBeingProcessedParmVO implements Serializable {

    @ApiModelProperty(value = "产品名称")
    private String pdName;
    @ApiModelProperty(value = "批次编号")
    private String wbNo;
}
