package com.yb.workbatch.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WorkbatchOeeShiftinVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    @ApiModelProperty(value = "工单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备名称
     */
    private String maName;

    private Integer ckId;
    /**
     * 排产id
     */
    @ApiModelProperty(value = "排产id")
    private Integer sdId;

    /**
     * 开始时间
     */
    private Date ckStartTime;
    /**
     * H类分类信息：/白班1/晚班2/夜班3
     */
    @ApiModelProperty(value = "H类分类信息：/白班1/晚班2/夜班3")
    private String ckName;
    /**
     * 结束时间
     */
    private Date ckEndTime;
    /**
     * 区间时间（秒）
     */
    private Integer ckStayTime;

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
    private String partName;
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
     * 工艺说明
     */
    @ApiModelProperty(value = "工艺说明")
    private String prDes;
    /**
     * 工作排序
     */
    @ApiModelProperty(value = "工序排序")
    private Integer sort;
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
    @ApiModelProperty(value = "应交数量（计划数量+冗余数量）")
    private Integer planNumber;
    /**
     * 完成数量
     */
    @ApiModelProperty(value = "完成数量")
    private Integer completeNum;

    /**
     * 冗余数
     */
    @ApiModelProperty(value = "冗余数")
    private Integer extraNum;
    /**
     * 状态（0起草1发布2正在生产3已完成4已挂起5废弃）
     */
    @ApiModelProperty(value = "状态（0起草1发布2正在生产3已完成4已挂起5废弃6已提交自审核7审核失败）")
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
     * 排产时间
     */
    @ApiModelProperty(value = "排产时间")
    private Date ordlinkTime;

    /**
     * 班次id
     */
    @ApiModelProperty(value = "班次id")
    private Integer wsId;

    /**
     * 生产排产设置OEE标准ID
     */
    private Integer oeeId;
    /**
     * 排产ID
     */
    @ApiModelProperty(value = "排产ID")
    private Integer wkId;
    /**
     * 输入部件id
     */
    @ApiModelProperty(value = "输入部件id")
    private Integer beforePtid;
    /**
     * 部件名称
     */
    @ApiModelProperty(value = "部件名称")
    private String beforePtname;
    /**
     * 部件编号
     */
    @ApiModelProperty(value = "部件编号")
    private String beforePtno;
    /**
     * 保养类型字典：1日保2周保3月保4季保5半年保6年保
     */
    @ApiModelProperty(value = "保养类型字典：1日保2周保3月保4季保5半年保6年保")
    private Integer maintain;
    /**
     * 保养时长（分）
     */
    @ApiModelProperty(value = "保养时长(分)")
    private Integer maintainStay;
    /**
     * 保养次数
     */
    @ApiModelProperty(value = "保养次数")
    private Integer maintainNum;
    /**
     * 换模时长（分）
     */
    @ApiModelProperty(value = "换模时长（分）")
    private  Integer mouldStay;
    /**
     * 换模次数
     */
    @ApiModelProperty(value = "换模次数")
    private Integer mouldNum;
    /**
     * 吃饭时间（分
     */
    @ApiModelProperty(value = "吃饭时间（分）")
    private Integer mealStay;
    /**
     * 吃饭次数
     */
    @ApiModelProperty(value = "吃饭次数")
    private Integer mealNum;

    /**
     * 质检次数
     */
    @ApiModelProperty(value = "质检次数")
    private Integer qualityNum;

    /**
     * 质检次数
     */
    @ApiModelProperty(value = "难易程度")
    private Integer difficultNum;

    /**
     *生产准备时间
     */
    private  Integer producePreTime;
}
