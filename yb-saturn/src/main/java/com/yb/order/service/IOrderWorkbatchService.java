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
import com.yb.order.entity.OrderWorkbatch;
import com.yb.order.vo.OrderWorkbatchVO;

import java.util.HashMap;
import java.util.List;

/**
 * 作业批次_yb_order_workbatch 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IOrderWorkbatchService extends IService<OrderWorkbatch> {

    /**
     * 自定义分页
     *
     * @param page
     * @param orderWorkbatch
     * @return
     */
    IPage<OrderWorkbatchVO> selectOrderWorkbatchPage(IPage<OrderWorkbatchVO> page, OrderWorkbatchVO orderWorkbatch);

    /**
     * 订单批量生产展示根据
     *
     * */
    HashMap getOrderByInfo(Integer mId, Integer wbId);

    OrderWorkbatchVO getWorkBatchByWbId(Integer wbId);

    //根据batchNo找到对象
    OrderWorkbatchVO findObjectBybatchNo(String batchNo);

    /**
     * 订单编号查询批次集合
     */
    List<OrderWorkbatchVO> selectOrderWorkbatchListByOdno(String odNo);

    /**
     * 新增订单批次
     * @param orderWorkbatch
     * @return
     */
    boolean saveOrderWorkbatch(OrderWorkbatch orderWorkbatch);

    OrderWorkbatch getNewest(Integer odId);
    /**
     * 条件查询批次集合
     */
    List<OrderWorkbatchVO> selectOrderWorkbatchList(OrderWorkbatch orderWorkbatch);

    /**
     * 查询该批次下已排产的工序
     * @param prId
     * @param ptId
     * @param wbId
     * @return
     */
    List<OrderWorkbatchVO> batchNumberList(Integer prId, Integer ptId, Integer wbId);
}
