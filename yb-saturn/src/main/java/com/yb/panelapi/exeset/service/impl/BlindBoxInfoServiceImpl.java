package com.yb.panelapi.exeset.service.impl;

import com.yb.machine.entity.MachineMixbox;
import com.yb.panelapi.exeset.entity.ExesetReadyWaste;
import com.yb.panelapi.exeset.mapper.BlindBoxInfoMapper;
import com.yb.panelapi.exeset.service.IBlindBoxInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlindBoxInfoServiceImpl implements IBlindBoxInfoService {

    @Autowired
    private BlindBoxInfoMapper blindBoxInfoMapper;



    @Override
    public List<MachineMixbox> getMixboxByMaId(Integer maId) {

        return blindBoxInfoMapper.getMixboxByMaId(maId);
    }

    @Override
    public ExesetReadyWaste getExesetReadyWaste(Integer maId) {

        return blindBoxInfoMapper.getExesetReadyWaste(maId);
    }

    @Override
    public boolean setExesetReadyWaste(ExesetReadyWaste readyWaste) {

        return blindBoxInfoMapper.setExesetReadyWaste(readyWaste);
    }


}



