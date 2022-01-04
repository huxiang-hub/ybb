package com.anaysis.service;

import com.anaysis.entity.MachineEntity;
import com.anaysis.executSupervise.entity.ClearZeroEntity;
import com.anaysis.socket1.BladeData;
import com.anaysis.socket1.CollectData;
import com.anaysis.socket1.ConmachRs;
import org.springframework.stereotype.Service;

/****
 * mongodb数据库操作信息
 */
@Service
public interface MachineService {

    boolean addMachine(String tenantId, MachineEntity machineEntity);

    boolean addBasedb(String tenantId, CollectData coldata);

    boolean addBladeData(BladeData bladeData);

    boolean addConmachRs(ConmachRs conmachrs);

    boolean clearZero(ClearZeroEntity clearZeroEntity);
}
