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
import com.yb.process.entity.ProcessParamlink;
import com.yb.process.mapper.ProcessParamlinkMapper;
import com.yb.process.service.IProcessParamlinkService;
import com.yb.process.vo.ProcessParamlinkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工序参数关联表_yb_proc_paramlink 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ProcessParamlinkServiceImpl extends ServiceImpl<ProcessParamlinkMapper, ProcessParamlink> implements IProcessParamlinkService {

    @Autowired
    private ProcessParamlinkMapper processParamlinkMapper;
    @Override
    public IPage<ProcessParamlinkVO> selectProcessParamlinkPage(IPage<ProcessParamlinkVO> page, ProcessParamlinkVO processParamlink) {
        return page.setRecords(baseMapper.selectProcessParamlinkPage(page, processParamlink));
    }

    @Override
    public String insertProcessParamlink(ProcessParamlink processParamlink) {
        int insert = processParamlinkMapper.insert(processParamlink);
        if(insert != 0){
            return "新增"+insert+"条数据";
        }
        return "新增失败";
    }

    @Override
    public String updateProcessParamlink(ProcessParamlink processParamlink) {
        int updateNum = processParamlinkMapper.update
                (processParamlink, new QueryWrapper<ProcessParamlink>()
                        .eq("pr_id", processParamlink.getPrId()));
        if(updateNum != 0){
            return "修改成功";
        }
        return "修改失败";
    }

    @Override
    public String deleteProcessParamlink(Integer prId) {
        int i = processParamlinkMapper.deleteById(prId);
        return "成功删除"+i+"条记录";
    }

    @Override
    public List<ProcessParamlinkVO> selectProcessParamlinkByPrId(Integer prId) {
        return processParamlinkMapper.selectProcessParamlinkByPrId(prId);
    }

}
