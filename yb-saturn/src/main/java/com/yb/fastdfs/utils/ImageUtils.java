package com.yb.fastdfs.utils;

import com.yb.fastdfs.FileSystem;
import com.yb.fastdfs.controller.FileController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.springblade.core.tool.api.R;
//import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;

@Slf4j
public class ImageUtils {

    /**
     * description 网络TRL中下载图片
     * @param uri
     * @param folder 存放文件的文件夹
     * @return java.lang.String
     */
    private String downloadByUrl(String uri, String folder){
        ReadableByteChannel readableByteChannel = null;
        FileChannel fileChannel = null;
        File file;
        URL url;
        FileOutputStream fileOutputStream = null;
        try {
            url = new URL(uri);
            //首先从 URL stream 中创建一个 ReadableByteChannel 来读取网络文件
            readableByteChannel = Channels.newChannel(url.openStream());
            String fileName = System.currentTimeMillis() + url.getPath().substring(url.getPath().lastIndexOf("."));
            String path = folder + "/" + fileName;
            file = new File(path);
            if (!file.getParentFile().exists() && !file.getParentFile().isDirectory()) {
                file.getParentFile().mkdirs();
            }
            //通过 ReadableByteChannel 读取到的字节会流动到一个 FileChannel 中，然后再关联一个本地文件进行下载操作
            fileOutputStream = new FileOutputStream(file);
            fileChannel = fileOutputStream.getChannel();
            //最后用 transferFrom()方法就可以把 ReadableByteChannel 获取到的字节写入本地文件
            fileChannel.transferFrom(readableByteChannel,0,Long.MAX_VALUE);
            return fileName;
        } catch (Exception e) {
            log.error("下载文件失败" + e.getMessage());
            return "";
        } finally {
            try {
                if (null != readableByteChannel) {
                    readableByteChannel.close();
                }
                if (null != fileChannel) {
                    fileChannel.close();
                }
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * description 将本地文件转换为MultipartFile
     * @param path
     * @return org.springframework.web.multipart.MultipartFile
     */
    private MultipartFile getMultipartFile(String path){
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File file = new File(path);
            FileItem fileItem = new DiskFileItem("formFieldName",//form表单文件控件的名字随便起
                    Files.probeContentType(file.toPath()),//文件类型
                    false, //是否是表单字段
                    file.getName(),//原始文件名
                    (int) file.length(),//Interger的最大值可以存储两部1G的电影
                    file.getParentFile());//文件会在哪个目录创建
            //为DiskFileItem的OutputStream赋值
            inputStream = new FileInputStream(file);
            outputStream = fileItem.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            return new CommonsMultipartFile(fileItem);
        } catch (IOException e) {
            log.error("文件类型转换失败" + e.getMessage());
            return null;
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }

                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error(">>文件流关闭失败" + e.getMessage());
            }
        }

    }

    /**
     * 获取网络图片流
     *
     * @param url
     * @return
     */
    public InputStream getImageStream(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            System.out.println("获取网络图片出现异常，图片路径为：" + url);
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        ImageUtils imageUtils = new ImageUtils();
        String url = "https://static-legacy.dingtalk.com/media/lADPDgQ9rRz1iWPNAbDNAbA_432_432.jpg";
        InputStream imageStream = imageUtils.getImageStream(url);
        System.out.println("imageStream获取成功");
//        MultipartFile multipartFile = new MockMultipartFile("2.jpg", "1.jpg", "", imageStream);
    }
}
