package com.yb.quality.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreatTableVO {
    @ApiModelProperty(value = "工序分类id", required = true)
    /*工序分类id*/
    private Integer pyId;
    @ApiModelProperty(value = "工序分类名称", required = true)
    /*工序分类名称*/
    private String pyName;
}
