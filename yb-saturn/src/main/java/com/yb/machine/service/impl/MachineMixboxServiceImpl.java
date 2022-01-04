package com.yb.machine.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.base.entity.BaseDeptinfo;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.entity.MachineMixbox;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.machine.mapper.MachineMixboxMapper;
import com.yb.machine.mapper.MainMixboxMapper;
import com.yb.machine.service.IMachineMixboxService;
import com.yb.machine.vo.MachineMixboxVO;
import com.yb.timer.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.exception.CommonException;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.saturn.entity.MainMixbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 印联盒（本租户的盒子），由总表分发出去 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
@Slf4j
public class MachineMixboxServiceImpl extends ServiceImpl<MachineMixboxMapper, MachineMixbox> implements IMachineMixboxService {

    @Autowired
    private MachineMixboxMapper mixboxMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private MainMixboxMapper mainMixboxMapper;


    @Override
    public IPage<MachineMixboxVO> selectMachineMixboxPage(IPage<MachineMixboxVO> page, MachineMixboxVO machineMixbox) {
        return page.setRecords(baseMapper.selectMachineMixboxPage(page, machineMixbox));
    }

    @Override
    public MachineMixbox selectBoxByBno(String bno) {
        return mixboxMapper.selectBoxByBno(bno);
    }

    @Override
    public int setMixboxByMaId(String uuid) {
        mixboxMapper.setMixboxByMaId(uuid);
        return 1;
    }

    @Override
    public int addMixboxByMaId(String boxId, Integer maId) {
        mixboxMapper.addMixboxByMaId(boxId, maId);
        return 1;
    }

    @Override
    public List getBlindBox() {
        return mixboxMapper.getBlindBox();
    }

    @Override
    public boolean setMixboxByListMaId(List<Integer> ids) {
        Integer result = mixboxMapper.setMixboxByListMaId(ids);
        return result != null && result >= 1;
    }

    @Override
    public IPage<MachineMixboxVO> getMachineMixboxPage(IPage page, MachineMixboxVO mixboxVO) {
        return page.setRecords(baseMapper.getMachineMixboxPage(page, mixboxVO));
    }


    @Override
    public List<BaseDeptinfo> getDeptInfo() {
        return mixboxMapper.getDeptInfo();
    }

    @Override
    public MachineMixboxVO findMixboxIsExit(String uuid) {
        return mixboxMapper.findMixboxIsExit(uuid);
    }

    @Override
    public List<MachineMainfo> selectMachineList() {
        /*查询所有设备信息*/
        List<MachineMainfo> machineMainfos = machineMainfoMapper.selectList(new QueryWrapper<>());
        /*查询所有盒子信息*/
        List<MachineMixbox> mixboxes = mixboxMapper.selectList(new QueryWrapper<>());
        if (!machineMainfos.isEmpty()) {
            Iterator<MachineMainfo> itera = machineMainfos.iterator();
            while (itera.hasNext()) {//遍历设备
                MachineMainfo machineMainfo = itera.next();
                if (!mixboxes.isEmpty()) {
                    Iterator<MachineMixbox> iterator = mixboxes.iterator();
                    while (iterator.hasNext()) {//遍历盒子
                        MachineMixbox mixbox = iterator.next();
                        if (machineMainfo.getId().equals(mixbox.getMaId())) {//如果该设备id在盒子中已存在,则干掉集合中的设备
                            itera.remove();
                            break;
                        }
                        machineMainfo.setId(machineMainfo.getId());

                    }
                }
            }
        }
        return machineMainfos;
    }

    @Override
    public void syncBox(String tenantId) {
        log.info("开始同步盒子数据,tenantId:{}", tenantId);
        if (Func.isBlank(tenantId)) {
            log.info("同步盒子数据失败，租户id为空：tenantId:{}", DBIdentifier.getProjectCode());
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "同步盒子数据失败,未找到租户信息");
        }
        List<MainMixbox> mainMixBoxList = mainMixboxMapper.findByTenantId(tenantId);
        if (!mainMixBoxList.isEmpty()) {
            //获取当前租户所在库的盒子
            List<MachineMixbox> mixBoxList = mixboxMapper.selectList(null);
            if (!mixBoxList.isEmpty()) {
                //获取本库不存在的盒子
                List<String> mixBoxUuids = mixBoxList.stream().map(MachineMixbox::getUuid).collect(Collectors.toList());
                mainMixBoxList = mainMixBoxList.stream().filter(o -> {
                    return !mixBoxUuids.contains(o.getUuid());
                }).collect(Collectors.toList());
                if (!mainMixBoxList.isEmpty()) {
                    mainMixBoxList.forEach(o->{
                        MachineMixbox machineMixbox = new MachineMixbox();
                        machineMixbox.setMaId(o.getMaId());
                        machineMixbox.setHdId(o.getHdId());
                        machineMixbox.setUuid(o.getUuid());
                        machineMixbox.setActive(o.getActive());
                        machineMixbox.setBatch(o.getBatch());
                        machineMixbox.setMac(o.getMac());
                        machineMixbox.setCreateAt(DateTimeUtil.now(DateTimeUtil.DEFAULT_DATE_FORMATTER));
                        machineMixbox.setRemark(o.getRemark());
                        machineMixbox.setDepository(o.getDepository());
                        mixboxMapper.insert(machineMixbox);
                    });
                }
            }
        }
        log.info("同步盒子数据成功,tenantId:{}", tenantId);
    }


    @Override
    public List<Integer> getMachineIds() {
        List<MachineMixbox> list = mixboxMapper.selectList(Wrappers.<MachineMixbox>lambdaQuery().select(MachineMixbox::getMaId));
        List<Integer> collect = list.stream().map(MachineMixbox::getMaId).collect(Collectors.toList());
        return collect;
    }
}
