package com.yb.yilong.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 盒子实时信息请求
 * @Author my
 */
@Data
@ApiModel("盒子实时信息请求")
public class BoxInfoRequest {

    @ApiModelProperty(value = "设备id", required = true)
    @NotNull(message = "设备id不能为空")
    private Integer maId;


}
