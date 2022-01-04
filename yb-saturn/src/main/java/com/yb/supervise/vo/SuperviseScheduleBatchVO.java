package com.yb.supervise.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单进度表/订单
 *
 * @author my
 * @since 2020-07-09
 */
@Data
@ApiModel(value = "订单进度表,批次进度VO")
public class SuperviseScheduleBatchVO {

    @ApiModelProperty("批次id")
    private Integer batchId;

    @ApiModelProperty("批次编号")
    private String batchNo;

    @ApiModelProperty("合计(百分比)")
    private BigDecimal total;

    @ApiModelProperty("排产所含部件信息")
    private List<SuperviseSchedulePtVO> ptList = new ArrayList<>();
}
