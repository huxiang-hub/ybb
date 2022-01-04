package com.yb.statis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.statis.entity.StatisShiftreach;
import com.yb.statis.vo.GetShiftreachVO;
import com.yb.statis.vo.ShiftreachListVO;
import com.yb.statis.vo.StatisShiftreachVO;

import java.util.List;

public interface StatisShiftreachService extends IService<StatisShiftreach> {

    Integer statisShiftreachExcelExport(String targetDay, Integer maType, Integer wsId);

    /**
     * 分页查询数据
     * @param page
     * @param statisShiftreachVO
     * @return
     */
    IPage<StatisShiftreachVO> statisShiftreachList(IPage<StatisShiftreachVO> page, StatisShiftreachVO statisShiftreachVO);

    /**
     * 重新生成班次达成率
     * @param targetDay
     * @param wsId
     * @return
     */
    boolean timedTaskStatisShiftreach(String targetDay, Integer wsId);

    /**
     * 获取设备班次达成率
     * @param getShiftreachVO
     * @return
     */
    List<ShiftreachListVO> getShiftreachList(GetShiftreachVO getShiftreachVO);

    Integer shiftreachExcelExport(GetShiftreachVO getShiftreachVO);

    /**
     * 获取设备班次达成率合计
     * @param getShiftreachVO
     * @return
     */
    List<ShiftreachListVO> getShiftreachTotal(GetShiftreachVO getShiftreachVO);
}
