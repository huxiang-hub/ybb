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
                if (wbNoMap.containsKey(progressVO.getWbNo())) {//一级菜单,工单编号
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
        if (ptNameMap.containsKey(ptName)) {//二级菜单,部件名称
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
//                    Integer stayTime = batchProgressVO.getStayTime();//(分钟)
                    Double differenceTime = 0.0;
                if(limitTime != null){
                    differenceTime = (limitTime.getTime() - date.getTime()) / (double) 1000 / 3600;//小时
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
     * 新增主计划数据
     */
    public void addWorkbatchProgress(WorkbatchOrdlink workbatchOrdlink){
        Date date = new Date();
        Integer status = 0;
        Integer totalTime = null;//总计划时间
        Integer stayTime = null;//工序剩余时间
        Integer planCount;//计划总量
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
        Integer planNum = workbatchOrdlink.getPlanNum();//计划总量
        Integer completeNum = workbatchOrdlink.getCompleteNum();//已完成数
        Date closeTime = workbatchOrdlink.getCloseTime();//截止时间
        Integer maId = workbatchOrdlink.getMaId();
        if(!StringUtil.isEmpty(wbNo)){
            /*查询该批次下的主计划*/
            List<WorkbatchProgress> workbatchProgressList =
                    workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>().eq("wb_no", wbNo).eq("wp_type", 1));
            if(workbatchProgressList.isEmpty()){//判断是否存在,不存在则创建
                WorkbatchProgress workbatchProgress = new WorkbatchProgress();
                workbatchProgress.setCmName(cmName);//客户名称
                workbatchProgress.setCreateAt(date);//创建时间
                workbatchProgress.setFinishTime(finishTime);//实际交货时间
                workbatchProgress.setLimitTime(closeTime);//限制时间(截止时间)
                workbatchProgress.setPdId(null);//产品id
                workbatchProgress.setPdName(pdName);//产品名称
                workbatchProgress.setPlanCount(planNum);//计划总量
                workbatchProgress.setRealCount(completeNum);//已完成数量
                workbatchProgress.setSdId(sdId);//排产id
                workbatchProgress.setStatus(status);//状态：0已导入1已排产2已生产3部分完成4全部完成5挂起6驳回
                workbatchProgress.setTotalTime(totalTime);//总计划时间(分钟)
                workbatchProgress.setUpdateAt(date);//修改时间
                workbatchProgress.setWarning(1);//预警状态1级36小时2级24小时
                workbatchProgress.setWbId(wbId);//批次id
                workbatchProgress.setWbNo(wbNo);//批次编号
                workbatchProgress.setWpType(1);//进度类型1、批次工单类型2、部件类型3、工序类型
                workbatchProgressMapper.insert(workbatchProgress);
            }else {//存在则查询部件主计划是否存在
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
                if(progressList.isEmpty()){//如果部件为空,则创建
                    WorkbatchProgress workbatchProgress = new WorkbatchProgress();
                    workbatchProgress.setCmName(cmName);//客户名称
                    workbatchProgress.setCreateAt(date);//创建时间
                    workbatchProgress.setFinishTime(finishTime);//实际交货时间
                    workbatchProgress.setLimitTime(closeTime);//限制时间(截止时间)
                    workbatchProgress.setPdId(null);//产品id
                    workbatchProgress.setPdName(pdName);//产品名称
                    workbatchProgress.setPtId(ptId);//部件id
                    workbatchProgress.setPtName(ptName);//部件名称
                    workbatchProgress.setSdId(sdId);//排产id
                    workbatchProgress.setWarning(1);//预警状态1级36小时2级24小时
                    workbatchProgress.setStatus(status);//状态：0已导入1已排产2已生产3部分完成4全部完成5挂起6驳回
                    workbatchProgress.setUpdateAt(date);//修改时间
                    workbatchProgress.setWbId(wbId);//批次id
                    workbatchProgress.setWbNo(wbNo);//批次编号
                    workbatchProgress.setWpType(2);//进度类型1、批次工单类型2、部件类型3、工序类型
                    workbatchProgressMapper.insert(workbatchProgress);

                }
            }
            List<WorkbatchProgress> progressList = workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>()
                    .eq("pr_id", prId).eq("pt_name", ptName).eq("wb_no", wbNo).eq("wp_type", 3));
            if(progressList.isEmpty()){
                WorkbatchProgress workbatchProgress = new WorkbatchProgress();
                workbatchProgress.setCmName(cmName);//客户名称
                workbatchProgress.setCreateAt(date);//创建时间
                workbatchProgress.setFinishTime(finishTime);//实际交货时间
                workbatchProgress.setLimitTime(closeTime);//限制时间(截止时间)
                workbatchProgress.setPdId(null);//产品id
                workbatchProgress.setPdName(pdName);//产品名称
                workbatchProgress.setPlanCount(planNum);//计划总量
                workbatchProgress.setPrId(prId);//工序id
                workbatchProgress.setPrName(prName);//工序名称
                workbatchProgress.setPtId(ptId);//部件id
                workbatchProgress.setPtName(ptName);//部件名称
                workbatchProgress.setRealCount(completeNum);//已完成数量
                workbatchProgress.setSdId(sdId);//排产id
                workbatchProgress.setStatus(status);//状态：0已导入1已排产2已生产3部分完成4全部完成5挂起6驳回
                workbatchProgress.setStayTime(stayTime);//工序剩余时间(分钟)
                workbatchProgress.setMaId(maId);//设备id
                workbatchProgress.setTotalTime(totalTime);//总计划时间(分钟)
                workbatchProgress.setUpdateAt(date);//修改时间
                workbatchProgress.setWarning(1);//预警状态1级36小时2级24小时
                workbatchProgress.setWbId(wbId);//批次id
                workbatchProgress.setWbNo(wbNo);//批次编号
                workbatchProgress.setWpType(3);//进度类型1、批次工单类型2、部件类型3、工序类型
                workbatchProgressMapper.insert(workbatchProgress);
            }
        }
    }
}
