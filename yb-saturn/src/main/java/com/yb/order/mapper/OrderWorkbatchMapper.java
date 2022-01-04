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
package com.yb.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.order.entity.OrderWorkbatch;
import com.yb.order.vo.OrderWorkbatchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * 作业批次_yb_order_workbatch Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface OrderWorkbatchMapper extends BaseMapper<OrderWorkbatch> {

    /**
     * 自定义分页
     *
     * @param page
     * @param orderWorkbatch
     * @return
     */
    List<OrderWorkbatchVO> selectOrderWorkbatchPage(IPage page, OrderWorkbatchVO orderWorkbatch);

    /**
     * 订单批量生产展示根据
     *
     * */
    HashMap getOrderByInfo(@Param("mId") Integer mId, @Param("wbId") Integer wbId);

    OrderWorkbatchVO getWorkBatchByWbId(Integer wbId);

    OrderWorkbatchVO findObjectBybatchNo(@Param("batchNo")String batchNo);
    /*未分批的订单数量*/
    Integer getOrderUnfinishedNum(Integer odId);

    List<OrderWorkbatchVO> selectOrderWorkbatchListByOdno(@Param("odNo") String odNo);

    OrderWorkbatch getNewest(@Param("odId")Integer odId);

    List<OrderWorkbatchVO> selectOrderWorkbatchList(@Param("orderWorkbatch")OrderWorkbatch orderWorkbatch);

    /**
     * 查询该批次下已排产的工序
     * @param prId
     * @param ptId
     * @param wbId
     * @return
     */
    List<OrderWorkbatchVO> batchNumberList(Integer prId, Integer ptId, Integer wbId);
}
