package com.yb.supervise.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 印聊获取绩效分析请求
 * @Author my
 * @Date Created in 2020/7/31 14:39
 */
@ApiModel("印聊获取绩效分析请求")
@Data
public class PerformanceAnalyRequest {

    @ApiModelProperty("设备类型")
    private String maType;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty("设备Id")
    private Integer maId;
}
