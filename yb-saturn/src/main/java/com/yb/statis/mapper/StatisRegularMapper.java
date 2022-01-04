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
package com.yb.statis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.statis.entity.StatisRegular;
import com.yb.statis.vo.StatisRegularVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * OEE数据信息表_yb_statis_regular（定时进行oee的统计；每半个小时进行OEE汇总统计） Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface StatisRegularMapper extends BaseMapper<StatisRegular> {

    /**
     * 自定义分页
     *
     * @param page
     * @param statisRegular
     * @return
     */
    List<StatisRegularVO> selectStatisRegularPage(IPage page, StatisRegularVO statisRegular);

}
