package com.yb.panelapi.order.service;

import com.yb.panelapi.order.entity.OrderReportNewVO;
import com.yb.panelapi.order.entity.OrderReportVo;
import com.yb.panelapi.request.OrderReportRequest;
import com.yb.panelapi.user.utils.R;
import com.yb.workbatch.vo.AnewWfsDSortVO;

import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/15.
 */
public interface IPApiOrderReportService{

    R orderReport(OrderReportVo reportVo);

    /****
     * 新的接口上报对象信息内容
     * @param reportVo
     * @return
     */
    R orderReportNew(OrderReportNewVO reportVo);

    /**
     * 机台排序
     * @param anewWfsDSortVO
     * @return
     */
    Boolean anewWfsDSort(AnewWfsDSortVO anewWfsDSortVO);
}
