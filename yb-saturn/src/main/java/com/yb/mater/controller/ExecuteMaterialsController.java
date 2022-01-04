package com.yb.mater.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.service.ExecuteTraycardService;
import com.yb.mater.entity.ExecuteMaterials;
import com.yb.mater.entity.ExecuteOffmater;
import com.yb.mater.service.IExecuteMaterialsService;
import com.yb.mater.service.IExecuteOffmaterService;
import com.yb.mater.vo.ExecuteMaterialsVO;
import com.yb.mater.vo.HrMaterial;
import com.yb.prod.mapper.ProdProcelinkMapper;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.service.impl.StoreInventoryServiceImpl;
import com.yb.synchrodata.service.impl.NxhrMaterialService;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.vo.WorkbatchOrdlinkShiftVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 控制层
 *
 * @author lzb
 * @date 2021-01-10
 */
@RestController
@Api(tags = "物料控制器")
@RequestMapping("materials")
public class ExecuteMaterialsController {

    @Autowired
    private IExecuteMaterialsService executeMaterialsService;
    @Autowired
    private NxhrMaterialService nxhrMaterialService;
    @Autowired
    private ExecuteTraycardService executeTraycardService;
    @Autowired
    private StoreInventoryServiceImpl storeInventoryService;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private ProdProcelinkMapper prodProcelinkMapper;
    @Autowired
    private IExecuteOffmaterService executeOffmaterService;


    @ApiOperation(value = "根据工单id查询上料信息")
    @GetMapping(value = "/list")
    public R<?> queryPageList(ExecuteMaterials executeMaterials) {
        List<ExecuteMaterialsVO> list = executeMaterialsService.getListByWfId(executeMaterials.getWfId());
        return R.data(list);
    }

    @ApiOperation(value = "编辑")
    @PutMapping(value = "/edit")
    public R<?> edit(@RequestBody ExecuteMaterials executeMaterials) {
        executeMaterialsService.updateById(executeMaterials);
        return R.success("编辑成功!");
    }

    @ApiOperation(value = "上料保存操作")
    @PostMapping(value = "/add")
    public R<?> add(@RequestBody ExecuteMaterials executeMaterials) {
        // mes出库
        if (null != executeMaterials.getEtId()) {
            ExecuteTraycard executeTraycard = executeTraycardService.getById(executeMaterials.getEtId());
            executeTraycard.setTyStatus(2);
            executeTraycardService.updateById(executeTraycard);
            storeInventoryService.outStore(executeMaterials.getEtId().toString(), executeMaterials.getUsId());
        }
//        if (executeMaterials.getBarCode().contains("NXWL-")) {
        ExecuteOffmater one = executeOffmaterService.getOne(Wrappers.<ExecuteOffmater>lambdaQuery().eq(ExecuteOffmater::getBarCode, executeMaterials.getBarCode()));
        if (null != one) {
            one.setStatus(2);
            executeOffmaterService.updateById(one);
        }
//        }
        executeMaterialsService.save(executeMaterials);
        return R.success("添加成功！");
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    public R<?> delete(@RequestParam(name = "id") Long id) {
        executeMaterialsService.removeById(id);
        return R.success("删除成功!");
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public R<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        executeMaterialsService.removeByIds(Arrays.asList(ids.split(",")));
        return R.success("批量删除成功!");
    }

    @ApiOperation(value = "通过id查询")
    @GetMapping(value = "/queryById")
    public R<?> queryById(@RequestParam(name = "id") Long id) {
        ExecuteMaterials executeMaterials = executeMaterialsService.getById(id);
        return R.data(executeMaterials);
    }

//    @ApiOperation("根据物料条形码添加物料")
//    @GetMapping("savaByBarCode")
//    public R<?> savaByBarCode(
//            @ApiParam(value = "条形码") @RequestParam(value = "barCode") String barCode,
//            @ApiParam(value = "设备id") @RequestParam(value = "maId") Integer maId,
//            @ApiParam(value = "排程单id") @RequestParam(value = "sfId") Integer sfId,
//            @ApiParam(value = "班次信息id") @RequestParam(value = "wfId") Integer wfId,
//            @ApiParam(value = "操作用户id") @RequestParam(value = "usId") Integer usId) {
//        return R.data(executeMaterialsService.savaByBarCode(barCode, maId, sfId, wfId, usId));
//    }

    @ApiOperation("根据物料条形码删除物料")
    @GetMapping("delByBarCode")
    public R<?> delByBarCode(String barCode) {
        executeMaterialsService.remove(Wrappers.<ExecuteMaterials>lambdaQuery().eq(ExecuteMaterials::getId, barCode));
        return R.success("删除成功");
    }

    @ApiOperation("根据条形码查询物料信息（erp或托盘）")
    @GetMapping("getMaterial")
    public R<ExecuteMaterials> getMaterialBybarCode(
            @ApiParam(value = "条形码") @RequestParam(value = "barCode") String barCode,
            @ApiParam(value = "操作人id") @RequestParam(value = "usId") Integer usId,
            @ApiParam(value = "排产单id") @RequestParam(value = "wfId") Integer wfId,
            @ApiParam(value = "班次id") @RequestParam(value = "wsId") Integer wsId,
            @ApiParam(value = "设备id") @RequestParam(value = "maId") Integer maId) {

        Integer cnum = executeMaterialsService.getBarCodeByExists(barCode);
        if (cnum > 0) {
            return R.fail("该物料已经使用，不能重复上料。");
        }


        ExecuteMaterialsVO materials = new ExecuteMaterialsVO();
        materials.setBarCode(barCode);
        materials.setMaId(maId);
        materials.setWsId(wsId);
        materials.setWfId(wfId);
        materials.setUsId(usId);
        // 如果是宁夏物料退料
        if (barCode.contains("NXWL-")) {
            ExecuteOffmater one = executeOffmaterService.getOne(Wrappers.<ExecuteOffmater>lambdaQuery().eq(ExecuteOffmater::getBarCode, barCode));
            if (2 == one.getStatus()) {
                return R.fail("宁夏物料无库存");
            }
            materials.setMatName(one.getMatName());
            materials.setMatNum(one.getMatNum());
            materials.setTotalNum(one.getTotalNum());
            materials.setEtId(one.getEtId());
            materials.setSeatId(one.getSeatId());
            materials.setMaterialStatus(1); //物料状态：1当前工单的上工序物料，2非当前工单上工序物料
            return R.data(materials);
        }
        // 如果是托盘则返回托盘信息
        if (barCode.contains("NXHR-")) {
            ExecuteTraycard executeTraycard = executeTraycardService.selectByTdNo(barCode);
            if (null != executeTraycard) {
                StoreInventory one = storeInventoryService.getOne(Wrappers.<StoreInventory>lambdaQuery().eq(StoreInventory::getEtId, executeTraycard.getId()));
                if (null == one) {
                    return R.fail("该托盘不存在，请核对后进行上料添加。");
                }
            }
            //判断托盘不存在，无法进行托盘上料操作
            if (executeTraycard != null) {
                WorkbatchShift currentShift = workbatchShiftMapper.selectById(wfId);
                WorkbatchShift lastShift = workbatchShiftMapper.selectById(executeTraycard.getWfId());
                WorkbatchOrdlinkShiftVO w1 = workbatchOrdlinkMapper.getOrdshiftById(currentShift.getSdId());
                WorkbatchOrdlinkShiftVO w2 = workbatchOrdlinkMapper.getOrdshiftById(lastShift.getSdId());
                if (!w1.getOdNo().equals(w2.getOdNo())) {
                    // 所上物料所属于订单与当前生产不是同一个订单
                    materials.setMaterialStatus(2);
                }
                materials.setTrayNo(executeTraycard.getTrayNo());
                materials.setMatName(executeTraycard.getPrName());
                materials.setMatNum(executeTraycard.getTrayNum());
                materials.setTotalNum(executeTraycard.getTrayNum());
                materials.setEtId(executeTraycard.getId());
                return R.data(materials);
            }
        }
        // 如果是erp条码则返回erp物料信息
        HrMaterial hrMaterial = nxhrMaterialService.selectByBarCode(barCode);
        if (null == hrMaterial) {
            return R.fail("物料不存在");
        }
        int total = Integer.parseInt(hrMaterial.getStockMeter().setScale(0, BigDecimal.ROUND_UP).toString());
        materials.setMatName(hrMaterial.getDescription());
        materials.setMatNum(total);
        materials.setTotalNum(total);
        materials.setMatId(hrMaterial.getErpId());
        return R.data(materials);
    }
}
