package com.yb.stroe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 仓库管理_yb_store_area
 */
@Data
@TableName("yb_store_area")
public class StoreArea implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /*仓库分类（数据字典1、半成品2、成品 3、原料4、辅料5、备品备件）*/
    private String srType;
    /*库区编号*/
    private String srNo;
    /*库区名称*/
    private String srName;
    /*库总面积（单位：平方）*/
    private Double srArea;
    /*库总容量（单位：立方）*/
    private Double srCapacity;
    /*分几个区域*/
    private Integer srNum;
    /*区域类型：（排序类型：1、数字1.2.3  2、字母A.B.C)*/
    private Integer srRule;
    /*库区平面图名字*/
    private String srMap;
    /*平面图url*/
    private String srMapurl;
    /*平面缩小图url*/
    private String srNarrowurl;
    /*平面图物理地址*/
    private String srMappath;
    /*创建时间*/
    private Date createAt;
    /*更新时间*/
    private Date updateAt;
}
