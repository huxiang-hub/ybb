package com.yb.rule.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author my
 * @date 2020-09-22
 * Description: 标准产能维度设置_yb_rule_scaleset
 */
@Data
@TableName("yb_rule_scaleset")
public class RuleScaleset implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 类型：数据字典：1施工单表、2物料表、3设备表、4
     */
    private Integer rsType;

    /**
     * 不同维度的表名；
     */
    private String rsTabname;

    /**
     * 对应的字段名；
     */
    private String rsColumn;

    /**
     * 对应返回的实体对象信息
     */
    private String rsObject;

    /**
     * 对应的sql对象字符串
     */
    private String rsDatasql;

    /**
     * 储备字段：关联条件内容
     */
    private String rsRelationsql;

    /**
     * 是否启用： 0停用1启用
     */
    private Integer isUsed;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
}
