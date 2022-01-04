package com.sso.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.panelapi.vo.ExecuteStateVO;
import com.sso.supervise.entity.ExecuteState;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 执行表状态_yb_execute_state Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ExecuteStateMapper extends BaseMapper<ExecuteState> {

    /**
     * 自定义分页
     *
     * @param page
     * @param executeState
     * @return
     */
    List<ExecuteStateVO> selectPage(IPage page, ExecuteStateVO executeState);

    int insertByTime(@Param("startAt") String startAt, @Param("endAt") String endAt);

    List<ExecuteStateVO> selectDayRecord(Date start_at);

    HashMap selectTask(int id);
    /**
     * 获取当前人的执行状态表信息
     * */
    public ExecuteState getLeaderAide(int userId);
    /**
     * 添加助理
     * */
    public boolean addAide(String teamId,int userId) throws SQLException;


    /**
     *
     * @param esId
     * @return
     */
    ExecuteState getExecuteStateByEsId(Integer esId);
    /**
     * 更新保养或者换膜价结束时间
     * @param state
     * @return
     */
    boolean updataExecuteState(ExecuteState state);


    ExecuteState  getExecuteStateById(Integer id);

    ExecuteState getExecuteState(Integer ma_id,Integer sd_id);


    Double selectFaultTimeById(@PathParam("maId") Integer maId,
                               @PathParam("sdId") Integer sdId);

    ExecuteState getStateByMaId(Integer maId);

    Double selectProTimeById(@PathParam("maId") Integer maId,
                                @PathParam("sdId") Integer sdId);

    ExecuteStateVO getExecuteVoListBy(Date startTime, Date endTime, String type,String maid);

    List<ExecuteStateVO> getAcceptedGoodsByTimeAndMa(Date startTime, Date endTime, String maId);

    List<ExecuteStateVO> getExecuteFailStatus(Date startTime, Date endTime, String maId, String equipmentErr);

    List<ExecuteStateVO> getExecuteFailclassify(Date startTime, Date endTime, String maId, String rest);

    List<ExecuteStateVO> getExecuteExamine(Date startTime, Date endTime, String maId, Integer reprotType);

    List<String> getDistinceIdBy(String userId, Date beforeDate, Date endDate);

    ExecuteStateVO getRealTimebyCondition(Date startTime, Date endTime, String userId);

    List<String> getSdIdbyConditon(Date startTime, Date endTime, String maId, String userId, String eventId);

    List<ExecuteState> getSdInfoByCondition(Integer sdId, String eventAccpt, String enventStop,Integer userId);

    List<ExecuteStateVO> getAcceptedGoodsByTimeAndMaId(Date classStartTime, Date classEndTime, String maId);

    List<ExecuteStateVO> findMostEalryRecord(String parse,Integer userId);

    List<ExecuteStateVO> findStartTimeByTime(Date tempTime, String parse, Integer userId);

    List<ExecuteStateVO> selectExecuteStatePage(IPage<ExecuteStateVO> page, ExecuteStateVO executeState);

    List<ExecuteStateVO> getExecuteStateDetailBysdId(String sdId);

    List<ExecuteStateVO> getWorkbatchInfo(Date startTime, Date endTime);
}
