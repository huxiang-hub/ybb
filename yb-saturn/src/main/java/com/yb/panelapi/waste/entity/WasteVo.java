package com.yb.panelapi.waste.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author by SUMMER
 * @date 2020/3/15.
 */
@Data
public class WasteVo implements Serializable {
    //废品id
    private Long id;
    //废品类型
    private String wasteType;
    //某个工序的废品
    private Integer processId;
    //某个工序的废品
    private String processName;
    //备注信息
    private String remarks;
}
