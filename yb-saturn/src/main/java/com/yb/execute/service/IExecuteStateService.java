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
package com.yb.execute.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.vo.EquipmentVO;
import com.yb.execute.vo.ExecuteStateParamVO;
import com.yb.execute.vo.ExecuteStateVO;
import com.yb.execute.vo.equipmenttimingVO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 执行表状态_yb_execute_state 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IExecuteStateService extends IService<ExecuteState> {

    /**
     * 自定义分页
     *
     * @param page
     * @param executeState
     * @return
     */
    IPage<ExecuteStateVO> selectExecuteStatePage(IPage<ExecuteStateVO> page, ExecuteStateVO executeState);

    int insertByTime(String startAt, String endAt);
    /**
     * 保养的信息记录
     * */
     List<ExecuteStateVO>   selectDayRecord(Date start_at);

    int saveState(ExecuteState state);

    int  insert(ExecuteState executeState);
    /**
     * 获取当前人的执行状态表信息
     */
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

    ExecuteState  getExecuteState();
    ExecuteState  getExecuteStateById(Integer id);

    /**
     * 保养时间的查询
     */
     ExecuteStateVO getExecuteVoListBy(Date startTime,Date endTime,String type,Integer mtId);
    /**
     * 良品数量的查询
     */
    List<ExecuteStateVO> getAcceptedGoodsByTimeAndMa(Date startTime,Date endTime,Integer mtId);

    /**
     * equipmentErr 大类的err
     * @param startTime
     * @param endTime
     * @param maId
     * @param equipmentErr
     * @return
     */

    List<ExecuteStateVO> getExecuteFailStatus(Date startTime, Date endTime, Integer maId, String equipmentErr);

    /**
     * 小类的err
     * @param startTime
     * @param endTime
     * @param maId
     * @param rest
     * @return
     */
    List<ExecuteStateVO> getExecuteFailclassify(Date startTime, Date endTime, Integer maId, String rest);

    /**
     * 拿到机器的工作时间
     * @param startTime
     * @param endTime
     * @param maId
     * @param reprotType
     * @return
     */
    List<ExecuteStateVO> getExecuteExamine(Date startTime, Date endTime, String maId, Integer reprotType);

    /**
     * 通过用户id 拿到唯一的排查id
     * @param userId
     * @param beforeDate
     * @param endDate
     * @return
     */
    List<String> getDistinceIdBy(String userId, Date beforeDate, Date endDate);

    /**
     * `通过开始时间和结束时间，还有用户id
     * @param startTime
     * @param endTime
     * @param userId
     * @return
     */
    ExecuteStateVO getRealTimebyCondition(Date startTime, Date endTime, String userId);

    /**
     *  通过条件查询排产的ids
     * @param startTime
     * @param endTime
     * @param maId
     * @param userId
     * @param eventId
     * @return
     */
    List<String> getSdIdbyConditon(Date startTime, Date endTime, Integer maId, String userId, String eventId);

    /**
     *  获取前2条数据为我们的executeState
     * @param sdId
     * @param eventAccpt
     * @param enventStop
     * @return
     */
    List<ExecuteState> getSdInfoByCondition(Integer sdId, String eventAccpt, String enventStop, Integer userId);

    /**
     * 获取我们现在需要的vos
     * @param classStartTime
     * @param classEndTime
     * @param maId
     * @return
     */
    List<ExecuteStateVO> getAcceptedGoodsByTimeAndMaId(Date classStartTime, Date classEndTime, Integer maId);

    /**
     * 通过今天的日期找到最早的打卡时间
     * @param parse
     * @return
     */
    List<ExecuteStateVO> findMostEalryRecord(String parse,Integer userId);

    /**
     * 根据我们的拿到第一个的班次的结束时间然后去找后面的第一次上班时间
     * @param tempTime
     * @param parse
     * @param userId
     * @return
     */
    List<ExecuteStateVO> findStartTimeByTime(Date tempTime, String parse, Integer userId);

    List<ExecuteStateVO> getExcuteStateDetailBysdId(String sdId,String maId);

    List<ExecuteStateVO> getWorkbatchInfo(Date startTime, Date endTime);

    List<ExecuteState> getParaTimeByCondition(Date startTime, Date endTime, Integer userId, Integer mtId);

    /**
     *设备时序图查询
     * @param executeStateParamVO
     * @return
     */
    List<EquipmentVO> stateList(ExecuteStateParamVO executeStateParamVO);

    List<ExecuteStateVO> getAcceptedGoodsByTime(Integer exId, Integer mtId);

    List<String> stateMachineList(ExecuteStateParamVO executeStateParamVO);

    List<String> getDistinceSdId(Date classStartTime, Date classEndTime, Integer mtId);

    List<ExecuteStateVO> getSumCountByMtId(Date classStartTime, Date classEndTime, Integer mtId);
}
