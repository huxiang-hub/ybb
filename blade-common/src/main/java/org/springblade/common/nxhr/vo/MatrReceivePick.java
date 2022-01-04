package org.springblade.common.nxhr.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lzb
 * @Date 2021/3/31
 **/
@Data
@ApiModel("辅料出入库")
public class MatrReceivePick {

    @ApiModelProperty("库区")
    private String location;

    @ApiModelProperty("库位")
    private String locSub;

    @ApiModelProperty("仓库类型，固定为“辅料”")
    private String stockType;

    @ApiModelProperty("料号")
    private String stockItem;

    @ApiModelProperty("入库日期")
    private String pickDate;

    @ApiModelProperty("入库数量")
    private String receivePickQty;

    @ApiModelProperty("领用部门")
    private String receivePickTo;

    @ApiModelProperty("领用机台")
    private String receivePickMachine;

    @ApiModelProperty("领用人")
    private String receivePickFrom;

    @ApiModelProperty("用途")
    private String receivePickFor;

    @ApiModelProperty("库存条码")
    private String barcode;

    @ApiModelProperty("物料规格说明")
    private String stockDescription;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("装车单")
    private String dummy;

}
