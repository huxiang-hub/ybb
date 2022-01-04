/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.saturn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.saturn.entity.BaseDeptinfo;
import org.springblade.saturn.entity.MachineMainfo;
import org.springblade.saturn.entity.Mixbox;
import org.springblade.saturn.mapper.MachineMainfoMapper;
import org.springblade.saturn.mapper.MixboxMapper;
import org.springblade.saturn.service.IMixboxService;
import org.springblade.saturn.vo.MixboxVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * 印联盒（本租户的盒子），由总表分发出去 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class MixboxServiceImpl extends ServiceImpl<MixboxMapper, Mixbox> implements IMixboxService {

    @Autowired
    private MixboxMapper mixboxMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;

    @Override
    public IPage<MixboxVO> selectMachineMixboxPage(IPage<MixboxVO> page, MixboxVO machineMixbox) {
        return page.setRecords(baseMapper.selectMachineMixboxPage(page, machineMixbox));
    }

    @Override
    public IPage<MixboxVO> getMachineMixboxPage(IPage page, MixboxVO mixboxVO) {
        return page.setRecords(baseMapper.getMachineMixboxPage(page,mixboxVO));
    }


    @Override
    public List<BaseDeptinfo> getDeptInfo() {
        return mixboxMapper.getDeptInfo();
    }

    @Override
    public MixboxVO findMixboxIsExit(String uuid) {
        return mixboxMapper.findMixboxIsExit(uuid);
    }

    @Override
    public List<MachineMainfo> selectMachineList() {
        /*查询所有设备信息*/
        List<MachineMainfo> machineMainfos = machineMainfoMapper.selectList(new QueryWrapper<>());
        /*查询所有盒子信息*/
        List<Mixbox> mixboxes = mixboxMapper.selectList(new QueryWrapper<>());
        if(!machineMainfos.isEmpty()){
            Iterator<MachineMainfo> itera = machineMainfos.iterator();
            while (itera.hasNext()){//遍历设备
                MachineMainfo machineMainfo = itera.next();
                if(!mixboxes.isEmpty()){
                    Iterator<Mixbox> iterator = mixboxes.iterator();
                    while (iterator.hasNext()){//遍历盒子
                        Mixbox mixbox = iterator.next();
                        if(mixbox.getMaId().equals(machineMainfo.getId())){//如果该设备id在盒子中已存在,则干掉集合中的设备
                            itera.remove();
                            break;
                        }
                        machineMainfo.setMaId(machineMainfo.getId());

                    }
                }
            }
        }
        return machineMainfos;
    }

}
