package com.yb.stroe.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.stroe.entity.StoreMorelog;
import com.yb.stroe.service.IStoreMorelogService;
import com.yb.stroe.utils.QueryGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 盘盈管理_yb_store_morelog 控制层
 *
 * @author lzb
 * @date 2020-09-22
 */
@RestController
@Api(tags = "store-盘点盘盈管理")
@RequestMapping("_store_morelog")
public class StoreMorelogController {

    @Autowired
    private IStoreMorelogService storeMorelogService;

    @ApiOperation(value = "分页查询")
    @GetMapping(value = "/list")
    public R<?> queryPageList(StoreMorelog storeMorelog,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest req) {
        QueryWrapper<StoreMorelog> queryWrapper = QueryGenerator.initQueryWrapper(storeMorelog, req.getParameterMap());
        Page<StoreMorelog> page = new Page<StoreMorelog>(pageNo, pageSize);
        IPage<StoreMorelog> pageList = storeMorelogService.page(page, queryWrapper);
        return R.data(pageList);
    }

    @ApiOperation(value = "编辑")
    @PutMapping(value = "/edit")
    public R<?> edit(@RequestBody StoreMorelog storeMorelog) {
        storeMorelogService.updateById(storeMorelog);
        return R.success("编辑成功!");
    }

    @ApiOperation(value = "添加")
    @PostMapping(value = "/add")
    public R<?> add(@RequestBody StoreMorelog storeMorelog) {
        storeMorelogService.save(storeMorelog);
        return R.success("添加成功！");
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    public R<?> delete(@RequestParam(name = "id") Long id) {
        storeMorelogService.removeById(id);
        return R.success("删除成功!");
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public R<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.storeMorelogService.removeByIds(Arrays.asList(ids.split(",")));
        return R.success("批量删除成功!");
    }

    @ApiOperation(value = "通过id查询")
    @GetMapping(value = "/queryById")
    public R<?> queryById(@RequestParam(name = "id") Long id) {
        StoreMorelog storeMorelog = storeMorelogService.getById(id);
        return R.data(storeMorelog);
    }

}
