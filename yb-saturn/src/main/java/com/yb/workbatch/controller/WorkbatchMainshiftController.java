package com.yb.workbatch.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.stroe.utils.QueryGenerator;
import com.yb.system.user.entity.SaUser;
import com.yb.workbatch.entity.WorkbatchMainshift;
import com.yb.workbatch.service.IWorkbatchMainshiftService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 排班名称yb_workbatch_mainshift 控制层
 *
 * @author lzbstatisOrdreach
 * @date 2020-09-28
 */
@RestController
@Api(tags = "新班次管理接口")
@RequestMapping("workbatch_mainshift")
public class WorkbatchMainshiftController {

    @Autowired
    private IWorkbatchMainshiftService workbatchMainshiftService;

    @ApiOperation(value = "分页查询")
    @GetMapping(value = "/list")
    public R<?> queryPageList(WorkbatchMainshift workbatchMainshift,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                              HttpServletRequest req) {
        SaUser user = SaSecureUtil.getUser();
        QueryWrapper<WorkbatchMainshift> queryWrapper = QueryGenerator.initQueryWrapper(workbatchMainshift, req.getParameterMap()).orderByAsc("create_at");
        Page<WorkbatchMainshift> page = new Page<WorkbatchMainshift>(pageNo, pageSize);
        IPage<WorkbatchMainshift> pageList = workbatchMainshiftService.page(page, queryWrapper);
        List<WorkbatchMainshift> records = pageList.getRecords();
        if (!records.isEmpty()) {
            records.forEach(o -> {
                o.setUserName(user.getRealName());
            });
        }
        pageList.setRecords(records);
        return R.data(pageList);
    }

    @ApiOperation(value = "编辑")
    @PostMapping(value = "/edit")
    public R<?> edit(@RequestBody WorkbatchMainshift workbatchMainshift) {
        workbatchMainshiftService.updateById(workbatchMainshift);
        return R.success("编辑成功!");
    }

    @ApiOperation(value = "添加")
    @PostMapping(value = "/add")
    public R<?> add(@RequestBody WorkbatchMainshift workbatchMainshift) {
        workbatchMainshift.setCreateAt(new Date());
        workbatchMainshift.setIsUsed(workbatchMainshift.getIsUsed());
        workbatchMainshiftService.save(workbatchMainshift);
        return R.success("添加成功！");
    }

    @ApiOperation(value = "启用停用")
    @GetMapping(value = "/changeStatus")
    public R changeStatus(@ApiParam(value = "需要变更的id", required = true) Integer id) {

        workbatchMainshiftService.changeStatus(id);

        return R.success("启用/停用成功!");
    }

    @ApiOperation(value = "批量删除")
    @PostMapping(value = "/deleteBatch")
    public R<?> deleteBatch(@ApiParam("id集合") @RequestBody List<Integer> ids) {
        if (!ids.isEmpty()) {
            this.workbatchMainshiftService.removeByIds(ids);
        }
        return R.success("批量删除成功!");
    }

    @ApiOperation(value = "通过id查询")
    @GetMapping(value = "/queryById")
    public R<?> queryById(@RequestParam(name = "id") Integer id) {
        WorkbatchMainshift workbatchMainshift = workbatchMainshiftService.getById(id);
        return R.data(workbatchMainshift);
    }


    @ApiOperation(value = "获取有效的班次列表")
    @GetMapping(value = "/validList")
    public R<?> validList() {
        List<WorkbatchMainshift> vos = workbatchMainshiftService.validList();
        return R.data(vos);
    }
}
