package com.yb.workbatch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_workbatch_progress")
@ApiModel(value = "WorkbatchShift对象", description = "批次主计划进度_yb_workbatch_progress")
public class WorkbatchProgress implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private  Integer id;
    /**
     * 进度类型1、批次工单类型2、部件类型3、工序类型
     */
    @ApiModelProperty(value = "进度类型1、批次工单类型2、部件类型3、工序类型")
    private Integer wpType;
    /**
     * 批次id
     */
    @ApiModelProperty(value = "批次id")
    private Integer wbId;
    /**
     * 批次编号
     */
    @ApiModelProperty(value = "批次编号")
    private String wbNo;
    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String cmName;
    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id")
    private Integer pdId;
    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    /**
     * 部件id
     */
    @ApiModelProperty(value = "部件id")
    private Integer ptId;
    /**
     * 部件名称
     */
    @ApiModelProperty(value = "部件名称")
    private String ptName;
    /**
     * 工序id
     */
    @ApiModelProperty(value = "工序id")
    private Integer prId;
    /**
     * 工序名称
     */
    @ApiModelProperty(value = "工序名称")
    private String prName;

    /**
     * 排产id
     */
    @ApiModelProperty(value = "排产id")
    private Integer sdId;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    /**
     * 限制时间(截止时间)
     */
    @ApiModelProperty(value = "限制时间(截止时间)")
    private Date limitTime;
    /**
     *总计划时间(分钟)
     */
    @ApiModelProperty(value = "总计划时间(分钟)")
    private Integer totalTime;

    /**
     * 工序剩余时间(分钟)
     */
    @ApiModelProperty(value = "工序剩余时间(分钟)")
    private Integer stayTime;
    /**
     * 计划总量
     */
    @ApiModelProperty(value = "计划总量")
    private Integer planCount;
    /**
     * 已完成数量
     */
    @ApiModelProperty(value = "已完成数量")
    private Integer realCount;
    /**
     * 状态：0已导入1已排产2已生产3部分完成4全部完成5挂起6驳回
     */
    @ApiModelProperty(value = "状态：0已导入1已排产2已生产3部分完成4全部完成5挂起6驳回")
    private Integer status;
    /**
     * 预警状态1级36小时2级24小时
     */
    @ApiModelProperty(value = "预警状态1级36小时2级24小时")
    private Integer warning;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
    /**
     * 实际交货时间
     */
    @ApiModelProperty(value = "实际交货时间")
    private Date finishTime;

}
