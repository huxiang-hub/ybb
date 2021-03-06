package com.yb.workbatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchProgress;
import com.yb.workbatch.mapper.WorkbatchProgressMapper;
import com.yb.workbatch.service.WorkbatchProgressService;
import com.yb.workbatch.vo.WorkbatchProgressCmVO;
import com.yb.workbatch.vo.WorkbatchProgressPtVO;
import com.yb.workbatch.vo.WorkbatchProgressVO;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class WorkbatchProgressServiceImpl extends ServiceImpl<WorkbatchProgressMapper, WorkbatchProgress> implements WorkbatchProgressService {
    @Autowired
    private WorkbatchProgressMapper workbatchProgressMapper;

    @Override
    public IPage selectWorkbatchProgressPage(IPage<String> page, WorkbatchProgressVO workbatchProgressVO) {

        List<String> wbNoList = workbatchProgressMapper.selectGroupWbNo(page, workbatchProgressVO);
        List<WorkbatchProgressVO> workbatchProgressVOList = null;
        if(!wbNoList.isEmpty()){
            workbatchProgressVOList = workbatchProgressMapper.workbatchProgressPage(wbNoList);
        }
        Map<String, Map<String, List<WorkbatchProgress>>> wbNoMap = new TreeMap<>();
        Map<String, List<WorkbatchProgress>> ptNameMap = new TreeMap<>();
//        List<WorkbatchProgress> workbatchProgressList = workbatchProgressMapper.selectWorkbatchProgressPage(page, workbatchProgress);
        if (!workbatchProgressVOList.isEmpty()) {
            for (WorkbatchProgressVO progressVO : workbatchProgressVOList) {
                String wbNo = progressVO.getWbNo();
                if (wbNoMap.containsKey(progressVO.getWbNo())) {//????????????,????????????
                    Map<String, List<WorkbatchProgress>> stringListMap = wbNoMap.get(wbNo);
                    stringListMap.putAll(getPtNameMap(ptNameMap, progressVO));
                } else {
                    ptNameMap = new TreeMap<>();
                    ptNameMap.putAll(getPtNameMap(ptNameMap, progressVO));
                    wbNoMap.put(wbNo, ptNameMap);
                }
            }
        }
        List<WorkbatchProgressCmVO> workbatchProgressCmVOList = new ArrayList<>();
        WorkbatchProgressCmVO workbatchProgressCmVO;
        List<WorkbatchProgressPtVO> workbatchProgressPtVOList;
        WorkbatchProgressPtVO workbatchProgressPtVO;
        for(String wbNoMapKey : wbNoMap.keySet()){
            Map<String, List<WorkbatchProgress>> stringListMap = wbNoMap.get(wbNoMapKey);
            workbatchProgressCmVO = new WorkbatchProgressCmVO();
            workbatchProgressPtVOList = new ArrayList<>();
            for(String wbNoKey : stringListMap.keySet()){
                workbatchProgressPtVO = new WorkbatchProgressPtVO();
                List<WorkbatchProgress> workbatchProgressVOS = stringListMap.get(wbNoKey);
                if(!workbatchProgressVOS.isEmpty()){
                    WorkbatchProgress progress = workbatchProgressVOS.get(0);
                    workbatchProgressCmVO.setCmName(progress.getCmName());
                    workbatchProgressCmVO.setPdId(progress.getPdId());
                    workbatchProgressCmVO.setPdName(progress.getPdName());
                    workbatchProgressCmVO.setWbId(progress.getWbId());
                    workbatchProgressCmVO.setWbNo(progress.getWbNo());
                    /*--------------------------------------------*/
                    workbatchProgressPtVO.setPtId(progress.getPtId());
                    workbatchProgressPtVO.setPtName(progress.getPtName());
                    workbatchProgressPtVO.setChildren(workbatchProgressVOS);
                    workbatchProgressPtVOList.add(workbatchProgressPtVO);
                }
            }
            workbatchProgressCmVO.setChildren(workbatchProgressPtVOList);
            workbatchProgressCmVOList.add(workbatchProgressCmVO);
        }
        IPage ipage = new Page();
        ipage.setRecords(workbatchProgressCmVOList);
        return ipage;
    }


    private Map<String, List<WorkbatchProgress>> getPtNameMap(Map<String, List<WorkbatchProgress>> ptNameMap, WorkbatchProgress workbatchProgress) {
        String ptName = workbatchProgress.getPtName();
        if (ptNameMap.containsKey(ptName)) {//????????????,????????????
            List<WorkbatchProgress> workbatchProgressS = ptNameMap.get(ptName);
            workbatchProgressS.add(workbatchProgress);
        } else {
            List<WorkbatchProgress> workbatchProgressS = new ArrayList<>();
            workbatchProgressS.add(workbatchProgress);
            ptNameMap.put(ptName, workbatchProgressS);
        }
        return ptNameMap;
    }


    @Override
    public IPage workbatchProgressPage(IPage<String> page, WorkbatchProgressVO workbatchProgressVO) {

        List<String> wbNoList = workbatchProgressMapper.selectGroupWbNo(page, workbatchProgressVO);
        List<WorkbatchProgressVO> workbatchProgressVOList = null;
        if(!wbNoList.isEmpty()){
            workbatchProgressVOList = workbatchProgressMapper.workbatchProgressPage(wbNoList);
            Date date = new Date();
            if(!workbatchProgressVOList.isEmpty()){
                for(WorkbatchProgressVO batchProgressVO : workbatchProgressVOList){
                    Date limitTime = batchProgressVO.getLimitTime();
//                    Integer stayTime = batchProgressVO.getStayTime();//(??????)
                    Double differenceTime = 0.0;
                if(limitTime != null){
                    differenceTime = (limitTime.getTime() - date.getTime()) / (double) 1000 / 3600;//??????
                    BigDecimal bg = new BigDecimal(differenceTime);
                    differenceTime = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                    batchProgressVO.setDifferenceTime(differenceTime);
                }
            }
        }

        page.setRecords(wbNoList);
        IPage iPage = new Page();
        iPage.setRecords(workbatchProgressVOList);
        iPage.setTotal(page.getTotal());
        iPage.setPages(page.getPages());
        iPage.setCurrent(page.getCurrent());
        iPage.setSize(page.getSize());
        return iPage;
    }

    /**
     * ?????????????????????
     */
    public void addWorkbatchProgress(WorkbatchOrdlink workbatchOrdlink){
        Date date = new Date();
        Integer status = 0;
        Integer totalTime = null;//???????????????
        Integer stayTime = null;//??????????????????
        Integer planCount;//????????????
        Date finishTime = null;

        String wbNo = workbatchOrdlink.getWbNo();
        Integer wbId = workbatchOrdlink.getWbId();
        Integer prId = workbatchOrdlink.getPrId();
        String prName = workbatchOrdlink.getPrName();
        Integer ptId = workbatchOrdlink.getPtId();
        String cmName = workbatchOrdlink.getCmName();
        String ptName = workbatchOrdlink.getPartName();
        String pdName = workbatchOrdlink.getPdName();
        Integer sdId = workbatchOrdlink.getId();
        Integer planNum = workbatchOrdlink.getPlanNum();//????????????
        Integer completeNum = workbatchOrdlink.getCompleteNum();//????????????
        Date closeTime = workbatchOrdlink.getCloseTime();//????????????
        Integer maId = workbatchOrdlink.getMaId();
        if(!StringUtil.isEmpty(wbNo)){
            /*??????????????????????????????*/
            List<WorkbatchProgress> workbatchProgressList =
                    workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>().eq("wb_no", wbNo).eq("wp_type", 1));
            if(workbatchProgressList.isEmpty()){//??????????????????,??????????????????
                WorkbatchProgress workbatchProgress = new WorkbatchProgress();
                workbatchProgress.setCmName(cmName);//????????????
                workbatchProgress.setCreateAt(date);//????????????
                workbatchProgress.setFinishTime(finishTime);//??????????????????
                workbatchProgress.setLimitTime(closeTime);//????????????(????????????)
                workbatchProgress.setPdId(null);//??????id
                workbatchProgress.setPdName(pdName);//????????????
                workbatchProgress.setPlanCount(planNum);//????????????
                workbatchProgress.setRealCount(completeNum);//???????????????
                workbatchProgress.setSdId(sdId);//??????id
                workbatchProgress.setStatus(status);//?????????0?????????1?????????2?????????3????????????4????????????5??????6??????
                workbatchProgress.setTotalTime(totalTime);//???????????????(??????)
                workbatchProgress.setUpdateAt(date);//????????????
                workbatchProgress.setWarning(1);//????????????1???36??????2???24??????
                workbatchProgress.setWbId(wbId);//??????id
                workbatchProgress.setWbNo(wbNo);//????????????
                workbatchProgress.setWpType(1);//????????????1?????????????????????2???????????????3???????????????
                workbatchProgressMapper.insert(workbatchProgress);
            }else {//??????????????????????????????????????????
                WorkbatchProgress progress = workbatchProgressList.get(0);
                planCount = progress.getPlanCount();
                if(planCount == null){
                    planCount = 0;
                }
                if(planNum == null){
                    planNum = 0;
                }
                planCount += planNum;
                progress.setPlanCount(planCount);
                workbatchProgressMapper.updateById(progress);
                List<WorkbatchProgress> progressList = workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>()
                        .eq("pt_name", ptName).eq("wb_no", wbNo).eq("wp_type", 2));
                if(progressList.isEmpty()){//??????????????????,?????????
                    WorkbatchProgress workbatchProgress = new WorkbatchProgress();
                    workbatchProgress.setCmName(cmName);//????????????
                    workbatchProgress.setCreateAt(date);//????????????
                    workbatchProgress.setFinishTime(finishTime);//??????????????????
                    workbatchProgress.setLimitTime(closeTime);//????????????(????????????)
                    workbatchProgress.setPdId(null);//??????id
                    workbatchProgress.setPdName(pdName);//????????????
                    workbatchProgress.setPtId(ptId);//??????id
                    workbatchProgress.setPtName(ptName);//????????????
                    workbatchProgress.setSdId(sdId);//??????id
                    workbatchProgress.setWarning(1);//????????????1???36??????2???24??????
                    workbatchProgress.setStatus(status);//?????????0?????????1?????????2?????????3????????????4????????????5??????6??????
                    workbatchProgress.setUpdateAt(date);//????????????
                    workbatchProgress.setWbId(wbId);//??????id
                    workbatchProgress.setWbNo(wbNo);//????????????
                    workbatchProgress.setWpType(2);//????????????1?????????????????????2???????????????3???????????????
                    workbatchProgressMapper.insert(workbatchProgress);

                }
            }
            List<WorkbatchProgress> progressList = workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>()
                    .eq("pr_id", prId).eq("pt_name", ptName).eq("wb_no", wbNo).eq("wp_type", 3));
            if(progressList.isEmpty()){
                WorkbatchProgress workbatchProgress = new WorkbatchProgress();
                workbatchProgress.setCmName(cmName);//????????????
                workbatchProgress.setCreateAt(date);//????????????
                workbatchProgress.setFinishTime(finishTime);//??????????????????
                workbatchProgress.setLimitTime(closeTime);//????????????(????????????)
                workbatchProgress.setPdId(null);//??????id
                workbatchProgress.setPdName(pdName);//????????????
                workbatchProgress.setPlanCount(planNum);//????????????
                workbatchProgress.setPrId(prId);//??????id
                workbatchProgress.setPrName(prName);//????????????
                workbatchProgress.setPtId(ptId);//??????id
                workbatchProgress.setPtName(ptName);//????????????
                workbatchProgress.setRealCount(completeNum);//???????????????
                workbatchProgress.setSdId(sdId);//??????id
                workbatchProgress.setStatus(status);//?????????0?????????1?????????2?????????3????????????4????????????5??????6??????
                workbatchProgress.setStayTime(stayTime);//??????????????????(??????)
                workbatchProgress.setMaId(maId);//??????id
                workbatchProgress.setTotalTime(totalTime);//???????????????(??????)
                workbatchProgress.setUpdateAt(date);//????????????
                workbatchProgress.setWarning(1);//????????????1???36??????2???24??????
                workbatchProgress.setWbId(wbId);//??????id
                workbatchProgress.setWbNo(wbNo);//????????????
                workbatchProgress.setWpType(3);//????????????1?????????????????????2???????????????3???????????????
                workbatchProgressMapper.insert(workbatchProgress);
            }
        }
    }
}
