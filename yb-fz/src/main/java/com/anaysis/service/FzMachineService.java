package com.anaysis.service;

import com.anaysis.sqlservermapper.domain.FzMachineDO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FzMachineService {
    List<FzMachineDO> list(String iGzzxIds);
}
