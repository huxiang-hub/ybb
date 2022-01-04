package org.springblade.common.nxhr.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lzb
 * @Date 2021/3/31
 **/
@Data
@ApiModel("纸卷库存")
public class PaperRollStock {

    @ApiModelProperty("库区")
    private String location;

    @ApiModelProperty("库位")
    private String locSub;

    @ApiModelProperty("仓库类型")
    private String stockType;

    @ApiModelProperty("残卷标志")
    private String stockMark;

    @ApiModelProperty("料号")
    private String stockItem;

    @ApiModelProperty("纸质，相当于料号的/前的部分")
    private String paper;

    @ApiModelProperty("门幅，相当于料号的/后的部分")
    private String paperWidth;

    @ApiModelProperty("克重")
    private String gPerM2;

    @ApiModelProperty("库存重量")
    private String actualQty;

    @ApiModelProperty("直径或厚度")
    private String diameter;

    @ApiModelProperty("库存米数")
    private String stockMeter;

    @ApiModelProperty("制造商简称")
    private String maker;

    @ApiModelProperty("纸卷条码")
    private String barcode;

    @ApiModelProperty("供应商编号")
    private String suppId;

    @ApiModelProperty("物料规格说明")
    private String stockDescription;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("原入库量")
    private String receiveQty;

    @ApiModelProperty("原入库米数")
    private String receiveDiameter;

    @ApiModelProperty("生产组别（在纸质代码上预定义的生产组别）")
    private String workingGroup;

    @ApiModelProperty("实际克重")
    private String actPer;

    @ApiModelProperty("供应商简称")
    private String suppShortName;
}
