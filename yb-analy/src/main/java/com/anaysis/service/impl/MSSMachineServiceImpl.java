package com.anaysis.service.impl;

import com.anaysis.entity.MMSMachineEntity;
import com.anaysis.fz.mapper.MachineMapper;
import com.anaysis.service.MSSMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MSSMachineServiceImpl implements MSSMachineService {

    @Autowired
    private MachineMapper machineMapper;

    @Override
    public int addMachine(MMSMachineEntity mmsMachineEntity) {
        return machineMapper.save(mmsMachineEntity);
    }
}
