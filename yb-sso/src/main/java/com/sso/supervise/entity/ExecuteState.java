package com.sso.supervise.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 执行表状态_yb_execute_state实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_execute_state")
@ApiModel(value = "ExecuteState对象", description = "执行表状态_yb_execute_state")
public class ExecuteState implements Serializable {

    private static final long serialVersionUID = 1L;

    public ExecuteState(){

    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单id；可选
     */
    @ApiModelProperty(value = "订单id；可选")
    private Integer odId;
    /**
     * 工单批次id
     */
    @ApiModelProperty(value = "工单批次id")
    private Integer wbId;
    /**
     * 排产id
     */
    @ApiModelProperty(value = "工单批次id")
    private Integer sdId;
    /**
     * 执行状态切换开始id信息
     */
    @ApiModelProperty(value = "执行状态切换开始id信息")
    private Integer ofId;
    /**
     * 操作人员
     */
    @ApiModelProperty(value = "操作人员")
    private Integer usId;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    /**
     * （1：上班、2：作业准备开始2-1---2-2完成、3生产准备完成、
     * 4：正式生产、5：下班、6：换班、 7: 其他工作
     * 11：停机、12：故障、,13：质检、14：换单、15：生产准备）
     */
    @ApiModelProperty(value = "人事A 生产准备B 正式生产C 结束生产D")
    private String status;

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
     * 持续时长秒为单位（sec）
     */
    @ApiModelProperty(value = "持续时长秒单位（sec）")
    private Double duration;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 机长
     */
    @ApiModelProperty(value = "机长")
    private Integer leaderId;
    /**
     * 人员ids中间用竖线分隔|
     */
    @ApiModelProperty(value = "人员ids中间用竖线分隔|")
    private String teamId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;


    private String event;


}
