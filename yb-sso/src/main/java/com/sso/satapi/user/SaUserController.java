package com.sso.satapi.user;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.sso.satapi.wrapper.UserWrapper;
import com.sso.system.entity.SaUser;
import com.sso.system.service.SaIUserService;
import com.sso.system.vo.SaUserVO;
import com.sso.utils.SaSecureUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.Func;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 控制器
 *
 * @author Chill
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class SaUserController {

    private SaIUserService userService;

    /**
     * 查询单条
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "查看详情", notes = "传入id")
    @GetMapping("/detail")
    public R<SaUserVO> detail(SaUser user) {
        SaUser detail = userService.getOne(Condition.getQueryWrapper(user));
        return R.data(UserWrapper.build().entityVO(detail));
    }

    /**
     * 查询单条
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "查看详情", notes = "传入id")
    @GetMapping("/info")
    public R<SaUserVO> info(SaUser user) {
        SaUser detail = userService.getOneUserById(user.getId());
        return R.data(UserWrapper.build().entityVO(detail));
    }

    /**
     * 用户列表
     */
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号名", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "realName", value = "姓名", paramType = "query", dataType = "string")
    })
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "列表", notes = "传入account和realName")
    public R<IPage<SaUserVO>> list(@ApiIgnore @RequestParam Map<String, Object> user, Query query, SaUser bladeUser) {
        bladeUser = SaSecureUtil.getUser();
        QueryWrapper<SaUser> queryWrapper = Condition.getQueryWrapper(user, SaUser.class);
        IPage<SaUser> pages = userService.page(Condition.getPage(query), (!bladeUser.getTenantId().equals(BladeConstant.ADMIN_TENANT_ID)) ? queryWrapper.lambda().eq(SaUser::getTenantId, bladeUser.getTenantId()) : queryWrapper);
        return R.data(UserWrapper.build().pageVO(pages));
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增或修改", notes = "传入User")
    public R submit(@Valid @RequestBody SaUser user) {
        return R.status(userService.submit(user));
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入User")
    public R update(@Valid @RequestBody SaUser user) {
        return R.status(userService.updateById(user));
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "删除", notes = "传入ids")
    @Transactional
    public R remove(@RequestParam String ids) {
        try {
            String[] strings = ids.split(",");
            for (int a = 0; a < strings.length; a++){
                SaUser saUser = new SaUser();
                saUser.setId(Integer.parseInt(strings[a]));
                saUser.setRoleId("");
                userService.updateOoleId(saUser);
            }
            return R.status(true);
        }catch (Exception e){
            e.printStackTrace();
            //设置手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.fail("移除失败");
        }
    }


    /**
     * 设置菜单权限
     *
     * @param userIds
     * @param roleIds
     * @return
     */
    @PostMapping("/grant")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "权限设置", notes = "传入roleId集合以及menuId集合")
    public R grant(@ApiParam(value = "userId集合", required = true) @RequestParam String userIds,
                   @ApiParam(value = "roleId集合", required = true) @RequestParam String roleIds) {
        boolean temp = userService.grant(userIds, roleIds);
        return R.status(temp);
    }

    @PostMapping("/reset-password")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "初始化密码123456", notes = "传入userId集合")
    public R resetPassword(@ApiParam(value = "userId集合", required = true) @RequestParam String userIds) {
        boolean temp = userService.resetPassword(userIds);
        return R.status(temp);
    }

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param newPassword1
     * @return
     */
    @PostMapping("/update-password")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "修改密码", notes = "传入密码")
    public R updatePassword(SaUser user, @ApiParam(value = "旧密码", required = true) @RequestParam String oldPassword,
                            @ApiParam(value = "新密码", required = true) @RequestParam String newPassword,
                            @ApiParam(value = "新密码", required = true) @RequestParam String newPassword1) {
        boolean temp = userService.updatePassword(user.getUserId(), oldPassword, newPassword, newPassword1);
        return R.status(temp);
    }

    /**
     * 用户列表
     *
     * @param user
     * @return
     */
    @GetMapping("/user-list")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "用户列表", notes = "传入user")
    public R<List<SaUser>> userList(SaUser user) {
        List<SaUser> list = userService.list(Condition.getQueryWrapper(user));
        return R.data(list);
    }
    /**
     * 用户列表
     *
     * @param user
     * @return
     */
    @GetMapping("/getUserList")
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "用户列表", notes = "传入user")
    public R<IPage<SaUser>> getUserList(Query query, @ApiIgnore @RequestParam() Map<String, Object> user) {
        SaUser bladeUser = SaSecureUtil.getUser();
        SaUser saUser = new SaUser();
        saUser.setTenantId(bladeUser.getTenantId());
        if (!user.isEmpty()){
            for (Map.Entry<String, Object> entry : user.entrySet()){
                if (entry.getKey().equals("realName")){
                    saUser.setRealName(entry.getValue()+"");
                }
            }
        }
        IPage<SaUser> pages = userService.getUserList(Condition.getPage(query), saUser);
        return R.data(pages);
    }
    /**
     * 给人员设置角色
     * @param saUser
     * @return
     */
    @PostMapping("/grants")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "权限设置", notes = "传入saUser")
    public R grants(@Valid @RequestBody SaUser saUser) {
        if (saUser.getId() == null){
//            查询出修改用户信息
            SaUser detail = userService.getOneUserCondition(saUser);
            if(Func.isEmpty(detail)){
                return R.fail("修改失败，请确认输入用户信息是否存在");
            }else{
                detail.setRoleId(saUser.getRoleId());
                return R.status(userService.updateOoleId(detail));
            }
        }else {
//            修改操作时
            return R.status(userService.updateOoleId(saUser));
        }
    }
}
