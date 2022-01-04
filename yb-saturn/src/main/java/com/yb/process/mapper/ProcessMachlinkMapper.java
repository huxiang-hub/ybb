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
package com.yb.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.process.entity.ProcessMachlink;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.vo.PrModelVO;
import com.yb.process.vo.ProModelVO;
import com.yb.process.vo.ProcessMachlinkVO;
import com.yb.process.vo.PyModelVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备工序关联表yb_process_machlink Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ProcessMachlinkMapper extends BaseMapper<ProcessMachlink> {

    /**
     * 自定义分页
     *
     * @param page
     * @param processMachlink
     * @return
     */
    List<ProcessMachlinkVO> selectProcessMachlinkPage(IPage page, ProcessMachlinkVO processMachlink);

    /**
     * 获取所有的工序
     * @return
     */
    List<ProModelVO> getAllPyName(IPage page,ProModelVO proModelVO);

    String getPridsBymaId(Integer maId);
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
     * @param prId
     * @return
     */
    String getPyNameById(Integer prId);

    /**
     * 获取没有被禁用主工序
     * @return
     */
    List<ProcessWorkinfo> getPrName(Integer pyId);

    /**
     *
     * @param maId
     * @param prId
     * @return
     */

    ProcessMachlink getEntityByPrMt(Integer maId, Integer prId);

    List<MachineMainfoVO> machineListByPrIdDeptId(Integer prId, Integer dpId);
}
