package com.yb.machine.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/12/1 11:14
 */
@ApiModel("设备监控产能分析VO")
@Data
public class MonitorCapacityAnalyVO {

    @ApiModelProperty("设备名称")
    private String name;

    @ApiModelProperty("产能")
    private Integer capacity;

    @JsonIgnore
    private Integer wsId;

    @JsonIgnore
    private Integer maId;
}
