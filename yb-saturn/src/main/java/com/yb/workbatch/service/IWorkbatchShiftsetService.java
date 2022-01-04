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
package com.yb.workbatch.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.system.user.entity.SaUser;
import com.yb.workbatch.entity.WorkbatchMainshift;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.request.WorkbatchShiftsetPageRequest;
import com.yb.workbatch.request.WorkbatchShiftsetSaveAndUpdateRequest;
import com.yb.workbatch.vo.*;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

/**
 * 排产班次设定_yb_workbatch_shifts（班次） 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IWorkbatchShiftsetService extends IService<WorkbatchShiftset> {


    /**
     * 自定义分页
     *
     * @param page
     * @param workbatchShiftset
     * @return
     */
    IPage<WorkbatchShiftsetVO> selectWorkbatchShiftsetPage(IPage<WorkbatchShiftsetVO> page, WorkbatchShiftsetVO workbatchShiftset);


    /**
     * 通过1公司2车间部门3工序4设备 、部门id、工序id、设备id 去对班次进行查询
     */
    List<WorkbatchShiftsetVO> getWorkbatchByCondition(Integer model, Integer bdId);

    /**
     * 查询班次
     *
     * @return
     */
    List<WorkbatchShiftset> getWorkbatchShift();

    List<WorkbatchShiftsetVO> getWorkbatchShiftByUserIdAndCheckDate(String dateStr, Integer userId);

    /**
     * 通过1公司2车间部门3工序4设备 、部门id、工序id、设备id 去对班次进行查询
     */
    List<WorkbatchShiftsetVO> getWorkbatchShiftByStartAndEndDate(Integer model, Integer dbId);

    /**
     * 查询今天的班次相关信息
     */
    List<WorkbatchShiftsetVO> getToDayList();

    /**
     * 临时调休的查询
     *
     * @param dateStr
     * @return
     */
    List<WorkbatchShiftsetVO> getTempWorkbatchShiftByUserIdAndCheckDate(String dateStr, Integer userid);

    /**
     * 查询当前时间所在班次,如果只属于一个班次返回这个班次,否则返回全部班次
     *
     * @return
     */
    List<WorkbatchMachShiftVO> getWorkbatchShiftsetList(List<WorkbatchMachShiftVO> workbatchShiftsetList);

    WorkbatchShiftset getShiftsetByNow();

    List<WorkbatchShiftset> getWorkbatchShiftsetByModel(Integer model);

    /***
     * 班次修改后的方法，通过设备id和班次id获取班次对应信息
     * @param wsId
     * @param maId
     * @return
     */
    WorkbatchShiftset selectByMaid(Integer wsId, Integer maId);

    WorkbatchShiftset getDefaultWsid();

    List<WorkbatchShiftset> getMainShift();


    void saveAndUpdate(WorkbatchShiftsetSaveAndUpdateRequest request, SaUser user);

    IPage<WorkbatchShiftsetPageVO> page(IPage<WorkbatchShiftsetPageVO> page, WorkbatchShiftsetPageRequest request);

    void delete(List<Integer> ids);

    Map shiftTypeList();

    String getShiftStartTime(Integer maId);

    /****
     * 根据设备判断当前是哪个班次id信息
     * @param maId
     * @return
     */
    WorkbatchMachShiftVO getNowWsid(Integer maId);

    List<WorkbatchMainshift> getShiftWsid();
}

