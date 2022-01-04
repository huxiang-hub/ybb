package com.yb.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.panelapi.user.entity.BaseFactory;
import com.yb.panelapi.user.mapper.BaseFactoryMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/BaseFactory")
@Api(tags = "公司信息")
public class BaseFactoryController {

    @Autowired
    private BaseFactoryMapper baseFactoryMapper;

    @GetMapping("/getCorpId")
    @ApiOperation(value = "查询钉钉的公司唯一标识")
    public R getCorpId(){
        BaseFactory baseFactory = baseFactoryMapper.selectOne(new QueryWrapper<>());
        return R.data(baseFactory.getCorpId());
    }
}
