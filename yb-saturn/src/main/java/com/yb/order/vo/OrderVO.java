package com.yb.order.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/4/17.
 */
@Data
public class OrderVO {

    private Integer id;
    private String name;
    private Integer num;
    private Double orderTotal;
    private List<OrderVO> children = new ArrayList<>();

}

