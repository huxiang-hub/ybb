package com.yb.mater.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 入库数据
 */
@Data
public class BePutInStorageVO implements Serializable {

    private Integer id;
    private Integer status;
}
