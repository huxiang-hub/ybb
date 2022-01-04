package com.yb.order.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Line implements Serializable {

    private String from;

    private String to;
}
