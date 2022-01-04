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
import com.yb.machine.entity.MachineBsinfo;
import com.yb.machine.mapper.MachineBsinfoMapper;
import com.yb.machine.service.IMachineBsinfoService;
import com.yb.machine.vo.MachineBsinfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备扩展信息_yb_machine_bsinfo 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class MachineBsinfoServiceImpl extends ServiceImpl<MachineBsinfoMapper, MachineBsinfo> implements IMachineBsinfoService {
    @Autowired
    private MachineBsinfoMapper machineBsinfoMapper;
    @Override
    public IPage<MachineBsinfoVO> selectMachineBsinfoPage(IPage<MachineBsinfoVO> page, MachineBsinfoVO machineBsinfo) {
        return page.setRecords(baseMapper.selectMachineBsinfoPage(page, machineBsinfo));
    }
    @Override
    public MachineBsinfo getMachineBsinfo(Integer maId) {
        MachineBsinfo machineBsinfo = null;
        try {
            machineBsinfo = machineBsinfoMapper.getMachineBsinfo(maId);
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("一台设备绑定多个盒子");
        }
        return machineBsinfo;
    }
    @Override
    public boolean updateMachineBaseInfo(MachineBsinfo info) {
        return machineBsinfoMapper.updateMachineBaseInfo(info);
    }

}
