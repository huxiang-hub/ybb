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
package com.yb.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.process.entity.ProcessMachlink;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.mapper.ProcessMachlinkMapper;
import com.yb.process.mapper.ProcessWorkinfoMapper;
import com.yb.process.service.IProcessMachlinkService;
import com.yb.process.vo.PrModelVO;
import com.yb.process.vo.ProModelVO;
import com.yb.process.vo.ProcessMachlinkVO;
import com.yb.process.vo.PyModelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备工序关联表yb_process_machlink 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ProcessMachlinkServiceImpl extends ServiceImpl<ProcessMachlinkMapper, ProcessMachlink> implements IProcessMachlinkService {
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private ProcessMachlinkMapper processMachlinkMapper;
    @Autowired
    private ProcessWorkinfoMapper processWorkinfoMapper;
    @Override
    public IPage<ProcessMachlinkVO> selectProcessMachlinkPage(IPage<ProcessMachlinkVO> page, ProcessMachlinkVO processMachlink) {
        return page.setRecords(baseMapper.selectProcessMachlinkPage(page, processMachlink));
    }

    @Override
    public IPage<ProModelVO> getAllPyName(IPage<ProModelVO> page,ProModelVO proModelVO) {
        return  page.setRecords(baseMapper.getAllPyName(page,proModelVO));
    }

    @Override
    public List<String> getMaNamesByPyId(Integer prId) {

        return baseMapper.getMaNamesByPyId(prId);
    }

    @Override
    public List<PrModelVO> getPrModels(Integer prId) {
        return baseMapper.getPrModels(prId);
    }

    /**
     * 获取工序分类
     * @return
     */
    @Override
    public List<PyModelVO> getPyModels() {
        List<PyModelVO> pyModelVOS = baseMapper.getPyModels();
      /*  for (PyModelVO item : pyModelVOS){
            *//**
             * 通过类型找到子工序
             *//*
            item.setProModelVOS(baseMapper.getPrModels(item.getPyId()));
        }*/
        return pyModelVOS;
    }

    @Override
    public String getPyNameById(Integer id) {

        return baseMapper.getPyNameById(id);
    }

    @Override
    public List<MachineMainfo> getMaNameList() {
        return machineMainfoMapper.selectList(new QueryWrapper<>());
    }

        @Override
        public IPage<ProcessMachlink> processMachlinkList(IPage<ProcessMachlink> page, ProcessMachlinkVO processMachlinkVO) {
            QueryWrapper<ProcessMachlink> queryWrapper = new QueryWrapper<>();
            if(processMachlinkVO != null && processMachlinkVO.getPrId() != null){//搜索框传入值
                queryWrapper.eq("pr_id",processMachlinkVO.getPrId());
            }
            //List<ProcessMachlink> processMachlinks = processMachlinkMapper.selectList(queryWrapper);
        return processMachlinkMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Boolean delete(Integer id) {
        int nums = processMachlinkMapper.delete(new QueryWrapper<ProcessMachlink>().eq("pr_id", id));
        return nums > 0 ? true : false;
    }

    @Override
    public List<ProcessWorkinfo> getPrName(Integer pyId) {

        return processMachlinkMapper.getPrName(pyId);
    }

    @Override
    public ProcessMachlink getEntityByPrMt(Integer mtId, Integer prId) {
        return processMachlinkMapper.getEntityByPrMt(mtId,prId);
    }

}
