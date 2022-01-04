package com.yb.supervise.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.supervise.entity.SuperviseIntervalalg;
import com.yb.supervise.vo.SuperviseIntervalalgEventVO;

public interface SuperviseIntervalalgService extends IService<SuperviseIntervalalg> {
    /**
     * 创建事件
     * @param superviseIntervalalgEventVO
     */
    void createEvent(SuperviseIntervalalgEventVO superviseIntervalalgEventVO);

    /**
     * 删除过期数据
     */
    void deleteSuperviseIntervalalg();
}
