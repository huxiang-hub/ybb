package com.yb.feishu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "飞书消息内容对象")
public class Content implements Serializable {
    @ApiModelProperty(value = "消息内容")
    private String text;
}
