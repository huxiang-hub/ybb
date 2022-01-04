package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author lzb
 * @date 2020-11-26
 */
@Data
@TableName("yb_prod_classify")
@Accessors(chain = true)
@ApiModel(value = "ProdClassify实体")
public class ProdClassify implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "产品分类名称")
    @TableField("cl_name")
    private String clName;


    @ApiModelProperty(value = "产品分类编号(两位01开始)")
    @TableField("classify")
    private String classify;


    @ApiModelProperty(value = "顺序，默认100")
    @TableField("sort")
    private Integer sort;


    @ApiModelProperty(value = "包含工序数量种类")
    @TableField("pr_num")
    private Integer prNum;


    @ApiModelProperty(value = "是否1启用0停用")
    @TableField("is_used")
    private Integer isUsed;


    @ApiModelProperty(value = "erp的id")
    @TableField("erp_id")
    private String erpId;

}
