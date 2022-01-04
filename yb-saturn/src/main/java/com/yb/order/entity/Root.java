package com.yb.order.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Root implements Serializable {
    private String name;

    private List<Node> nodeList;

    private List<Line> lineList;
}
