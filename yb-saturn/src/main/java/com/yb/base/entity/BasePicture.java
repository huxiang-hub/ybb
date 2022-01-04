package com.yb.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_base_picture")
public class BasePicture implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /*图片类型：数据字典1、头像2、首检图片3、巡检图片btType*/
    @ApiModelProperty("图片类型：数据字典1、头像2、首检图片3、巡检图片btType")
    private String btType;
    /*对应表的主键ID信息，跟类型表关联'*/
    @ApiModelProperty("对应表的主键ID信息，跟类型表关联")
    private Integer btId;
    /*对话id(可选)*/
    @ApiModelProperty("对话id(可选)")
    private String talkId;
    /*图片标题名称*/
    @ApiModelProperty("图片标题名称")
    private String picTitle;
    /*图片尺寸200*100*/
    @ApiModelProperty("图片尺寸200*100")
    private String picSize;
    /*屏幕分辨率*/
    @ApiModelProperty("屏幕分辨率")
    private String picScreen;
    /*图片物理path路径*/
    @ApiModelProperty("图片物理path路径")
    private String picPath;
    /*图片访问url路径*/
    @ApiModelProperty("图片访问url路径")
    private String picUrl;
    /*图片缩略path路径*/
    @ApiModelProperty("图片缩略path路径")
    private String picMinpath;
    /*图片缩略访问url路径*/
    @ApiModelProperty("图片缩略访问url路径")
    private String picMinurl;
    /*操作用户id*/
    @ApiModelProperty("操作用户id")
    private Integer usId;
    /*用户名称*/
    @ApiModelProperty("用户名称")
    private String usName;
    /*创建时间*/
    @ApiModelProperty("创建时间")
    private Date createAt;
    /*更新时间*/
    @ApiModelProperty("更新时间")
    private Date updateAt;
    /*uuid 钉钉上传图片时使用*/
    @ApiModelProperty("uuid")
    private String uuid;
}
