package com.yb.order.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Node implements Serializable {
    private String uuid;

    private String reUuid;

    private String left;

    private String top;

    private boolean show;

    private Integer id;

    private String mlName;

    private String mlNo;

    private Integer mcId;

    private String material;

    private String model;

    private Integer mold;

    private String specification;

    private String size;

    private String brand;

    private String manufactor;

    private Integer islocal;

    private Integer isdel;

    private String createAt;

    private String updateAt;

    private String ico;

    private String name;

    private Integer type;

    private String mcName;

    private String ptUuid;

    private Integer mlNum;

    private Integer pid;

    private Integer pdId;

    private Integer ptType;

    private String ptName;

    private String ptNo;

    private Integer ptClassify;

    private String ptIds;

    private String parentId;

    private String parentName;

    private String wbId;

    private String prName;

    private String prNo;

    private Integer sort;

    private Integer status;

    private String prId;

    private String pyId;

    private String pyName;

    private String difficulty;

    private String lossRate;

    private String remarks;

    private String prParam;

}
