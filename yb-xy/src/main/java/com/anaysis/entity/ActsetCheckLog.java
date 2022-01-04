package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_actset_checklog")
public class ActsetCheckLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设置流程id：A订单、B产品
     */
    @ApiModelProperty(value = "设置流程id：A订单、B产品")
    private Integer awId;
    /**
     * 审批主键ID
     */
    @ApiModelProperty(value = "审批主键ID")
    private Integer dbId;
    /**
     * 审批人
     */
    @ApiModelProperty(value = "审批人")
    private Integer usId;
    /**
     * 审批日期时间
     */
    @ApiModelProperty(value = "审批日期时间")
    private Date checkTime;
    /**
     * 审核结果
     */
    @ApiModelProperty(value = "审核结果")
    private String result;

    /**
     * 1待审核2超时3已审核
     */
    @ApiModelProperty(value = "1待审核2超时3已审核")
    private Integer status;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;

}
