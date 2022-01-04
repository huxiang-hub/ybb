package com.yb.order.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/4/17.
 */
@Data
public class WorkBatchVO {

    private String batchNo;
    private Double batchTotal;
    private List<ProcessVO> children = new ArrayList<>();
}
