package com.yb.quality.controller;

import com.yb.quality.entity.QualityTacitly;
import com.yb.quality.service.QualityTacitlyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/QualityTacitly")
@AllArgsConstructor
@Api(tags = "质量默认模型")
public class QualityTacitlyController {
    @Autowired
    private QualityTacitlyService qualityTacitlyService;

    @GetMapping("list")
    @ApiOperation(value = "查询质量默认模型列表", notes = "传入qualityTacitly")
    public R list(QualityTacitly qualityTacitly){
        return R.data(qualityTacitlyService.selectQualityTacitlyList(qualityTacitly));
    }

    /**
     * 查询工序分类,检查分类的表详情
     * @param pyId
     * @param checkType
     * @return
     */
    @GetMapping("getQualityTacitly")
    @ApiOperation(value = "查询工序、检查分类的表详情", notes = "传入工序分类id:pyId,检查类型:checkType")
    public R getQualityTacitly(Integer pyId, String checkType){
        return R.data(qualityTacitlyService.getQualityTacitly(pyId, checkType));
    }
    @PostMapping("save")
    @ApiOperation(value = "保存新增表的表名集相关信息", notes = "传入qualityTacitly")
    public R save(QualityTacitly qualityTacitly){
        return R.status(qualityTacitlyService.save(qualityTacitly));
    }
    @PostMapping("delete")
    @ApiOperation(value = "删除质量检查表名数据", notes = "传入ids")
    public R delete(@RequestParam String ids){
        List<Integer> idList = Func.toIntList(ids);
        List<QualityTacitly> qualityTacitlyList = new ArrayList<>();
        QualityTacitly qualityTacitly;
        for(Integer id : idList){
            qualityTacitly = new QualityTacitly();
            qualityTacitly.setId(id);
            qualityTacitly.setStatus(0);
            qualityTacitlyList.add(qualityTacitly);
        }
        return R.status(qualityTacitlyService.saveOrUpdateBatch(qualityTacitlyList));
    }

    @PostMapping("update")
    @ApiOperation(value = "修改质量检查表相关数据", notes = "传入qualityTacitly")
    public R update(QualityTacitly qualityTacitly) {
        return R.status(qualityTacitlyService.updateById(qualityTacitly));
    }
}
