package com.yb.panelapi.exeset.mapper;

import com.yb.machine.entity.MachineMixbox;
import com.yb.panelapi.exeset.entity.ExesetReadyWaste;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlindBoxInfoMapper {

    /*查询盒子信息yb_machinmixbox*/
    public List<MachineMixbox> getMixboxByMaId(Integer maId);

    ExesetReadyWaste getExesetReadyWaste(Integer maId);

    boolean setExesetReadyWaste(ExesetReadyWaste readyWaste);

}
