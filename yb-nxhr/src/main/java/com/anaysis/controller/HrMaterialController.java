package com.anaysis.controller;

import com.alibaba.fastjson.JSONObject;
import com.anaysis.entity.HrMaterial;
import com.anaysis.parames.ProceduerParam;
import com.anaysis.service.impl.HrMaterialService;
import com.anaysis.utis.ProceduerUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author lzb
 * @Date 2021/1/10 09:47
 **/
@RestController
@RequestMapping("/material")
public class HrMaterialController {

    @Autowired
    private HrMaterialService hrMaterialService;

    @ApiOperation("根据物料条码查询物料")
    @GetMapping("selectByBarCode")
    public HrMaterial selectByBarCode(@RequestParam(value = "barCode") String barCode) {
        return hrMaterialService.selectByBarCode(barCode);
    }

}
