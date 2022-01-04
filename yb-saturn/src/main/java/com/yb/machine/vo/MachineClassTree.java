package com.yb.machine.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MachineClassTree implements Serializable {
    private static final long serialVersionUID = 1L;

    private String label;// dpName
    private Integer value; // id
    /**
     * 部门对应车间，二级间
     */
    private List<MainfoVO> children;
}
