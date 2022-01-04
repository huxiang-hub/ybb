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
            List<Dict> dictList = saDictMapper.getList("maType");//查询所有的设备类型
            for(Dict dict : dictList){
                Integer dictKey = dict.getDictKey();
                if(dictKey == -1){//排除-1的
                    continue;
                }
                addStatisDayreach(targetDay, dictKey);
            }
        }
    }

    @Override
    public boolean timedTaskStatisDayreach(String targetDay) {
        try {
            /*先删除这天的达成率日报*/
            statisDayreachMapper.delete(new QueryWrapper<StatisDayreach>().eq("target_day", targetDay));
            List<Dict> dictList = saDictMapper.getList("maType");//查询所有的设备类型
            for (Dict dict : dictList) {
                Integer dictKey = dict.getDictKey();
                if (dictKey == -1) {//排除-1的
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
            Integer planCount = statisDayreach.getPlanCount();//计划数
            Integer realCount = statisDayreach.getRealCount() == null ? 0 : statisDayreach.getRealCount();//完成数
            if(planCount != null && planCount != 0){
                rateNum = realCount / (double)planCount;
            }else {
                rateNum = 1.0;//如果计划数为0或空,则达成率默认100%
            }
            statisDayreach.setRateNum(rateNum);
            statisDayreachMapper.insert(statisDayreach);
        }
    }


    @Override
    public Integer exportDayreach(DayreachParmsVO dayreachParmsVO) {
        /*查询需要导出的数据*/
        List<StatisDayreachVO> statisDayreachList = statisDayreachMapper.selectDayreach(dayreachParmsVO);
        if(statisDayreachList.isEmpty()){
            return null;
        }
        AddImageUtils addImageUtils = new AddImageUtils();
        /*根据达成率插入不同的图标*/
        for(StatisDayreachVO statisDayreachVO : statisDayreachList){
            Integer maType = statisDayreachVO.getMaType();
            Double rateNum = statisDayreachVO.getRateNum();
            statisDayreachVO.setImage(addImageUtils.addImage(rateNum));//插入图片
            String maTypeValue = saDictMapper.getValue("maType", Integer.valueOf(maType));
            statisDayreachVO.setMaTypeValue(maTypeValue);
        }
        /*处理一页数据*/
        List<DayreachSheetVO> dayreachSheetVOList = exportDayreachSheet(statisDayreachList);

        String templatePath = "model/reachDayModel.xls";
        BufferedOutputStream bos = null;//导出到网页
        List<Page> page = individual(dayreachSheetVOList); // 一张表一个对象数据
        Map<String, Object> model = new HashMap<>();

        model.put("pages", page);
        model.put("sheetNames", getSheetName(page));
        model.put("slName", getSheetName(page));
        try {
            bos = ExportlUtil.getBufferedOutputStream("生产车间日报表.xls", response);//返回前端处理
            JxlsUtils jxlsUtils = new JxlsUtils();
            jxlsUtils.exportExcel(templatePath, bos, model);//返回给前端处理
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
            //设置sheet名称
            pages.add(p);
        }
        return pages;
    }

    /**
     * Excel 的分页名（页码）的封装
     * 此方法用来获取分好页的页名信息，将信息放入一个链表中返回
     */
    public static ArrayList<String> getSheetName(List<Page> page) {
        ArrayList<String> al = new ArrayList<>();
        for (int i = 0; i < page.size(); i++) {
            al.add(page.get(i).getSheetName());
        }
        return al;
    }

    /**
     * 处理一个sheet数据
     * @param statisDayreachList
     * @return
     */
    private List<DayreachSheetVO> exportDayreachSheet(List<StatisDayreachVO> statisDayreachList) {
        List<DayreachSheetVO> dayreachSheetVOList = new ArrayList<>();
        Map<String, List<StatisDayreachVO>> statisDayreachVOMap  = new HashMap<>();
        for(StatisDayreachVO statisDayreachVO : statisDayreachList){//根据日期处里成不同的list
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
        for(String key : statisDayreachVOMap.keySet()){//遍历map,一个map为一个sheet
            DayreachSheetVO dayreachSheetVO = new DayreachSheetVO();
            dayreachSheetVO.setMounth(key.substring(5, 7));//月份
            dayreachSheetVO.setDay(key.substring(8, 10));//日
            ReachImageVO reachImageVO = addImageUtils.saveImageReach();
            dayreachSheetVO.setReachImageVO(reachImageVO);//达成率规则图标
            dayreachSheetVO.setTargetDay(key);//日期
            dayreachSheetVO.setStatisDayreachVOList(statisDayreachVOMap.get(key));
            dayreachSheetVOList.add(dayreachSheetVO);
        }
        return dayreachSheetVOList;
    }
}
