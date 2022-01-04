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
import com.vim.chatapi.staff.entity.StaffDayoff;
import com.vim.chatapi.staff.mapper.StaffDayoffMapper;
import com.vim.chatapi.staff.service.IStaffDayoffService;
import com.vim.chatapi.staff.vo.StaffDayoffVO;
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
public class IStaffDayoffServiceImpl extends ServiceImpl<StaffDayoffMapper, StaffDayoff> implements IStaffDayoffService {
    @Autowired
    private StaffDayoffMapper staffDayoffMapper;
    @Override
    public Integer saveStaffDayoff(StaffDayoff staff) {
        return staffDayoffMapper.saveStaffDayoff(staff);
    }

    @Override
    public List<StaffDayoffVO> checkApplyDayoff(Integer userId, Integer status) {
        return staffDayoffMapper.checkApplyDayoff(userId,status);
    }

    @Override
    public boolean updateStaffDayoff(StaffDayoff staffDayoff) {
        return staffDayoffMapper.updateStaffDayoff(staffDayoff);
    }

    @Override
    public List<StaffDayoff> getStaffDayoff(Integer userId, Integer status) {
        return staffDayoffMapper.getStaffDayoff(userId,status);
    }

    @Override
    public StaffDayoff getStaffDayoffById(Integer id) {

        return staffDayoffMapper.getStaffDayoffById(id);
    }
}

