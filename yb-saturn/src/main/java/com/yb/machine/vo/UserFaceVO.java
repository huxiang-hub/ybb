package com.yb.machine.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "人脸识别对象")
public class UserFaceVO implements Serializable {
    @ApiModelProperty(value = "用户id")
    private Integer usId;
    @ApiModelProperty(value = "用户名称")
    private String usName;
    @ApiModelProperty(value = "职位")
    private String job;
    @ApiModelProperty(value = "照片路径")
    private String avatarUrl;
    @ApiModelProperty(value = "登录账号")
    private String jobnum;
}
