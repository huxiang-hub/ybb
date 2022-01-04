package com.yb.process.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProModelVO implements Serializable {

    /**
     * 工序id
     */
    private Integer id;
    /**
     * 工序名称
     */
    private String prName;
    /**
     * 工序编号
     */
    private String prNo;
    /**
     * 设备名字
     */
    private List<String> maName;
    /**
     * 员工总数
     */
    private Integer staffSum;

}
