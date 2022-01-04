package com.yb.dingding.controller;

import com.yb.base.entity.BasePicture;
import com.yb.base.mapper.BasePictureMapper;
import com.yb.dingding.request.DdUploadRequest;
import com.yb.fastdfs.FileSystem;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author my
 * #Description
 */
@RestController
@RequestMapping("/dingUpload")
@Slf4j
public class DingUploadController {

    @Autowired
    private BasePictureMapper basePictureMapper;

    @ApiOperation("dd文件上传")
    @PostMapping("upload")
    public R upload(@RequestBody DdUploadRequest request) throws IOException {
//        if (ids != null) {
//            for (MultipartFile file : files) {
//                byte[] bytes = file.getBytes();
//                FileSystem fileSystem = new FileSystem();
//                //获取原始名称
//                String originalFilename = file.getOriginalFilename();
//                //获取扩展名
//                String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
//                try {
//                    //执行上传
//                    //加载fast的配置文件
//                    ClientGlobal.initByProperties("fastdfs-client.properties");
//                    log.info("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
//                    log.info("charset=" + ClientGlobal.g_charset);
//                    //创建tracker客户端
//                    TrackerClient tracker = new TrackerClient();
//                    TrackerServer trackerServer = tracker.getConnection();
//                    StorageServer storageServer = null;
//                    storageServer = tracker.getStoreStorage(trackerServer);
//                    //定义storage的客户端
//                    StorageClient1 client = new StorageClient1(trackerServer, storageServer);
//                    //文件元信息
//                    NameValuePair[] metaList = new NameValuePair[1];
//                    metaList[0] = new NameValuePair("fileName", originalFilename);
//                    //执行上传
//                    String fileId = client.upload_file1(bytes, ext, metaList);
//                    log.info("upload success. file id is: " + fileId);
//                    trackerServer.close();
//                    fileSystem.setFilePath(fileId);
//                    fileSystem.setFileName(originalFilename);
//                    BasePicture basePicture = new BasePicture();
//                    basePicture.setPicUrl(fileId);
//                    basePicture.setUuid(code);
//                    basePictureMapper.insert(basePicture);
//                    log.info("钉钉上传图片成功:[code:{}]", code);
//                    fileSystem.setFileId(basePicture.getId().toString());
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
            List<BasePicture> basePictures = basePictureMapper.selectBatchIds(request.getIds());
            if (!basePictures.isEmpty()) {
                for (BasePicture basePicture : basePictures) {
                    basePicture.setUuid(request.getCode());
                    basePictureMapper.updateById(basePicture);
                }
            }
        return R.success("上传成功");
    }

    @ApiOperation("dd文件上传路径获取")
    @PostMapping("getFile")
    public R<List<FileSystem>> getFile(@RequestParam("code") String code) throws IOException {
        List<FileSystem> vos = basePictureMapper.findByUuid(code);
        return R.data(vos);
    }
}
