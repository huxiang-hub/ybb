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
package com.yb.machine.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.machine.entity.MachineClassify;
import com.yb.machine.mapper.MachineClassifyMapper;
import com.yb.machine.service.IMachineClassifyService;
import com.yb.machine.vo.MachineClassifyVO;
import com.yb.system.dict.entity.Dict;
import com.yb.system.dict.vo.DictVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备型号_yb_mach_classify 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class MachineClassifyServiceImpl extends ServiceImpl<MachineClassifyMapper, MachineClassify> implements IMachineClassifyService {
    @Autowired
    private MachineClassifyMapper machineClassifyMapper;
    @Override
    public IPage<MachineClassifyVO> selectMachineClassifyPage(IPage<MachineClassifyVO> page, MachineClassifyVO machineClassify) {
        return page.setRecords(baseMapper.selectMachineClassifyPage(page, machineClassify));
    }

    @Override
    public MachineClassify getMachineInfo(Integer maId) {

        return machineClassifyMapper.getMachineInfo(maId);
    }
    @Override
    public boolean updateMachineInfo(MachineClassify info) {

        return machineClassifyMapper.updateMachineInfo(info);
    }

    @Override
    public List<MachineClassifyVO> getAllBrand() {
        return machineClassifyMapper.getAllBrand();
    }

    @Override
    public List<DictVO> listByMachine() {
        return machineClassifyMapper.listByMachine();
    }

}
