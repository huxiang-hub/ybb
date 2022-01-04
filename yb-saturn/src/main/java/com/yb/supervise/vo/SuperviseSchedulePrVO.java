package com.yb.supervise.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单进度表/工序vo
 *
 * @author my
 * @since 2020-07-09
 */
@Data
@ApiModel(value = "订单进度表,工序vo")
public class SuperviseSchedulePrVO {

    @ApiModelProperty("工序id")
    private Integer prId;

    @ApiModelProperty("工序名称")
    private String prName;

    @ApiModelProperty("合计(百分比)")
    private BigDecimal total;
}
