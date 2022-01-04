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
@TableName("yb_store_morelog")
@Accessors(chain = true)
@ApiModel(value = "盘盈管理_yb_store_morelogStoreMorelog实体")
public class StoreMorelog implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "托盘标识卡ID")
    @TableField("et_id")
    private Integer etId;


    @ApiModelProperty(value = "托盘占用位置数量；单位：板")
    @TableField("lay_num")
    private Integer layNum;


    @ApiModelProperty(value = "当前库数量（需要修改台账；并且更新标识卡信息）")
    @TableField("et_pdnum")
    private Integer etPdnum;


    @ApiModelProperty(value = "盘点前库数量")
    @TableField("bef_pdnum")
    private Integer befPdnum;


    @ApiModelProperty(value = "数据差")
    @TableField("diff_num")
    private Integer diffNum;


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

}
