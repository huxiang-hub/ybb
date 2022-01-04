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
package com.yb.staff.wrapper;

import com.yb.staff.entity.StaffDayoff;
import com.yb.staff.vo.StaffDayoffVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 人事请假管理_yb_staff_dayoff包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2020-03-10
 */
public class StaffDayoffWrapper extends BaseEntityWrapper<StaffDayoff, StaffDayoffVO> {

    public static StaffDayoffWrapper build() {
        return new StaffDayoffWrapper();
    }

    @Override
    public StaffDayoffVO entityVO(StaffDayoff staffDayoff) {
        StaffDayoffVO staffDayoffVO = BeanUtil.copy(staffDayoff, StaffDayoffVO.class);

        return staffDayoffVO;
    }

}
