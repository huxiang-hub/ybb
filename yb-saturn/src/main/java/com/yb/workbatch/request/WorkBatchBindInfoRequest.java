package com.yb.workbatch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/17 18:27
 */
@Data
@ApiModel("排产单绑定设备详细内容")
public class WorkBatchBindInfoRequest {

    @ApiModelProperty(value = "移动日期(必传)", required = true)
    private String sdDate;

    @ApiModelProperty(value = "移动数量(必传)", required = true)
    private Integer number;

    @ApiModelProperty(value = "班次id(必传)", required = true)
    private Integer wsId;

    @ApiModelProperty(value = "排产id(必传)", required = true)
    private Integer sdId;
}
