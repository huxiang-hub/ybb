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
package com.yb.execute.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.common.DateUtil;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.entity.ExecuteWaste;
import com.yb.execute.mapper.ExecuteStateMapper;
import com.yb.execute.mapper.ExecuteWasteMapper;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.execute.service.IExecuteWasteService;
import com.yb.execute.vo.ExecuteWastReportVO;
import com.yb.execute.vo.ExecuteWasteNumberVO;
import com.yb.execute.vo.ExecuteWasteVO;
import com.yb.execute.vo.WastTypeSum;
import com.yb.panelapi.order.entity.QualityWasteReportDto;
import com.yb.panelapi.user.utils.R;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.service.IProcessWorkinfoService;
import com.yb.prod.entity.ProdProcelink;
import com.yb.prod.service.IProdProcelinkService;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import com.yb.workbatch.service.WorkbatchShiftService;
import com.yb.workbatch.vo.WorkbatchOrdlinkShiftVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 质量检查废品表_yb_execute_waste 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ExecuteWasteServiceImpl extends ServiceImpl<ExecuteWasteMapper, ExecuteWaste> implements IExecuteWasteService {

    @Autowired
    private ExecuteWasteMapper wasteMapper;
    @Autowired
    private ExecuteStateMapper stateMapper;
    @Autowired
    private IProcessWorkinfoService processWorkinfoService;
    @Autowired
    private IWorkbatchOrdlinkService ordlinkService;
    @Autowired
    private WorkbatchShiftService shiftService;
    @Autowired
    private IProdProcelinkService prodProcelinkService;
    @Autowired
    private IExecuteBrieferService brieferService;

    @Override
    public IPage<ExecuteWasteVO> selectExecuteWastePage(IPage<ExecuteWasteVO> page, ExecuteWasteVO executeWaste) {
        return page.setRecords(baseMapper.selectExecuteWastePage(page, executeWaste));
    }

    /**
     * 质量检测上报
     * @param wasteReportDto
     * @return
     */
    @Override
    public R wasteReport(QualityWasteReportDto wasteReportDto) {
        ExecuteWaste executeWaste = wasteReportDto.getExecuteWaste();
        ExecuteWaste executeWaste1 = wasteMapper.selectById(executeWaste.getId());
        executeWaste1.setWaste(executeWaste.getWaste());
        executeWaste1.setReason(executeWaste.getReason());
        //更新 质检上报表信息
        wasteMapper.updateById(executeWaste1);
        /**
         * 执行状态表信息更新
         */
        Date curTime = new Date();
        ExecuteState executeState = wasteReportDto.getExecuteState();
        ExecuteState executeState1 = stateMapper.selectById(executeState.getId());
        executeState1.setEndAt(curTime);
        //持续时长
        executeState1.setDuration(DateUtil.calLastedTime(curTime.getTime(),executeState1.getStartAt()));
        stateMapper.updateById(executeState1);
        return R.ok();
    }

    @Override
    public List<ExecuteWasteVO> getWateByTime(Date startTime, Date endTime) {
        return wasteMapper.getWateByTime(startTime,endTime);
    }


    @Override
    public void submitWaste(ExecuteWastReportVO reportVO) {
        ArrayList<ExecuteWaste> executeWastes = new ArrayList<>();
        ExecuteBriefer briefer = brieferService.getById(reportVO.getBfId());
        if (null == briefer) {
            throw new RuntimeException("未查询到执行单");
        }
        for (WastTypeSum wastTypeSum : reportVO.getWastTypeSumList()) {
            ExecuteWaste executeWaste = new ExecuteWaste();
            executeWaste.setExId(briefer.getExId());
            executeWaste.setExPrid(reportVO.getExPrid());
            executeWaste.setPrId(wastTypeSum.getPrId());
            executeWaste.setMfId(wastTypeSum.getWastClassId());
            executeWaste.setUsId(reportVO.getUsId());
            executeWaste.setWsType(reportVO.getWsType());
            executeWaste.setWaste(wastTypeSum.getWaste());
            executeWaste.setCreateAt(new Date());
            executeWastes.add(executeWaste);
        }
        this.saveBatch(executeWastes);
    }

    @Override
    public List<ExecuteWasteNumberVO> lastTwoWaste(Integer wfId) {
        Integer lastOnePrId;
        Integer lastTowPrId;
        ArrayList<ExecuteWasteNumberVO> result = new ArrayList<>();
        WorkbatchShift workbatchShift = shiftService.getById(wfId);
        WorkbatchOrdlinkShiftVO workbatchOrdlinkShiftVO = ordlinkService.getById(workbatchShift.getSdId());
        Integer currentPrSort = workbatchOrdlinkShiftVO.getSort();
        List<ProdProcelink> list = prodProcelinkService.list(
                Wrappers.<ProdProcelink>lambdaQuery()
                        .eq(ProdProcelink::getPdId, workbatchOrdlinkShiftVO.getPdId())
                        .lt(ProdProcelink::getSortNum, currentPrSort)
                        .orderByDesc(ProdProcelink::getSortNum));
        if (list.size() == 0) {
            return null;
        }
        if (list.size() == 1) {
            lastOnePrId = list.get(1).getPrId();
            ProcessWorkinfo processWorkinfoOne = processWorkinfoService.getById(lastOnePrId);
            List<ExecuteWaste> executeWastes = wasteMapper.selectList(Wrappers.<ExecuteWaste>lambdaQuery().eq(ExecuteWaste::getExPrid, lastOnePrId));
            ExecuteWasteNumberVO wasteNumberOne = new ExecuteWasteNumberVO();
            wasteNumberOne.setPrName(processWorkinfoOne.getPrName());
            wasteNumberOne.setPrId(processWorkinfoOne.getId());
            wasteNumberOne.setWasteList(executeWastes);
            wasteNumberOne.setPrSort(list.get(1).getSortNum());
            result.add(wasteNumberOne);
        }
        if (list.size() == 2) {
            lastTowPrId = list.get(2).getPrId();
            ProcessWorkinfo processWorkinfoTow = processWorkinfoService.getById(lastTowPrId);
            List<ExecuteWaste> executeWastes = wasteMapper.selectList(Wrappers.<ExecuteWaste>lambdaQuery().eq(ExecuteWaste::getExPrid, lastTowPrId));
            ExecuteWasteNumberVO wasteNumberTow = new ExecuteWasteNumberVO();
            wasteNumberTow.setPrName(processWorkinfoTow.getPrName());
            wasteNumberTow.setPrId(processWorkinfoTow.getId());
            wasteNumberTow.setWasteList(executeWastes);
            wasteNumberTow.setPrSort(list.get(2).getSortNum());
            result.add(wasteNumberTow);
        }
        return result;
    }

}
