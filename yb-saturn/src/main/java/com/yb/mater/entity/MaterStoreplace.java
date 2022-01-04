package com.yb.mater.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_mater_storeplace")
public class MaterStoreplace implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /*库位类型1、机台型2、仓库型*/
    private Integer mpType;
    /*类型关联组件；比如1机台就关联设备id，仓库就关联仓库主键*/
    private Integer dbId;
    /*库位名称*/
    private String mpName;
    /*库位尺寸*/
    private String mpSize;
    /*是否启用*/
    private Integer isUsed;
    /*创建时间*/
    private Date createAt;
    /*更新时间*/
    private Date updateAt;
}
