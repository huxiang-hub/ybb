package com.sso.panelapi.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.mapper.MachineMainfoMapper;
import com.sso.panelapi.entity.MachineMainfo;
import com.sso.panelapi.service.IMachineMainfoService;
import com.sso.panelapi.vo.CompanyVO;
import com.sso.panelapi.vo.MachineMainfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备_yb_mach_mainfo 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class MachineMainfoServiceImpl extends ServiceImpl<MachineMainfoMapper, MachineMainfo> implements IMachineMainfoService {
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;

    @Override
    public IPage<MachineMainfoVO> selectMachineMainfoPage(IPage<MachineMainfoVO> page,
                                                          Integer dpId, Integer prId, Integer status) {
        return page.setRecords(baseMapper.selectMachineMainfoPage(page, dpId, prId, status));
    }

    @Override
    public MachineMainfoVO getMachineMainfo(Integer maId) {
        return machineMainfoMapper.getMachineMainfo(maId);
    }

    @Override
    public MachineMainfoVO getMachineMainfoById(Integer id) {
        return machineMainfoMapper.getMachineMainfoById(id);
    }

    @Override
    public boolean updateMachineMaInfo(MachineMainfo info) {

        return machineMainfoMapper.updateMachineMaInfo(info);
    }

    @Override
    public List<MachineMainfo> getMachins(String name) {

        return machineMainfoMapper.getMachins(name);
    }

    @Override
    public List<MachineMainfoVO> getMachinsByDpIdPrId(Integer maId,Integer dpId,Integer prId, Integer status) {
        List<MachineMainfoVO> oldList = machineMainfoMapper.getMachinsByDpIdPrId(maId,dpId, prId, status);
        List<MachineMainfoVO> newList = new ArrayList<>();
        for (MachineMainfoVO mv : oldList) {
            mv.setId(mv.getMaId());//让组件id 显是MaId
            mv.setLabel(mv.getMaName());//让组件Label 显是MaName
            mv.setValue(mv.getMaId());//让组件Value 显是MaId
            newList.add(mv);
        }
        return newList;
    }

    @Override

    public IPage<MachineMainfoVO> getAllMachinePages(IPage page, MachineMainfoVO mainfoVO) {
        return page.setRecords(baseMapper.getAllMachinePages(page, mainfoVO));
    }

    @Override
    public MachineMainfoVO getMachineInfoByCondition(String machineName, String machineNo) {
        return machineMainfoMapper.getMachineInfoByCondition(machineName,machineNo);

    }

    @Override
    public List<MachineMainfoVO> getRateByMachineId(Integer maId,String prName) {
        return machineMainfoMapper.getRateByMachineId(maId,prName);
    }

    @Override
    public List<MachineMainfoVO> selectMaNames(Integer prId) {
        return machineMainfoMapper.getMachinsByDpIdPrId(null, null, prId, null);
    }

    @Override
    public List<MachineMainfoVO> getMachineByDpId(Integer dpId) {
        List<MachineMainfoVO> oldList = machineMainfoMapper.getMachineByDpId(dpId);
        List<MachineMainfoVO> newList = new ArrayList<>();
        for (MachineMainfoVO mv : oldList) {
            mv.setId(mv.getMaId());//让组件id 显是MaId
            mv.setLabel(mv.getMaName()+"【"+(mv.getPrName()==null?"暂无":mv.getPrName())+"】");//让组件Label 显是MaName
            mv.setValue(mv.getMaId());//让组件Value 显是MaId
            newList.add(mv);
        }
        return newList;
    }

    @Override
    public CompanyVO getCompanyInfoByMaId(Integer maId) {

        return baseMapper.getCompanyInfoByMaId(maId);
    }

    @Override
    public List<MachineMainfo> machineListByDeptId(Integer prId, Integer dpId) {
        return baseMapper.machineListByDeptId(prId, dpId);
    }

}
