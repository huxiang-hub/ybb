package com.yb.stroe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.stroe.entity.StoreSeat;
import com.yb.stroe.vo.StoreSeatVO;

import java.util.List;

public interface StoreSeatService extends IService<StoreSeat> {
    List<StoreSeat> getByAreaId(Integer areaId);

    /**
     * 查询上工序托盘的库位信息
     * @param wfId
     * @return
     */
    List<StoreSeatVO> upStoreSeatList(Integer wfId);

    /**
     * 根据执行单查询本工序库位详情列表
     * @param exId
     * @return
     */
    List<StoreSeatVO> getStoreSeatByExId(Integer exId);
}
