package com.yb.stroe.controller;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.stroe.entity.StoreInlog;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.service.IStoreInlogService;
import com.yb.stroe.service.IStoreInventoryService;
import com.yb.stroe.utils.QueryGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 库位入库管理_yb_store_inlog 控制层
 *
 * @author lzb
 * @date 2020-09-19
 */
@RestController
@Api(tags = "store-库位入库管理")
@RequestMapping("_store_inlog")
public class StoreInlogController {

    @Autowired
    private IStoreInlogService storeInlogService;

    @ApiOperation(value = "分页查询")
    @GetMapping(value = "/list")
    public R<?> queryPageList(StoreInlog storeInlog,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest req) {
        QueryWrapper<StoreInlog> queryWrapper = QueryGenerator.initQueryWrapper(storeInlog, req.getParameterMap());
        Page<StoreInlog> page = new Page<StoreInlog>(pageNo, pageSize);
        IPage<StoreInlog> pageList = storeInlogService.page(page, queryWrapper);
        return R.data(pageList);
    }

    @ApiOperation(value = "根据订单编号和工序查询")
    @GetMapping("getByWbNoPrId")
    public R<List<StoreInlog>> getByWbNoPrId(@ApiParam(value = "订单编号") String wbNo, @ApiParam(value = "工序id") Integer prId) {
        List<StoreInlog> list = storeInlogService.getByWbNoPrId(wbNo, prId);
        return R.data(list);
    }

    @ApiOperation(value = "编辑")
    @PutMapping(value = "/edit")
    public R<?> edit(@RequestBody StoreInlog storeInlog) {
        storeInlogService.updateById(storeInlog);
        return R.success("编辑成功!");
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    public R<?> delete(@RequestParam(name = "id", required = true) Long id) {
        storeInlogService.removeById(id);
        return R.success("删除成功!");
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public R<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.storeInlogService.removeByIds(Arrays.asList(ids.split(",")));
        return R.success("批量删除成功!");
    }

    @ApiOperation(value = "通过id查询")
    @GetMapping(value = "/queryById")
    public R<?> queryById(@RequestParam(name = "id", required = true) Long id) {
        StoreInlog storeInlog = storeInlogService.getById(id);
        return R.data(storeInlog);
    }

}
