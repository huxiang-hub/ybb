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
package com.vim.chatapi.staff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vim.chatapi.staff.entity.StaffDayoff;
import com.vim.chatapi.staff.vo.StaffDayoffVO;

import java.util.List;

/**
 * 故障停机设置_yb_exeset_fault 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IStaffDayoffService extends IService<StaffDayoff> {

    /***
     * 向yb_staff_dayoff 插入申请人的信息
     */
    Integer saveStaffDayoff(StaffDayoff staff);
    /***
     *
     *审核人审批申请
     */
    List<StaffDayoffVO> checkApplyDayoff(Integer userId, Integer status);
    /**
     * 修改请假人的 请假申请状态
     */
    boolean updateStaffDayoff(StaffDayoff staffDayoff);

    /**
     * 获取所有的请假记录
     * @param userId
     * @return
     */
    List<StaffDayoff> getStaffDayoff(Integer userId,Integer status);

    /**
     * 请假详情
     * @param id
     * @return
     */
    StaffDayoff getStaffDayoffById(Integer id);

}

