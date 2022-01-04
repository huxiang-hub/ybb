package com.yb.panelapi.hb.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/plapi/sgdbill")
public class HbSGDBillController {

    static String printID = "12eedf2d-8c28-4398-b85e-ec6a53e4ca10";//默认为打印施工单pdf文件
    static String dataID = "7fe437fb-e4a1-4262-8a09-bda2380b0bbb";
    static String reportUrl = "http://192.168.5.230:8887/admin/Report/Public_Report.aspx?DataID=" + dataID +
            "&PrintID=" + printID;

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr   pdf网页的url
     * @param fileName 下载到本地的文件名
     * @param savePath 下载路径
     * @throws IOException 抛出异常
     */
    public static void downLoadByUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(5 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);
        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        System.out.println("info:" + url + " download success");

    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    //    @RequestMapping(value = "/getpdf", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public static void getPdfbyUrl(String dataId) {
    public static void main(String[] args) {
        try {
            downLoadByUrl(reportUrl, "ELISA.pdf", "E:/upload/protocol");
//            downLoadByUrl("https://www.mybiosource.com/images/tds/protocol_samples/MBS700_Antibody_Set_Sandwich_ELISA_Protocol.pdf",
//                    "ELISA.pdf", "E:/upload/protocol");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }
}
