package com.yb.stroe.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.stroe.entity.StoreInlog;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.service.IStoreInventoryService;
import com.yb.stroe.utils.QueryGenerator;
import com.yb.stroe.vo.InInventoryByNumberVO;
import com.yb.stroe.vo.SeatInventoryVO;
import com.yb.stroe.vo.StoreInventorySemiVO;
import com.yb.stroe.vo.StoreInventoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 仓库的台账_yb_store_inventory 控制层
 *
 * @author lzb
 * @date 2020-09-19
 */
@RestController
@Api(tags = "store-仓库台账")
@RequestMapping("store_inventory")
public class StoreInventoryController {

    @Autowired
    private IStoreInventoryService storeInventoryService;

    @ApiOperation(value = "分页查询")
    @GetMapping(value = "/list")
    public R<?> queryPageList(StoreInventory storeInventory,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest req) {
        QueryWrapper<StoreInventory> queryWrapper = QueryGenerator.initQueryWrapper(storeInventory, req.getParameterMap());
        Page<StoreInventory> page = new Page<>(pageNo, pageSize);
        IPage<StoreInventory> pageList = storeInventoryService.page(page, queryWrapper);
        return R.data(pageList);
    }

    /**
     * 分页 生产执行上报信息_yb_execute_briefer
     */
    @GetMapping("/listcs")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入storeInventoryvo")
    public R<IPage<StoreInventorySemiVO>> listcs(Query query, StoreInventoryVO storeInventoryVO) {
        Integer courrent = (query.getCurrent() - 1) * query.getSize();
        IPage<StoreInventorySemiVO> page = storeInventoryService.pageStoreFindList(courrent, query.getSize(), storeInventoryVO);
        return R.data(page);
    }

    @ApiOperation(value = "编辑")
    @PutMapping(value = "/edit")
    public R<?> edit(@RequestBody StoreInventory storeInventory) {
        storeInventoryService.updateById(storeInventory);
        return R.success("编辑成功");
    }

    @ApiOperation(value = "添加")
    @PostMapping(value = "/add")
    public R<?> add(@RequestBody StoreInventory storeInventory) {
        storeInventoryService.save(storeInventory);
        return R.success("添加成功！");
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    public R<?> delete(@RequestParam(name = "id", required = true) Long id) {
        storeInventoryService.removeById(id);
        return R.success("删除成功!");
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public R<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        storeInventoryService.removeByIds(Arrays.asList(ids.split(",")));
        return R.success("批量删除成功!");
    }

    @ApiOperation(value = "通过id查询")
    @GetMapping(value = "/queryById")
    public R<?> queryById(@RequestParam(name = "id", required = true) Long id) {
        StoreInventory storeInventory = storeInventoryService.getById(id);
        return R.data(storeInventory);
    }

    @ApiOperation(value = "入库")
    @GetMapping(value = "/putStore")
    public R<?> putStore(@ApiParam(value = "库位id", required = true) @RequestParam Integer stId,
                         @ApiParam(value = "托板id", required = true) @RequestParam Integer trayId,
                         @ApiParam(value = "存储类型：1、半成品2、成品 3、原料4、辅料5、备品备件", required = true) @RequestParam Integer stType,
                         @ApiParam(value = "标识卡id集合，多个用逗号隔开,非必传，重新打印标识卡时需要传入上次打印的标识卡") @RequestParam(required = false) String etIds) {
        boolean b = storeInventoryService.putStore(stId, trayId, stType, etIds);
        if (b) {
            return R.success("入库成功");
        }
        return R.fail("入库失败");
    }

    @ApiOperation(value = "出库")
    @GetMapping(value = "/outStore")
    public R<?> outStore(@ApiParam(value = "托板标识id集合，多个用逗号隔开") @RequestParam(name = "trayIds") String trayIds,
                         @ApiParam(value = "用户id") @RequestParam(name = "usId") Integer usId) {
        boolean b = storeInventoryService.outStore(trayIds, usId);
        if (b) {
            return R.success("出库成功！");
        }
        return R.fail("出库失败！");
    }

    @ApiOperation(value = "盘点")
    @GetMapping("check")
    public R<?> check(@ApiParam(value = "托板标识卡id") @RequestParam Integer trayId,
                      @ApiParam(value = "当前数量") @RequestParam Integer currentNum,
                      @ApiParam(value = "当前库位id") @RequestParam Integer currentSeatId,
                      @ApiParam(value = "当前位置编码") @RequestParam String currentLocal) {
        storeInventoryService.check(trayId, currentNum, currentSeatId, currentLocal);
        return R.success("盘点成功");
    }

    @ApiOperation(value = "根据库区id获取库位的台账已经占用及剩余托板位置")
    @GetMapping("/seatInventoryInfo")
    public R<List<SeatInventoryVO>> seatInventoryInfo(@ApiParam(value = "库区id，如果为空就查所有") @RequestParam(required = false) Integer areaId) {
        List<SeatInventoryVO> list = storeInventoryService.seatInventoryInfo(areaId);
        return R.data(list);
    }

    @ApiOperation(value = "根据库位id获取当前库位后面的连续库位台账已经占用及剩余托板位置")
    @GetMapping("/seatInventoryInfoBySort")
    public R<List<SeatInventoryVO>> seatInventoryInfoBySort(@ApiParam(value = "库位id") @RequestParam Integer seatId) {
        List<SeatInventoryVO> list = storeInventoryService.seatInventoryInfoBySort(seatId);
        return R.data(list);
    }

    @ApiOperation(value = "重新生成标识卡删除台账(根据标识卡批量删除)")
    @GetMapping("/deleteBatchByetIds")
    public R<?> deleteBatchByetIds(@ApiParam(value = "标识卡ids（逗号隔开）")
                                   @RequestParam(name = "etIds") String etIds) {
        List<Integer> integers = Func.toIntList(etIds);
        storeInventoryService.deleteBatchByetIds(integers);
        return R.success("操作成功");
    }

    @ApiOperation(value = "手机盘点-根据库区id获取库位托盘信息")
    @GetMapping("/seatInventoryInfoAll")
    public R<List<SeatInventoryVO>> seatInventoryInfoAll(@ApiParam(value = "库区id") @RequestParam Integer areaId) {
        List<SeatInventoryVO> list = storeInventoryService.seatInventoryInfo1(areaId);
        return R.data(list);
    }

    @GetMapping("storeExcelExport")
    @ApiOperation("库存导出")
    public R storeExcelExport() {
        Integer i = storeInventoryService.storeExcelExport();
        if(i == null){
            return R.fail("没有数据可导出");
        }
        return null;
    }

    @GetMapping("outInventory")
    @ApiOperation("根据工单id和数量出库")
    public R outInventory(@ApiParam("工单id") @RequestParam Integer sdId, @ApiParam("出库数量") @RequestParam Integer number) {
        storeInventoryService.outInventory(sdId, number);
        return R.success("出库成功");
    }

    @GetMapping("inInventoryByNumber")
    @ApiOperation("根据数量入库")
    public R inInventoryByNumber(InInventoryByNumberVO parames) {
        storeInventoryService.inInventoryByNumber(parames);
        return R.success("入库成功");
    }



}
