package com.yb.barcodeUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

public class PdfOperate {
    static String savepath = "C:/Users/Administrator/Desktop/capacityModel/all.pdf";

/*
    public static void main(String[] args) {
        System.out.println("start " + new Date());
        List<String> fileList = getFiles();
        morePdfTopdf(fileList, savepath);

    }
*/

    public static List<String> getFiles() {
        List<String> fileList = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            int n = i + 1;
            fileList.add("D:/ChromeCoreDownloads/"+ n +".pdf");
        }
        return fileList;
    }

    public static void morePdfTopdf(List<byte[]> byteList, String savepath) {
        Document document = null;
        try {
//            byte[] b = new byte[2];
//            document = new Document(new PdfReader(b).getPageSize(1));
            document = new Document(new PdfReader(byteList.get(0)).getPageSize(1));
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(savepath));
            document.open();
            for (int i = 0; i < byteList.size(); i++) {
                PdfReader reader = new PdfReader(byteList.get(i));
                int n = reader.getNumberOfPages();// 获得总页码
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);// 从当前Pdf,获取第j页
                    copy.addPage(page);
                }
                System.out.println(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
            System.out.println("finish " + new Date());
        }
    }

    public static void main(String[] args) throws IOException {
        //创建合并流管道
        SequenceInputStream sis = new SequenceInputStream(new FileInputStream("D:/java/文件1.txt"), new FileInputStream("D:/java/文件2.txt"));
        //创建输出流管道
        OutputStream out = new FileOutputStream("D:/java/文件3.txt");
        //声明byte数组用于存储从输入流读取到的数据
        byte[] by = new byte[1024];
        //该变量纪录每次读取到的字符个数
        int count = 0;
        //读取输入流中的数据
        while((count = sis.read(by))!=-1){
            //输出从输入流中读取到的数据
            out.write(by, 0, count);
        }
        //关闭合并流
        sis.close();
        //强制把输出流中的数据写入
        out.flush();
        //关闭输出流
        out.close();
    }


}
