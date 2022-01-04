package org.springblade.common.nxhr.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lzb
 * @Date 2021/3/31
 **/
@Data
@ApiModel("辅料库存")
public class MatrStock {

    @ApiModelProperty("库区")
    private String location;

    @ApiModelProperty("库位")
    private String locSub;

    @ApiModelProperty("仓库类型，固定为“辅料”")
    private String stockType;

    @ApiModelProperty("料号")
    private String stockItem;

    @ApiModelProperty("库存数量")
    private String actualQty;

    @ApiModelProperty("制造商简称")
    private String maker;

    @ApiModelProperty("库存条码")
    private String barcode;

    @ApiModelProperty("供应商编号")
    private String suppId;

    @ApiModelProperty("物料规格说明")
    private String stockDescription;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("供应商简称")
    private String suppShortName;

}
