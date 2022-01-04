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
package com.yb.machine.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.machine.entity.MachineScreen;
import com.yb.machine.vo.MachineScreenVO;

import java.util.List;

/**
 * 设备关联屏幕（主系统同步数据） 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IMachineScreenService extends IService<MachineScreen> {

    /**
     * 自定义分页
     *
     * @param page
     * @param machineScreen
     * @return
     */
    IPage<MachineScreenVO> selectMachineScreenPage(IPage<MachineScreenVO> page, MachineScreenVO machineScreen);

    /***
     * 获取显示屏幕信息
     * remark  yb_machine_screen
     * 预留传参 以防以后更新
     */
    public List<MachineScreen> getScreenInfo();
    /***
     * 解绑屏幕
     */
    public boolean setBlindScreen(String uuid);
    /**
     *绑定屏幕
     */
    public boolean addBlindScreen(Integer maId,String uuid);

}
