package com.yb.yilong.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: 工单上报请求
 * @Author my
 */
@Data
@ApiModel("工单上报请求")
public class OpBrieferRequest {

    @ApiModelProperty(value = "工单编号", required = true)
    @NotBlank(message = "工单编号不能为空")
    private String wbNo;

    @ApiModelProperty(value = "设备id", required = true)
    @NotNull(message = "设备id不能为空")
    private Integer maId;

    @ApiModelProperty(value = "上报良品数", required = true)
    @NotNull(message = "上报良品数不能为空")
    private Integer goodsNum;

    @ApiModelProperty(value = "上报废品数", required = true)
    @NotNull(message = "上报废品数不能为空")
    private Integer wasteNum;

    @ApiModelProperty(value = "上报次数", required = true)
    @NotNull(message = "上报次数不能为空")
    private Integer index;
}
