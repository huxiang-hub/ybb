package com.yb.workbatch.vo;

import lombok.Data;
import java.util.List;

@Data
public class RworkbatchOrdlinkAcceptVO {
    Integer usId;
    Integer maId;
    Integer wsId;
    List<Integer> usIds;
    Integer wfId;
}

