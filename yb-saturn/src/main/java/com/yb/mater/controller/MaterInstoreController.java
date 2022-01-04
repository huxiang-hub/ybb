package com.yb.mater.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.mater.entity.MaterBatchlink;
import com.yb.mater.entity.MaterInstore;
import com.yb.mater.service.MaterBatchlinkService;
import com.yb.mater.service.MaterInstoreService;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/MaterInstore")
public class MaterInstoreController {
    @Autowired
    private MaterInstoreService materInstoreService;
    @Autowired
    private MaterBatchlinkService materBatchlinkService;

    @RequestMapping("/getDetail")
    public R getDetail(Integer mbId){
        List<MaterInstore> materInstoreList =
                materInstoreService.getBaseMapper().selectList(new QueryWrapper<MaterInstore>().eq("mb_id", mbId));
        return R.data(materInstoreList);
    }
    @RequestMapping("/save")
    public R add(@RequestBody MaterInstore materInstore){
        Integer mbId = materInstore.getId();
        MaterBatchlink materBatchlink = materBatchlinkService.getById(mbId);
        Integer plateNumber = 0;
        if(materBatchlink != null){
            String plateNum = materInstore.getPlateNum();//板数（可选）
            if(!StringUtil.isEmpty(plateNum)){
                plateNumber = Integer.valueOf(plateNum);
            }
            String batchlinkPlateNum = materBatchlink.getPlateNum();
            if(!StringUtil.isEmpty(batchlinkPlateNum)){
                plateNumber += Integer.valueOf(batchlinkPlateNum);
            }
            String location = materInstore.getLocation();//摆放位置
            String batchlinkLocation = materBatchlink.getLocation();
            if(!StringUtil.isEmpty(batchlinkLocation)){
                location = batchlinkLocation + "," + location;
            }

            Integer processNum = materBatchlink.getProcessNum();//工序数量
            Integer status = 2;
            processNum = processNum == null ? 0 : processNum;
            Integer realacceptNum = materInstore.getRealacceptNum() == null ? 0 : materInstore.getRealacceptNum();//实收数量
            Integer materBatchlinkRealacceptNum = materBatchlink.getRealacceptNum() == null ? 0 : materBatchlink.getRealacceptNum();
            Integer batchlinkRealacceptNum =  materBatchlinkRealacceptNum + realacceptNum;
            if(processNum > batchlinkRealacceptNum){
                status = 1;//部分入库
            }
            materBatchlink.setStatus(status);
            materBatchlink.setPlateNum(plateNumber.toString());
            materBatchlink.setLocation(location);
            materBatchlink.setRealacceptNum(batchlinkRealacceptNum);
            materBatchlinkService.updateById(materBatchlink);
        }
        Integer userId = SaSecureUtil.getUserId();//获取当前登录人id
        materInstore.setReceiveUsid(userId);
        materInstore.setMbId(mbId);
        materInstore.setId(null);
        materInstore.setInTime(new Date());
        materInstore.setCreateAt(new Date());
        return R.status(materInstoreService.save(materInstore));
    }

}
