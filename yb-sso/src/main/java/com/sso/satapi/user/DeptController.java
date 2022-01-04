//package com.oss.satapi.user;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
//import com.oss.base.service.IBaseStaffinfoService;
//import com.oss.entity.User;
//import com.oss.system.dept.service.SaIDeptService;
//import com.oss.system.dept.vo.SaDeptVO;
//import com.oss.system.dept.wrapper.DeptWrapper;
//import io.swagger.annotations.*;
//import lombok.AllArgsConstructor;
//import org.springblade.core.boot.ctrl.BladeController;
//import org.springblade.core.mp.support.Condition;
//import org.springblade.core.mp.support.Query;
//import org.springblade.core.secure.utils.SecureUtil;
//import org.springblade.core.tool.api.R;
//import org.springblade.core.tool.constant.BladeConstant;
//import org.springblade.core.tool.node.INode;
//import org.springblade.core.tool.utils.Func;
//import com.oss.entity.BaseDeptinfo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import springfox.documentation.annotations.ApiIgnore;
//
//import javax.validation.Valid;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 控制器
// *
// * @author Chill
// */
//@RestController
//@AllArgsConstructor
//@RequestMapping("/dept")
//@Api(value = "部门", tags = "部门")
//public class DeptController extends BladeController {
//
//    private SaIDeptService deptService;
//    @Autowired
//    private IBaseStaffinfoService baseStaffinfoService;
//
//    /**
//     * 详情
//     */
//    @GetMapping("/detail")
//    @ApiOperationSupport(order = 1)
//    @ApiOperation(value = "详情", notes = "传入dept")
//    public R<SaDeptVO> detail(BaseDeptinfo dept) {
//        BaseDeptinfo detail = deptService.getOne(Condition.getQueryWrapper(dept));
//        return R.data(DeptWrapper.build().entityVO(detail));
//    }
//
//    /**
//     * 列表
//     */
//    @GetMapping("/list")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "deptName", value = "部门名称", paramType = "query", dataType = "string"),
//            @ApiImplicitParam(name = "fullName", value = "部门全称", paramType = "query", dataType = "string")
//    })
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "列表", notes = "传入dept")
//    public R<List<INode>> list(@ApiIgnore @RequestParam Map<String, Object> dept, User bladeUser) {
//        bladeUser = SecureUtil.getUser();
//        QueryWrapper<BaseDeptinfo> queryWrapper = Condition.getQueryWrapper(dept, BaseDeptinfo.class);
//        List<BaseDeptinfo> list = deptService.list((!bladeUser.getTenantId().equals(BladeConstant.ADMIN_TENANT_ID)) ? queryWrapper.lambda().eq(BaseDeptinfo::getTenantId, bladeUser.getTenantId()) : queryWrapper);
//        return R.data(DeptWrapper.build().listNodeVO(list),"成功");
//    }
//
//    /**
//     * 获取部门树形结构
//     *
//     * @return
//     */
//    @GetMapping("/tree")
//    @ApiOperationSupport(order = 3)
//    @ApiOperation(value = "树形结构", notes = "树形结构")
//    public R<List<SaDeptVO>> tree(String tenantId, User bladeUser) {
//        bladeUser = SecureUtil.getUser();
//        List<SaDeptVO> tree = deptService.tree(Func.toStr(tenantId, bladeUser.getTenantId()));
//        return R.data(tree);
//    }
//
//    /**
//     * 新增或修改
//     */
//    @PostMapping("/submit")
//    @ApiOperationSupport(order = 4)
//    @ApiOperation(value = "新增或修改", notes = "传入dept")
//    public R submit(@Valid @RequestBody BaseDeptinfo dept, User user) {
//        if (Func.isEmpty(dept.getId())) {
//            dept.setTenantId(user.getTenantId());
//        }
//        return R.status(deptService.saveOrUpdate(dept));
//    }
//
//    /**
//     * 删除
//     */
//    /**
//     * 删除，将状态修改为停用
//     */
//    @PostMapping("/remove")
//    @ApiOperationSupport(order = 5)
//    @ApiOperation(value = "删除", notes = "传入ids")
//    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
//        String[] strings = ids.split(",");
//        Integer[] id = new Integer[strings.length];;
//        for (int a = 0; a < strings.length; a++){
//            id[a] = Integer.parseInt(strings[a]);
//        }
//        return R.status(baseStaffinfoService.updateStaffInfoIsUsedById(id));
//    }
//
//    /**
//     * 列表
//     */
//    @GetMapping("/lists")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "列表", notes = "传入current和size")
//    public R<IPage<BaseDeptinfo>> lists(Query query, @ApiIgnore @RequestParam() Map<String, Object> baseDeptinfo) {
//        BaseDeptinfo baseDeptinfo1 = new BaseDeptinfo();
//        if(!baseDeptinfo.isEmpty()){
//            for (Map.Entry<String,Object> entry: baseDeptinfo.entrySet()){
//                if(entry.getKey().equals("id")){
//                    baseDeptinfo1.setId(Integer.parseInt((String)entry.getValue()));
//                }
//                if(entry.getKey().equals("dpName")){
//                    baseDeptinfo1.setDpName(""+entry.getValue());
//                }
//            }
//        }
//        IPage<BaseDeptinfo> pages = deptService.selectDeptPages(Condition.getPage(query), baseDeptinfo1);
//        return R.data(pages,"成功");
//    }
//
//    /**
//     * 全部
//     */
//    @GetMapping("/listdepts")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "列表", notes = "")
//    public R<List<BaseDeptinfo>> listdepts(){
//        User Sauser = SecureUtil.getUser();
//        Map<String,Object> map = new HashMap<>();
//        map.put("is_deleted",0);
//        map.put("tenant_id",Sauser.getTenantId());
//        return R.data(deptService.listByMap(map));
//    }
//}
