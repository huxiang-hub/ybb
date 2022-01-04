package com.yb.workbatch.vo;

import lombok.Data;

/**
 * 用于计算平方数
 */
@Data
public class SquareVO {

    private String operateSize;//上机尺寸
    private Integer productNum;//上报作业数
    private Integer handleUsid;//上报人id
    private Integer wfId;//排产班次id
}
