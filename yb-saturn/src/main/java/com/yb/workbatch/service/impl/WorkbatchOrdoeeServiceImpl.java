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
package com.yb.workbatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.workbatch.entity.WorkbatchOrdoee;
import com.yb.workbatch.mapper.WorkbatchOrdoeeMapper;
import com.yb.workbatch.mapper.WorkbatchShiftinfoMapper;
import com.yb.workbatch.service.IWorkbatchOrdoeeService;
import com.yb.workbatch.vo.WorkbatchOrdoeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 人员排班表_yb_workbatch_staffwk 服务实现类
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class WorkbatchOrdoeeServiceImpl extends ServiceImpl<WorkbatchOrdoeeMapper, WorkbatchOrdoee> implements IWorkbatchOrdoeeService {
    @Autowired
    private WorkbatchOrdoeeMapper workbatchOrdoeeMapper;

    @Override
    public WorkbatchOrdoeeVo getOrdeeBySdId(Integer sdId) {
        return workbatchOrdoeeMapper.getOrdeeBySdId(sdId);
    }
    @Override
    public WorkbatchOrdoee getOrdlinkOeeBySdId(Integer sdId) {
        return workbatchOrdoeeMapper.getOeeBySdId(sdId);
    }
}
