package com.yb.statis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.execute.mapper.ExecuteBrieferMapper;
import com.yb.execute.vo.ExecuteBrieferVO;
import com.yb.statis.entity.StatisMachreach;
import com.yb.statis.mapper.StatisMachreachMapper;
import com.yb.statis.request.StatisMachreachListRequest;
import com.yb.statis.request.HourPlanRateRequest;
import com.yb.statis.service.IStatisMachreachService;
import com.yb.statis.vo.DayStatisMachreachListVO;
import com.yb.statis.vo.MonthStatisMachreachListVO;
import com.yb.statis.vo.StatisMachreachListVO;
import com.yb.statis.vo.StatisMachreachVO;
import com.yb.timer.DateTimeUtil;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class StatisMachreachServiceImpl extends ServiceImpl<StatisMachreachMapper, StatisMachreach> implements IStatisMachreachService {
    @Autowired
    private StatisMachreachMapper statisMachreachMapper;
    @Autowired
    private ExecuteBrieferMapper executeBrieferMapper;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;


    @Override
    public IPage<StatisMachreach> selectStatisMachreach(IPage<StatisMachreach> page, StatisMachreachVO statisMachreach) {
        List<StatisMachreach> statisMachreaches = statisMachreachMapper.selectStatisMachreach(page, statisMachreach);
        return page.setRecords(statisMachreaches);
    }

    @Override
    public IPage<StatisMachreachVO> selectOne(Integer id) {
        List<StatisMachreachVO> listStatis = statisMachreachMapper.selectlist(id);
        IPage page = new Page();
        page.setRecords(listStatis);
        return page;
    }

    //???????????????????????????UPdate ??????
    public void updateStatisMachreachObj(Integer bId, WorkbatchOrdlink ordlink, WorkbatchShift workbatchShift) {
        String strDateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        ExecuteBrieferVO executeBrieferVO = executeBrieferMapper.getExecuteVoByEsId(bId);
        StatisMachreach machreachVO = statisMachreachMapper.getLastBySdId(workbatchShift.getId(), ordlink.getMaId(), workbatchShift.getWsId(), workbatchShift.getSdDate());
        if (machreachVO != null) {
            machreachVO.setEbId(executeBrieferVO.getEbId());
//            String targetDate = sdf.format(new Date());
//            //oee????????????2020-04-15
//            machreachVO.setTargetDate(targetDate);
//            //??????ID
//            machreachVO.setMaId(executeBrieferVO.getMaId());
//            //????????????
//            machreachVO.setMaName(executeBrieferVO.getMachineName());
//            //?????????ID
//            machreachVO.setUsId(executeBrieferVO.getUsId());
//            //????????????
//            machreachVO.setUsName(executeBrieferVO.getUsrName());
//            //?????????ID
//            machreachVO.setSdId(executeBrieferVO.getSdId());
//            //????????????
//            machreachVO.setPlanNum(executeBrieferVO.getPlanNum());
            //?????????
            Integer countNum = machreachVO.getCountNum() == null ? 0 : machreachVO.getCountNum();
            machreachVO.setCountNum(countNum + executeBrieferVO.getCountNum());
            //?????????
            Integer wasteNum = machreachVO.getWasteNum() == null ? 0 : machreachVO.getWasteNum();
            machreachVO.setWasteNum(wasteNum + executeBrieferVO.getWasteNum());
            //??????
            Integer totalNumber = machreachVO.getCountNum() + machreachVO.getWasteNum();
            //?????????
            machreachVO.setYieldRate(new BigDecimal((float) executeBrieferVO.getCountNum() / totalNumber).setScale(2, BigDecimal.ROUND_HALF_UP));
            if (machreachVO.getYieldRate().compareTo(new BigDecimal(0)) == 0) {
                machreachVO.setYieldRate(new BigDecimal("0.01"));
            }
            //?????????
            machreachVO.setReachRate(new BigDecimal((float) (totalNumber) / machreachVO.getPlanNum()).setScale(2, BigDecimal.ROUND_HALF_UP));   //???????????????+????????????/????????????
            if (machreachVO.getReachRate().compareTo(new BigDecimal(0)) == 0) {
                machreachVO.setReachRate(new BigDecimal("0.01"));
            }
            //???????????????
            Integer wfFinishNum = workbatchShiftMapper.selectCount(new QueryWrapper<WorkbatchShift>().eq("shift_status", 2).eq("ma_id", workbatchShift.getMaId()).eq("sd_date", workbatchShift.getSdDate()));
            machreachVO.setWfFinishnum(wfFinishNum);
            //?????????
            machreachVO.setBoxNum(totalNumber);
            //????????????
            machreachVO.setUpdateAt(new Date());
            statisMachreachMapper.updateById(machreachVO);
        }

//        else {
//            machreachVO.setEbId(executeBrieferVO.getEbId());
//            String targetDate = sdf.format(new Date());
//            //oee????????????2020-04-15
//            machreachVO.setTargetDate(targetDate);
//            //??????ID
//            machreachVO.setMaId(executeBrieferVO.getMaId());
//            //????????????
//            machreachVO.setMaName(executeBrieferVO.getMachineName());
//            //?????????ID
//            machreachVO.setUsId(executeBrieferVO.getUsId());
//            //????????????
//            machreachVO.setUsName(executeBrieferVO.getUsrName());
//            //?????????ID
//            machreachVO.setSdId(executeBrieferVO.getSdId());
//            //????????????
//            machreachVO.setPlanNum(executeBrieferVO.getPlanNum());
//            //?????????
//            machreachVO.setCountNum(executeBrieferVO.getCountNum());
//            //?????????
//            machreachVO.setWasteNum(executeBrieferVO.getWasteNum());
//            //??????
//            Integer totalNumber = machreachVO.getCountNum() + machreachVO.getWasteNum();
//            //?????????
//            machreachVO.setYieldRate(new BigDecimal((float) executeBrieferVO.getCountNum() / totalNumber).setScale(2, BigDecimal.ROUND_HALF_UP));
//            if (machreachVO.getYieldRate().compareTo(new BigDecimal(0)) == 0) {
//                machreachVO.setYieldRate(new BigDecimal("0.01"));
//            }
//            //?????????
//            machreachVO.setReachRate(new BigDecimal((float) (totalNumber) / machreachVO.getPlanNum()).setScale(2, BigDecimal.ROUND_HALF_UP));   //???????????????+????????????/????????????
//            if (machreachVO.getReachRate().compareTo(new BigDecimal(0)) == 0) {
//                machreachVO.setReachRate(new BigDecimal("0.01"));
//            }
//            //????????????????????????
//            if (workbatchShift.getShiftStatus().equals("2")) {
//                machreachVO.setWfNum(machreachVO.getWfFinishnum() + 1);
//            }
//            //?????????
//            machreachVO.setBoxNum(totalNumber);
//            //????????????
//            machreachVO.setUpdateAt(new Date());
//            //????????????????????????
//            if (workbatchShift.getShiftStatus().equals("2")) {
//                machreachVO.setWfNum(machreachVO.getWfFinishnum() + 1);
//            }
//            statisMachreachMapper.updateById(machreachVO);
//        }
    }

    @Override
    public IPage<StatisMachreachListVO> planRate(IPage<StatisMachreachListVO> page, StatisMachreachListRequest request) {
        List<StatisMachreachListVO> statisMachreachListVOS = statisMachreachMapper.planRate(page, request);
        return page.setRecords(statisMachreachListVOS);
    }

    @Override
    public List<MonthStatisMachreachListVO> monthPlanRate(String maType, Integer wsId) {
        String groupBy = "ymm.ma_type,DATE(ysm.create_at)";
        if (StringUtils.isNotBlank(maType)) {
            groupBy = "ymm.id,DATE(ysm.create_at)";
        }
        //??????????????????????????????
        Date startTime = DateTimeUtil.getBeginDayOfMonth();
        Date endTime = DateTimeUtil.getEndDayOfMonth();
        List<MonthStatisMachreachListVO> monthStatisMachreachListVOS = statisMachreachMapper.monthPlanRate(startTime, endTime, maType, groupBy, wsId);
        return monthStatisMachreachListVOS;
    }

    @Override
    public List<DayStatisMachreachListVO> hourPlanRate(HourPlanRateRequest request) {
        if (StringUtils.isNotBlank(request.getMaType())) {
            request.setGroupBy("ymm.id,yso.target_hour,target_day");
        } else {
            request.setGroupBy("ymm.ma_type,yso.target_hour,target_day");
        }
        List<DayStatisMachreachListVO> dayStatisMachreachListVOS = statisMachreachMapper.hourPlanRate(request);
        return dayStatisMachreachListVOS;
    }

    //?????????????????????????????????
    @Override
    public void createStatisMachreachObj(Integer Id) {
        String strDateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        ExecuteBrieferVO executeBrieferVO = executeBrieferMapper.getExecuteVoByEsId(Id);
        StatisMachreachVO machreachVO = new StatisMachreachVO();
        //??????ID
        machreachVO.setEbId(executeBrieferVO.getEbId());
        String targetDate = sdf.format(new Date());
        //oee????????????2020-04-15
        machreachVO.setTargetDate(targetDate);
        //??????ID
        machreachVO.setMaId(executeBrieferVO.getMaId());
        //????????????
        machreachVO.setMaName(executeBrieferVO.getMachineName());
        //?????????ID
        machreachVO.setUsId(executeBrieferVO.getUsId());
        //????????????
        machreachVO.setUsName(executeBrieferVO.getUsrName());
        //?????????ID
        machreachVO.setSdId(executeBrieferVO.getSdId());
        //????????????
        machreachVO.setBoxNum(executeBrieferVO.getBoxNum());
        //????????????
        machreachVO.setPlanNum(executeBrieferVO.getPlanNum());
        //???????????????
        machreachVO.setReachRate(new BigDecimal((float) machreachVO.getBoxNum() / machreachVO.getPlanNum()).setScale(2, BigDecimal.ROUND_HALF_UP));   //????????????/????????????
        //?????????
        machreachVO.setCountNum(0);
        //?????????
        machreachVO.setWasteNum(0);
        //?????????
        machreachVO.setYieldRate(new BigDecimal(0));
        //????????????
        machreachVO.setCreateAt(new Date());
        //????????????
        machreachVO.setUpdateAt(null);
        statisMachreachMapper.insert(machreachVO);
    }


    @Override
    public void createShiftStatisMachreach(Integer bId, WorkbatchShift shift, Integer maId, Integer currNum) {
        StatisMachreach machreach = statisMachreachMapper.selectOne(new QueryWrapper<StatisMachreach>().eq("wf_id", shift.getId()).eq("ma_id", maId).eq("ws_id", shift.getWsId()));
        ExecuteBrieferVO executeBrieferVO = executeBrieferMapper.getExecuteVoByEsId(bId);
        if (machreach == null) {
            Integer wsId = shift.getWsId();
            StatisMachreach statisMachreach = new StatisMachreach();
            //??????ID
            statisMachreach.setEbId(executeBrieferVO.getEbId());
            statisMachreach.setWfId(shift.getId());
            statisMachreach.setTargetDate(shift.getSdDate());
            //??????ID
            statisMachreach.setMaId(executeBrieferVO.getMaId());
            //????????????
            statisMachreach.setMaName(executeBrieferVO.getMachineName());
            //?????????ID
            statisMachreach.setUsId(executeBrieferVO.getUsId());
            //????????????
            statisMachreach.setUsName(executeBrieferVO.getUsrName());
            //?????????ID
            statisMachreach.setSdId(shift.getSdId());
            //???????????????
            statisMachreach.setBoxNum(currNum);
            //????????????
            statisMachreach.setPlanNum(executeBrieferVO.getPlanNum());
            //?????????
            statisMachreach.setCountNum(0);
            //?????????
            statisMachreach.setWasteNum(0);
            //?????????
            statisMachreach.setYieldRate(new BigDecimal(0));
            //????????????
            statisMachreach.setCreateAt(new Date());
            //????????????
            statisMachreach.setUpdateAt(null);
            //????????????
            statisMachreach.setWsId(wsId);
            //????????????????????????????????????
            //Integer wfNum = workbatchShiftMapper.getShiftCountNum(shift.getSdDate(), shift.getWsId(), maId);
            //?????????????????????
            statisMachreachMapper.insert(statisMachreach);
        } else {
            statisMachreachMapper.updateById(machreach);
        }
    }
}
