package com.yb.sysShow.controller;

import com.yb.common.DateUtil;
import com.yb.panelapi.user.utils.R;
import com.yb.sysShow.entity.BoxCleanLogEntity;
import com.yb.sysShow.entity.BoxinfoViewEntity;
import com.yb.sysShow.mapper.BoxinfoViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author by summer
 * @date 2020/5/27.
 */
@RestController
@RequestMapping("/sys/")
public class SysShowController {

    @Autowired
    private BoxinfoViewMapper boxinfoView;


    @GetMapping("/show")
    public List<BoxinfoViewEntity> getList(){
        List<BoxinfoViewEntity> blist = boxinfoView.getlist();
        return blist;
    }

    /**
     * 查询
     * @param uuid
     * @return
     */
    @GetMapping("/detail")
    public BoxinfoViewEntity getUuid(String uuid){
        BoxinfoViewEntity boxinfo = boxinfoView.getUuid(uuid);
        return boxinfo;
    }

    /**
     * 清零操作
     */
    @GetMapping("/clean")
    public R clean(String uuid){
        if (uuid == null || uuid.length() <= 0) {
            return R.error();
        } else {
            BoxinfoViewEntity boxinfo = boxinfoView.getUuid(uuid);
            BoxCleanLogEntity cldo = new BoxCleanLogEntity();
            cldo.setUuid(uuid);
            if (boxinfo != null) {
                cldo.setStatus(boxinfo.getStatus());
                cldo.setNumber(boxinfo.getNumber());
                cldo.setNumber_of_day(boxinfo.getNumberOfday());
                cldo.setDspeed(boxinfo.getDspeed());
                cldo.setUpdate_at(boxinfo.getUpdateAt());
            }
            int num = boxinfoView.cleanByuuid(uuid);

            Date curnow = new Date();
            Date day = DateUtil.changeDay(DateUtil.format(curnow));
            cldo.setOp_date(day); //操作的日
            cldo.setClean_time(curnow);
            String msg = "操作失败，请重试";
            if (num > 0) {
                msg = "清零成功。";
                boxinfoView.cleanLog(cldo); //記錄清理日志信息
            }
            return R.ok(msg);
        }
    }
}
