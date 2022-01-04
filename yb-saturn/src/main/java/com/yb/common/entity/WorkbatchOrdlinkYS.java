package com.yb.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class WorkbatchOrdlinkYS implements Serializable {
    private static final long serialVersionUID = 1L;

    /*------------排产表所需字段-------------*/
    /**
     * 作业批次ID
     */
    @ApiModelProperty(value = "作业批次ID")
    private Integer wbId;
    /**
     * 流水号-设备id
     */
    @ApiModelProperty(value = "流水号-设备id")
    private Integer maId;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String odNo;

    /**
     * 工序ID
     */
    @ApiModelProperty(value = "工序ID")
    private Integer prId;
    /**
     * 工序名称
     */
    @ApiModelProperty(value = "工序名称")
    private String prName;
    /**
     * 部件id
     */
    @ApiModelProperty(value = "部件id")
    private Integer ptId;
    /**
     * 部件名称
     */
    @ApiModelProperty(value = "部件编号")
    private String ptNo;
    /**
     * 部件名称
     */
    @ApiModelProperty(value = "部件名称")
    private String ptName;

    /**
     * 产品编号
     */
    @ApiModelProperty(value = "产品编号")
    private String pdNo;
    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    /**
     * 产品图片
     */
    @ApiModelProperty(value = "产品图片")
    private String pdImageurl;
/*    *
     * 产品类型
    @ApiModelProperty(value = "产品类型")
    private String pdType;*/
    /**
     * 计划数量
     */
    @ApiModelProperty(value = "计划数量")
    private Integer planNum;
    /**
     * 计划数量-应交数量
     */
    @ApiModelProperty(value = "应交数量（计划数量+冗余数量）")
    private Integer planNumber;
    /**
     * 冗余数
     */
    @ApiModelProperty(value = "冗余数")
    private Integer extraNum;
    /**
     * 完成数量
     */
    @ApiModelProperty(value = "完成数量")
    private Integer completeNum;
    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date closeTime;
    /**
     * 计划用时
     */
    @ApiModelProperty(value = "计划用时(分)")
    private Integer planTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 车间id
     */
    @ApiModelProperty(value = "车间id")
    private Integer dpId;

    /**
     *上机规格
     */
    @ApiModelProperty(value = "上机规格")
    private String specification;//查询的规则
    /**
     *尺寸
     */
    @ApiModelProperty(value = "尺寸")
    private Integer size;//查询的规则
    /**
     *质量
     */
    @ApiModelProperty(value = "质量")
    private Integer material;//查询的规则

    /*--------剩余字段--------*/
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String cmName;
    /**
     *用料名称
     */
    @ApiModelProperty(value = "用料名称")
    private String materialName;
    /**
     *辅料名称
     */
    @ApiModelProperty(value = "辅料名称")
    private String assistMaterialName;
    /**
     *辅料数量
     */
    @ApiModelProperty(value = "辅料数量")
    private Integer assistMaterialNum;
    /**
     *进度
     */
    @ApiModelProperty(value = "进度")
    private String schedule;

    /**
     *版类
     */
    @ApiModelProperty(value = "版类")
    private String print;
    /**
     * 印色
     */
    @ApiModelProperty(value = "印色")
    private String colour;
    /**
     * 工艺流程
     */
    @ApiModelProperty(value = "工艺流程")
    private String technological;
    /**
     * CTP号
     */
    @ApiModelProperty(value = "CTP号")
    private String ctp;
    /**
     * 订单类型
     */
    @ApiModelProperty(value = "订单类型")
    private String orderType;
    /**
     * 总色数
     */
    @ApiModelProperty(value = "总色数")
    private Integer chromaticNumber;
    /**
     * 下资料袋时间
     */
    @ApiModelProperty(value = "下资料袋时间")
    private Date informationTime;

}
