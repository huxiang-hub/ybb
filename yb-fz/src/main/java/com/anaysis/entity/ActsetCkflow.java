package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_actset_ckflow")
public class ActsetCkflow implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 流程设置唯一主键id
     */
    @ApiModelProperty(value = "流程设置唯一主键id")
    private Integer asId;
    /**
     * 审批名称
     */
    @ApiModelProperty(value = "审批名称")
    private String awName;
    /**
     * 类型订单：sale销售、技术technology、计划plan、storage仓储、生产produce
     */
    @ApiModelProperty(value = "类型订单：sale销售、技术technology、计划plan、storage仓储、生产produce")
    private String awType;
    /**
     * 审批对象；1、固定人2提交人上级3指定部门负责人
     */
    @ApiModelProperty(value = "审批对象；1、固定人2提交人上级3指定部门负责人")
    private Integer target;
    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Integer dpId;
    /**
     * 审批人id
     */
    @ApiModelProperty(value = "审批人id")
    private Integer usId;
    /**
     * 审批期限记录小时一天24小时
     */
    @ApiModelProperty(value = "审批期限记录小时一天24小时")
    private Integer limitTime;
    /**
     * 是否必填理由 0否1是
     */
    @ApiModelProperty(value = "是否必填理由 0否1是")
    private Integer isReason;
    /**
     * 先后顺序
     */
    @ApiModelProperty(value = "先后顺序")
    private Integer sort;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;


}
