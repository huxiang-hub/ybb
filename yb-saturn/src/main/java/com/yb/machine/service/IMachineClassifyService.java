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
import com.yb.machine.entity.MachineClassify;
import com.yb.machine.vo.MachineClassifyVO;
import com.yb.system.dict.vo.DictVO;

import java.util.List;

/**
 * 设备型号_yb_mach_classify 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IMachineClassifyService extends IService<MachineClassify> {

    /**
     * 自定义分页
     *
     * @param page
     * @param machineClassify
     * @return
     */
    IPage<MachineClassifyVO> selectMachineClassifyPage(IPage<MachineClassifyVO> page, MachineClassifyVO machineClassify);

    /**
     * 获取设备信息
     * remark   yb_machine_classify
     *  预留传参 以防以后更新
     */
    public MachineClassify getMachineInfo(Integer maId);
    /***
     *  修改设备信息
     * remark   yb_machine_classify
     *  预留传参 以防以后更新
     */
    public boolean updateMachineInfo( MachineClassify info);

    List<MachineClassifyVO> getAllBrand();

    List<DictVO> listByMachine();

}
