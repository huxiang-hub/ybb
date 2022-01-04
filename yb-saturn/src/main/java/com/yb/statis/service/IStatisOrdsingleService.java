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
import com.yb.statis.entity.StatisOrdsingle;
import com.yb.statis.vo.StatisOrdsingleVO;

import java.text.ParseException;
import java.util.List;

/**
 * OEE数据表_yb_statis_ordsingle（针对每个班次的单独订单数据统计） 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IStatisOrdsingleService extends IService<StatisOrdsingle> {

    /**
     * 自定义分页
     *
     * @param page
     * @param statisOrdsingle
     * @return
     */
    IPage<StatisOrdsingleVO> selectStatisOrdsinglePage(IPage<StatisOrdsingleVO> page, StatisOrdsingleVO statisOrdsingle);

    IPage<StatisOrdsingleVO> selectOEEStatisOrdsinglePage(IPage<StatisOrdsingleVO> page, StatisOrdsingleVO statisOrdsingle);

    void generateOrderOEEReport(Integer userId, Integer mallId, Integer sdId,Double dutyNumber,String id,Integer exId) throws ParseException;

    List<StatisOrdsingleVO> getStatisInfoBySdId(Integer sdId);
}
