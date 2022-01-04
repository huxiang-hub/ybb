package com.yb.statis.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 计划达成率列表请求
 * @Author my
 * @Date Created in 2020/7/31 16:52
 */
@ApiModel("计划达成率列表请求")
@Data
public class StatisMachreachListRequest {

    @ApiModelProperty("日期(必传)")
    @NotBlank(message = "日期不能为空")
    private String targetDay;

    @ApiModelProperty("设备类型")
    private String maType;

    @ApiModelProperty("设备id")
    private Integer maId;

    @ApiModelProperty("班次id")
    private Integer wsId;

    @ApiModelProperty("部门id")
    private Integer dpId;

}
