package com.yb.mater.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_mater_instore")
public class MaterInstore implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 物料关联
     */
    @ApiModelProperty(value = "物料关联")
    private Integer mbId;
    /**
     * 摆放位置
     */
    @ApiModelProperty(value = "摆放位置")
    private String location;
    /**
     * 实收数量
     */
    @ApiModelProperty(value = "实收数量")
    private Integer realacceptNum;
    /**
     * 板数（可选）
     */
    @ApiModelProperty(value = "板数（可选）")
    private String plateNum;
    /**
     * 收货人usid
     */
    @ApiModelProperty(value = "收货人usid")
    private Integer receiveUsid;
    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    private String contacterName;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 入库实际时间
     */
    @ApiModelProperty(value = "入库实际时间")
    private Date inTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
}
