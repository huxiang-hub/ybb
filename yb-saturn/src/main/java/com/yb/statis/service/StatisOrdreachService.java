package com.yb.statis.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.statis.entity.StatisOrdreach;
import com.yb.statis.request.StatisOrdreachPageRequest;
import com.yb.statis.request.StatisOrdreachSaveUpdateRequest;
import com.yb.statis.vo.HourRateVO;
import com.yb.statis.vo.StatisOrdreachListVO;
import com.yb.statis.vo.StatisOrdreachVO;
import com.yb.statis.vo.TodayOrdreachVO;
import com.yb.system.dict.entity.Dict;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author my
 * @date 2020-06-11
 * Description: 设备订单达成率_yb_statis_ordreach Service
 */
public interface StatisOrdreachService extends IService<StatisOrdreach> {

    List<StatisOrdreachListVO> list(StatisOrdreachPageRequest request);


    /**
     * 小时达成率
     *
     * @param request
     * @return
     */
    List<StatisOrdreachListVO> hourRateList(StatisOrdreachPageRequest request);

    /**
     * 修改达成率备注
     *
     * @param request
     */
    void update(StatisOrdreachSaveUpdateRequest request);

    /**
     * 设置达成率计划-1
     */
    void setOrdreachByMaid(String targetDay, Integer wsId, Integer maId);


    /****
     * 设定排序顺序
     * @param targetDay
     * @param wsId
     * @param maId
     */
    void setOrdreach(String targetDay, Integer wsId, Integer maId, String tenantId);

    /****
     * 前期编写测试方法之一，当前已作废未使用
     * @param targetDay
     * @param wsId
     * @param maId
     */
    void updateStatisOrdreach(String targetDay, Integer wsId, Integer maId);

    /**
     * 小时达成率详情信息
     *
     * @param id
     * @return
     */
    StatisOrdreachVO get(Integer id);

    /**
     * 获取达成率备注字典信息
     *
     * @return
     */
    List<Dict> getReachDictList();

    int ordreachInsertT(StatisOrdreach ordreach);

    //清除当天当班次的设备信息
    int clearOrdreach(String targetDay, Integer wsId, Integer maId);

    Integer getIslockReach(String targetDay, Integer wsId, Integer maId);

    Map<Integer, Integer> getRealcoutByHour(Date classStartTime, Date classEndTime, Integer maId);

    List<TodayOrdreachVO> todyOrdreach();

    /**
     * 获取小时达成率列表
     *
     * @param hourRateVO
     * @param page
     * @return
     */
    IPage<StatisOrdreachVO> gethourRateList(HourRateVO hourRateVO, IPage<StatisOrdreachVO> page);
}
