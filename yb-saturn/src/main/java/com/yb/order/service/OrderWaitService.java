package com.yb.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.actset.vo.OrderCheckModelVO;
import com.yb.order.entity.OrderOrdinfo;
import com.yb.order.vo.OrderWaitVO;

import java.util.List;

public interface OrderWaitService extends IService<OrderOrdinfo> {

    IPage<OrderWaitVO> selectOrderWaitV(OrderWaitVO orderWaitVO, IPage<OrderWaitVO> page);

    /**
     * 根据id查询待生产的订单信息
     * @param odId
     * @return
     */
    OrderWaitVO selectOrderWaitById(Integer odId);

    List<OrderWaitVO> orderList();
}
