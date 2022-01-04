package com.yb.prod.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.prod.entity.ProdClassify;
import com.yb.prod.entity.ProdPdinfo;
import com.yb.prod.service.IProdClassifyService;
import com.yb.prod.service.IProdPartsinfoService;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.prod.vo.ProdPdinfoVO;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/prodclassify")
public class ProdClassifyController {
    @Autowired
    private IProdClassifyService prodClassifyService;

    /**
     * 获取全部产品分类
     */
    @GetMapping("/getAllList")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "集合", notes = "无参数")
    public R<List<ProdClassify>> getAllList(){
        Map<String,Object> map = new HashMap<>();
        map.put("is_used",1);//查询启用的全部产品分类
        return R.data(prodClassifyService.listByMap(map));
    }
}
