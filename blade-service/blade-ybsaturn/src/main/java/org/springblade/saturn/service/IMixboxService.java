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
package org.springblade.saturn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.saturn.entity.BaseDeptinfo;
import org.springblade.saturn.entity.MachineMainfo;
import org.springblade.saturn.entity.Mixbox;
import org.springblade.saturn.vo.MixboxVO;

import java.util.List;

/**
 * 印联盒（本租户的盒子），由总表分发出去 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IMixboxService extends IService<Mixbox> {

    /**
     * 自定义分页
     *
     * @param page
     * @param
     * @return
     */
    IPage<MixboxVO> selectMachineMixboxPage(IPage<MixboxVO> page, MixboxVO mixboxVO);

    IPage<MixboxVO> getMachineMixboxPage(IPage page, MixboxVO mixboxVO);

    /**
     * 获取部门
     * @return
     */
    List<BaseDeptinfo> getDeptInfo();

    /**
     * 通过盒子uuid 找盒子
     * @param uuid
     * @return
     */
    MixboxVO findMixboxIsExit(String uuid);

    /**
     * 查询未绑定盒子的设备信息系
     * @return
     */
    List<MachineMainfo> selectMachineList();
}
