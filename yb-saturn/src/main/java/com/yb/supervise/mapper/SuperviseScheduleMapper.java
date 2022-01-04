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
package com.yb.supervise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.supervise.entity.SuperviseSchedule;
import com.yb.supervise.request.SuperviseOrderScheduleRequest;
import com.yb.supervise.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单进度表（进度表-执行） Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface SuperviseScheduleMapper extends BaseMapper<SuperviseSchedule> {

    /**
     * 自定义分页
     *
     * @param page
     * @param superviseSchedule
     * @return
     */
    List<SuperviseScheduleVO> selectSuperviseSchedulePage(IPage page, SuperviseScheduleVO superviseSchedule);

    /**
     * 订单进度
     *
     * @param page
     * @param superviseSchedule
     * @return
     */
    List<SuperviseScheduleVO> getOrderSchedule(IPage page, SuperviseScheduleVO superviseSchedule);

//    /**
//     * 批次进度
//     *
//     * @param odId
//     * @return
//     */
//    List<SuperviseScheduleVO> getBatchSchedule(Integer odId, SuperviseScheduleVO superviseSchedule);

    /**
     * 部件进度
     *
     * @param batchId
     * @return
     */
    List<SuperviseSchedulePtVO> getPartSchedule(Integer batchId);

    /**
     * 工序进度
     *
     * @param ptId
     * @return
     */
    List<SuperviseScheduleVO> getProcessSchedule(Integer ptId);

    /**
     * @param odId
     * @param wbId
     * @param ptId
     * @param prId
     * @param maId
     * @return
     */
    SuperviseSchedule getSchedule(@Param("odId") Integer odId, @Param("wbId") Integer wbId, @Param("ptId") Integer ptId, @Param("prId") Integer prId, @Param("maId") Integer maId);


    /**
     * 订单进度/按订单
     *
     * @param page
     * @param request
     * @return
     */
    List<SuperviseScheduleOrderVO> getOrderScheduleByOrder(IPage page, @Param("request") SuperviseOrderScheduleRequest request);


    /**
     * 批次进度
     * @param odId
     * @param request
     * @return
     */
    List<SuperviseScheduleBatchVO> getBatchSchedule(@Param("odId") Integer odId, @Param("request") SuperviseOrderScheduleRequest request);

    /**
     * 工序进度
     *
     * @param ptId
     * @return
     */
    List<SuperviseSchedulePrVO> getPrSchedule(Integer ptId);
}
