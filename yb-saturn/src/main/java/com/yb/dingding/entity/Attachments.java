package com.yb.dingding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Attachments implements Serializable {
    @JsonProperty("space_id")
    @ApiModelProperty(value = "钉盘空间ID")
    private String spaceId;
    @JsonProperty("file_size")
    @ApiModelProperty(value = "文件大小")
    private String fileSize;
    @JsonProperty("file_id")
    @ApiModelProperty(value = "文件ID")
    private String fileId;
    @JsonProperty("file_name")
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    @JsonProperty("file_type")
    @ApiModelProperty(value = "文件类型")
    private String fileType;
}
