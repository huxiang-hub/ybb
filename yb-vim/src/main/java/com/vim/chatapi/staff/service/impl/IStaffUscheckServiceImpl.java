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
package com.vim.chatapi.staff.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vim.chatapi.staff.entity.StaffUscheck;
import com.vim.chatapi.staff.mapper.StaffUscheckMapper;
import com.vim.chatapi.staff.service.IStaffUscheckService;
import com.vim.chatapi.staff.vo.DateModelVO;
import com.vim.chatapi.staff.vo.StaffUscheckVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 故障停机设置_yb_exeset_fault 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class IStaffUscheckServiceImpl extends ServiceImpl<StaffUscheckMapper, StaffUscheck> implements IStaffUscheckService {
    @Autowired
    private StaffUscheckMapper staffUscheckMapper;
    @Override
    public List<StaffUscheck> getStaffUschecks(Integer usId, String date,Integer model) {
        return staffUscheckMapper.getStaffUschecks(usId,date,model);
    }

    @Override
    public boolean saveStaffUscheck(StaffUscheck staffUscheck) {
        return staffUscheckMapper.saveStaffUscheck(staffUscheck);
    }

    @Override
    public boolean updateStaffUscheck(StaffUscheckVO staffUscheck) {
        return staffUscheckMapper.updateStaffUscheck(staffUscheck);
    }

    @Override
    public boolean updateSUscheck(StaffUscheck staffUscheck) {
        return staffUscheckMapper.updateSUscheck(staffUscheck);
    }

    @Override
    public List<StaffUscheck> getStaffUscheckByDate(DateModelVO dateModelVO) {
        return staffUscheckMapper.getStaffUscheckByDate(dateModelVO);
    }

    @Override
    public StaffUscheck getStaffUscheck(Integer userId) {
        return staffUscheckMapper.getStaffUscheck(userId);
    }


    @Override
    public StaffUscheck getStaffUscheckInfo(Integer userId, String date, Integer model) {

        return staffUscheckMapper.getStaffUscheckInfo(userId,date,model);
    }
}

