package com.yb.quality.controller;

import com.yb.common.DateUtil;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.quality.entity.QualityItemset;
import com.yb.quality.mapper.QualityItemsetMapper;
import com.yb.quality.request.QualityRequest;
import com.yb.quality.service.QualityItemsetService;
import com.yb.quality.vo.AddColsVO;
import com.yb.quality.vo.CreatTableVO;
import com.yb.quality.vo.OptionVO;
import com.yb.quality.vo.ProcessClassifyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/QualityItemset")
@Api(tags = "质量检查内容定义")
public class QualityItemsetController {
    @Autowired
    private QualityItemsetService qualityItemsetService;
    @Autowired
    private QualityItemsetMapper qualityItemsetMapper;

    @GetMapping("/getQualityItemsetList")
    @ApiOperation(value = "检查可选择的项", notes = "传入检查类型和工序分类id")
    public R<List<OptionVO>> getQualityItemsetList(Integer pyId, String checkType) {
        return R.data(qualityItemsetService.getQualityItemsetList(pyId, checkType));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除质量检查内容", notes = "传入ids")
    public R delete(@RequestParam String ids) {
        List<QualityItemset> qualityItemsetList = new ArrayList<>();
        QualityItemset qualityItemset;
        for (Integer id : Func.toIntList(ids)) {
            qualityItemset = new QualityItemset();
            qualityItemset.setId(id);
            qualityItemset.setStatus(2);
            qualityItemsetList.add(qualityItemset);
        }
        return R.status(qualityItemsetService.saveOrUpdateBatch(qualityItemsetList));
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改质量检查内容", notes = "传入qualityItemset")
    public R update(QualityItemset qualityItemset) {
        return R.status(qualityItemsetService.updateById(qualityItemset));
    }

    @PostMapping("/creatTable")
    @ApiOperation(value = "新增表默认表", notes = "creatTableVO")
    public R creatTable(@RequestBody CreatTableVO creatTableVO) {
        return R.status(qualityItemsetService.creatTable(creatTableVO));
    }

    @PostMapping("/insertTable")
    @ApiOperation(value = "插入自定义新增表数据", notes = "传入insertTableMap,字段名为key,值为value(key为pyId的工序分类和key为checkType的检查类型必须传)")
    public R insertTable(@RequestBody Map<String, Object> insertTableMap) {
        if (insertTableMap == null) {
            return R.fail("传入的insertTableMap不能为null");
        }
        return R.status(qualityItemsetService.insertTable(insertTableMap));
    }

    @PostMapping("/updateTable")
    @ApiOperation(value = "修改自定义表数据", notes = "传入insertTableMap,字段名为key(key为pyId的工序分类和key为checkType的检查类型和key为id必须传),值为value")
    public R updateTable(@RequestBody Map<String, Object> insertTableMap) {
        if (insertTableMap == null) {
            return R.fail("传入的insertTableMap不能为null");
        }
        if (insertTableMap.get("id") == null) {
            return R.fail("修改时key为id的值不能为空");
        }
        Date date = new Date();
        insertTableMap.put("end_at", date);
        insertTableMap.put("report_status", 1);
        return R.status(qualityItemsetService.updateTable(insertTableMap));
    }

    @PostMapping("/addTableCols")
    @ApiOperation(value = "添加质检检查项", notes = "传入creatTableVO")
    public R addTableCols(@RequestBody AddColsVO addColsVO) {
        boolean status = qualityItemsetService.addTableCols(addColsVO);
        if (status) {
            return R.success("添加成功");
        }
        return R.fail("添加失败,请检查表是否创建或者是否存在重复字段");
    }

    @PostMapping("/processTree")
    @ApiOperation(value = "质量巡检检查工序树")
    public R processTree(){
        List<ProcessClassifyVo> processClassifyVos=qualityItemsetService.processList();
        if (!Func.isEmpty(processClassifyVos)) {
            processClassifyVos.forEach(proce -> {
                List<MachineMainfoVO> machins = qualityItemsetService.getMachins(proce.getId());
                proce.setMachines(machins);
            });
        }
       return R.data(processClassifyVos);
    }

    @PostMapping("/selectTable")
    @ApiOperation(value = "查询自定义新增表数据", notes = "传入pyId工序分类id,checkType检查类型, wfId")
    public R selectTable(@RequestBody QualityRequest qualityRequest) {
        String startTime = qualityRequest.getStartTime();
        if (StringUtil.isEmpty(startTime)) {
            qualityRequest.setStartTime(DateUtil.refNowDay());
        }
        String checkType = qualityRequest.getCheckType();//检查类型
        List<Integer> pyIds = qualityRequest.getPyIds();//工序分类id
        List<String> tableNames = qualityItemsetMapper.getInsertTableNames(pyIds, checkType);
        //String[] tableNameArry = (tableName != null && tableName.trim().length() > 0) ? tableName.trim().split(",") : null;
        if (tableNames == null || tableNames.isEmpty()) {
            return R.fail("该工序分类检查所需的表未建立");
        }
        return R.data(qualityItemsetService.selectTable(qualityRequest, Condition.getPage(qualityRequest)));
    }
    @GetMapping("/selectTableDetail")
    @ApiOperation(value = "查询自定义新增表数据详情", notes = "传入表名和id")
    public R selectTableDetail(@ApiParam("表名") @RequestParam(value = "tabName") String tabName,
                               @ApiParam("id")@RequestParam(value = "id") Integer id) {

        return R.data(qualityItemsetService.selectTableDetail(tabName, id));
    }
}
