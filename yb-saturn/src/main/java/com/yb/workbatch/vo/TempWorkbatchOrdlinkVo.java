package com.yb.workbatch.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/4/23.
 */
@Data
public class TempWorkbatchOrdlinkVo {

    private Integer maId;
    private String machineName;
    private List<WorkbatchOrdlinkVO> list = new ArrayList<>();
}
