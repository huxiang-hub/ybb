package com.yb.supervise.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.supervise.entity.SuperviseSchedule;
import com.yb.supervise.mapper.SuperviseScheduleMapper;
import com.yb.supervise.request.SuperviseOrderScheduleRequest;
import com.yb.supervise.service.ISuperviseScheduleService;
import com.yb.supervise.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 订单进度表（进度表-执行） 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
@Slf4j
public class SuperviseScheduleServiceImpl extends ServiceImpl<SuperviseScheduleMapper, SuperviseSchedule> implements ISuperviseScheduleService {

    @Autowired
    private SuperviseScheduleMapper scheduleMapper;

    @Override
    public IPage<SuperviseScheduleVO> selectSuperviseSchedulePage(IPage<SuperviseScheduleVO> page, SuperviseScheduleVO superviseSchedule) {
        List<SuperviseScheduleVO> orderSchedule = scheduleMapper.getOrderSchedule(page, superviseSchedule);
//        for (SuperviseScheduleVO order : orderSchedule) {
//            order.setUuid("orderId" + getUUID());
//            List<SuperviseScheduleVO> batchSchedule = scheduleMapper.getBatchSchedule(order.getOrderId(), superviseSchedule);
//            order.setChildren(batchSchedule);
//            for (SuperviseScheduleVO batch : batchSchedule) {
//                batch.setUuid("batchId" + getUUID());
//                List<SuperviseScheduleVO> partSchedule = scheduleMapper.getPartSchedule(batch.getBatchId());
//                batch.setChildren(partSchedule);
//                for (SuperviseScheduleVO part : partSchedule) {
//                    part.setUuid("partId" + getUUID());
//                    List<SuperviseScheduleVO> processSchedule = scheduleMapper.getProcessSchedule(part.getPtId());
//                    part.setChildren(processSchedule);
//                    for (SuperviseScheduleVO process : processSchedule) {
//                        process.setUuid("prId" + getUUID());
//                    }
//                }
//            }
//        }
        return page.setRecords(orderSchedule);
    }

    /**
     * 订单进度/按订单
     *
     * @param page    分页条件
     * @param request 请求类
     * @return
     */
    @Override
    public IPage<SuperviseScheduleOrderVO> orderScheduleByOrder(IPage<SuperviseScheduleOrderVO> page, SuperviseOrderScheduleRequest request) {
        List<SuperviseScheduleOrderVO> orderSchedule = scheduleMapper.getOrderScheduleByOrder(page, request);
        if (orderSchedule.isEmpty()) {
            log.info("按订单获取订单进度成功，暂无订单进度数据:request:{}", request);
            return page.setRecords(new ArrayList());
        }
        for (SuperviseScheduleOrderVO superviseSchedule : orderSchedule) {
            List<SuperviseScheduleBatchVO> batchSchedule = scheduleMapper.getBatchSchedule(superviseSchedule.getOdId(), request);
            superviseSchedule.setBatchList(batchSchedule);
        }
        return page.setRecords(orderSchedule);
    }

    /**
     * 订单进度/按批次
     *
     * @param page    分页条件
     * @param request 请求类
     * @return
     */
    @Override
    public IPage<List<SuperviseScheduleBatchVO>> orderScheduleByBatch(IPage<List<SuperviseScheduleBatchVO>> page, SuperviseOrderScheduleRequest request) {

        List<List<SuperviseScheduleBatchVO>> batchList = new ArrayList<>();
        List<SuperviseScheduleOrderVO> orderSchedule = scheduleMapper.getOrderScheduleByOrder(page, request);
        if (orderSchedule.isEmpty()) {
            log.info("按批次获取订单进度成功，暂无订单进度数据:request:{}", request);
            return page.setRecords(new ArrayList());
        }
        for (SuperviseScheduleOrderVO superviseSchedule : orderSchedule) {
            List<SuperviseScheduleBatchVO> batchSchedule = scheduleMapper.getBatchSchedule(superviseSchedule.getOdId(), request);
            for (SuperviseScheduleBatchVO batch : batchSchedule) {
                List<SuperviseSchedulePtVO> partSchedule = scheduleMapper.getPartSchedule(batch.getBatchId());
                batch.setPtList(partSchedule);
                for (SuperviseSchedulePtVO part : partSchedule) {
                    List<SuperviseSchedulePrVO> processSchedule = scheduleMapper.getPrSchedule(part.getPtId());
                    part.setPrList(processSchedule);
                }
            }
            batchList.add(batchSchedule);
        }
        return page.setRecords(batchList);
    }

    @Override
    public List<SuperviseScheduleVO> getBatchSchedule(Integer odId) {
        return null;
    }

    @Override
    public List<SuperviseScheduleVO> getProcessSchedule(Integer batchId) {
        return scheduleMapper.getProcessSchedule(batchId);
    }


    //获取uuid
    private String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }
}
