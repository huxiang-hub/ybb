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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.workbatch.entity.WorkbatchShiftinfo;
import com.yb.workbatch.mapper.WorkbatchShiftinfoMapper;
import com.yb.workbatch.service.IWorkbatchShiftinfoService;
import com.yb.workbatch.vo.WorkbatchShiftinfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 排产班次执行表_yb_workbatch_shiftinfo（日志表） 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class WorkbatchShiftinfoServiceImpl extends ServiceImpl<WorkbatchShiftinfoMapper, WorkbatchShiftinfo> implements IWorkbatchShiftinfoService {

    @Autowired
    private WorkbatchShiftinfoMapper WorkbatchShiftinfoMapper;
    @Override
    public IPage<WorkbatchShiftinfoVO> selectWorkbatchShiftinfoPage(IPage<WorkbatchShiftinfoVO> page, WorkbatchShiftinfoVO workbatchShiftinfo) {
        return page.setRecords(baseMapper.selectWorkbatchShiftinfoPage(page, workbatchShiftinfo));
    }

    @Override
    public List<WorkbatchShiftinfo> getWorkBatchListByCondition(Date date, String userId) {
        return WorkbatchShiftinfoMapper.getWorkBatchListByCondition(date,userId);
    }
}
