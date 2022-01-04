package com.anaysis.service.impl;

import com.anaysis.entity.MachineMainfo;
import com.anaysis.mysqlmapper.BaseClassinfoMapper;
import com.anaysis.mysqlmapper.MachineMainfoMapper;
import com.anaysis.sqlservermapper.HrWorkShiftMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anaysis.entity.BaseClassinfo;
import com.anaysis.service.IBaseClassinfoService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author lzb
 * @date 2020-12-10
 */

@Service
public class BaseClassinfoServiceImpl extends ServiceImpl<BaseClassinfoMapper, BaseClassinfo> implements IBaseClassinfoService {

    @Autowired
    private HrWorkShiftMapper hrWorkShiftMapper;
    @Autowired
    private BaseClassinfoMapper baseClassinfoMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;

    /**
     * 一次同步所有班组
     */
    public void sync() {
        List<BaseClassinfo> list = hrWorkShiftMapper.getAll();
        List<MachineMainfo> machineMainfos = machineMainfoMapper.selectList(null);
        Map<String, Integer> machineMap = machineMainfos.stream().collect(Collectors.toMap(MachineMainfo::getErpId, MachineMainfo::getDpId,(k1, k2) -> k2));
        for (BaseClassinfo baseClassinfo : list) {
            String erpMachineId = baseClassinfo.getErpMachineId();
            Integer dpId = machineMap.get(erpMachineId);
            if (null != dpId) {
                baseClassinfo.setDpId(dpId);
            } else {
                // 待定车间
                baseClassinfo.setDpId(17);
            }
            baseClassinfo.setIsUsed(1);
            baseClassinfoMapper.insert(baseClassinfo);
        }
    }
}