package com.yb.statis.utils;

import com.yb.base.entity.BasePicture;
import com.yb.base.mapper.BasePictureMapper;
import com.yb.fastdfs.FileSystem;
import com.yb.fastdfs.controller.FileController;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author my
 * #Description
 */
@Component
@Slf4j
public class FileUtils {

    @Autowired
    private Environment env;

    @Autowired
    BasePictureMapper basePictureMapper;

    @Autowired
    private FileController fileController;

    private static FileUtils FileUtils;

    @PostConstruct
    public void init() {
        FileUtils = this;
        FileUtils.env = this.env;
        FileUtils.basePictureMapper = this.basePictureMapper;
        FileUtils.fileController = this.fileController;
    }

    /**
     * 上传图片
     *
     * @param id      保存的表的主键id
     * @param type    图片类型  数据字典1、头像2、首检图片3、巡检图片btType 4、库区图片
     * @param file    正常图片
     * @param minFile 缩略图
     * @return
     * @throws IOException
     */
    public static void uploadImg(Integer id, String type, MultipartFile file, MultipartFile minFile) throws IOException {
        FileSystem fileSystem = new FileSystem();
        BasePicture basePicture = new BasePicture();
        basePicture.setBtId(id);
        basePicture.setBtType(type);
        FileUtils.basePictureMapper.insert(basePicture);
        try {
            if (file != null) {
                byte[] bytes = file.getBytes();
                //获取原始名称
                String originalFilename = file.getOriginalFilename();
                //获取扩展名
                String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
                String filePath = fastDfsUpload(bytes, fileSystem, originalFilename, ext, id, type);
                //存储图片信息
                basePicture.setPicTitle(originalFilename);
                basePicture.setPicUrl(filePath);
            }
            if (minFile != null) {
                byte[] minFileBytes = minFile.getBytes();
                //获取原始名称
                String filename = file.getOriginalFilename();
                //获取扩展名
                String extName = filename.substring(filename.lastIndexOf("."));
                String path = fastDfsUpload(minFileBytes, fileSystem, filename, extName, id, type);
                basePicture.setPicMinurl(path);
            }
            FileUtils.basePictureMapper.updateById(basePicture);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String fastDfsUpload(byte[] bytes, FileSystem fileSystem, String originalFilename, String ext, Integer id, String type) throws IOException, MyException {
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
        fileSystem.setFilePath(fileId);
        fileSystem.setFileName(originalFilename);
        log.info("upload success. file id is: " + fileId);
        trackerServer.close();
        return fileId;
    }

    /**
     * 保存图片后关联图片和表
     *
     * @param id   表id
     * @param type 类型
     * @param ids  图片ids
     */
    public static void updatePic(Integer id, String type, List<Integer> ids) {
        if (ids.isEmpty()) {
            return;
        }
        List<BasePicture> basePictures = FileUtils.basePictureMapper.selectBatchIds(ids);
        if (!basePictures.isEmpty()) {
            basePictures.forEach(o -> {
                o.setBtId(id);
                o.setBtType(type);
                FileUtils.basePictureMapper.updateById(o);
            });
        }
    }

    /**
     * 修改图片
     *
     * @param id   表id
     * @param type 类型
     * @param ids  修改的图片ids
     * @param ids  旧图片ids
     */
    public static void updateImg(Integer id, String type, List<Integer> ids, List<Integer> oldIds) {
        //不存在图片时
        if (ids.isEmpty() || oldIds.isEmpty()) {
            return;
        }
        //修改时才增加的图片
        if (!ids.isEmpty() && oldIds.isEmpty()) {
            List<BasePicture> basePictures = FileUtils.basePictureMapper.selectBatchIds(ids);
            basePictures.forEach(o -> {
                o.setBtType(type);
                o.setBtId(id);
                FileUtils.basePictureMapper.updateById(o);
            });
            return;
        }
        //修改时才增加的图片
        if (ids.isEmpty() && !oldIds.isEmpty()) {
            FileUtils.basePictureMapper.deleteBatchIds(oldIds);
            return;
        }

        //是否修改过图片
        if (oldIds.containsAll(ids)) {
            return;
        }
        List list = oldIds;
        oldIds.retainAll(list);
        //老图片全部修改了
        if (oldIds.isEmpty()) {

            FileUtils.basePictureMapper.deleteBatchIds(ids);
            //保存上新图片
            List<BasePicture> basePictures = FileUtils.basePictureMapper.selectBatchIds(ids);
            if (!basePictures.isEmpty()) {
                basePictures.forEach(o -> {
                    o.setBtId(id);
                    o.setBtType(type);
                    FileUtils.basePictureMapper.updateById(o);
                });
            }
        } else {
            //部分被修改
            list.removeAll(oldIds);
            if (!list.isEmpty()) {
                deleteImg(list);
            }
            ids.removeAll(oldIds);
            List<BasePicture> basePictures = FileUtils.basePictureMapper.selectBatchIds(ids);
            if (!basePictures.isEmpty()) {
                basePictures.forEach(o -> {
                    o.setBtId(id);
                    o.setBtType(type);
                    FileUtils.basePictureMapper.updateById(o);
                });
            }
        }
    }

    static void deleteImg(List ids) {
        new Thread(() -> {
            List<BasePicture> oldBasePictures = FileUtils.basePictureMapper.selectBatchIds(ids);
            //删除旧服务器上的图片
            oldBasePictures.forEach(o -> {
                int index = o.getPicUrl().indexOf("/");
                try {
                    FileUtils.fileController.deleteFile(o.getPicUrl().substring(0, index), o.getPicUrl().substring(index + 1));
                    FileUtils.basePictureMapper.deleteBatchIds(ids);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }).start();
    }
}
