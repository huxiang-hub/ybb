package com.yb.statis.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/23 9:30
 */
@Data
@ApiModel("设备产能总进度请求")
public class DeviceCapacityProgressRequest {

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

    @ApiModelProperty("班次Id")
    private List<Integer> wsIds;
}
