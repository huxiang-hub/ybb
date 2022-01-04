package com.sso.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_base_staffclass")
@ApiModel(value = "BaseStaffclass对象", description = "人员班组临时调班_yb_base_staffclass")
public class BaseStaffclass implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 人员ID
     */
    @ApiModelProperty(value = "人员ID")
    private Integer usId;
    /**
     * 班组ID
     */
    @ApiModelProperty(value = "班组ID")
    private Integer bcId;
    /**
     * 状态：有效1无效0
     */
    @ApiModelProperty(value = "状态：有效1无效0")
    private Integer isUsed;
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
