package com.yb.supervise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.supervise.entity.SuperviseSchedule;
import com.yb.supervise.request.SuperviseOrderScheduleRequest;
import com.yb.supervise.vo.SuperviseScheduleBatchVO;
import com.yb.supervise.vo.SuperviseScheduleOrderVO;
import com.yb.supervise.vo.SuperviseScheduleVO;

import java.util.List;

/**
 * 订单进度表（进度表-执行） 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface ISuperviseScheduleService extends IService<SuperviseSchedule> {

    /**
     * 自定义分页
     *
     * @param page
     * @param superviseSchedule
     * @return
     */
    IPage<SuperviseScheduleVO> selectSuperviseSchedulePage(IPage<SuperviseScheduleVO> page, SuperviseScheduleVO superviseSchedule);


    List<SuperviseScheduleVO> getBatchSchedule(Integer odId);


    List<SuperviseScheduleVO> getProcessSchedule(Integer batchId);


    /**
     *  订单进度/按订单
     * @param page 分页条件
     * @param request 请求类
     * @return
     */
    IPage<SuperviseScheduleOrderVO> orderScheduleByOrder(IPage<SuperviseScheduleOrderVO> page, SuperviseOrderScheduleRequest request);

    /**
     *  订单进度/按批次
     * @param page 分页条件
     * @param request 请求类
     * @return
     */
    IPage<List<SuperviseScheduleBatchVO>> orderScheduleByBatch(IPage<List<SuperviseScheduleBatchVO>> page, SuperviseOrderScheduleRequest request);


}
