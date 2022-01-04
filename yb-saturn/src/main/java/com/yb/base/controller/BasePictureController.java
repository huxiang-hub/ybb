package com.yb.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.base.entity.BasePicture;
import com.yb.base.service.BasePictureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/BasePicture")
@Api(tags = "图片属性管理")
public class BasePictureController {

    @Autowired
    private BasePictureService basePictureService;

    /**
     * 保存图片上传相关信息
     *
     * @param basePicture
     * @return
     */
    @PostMapping("saveImage")
    @ApiOperation(value = "保存上传图片信息", notes = "传入basePicture")
    public R saveImage(BasePicture basePicture) {
        Integer userId = SaSecureUtil.getUserId();
        basePicture.setUsId(userId);
        Date date = new Date();
        basePicture.setUpdateAt(date);
        basePicture.setCreateAt(date);
        return R.status(basePictureService.save(basePicture));
    }

    /**
     * 查询所需图片路径
     *
     * @param btType 类型
     * @param btId   对应表的主键id
     * @return
     */
    @GetMapping("getImageUrl")
    @ApiOperation(value = "查询图片信息", notes = "传入btType, btId")
    public R getImageUrl(@ApiParam(value = "保存的图片类型 1、头像2、首检图片3、巡检图片btType 4、库区图片") String btType,
                         @ApiParam(value = "保存的图片属性表的主键id") Integer btId) {
        QueryWrapper<BasePicture> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bt_type", btType)
                .eq("bt_id", btId);
        return R.data(basePictureService.getBaseMapper().selectList(queryWrapper));
    }
}
