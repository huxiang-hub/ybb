package com.yb.statis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.statis.entity.StatisShiftreach;
import com.yb.statis.vo.GetShiftreachVO;
import com.yb.statis.vo.ShiftreachListVO;
import com.yb.statis.vo.StatisShiftreachVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StatisShiftreachMapper extends BaseMapper<StatisShiftreach> {
    /**
     * 查询需要导出的数据
     * @param targetDay
     * @param maType
     * @param wsId
     * @return
     */
    List<StatisShiftreachVO> selectExcelExport(String targetDay, Integer maType, Integer wsId);

    /**
     * 分页查询数据
     * @param page
     * @param statisShiftreachVO
     * @return
     */
    List<StatisShiftreachVO> statisShiftreachList(IPage<StatisShiftreachVO> page, StatisShiftreachVO statisShiftreachVO);

    /**
     * 获取设备班次达成率
     * @param getShiftreachVO
     * @return
     */
    List<ShiftreachListVO> getShiftreachList(@Param("getShiftreachVO") GetShiftreachVO getShiftreachVO);

    /**
     * 获取设备班次达成率班次合计
     * @param getShiftreachVO
     * @return
     */
    List<ShiftreachListVO> getShiftreachTotal(@Param("getShiftreachVO")GetShiftreachVO getShiftreachVO);

    /**
     * 获取设备班次达成率总合计
     * @param getShiftreachVO
     * @return
     */
    ShiftreachListVO getTotal(@Param("getShiftreachVO")GetShiftreachVO getShiftreachVO);
}
