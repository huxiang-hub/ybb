package com.yb.dingding.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DingFile implements Serializable {
    @ApiModelProperty(value = "附件列表")
    private List<Attachments> attachments;
    @ApiModelProperty(value = "图片URL地址")
    private List<String> photos;
}
