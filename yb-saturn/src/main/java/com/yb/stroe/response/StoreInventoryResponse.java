package com.yb.stroe.response;

import com.yb.stroe.entity.StoreInventory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("仓库台账数据对应相关联数据信息")
public class StoreInventoryResponse extends StoreInventory {
    @ApiModelProperty("仓库半成品id对象信息")
    private Integer siId;

    @ApiModelProperty("工单编号")
    private String odNo;

    @ApiModelProperty("托盘编号")
    private String tdNo;

//    @ApiModelProperty("库位编号")
//    private String stNo;

    @ApiModelProperty("入库时间")
    private String handleTime;

    @ApiModelProperty("入库人真实姓名")
    private String realName;

    @ApiModelProperty("班次时间")
    private String sdDate;

    @ApiModelProperty("白班夜班")
    private String ckName;

    @ApiModelProperty("上报审核状态：0 未审核 1 通过")
    private String exStatus;

    @ApiModelProperty("上报表id")
    private Integer bfId;

    @ApiModelProperty("托盘id")
    private Integer tyId;

    @ApiModelProperty("第几拖")
    private String trayNo;

    @ApiModelProperty("产品名")
    private String pdName;

    @ApiModelProperty("设备名")
    private String maName;

    @ApiModelProperty("库位id")
    private Integer seatId;

//    @ApiModelProperty("托盘产品数量")
//    private Integer etPdnum;

    @ApiModelProperty("所有托盘产品总数量")
    private Integer etPdTotalNum;

    @ApiModelProperty("工单完成数量")
    private Integer completeNum;

    @ApiModelProperty("工单总数量")
    private Integer planNum;

    @ApiModelProperty("托盘修改前数量")
    private Integer dataBefore;

    @ApiModelProperty("托盘修改后数量")
    private Integer dataAfter;

    @ApiModelProperty("托盘修改前库位")
    private String storeBefore;

    @ApiModelProperty("托盘修改后库位")
    private String storeAfter;

}
