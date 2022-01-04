package com.yb.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.order.entity.OrderOrdinfo;
import com.yb.order.mapper.OrderOrdinfoMapper;
import com.yb.order.service.IOrderOrdinfoService;
import com.yb.order.vo.OrderOrdinfoVO;
import com.yb.order.vo.OrderVO;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.mapper.ProcessWorkinfoMapper;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单表_yb_order_ordinfo 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class OrderOrdinfoServiceImpl extends ServiceImpl<OrderOrdinfoMapper, OrderOrdinfo> implements IOrderOrdinfoService {

    @Autowired
    private OrderOrdinfoMapper ordinfoMapper;
    @Autowired
    private WorkbatchOrdlinkMapper ordlinkMapper;
    @Autowired
    private ProcessWorkinfoMapper processMapper;
    @Autowired
    private SuperviseExecuteMapper executeMapper;

    @Override
    public IPage<OrderOrdinfoVO> selectOrderOrdinfoPage(IPage<OrderOrdinfoVO> page, OrderOrdinfoVO orderOrdinfo) {
        return page.setRecords(baseMapper.selectOrderOrdinfoPage(page, orderOrdinfo));
    }

    @Override
    public List<OrderOrdinfoVO> getOrderByStatus(Integer productionState) {

        return ordinfoMapper.getOrderByStatus(productionState);
    }

    @Override
    public List<OrderOrdinfoVO> seletOrderByCondition(OrderOrdinfoVO orderOrdinfoVO) {

        return ordinfoMapper.seletOrderByCondition(orderOrdinfoVO);
    }

    @Override
    public OrderOrdinfoVO getOrderinfoByObno(String obno) {
        return ordinfoMapper.getOrderinfoByObno(obno);
    }

    @Override
    @Transactional
    public R getSchedule() {
        //查询所有订单。
        List<OrderOrdinfo> ordinfos = ordinfoMapper.selectList(null);
        for (OrderOrdinfo ordinfo : ordinfos) {
            //根据订单id 查询所有的排产工单
            List<WorkbatchOrdlink> ordlinks = ordlinkMapper.getOrderListByOrderId(ordinfo.getId());
            //根据订单id，查询当前订单所生产产品的所有工序 （排序过后的工序）
            List<String> processId = processMapper.getProcessIdByOrderId(ordinfo.getId());
            List<ProcessWorkinfo> process = processMapper.getProcessListByOrderId(processId);
            //循环排产列表
            List<WorkbatchOrdlink> arr = new ArrayList<>();
            Integer orderCompleteNum = 0;
            //操作单条的排产信息
            continueOut:
            for (ProcessWorkinfo proInfo : process) {
                for (WorkbatchOrdlink ordlink : ordlinks) {
                    //确认当前工序是否已经排产。
                    if (ordlink.getPrName().equals(proInfo.getPrName())) {
                        Integer currNum = executeMapper.getCurrNumByOderId(ordlink.getMaId(), ordlink.getId());
                        if (currNum == null || "".equals(currNum)) {
                            currNum = 0;
                        }
                        //保存到订单工序进度表，已完成成品数量
                        orderCompleteNum += ordlink.getCompleteNum() + currNum;
                        arr.add(ordlink);
                        //跳出循环
                        continue continueOut;
                    }
                }
                //循环工序信息列表
                //添加没有生产的工序
                WorkbatchOrdlink ordlink1 = new WorkbatchOrdlink();
                ordlink1.setPrName(proInfo.getPrName());
                arr.add(ordlink1);
            }
            ordinfo.setCompleteNum(orderCompleteNum);
            ordinfo.setChildren(arr);
        }
        return R.data(ordinfos);
    }

    @Override   //
    public R getSemiPro() {
        //查询所有订单。
        List<OrderOrdinfo> ordinfos = ordinfoMapper.selectList(null);
        List<OrderVO> result = new ArrayList<>();
        for (OrderOrdinfo ordinfo : ordinfos) {
            OrderVO orderVO = new OrderVO();
            orderVO.setId(ordinfo.getId());
            orderVO.setName(ordinfo.getOdName());
            //根据订单id 查询所有的排产工单
            List<WorkbatchOrdlink> ordlinks = ordlinkMapper.getOrderListByOrderId(ordinfo.getId());
            //根据订单id，查询当前订单所生产产品的所有工序 （排序过后的工序）
            //循环排产列表
            Integer orderCompleteNum = 0;
            //操作单条的排产信息
            List<OrderVO> children = new ArrayList<>();
            for (WorkbatchOrdlink ordlink : ordlinks) {
                OrderVO order = new OrderVO();
                order.setId(ordlink.getId());
                order.setName(ordlink.getPrName());
                order.setNum(ordlink.getCompleteNum());
                children.add(orderVO);
            }
            orderVO.setChildren(children);
            result.add(orderVO);
        }
        return R.data(result);
    }

    /**
     * 计算两个整数的除法，保留2为小数
     *
     * @param a
     * @param b
     * @return
     */
    private Double txfloat(int a, int b) {
        //设置保留位数
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format((float) a / b);
        return Double.valueOf(format);
    }

    /**
     *
     * @param page
     * @param orderOrdinfo
     * @return
     */
    @Override
    public IPage<OrderOrdinfoVO> selectOrderOrdinfoPages(IPage<OrderOrdinfoVO> page, OrderOrdinfoVO orderOrdinfo) {
        return page.setRecords(baseMapper.selectOrderOrdinfoPages(page, orderOrdinfo));
    }
    /**
     *
     * @param page
     * @param orderOrdinfo
     * @return
     */
    @Override
    public IPage<OrderOrdinfoVO> selectOrderOrdinfoPagesByUserId(IPage<OrderOrdinfoVO> page, OrderOrdinfoVO orderOrdinfo) {
        return page.setRecords(baseMapper.selectOrderOrdinfoPagesByUserId(page, orderOrdinfo));
    }
    /**
     *
     */
    @Override
    public Integer getObnoExist(String odNo){
        return ordinfoMapper.getObnoExist(odNo);
    }
    /**
     *
     */
    @Override
    public OrderOrdinfoVO getOneById(Integer id){
        return ordinfoMapper.getOneById(id);
    }

    @Override
    public List<OrderOrdinfoVO> listsAllOrder(OrderOrdinfoVO orderOrdinfo) {
        return ordinfoMapper.listsAllOrder(orderOrdinfo);
    }

    @Override
    public String getNewOdId(String od) {
        return ordinfoMapper.getNewOdId(od);
    }
}
