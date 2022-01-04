package com.yb.execute.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.vo.ExecuteInfoVO;
import com.yb.statis.request.ShiftReachExeinfoRequest;
import com.yb.yilong.request.WbNoInfoRequest;
import com.yb.yilong.response.WbNoInfoVO;

import java.util.Date;
import java.util.List;

/**
 * 执行单_yb_execute_info 服务类
 *
 */
public interface IExecuteInfoService extends IService<ExecuteInfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param executeInfo
     * @return
     */
    IPage<ExecuteInfoVO> selectExecuteInfoPage(IPage<ExecuteInfoVO> page, ExecuteInfoVO executeInfo);

    /**
     * 在规定时间呢 拿到sd——id
     * @param startTime
     * @param endTime
     * @return
     */
    List<ExecuteInfo> getSdIdsbyTime(Date startTime, Date endTime,Integer maId);

    List<WbNoInfoVO> wbNoInfo(WbNoInfoRequest request);

    int upExeTime(Integer exId,Date currTime);

    int upEndTime(ExecuteInfo executeInfo);

    int upFinishTime(Integer exId,Date currTime);

    List<ExecuteInfoVO> getReachExecuteInfo(ShiftReachExeinfoRequest request);

}
