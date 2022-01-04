package com.anaysis.statis.mapper;


import com.anaysis.statis.entity.StatisOrdreach;
import com.anaysis.workbatch.entity.WorkbatchShiftset;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author my
 * @date 2020-06-11
 * Description: 设备实时达成率_yb_statis_ordreach Mapper
 */
@Mapper
public interface StatisOrdreachMapper extends BaseMapper<StatisOrdreach> {

    StatisOrdreach getByMaIdAndSdIdAndTargetDayAndTargetHour(@Param("maId") Integer maId, @Param("targetDay") String targetDay, @Param("targetHour") Integer targetHour);
    WorkbatchShiftset findByMaIdWsTime(Integer maId, @Param("wsFormat") String wsFormat);
}