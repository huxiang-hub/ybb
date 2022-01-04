package com.yb.quality.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "机台质量巡检选项卡对象")
public class TabControlVO implements Serializable {
    @ApiModelProperty(value = "选择值")
    private String selectiveValue;
    @ApiModelProperty(value = "显示名")
    private String displayName;

}
