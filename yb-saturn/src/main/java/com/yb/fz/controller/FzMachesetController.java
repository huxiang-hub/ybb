package com.yb.fz.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.fz.entity.FzMacheset;
import com.yb.fz.service.FzMachesetService;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plapi/fzmacheset")
public class FzMachesetController {
    @Autowired
    private FzMachesetService fzMachesetService;

    /**
     * 获取全部方正上报设定
     */
    @GetMapping("/getAllList")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "集合", notes = "无参数")
    public R<List<FzMacheset>> getAllList(){
        Map<String,Object> map = new HashMap<>();
        map.put("status",1);//查询启用的方正上报设定
        return R.data(fzMachesetService.listByMap(map));
    }

    /**
     * 获取全部方正上报设定
     */
    @GetMapping("/getListByMaId")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "集合", notes = "无参数")
    @ResponseBody
    public R<List<FzMacheset>> getListByMaId(Integer maId){
        Map<String,Object> map = new HashMap<>();
        map.put("status",1);//查询启用的方正上报设定
        map.put("ma_Id",maId);//查询某台设备的上报设定
        return R.data(fzMachesetService.listByMap(map));
    }
}
