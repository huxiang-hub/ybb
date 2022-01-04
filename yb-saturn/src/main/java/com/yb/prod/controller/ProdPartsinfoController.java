package com.yb.prod.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.prod.service.IProdPartsinfoService;
import com.yb.prod.vo.ProdPartsinfoVo;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/prodpartsinfo")
public class ProdPartsinfoController {
    @Autowired
    private IProdPartsinfoService iProdPartsinfoService;
    @RequestMapping("/selectPtNames")
    public List<ProdPartsinfoVo> selectPtNames(Integer wbId) {

        return iProdPartsinfoService.selectPtNames(wbId);
    }

    /**
     * 删除部件
     * @param id
     * @return
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入orderOrdinfo")
    public R remove(Integer id){
        return R.data(iProdPartsinfoService.removeById(id));
    }

    /**
     * 增加部件
     * @param prodPartsinfoVo
     * @return
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入orderOrdinfo")
    public R save(ProdPartsinfoVo prodPartsinfoVo){
        iProdPartsinfoService.save(prodPartsinfoVo);
        return R.data(prodPartsinfoVo);
    }
}
