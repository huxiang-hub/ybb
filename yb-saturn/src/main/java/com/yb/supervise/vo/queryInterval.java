package com.yb.supervise.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel("设备状态间隔表请求类")
public class queryInterval {
    @ApiModelProperty("车间id")
    private Integer dpId;
    @ApiModelProperty("设备类型")
    private String maType;
    @ApiModelProperty("设备id")
    private Integer maId;
    @ApiModelProperty("班次id")
    private Integer wsId;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("间隔时间")
    private Integer diffTime;
}
