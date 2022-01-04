package com.yb.prod.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("yb_prod_procdiff")
@ApiModel(value = "procdif", description = " 工艺难易程度")
public class ProdDiff implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;
    //产品分类Id
    private Integer pcId;
    //工序ID
    private Integer prId;
    //难度
    private Double diff;
    /** 是否1启用0停用*/
    Integer isUsed;
}
