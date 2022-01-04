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
@TableName("yb_prod_procelink")
@Accessors(chain = true)
@ApiModel(value = "产品对应工序关联表ProdProcelink实体")
public class ProdProcelink implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "部件id")
    @TableField("pt_id")
    private Integer ptId;

    @ApiModelProperty(value = "产品id")
    @TableField("pd_id")
    private Integer pdId;


    @ApiModelProperty(value = "工序id")
    @TableField("pr_id")
    private Integer prId;


    @ApiModelProperty(value = "工序要求")
    @TableField("remarks")
    private String remarks;


    @ApiModelProperty(value = "工序参数")
    @TableField("pr_param")
    private String prParam;


    @ApiModelProperty(value = "工序图片路径，可为空")
    @TableField("image_url")
    private String imageUrl;


    @ApiModelProperty(value = "工序损耗率")
    @TableField("waste_rate")
    private Double wasteRate;


    @ApiModelProperty(value = "难易程度")
    @TableField("diff_level")
    private Double diffLevel;


    @ApiModelProperty(value = "排序")
    @TableField("sort_num")
    private Integer sortNum;


    @ApiModelProperty(value = "关键点1起始工序2过程工序3合并工序4结束工序")
    @TableField("point")
    private Integer point;


    @ApiModelProperty(value = "工单id")
    @TableField("wb_id")
    private Integer wbId;


    @ApiModelProperty(value = "工单编号")
    @TableField("wb_no")
    private String wbNo;


    @ApiModelProperty(value = "是否启用1启用0停用")
    @TableField("is_used")
    private Integer isUsed;

}
