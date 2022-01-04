package com.yb.system.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.fastdfs.FileSystem;
import com.yb.fastdfs.controller.FileController;
import com.yb.system.user.entity.SaUser;
import com.yb.system.user.request.*;
import com.yb.system.user.response.SysUserPageVO;
import com.yb.system.user.service.SaIUserService;
import com.yb.system.user.service.SystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description: 新系统管理接口
 * @Author my
 * @Date Created in 2020/10/21 10:57
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/sysUser")
@Api(tags = "新系统管理接口")
public class SystemUserController extends BladeController {

    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SaIUserService saIUserService;
    @Autowired
    private FileController fileController;

    @PostMapping("/page")
    @ApiOperation(value = "分页")
    public R<IPage<SysUserPageVO>> page(@RequestBody UserListRequest request) {

//        bladeUser = SaSecureUtil.getUser();
        IPage<SysUserPageVO> page = systemUserService.page(Condition.getPage(request), request);

        return R.data(page);
    }


    @PostMapping("/saveAndUpdate")
    @ApiOperation(value = "新增/修改")
    public R saveAndUpdate(@RequestBody @Validated SaveAndUpdateRequest request) {

        systemUserService.saveAndUpdateRequest(request);

        return R.success("ok");
    }

    @PostMapping("/roleAuthor")
    @ApiOperation(value = "角色授权")
    public R roleAuthor(@RequestBody @Validated RoleAuthorRequest request) {

        systemUserService.roleAuthor(request);

        return R.success("角色授权成功");
    }

    @PostMapping("/resetPassword")
    @ApiOperation(value = "重置密码")
    public R resetPassword(@RequestBody @Validated ResetPasswordRequest request) {

        systemUserService.resetPassword(request.getId(), request.getPassword());

        return R.success("重置密码成功");
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除")
    public R delete(@RequestBody @Validated UserDeleteRequest request) {

        systemUserService.delete(request.getIds());

        return R.success("删除成功");
    }

    @PostMapping("/updateAvatar")
    @ApiOperation(value = "修改用户头像", notes = "传入用户id和图片路径即可")
    public R updateAvatar(@RequestParam("file") MultipartFile file, @RequestParam("id")Integer id) {
        if(id == null){
            return R.fail("传入用户id不能为空");
        }
        SaUser saUser = saIUserService.getBaseMapper().selectById(id);
        if(saUser == null){
            return R.fail("传入的id未找到相应的用户");
        }
        String avatar = saUser.getAvatar();
        if (!StringUtil.isEmpty(avatar)) {//如果存在则删除
            int index = avatar.indexOf("/");
            try {
                fileController.deleteFile(avatar.substring(0, index), avatar.substring(index + 1));
            } catch (IOException e) {
                log.error("删除图片库时出错");
                e.printStackTrace();
            }
        }
        FileSystem data = null;
        try {
            data = fileController.upload(file).getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(data == null){
            log.error("保存图片时返回的路径对象不存在");
            return R.fail("头像保存失败");
        }
        saUser.setAvatar(data.getFilePath());
        try {
            saIUserService.updateById(saUser);
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("头像保存失败");
        }
        return R.success("头像保存成功");
    }
}
