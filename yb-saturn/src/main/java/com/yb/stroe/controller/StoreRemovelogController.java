package com.yb.stroe.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.stroe.entity.StoreRemovelog;
import com.yb.stroe.service.IStoreRemovelogService;
import com.yb.stroe.utils.QueryGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 盘缺管理_yb_store_removelog 控制层
 *
 * @author lzb
 * @date 2020-09-22
 */
@RestController
@Api(tags = "store-盘点盘缺管理")
@RequestMapping("_store_removelog")
public class StoreRemovelogController {

    @Autowired
    private IStoreRemovelogService storeRemovelogService;

    @ApiOperation(value = "分页查询")
    @GetMapping(value = "/list")
    public R<?> queryPageList(StoreRemovelog storeRemovelog,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest req) {
        QueryWrapper<StoreRemovelog> queryWrapper = QueryGenerator.initQueryWrapper(storeRemovelog, req.getParameterMap());
        Page<StoreRemovelog> page = new Page<StoreRemovelog>(pageNo, pageSize);
        IPage<StoreRemovelog> pageList = storeRemovelogService.page(page, queryWrapper);
        return R.data(pageList);
    }

    @ApiOperation(value = "编辑")
    @PutMapping(value = "/edit")
    public R<?> edit(@RequestBody StoreRemovelog storeRemovelog) {
        storeRemovelogService.updateById(storeRemovelog);
        return R.success("编辑成功!");
    }

    @ApiOperation(value = "添加")
    @PostMapping(value = "/add")
    public R<?> add(@RequestBody StoreRemovelog storeRemovelog) {
        storeRemovelogService.save(storeRemovelog);
        return R.success("添加成功！");
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    public R<?> delete(@RequestParam(name = "id") Long id) {
        storeRemovelogService.removeById(id);
        return R.success("删除成功!");
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public R<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.storeRemovelogService.removeByIds(Arrays.asList(ids.split(",")));
        return R.success("批量删除成功!");
    }

    @ApiOperation(value = "通过id查询")
    @GetMapping(value = "/queryById")
    public R<?> queryById(@RequestParam(name = "id") Long id) {
        StoreRemovelog storeRemovelog = storeRemovelogService.getById(id);
        return R.data(storeRemovelog);
    }

}
