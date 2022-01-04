package com.yb.execute.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.vo.ExecuteInfoVO;
import com.yb.statis.request.ShiftReachExeinfoRequest;
import com.yb.yilong.request.WbNoInfoRequest;
import com.yb.yilong.response.WbNoInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 执行单_yb_execute_info Mapper 接口
 */
@Mapper
public interface ExecuteInfoMapper extends BaseMapper<ExecuteInfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param executeInfo
     * @return
     */
    List<ExecuteInfoVO> selectExecuteInfoPage(IPage page, ExecuteInfoVO executeInfo);

    List<ExecuteInfo> getSdIdsbyTime(Date startTime, Date endTime, Integer maId);

    List<ExecuteInfo> findByStartTimeAndEndTimeAndMaId(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("maId") Integer maId);

    ExecuteInfo getByMaId(Integer maId);


    ExecuteInfo getByMaIdAndSdId(@Param("maId") Integer maId, @Param("sdId") Integer sdId);

    ExecuteInfo getWorshiftByMaIdAndSdId(Integer maId, Integer sdId, Integer wfId);

    ExecuteInfo getById(Integer exId);

    List<WbNoInfoVO> wbNoInfo(@Param("request") WbNoInfoRequest request);

    List<ExecuteInfoVO> getExecutinfoList(Integer wfId);

    int upEndTime(Integer exId,Date currTime,String operator);

    int upExeTime(Integer exId,Date currTime);

    int upFinishTime(Integer exId,Date currTime);

    List<ExecuteInfoVO> getReachExecuteInfo(@Param("request")ShiftReachExeinfoRequest request);

}
