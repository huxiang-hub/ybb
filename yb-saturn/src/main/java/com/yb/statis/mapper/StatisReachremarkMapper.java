package com.yb.statis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.panelapi.request.WeekHourRateLossRequest;
import com.yb.panelapi.vo.HourRateLossStatisticsVO;
import com.yb.statis.entity.StatisReachremark;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author my
 * @date 2020-07-23
 * Description: 小时达成率-备注_yb_statis_reachremark Mapper
 */
@Mapper
public interface StatisReachremarkMapper extends BaseMapper<StatisReachremark> {


    void updateRemark(@Param("reachremark") StatisReachremark reachremark);

    List<HourRateLossStatisticsVO> hourRateLossStatistics(WeekHourRateLossRequest request);
}