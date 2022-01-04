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
package com.yb.statis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.statis.entity.StatisMachalg;
import com.yb.statis.vo.StatisMachalgVO;

/**
 * OEE数据信息表_yb_statis_machalg（OEE的全量统计-状态变更及中间定时5分钟已上报记录） 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IStatisMachalgService extends IService<StatisMachalg> {

    /**
     * 自定义分页
     *
     * @param page
     * @param statisMachalg
     * @return
     */
    IPage<StatisMachalgVO> selectStatisMachalgPage(IPage<StatisMachalgVO> page, StatisMachalgVO statisMachalg);

}
