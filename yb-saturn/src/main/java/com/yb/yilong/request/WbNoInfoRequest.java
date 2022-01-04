package com.yb.yilong.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 实时工单请求类
 * @Author my
 */
@Data
@ApiModel("实时工单请求类")
public class WbNoInfoRequest {

    @ApiModelProperty(value = "设备id", required = false)
    private Integer maId;

    @ApiModelProperty(value = "工单信息", required = false)
    private String wbNo;

    @ApiModelProperty(value = "执行日期", required = false)
    private String targetDay;

    @JsonIgnore
    private String targetTime;
}
