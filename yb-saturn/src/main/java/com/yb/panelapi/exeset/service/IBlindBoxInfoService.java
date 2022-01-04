package com.yb.panelapi.exeset.service;


import com.yb.machine.entity.MachineMixbox;
import com.yb.panelapi.exeset.entity.ExesetReadyWaste;

import java.util.List;

public interface IBlindBoxInfoService {
    /*查询盒子信息yb_machinmixbox*/
    public List<MachineMixbox> getMixboxByMaId(Integer maId);
    ExesetReadyWaste getExesetReadyWaste(Integer maId);

    boolean setExesetReadyWaste(ExesetReadyWaste readyWaste);

}
