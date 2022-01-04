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
import com.yb.base.entity.BaseDeptinfo;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.entity.MachineMixbox;
import com.yb.machine.vo.MachineMixboxVO;

import java.util.List;

/**
 * 印联盒（本租户的盒子），由总表分发出去 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IMachineMixboxService extends IService<MachineMixbox> {

    /**
     * 自定义分页
     *
     * @param page
     * @param machineMixbox
     * @return
     */
    IPage<MachineMixboxVO> selectMachineMixboxPage(IPage<MachineMixboxVO> page, MachineMixboxVO machineMixbox);

    //通过盒子编号UUID查询
    MachineMixbox selectBoxByBno(String bno);
    /**
     * 修改盒子绑定状态
     * */
    public int setMixboxByMaId(String uuid);
    /**
     * 修改盒子绑定状态
     * */
    public int addMixboxByMaId(String uuid,Integer maId);
    /**
     * 获取可以active状态为0的盒子
     * */
    public List getBlindBox();

    /**
     * 將删除设备的盒子解绑
     */
    boolean setMixboxByListMaId(List<Integer> ids);


    /****blade x 搬运项目***/
    /**
     * 自定义分页
     *
     * @param page
     * @param
     * @return
     */

    IPage<MachineMixboxVO> getMachineMixboxPage(IPage page, MachineMixboxVO mixboxVO);

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
    MachineMixboxVO findMixboxIsExit(String uuid);
    /**
     * 查询未绑定盒子的设备信息系
     * @return
     */
    List<MachineMainfo> selectMachineList();

    /**
     * 盒子信息同步
     * @param tenantId
     */
    void syncBox(String tenantId);

    List<Integer> getMachineIds();
}
