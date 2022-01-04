package com.anaysis.service.impl;

import com.anaysis.entity.MachineMainfo;
import com.anaysis.sqlservermapper.HrMachineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/25 11:52
 **/
@Service
public class HrMachineService {
    @Autowired
    private HrMachineMapper machineMapper;

    public List<String> getAllErpIds() {
        return machineMapper.getAllErpIds();
    }

    public List<MachineMainfo> getByErpIds(List<String> erpIds) {
        return machineMapper.getByErpIds(erpIds);
    }
}
