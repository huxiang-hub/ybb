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
package com.yb.exeset.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.exeset.entity.ExesetFault;
import com.yb.exeset.mapper.ExesetFaultMapper;
import com.yb.exeset.service.IExesetFaultService;
import com.yb.exeset.vo.ExesetFaultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 故障停机设置_yb_exeset_fault 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ExesetFaultServiceImpl extends ServiceImpl<ExesetFaultMapper, ExesetFault> implements IExesetFaultService {

    @Autowired
    private ExesetFaultMapper faultMapper;

    @Override
    public IPage<ExesetFaultVO> selectExesetFaultPage(IPage<ExesetFaultVO> page, ExesetFaultVO exesetFault) {
        return page.setRecords(baseMapper.selectExesetFaultPage(page, exesetFault));
    }

    @Override
    public ExesetFault selectExesetFault(Integer mId)
    {
        return faultMapper.selectExesetFault(mId);
    }
    @Override
    public boolean updateFaultPoP(ExesetFault fault) {

        return faultMapper.updateFaultPoP(fault);
    }




}
