package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author lzb
 * @Date 2020/12/23 17:11
 **/
@Data
@ApiModel(value = "工单-数量")
public class SdIdNumberVo {

    @NotEmpty(message = "排产id不能为空")
    @ApiModelProperty("工单id")
    private Integer sdId;

    @NotEmpty(message = "排产数量不能为空")
    @ApiModelProperty("排产数量")
    private Integer number;
}
