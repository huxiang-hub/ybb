package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "在制品批次和工序相关信息")
public class ArticlesWbProcessVO implements Serializable {

    @ApiModelProperty(value = "批次编号")
    private String wbNo;
    @ApiModelProperty(value ="批次计划数")
    private Integer wbPlanCount;
    @ApiModelProperty(value = "批次计划放数")
    private Integer wbExtraNum;
    @ApiModelProperty(value = "工序相关数据列表")
    private List<ArticlesProcessVO> articlesProcessVOList;
}
