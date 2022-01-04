package com.yb.stroe.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.stroe.entity.StoreInlog;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.entity.StoreOutlog;
import com.yb.stroe.service.IStoreInlogService;
import com.yb.stroe.service.IStoreInventoryService;
import com.yb.stroe.service.IStoreOutlogService;
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
 * 库位出库管理_yb_store_outlog 控制层
 *
 * @author lzb
 * @date 2020-09-19
 */
@RestController
@Api(tags = "store-库位出库管理")
@RequestMapping("_store_outlog")
public class StoreOutlogController {

    @Autowired
    private IStoreOutlogService storeOutlogService;

    @ApiOperation(value = "分页查询")
    @GetMapping(value = "/list")
    public R<?> queryPageList(StoreOutlog storeOutlog,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest req) {
        QueryWrapper<StoreOutlog> queryWrapper = QueryGenerator.initQueryWrapper(storeOutlog, req.getParameterMap());
        Page<StoreOutlog> page = new Page<StoreOutlog>(pageNo, pageSize);
        IPage<StoreOutlog> pageList = storeOutlogService.page(page, queryWrapper);
        return R.data(pageList);
    }

    @ApiOperation(value = "根据订单编号和工序查询")
    @GetMapping("getByWbNoPrId")
    public R<List<StoreOutlog>> getByWbNoPrId(@ApiParam(value = "订单编号") String wbNo, @ApiParam(value = "工序id") Integer prId) {
        List<StoreOutlog> list = storeOutlogService.getByWbNoPrId(wbNo, prId);
        return R.data(list);
    }

    @ApiOperation(value = "编辑")
    @PutMapping(value = "/edit")
    public R<?> edit(@RequestBody StoreOutlog storeOutlog) {
        storeOutlogService.updateById(storeOutlog);
        return R.success("编辑成功!");
    }

    @ApiOperation(value = "添加")
    @PostMapping(value = "/add")
    public R<?> add(@RequestBody StoreOutlog storeOutlog) {
        storeOutlogService.save(storeOutlog);
        return R.success("添加成功！");
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    public R<?> delete(@RequestParam(name = "id", required = true) Long id) {
        storeOutlogService.removeById(id);
        return R.success("删除成功!");
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public R<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.storeOutlogService.removeByIds(Arrays.asList(ids.split(",")));
        return R.success("批量删除成功!");
    }

    @ApiOperation(value = "通过id查询")
    @GetMapping(value = "/queryById")
    public R<?> queryById(@RequestParam(name = "id", required = true) Long id) {
        StoreOutlog storeOutlog = storeOutlogService.getById(id);
        return R.data(storeOutlog);
    }

}
