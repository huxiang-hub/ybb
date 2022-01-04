package com.yb.statis.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 设备当前排产单进度信息
 * @Author my
 * @Date Created in 2020/8/22 20:00
 */
@Data
@ApiModel("设备当前排产单进度信息请求")
public class DeviceCurrentOrderRequest {

    @ApiModelProperty("车间id")
    private Integer dpId;

    @ApiModelProperty("设备id")
    private Integer maId;

    @ApiModelProperty("工序分类id")
    private Integer pyId;

    @ApiModelProperty("设备类型")
    private String maType;

    @ApiModelProperty("查询日期")
    private String targetDay;

    @ApiModelProperty("班次id")
    private Integer wsId;
}
