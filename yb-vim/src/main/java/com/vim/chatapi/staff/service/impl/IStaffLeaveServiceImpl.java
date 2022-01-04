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
import com.vim.chatapi.staff.entity.StaffLeave;
import com.vim.chatapi.staff.mapper.StaffLeaveMapper;
import com.vim.chatapi.staff.service.IStaffLeaveService;
import com.vim.chatapi.staff.vo.StaffLeaveVO;
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
public class IStaffLeaveServiceImpl extends ServiceImpl<StaffLeaveMapper,StaffLeave> implements IStaffLeaveService {
    @Autowired
    private StaffLeaveMapper staffLeaveMapper;
    @Override
    public List<StaffLeaveVO> checkApplyLeave(Integer userId, Integer status) {
        return staffLeaveMapper.checkApplyLeave(userId,status);
    }

    @Override
    public boolean saveStaffLeave(StaffLeave staffLeave) {
        return staffLeaveMapper.saveStaffLeave(staffLeave);
    }

    @Override
    public boolean updateStaffLeave(StaffLeave staffLeave) {
        return staffLeaveMapper.updateStaffLeave(staffLeave);
    }

    @Override
    public List<StaffLeave> getStaffLeaves(Integer userId, Integer status) {
        return staffLeaveMapper.getStaffLeaves(userId,status);
    }

    @Override
    public StaffLeaveVO getStaffLeaveById(Integer id) {
        return staffLeaveMapper.getStaffLeaveById(id);
    }
}

