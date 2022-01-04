package org.springblade.common.nxhr.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lzb
 * @Date 2021/3/31
 **/
@Data
@ApiModel("纸卷出入库")
public class PaperRollReceivePick {

    @ApiModelProperty("库区")
    private String location;

    @ApiModelProperty("库位")
    private String locSub;

    @ApiModelProperty("仓库类型，固定为“纸卷”")
    private String stockType;

    @ApiModelProperty("料号")
    private String stockItem;

    @ApiModelProperty("出入库日期")
    private String receivePickDate;

    @ApiModelProperty("出入库量")
    private String recivePickQty;

    @ApiModelProperty("mm直径或厚度")
    private String diameter;

    @ApiModelProperty("M出入库米数（*）")
    private String receivePickMeter;

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

    @ApiModelProperty("叉车工")
    private String matrForkMan;

}
