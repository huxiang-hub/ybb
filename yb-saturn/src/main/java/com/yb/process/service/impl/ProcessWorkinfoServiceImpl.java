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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.mapper.ProcessWorkinfoMapper;
import com.yb.process.service.IProcessWorkinfoService;
import com.yb.process.vo.ProcessWorkinfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工序表--租户的工序内容（可以依据行业模版同步） 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ProcessWorkinfoServiceImpl extends ServiceImpl<ProcessWorkinfoMapper, ProcessWorkinfo> implements IProcessWorkinfoService {

    @Autowired
    private  ProcessWorkinfoMapper processWorkinfoMapper;

    @Override
    public IPage<ProcessWorkinfoVO> selectProcessWorkinfoPage(IPage<ProcessWorkinfoVO> page, ProcessWorkinfoVO processWorkinfo) {
        return page.setRecords(baseMapper.selectProcessWorkinfoPage(page, processWorkinfo));
    }

    @Override
    public ProcessWorkinfoVO getProcessWorkInByName(String ProcessName) {
        return processWorkinfoMapper.getProcessWorkInByName(ProcessName);
    }

    @Override
    public Integer unfinishNum(Integer prId, Integer ptId, Integer wbId) {
        return processWorkinfoMapper.unfinishNum(prId, ptId, wbId);
    }

    @Override
    public IPage<ProcessWorkinfoVO> selectSortProcessWorkinfoPage(IPage<ProcessWorkinfoVO> page, ProcessWorkinfoVO processWorkinfo) {

        return page.setRecords(baseMapper.selectSortProcessWorkinfoPage(page, processWorkinfo));
    }

    @Override
    public List<ProcessWorkinfoVO> workInfoBy(Integer pyId) {

        return baseMapper.workInfoBy(pyId);
    }

    @Override
    public ProcessWorkinfoVO selectSortProcessWorkinfo(Integer id) {
        return baseMapper.selectSortProcessWorkinfo(id);
    }

    @Override
    public List<ProcessWorkinfo> listByMachine() {
        return baseMapper.listByMachine();
    }

}
