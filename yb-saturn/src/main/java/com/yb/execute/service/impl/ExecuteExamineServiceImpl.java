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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.base.entity.BasePicture;
import com.yb.base.service.BasePictureService;
import com.yb.common.DateUtil;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteExamine;
import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.mapper.ExecuteExamineMapper;
import com.yb.execute.request.ExecuteBrieferinfoRequest;
import com.yb.execute.request.ExecuteExamineRequest;
import com.yb.execute.service.ExecuteTraycardService;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.execute.service.IExecuteExamineService;
import com.yb.execute.vo.ExamineParamVO;
import com.yb.execute.vo.ExecuteExamineVO;
import com.yb.execute.vo.TakeStockVO;
import com.yb.stroe.entity.StoreInlog;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.entity.StoreOutlog;
import com.yb.stroe.entity.StoreSeat;
import com.yb.stroe.service.IStoreInlogService;
import com.yb.stroe.service.IStoreInventoryService;
import com.yb.stroe.service.IStoreOutlogService;
import com.yb.stroe.service.StoreSeatService;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.service.WorkbatchShiftService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 上报审核表_yb_execute_examine 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ExecuteExamineServiceImpl extends ServiceImpl<ExecuteExamineMapper, ExecuteExamine> implements IExecuteExamineService {
    @Autowired
    ExecuteExamineMapper executeExamineMapper;
    @Autowired
    WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    StoreSeatService storeSeatService;
    @Autowired
    IStoreInventoryService storeInventoryService;
    @Autowired
    ExecuteTraycardService traycardService;
    @Autowired
    WorkbatchShiftService workbatchShiftService;
    @Autowired
    IExecuteBrieferService executeBrieferService;
    @Autowired
    ExecuteTraycardService executeTraycardService;
    @Autowired
    BasePictureService basePictureService;


    @Override
    public IPage<ExecuteExamineVO> selectExecuteExaminePage(IPage<ExecuteExamineVO> page, ExecuteExamineVO executeExamine) {
        return page.setRecords(baseMapper.selectExecuteExaminePage(page, executeExamine));
    }

    @Override
    public IPage<ExecuteExamineVO> pageFindList(IPage<ExecuteExamineVO> page, ExamineParamVO examineParamVO) {
        String startTime = examineParamVO.getStartTime();
        if(StringUtil.isEmpty(startTime)){
            examineParamVO.setStartTime(DateUtil.refNowDay());
        }
        List<ExecuteExamineVO> listExecuteExamineVO = executeExamineMapper.pageFindExamineList(page, examineParamVO);
        return page.setRecords(listExecuteExamineVO);
    }

    @Override
    public IPage<ExecuteExamineVO> pageQueryList(IPage<ExecuteExamineVO> iPage, ExecuteBrieferinfoRequest request) {
        String startTime = request.getStartTime();
        ExamineParamVO examineParamVO=new ExamineParamVO();
        examineParamVO.setStartTime(request.getStartTime());
        examineParamVO.setEndTime(request.getEndTime());
        examineParamVO.setMaIdList(request.getMaIdList());
        examineParamVO.setPdName(request.getPdName());
        examineParamVO.setPrName(request.getPrName());
        examineParamVO.setWbNo(request.getWbNo());
        if(StringUtil.isEmpty(startTime)){
            examineParamVO.setStartTime(DateUtil.refNowDay());
        }
        List<ExecuteExamineVO> listExecuteExamineVO = executeExamineMapper.pageFindExamineList(iPage, examineParamVO);
        return iPage.setRecords(listExecuteExamineVO);
    }

    @Override
    public List<ExecuteExamineVO> getOutOfWorkRecord(String datesStr, Integer userId) {
        return executeExamineMapper.getOutOfWorkRecord(datesStr, userId);
    }

    @Override
    public ExecuteExamineVO selectExecuteExamine(Integer id) {
        return executeExamineMapper.selectExecuteExamine(id);
    }

    @Override
    public IPage<TakeStockVO> getPhoneDetail(IPage<TakeStockVO> page, String wbNo, Integer maId, Integer prId, Integer exStatus, Integer storeAreaId) {
        LambdaQueryWrapper<StoreSeat> storeSeatLambdaQueryWrapper = Wrappers.<StoreSeat>lambdaQuery();
        if (null != storeAreaId) {
            storeSeatLambdaQueryWrapper.eq(StoreSeat::getSrId, storeAreaId);
        }
        List<StoreSeat> seats = storeSeatService.list(storeSeatLambdaQueryWrapper);
        List<Integer> seatIds = seats.stream().map(StoreSeat::getId).collect(Collectors.toList());
        if (seatIds.size() == 0) {
            return null;
        }
        List<StoreInventory> storeInventories = storeInventoryService.list(Wrappers.<StoreInventory>lambdaQuery().in(StoreInventory::getStId, seatIds));
        List<Integer> etIds = storeInventories.stream().map(StoreInventory::getEtId).collect(Collectors.toList());
        if (etIds.size() == 0) {
            return null;
        }

        LambdaQueryWrapper<ExecuteTraycard> trayWrappers = Wrappers.<ExecuteTraycard>lambdaQuery();
        if (null != exStatus && 2 == exStatus) {
            // 无需审核
            trayWrappers.eq(ExecuteTraycard::getTyStatus, 2);
        }

        List<ExecuteTraycard> traycards = traycardService.list(trayWrappers.in(ExecuteTraycard::getId, etIds));
        List<Integer> wfIds = traycards.stream().map(ExecuteTraycard::getWfId).collect(Collectors.toList());
        if (wfIds.size() == 0) {
            return null;
        }

        List<TakeStockVO> takeStockList = workbatchShiftService.selectByIds(page, wfIds, wbNo, maId, prId, exStatus);
        for (TakeStockVO takeStockVO : takeStockList) {
            List<ExecuteTraycard> traycardList = traycards.stream().filter(t -> t.getExId().equals(takeStockVO.getExId())).collect(Collectors.toList());
            int trayNumber = traycardList.size();
            int trayProdNumber = traycardList.stream().mapToInt(ExecuteTraycard::getTrayNum).sum();
            takeStockVO.setTrayNumber(trayNumber);
            takeStockVO.setTrayProdNumber(trayProdNumber);
        }
        page.setRecords(takeStockList);
        return page;
    }

    @Override
    public IPage<TakeStockVO> getPhoneDetailNocheck(IPage<TakeStockVO> page, String wbNo, Integer maId, Integer prId, Integer exStatus, Integer storeAreaId) {
        LambdaQueryWrapper<StoreSeat> storeSeatLambdaQueryWrapper = Wrappers.<StoreSeat>lambdaQuery();
        if (null != storeAreaId) {
            storeSeatLambdaQueryWrapper.eq(StoreSeat::getSrId, storeAreaId);
        }
        List<StoreSeat> seats = storeSeatService.list(storeSeatLambdaQueryWrapper);
        List<Integer> seatIds = seats.stream().map(StoreSeat::getId).collect(Collectors.toList());
        if (seatIds.size() == 0) {
            return null;
        }

//        List<StoreInventory> storeInventories = storeInventoryService.list(Wrappers.<StoreInventory>lambdaQuery().in(StoreInventory::getStId, seatIds));
//        List<Integer> etIds = storeInventories.stream().map(StoreInventory::getEtId).collect(Collectors.toList());
//        if (etIds.size() == 0) {
//            return null;
//        }

        LambdaQueryWrapper<ExecuteTraycard> trayWrappers = Wrappers.<ExecuteTraycard>lambdaQuery();
        if (null != exStatus && 2 == exStatus) {
            // 无需审核
            trayWrappers.eq(ExecuteTraycard::getTyStatus, 2);
        }

        List<ExecuteTraycard> traycards = traycardService.list(trayWrappers.in(ExecuteTraycard::getMpId, seatIds).eq(ExecuteTraycard::getTyStatus, 2));
        List<Integer> wfIds = traycards.stream().map(ExecuteTraycard::getWfId).collect(Collectors.toList());
        if (wfIds.size() == 0) {
            return null;
        }

        List<TakeStockVO> resultList = new ArrayList<>();
        List<TakeStockVO> takeStockList = workbatchShiftService.selectByIds(page, wfIds, wbNo, maId, prId, exStatus);
        for (TakeStockVO takeStockVO : takeStockList) {
            List<ExecuteTraycard> traycardList = traycards.stream().filter(t -> t.getExId().equals(takeStockVO.getExId())).collect(Collectors.toList());
            int trayNumber = traycardList.size();
            int trayProdNumber = traycardList.stream().mapToInt(ExecuteTraycard::getTrayNum).sum();
            takeStockVO.setTrayNumber(trayNumber);
            takeStockVO.setTrayProdNumber(trayProdNumber);
            Integer exId = takeStockVO.getExId();
            List<ExecuteTraycard> list = traycardService.list(
                    Wrappers.<ExecuteTraycard>lambdaQuery()
                            .eq(ExecuteTraycard::getExId, exId)
                            .eq(ExecuteTraycard::getTyStatus, 2)
                            .select(ExecuteTraycard::getId)
            );
            List<ExecuteTraycard> list1 = traycardService.list(
                    Wrappers.<ExecuteTraycard>lambdaQuery()
                            .eq(ExecuteTraycard::getExId, exId)
                            .select(ExecuteTraycard::getId)
            );
            if (list.size() > 0 && list1.size() > 0 && list.size() == list1.size()) {
                resultList.add(takeStockVO);
            }
        }
        page.setRecords(resultList);
        return page;
    }

    @Override
    public void phoneUpdateEtIdTdNo(Integer bfId, Integer tyId, String exPics, String originalStNo, String modifyStNo,
                                    Integer originalNumber, Integer modifyNumber, Integer rptUserid, String exMold, Integer exUserid) {
        String s = phoneUpdateEtIdTdNoNotTray(bfId, rptUserid, exUserid, modifyNumber, modifyStNo, exMold);
        if (null != s) {
            return;
        }
        ExecuteBriefer executeBriefer = executeBrieferService.getOne(
                Wrappers.<ExecuteBriefer>lambdaQuery()
                        .select(ExecuteBriefer::getId)
                        .select(ExecuteBriefer::getExId)
                        .eq(ExecuteBriefer::getId, bfId)
        );
        ExecuteExamine executeExamine = new ExecuteExamine();
        executeExamine.setBfId(bfId);
        executeExamine.setRptUserid(executeBriefer.getHandleUsid());
        executeExamine.setRptTime(new Date());
        executeExamine.setDataBefore(originalNumber + "");
        executeExamine.setExWay(1);
        executeExamine.setStoreBefore(originalStNo);
        executeExamine.setExId(executeBriefer.getExId());
        executeExamine.setExMold(exMold);
        executeExamine.setExPics(exPics);
        executeExamine.setTyId(tyId);
        executeExamine.setCreateAt(new Date());
        executeExamine.setExStatus(1);
        executeExamine.setExUserid(exUserid);

        if (null == modifyNumber) {
            modifyNumber = originalNumber;
        }
        if (StringUtils.isBlank(modifyStNo)) {
            modifyStNo = originalStNo;
        }
        executeExamine.setStoreAfter(modifyStNo);
        executeExamine.setDataAfter(modifyNumber + "");
        executeExamineMapper.insert(executeExamine);

        StoreSeat seat = storeSeatService.getOne(Wrappers.<StoreSeat>lambdaQuery().eq(StoreSeat::getStNo, modifyStNo));
        ExecuteTraycard tray = traycardService.getById(tyId);
        tray.setTrayNum(modifyNumber);
        tray.setMpId(seat.getId());
        tray.setExStatus(1);
        tray.setStorePlace(seat.getStNo());
        traycardService.updateById(tray);

        StoreInventory storeInventory = storeInventoryService.getOne(Wrappers.<StoreInventory>lambdaQuery().eq(StoreInventory::getEtId, tyId));
        storeInventoryService.inOrOut(storeInventory, modifyNumber, modifyStNo, rptUserid);
        this.updateExstatus(bfId);
    }


    public String phoneUpdateEtIdTdNoNotTray(Integer bfId, Integer rptUserid, Integer exUserid, Integer modifyNumber, String modifyStNo, String exMold) {
        ExecuteBriefer briefer = executeBrieferService.getById(bfId);
        StoreInventory storeInventory = storeInventoryService.getOne(Wrappers.<StoreInventory>lambdaQuery().eq(StoreInventory::getDbId, bfId).isNull(StoreInventory::getEtId));
        if (null == storeInventory) {
            return null;
        }
        if (null == modifyNumber) {
            modifyNumber = storeInventory.getEtPdnum();
        }
        ExecuteExamine executeExamine = new ExecuteExamine();
        executeExamine.setExId(briefer.getExId());
        executeExamine.setExMold(exMold);
        executeExamine.setBfId(bfId);
        executeExamine.setExUserid(exUserid);
        executeExamine.setRptTime(briefer.getHandleTime());
        executeExamine.setDataBefore(storeInventory.getEtPdnum().toString());
        executeExamine.setDataAfter(modifyNumber.toString());
        executeExamine.setExStatus(1);
        executeExamine.setExWay(1);
        executeExamine.setRptUserid(briefer.getHandleUsid());
        this.save(executeExamine);
        storeInventoryService.inOrOut(storeInventory, modifyNumber, modifyStNo, rptUserid);
        this.updateExstatus(bfId);
        return "1";
    }

    /****
     *
     * @param request
     */
    @Override
    public void phoneUpdateEtIdTdNo(ExecuteExamineRequest request){
//        String s = phoneUpdateEtIdTdNoNotTray(bfId, rptUserid, exUserid, modifyNumber, modifyStNo);
//        if (null != s) {
//            return;
//        }
//        ExecuteBriefer executeBriefer = executeBrieferService.getOne(
//                Wrappers.<ExecuteBriefer>lambdaQuery()
//                        .select(ExecuteBriefer::getId)
//                        .select(ExecuteBriefer::getExId)
//                        .eq(ExecuteBriefer::getId, bfId)
//        );
//        ExecuteExamine executeExamine = new ExecuteExamine();
//        executeExamine.setBfId(bfId);
//        executeExamine.setRptUserid(executeBriefer.getHandleUsid());
//        executeExamine.setRptTime(new Date());
//        executeExamine.setDataBefore(originalNumber + "");
//        executeExamine.setExWay(1);
//        executeExamine.setStoreBefore(originalStNo);
//        executeExamine.setExId(executeBriefer.getExId());
//        executeExamine.setExMold(exMold);
//        executeExamine.setExPics(exPics);
//        executeExamine.setTyId(tyId);
//        executeExamine.setCreateAt(new Date());
//        executeExamine.setExStatus(1);
//        executeExamine.setExUserid(exUserid);
//
//        if (null == modifyNumber) {
//            modifyNumber = originalNumber;
//        }
//        if (StringUtils.isBlank(modifyStNo)) {
//            modifyStNo = originalStNo;
//        }
//        executeExamine.setStoreAfter(modifyStNo);
//        executeExamine.setDataAfter(modifyNumber + "");
//        executeExamineMapper.insert(executeExamine);
//
//        StoreSeat seat = storeSeatService.getOne(Wrappers.<StoreSeat>lambdaQuery().eq(StoreSeat::getStNo, modifyStNo));
//        ExecuteTraycard tray = traycardService.getById(tyId);
//        tray.setTrayNum(modifyNumber);
//        tray.setMpId(seat.getId());
//        tray.setExStatus(1);
//        tray.setStorePlace(seat.getStNo());
//        traycardService.updateById(tray);
//
//        StoreInventory storeInventory = storeInventoryService.getOne(Wrappers.<StoreInventory>lambdaQuery().eq(StoreInventory::getEtId, tyId));
//        storeInventoryService.inOrOut(storeInventory, modifyNumber, modifyStNo, rptUserid);
//        this.updateExstatus(bfId);
    }

    @Override
    public Map<String, Integer> getSumByBfId(Integer bfId, String exMold) {
        int dataBeforeSum = 0;
        int dataAfterSum = 0;
        List<ExecuteExamine> executeExamines = executeExamineMapper.selectList(
                Wrappers.<ExecuteExamine>lambdaQuery()
                        .eq(ExecuteExamine::getBfId, bfId)
                        .eq(ExecuteExamine::getExMold, exMold)
        );
        HashMap<String, Integer> map = new HashMap<>();
        if (0 == executeExamines.size()) {
            map.put("dataBeforeSum", dataBeforeSum);
            map.put("dataAfterSum", dataAfterSum);
            return map;
        }
        for (ExecuteExamine executeExamine : executeExamines) {
            dataBeforeSum += Integer.parseInt(executeExamine.getDataBefore() == null ? "0" : executeExamine.getDataBefore());
            dataAfterSum += Integer.parseInt(executeExamine.getDataAfter() == null ? "0" : executeExamine.getDataAfter());
        }
        map.put("dataBeforeSum", dataBeforeSum);
        map.put("dataAfterSum", dataAfterSum);
        return map;
    }

    @Override
    public List<ExecuteExamineVO> getModifyTrayByBfId(Integer bfId, String exMold) {
        return executeExamineMapper.getModifyTrayByBfId(bfId, exMold);
    }


    public void updateExstatus(Integer bfId) {
        ExecuteBriefer briefer = executeBrieferService.getById(bfId);
        List<ExecuteTraycard> executeTraycards = executeTraycardService.list(Wrappers.<ExecuteTraycard>lambdaQuery().eq(ExecuteTraycard::getExId, briefer.getExId()));
        List<ExecuteExamine> executeExamines = executeExamineMapper.selectList(Wrappers.<ExecuteExamine>lambdaQuery().eq(ExecuteExamine::getExId, briefer.getExId()));
        if (executeTraycards.size() == executeExamines.size()) {
            briefer.setExStatus(1);
            executeBrieferService.updateById(briefer);
        }
    }

    @Override
    public Map<String, Map<String, Integer>> getAllSumByBfId(Integer bfId) {
        Map<String, Integer> one = getSumByBfId(bfId, "1");
        Map<String, Integer> tow = getSumByBfId(bfId, "2");
        HashMap<String, Map<String, Integer>> result = new HashMap<>(8);
        result.put("exMoldOne", one);
        result.put("exMoldTow", tow);
        return result;
    }

    @Override
    public List<BasePicture> getImageByBfId(Integer bfId, String exMold) {
       String picIds = null;
        List<ExecuteExamineVO> list = getModifyTrayByBfId(bfId, exMold);
        for (ExecuteExamineVO examineVO : list) {
//            List<Integer> imageIds = Func.toIntList(examineVO.getExPics());
            String exPics = examineVO.getExPics();
            picIds = exPics.replace("|", ",");
        }
        if (StringUtil.isEmpty(picIds)) {
            return null;
        }
        return basePictureService.listByIds(Func.toIntList(picIds));
    }


}
