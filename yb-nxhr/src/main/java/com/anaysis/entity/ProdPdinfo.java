package com.anaysis.entity;

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
 * @date 2020-11-26
 */
@Data
@TableName("yb_prod_pdinfo")
@Accessors(chain = true)
@ApiModel(value = "产品信息（product）ProdPdinfo实体")
public class ProdPdinfo implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "产品名称")
    @TableField("pd_name")
    private String pdName;


    @ApiModelProperty(value = "产品编号")
    @TableField("pd_no")
    private String pdNo;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;


    @ApiModelProperty(value = "图片地址")
    @TableField("image_url")
    private String imageUrl;


    @ApiModelProperty(value = "工序展示图")
    @TableField("proce_pic")
    private String procePic;


    @ApiModelProperty(value = "是否启用1是0否")
    @TableField("is_used")
    private Integer isUsed;


    @ApiModelProperty(value = "模型对象的记录")
    @TableField("model_json")
    private String modelJson;


    @ApiModelProperty(value = "产品分类")
    @TableField("pc_id")
    private Integer pcId;


    @ApiModelProperty(value = "ERP的UUID")
    @TableField("erp_id")
    private String erpId;


    @ApiModelProperty(value = "产品规格")
    @TableField("pd_size")
    private String pdSize;

    @ApiModelProperty(value = "部件类型")
    @TableField(exist = false)
    private Integer ptType;

    @ApiModelProperty(value = "客户id")
    @TableField(exist = false)
    private String custId;

}
