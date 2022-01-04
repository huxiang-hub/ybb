package com.yb.rule.entity;

import lombok.Data;

/**
 * 查询条件
 */
@Data
public class RuleCondition {
    /*数量*/
    private Integer planNum;
    /*尺寸*/
    private Integer size;
    /*物料质量*/
    private Integer material;
    /*设备id*/
    private Integer maId;

    private Integer model;
}
