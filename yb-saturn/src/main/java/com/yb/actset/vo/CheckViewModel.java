package com.yb.actset.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CheckViewModel implements Serializable {
    /**
     * 审核人名字
     */
    private String name;
    /**
     * 审核人的职位
     */
    private Integer jobs;
    /**
     * 审核人电话
     */
    private String phone;
    /**
     * 审核人回执信息
     */
    private String result;
    /**
     * 记录审核状态
     */
    private Integer status;
    /**
     * 审核时间
     */
    private Date checkTime;
    /**
     * 审核流程名字
     */
    private String awName;
}
