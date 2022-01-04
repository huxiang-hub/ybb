package com.yb.quality.vo;

import lombok.Data;

@Data
public class InsertTableVO {

    /*工序分类id*/
    private Integer pyId;
    /*工序分类名称*/
    private String pyName;
    /*检查类型*/
    private String checkType;
    /*字段名*/
    private String colInfo;
}
