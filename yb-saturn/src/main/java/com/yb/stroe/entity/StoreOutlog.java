package com.yb.stroe.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lzb
 * @date 2020-09-19
 */
@Data
@TableName("yb_store_outlog")
@Accessors(chain = true)
@ApiModel(value = "库位出库管理_yb_store_outlogStoreOutlog实体")
public class StoreOutlog implements Serializable {


    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "存储类型（数据字典1、半成品2、成品 3、原料4、辅料5、备品备件）")
    @TableField("st_type")
    private Integer stType;


    @ApiModelProperty(value = "库位id")
    @TableField("st_id")
    private Integer stId;


    @ApiModelProperty(value = "库位编号")
    @TableField("st_no")
    private String stNo;


    @ApiModelProperty(value = "入库尺寸")
    @TableField("st_size")
    private String stSize;


    @ApiModelProperty(value = "物料ID(可选)")
    @TableField("ml_id")
    private String mlId;


    @ApiModelProperty(value = "托盘标识卡ID")
    @TableField("et_id")
    private Integer etId;


    @ApiModelProperty(value = "托盘占用位置数量；单位：板")
    @TableField("lay_num")
    private Integer layNum;

    @ApiModelProperty(value = "托板产品数量")
    @TableField("et_pdnum")
    private Integer etPdnum;


    @ApiModelProperty(value = "操作人id")
    @TableField("us_id")
    private Integer usId;


    @ApiModelProperty(value = "操作类型：1正常入库，2先出库后生成入库, 3盘点出库")
    @TableField("operate_type")
    private Integer operateType;


    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    private LocalDateTime createAt;

    @ApiModelProperty(value = "半成品类型：1托盘、2工单数")
    @TableField("st_model")
    private Integer stModel;


    @ApiModelProperty(value = "1托盘唯一标识et_id、2上报数唯一标识bf_id")
    @TableField("db_id")
    private Integer dbId;

}
