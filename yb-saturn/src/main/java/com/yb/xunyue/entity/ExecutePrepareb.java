package com.yb.xunyue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 生产准备记录_yb_execute_prepareB实体类
 *
 * @author BladeX
 * @since 2021-03-30
 */
@Data
@TableName("yb_execute_prepareb")
@ApiModel(value = "ExecutePrepareb对象", description = "生产准备记录_yb_execute_prepareB")
public class ExecutePrepareb {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 设备唯一ID
     */
    @ApiModelProperty(value = "设备唯一ID")
    private Integer maId;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Integer usId;
    /**
     * 白班/夜班
     */
    @ApiModelProperty(value = "白班/夜班")
    private String wsId;
    /**
     * 执行状态ID
     */
    @ApiModelProperty(value = "执行状态ID")
    private Integer exId;
    /**
     * 接单B1保养B2换模B3
     */
    @ApiModelProperty(value = "接单B1保养B2换模B3")
    private String event;
    /**
     * 盒子开始张数
     */
    @ApiModelProperty(value = "盒子开始张数")
    private Integer startNum;
    /**
     * 盒子结束张数
     */
    @ApiModelProperty(value = "盒子结束张数")
    private Integer endNum;
    /**
     * 合计数量
     */
    @ApiModelProperty(value = "合计数量")
    private Integer totalNum;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startAt;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endAt;
    /**
     * 合计秒数计算
     */
    @ApiModelProperty(value = "合计秒数计算")
    private Integer totalTime;
    /**
     * 备注说明
     */
    @ApiModelProperty(value = "备注说明")
    private String remark;
    /**
     * 班组人员ids中间用竖线分隔|
     */
    @ApiModelProperty(value = "班组人员ids中间用竖线分隔|")
    private String usIds;
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
}
