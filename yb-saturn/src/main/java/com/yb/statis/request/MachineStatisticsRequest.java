package com.yb.statis.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 机台产能统计请求类
 * @Author my
 * @Date Created in 2020/8/19 19:08
 */
@Data
@ApiModel("机台产能统计请求类")
public class MachineStatisticsRequest {

    @ApiModelProperty("设备id")
    private Integer maId;

    @ApiModelProperty("查询日期")
    private String sdDate;

    @ApiModelProperty("班次id")
    private Integer wsId;
}
