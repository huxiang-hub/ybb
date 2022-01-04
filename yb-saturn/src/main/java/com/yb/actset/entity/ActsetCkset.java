package com.yb.actset.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_actset_ckset")
public class ActsetCkset implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 流程名称：A订单B产品
     */
    @ApiModelProperty(value = "流程名称：A订单B产品")
    private String asType;
    /**
     * 审批数据表table;关联视图
     */
    @ApiModelProperty(value = "审批数据表table;关联视图")
    private String dataSource;
    /**
     * 审批级数支持多级审批
     */
    @ApiModelProperty(value = "审批级数支持多级审批")
    private Integer leve;
    /**
     * 是否停用1启用0停用
     */
    @ApiModelProperty(value = "是否停用1启用0停用")
    private Integer isUsed;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;

}
