package com.yb.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lzb
 * @Date 2021/2/27
 **/
@Data
@ApiModel(value = "手机端审核托盘对象")
public class PhoneTrayCardVO {

    @ApiModelProperty("仓库半成品id对象信息")
    private Integer siId;

    @ApiModelProperty("工单编号")
    private String odNo;

    @ApiModelProperty("托盘编号")
    private String tdNo;

    @ApiModelProperty("库位编号")
    private String stNo;

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

    @ApiModelProperty("托盘产品数量")
    private Integer etPdnum;

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




//    @ApiModelProperty("确认修改类型对应数量总和对象集合")
//    private List<ExMoldSum> exMoldSums;

//    @Data
//    @ApiModel(value = "确认修改类型对应数量总和对象")
//    public static
//    class ExMoldSum {
//
//        @ApiModelProperty(value = "修改类型1、盘点2、报废、3机长")
//        private Integer exMold;
//
//        @ApiModelProperty("审核前数量总和")
//        private Integer dataBeforeSum;
//
//        @ApiModelProperty("审核后数量总和")
//        private Integer dataAfterSum;
//    }

}
