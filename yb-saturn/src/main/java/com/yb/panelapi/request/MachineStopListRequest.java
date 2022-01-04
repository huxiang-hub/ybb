package com.yb.panelapi.request;

import com.yb.common.constant.LocalEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/10/10 15:29
 */
@ApiModel("机台停机列表请求")
@Data
public class MachineStopListRequest {

    @ApiModelProperty(value = "设备id(必传)", required = true)
    @NotBlank(message = "设备id不能为空")
    private Integer maId;

    @ApiModelProperty(value = "大于小于的时间(必传)", required = true)
    @NotNull(message = "大于小于的时间")
    private Integer time;

    @ApiModelProperty(value = "大于小于(必传)", required = true)
    @NotNull(message = "大于小于")
    private LocalEnum.StopListType operator;
}
