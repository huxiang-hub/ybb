package com.yb.workbatch.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class WorkbatchOrdlinkExcel {
    /**
     * 工单ID
     */
    @ApiModelProperty(value = "工单ID")
    private Integer id;
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
     * ERP工单（待定）
     */
    @ApiModelProperty(value = "ERP工单（待定）")
    private Integer erpWbId;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String odNo;
    /**
     * 部件名称
     */
    @ApiModelProperty(value = "部件名称")
    private String partName;
    /**
     * 工作名称
     */
    @ApiModelProperty(value = "工序名称")
    private String prName;
    /**
     * 工艺说明
     */
    @ApiModelProperty(value = "工艺说明")
    private String prDes;
    /**
     * 产品编号
     */
    @ApiModelProperty(value = "产品编号")
    private String pdCode;
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
    /**
     * 产品类型
     */
    @ApiModelProperty(value = "产品类型")
    private String pdType;
    /**
     * 计划数量
     */
    @ApiModelProperty(value = "计划数量")
    private Integer planNum;
    /**
     * 计划数量-应交数量
     */
    @ApiModelProperty(value = "计划数量-应交数量")
    private Integer planNumber;
    /**
     * 完成数量
     */
    @ApiModelProperty(value = "完成数量")
    private Integer completeNum;
    /**
     * 未完成数量
     */
    @ApiModelProperty(value = "未完成数量")
    private Integer incompleteNum;
    /**
     * 冗余数
     */
    @ApiModelProperty(value = "冗余数")
    private Integer extraNum;
    /**
     * 状态（0起草1发布2正在生产3已完成4已挂起5废弃）
     */
    @ApiModelProperty(value = "状态（0起草1发布2正在生产3已完成4已挂起5废弃）")
    private String status;
    /**
     * 0:待接单，1：生产中，2：生产完成
     */
    @ApiModelProperty(value = "0:待接单，1：生产中，2：生产完成")
    private Integer runStatus;
    /**
     * 计划开始时间
     */
    @ApiModelProperty(value = "计划开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    /**
     * 实际开始时间
     */
    @ApiModelProperty(value = "实际开始时间")
    private Date actuallyStarttime;
    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    private Date closeTime;
    /**
     * 计划用时
     */
    @ApiModelProperty(value = "计划用时")
    private Integer planTime;
    /**
     * 废品
     */
    @ApiModelProperty(value = "废品")
    private Integer waste;
    /**
     * 废品率
     */
    @ApiModelProperty(value = "废品率")
    private Integer nbwaste;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date updateAt;
    /**
     * user——id
     */
    @ApiModelProperty(value = "用户id")
    private Date user_id;
    /**
     * 订单名称
     */
    @ApiModelProperty(value = "订单名称")
    private String odName;

    /**
     * 订单数量
     */
    @ApiModelProperty(value = "订单数量")
    private Integer odCount;
    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String contractNum;
    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    /**
     * 预计废品
     */
    @ApiModelProperty(value = "预计废品")
    private Integer wasteCount;
    /**
     * 订货厂家
     */
    @ApiModelProperty(value = "订货厂家")
    private String indentor;
    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date limitDate;
    /**
     * 审核状态 -1草稿  0提交至技术中心  1 提交至工艺审核  2 审核中  3审核通过
     */
    @ApiModelProperty(value = "审核状态 -1草稿  0提交至技术中心  1 提交至工艺审核  2 审核中  3审核通过  ")
    private Integer auditStatus;
    /**
     * 生产状态   0 未执行  1 正在执行 2 已完成
     */
    @ApiModelProperty(value = "生产状态   0 未执行  1 正在执行 2 已完成")
    private Integer productionState;
    /**
     * 产品编号
     */
    @ApiModelProperty(value = "产品编号")
    private String pdNo;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private Integer odId;
    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;
    /**
     * 是否启用1是0否
     */
    @ApiModelProperty(value = "是否启用1是0否")
    private Integer isUsed;
    /**
     * 工序展示图
     */
    @ApiModelProperty(value = "工序展示图")
    private String procePic;
}