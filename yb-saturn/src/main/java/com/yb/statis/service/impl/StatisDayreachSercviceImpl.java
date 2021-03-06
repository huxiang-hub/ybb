package com.yb.statis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.common.DateUtil;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.statis.entity.StatisDayreach;
import com.yb.statis.excelUtils.JxlsUtils;
import com.yb.statis.excelUtils.Page;
import com.yb.statis.mapper.StatisDayreachMapper;
import com.yb.statis.mapper.StatisOrdreachMapper;
import com.yb.statis.service.StatisDayreachService;
import com.yb.statis.utils.AddImageUtils;
import com.yb.statis.vo.*;
import com.yb.system.dict.entity.Dict;
import com.yb.system.dict.mapper.SaDictMapper;
import com.yb.workbatch.excelUtils.ExportlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class StatisDayreachSercviceImpl extends ServiceImpl<StatisDayreachMapper, StatisDayreach> implements StatisDayreachService {

    @Autowired
    private HttpServletResponse response;
    @Autowired
    private StatisDayreachMapper statisDayreachMapper;
    @Autowired
    private SaDictMapper saDictMapper;
    @Autowired
    private StatisOrdreachMapper statisOrdreachMapper;


    @Override
    public IPage<StatisDayreachVO> statisDayreachPage(StatisDayreachVO statisDayreachVO, IPage<StatisDayreachVO> page) {
        List<StatisDayreachVO> statisDayreachList = statisDayreachMapper.statisDayreachPage(statisDayreachVO, page);
        for(StatisDayreachVO dayreachVO : statisDayreachList){
            Integer maType = dayreachVO.getMaType();
            String maTypeValue = saDictMapper.getValue("maType", maType);
            dayreachVO.setMaTypeValue(maTypeValue);
        }
        return page.setRecords(statisDayreachList);
    }



    @Scheduled(cron = "0 3 8 * * ?")
    public void timedTaskStatisDayreach(){
        String targetDay = DateUtil.format(DateUtil.addDayForDate(new Date(), -1));
        List<String> stringList = new ArrayList<>();
        stringList.add("xingyi");
        stringList.add("nxhr");
        for(String s : stringList){
            DBIdentifier.setProjectCode(s);
            List<Dict> dictList = saDictMapper.getList("maType");//???????????????????????????
            for(Dict dict : dictList){
                Integer dictKey = dict.getDictKey();
                if(dictKey == -1){//??????-1???
                    continue;
                }
                addStatisDayreach(targetDay, dictKey);
            }
        }
    }

    @Override
    public boolean timedTaskStatisDayreach(String targetDay) {
        try {
            /*?????????????????????????????????*/
            statisDayreachMapper.delete(new QueryWrapper<StatisDayreach>().eq("target_day", targetDay));
            List<Dict> dictList = saDictMapper.getList("maType");//???????????????????????????
            for (Dict dict : dictList) {
                Integer dictKey = dict.getDictKey();
                if (dictKey == -1) {//??????-1???
                    continue;
                }
                addStatisDayreach(targetDay, dictKey);
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void addStatisDayreach(String targetDay, Integer maType){
        StatisDayreach statisDayreach = statisOrdreachMapper.selectStatisDayreach(targetDay, maType);
        if(statisDayreach != null){
            Double rateNum;
            Date date = new Date();
            statisDayreach.setCreateAt(date);
            statisDayreach.setMaType(maType);
            statisDayreach.setTargetDay(targetDay);
            statisDayreach.setUpdateAt(date);
            Integer planCount = statisDayreach.getPlanCount();//?????????
            Integer realCount = statisDayreach.getRealCount() == null ? 0 : statisDayreach.getRealCount();//?????????
            if(planCount != null && planCount != 0){
                rateNum = realCount / (double)planCount;
            }else {
                rateNum = 1.0;//??????????????????0??????,??????????????????100%
            }
            statisDayreach.setRateNum(rateNum);
            statisDayreachMapper.insert(statisDayreach);
        }
    }


    @Override
    public Integer exportDayreach(DayreachParmsVO dayreachParmsVO) {
        /*???????????????????????????*/
        List<StatisDayreachVO> statisDayreachList = statisDayreachMapper.selectDayreach(dayreachParmsVO);
        if(statisDayreachList.isEmpty()){
            return null;
        }
        AddImageUtils addImageUtils = new AddImageUtils();
        /*????????????????????????????????????*/
        for(StatisDayreachVO statisDayreachVO : statisDayreachList){
            Integer maType = statisDayreachVO.getMaType();
            Double rateNum = statisDayreachVO.getRateNum();
            statisDayreachVO.setImage(addImageUtils.addImage(rateNum));//????????????
            String maTypeValue = saDictMapper.getValue("maType", Integer.valueOf(maType));
            statisDayreachVO.setMaTypeValue(maTypeValue);
        }
        /*??????????????????*/
        List<DayreachSheetVO> dayreachSheetVOList = exportDayreachSheet(statisDayreachList);

        String templatePath = "model/reachDayModel.xls";
        BufferedOutputStream bos = null;//???????????????
        List<Page> page = individual(dayreachSheetVOList); // ???????????????????????????
        Map<String, Object> model = new HashMap<>();

        model.put("pages", page);
        model.put("sheetNames", getSheetName(page));
        model.put("slName", getSheetName(page));
        try {
            bos = ExportlUtil.getBufferedOutputStream("?????????????????????.xls", response);//??????????????????
            JxlsUtils jxlsUtils = new JxlsUtils();
            jxlsUtils.exportExcel(templatePath, bos, model);//?????????????????????
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 1;
    }


    public static List<Page> individual(List<DayreachSheetVO> list) {
        List<Page> pages = new ArrayList<Page>();
        for (int i = 0; i < list.size(); i++) {
            Page p = new Page();
            p.setOnlyOne(list.get(i));
            p.setSheetName(list.get(i).getTargetDay());
            //??????sheet??????
            pages.add(p);
        }
        return pages;
    }

    /**
     * Excel ?????????????????????????????????
     * ????????????????????????????????????????????????????????????????????????????????????
     */
    public static ArrayList<String> getSheetName(List<Page> page) {
        ArrayList<String> al = new ArrayList<>();
        for (int i = 0; i < page.size(); i++) {
            al.add(page.get(i).getSheetName());
        }
        return al;
    }

    /**
     * ????????????sheet??????
     * @param statisDayreachList
     * @return
     */
    private List<DayreachSheetVO> exportDayreachSheet(List<StatisDayreachVO> statisDayreachList) {
        List<DayreachSheetVO> dayreachSheetVOList = new ArrayList<>();
        Map<String, List<StatisDayreachVO>> statisDayreachVOMap  = new HashMap<>();
        for(StatisDayreachVO statisDayreachVO : statisDayreachList){//??????????????????????????????list
            String targetDay = statisDayreachVO.getTargetDay();
            if(statisDayreachVOMap.containsKey(targetDay)){
                statisDayreachVOMap.get(targetDay).add(statisDayreachVO);
            }else {
                List<StatisDayreachVO> statisDayreachVOS= new ArrayList<>();
                statisDayreachVOS.add(statisDayreachVO);
                statisDayreachVOMap.put(targetDay, statisDayreachVOS);
            }
        }
        AddImageUtils addImageUtils = new AddImageUtils();
        for(String key : statisDayreachVOMap.keySet()){//??????map,??????map?????????sheet
            DayreachSheetVO dayreachSheetVO = new DayreachSheetVO();
            dayreachSheetVO.setMounth(key.substring(5, 7));//??????
            dayreachSheetVO.setDay(key.substring(8, 10));//???
            ReachImageVO reachImageVO = addImageUtils.saveImageReach();
            dayreachSheetVO.setReachImageVO(reachImageVO);//?????????????????????
            dayreachSheetVO.setTargetDay(key);//??????
            dayreachSheetVO.setStatisDayreachVOList(statisDayreachVOMap.get(key));
            dayreachSheetVOList.add(dayreachSheetVO);
        }
        return dayreachSheetVOList;
    }
}
