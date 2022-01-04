package com.yb.stroe.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author lzb
 * @date 2020-09-22
 */
@Data
@TableName("yb_store_checklog")
@Accessors(chain = true)
@ApiModel(value = "盘点库存管理_yb_store_checklogStoreChecklog实体")
public class StoreChecklog implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "存储类型（数据字典1、半成品2、成品 3、原料4、辅料5、备品备件）")
    @TableField("st_type")
    private Integer stType;


    @ApiModelProperty(value = "盘点类型（数据字典：日盘、周盘、月盘）")
    @TableField("sk_type")
    private Integer skType;


    @ApiModelProperty(value = "库位id")
    @TableField("st_id")
    private Integer stId;


    @ApiModelProperty(value = "库位编号")
    @TableField("st_no")
    private String stNo;

    @ApiModelProperty(value = "标识卡id")
    @TableField("et_id")
    private Integer etId;


    @ApiModelProperty(value = "盘点人员")
    @TableField("us_id")
    private Integer usId;


    @ApiModelProperty(value = "状态：1数量盘盈2数量盘亏3盘错位置4盘点正常(4可选)")
    @TableField("status")
    private Integer status;


    @ApiModelProperty(value = "人员名称")
    @TableField("us_name")
    private String usName;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;


    @ApiModelProperty(value = "更新时间")
    @TableField("update_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;


    @ApiModelProperty(value = "是否复核0否1是")
    @TableField("is_review")
    private Integer isReview;


    @ApiModelProperty(value = "复核人")
    @TableField("rw_usid")
    private Integer rwUsid;


    @ApiModelProperty(value = "复核姓名")
    @TableField("rw_usname")
    private String rwUsname;


    @ApiModelProperty(value = "复核时间")
    @TableField("rw_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rwTime;

}
