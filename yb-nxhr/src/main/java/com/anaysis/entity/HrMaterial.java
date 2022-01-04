package com.anaysis.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author lzb
 * @Date 2021/1/10
 **/
@Data
@ApiModel(value = "和瑞erp物料vo")
public class HrMaterial {

    @ApiModelProperty("原料名称")
    private String description;

    @ApiModelProperty("原料号")
    private String stockItem;

    @ApiModelProperty("原料规格")
    private String stockDescription;

    @ApiModelProperty("条码")
    private String barCode;

    @ApiModelProperty("库存量")
    private BigDecimal actualQty;

    @ApiModelProperty("米数")
    private BigDecimal stockMeter;

    @ApiModelProperty("类型")
    private String stockMark;

    @ApiModelProperty("入库日期")
    private String receiveDate;

    @ApiModelProperty("库区")
    private String location;

    @ApiModelProperty("库位")
    private String locSub;

    @ApiModelProperty("erpId")
    private String erpId;
}
