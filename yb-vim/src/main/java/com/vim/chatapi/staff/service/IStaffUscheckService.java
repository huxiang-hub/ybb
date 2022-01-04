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
import com.vim.chatapi.staff.entity.StaffUscheck;
import com.vim.chatapi.staff.vo.DateModelVO;
import com.vim.chatapi.staff.vo.StaffUscheckVO;

import java.util.List;

/**
 * 故障停机设置_yb_exeset_fault 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IStaffUscheckService extends IService<StaffUscheck> {

    /**
     * 获取所有的考情记录
     * @return
     */
    List<StaffUscheck> getStaffUschecks(Integer usId, String date,Integer model);
    /**
     * 打卡
     * @param staffUscheck
     * @return
     */
    boolean saveStaffUscheck(StaffUscheck staffUscheck);

    /**
     * 下班打卡
     * @param staffUscheck
     * @return
     */
    boolean updateStaffUscheck(StaffUscheckVO staffUscheck);

    /**
     * 更新打卡记录表
     * @param staffUscheck
     * @return
     */
    boolean updateSUscheck(StaffUscheck staffUscheck);

    /**
     * 查询某个时间段的的考勤
     * @param dateModelVO
     * @return
     */
    List<StaffUscheck> getStaffUscheckByDate(DateModelVO dateModelVO);

    /**
     * 查看考勤记录
     * @param userId
     * @return
     */
    StaffUscheck getStaffUscheck(Integer userId);


    /**
     * 获取当前的考勤状态
     * @param userId
     * @param date
     * @return
     */
    StaffUscheck getStaffUscheckInfo(Integer userId,String date,Integer model);


}

