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
package com.yb.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.order.entity.OrderOrdinfo;
import com.yb.order.vo.OrderOrdinfoVO;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 订单表_yb_order_ordinfo 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IOrderOrdinfoService extends IService<OrderOrdinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param orderOrdinfo
     * @return
     */
    IPage<OrderOrdinfoVO> selectOrderOrdinfoPage(IPage<OrderOrdinfoVO> page, OrderOrdinfoVO orderOrdinfo);
    /**
     *订单的查询 可以根据的生产状态
     *
     *
     * @return*/
    List<OrderOrdinfoVO> getOrderByStatus(Integer productionState);

    /**
     *订单的查询 可以根据订单的编号、订单的时间、可以根据订单厂家查询
     *
     * */
    List<OrderOrdinfoVO> seletOrderByCondition(OrderOrdinfoVO orderOrdinfoVO);

    /**
     * 根据编号查询Vo对象
     * @param s
     * @return
     */

    OrderOrdinfoVO getOrderinfoByObno(String s);

    R getSchedule();

    R getSemiPro();

    /**
     * 自定义分页
     *负责人
     * @param page
     * @param orderOrdinfo
     * @return
     */
    IPage<OrderOrdinfoVO> selectOrderOrdinfoPages(IPage<OrderOrdinfoVO> page, OrderOrdinfoVO orderOrdinfo);
    /**
     * 自定义分页
     *个人
     * @param page
     * @param orderOrdinfo
     * @return
     */
    IPage<OrderOrdinfoVO> selectOrderOrdinfoPagesByUserId(IPage<OrderOrdinfoVO> page, OrderOrdinfoVO orderOrdinfo);

    Integer getObnoExist(String odNo);
    /**单个订单的详情
     * @param id
     * @return
     */
    OrderOrdinfoVO getOneById(Integer id);
    /**
     * 查询正在执行中或者已经完成的订单
     */
    List<OrderOrdinfoVO> listsAllOrder(OrderOrdinfoVO orderOrdinfo);
    /**
     * 返回一个当前条件下最新的订单
     */
    String getNewOdId(String od);
}
