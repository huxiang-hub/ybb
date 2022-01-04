package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "在制品返回的客户与订单信息")
public class ArticlesCmNameOdNoVO implements Serializable {

    @ApiModelProperty(value = "客户名称")
    private String cmName;
    @ApiModelProperty(value = "订单相关信息列表")
    private List<ArticlesOrderVO> articlesOrderVOList;
}
