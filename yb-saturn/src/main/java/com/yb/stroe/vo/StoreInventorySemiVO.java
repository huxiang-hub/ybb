package com.yb.stroe.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "仓库-台账-半成品对象")
public class StoreInventorySemiVO {

    @ApiModelProperty(value = "工单工序唯一标识")
    private Integer sdId;

    @ApiModelProperty(value = "仓储类型你：1、半成品2、成品 3、原料4、辅料5、备品备件")
    private Integer stType;

    @ApiModelProperty(value = "工序唯一标识ID")
    private String prId;

    @ApiModelProperty(value = "工序名称")
    private String prName;

    @ApiModelProperty(value = "产品名称")
    private String pdName;

    @ApiModelProperty(value = "产品编号")
    private String pdCode;

    @ApiModelProperty(value = "工单号")
    private String wbNo;

    @ApiModelProperty(value = "计划数")
    private Integer planNum;

    @ApiModelProperty(value = "已完成数")
    private Integer completeNum;

    @ApiModelProperty(value = "待排产数")
    private Integer arrangeNum;

    @ApiModelProperty(value = "库存数量")
    private String etPdnum;

    @ApiModelProperty(value = "工单工序托数")
    private Integer tyNum;

    @ApiModelProperty(value = "托盘的唯一标识集合Ids")
    private String tyIds;

}
