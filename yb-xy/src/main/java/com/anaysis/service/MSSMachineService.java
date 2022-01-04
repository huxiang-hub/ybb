package com.anaysis.service;

import com.anaysis.entity.MMSMachineEntity;
import org.springframework.stereotype.Service;

@Service
public interface MSSMachineService {
    int addMachine(MMSMachineEntity mmsMachineEntity);
}
