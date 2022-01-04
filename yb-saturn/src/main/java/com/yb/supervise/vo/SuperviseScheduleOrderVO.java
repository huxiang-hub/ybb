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
@ApiModel(value = "订单进度表,订单对象")
public class SuperviseScheduleOrderVO {

    @ApiModelProperty("订单id")
    private Integer odId;
    @ApiModelProperty("订单名称")
    private String odName;
    @ApiModelProperty("订单编号")
    private String odNo;
    @ApiModelProperty("合计(百分比)")
    private BigDecimal total;
    @ApiModelProperty("订单进度批次信息")
    private List<SuperviseScheduleBatchVO> batchList=new ArrayList<>();
}
