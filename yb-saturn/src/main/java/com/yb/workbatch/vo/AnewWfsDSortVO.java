package com.yb.workbatch.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 机台排序传入对象
 */
@Data
public class AnewWfsDSortVO implements Serializable {
    /*操作人id*/
    private Integer usId;
    /*操作人姓名*/
    private String userName;
    /*设备id*/
    private Integer maId;
    /*班次id*/
    private Integer wsId;
    /*排产日期*/
    private String wsSdDate;
    /*前一个排产单id*/
    private Integer preWfId;
    /*后一个排产单id*/
    private Integer suffixWfId;
    /*改变顺序的方式 是否交换1是交换2是插入*/
    private Integer isSwap;
}
