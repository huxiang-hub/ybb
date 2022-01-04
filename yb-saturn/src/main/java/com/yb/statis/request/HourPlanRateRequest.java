package com.yb.statis.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 天计划达成率请求类
 * @Author my
 * @Date Created in 2020/8/1 19:34
 */
@Data
@ApiModel("天计划达成率请求类")
public class HourPlanRateRequest {

    @ApiModelProperty("设备类型")
    private String maType;

    @ApiModelProperty("查询日期")
    private String targetDay;

    @ApiModelProperty("设备id")
    private Integer maId;

    @JsonIgnore
    private String groupBy;

}
