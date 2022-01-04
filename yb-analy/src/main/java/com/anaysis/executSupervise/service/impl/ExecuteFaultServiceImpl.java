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
package com.anaysis.executSupervise.service.impl;


import com.anaysis.executSupervise.entity.ExecuteFault;
import com.anaysis.executSupervise.mapper.ExecuteFaultMapper;
import com.anaysis.executSupervise.service.ExecuteFaultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.yb.execute.entity.ExecuteFault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备停机故障记录表_yb_execute_fault 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ExecuteFaultServiceImpl extends ServiceImpl<ExecuteFaultMapper, ExecuteFault> implements ExecuteFaultService {

    @Autowired
    private ExecuteFaultMapper faultMapper;


    @Override
    public int saveFault(ExecuteFault fault) {
        return faultMapper.insert(fault);
    }

    @Override
    public int getDownCount(Integer maId) {
        return faultMapper.getDownCount(maId);
    }
}
