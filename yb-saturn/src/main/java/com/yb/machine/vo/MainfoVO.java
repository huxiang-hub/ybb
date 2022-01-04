package com.yb.machine.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MainfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String label;// dpName
    private Integer value; // id
    private Integer maId; // id
}
