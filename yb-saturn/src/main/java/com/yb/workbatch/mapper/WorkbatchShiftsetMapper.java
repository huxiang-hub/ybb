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
package com.yb.workbatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.workbatch.entity.WorkbatchMainshift;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.request.WorkbatchShiftsetPageRequest;
import com.yb.workbatch.vo.WorkbatchMachShiftVO;
import com.yb.workbatch.vo.WorkbatchShiftsetPageVO;
import com.yb.workbatch.vo.WorkbatchShiftsetVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 排产班次设定_yb_workbatch_shifts（班次） Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface WorkbatchShiftsetMapper extends BaseMapper<WorkbatchShiftset> {


    WorkbatchShiftset selectByMaid(Integer wsId, Integer maId);

    WorkbatchShiftset getDefaultWsid();

    List<WorkbatchShiftset> getMainShift();

    List<WorkbatchShiftset> getShiftBymaType(Integer maType);

    /**
     * 自定义分页
     *
     * @param page
     * @param workbatchShiftset
     * @return
     */
    List<WorkbatchShiftsetVO> selectWorkbatchShiftsetPage(IPage page, @Param("workbatchShiftset") WorkbatchShiftsetVO workbatchShiftset);

    /**
     * 根据班次model 信息查询班次
     *
     * @param model
     * @param bdId
     * @return
     */
    List<WorkbatchShiftsetVO> getWorkbatchByCondition(Integer model, Integer bdId);

    /**
     * 根据userId 和checkeDate
     *
     * @param dateStr
     * @param userId
     * @return
     */
    List<WorkbatchShiftsetVO> getWorkbatchShiftByUserIdAndCheckDate(String dateStr, Integer userId);

    List<WorkbatchShiftsetVO> getStartAndEndDate(Integer model, Integer dbId);

    List<WorkbatchShiftsetVO> getWorkbatchBydate(@Param("workbatchshiftsetvo") WorkbatchShiftsetVO workbatchshiftsetvo);

    /**
     * 获取今天班次集合
     *
     * @return
     */
    List<WorkbatchShiftsetVO> getToDayList();

    /**
     * 查询这个人是否有调休
     *
     * @param dateStr
     * @param userId
     * @return
     */
    List<WorkbatchShiftsetVO> getTempWorkbatchShiftByUserIdAndCheckDate(String dateStr, Integer userId);


    List<WorkbatchShiftset> getList();

    WorkbatchShiftset getListBytoday();

    WorkbatchShiftset getListBytomorrow();

    WorkbatchShiftset findByMaIdWsTime(Integer maId, String wsFormat);

    List<WorkbatchShiftset> getShiftsetByMatype(Integer maType);

    List<WorkbatchShiftset> getWorkbatchShiftsetByModel(Integer model);

    List<Integer> findNowWsIds();

    List<WorkbatchShiftsetPageVO> page(IPage page, @Param("request") WorkbatchShiftsetPageRequest request);

    @Select("SELECT DISTINCT MIN(start_time) FROM yb_workbatch_shiftset WHERE model = 5")
    String getStartWorkTime();

    Integer getFirstModel();

    WorkbatchShiftset getNowWsTime(Integer maId);

    WorkbatchShiftset getNowWsDate(@Param("maId") Integer maId);

    WorkbatchShiftset getByWsIdAndMaId(@Param("wsId") Integer wsId, @Param("maId") Integer maId);

    /**
     * 根据设备id获取班次开始时间
     *
     * @param maId
     * @return
     */
    String getShiftStartTime(Integer maId);

    WorkbatchShiftset getShiftsetBymaId(Integer maId);

    /***
     * 获取当前是哪个班次ID信息，
     * @param maId
     * @return
     */
    WorkbatchMachShiftVO getNowWsid(Integer maId);

    List<WorkbatchMainshift> getShiftWsid();

    /**
     * 根据设备id和班次id查询班次详情
     * @param wsId
     * @param maId
     * @return
     */
    WorkbatchShiftset getWorkbatchShiftset(@Param("wsId") Integer wsId, @Param("maId")Integer maId);
}
