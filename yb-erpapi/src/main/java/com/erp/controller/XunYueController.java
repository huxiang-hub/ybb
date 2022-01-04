package com.erp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erp.entity.XYDevStatusRec;
import com.erp.entity.XYPartsOutPut;
import com.erp.mapper.XYDevStatusRecMapper;
import com.erp.mapper.XYPartsOutPutMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springblade.system.feign.XunYueClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author my
 * #Description
 */

@RestController
@RequestMapping("/xunyue")
@Api(tags = "讯越erp修改")
@Slf4j
public class XunYueController implements XunYueClient {

    @Autowired
    private XYPartsOutPutMapper xyPartsOutPutMapper;

    @Autowired
    private XYDevStatusRecMapper xyDevStatusRecMapper;

    @GetMapping("/open")
    @ApiOperation(value = "开始生产增加数据")
    public void open(@RequestParam Integer maId, @RequestParam String wbNo, @RequestParam Integer shiftId) {
        Date date = new Date();
        //status 状态为：1调机状态，2正式生产状态
        XYPartsOutPut xyPartsOutPut = new XYPartsOutPut();
        xyPartsOutPut.setBeginTime(date);
        xyPartsOutPut.setMaId(maId);
        xyPartsOutPut.setWOStatus(1);
        xyPartsOutPut.setPlanGUID(wbNo);
        xyPartsOutPut.setShiftId(shiftId);
        xyPartsOutPutMapper.insert(xyPartsOutPut);
    }


    @GetMapping("/update")
    public void update(@RequestParam Integer maId, @RequestParam String wbNo, @RequestParam Integer status, @RequestParam Integer number) {

        Date date = new Date();

        XYPartsOutPut xyPartsOutPut = xyPartsOutPutMapper.get(maId, wbNo);

        if (status == 1) {
            xyPartsOutPut.setAdjustOutput(number);
        } else {
            xyPartsOutPut.setGoodOutput(number);
        }
        xyPartsOutPut.setWOStatus(status);
        xyPartsOutPut.setUpdateTime(date);
        xyPartsOutPutMapper.updateById(xyPartsOutPut);
    }


    @GetMapping("/finishUpdate")
    public void finishUpdate(@RequestParam Integer maId, @RequestParam String wbNo, @RequestParam Integer number, @RequestParam Integer proNumber) {
        Date date = new Date();

        XYPartsOutPut xyPartsOutPut = xyPartsOutPutMapper.get(maId, wbNo);
        xyPartsOutPut.setAdjustOutput(number);
        xyPartsOutPut.setGoodOutput(proNumber);
        xyPartsOutPut.setUpdateTime(date);
        xyPartsOutPut.setEndTime(date);
        xyPartsOutPutMapper.updateById(xyPartsOutPut);
    }


    @GetMapping("/updateBoxNum")
    public void updateBoxNum(@RequestParam Integer maId, @RequestParam String wbNo, @RequestParam Integer exId, @RequestParam Integer number,
                             @RequestParam String status, @RequestParam Integer wStatus, @RequestParam BigDecimal speed, @RequestParam Date beginTime) {
        Date date = new Date();
        XYDevStatusRec xyDevStatusRec = xyDevStatusRecMapper.selectOne(new QueryWrapper<XYDevStatusRec>().eq("maId", maId));
        if (xyDevStatusRec != null) {
            xyDevStatusRec.setWOStatus(wStatus);
            xyDevStatusRec.setDeviceStatus(status);
            xyDevStatusRec.setExeId(exId);
            xyDevStatusRec.setUpdateTime(date);
            xyDevStatusRec.setBoxNum(number);
            xyDevStatusRec.setDevSpeed(speed);
            xyDevStatusRec.setBeginTime(beginTime);
            XYPartsOutPut xyPartsOutPut = xyPartsOutPutMapper.getByMaId(maId);
            xyDevStatusRec.setShiftId(xyPartsOutPut.getShiftId());
            xyDevStatusRecMapper.updateById(xyDevStatusRec);
        }
    }
}
