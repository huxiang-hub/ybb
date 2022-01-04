package com.yb.workbatch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 排产单绑定设备请求
 * @Author my
 * @Date Created in 2020/7/28 2:43
 */
@ApiModel("排产单绑定设备请求")
@Data
public class WorkBatchBindRequest {

    @ApiModelProperty(value = "设备id(必传)", required = true)
    @NotNull(message = "设备id不能为空")
    private Integer maId;

    @ApiModelProperty(value = "排产移动信息不能为空(必传)", required = true)
    @NotEmpty(message = "移动信息不能为空")
    List<WorkBatchBindInfoRequest> workBatchBindInfoRequests;
}
