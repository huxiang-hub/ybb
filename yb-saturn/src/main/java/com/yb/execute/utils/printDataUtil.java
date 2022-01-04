package com.yb.execute.utils;

import com.yb.common.DateUtil;
import com.yb.execute.vo.TraycardDataVO;
import com.yb.execute.vo.TraycardTextVO;
import com.yb.panelapi.user.entity.BaseFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class printDataUtil {

    /**
     * 标识卡,退料数据处理
     * @param traycardDataVOList
     * @return
     */
    public static List<TraycardTextVO> getTraycardTextVOList(List<TraycardDataVO> traycardDataVOList, BaseFactory baseFactory){

        List<TraycardTextVO> textList = new ArrayList<>();
        TraycardTextVO text;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        for(TraycardDataVO traycardDataVO : traycardDataVOList){
            text = new TraycardTextVO();
            String refNowDay;
            String time = "";
            Date startTime = traycardDataVO.getStartTime();//开始时间
            if(startTime != null){
                refNowDay = DateUtil.refNowDay(startTime);
                time = format.format(startTime);
            }else {
                refNowDay = DateUtil.refNowDay();
            }
            text.setWbNo(traycardDataVO.getWbNo());
            text.setMaName(traycardDataVO.getMaName());
            text.setNum(traycardDataVO.getNum());
            text.setPdName(traycardDataVO.getPdName());
            text.setUsName(traycardDataVO.getUsName());
            text.setWsName(traycardDataVO.getCkName());
            text.setPrName(traycardDataVO.getPrName());
            text.setStNo(traycardDataVO.getStNo());
            text.setTrayNo(traycardDataVO.getTrayNo());
            text.setPrintTime(new Date());
            text.setId(traycardDataVO.getId());
            text.setMpId(traycardDataVO.getMpId());
            text.setPrintNum(traycardDataVO.getPrintNum());
            text.setTdNo(traycardDataVO.getTdNo());
            text.setProductTime(refNowDay + " " + time +
                    "~" + format.format(date));//开始时间~打印时间
            textList.add(text);
        }
        return textList;

    }
}
