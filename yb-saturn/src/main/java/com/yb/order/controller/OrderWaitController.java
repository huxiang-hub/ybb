package com.yb.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.order.service.OrderWaitService;
import com.yb.order.vo.OrderWaitVO;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orderwait")
public class OrderWaitController extends BladeController {

    @Autowired
    private OrderWaitService orderWaitService;

    @RequestMapping("/list")
    public R<IPage<OrderWaitVO>> selectOrderWaitV(Query query, OrderWaitVO orderWaitVO) {

        IPage<OrderWaitVO> pages = orderWaitService.selectOrderWaitV(orderWaitVO, Condition.getPage(query));
        return R.data(pages);
    }

    @RequestMapping("/ordername")
    public List orderNameList() {

        return orderWaitService.orderList();
    }
    @RequestMapping("/detail")
    public OrderWaitVO selectOrderWaitById(Integer odId) {

        return orderWaitService.selectOrderWaitById(odId);
    }
}