/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yb.execute.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.vo.ExecuteStateParamVO;
import com.yb.execute.vo.ExecuteStateVO;
import io.swagger.models.auth.In;
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
     */
    public ExecuteState getLeaderAide(int userId);

    /**
     * 添加助理
     */
    public boolean addAide(String teamId, int userId) throws SQLException;

    /**
     * @param esId
     * @return
     */
    ExecuteState getExecuteStateByEsId(Integer esId);

    /**
     * 更新保养或者换膜价结束时间
     *
     * @param state
     * @return
     */
    boolean updataExecuteState(ExecuteState state);

    ExecuteState getExecuteStateById(Integer id);

    ExecuteState getExecuteState(Integer ma_id, Integer sd_id);


    Double selectFaultTimeById(@PathParam("maId") Integer maId,
                               @PathParam("sdId") Integer sdId);

    ExecuteState getStateByMaId(Integer maId);

    Double selectProTimeById(@PathParam("maId") Integer maId,
                             @PathParam("sdId") Integer sdId);

    ExecuteStateVO getExecuteVoListBy(Date startTime, Date endTime, String type, Integer maid);

    List<ExecuteStateVO> getAcceptedGoodsByTimeAndMa(Date startTime, Date endTime, Integer maId);

    List<ExecuteStateVO> getExecuteFailStatus(Date startTime, Date endTime, Integer maId, String equipmentErr);

    List<ExecuteStateVO> getExecuteFailclassify(Date startTime, Date endTime, Integer maId, String rest);

    List<ExecuteStateVO> getExecuteExamine(Date startTime, Date endTime, String maId, Integer reprotType);

    List<String> getDistinceIdBy(String userId, Date beforeDate, Date endDate);

    ExecuteStateVO getRealTimebyCondition(Date startTime, Date endTime, String userId);

    List<String> getSdIdbyConditon(Date startTime, Date endTime, Integer maId, String userId, String eventId);

    List<ExecuteState> getSdInfoByCondition(Integer sdId, String eventAccpt, String enventStop, Integer userId);

    List<ExecuteStateVO> getAcceptedGoodsByTimeAndMaId(Date classStartTime, Date classEndTime, Integer maId);

    List<ExecuteStateVO> findMostEalryRecord(String parse, Integer userId);

    List<ExecuteStateVO> findStartTimeByTime(Date tempTime, String parse, Integer userId);

    List<ExecuteStateVO> selectExecuteStatePage(IPage<ExecuteStateVO> page, ExecuteStateVO executeState);

    List<ExecuteStateVO> getExecuteStateDetailBysdId(String sdId, String maId);

    List<ExecuteStateVO> getWorkbatchInfo(Date startTime, Date endTime);

    /*查询工单生产时中途加入或退出的信息*/
    List<ExecuteState> getExecuteStateProdectList(Date startAtProdect, Date startAtStop, Integer sdId, String event);

    ExecuteState getByStartTimeAndEndTimeAndMaIdAndEventAndSdId(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("maId") int maId, @Param("event") String event, @Param("sdId") Integer sdId);

    ExecuteState getByMaIdAndEventAndSdId(@Param("maId") int maId, @Param("event") String event, @Param("sdId") int sdId);

    List<ExecuteState> getParaTimeByCondition(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("userId") Integer userId, @Param("mtId") Integer mtId);

    ExecuteState getByMaIdAndEventAndSdId(@Param("maId") Integer maId, @Param("event") String event, @Param("sdId") Integer sdId);

    /**
     * 设备时序图查询
     * @param executeStateVO
     * @return
     */
    List<ExecuteStateVO> stateList(@Param("executeStateVO") ExecuteStateParamVO executeStateVO);

    List<ExecuteStateVO> getAcceptedGoodsByTime(Integer exId, Integer mtId);

    List<String> getDistinceSdId(Date classStartTime, Date classEndTime, Integer mtId);

    List<ExecuteStateVO> getSumCountByMtId(Date classStartTime, Date classEndTime, Integer mtId);
}
