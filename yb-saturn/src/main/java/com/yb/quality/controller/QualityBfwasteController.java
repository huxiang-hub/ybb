package com.yb.quality.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.quality.entity.QualityBfwaste;
import com.yb.quality.service.QualityBfwasteService;
import com.yb.quality.vo.QualityBfVO;
import com.yb.quality.vo.QualityBfwasteVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/QualityBfwaste")
@Api(tags = "废品上报_yb_quality_bfwaste")
public class QualityBfwasteController {

    @Autowired
    private QualityBfwasteService qualityBfwasteService;

    @GetMapping("/qualityBfwasteList")
    @ApiOperation(value = "查询机台排程单列表数据")
    public R<List<QualityBfwasteVO>> qualityBfwasteList(@ApiParam("设备id") @RequestParam("maId") Integer maId,
                                                        @ApiParam("班次id")@RequestParam("wsId") Integer wsId,
                                                        @ApiParam("日期")@RequestParam("targetDay") String targetDay){

        return R.data( qualityBfwasteService.qualityBfwasteList(maId, wsId, targetDay));
    }
    @GetMapping("/qualityBfVOList")
    @ApiOperation(value = "查询机台自检数据")
    public R<IPage<QualityBfVO>> qualityBfVOList(Query query,
                                                 @ApiParam("排程单id") @RequestParam("exId") Integer exId){
        if(exId == null){
            return R.data(null);
        }
        return R.data(qualityBfwasteService.qualityBfVOList(Condition.getPage(query), exId));
    }
    @PostMapping("/update")
    @ApiOperation(value = "上报")
    public R qualityBfVOList(@RequestBody QualityBfwaste qualityBfwaste){
        qualityBfwaste.setReportTime(new Date());
        qualityBfwaste.setReportStatus(1);
        return R.status(qualityBfwasteService.updateById(qualityBfwaste));
    }


}
