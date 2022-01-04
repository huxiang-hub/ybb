package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("yb_prod_classify")
@ApiModel(value = "ProdClassify对象", description = " 产品分类表")
public class ProdClassify implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;
    /**产品分类名称*/
    @ApiModelProperty(value = "产品分类名称")
    String clName;
    /** 产品分类编号(两位01开始)*/
    @ApiModelProperty(value = "产品分类编号(两位01开始)")
    String classify;
    /** 顺序，默认100'*/
    @ApiModelProperty(value = "顺序，默认100'")
    Integer sort;
    /**包含工序数量种类' */
    @ApiModelProperty(value = "包含工序数量种类'")
    Integer prNum;
    /** 是否1启用0停用*/
    @ApiModelProperty(value = "是否1启用0停用")
    Integer isUsed;
    /**
     * 华博erp的uuid
     */
    @ApiModelProperty(value = "华博erp的uuid")
    String erpId;

}
