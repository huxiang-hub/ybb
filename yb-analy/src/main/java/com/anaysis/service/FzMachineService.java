package com.anaysis.service;

import com.anaysis.entity.FzMachineDO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FzMachineService {
    List<FzMachineDO> list(String iGzzxIds);
}
