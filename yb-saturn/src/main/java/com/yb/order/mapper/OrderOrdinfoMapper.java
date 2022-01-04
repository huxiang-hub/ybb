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
import com.yb.order.entity.OrderOrdinfo;
import com.yb.order.entity.OrderSchedule;
import com.yb.order.vo.OrderOrdinfoVO;
import com.yb.statis.request.MasterPlanRequest;
import com.yb.statis.vo.MasterPlanVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单表_yb_order_ordinfo Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface OrderOrdinfoMapper extends BaseMapper<OrderOrdinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param orderOrdinfo
     * @return
     */
    List<OrderOrdinfoVO> selectOrderOrdinfoPage(IPage page, OrderOrdinfoVO orderOrdinfo);

    /**
     * 订单的查询 可以根据订单的编号、订单的时间、可以根据订单厂家查询
     */
    List<OrderOrdinfoVO> seletOrderByCondition(OrderOrdinfoVO orderOrdinfoVO);

    /**
     * 订单的查询 可以根据的生产状态
     */
    List<OrderOrdinfoVO> getOrderByStatus(Integer productionState);

    /**
     * 查询发布订单生产订单的信息
     */
    List<OrderOrdinfoVO> getOrderByInfo(OrderOrdinfoVO orderOrdinfoVO);

    /**
     * 根据编号进行查询
     *
     * @param obno
     * @return
     */
    OrderOrdinfoVO getOrderinfoByObno(String obno);

    Boolean save(OrderSchedule schedule);

    /**
     * 自定义分页
     * (部门负责人看全部)
     *
     * @param page
     * @param orderOrdinfo
     * @return
     */
    List<OrderOrdinfoVO> selectOrderOrdinfoPages(IPage page, @Param("orderOrdinfo") OrderOrdinfoVO orderOrdinfo);

    /**
     * 自定义分页
     * (userid权限)
     *
     * @param page
     * @param orderOrdinfo
     * @return
     */
    List<OrderOrdinfoVO> selectOrderOrdinfoPagesByUserId(IPage page, @Param("orderOrdinfo") OrderOrdinfoVO orderOrdinfo);

    /**
     * 查詢訂單編號是否存在
     * (userid权限)
     *
     * @param odNo
     * @return
     */
    Integer getObnoExist(@Param("odNo") String odNo);

    /**
     * 查詢訂單详情
     * (userid权限)
     *
     * @param
     * @return
     */
    OrderOrdinfoVO getOneById(@Param("id") Integer id);

    /**
     * 查询正在执行中或者已经完成的订单
     */
    List<OrderOrdinfoVO> listsAllOrder(@Param("orderOrdinfo") OrderOrdinfoVO orderOrdinfo);

    OrderOrdinfo getOrderInfoByodNo(String odNo);

    String getNewOdId(String od);

    /**
     * 查询订单客户名称和批次编号
     *
     * @return
     */
    List<OrderOrdinfoVO> getCmNameList();

    /**
     * 获取主计划
     * @param request
     * @return
     */
    List<MasterPlanVO> getMasterPlan(@Param("request") MasterPlanRequest request);
}
