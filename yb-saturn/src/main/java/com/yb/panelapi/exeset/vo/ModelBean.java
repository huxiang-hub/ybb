package com.yb.panelapi.exeset.vo;

import com.yb.exeset.entity.ExesetFault;
import com.yb.exeset.entity.ExesetQuality;
import lombok.Data;

@Data
public class ModelBean {

    private ExesetFault fault;
    private ExesetQuality quality;
}
