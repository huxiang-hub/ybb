package com.yb.execute.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 审核清单_yb_execute_scrap实体类
 *
 * @author BladeX
 * @since 2021-03-08
 */
@Data
@TableName("yb_execute_scrap")
@ApiModel(value = "ExecuteScrap对象", description = "审核清单_yb_execute_scrap")
public class ExecuteScrap implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "生产批次号")
    private String wbNo;

    @ApiModelProperty(value = "生产工单")
    private Integer sdId;

    @ApiModelProperty(value = "排程id")
    private Integer wfId;

    @ApiModelProperty(value = "执行单id")
    private Integer exId;

    @ApiModelProperty(value = "审核类型1、盘点2、报废、3机长")
    private Integer spMold;

    @ApiModelProperty(value = "审核前总数")
    private Integer spBefore;

    @ApiModelProperty(value = "审核后总数")
    private Integer spAfter;

    @ApiModelProperty(value = "申请报废数量")
    private Integer spNum;

    @ApiModelProperty(value = "报废原因；下拉选择；选项：数据字典选择，如果有其他，增加到sp_other")
    private String spReason;

    @ApiModelProperty(value = "其他原因")
    private String spOther;

    @ApiModelProperty(value = "托盘id集合S，中间用逗号分隔")
    private String tyIds;

    @ApiModelProperty(value = "班次id")
    private Integer wsId;

    @ApiModelProperty(value = "操作提交人")
    private Integer usId;

    @ApiModelProperty(value = "审核流程：提交")
    private String exDesc;

    @ApiModelProperty(value = "图片ids")
    private String imagIds;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateAt;


}
