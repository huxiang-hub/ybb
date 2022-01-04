package com.yb.process.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PyModelVO implements Serializable {
    /**
     * 工序类型ID
     */
    private Integer pyId;
    /**
     * 工序类型名字
     */
    private String pyName;

    /**
     * 类型下面的工序
     */
    List<PrModelVO> proModelVOS = new ArrayList<>();
    /***
     *
     */
    private String proType;
}
