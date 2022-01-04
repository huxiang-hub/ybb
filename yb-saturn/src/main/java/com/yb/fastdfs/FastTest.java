package com.yb.fastdfs;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Test;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/9/16 15:22
 */
public class FastTest {
    @Test
    public void test() throws Exception {
        try {
            //加载fast的配置文件
            ClientGlobal.initByProperties("fastdfs-client.properties");
            System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
            System.out.println("charset=" + ClientGlobal.g_charset);

            //创建tracker客户端
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            storageServer = tracker.getStoreStorage(trackerServer);
            //定义storage的客户端
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);

            //文件元信息
            NameValuePair[] metaList = new NameValuePair[1];
            metaList[0] = new NameValuePair("fileName", "1.png");
            //执行上传
            String fileId = client.upload_file1("C:\\Users\\jch8\\Desktop\\1.PNG", null, metaList);
            System.out.println("upload success. file id is: " + fileId);
            trackerServer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public void download() throws Exception{
//        //加载fast的配置文件
//        ClientGlobal.initByProperties("fastdfs-client.properties");
//        System.out.println("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
//        System.out.println("charset=" + ClientGlobal.g_charset);
//
//        //创建tracker客户端
//        TrackerClient tracker = new TrackerClient();
//        TrackerServer trackerServer = tracker.getConnection();
//        StorageServer storageServer = null;
//        storageServer = tracker.getStoreStorage(trackerServer);
//        //定义storage的客户端
//        StorageClient1 client = new StorageClient1(trackerServer, storageServer);
//        client.download_file1();
//    }
}
