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
 * 审核记录_yb_execute_spverify实体类
 *
 * @author BladeX
 * @since 2021-03-07
 */
@Data
@TableName("yb_execute_spverify")
@ApiModel(value = "ExecuteSpverify对象", description = "审核记录_yb_execute_spverify")
public class ExecuteSpverify implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 审核清单
     */
    @ApiModelProperty(value = "审核清单")
    private Integer spId;
    /**
     * 接收人-待审核
     */
    @ApiModelProperty(value = "接收人-待审核")
    private String acceptUsids;
    @ApiModelProperty(value = "发起流程id")
    private String processInstanceId;
    /**
     * 审核状态 1待审核2审核通过3审核驳回
     */
    @ApiModelProperty(value = "审核状态 1：审批中, 2：被终止, 3：完成, 4：取消")
    private Integer exStatus;
    /**
     * 审核意见
     */
    @ApiModelProperty(value = "审核意见")
    private String exApprove;
    /**
     * 审核操作人us_id的对象
     */
    @ApiModelProperty(value = "审核操作人us_id的对象")
    private Integer exOperator;
    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    private LocalDateTime exTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createAt;


}
