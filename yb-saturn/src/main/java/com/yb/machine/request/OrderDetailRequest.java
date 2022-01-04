package com.yb.machine.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @Description:
 * @Author my
 * @Date Created in 2021/1/27 15:31
 */
@Data
@ApiModel("工单详情请求")
public class OrderDetailRequest {

    @ApiModelProperty(value = "工单号（必传）", required = true)
    @NotBlank(message = "工单号不能为空")
    private String wbNo;
}
