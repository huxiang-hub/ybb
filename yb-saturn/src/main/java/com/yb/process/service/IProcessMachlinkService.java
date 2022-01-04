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
package com.yb.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.machine.entity.MachineMainfo;
import com.yb.process.entity.ProcessMachlink;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.vo.PrModelVO;
import com.yb.process.vo.ProModelVO;
import com.yb.process.vo.ProcessMachlinkVO;
import com.yb.process.vo.PyModelVO;

import java.util.List;

/**
 * 设备工序关联表yb_process_machlink 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IProcessMachlinkService extends IService<ProcessMachlink> {

    /**
     * 自定义分页
     *
     * @param page
     * @param processMachlink
     * @return
     */
    IPage<ProcessMachlinkVO> selectProcessMachlinkPage(IPage<ProcessMachlinkVO> page, ProcessMachlinkVO processMachlink);
    /**
     * 获取所有的工序
     * @return
     */
    IPage<ProModelVO> getAllPyName(IPage<ProModelVO> page,ProModelVO proModelVO);
    /**
     * 获取指定工序的设备名字
     */
    List<String> getMaNamesByPyId(Integer prId);

    /**
     * 获取工序
     * @param prId
     * @return
     */
    List<PrModelVO> getPrModels(Integer prId);
    /**
     * 获取工序分类
     * @return
     */
    List<PyModelVO> getPyModels();

    /**
     * 通过工序id查找他对应的工序类型
     * @param id
     * @return
     */
   String getPyNameById(Integer id);

    List<MachineMainfo> getMaNameList();

    /**
     * 查询工序对应设备信息
     * @param page
     * @param processMachlinkVO
     * @return
     */
    IPage<ProcessMachlink> processMachlinkList(IPage<ProcessMachlink> page, ProcessMachlinkVO processMachlinkVO);

    /**
     * 删除工序设备对应关系
     * @param id
     * @return
     */
    Boolean delete(Integer id);

    /**
     * 获取没有被禁用主工序
     * @return
     */
    List<ProcessWorkinfo> getPrName(Integer pyId);
    /**
     * 通过设备Id 和工序Id 去获取准备时间
     */
    ProcessMachlink getEntityByPrMt(Integer mtId,Integer prId);

}
