package com.anaysis.service.impl;

import com.anaysis.entity.MMSMachineEntity;
import com.anaysis.service.MSSMachineService;
import com.anaysis.sqlservermapper.MachineDAO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MSSMachineServiceImpl implements MSSMachineService {

    @Resource
    private MachineDAO machineDAO;

    @Override
    public int addMachine(MMSMachineEntity mmsMachineEntity) {
        return machineDAO.save(mmsMachineEntity);
    }
}
