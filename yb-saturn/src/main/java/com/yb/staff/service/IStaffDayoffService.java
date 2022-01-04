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
package com.yb.staff.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.staff.entity.StaffDayoff;
import com.yb.staff.vo.StaffDayoffVO;

/**
 * 人事请假管理_yb_staff_dayoff 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IStaffDayoffService extends IService<StaffDayoff> {

    /**
     * 自定义分页
     *
     * @param page
     * @param staffDayoff
     * @return
     */
    IPage<StaffDayoffVO> selectStaffDayoffPage(IPage<StaffDayoffVO> page, StaffDayoffVO staffDayoff);

}
