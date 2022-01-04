package com.yb.supervise.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单进度表/部件vo
 *
 * @author my
 * @since 2020-07-09
 */
@Data
@ApiModel(value = "订单进度表,部件vo")
public class SuperviseSchedulePtVO {

    @ApiModelProperty("部件id")
    private Integer ptId;

    @ApiModelProperty("部件名称")
    private String ptName;

    @ApiModelProperty("合计(百分比)")
    private BigDecimal total;

    @ApiModelProperty("排产所含工序信息")
    private List<SuperviseSchedulePrVO> prList = new ArrayList<>();
}
