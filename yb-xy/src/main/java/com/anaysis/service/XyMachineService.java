package com.anaysis.service;

import com.anaysis.sqlservermapper.domain.XyMachineDO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface XyMachineService {
    List<XyMachineDO> list(String iGzzxIds);
}
