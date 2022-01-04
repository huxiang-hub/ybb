package com.yb.actset.vo;

import com.yb.actset.entity.ActsetCheckLog;
import lombok.Data;

@Data
public class ActsetCheckLogVO extends ActsetCheckLog {
    /*yb_actset_ckset流程表的主键*/
    private Integer asId;
    /**
     * 顺序
     */
    private Integer sort;
}
