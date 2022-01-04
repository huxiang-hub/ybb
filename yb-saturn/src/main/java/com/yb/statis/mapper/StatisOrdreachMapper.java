package com.yb.statis.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.machine.request.MachineReportRequest;
import com.yb.machine.response.MachineOrdreachPortVO;
import com.yb.statis.entity.StatisDayreach;
import com.yb.statis.entity.StatisOrdreach;
import com.yb.statis.request.StatisOrdreachPageRequest;
import com.yb.statis.vo.HourRateVO;
import com.yb.statis.vo.StatisOrderreachListVO;
import com.yb.statis.vo.StatisOrdreachVO;
import com.yb.statis.vo.TodayOrdreachVO;
import com.yb.supervise.request.GetYieldStatisticsRequest;
import com.yb.supervise.vo.YieldStatisticsListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author my
 * @date 2020-06-11
 * Description: 设备实时达成率_yb_statis_ordreach Mapper
 */
@Mapper
public interface StatisOrdreachMapper extends BaseMapper<StatisOrdreach> {

    List<StatisOrderreachListVO> list(@Param("request") StatisOrdreachPageRequest request);

    StatisOrdreach getByMaIdAndSdIdAndTargetHour(@Param("maId") Integer maId, @Param("sdId") Integer sdId, @Param("hours") int hours);


    StatisOrdreach getOrdreachBySingle(String sdDate, Integer wsId, Integer maId, Integer hour);


    List<StatisOrdreach> findByMaIdAndSdIdAndTargetHourAndTargetDay(@Param("maId") Integer maId, @Param("sdId") Integer sdId, @Param("hours") int hours, @Param("targetDay") String targetDay);

    /**
     * 跨天班次查询前半段
     *
     * @param targetDay
     * @param startTargetHour
     * @return
     */
    List<StatisOrdreach> startOrdreachList(String targetDay, Integer startTargetHour, Integer maId);

    /**
     * 跨天班次查询后半段
     *
     * @param targetDay
     * @param endTargetHour
     * @return
     */
    List<StatisOrdreach> endOrdreachList(String targetDay, Integer endTargetHour, Integer maId);

    /**
     * 查询班次下的数据
     *
     * @param targetDay
     * @param startTargetHour
     * @param endTargetHour
     * @return
     */
    List<StatisOrdreach> statisOrdreachList(String targetDay, Integer startTargetHour, Integer endTargetHour, Integer maId);

    List<StatisOrdreach> statisOrdInitList(String targetDay, Integer wsId, Integer maId);

    int statisOrdInitListCount(String targetDay, Integer wsId, Integer maId);

    void updateStatisOrdreach(Integer maId, Integer wsId, String targetDay, Integer hour, Integer cont, String sdId);

    int updateByStatisOrdreach(@Param("ordreach") StatisOrdreach ordreach);

    int clearOrdreach(String targetDay, Integer wsId, Integer maId);

    List<YieldStatisticsListVO> findByTargetDateAndMaTypeAndWsId(@Param("request") GetYieldStatisticsRequest request);

    int lockOrdreach(List<Integer> ordrIds, Integer islock);

    Integer getIslockReach(String targetDay, Integer wsId, Integer maId);

    int refreshRealcount(Integer wsId, String sdDate, Integer maId, Integer hour, Integer cont);

    /**
     * 查询当天的设备id
     *
     * @param targetDay
     * @return
     */
    List<Integer> getMaidsByTargetDay(String targetDay);

    void updateRemark(@Param("id") Integer id, @Param("remark") String remark);

    /**
     * 查询达成率日报的计划数和实际完成数
     *
     * @param targetDay
     * @param maType
     * @return
     */
    StatisDayreach selectStatisDayreach(String targetDay, Integer maType);

    List<TodayOrdreachVO> todyOrdreach();

    List<MachineOrdreachPortVO> ordreachPort(@Param("request") MachineReportRequest request);

    /**
     * 获取小时达成率列表
     * @param hourRateVO
     * @param page
     * @return
     */
    List<StatisOrdreachVO> gethourRateList(@Param("hourRateVO") HourRateVO hourRateVO, IPage<StatisOrdreachVO> page);
}
