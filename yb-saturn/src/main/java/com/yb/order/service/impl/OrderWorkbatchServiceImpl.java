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
package com.yb.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.order.entity.OrderWorkbatch;
import com.yb.order.mapper.OrderWorkbatchMapper;
import com.yb.order.service.IOrderWorkbatchService;
import com.yb.order.vo.OrderWorkbatchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 作业批次_yb_order_workbatch 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class OrderWorkbatchServiceImpl extends ServiceImpl<OrderWorkbatchMapper, OrderWorkbatch> implements IOrderWorkbatchService {

    @Autowired
    private  OrderWorkbatchMapper orderWorkbatchMapper;

    @Override
    public IPage<OrderWorkbatchVO> selectOrderWorkbatchPage(IPage<OrderWorkbatchVO> page, OrderWorkbatchVO orderWorkbatch) {
        return page.setRecords(baseMapper.selectOrderWorkbatchPage(page, orderWorkbatch));
    }

    @Override
    public HashMap getOrderByInfo(Integer mId, Integer wbId) {


          return orderWorkbatchMapper.getOrderByInfo(mId,wbId);

    }

    @Override
    public OrderWorkbatchVO getWorkBatchByWbId(Integer wbId) {
        return orderWorkbatchMapper.getWorkBatchByWbId(wbId);
    }

    @Override
    public OrderWorkbatchVO findObjectBybatchNo(String batchNo) {
        return  orderWorkbatchMapper.findObjectBybatchNo(batchNo);
    }

    @Override
    public List<OrderWorkbatchVO> selectOrderWorkbatchListByOdno(String odNo) {
        return orderWorkbatchMapper.selectOrderWorkbatchListByOdno(odNo);
    }

    @Override
    public boolean saveOrderWorkbatch(OrderWorkbatch orderWorkbatch) {
        Boolean status = false;
        OrderWorkbatch orderWorkba = new OrderWorkbatch();
        orderWorkba.setOdId(orderWorkbatch.getOdId());
        orderWorkba.setPlanNum(orderWorkbatch.getPlanNum());
        orderWorkba.setCreateAt(new Date());
        orderWorkba.setCloseTime(orderWorkbatch.getCloseTime());
        orderWorkba.setStatus(1);
        orderWorkba.setUserId(orderWorkbatch.getUserId());
        String batchNoOld = orderWorkbatch.getBatchNo();
        String odNo = batchNoOld.substring(0, 12);
        orderWorkba.setOdNo(odNo);
        int  batchNo= Integer.valueOf(batchNoOld.substring(12)) + 1;
        orderWorkba.setBatchNo(odNo+batchNo);
        int insert = orderWorkbatchMapper.insert(orderWorkba);
        if(insert != 0){
            status  = true;
        }
        return status;
    }

    @Override
    public OrderWorkbatch getNewest(Integer odId) {
        return orderWorkbatchMapper.getNewest(odId);
    }

    @Override
    public List<OrderWorkbatchVO> selectOrderWorkbatchList(OrderWorkbatch orderWorkbatch) {
        return orderWorkbatchMapper.selectOrderWorkbatchList(orderWorkbatch);
    }

    @Override
    public List<OrderWorkbatchVO> batchNumberList(Integer prId, Integer ptId, Integer wbId) {
        return orderWorkbatchMapper.batchNumberList(prId, ptId, wbId);

    }
}
