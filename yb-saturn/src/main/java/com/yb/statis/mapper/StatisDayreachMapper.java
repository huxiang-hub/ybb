package com.yb.statis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.statis.entity.StatisDayreach;
import com.yb.statis.vo.DayreachParmsVO;
import com.yb.statis.vo.StatisDayreachVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StatisDayreachMapper extends BaseMapper<StatisDayreach> {
    /**
     * 查询需要导出的数据
     * @param dayreachParmsVO
     * @return
     */
    List<StatisDayreachVO> selectDayreach(@Param("dayreachParmsVO") DayreachParmsVO dayreachParmsVO);

    /**
     * 分页查询
     * @param statisDayreachVO
     * @param page
     * @return
     */
    List<StatisDayreachVO> statisDayreachPage(StatisDayreachVO statisDayreachVO, IPage<StatisDayreachVO> page);
}
