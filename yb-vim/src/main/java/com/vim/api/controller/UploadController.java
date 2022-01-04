package com.vim.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.vim.chatapi.friend.entity.IMResult;
import com.vim.chatapi.user.service.IChatUserService;
import com.vim.chatapi.utils.IMUpdateUserUtil;
import com.vim.chatapi.utils.ObjectMapperUtil;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springblade.system.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * 文件上传控制器
 *
 * @author 乐天
 * @since 2018-10-07
 */
@Controller
@RequestMapping("api")
public class UploadController {

    @Value("${web.upload-path}")
    private String uploadPath;
    @Value("${web.download-path}")
    private String url;
    @Autowired
    private IChatUserService iChatUserService;

    /**
     * 上传接口
     *
     * @param file    文件
     * @param request 请求
     * @return json
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request, Integer userId) {
        String host = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        //重新给文件取名字
        String fileName = UUID.randomUUID() + "." + file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".") + 1);
        File targetFile = new File(uploadPath);
        JSONObject json = new JSONObject();
        if (!targetFile.exists()) {
            if (!targetFile.mkdirs()) {
                json.put("msg", "error");
                return R.fail(json.toJSONString());
            }
        }
        json.put("msg", "success");
        File tempFile = new File(uploadPath, fileName);
        //保存
        try {
            file.transferTo(tempFile);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("msg", "error");
            return R.fail(json.toJSONString());
        }
        json.put("filePath", host +url + fileName);
        //上传成功更新用户头像
        if (userId!=null) {
            String jsonString = json.getString("filePath");
            User user =
                    iChatUserService.getById(userId);
            user.setAvatar(jsonString);
            String updateIM;
            try {
                updateIM = IMUpdateUserUtil.updateIM(user);//修改IM上的用户头像
            } catch (IOException e) {
                return R.fail("头像上传失败");
            }
            IMResult iMResult = ObjectMapperUtil.toObject(updateIM, IMResult.class);
            if(iMResult.getErrorCode() != 0){
                return R.fail("头像上传失败");
            }
            //更新user
            iChatUserService.updateById(user);
        }
        return R.data(json.getString("filePath"),"头像上传成功！");
    }

    @GetMapping("/modifyThePicture")
    @ApiOperation(value = "修改头像", notes = "传入用户id和头像路径")
    public R modifyThePicture(Integer userId, String uploadPath){
        if (userId != null) {
            User user =
                    iChatUserService.getById(userId);
            user.setAvatar(uploadPath);
            String updateIM;
            try {
                updateIM = IMUpdateUserUtil.updateIM(user);//修改IM上的用户头像
            } catch (IOException e) {
                return R.fail("头像上传失败");
            }
            IMResult iMResult = ObjectMapperUtil.toObject(updateIM, IMResult.class);
            if(iMResult.getErrorCode() != 0){
                return R.fail("头像上传失败");
            }
            //更新user
            iChatUserService.updateById(user);
        }
        return R.data(uploadPath, "头像上传成功！");
    }
}
