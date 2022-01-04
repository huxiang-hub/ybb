package com.yb.fastdfs.controller;

import com.yb.base.entity.BasePicture;
import com.yb.base.mapper.BasePictureMapper;
import com.yb.fastdfs.FileSystem;
import com.yb.statis.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/9/17 14:19
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/file")
@Api(tags = "文件上传controller")
public class FileController {

    @Autowired
    private Environment env;
    @Autowired
    private BasePictureMapper basePictureMapper;

    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R<FileSystem> upload(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        FileSystem fileSystem = new FileSystem();
        //获取原始名称
        String originalFilename = file.getOriginalFilename();
        //获取扩展名
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        try {
            //执行上传
            String filePath = upload(bytes, originalFilename, ext);
            fileSystem.setFilePath(filePath);
            fileSystem.setFileName(originalFilename);
            BasePicture basePicture = new BasePicture();
            basePicture.setPicUrl(filePath);
            basePictureMapper.insert(basePicture);
            fileSystem.setFileId(basePicture.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return R.data(fileSystem);
    }


//    @ApiOperation("修改图片")
//    @PostMapping("updateImg")
//    public R updateImg(@RequestParam("file") MultipartFile file, @RequestParam("minFile") MultipartFile minFile,
////                       @RequestParam("type") String type, @RequestParam("id") Integer id) throws IOException {
////
//        String fileId = "";
//        String minFileId = "";
//        try {
//            if (file != null) {
//                byte[] bytes = file.getBytes();
//                //获取原始名称
//                String originalFilename = file.getOriginalFilename();
//                //获取扩展名
//                String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
//                fileId = upload(bytes, originalFilename, ext);
//            }
//            if (minFile != null) {
//                byte[] minFileBytes = minFile.getBytes();
//                //获取原始名称
//                String minFileOriginalFilename = minFile.getOriginalFilename();
//                //获取扩展名
//                String minExt = minFileOriginalFilename.substring(minFileOriginalFilename.lastIndexOf("."));
//                minFileId = upload(minFileBytes, minFileOriginalFilename, minExt);
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        List<BasePicture> byBtIdAndBtType = basePictureMapper.getByBtIdAndBtType(id, type);
//        if (basePicture != null) {
//            //删除存在的图片进行覆盖
//            if (Func.isNotBlank(basePicture.getPicUrl())) {
//                int index = basePicture.getPicUrl().indexOf("/");
//
//                deleteFile(basePicture.getPicUrl().substring(0, index), basePicture.getPicUrl().substring(index + 1));
//            }
//            if (Func.isNotBlank(basePicture.getPicMinurl())) {
//                int index = basePicture.getPicMinurl().indexOf("/");
//                deleteFile(basePicture.getPicMinurl().substring(0, index), basePicture.getPicMinurl().substring(index + 1));
//            }
//            basePicture.setPicUrl(fileId);
//            basePicture.setPicMinurl(minFileId);
//            basePictureMapper.updateById(basePicture);
//        } else {
//            basePicture = new BasePicture();
//            basePicture.setPicUrl(fileId);
//            basePicture.setPicMinurl(minFileId);
//            basePicture.setBtType(type);
//            basePicture.setBtId(id);
//            basePictureMapper.insert(basePicture);
//        }
//        return R.success("修改图片成功");
//    }

    private String upload(byte[] bytes, String originalFilename, String ext) throws IOException, MyException {
        //加载fast的配置文件
        ClientGlobal.initByProperties("fastdfs-client.properties");
        log.info("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
        log.info("charset=" + ClientGlobal.g_charset);
        //创建tracker客户端
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        storageServer = tracker.getStoreStorage(trackerServer);
        //定义storage的客户端
        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
        //文件元信息
        NameValuePair[] metaList = new NameValuePair[1];
        metaList[0] = new NameValuePair("fileName", originalFilename);
        //执行上传
        String fileId = client.upload_file1(bytes, ext, metaList);
        log.info("upload success. file id is: " + fileId);
        trackerServer.close();
        return fileId;
    }

    /**
     * 从FastDFS删除文件
     *
     * @param groupName      文件在FastDFS中的组名
     * @param remoteFilename 文件在FastDFS中的名称
     */
    @PostMapping("delete")
    public void deleteFile(String groupName, String remoteFilename) throws IOException {
        TrackerServer trackerServer = null;
        try {
            ClientGlobal.initByProperties("fastdfs-client.properties");
            //创建tracker客户端
            TrackerClient tracker = new TrackerClient();
            trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            storageServer = tracker.getStoreStorage(trackerServer);
            //定义storage的客户端
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);
            int i = client.delete_file(groupName, remoteFilename);
            System.out.println(i);
            trackerServer.close();

        } catch (IOException e) {
            log.error("error", e);
            return;
        } catch (MyException e) {
            e.printStackTrace();
        }
    }
}
