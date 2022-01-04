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
package com.yb.statis.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.statis.entity.StatisMachregular;
import com.yb.statis.mapper.StatisMachRegularMapper;
import com.yb.statis.service.IStatisMachRegularService;
import com.yb.statis.vo.StatisMachrRegularVO;
import com.yb.statis.vo.StatisMachsingleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OEE数据信息表_yb_statis_regular（定时进行oee的统计；每半个小时进行OEE汇总统计） 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */

@Service
public class StatisMachRegularServiceImpl extends ServiceImpl<StatisMachRegularMapper, StatisMachregular> implements IStatisMachRegularService {
    @Autowired
    private StatisMachRegularMapper statisMachRegularMapper;


    @Override
    public IPage<StatisMachsingleVO> selectStatisMachRegularPage(IPage<StatisMachsingleVO> page, StatisMachrRegularVO statisMachrRegularVO) {
        return statisMachRegularMapper.selectStatisMachRegularPage(page,statisMachrRegularVO);
    }
}