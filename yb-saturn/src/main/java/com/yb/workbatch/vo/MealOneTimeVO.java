package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "吃饭时间对象")
public class MealOneTimeVO implements Serializable {
    @ApiModelProperty(value = "吃饭开始时间")
    private String starTime;
    @ApiModelProperty(value = "吃饭结束时间")
    private String endTime;
}
