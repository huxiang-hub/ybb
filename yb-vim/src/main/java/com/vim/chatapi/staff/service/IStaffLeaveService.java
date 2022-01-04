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
import com.vim.chatapi.staff.entity.StaffLeave;
import com.vim.chatapi.staff.vo.StaffLeaveVO;

import java.util.List;

/**
 * 故障停机设置_yb_exeset_fault 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IStaffLeaveService extends IService<StaffLeave> {

    /***
     *
     *审核人审批申请
     */
    List<StaffLeaveVO> checkApplyLeave(Integer userId, Integer status);

    /**
     * 保存补卡申请记录
     * @param staffLeave
     * @return
     */
    boolean saveStaffLeave(StaffLeave staffLeave);

    /**
     * 修改补卡的 申请状态
     */
    boolean updateStaffLeave(StaffLeave staffLeave);
    /**
     * 获取所有的补卡记录
     * @param userId
     * @return
     */
    List<StaffLeave> getStaffLeaves(Integer userId,Integer status);

    /**
     * 补卡详情
     * @param id
     * @return
     */
    StaffLeaveVO getStaffLeaveById(Integer id);


}

