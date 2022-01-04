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
package com.yb.staff.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.staff.entity.StaffDayoff;
import com.yb.staff.mapper.StaffDayoffMapper;
import com.yb.staff.service.IStaffDayoffService;
import com.yb.staff.vo.StaffDayoffVO;
import org.springframework.stereotype.Service;

/**
 * 人事请假管理_yb_staff_dayoff 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class StaffDayoffServiceImpl extends ServiceImpl<StaffDayoffMapper, StaffDayoff> implements IStaffDayoffService {

    @Override
    public IPage<StaffDayoffVO> selectStaffDayoffPage(IPage<StaffDayoffVO> page, StaffDayoffVO staffDayoff) {
        return page.setRecords(baseMapper.selectStaffDayoffPage(page, staffDayoff));
    }

}
