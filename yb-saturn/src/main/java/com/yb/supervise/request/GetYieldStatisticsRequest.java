package com.yb.supervise.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/7/31 10:30
 */
@ApiModel("印聊获取今日产能分析请求")
@Data
public class GetYieldStatisticsRequest {

    @ApiModelProperty("所选日期(必传)")
    @NotNull(message = "所选日期不能为空")
    private String targetDate;

    @ApiModelProperty("设备类型")
    private String maType;

    @ApiModelProperty("班次id")
    private Integer wsId;

    @ApiModelProperty("设备id")
    private Integer maId;

    @JsonIgnore
    private String groupBy;
}
