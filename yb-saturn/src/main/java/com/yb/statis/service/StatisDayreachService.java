package com.yb.statis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.statis.entity.StatisDayreach;
import com.yb.statis.vo.DayreachParmsVO;
import com.yb.statis.vo.StatisDayreachVO;

public interface StatisDayreachService extends IService<StatisDayreach> {
    /**
     * 导出设备类型Excel
     * @param dayreachParmsVO
     */
    Integer exportDayreach(DayreachParmsVO dayreachParmsVO);

    IPage<StatisDayreachVO> statisDayreachPage(StatisDayreachVO statisDayreachVO, IPage<StatisDayreachVO> page);

    /**
     * 重新生成传入日期的达成率日报
     * @param targetDay
     * @return
     */
    boolean timedTaskStatisDayreach(String targetDay);
}
