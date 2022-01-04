package com.yb.quality.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.panelapi.waste.vo.QualityBfwasteVO;
import com.yb.quality.entity.QualityWastClass;
import com.yb.quality.service.QualityWastClassService;
import com.yb.quality.vo.ProcessWastClassVO;
import com.yb.quality.vo.QualityWastClassVO;
import com.yb.workbatch.service.WorkbatchShiftService;
import com.yb.workbatch.vo.WorkbatchShiftDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/quality")
@Api(value = "废品分类控制器", tags = "废品分类控制器")
public class QualityWastClassController extends BladeController {

    private QualityWastClassService wastClassService;
    private IExecuteBrieferService exebrieferService;
    @Autowired
    private WorkbatchShiftService workbatchShiftService;

    /**
     * 获取全部工序废品分类
     * 自定义分页
     */
    @RequestMapping("/getQualityWastClassList")
    public R<IPage<QualityWastClassVO>> getQualityWastClassList(QualityWastClassVO wastClassVO, Query query) {
        IPage<QualityWastClassVO> pages =
                wastClassService.getQualityWastClassList(Condition.getPage(query), wastClassVO);
        return R.data(pages);
    }

    /**
     * 详情
     */
    @RequestMapping("/detail")
    public R<QualityWastClassVO> detail(QualityWastClassVO wastClassVO) {
        QualityWastClassVO detail =
                wastClassService.getQualityWastClassById(wastClassVO.getId());
        return R.data(detail);
    }

    /**
     * 更新修改
     *
     * @param wastClassVO
     * @return
     */
    @RequestMapping("/update")
    public R update(@RequestBody QualityWastClassVO wastClassVO) {
        wastClassService.updateById(wastClassVO);
        return R.success("OK");
    }


    /**
     * 新增
     *
     * @param wastClassVO
     * @return
     */
    @RequestMapping("/save")
    public R save(@RequestBody QualityWastClassVO wastClassVO) {
        wastClassService.save(wastClassVO);
        return R.success("OK");
    }

    /**物理删除（删除就不可找回）**/
    /**
     * 删除
     */
    @RequestMapping("/remove")
    public R remove(@RequestParam String ids) {
        wastClassService.removeByIds(Func.toIntList(ids));
        return R.success("OK");
    }

    @GetMapping("getListByPrId")
    @ApiOperation("机台废品-根据工序id获取废品分类")
    public R<List<QualityWastClass>> getListByPrId(@ApiParam("工序id") @RequestParam Integer prId) {
        List<QualityWastClass> list = wastClassService.list(Wrappers.<QualityWastClass>lambdaQuery().eq(QualityWastClass::getPrId, prId));
        return R.data(list);
    }


    @GetMapping("getPrBeforByPrId")
    @ApiOperation("机台废品-根据工序产品id获取前道所有工序废品分类")
    public R<List<ProcessWastClassVO>> getPrBeforByPrId(
            @ApiParam("工序id") @RequestParam Integer prId,
            @ApiParam("产品id") @RequestParam Integer pdId) {
        List<ProcessWastClassVO> list = wastClassService.getPrBeforByPrId(prId, pdId);
        return R.data(list);
    }


    @GetMapping("getWastClass")
    @ApiOperation("根据工单工序id，和上报主键bfId进行废品类型查询数据")
    public R<List<QualityBfwasteVO>> getWastClass(@ApiParam("工序id") @RequestParam Integer prId, @ApiParam("上报bfid") @RequestParam Integer bfId) {

        if (prId == null)
            return R.fail("工序为空，不能够进行废品上报数据");

        List<QualityBfwasteVO> prlist = wastClassService.getReportWastByPrid(prId);

        if (prlist == null || prlist.size() <= 0)
            prlist = new ArrayList<>();

        if (bfId != null) {
            ExecuteBriefer bfinfo = exebrieferService.getById(bfId);
            if (bfinfo != null && bfinfo.getWfId() != null) {
                WorkbatchShiftDataVO ordinfo = workbatchShiftService.getShiftData(bfinfo.getWfId());
                if (ordinfo != null && ordinfo.getPdId() != null) {
                    List<ProcessWastClassVO> list = wastClassService.getPrBeforByPrId(prId, ordinfo.getPdId());
                    if (list != null && list.size() > 0) {
                        for (ProcessWastClassVO pwl : list) {
                            QualityBfwasteVO qwaste = new QualityBfwasteVO();
                            qwaste.setPrId(pwl.getPrId());
                            qwaste.setExPrid(null);//不是本次工序的设置为空
                            qwaste.setWasteName(pwl.getPrName());
                            prlist.add(qwaste);
                        }
                    }
                }
            }
        }
        return R.data(prlist);
    }
}
