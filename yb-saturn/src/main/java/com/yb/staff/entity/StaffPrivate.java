package com.yb.staff.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_staff_private")
@ApiModel(value = "yb_staff_private")
public class StaffPrivate implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer usId;
    /**
     * 类型1表样式2
     */
    @ApiModelProperty(value = "类型1表样式2")
    private Integer model;
    /**
     * 表唯一标识
     */
    @ApiModelProperty(value = "表唯一标识", required = true)
    private String tabId;
    /**
     * 功能属性标识
     */
    @ApiModelProperty(value = "功能属性标识", required = true)
    private String funKey;
    /**
     * 显示属性json串
     */
    @ApiModelProperty(value = "显示属性json串")
    private String tabAttribute;
    /**
     * 是否启用0禁用,1启用
     */
    @ApiModelProperty(value = "是否启用0禁用,1启用")
    private Integer isUsed;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
}
