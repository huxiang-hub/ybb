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
import com.yb.machine.entity.MachineScreen;
import com.yb.machine.mapper.MachineScreenMapper;
import com.yb.machine.service.IMachineScreenService;
import com.yb.machine.vo.MachineScreenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备关联屏幕（主系统同步数据） 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class MachineScreenServiceImpl extends ServiceImpl<MachineScreenMapper, MachineScreen> implements IMachineScreenService {
    @Autowired MachineScreenMapper machineScreenMapper;
    @Override
    public IPage<MachineScreenVO> selectMachineScreenPage(IPage<MachineScreenVO> page, MachineScreenVO machineScreen) {
        return page.setRecords(baseMapper.selectMachineScreenPage(page, machineScreen));
    }

    @Override
    public List<MachineScreen> getScreenInfo() {
        return baseMapper.getScreenInfo();
    }


    @Override
    public boolean setBlindScreen(String uuid) {

        return machineScreenMapper.setBlindScreen(uuid);
    }

    @Override
    public boolean addBlindScreen(Integer maId,String uuid) {
        return machineScreenMapper.addBlindScreen(maId,uuid);
    }
}
