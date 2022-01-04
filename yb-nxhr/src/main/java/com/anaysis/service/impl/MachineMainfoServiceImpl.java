package com.anaysis.service.impl;

import com.anaysis.entity.*;
import com.anaysis.mysqlmapper.MachineMainfoMapper;
import com.anaysis.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lzb
 * @date 2020-11-26
 */

@Service
public class MachineMainfoServiceImpl extends ServiceImpl<MachineMainfoMapper, MachineMainfo> implements IMachineMainfoService {

    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private HrMachineService hrMachineService;
    @Autowired
    private IProcessWorkinfoService processWorkinfoService;
    @Autowired
    private IMachineBsinfoService machineBsinfoService;
    @Autowired
    private IProcessMachlinkService processMachlinkService;
    @Autowired
    private IMachineClassifyService machineClassifyService;

    @Override
    public void syn() {
        List<String> myErpIds = machineMainfoMapper.getAllErpIds();
        List<String> hrErpIds = hrMachineService.getAllErpIds();
        List<String> addErpIds = hrErpIds.stream().filter(o -> !myErpIds.contains(o)).collect(Collectors.toList());
        List<MachineMainfo> machineMainfos = hrMachineService.getByErpIds(addErpIds);
        for (MachineMainfo machineMainfo : machineMainfos) {
            ProcessWorkinfo processWorkinfo = processWorkinfoService.getByErpId(machineMainfo.getErpId());
            if (null != processWorkinfo) {
                machineMainfo.setProId(processWorkinfo.getId());
            } else {
                // 查询设备对应工序
//                ProcessWorkinfo addProcess = hrProcessService.getByErpId(machineMainfo.getProId());
//                processWorkinfoService.save(addProcess);
//                machineMainfo.setProId(addProcess.getId());
            }
            MachineClassify machineClassify = new MachineClassify();
            // 保存设备型号
            machineClassifyService.save(machineClassify);
            machineMainfo.setMtId(machineClassify.getId());
            machineMainfoMapper.insert(machineMainfo);
            MachineBsinfo machineBsinfo = new MachineBsinfo();
            machineBsinfo.setMaId(machineMainfo.getId());
//            machineBsinfo.setSerialno();
//            machineBsinfo.setOutDate();
//            machineBsinfo.setWeight();
//            machineBsinfo.setSize();
//            machineBsinfo.setPower();
//            machineBsinfo.setVoltage();
//            machineBsinfo.setPhone();
//            machineBsinfo.setContact();
            // 保存设备扩展信息
            machineBsinfoService.save(machineBsinfo);
            ProcessMachlink processMachlink = new ProcessMachlink();
            processMachlink.setMaId(machineMainfo.getId());
            processMachlink.setPrId(machineMainfo.getProId());
            // 保存设备工序关联信息
            processMachlinkService.save(processMachlink);
        }
    }


    @Override
    public void sync() {
        List<MachineMainfo> machineMainfos;
        Map<String, Integer> maTypeMap = machineMainfoMapper.getAllMatype().stream().collect(Collectors.toMap(BladeDict::getDictValue, BladeDict::getDictKey, (k1, k2) -> k2));
        List<String> notImport = this.notImport();
        if (notImport.size() > 0) {
            machineMainfos = hrMachineService.getByErpIds(notImport);
        } else {
            return;
        }
        Map<String, Integer> prMap = processWorkinfoService.list().stream().collect(Collectors.toMap(ProcessWorkinfo::getPrName, ProcessWorkinfo::getId, (k1, k2) -> k2));
        for (MachineMainfo machineMainfo : machineMainfos) {
            Integer MaType = maTypeMap.get(machineMainfo.getPrCode());
            machineMainfo.setMaType(MaType.toString());
            // todo 现在先设定一个
            machineMainfo.setDpId(17);
            machineMainfo.setProId(prMap.get(machineMainfo.getPrCode()));
            machineMainfoMapper.insert(machineMainfo);
            ProcessMachlink processMachlink = new ProcessMachlink();
            processMachlink.setMaId(machineMainfo.getId());
            processMachlink.setPrId(machineMainfo.getProId());
            processMachlinkService.save(processMachlink);
        }
    }

    public List<String> notImport() {
        List<String> myErpIds = machineMainfoMapper.getAllErpIds();
        List<String> hrErpIds = hrMachineService.getAllErpIds();
        List<String> notImports = hrErpIds.stream().filter(o -> !myErpIds.contains(o)).collect(Collectors.toList());
        return notImports;
    }
}