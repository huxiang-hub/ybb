package com.yb.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.actset.mapper.ActsetCheckLogMapper;
import com.yb.actset.vo.CheckModelVO;
import com.yb.actset.vo.OrderCheckModelVO;
import com.yb.mater.entity.MaterMtinfo;
import com.yb.mater.mapper.MaterMtinfoMapper;
import com.yb.order.entity.OrderOrdinfo;
import com.yb.order.entity.OrderWorkbatch;
import com.yb.order.mapper.OrderOrdinfoMapper;
import com.yb.order.mapper.OrderWaitMapper;
import com.yb.order.mapper.OrderWorkbatchMapper;
import com.yb.order.service.OrderWaitService;
import com.yb.order.vo.OrderWaitVO;
import com.yb.order.wrapper.OrderWaitWrapper;
import com.yb.prod.mapper.ProdProcelinkMapper;
import com.yb.prod.vo.ProdProcelinkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class OrderWaitServiceImpl extends ServiceImpl<OrderWaitMapper, OrderOrdinfo> implements OrderWaitService {

    @Autowired
    private MaterMtinfoMapper materMtinfoMapper;
    @Autowired
    private OrderWaitMapper orderWaitMapper;
    @Autowired
    private ProdProcelinkMapper prodProcelinkMapper;
    @Autowired
    private OrderWorkbatchMapper orderWorkbatchMapper;
    @Autowired
    private OrderOrdinfoMapper orderOrdinfoMapper;

    @Override
    public IPage<OrderWaitVO> selectOrderWaitV(OrderWaitVO orderWaitVO, IPage<OrderWaitVO> page) {
        CheckModelVO modelVO = new CheckModelVO();
        modelVO.setStatus(1);
        modelVO.setAsType("A");
        modelVO.setAwType("produce");
        List<OrderWaitVO> orderWaitVOS = orderWaitMapper.selectOrderWaitV
                (modelVO, page, orderWaitVO.getCmShortname(), orderWaitVO.getOdNo());
        for (OrderWaitVO orderWait : orderWaitVOS) {
            if (orderWait != null){
                Integer pdId = orderWait.getPdId();
                List<MaterMtinfo> materMtinfos = materMtinfoMapper.selectMaterMtinfos(pdId);
                List<ProdProcelinkVO> prodProcelinkVOs = prodProcelinkMapper.selectOrderProcelink(pdId);
                List<String> materMtinfoss = new ArrayList();
                for (MaterMtinfo materMtinfo : materMtinfos) {
                    if (materMtinfo.getMlName() != null) {
                        materMtinfoss.add(materMtinfo.getMlName());
                    }
                }
                orderWait.setMaterMtinfos(materMtinfoss);
                List PrNames = new ArrayList();
                for (ProdProcelinkVO prodProcelinkVO : prodProcelinkVOs) {
                    if (prodProcelinkVO.getPrName() != null) {
                        PrNames.add(prodProcelinkVO.getPrName());
                    }
                }
                orderWait.setProdProcelink(PrNames);
            }
        }
        return page.setRecords(orderWaitVOS);
    }

    @Override
    public OrderWaitVO selectOrderWaitById(Integer odId) {
        return orderWaitMapper.selectOrderWaitById(odId);
    }

    @Override
    public List<OrderWaitVO> orderList() {
       /* //TODO:审核流程修改了需要修改----现在是查询计划待审核的订单
        CheckModelVO modelVO = new CheckModelVO();//审核流程
        modelVO.setStatus(1);
        modelVO.setAsType("A");
        modelVO.setAwType("plan");
        List<OrderWaitVO> orderNames = orderWaitMapper.orderList(modelVO);*/
        /*查询所有订单*/
        List<OrderOrdinfo> orderOrdinfos = orderOrdinfoMapper.selectList(new QueryWrapper<>());
        OrderWaitWrapper orderWaitWrapper = new OrderWaitWrapper();
        List<OrderWaitVO> orderNames = new ArrayList<>();
        if(!orderOrdinfos.isEmpty()){//把OrderOrdinfo转化为OrderWaitVO
            for(OrderOrdinfo orderOrdinfo : orderOrdinfos){
                OrderWaitVO orderWaitVO = orderWaitWrapper.entityVO(orderOrdinfo);
                orderNames.add(orderWaitVO);
            }
        }
        /*if(!orderNames.isEmpty()){
            Iterator<OrderWaitVO> it = orderNames.iterator();
            while (it.hasNext()){
                OrderWaitVO odfo = it.next();
                Integer planNum = 0;
                *//*查询审核通过的批次信息*//*
                List<OrderWorkbatch> orderWorkbatches = orderWorkbatchMapper.selectList(new QueryWrapper<OrderWorkbatch>()
                        .eq("od_id", odfo.getId()).eq("status", 5));
                if(orderWorkbatches.isEmpty()){//如果该订单下没有审核通过的批次则干掉这个订单信息(不展示)
                    it.remove();
                    continue;
                }
               *//* for(OrderWorkbatch orderWorkbatche: orderWorkbatches){
                    planNum = planNum + orderWorkbatche.getPlanNum();
                }
                Integer orderUnfinishedNum = odfo.getOdCount() - planNum;//订单未分批的数量,未用*//*
                odfo.setWbNames(orderWorkbatches);
                //odfo.setOrderUnfinishedNum(orderUnfinishedNum);//订单未分批的数量,未用
            }
        }*/
        //------------优化-----------------
        /*查询所有已下发批次*/
        List<OrderWorkbatch> orderWorkbatches = orderWorkbatchMapper.selectList(new QueryWrapper<OrderWorkbatch>().eq("status", 5));
        if(!orderNames.isEmpty()){
            Iterator<OrderWaitVO> it = orderNames.iterator();
            while (it.hasNext()){//遍历订单
                OrderWaitVO orderWaitVO = it.next();
                List<OrderWorkbatch> orderWorkbatcheList = new ArrayList<>();
                if(!orderWorkbatches.isEmpty()){
                    for(OrderWorkbatch OrderWorkbatch : orderWorkbatches){//遍历批次
                        if(orderWaitVO.getId().equals(OrderWorkbatch.getOdId())){//找到每个订单下对应的批次
                            orderWorkbatcheList.add(OrderWorkbatch);
                        }
                    }
                }
                if(orderWorkbatcheList.isEmpty()){//如果订单下没有批次则干掉订单
                    it.remove();
                    continue;
                }
                orderWaitVO.setWbNames(orderWorkbatcheList);
            }

        }

        return orderNames;
    }
}
