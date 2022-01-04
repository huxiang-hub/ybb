package com.yb.machine.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/11/17 15:15
 */
@ApiModel("设备监控请求类")
@Data
public class MonitorRequest {

    @ApiModelProperty("车间id")
    private Integer dpId;

    @ApiModelProperty("设备编号，名称")
    private String query;

    @ApiModelProperty("车间id")
    private Integer wsId;

}
