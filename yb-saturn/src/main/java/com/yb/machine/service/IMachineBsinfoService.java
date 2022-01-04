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
import com.yb.machine.entity.MachineBsinfo;
import com.yb.machine.vo.MachineBsinfoVO;

/**
 * 设备扩展信息_yb_machine_bsinfo 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IMachineBsinfoService extends IService<MachineBsinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param machineBsinfo
     * @return
     */
    IPage<MachineBsinfoVO> selectMachineBsinfoPage(IPage<MachineBsinfoVO> page, MachineBsinfoVO machineBsinfo);
    /**
     * 获取设备基本信息
     * remark   yb_machine_bsinfo
     *  预留传参 以防以后更新
     */
    MachineBsinfo getMachineBsinfo(Integer maId);
    /***
     *  修改设备信息
     * remark    yb_machine_bsinfo
     *  预留传参 以防以后更新
     */
    boolean updateMachineBaseInfo(MachineBsinfo info);


}
