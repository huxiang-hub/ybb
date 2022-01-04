package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础信息表_yb_base_staffext实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_base_staffext")
@ApiModel(value = "BaseStaffext对象", description = "基础信息表_yb_base_staffext")
public class    BaseStaffext implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    /**
     * 人员ID
     */
    @ApiModelProperty(value = "人员ID")
    private Integer sfId;
    /**
     * 性别 1男0女
     */
    @ApiModelProperty(value = "性别 1男0女")
    private Integer sex;
    /**
     * 学历：初中及以下1、高中2、大专3、本科4、硕士5、博士及以上6
     */
    @ApiModelProperty(value = "学历：初中及以下1、高中2、大专3、本科4、硕士5、博士及以上6")
    private Integer education;
    /**
     * 出生年月
     */
    @ApiModelProperty(value = "出生年月")
    private Date birthday;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idcard;
    /**
     * 身份证地址
     */
    @ApiModelProperty(value = "身份证地址")
    private String idaddr;
    /**
     * 籍贯（出生地）
     */
    @ApiModelProperty(value = "籍贯（出生地）")
    private String hometown;
    /**
     * 现居住址
     */
    @ApiModelProperty(value = "现居住址")
    private String curraddr;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    /**
     * 华博erpUUID
     */
    @ApiModelProperty(value = "华博erpUUID")
    private String erpId;
}
