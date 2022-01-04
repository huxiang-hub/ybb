package com.yb.order.controller;

import com.yb.order.service.IOrderOrdinfoService;
import com.yb.prod.service.IProdPartsinfoService;
import com.yb.prod.service.IProdProcelinkService;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.prod.vo.ProdProcelinkVO;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/4/12.
 */
@RestController
@RequestMapping("/order")
public class OrderScheduleController {

    @Autowired
    private IOrderOrdinfoService ordinfoService;
    @Autowired
    private IProdPartsinfoService prodPartsinfoService;
    @Autowired
    private IProdProcelinkService prodProcelinkService;

    @GetMapping("/schedule")
    public R schedule() {
        //查询部件集合
        List<ProdPartsinfoVo> prodPartsinfoVoList = prodPartsinfoService.listByPdId(1, 1);
//        查询所有部件的工序
        List<ProdProcelinkVO> prodProcelinkVOList = new ArrayList<>();
        for (ProdPartsinfoVo partsinfoVo : prodPartsinfoVoList){
//            查询每个部件对应的工序集合
            List<ProdProcelinkVO> procelinkVOS = prodProcelinkService.list(partsinfoVo.getId());
            prodProcelinkVOList.addAll(procelinkVOS);
            partsinfoVo.setProdProcelinkVOList(procelinkVOS);
        }
        return R.data(prodProcelinkVOList);
    }

    @GetMapping("/semiPro")
    public R getSemiPro() {
        return R.data(ordinfoService.getSemiPro());
    }
}
