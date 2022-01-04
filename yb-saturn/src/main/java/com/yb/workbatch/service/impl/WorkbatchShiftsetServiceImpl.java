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
package com.yb.workbatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.common.DateUtil;
import com.yb.system.dept.mapper.SaDeptMapper;
import com.yb.system.dict.mapper.SaDictMapper;
import com.yb.system.user.entity.SaUser;
import com.yb.timer.DateTimeUtil;
import com.yb.workbatch.entity.WorkbatchMainshift;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import com.yb.workbatch.request.WorkbatchShiftsetPageRequest;
import com.yb.workbatch.request.WorkbatchShiftsetSaveAndUpdateRequest;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import com.yb.workbatch.vo.*;
import org.springblade.common.exception.CommonException;
import org.springblade.common.modelMapper.ModelMapperUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 排产班次设定_yb_workbatch_shifts（班次） 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class WorkbatchShiftsetServiceImpl extends ServiceImpl<WorkbatchShiftsetMapper, WorkbatchShiftset> implements IWorkbatchShiftsetService {


    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;
    @Autowired
    private SaDeptMapper saDeptMapper;
    @Autowired
    private SaDictMapper saDictMapper;

    @Override
    public IPage<WorkbatchShiftsetVO> selectWorkbatchShiftsetPage(IPage<WorkbatchShiftsetVO> page, WorkbatchShiftsetVO workbatchShiftset) {
        return page.setRecords(baseMapper.selectWorkbatchShiftsetPage(page, workbatchShiftset));
    }

    @Override
    public List<WorkbatchShiftsetVO> getWorkbatchByCondition(Integer model, Integer bdId) {
        return workbatchShiftsetMapper.getWorkbatchByCondition(model, bdId);
    }

    @Override
    public List<WorkbatchShiftset> getWorkbatchShift() {
        List<WorkbatchShiftset> workbatchShiftsets = workbatchShiftsetMapper.selectList(new QueryWrapper<>());
        return workbatchShiftsets;
    }


    @Override
    public List<WorkbatchShiftsetVO> getWorkbatchShiftByUserIdAndCheckDate(String dateStr, Integer userId) {
        return workbatchShiftsetMapper.getWorkbatchShiftByUserIdAndCheckDate(dateStr, userId);
    }

    public List<WorkbatchShiftsetVO> getWorkbatchShiftByStartAndEndDate(Integer model, Integer dbId) {
        List<WorkbatchShiftsetVO> ListDate = workbatchShiftsetMapper.getStartAndEndDate(model, dbId);
        if (!Func.isEmpty(ListDate)) {
            for (WorkbatchShiftsetVO workbatchshiftsetvo : ListDate) {
                List<WorkbatchShiftsetVO> list = workbatchShiftsetMapper.getWorkbatchBydate(workbatchshiftsetvo);
                if (!Func.isEmpty(list)) {
                    workbatchshiftsetvo.setWorkbatchShiftsetVOList(list);
                }
            }
        }
        return ListDate;
    }

    @Override
    public List<WorkbatchShiftsetVO> getToDayList() {
        return workbatchShiftsetMapper.getToDayList();
    }

    @Override
    public List<WorkbatchShiftsetVO> getTempWorkbatchShiftByUserIdAndCheckDate(String dateStr, Integer userId) {
        return workbatchShiftsetMapper.getTempWorkbatchShiftByUserIdAndCheckDate(dateStr, userId);
    }

    @Override
    public List<WorkbatchMachShiftVO> getWorkbatchShiftsetList(List<WorkbatchMachShiftVO> workbatchShiftsetList) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String dateFormat = simpleDateFormat.format(date);
        Date dateParse = null;
        try {
            dateParse = simpleDateFormat.parse(dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("时间格式错误");
        }

        List<WorkbatchMachShiftVO> workbatchShiftsets = new ArrayList<>();
        Iterator<WorkbatchMachShiftVO> it = workbatchShiftsetList.iterator();
        while (it.hasNext()) {
            WorkbatchMachShiftVO workbatchShiftset = it.next();
            Date startTime = workbatchShiftset.getStartTime();
            Date endTime = workbatchShiftset.getEndTime();
            if (endTime.before(startTime)) {//如果结束时间小于开始时间,说明跨天
                if (startTime.before(dateParse) || dateParse.before(endTime)) {
                    workbatchShiftsets.add(workbatchShiftset);
                }
            } else {
                if (startTime.before(dateParse) && dateParse.before(endTime)) {//如果没有跨天,班次开始时间小于当前时间并且班次结束时间大于当前时间
                    workbatchShiftsets.add(workbatchShiftset);
                }
            }
        }
        if (workbatchShiftsets.size() == 1) {//如果当前时间只属于一个班次则只返回这个班次
            return workbatchShiftsets;
        }
        return workbatchShiftsetList;//如果当前时间不止一个班次,则查询全部班次返回
    }

    /*****
     * 查看当前时间是否符合当天的班次信息 数据修改字段，该方法已经不适用
     * @return
     * @deprecated
     */
    public WorkbatchShiftset getShiftsetByNow() {
        //返回当天的设定数据局
        WorkbatchShiftset today = workbatchShiftsetMapper.getListBytoday();
        WorkbatchShiftset tomorrow = workbatchShiftsetMapper.getListBytomorrow();
        //
        if (today != null && today.getId() != null)
            return today;
        else if (tomorrow != null && tomorrow.getId() != null)
            return tomorrow;
        else
            return null;
    }

    @Override
    public List<WorkbatchShiftset> getWorkbatchShiftsetByModel(Integer model) {
        return workbatchShiftsetMapper.getWorkbatchShiftsetByModel(model);
    }

    /*******
     * 设定选择的班次id和设备id对应信息内容
     * @param wsId
     * @param maId
     * @return
     */
    @Override
    public WorkbatchShiftset selectByMaid(Integer wsId, Integer maId) {
        return workbatchShiftsetMapper.selectByMaid(wsId, maId);
    }

    @Override
    public WorkbatchShiftset getDefaultWsid() {
        return workbatchShiftsetMapper.getDefaultWsid();
    }

    @Override
    public List<WorkbatchShiftset> getMainShift() {
        return workbatchShiftsetMapper.getMainShift();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAndUpdate(WorkbatchShiftsetSaveAndUpdateRequest request, SaUser user) {

        if (request.getStartDate().getTime() > request.getEndDate().getTime()) {
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "开始日期不可大于结束日期");
        }
        if (DateUtil.addDayForDate(new Date(), 1).getTime() >= request.getEndDate().getTime()) {
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "临近结束时间（1天内）");
        }
        //id不为空为修改
        if (request.getId() != null) {
            WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectById(request.getId());
            if (workbatchShiftset == null) {
                throw new CommonException(HttpStatus.NOT_FOUND.value(), "未找到班次信息");
            }
            ModelMapperUtil.getStrictModelMapper().map(request, workbatchShiftset);
            workbatchShiftset.setUpdateAt(new Date());
            workbatchShiftsetMapper.updateById(workbatchShiftset);
            return;
        }
        //新增
        WorkbatchShiftset workbatchShiftset = ModelMapperUtil.getStrictModelMapper().map(request, WorkbatchShiftset.class);
        workbatchShiftset.setCreateAt(new Date());
        if (user != null) {
            workbatchShiftset.setUsId(user.getUserId());
        }
        Date startTime = workbatchShiftset.getStartTime();
        Date endTime = workbatchShiftset.getEndTime();
        if (endTime.getTime() < startTime.getTime()) {
            Date date = DateTimeUtil.addDate(endTime, 1);
            Long time = (date.getTime() - startTime.getTime()) / 1000 / 60;
            workbatchShiftset.setStayTime(time.intValue());
        }else{
            Long time = (endTime.getTime() - startTime.getTime()) / 1000 / 60;
            workbatchShiftset.setStayTime(time.intValue());
        }
        workbatchShiftsetMapper.insert(workbatchShiftset);
    }

    @Override
    public IPage<WorkbatchShiftsetPageVO> page(IPage<WorkbatchShiftsetPageVO> page, WorkbatchShiftsetPageRequest request) {
        List<WorkbatchShiftsetPageVO> vos = workbatchShiftsetMapper.page(page, request);
        if (vos.isEmpty()) {
            return page.setRecords(new ArrayList<>());
        }
        return page.setRecords(vos);
    }

    @Override
    public void delete(List<Integer> ids) {
        List<WorkbatchShiftset> workbatchShiftsets = workbatchShiftsetMapper.selectBatchIds(ids);
        if (!workbatchShiftsets.isEmpty()) {
            workbatchShiftsetMapper.deleteBatchIds(ids);
        }
    }

    @Override
    public Map shiftTypeList() {
        List<WorkbatchShiftset> workbatchShiftsets = workbatchShiftsetMapper.selectList(null);

        List<ShiftTypeListVO> deptList = saDeptMapper.findByClassify(2);
        List<ShiftTypeListVO> companyDeptList = saDeptMapper.findByClassify(1);
        List<ShiftTypeListVO> list = saDictMapper.findList();
        Map map = new HashMap<String, Object>();
        if (workbatchShiftsets.isEmpty()) {
            map.put("dept", deptList);
            map.put("maType", list);
            map.put("company", companyDeptList);
        } else {
            Integer model = workbatchShiftsetMapper.getFirstModel();
            if (model == 1) {
                map.put("company", companyDeptList);
            } else if (model == 2) {
                map.put("dept", deptList);
            } else {
                map.put("maType", list);
            }
        }
        return map;
    }


    public String getShiftStartTime(Integer maId) {
        return workbatchShiftsetMapper.getShiftStartTime(maId);
    }

    public WorkbatchMachShiftVO getNowWsid(Integer maId){
        return workbatchShiftsetMapper.getNowWsid(maId);
    }

    public List<WorkbatchMainshift> getShiftWsid(){
        return workbatchShiftsetMapper.getShiftWsid();
    }
}
