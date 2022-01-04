package com.yb.barcodeUtils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据pdf模板生成pdf文件生成到某路径下或导出
 *
 */
public class PDFUtils {
    /**
     * 利用模板生成pdf保存到某路径下
     */
    public static void pdfOut(Map<String, Object> inputMap) {

        // 生成的新文件路径
        String path0 = "C:/Users/Administrator/Desktop/test";
        File f = new File(path0);

        if (!f.exists()) {
            f.mkdirs();
        }
        // 模板路径
        String templatePath = "C:/Users/Administrator/Desktop/PDFTest.pdf";
        // 创建文件夹
        String newPdfPath = "C:/Users/Administrator/Desktop/test/test.pdf";
        File file = new File(templatePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file1 = new File(newPdfPath);
        if (!file1.exists()) {
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
//            BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/simfang.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            // 输出流
            out = new FileOutputStream(newPdfPath);
            // 读取pdf模板
            reader = new PdfReader(templatePath);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            //文字类的内容处理
            Map<String, String> datamap = (Map<String, String>) inputMap.get("datamap");
            form.addSubstitutionFont(bf);
            for (String key : datamap.keySet()) {
                String value = datamap.get(key);
                form.setField(key, value);
            }
            stamper.setFormFlattening(false);
            stamper.close();
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();
        } catch (IOException | DocumentException e) {
            System.out.println(e);
        }

    }

    /**
     * 利用模板生成pdf导出
     */
    public static void pdfExport(Map<String, Object> inputMap, String templatePath, HttpServletResponse response) {

        // 模板路径
//        String templatePath = "D:/ProhibitDelete/template12.pdf";

        File file = new File(templatePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        OutputStream out = null;
        try {
            Map<String, String> datamap = (Map<String, String>) inputMap.get("datamap");
            BaseFont bf =  BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            // 输出流
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + URLEncoder.encode(datamap.get("billId") + ".pdf", "UTF-8"));
            out = new BufferedOutputStream(response.getOutputStream());
            // 读取pdf模板
            reader = new PdfReader(templatePath);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            //文字类的内容处理
            form.addSubstitutionFont(bf);
            for (String key : datamap.keySet()) {
                String value = datamap.get(key);
                form.setField(key, value);
            }
            stamper.setFormFlattening(false);
            stamper.close();
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (IOException | DocumentException e) {
            System.out.println(e);
        } finally {
            try {
                assert out != null;
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /*---------------------------*/

    /**
     * 导出到指定文件夹
     * @param map
     * @param filePath 导出到目录
     * @param templatePath 模板路径
     */
    public static void creatPdf(Map<String, Object> map,String filePath, String templatePath) {
        try {
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            FileOutputStream out = new FileOutputStream(filePath);// 输出流
//            String templatePath = "C:/Users/Administrator/Desktop/PDFTest.pdf";
            PdfReader reader = new PdfReader(templatePath);// 读取pdf模板
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, bos);
            stamper.setFormFlattening(true);
            AcroFields form = stamper.getAcroFields();
            // 文字类的内容处理
            Map<String, String> datamap = (Map<String, String>) map.get("datamap");
            form.addSubstitutionFont(bf);
            for (String key : datamap.keySet()) {
                String value = datamap.get(key);
                form.setField(key, value);
            }
            // 图片类的内容处理
            Map<String, String> imgmap = (Map<String, String>) map.get("imgmap");
            for (String key : imgmap.keySet()) {
                String value = imgmap.get(key);
                String imgpath = value;
                int pageNo = form.getFieldPositions(key).get(0).page;
                Rectangle signRect = form.getFieldPositions(key).get(0).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                // 根据路径读取图片
                Image image = Image.getInstance(imgpath);
                // 获取图片页面
                PdfContentByte under = stamper.getOverContent(pageNo);
                // 图片大小自适应
                image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                // 添加图片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
            // 表格类
            Map<String, List<List<String>>> listMap =  (Map<String, List<List<String>>>) map.get("list");
            if(listMap != null){
                for (String key : listMap.keySet()) {
                    List<List<String>> lists = listMap.get(key);
                    int pageNo = form.getFieldPositions(key).get(0).page;
                    PdfContentByte pcb = stamper.getOverContent(pageNo);
                    Rectangle signRect = form.getFieldPositions(key).get(0).position;
                    //表格位置
                    int column = lists.get(0).size();
                    int row = lists.size();
                    PdfPTable table =  new PdfPTable(column);
                    float tatalWidth = signRect.getRight() - signRect.getLeft() - 1;
                    int size = lists.get(0).size();
                    float width[] = new float[size];
                    for(int i=0;i<size;i++){
                        if(i==0){
                            width[i]=60f;
                        }else{
                            width[i]=(tatalWidth-60)/(size-1);
                        }
                    }
                    table.setTotalWidth(width);
                    table.setLockedWidth(true);
                    table.setKeepTogether(true);
                    table.setSplitLate(false);
                    table.setSplitRows(true);
                    Font FontProve = new Font(bf, 10, 0);
                    //表格数据填写
                    for(int i=0;i<row;i++){
                        List<String> list = lists.get(i);
                        for(int j=0;j<column;j++){
                            Paragraph paragraph = new Paragraph(String.valueOf(list.get(j)), FontProve);
                            PdfPCell cell = new PdfPCell(paragraph);
                            cell.setBorderWidth(1);
                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setLeading(0, (float) 1.4);
                            table.addCell(cell);
                        }
                    }
                    table.writeSelectedRows(0, -1, signRect.getLeft(), signRect.getTop(), pcb);
                }
            }

            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            int pageNum = reader.getNumberOfPages();
            for(int i = 1;i <= pageNum;i++){
                PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), i);
                copy.addPage(importPage);
            }
            doc.close();
        } catch (IOException e) {
            System.out.println(e);
        } catch (DocumentException e) {
            System.out.println(e);
        }

    }

    /**
     * 导出d流,前端处理
     * @param map
     * @param templatePath
     * @param response
     */
    public static void creatPdf(Map<String, Object> map, String templatePath, HttpServletResponse response) {
        OutputStream out = null;// 输出流
        PdfReader reader = null;
        PdfStamper stamper = null;
        ByteArrayOutputStream bos = null;
        Document doc = null;
        try {
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            stamper.setFormFlattening(true);
            AcroFields form = stamper.getAcroFields();
            // 文字类的内容处理
            Map<String, String> datamap = (Map<String, String>) map.get("datamap");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + URLEncoder.encode(datamap.get("billId") + ".pdf", "UTF-8"));
            out = new BufferedOutputStream(response.getOutputStream());
            form.addSubstitutionFont(bf);
            for (String key : datamap.keySet()) {
                String value = datamap.get(key);
                form.setField(key, value);
            }
            // 图片类的内容处理
            Map<String, String> imgmap = (Map<String, String>) map.get("imgmap");
            if(imgmap != null){
                for (String key : imgmap.keySet()) {
                    String value = imgmap.get(key);
                    String imgpath = value;
                    int pageNo = form.getFieldPositions(key).get(0).page;
                    Rectangle signRect = form.getFieldPositions(key).get(0).position;
                    float x = signRect.getLeft();
                    float y = signRect.getBottom();
                    // 根据路径读取图片
                    Image image = Image.getInstance(imgpath);
                    // 获取图片页面
                    PdfContentByte under = stamper.getOverContent(pageNo);
                    // 图片大小自适应
                    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                    // 添加图片
                    image.setAbsolutePosition(x, y);
                    under.addImage(image);
                }
            }

            // 表格类
            Map<String, List<List<String>>> listMap =  (Map<String, List<List<String>>>) map.get("list");
            if(listMap != null){
                for (String key : listMap.keySet()) {
                    List<List<String>> lists = listMap.get(key);
                    int pageNo = form.getFieldPositions(key).get(0).page;
                    PdfContentByte pcb = stamper.getOverContent(pageNo);
                    Rectangle signRect = form.getFieldPositions(key).get(0).position;
                    //表格位置
                    int column = lists.get(0).size();
                    int row = lists.size();
                    PdfPTable table =  new PdfPTable(column);
                    float tatalWidth = signRect.getRight() - signRect.getLeft() - 1;
                    int size = lists.get(0).size();
                    float width[] = new float[size];
                    for(int i=0;i<size;i++){
                        if(i==0){
                            width[i]=60f;
                        }else{
                            width[i]=(tatalWidth-60)/(size-1);
                        }
                    }
                    table.setTotalWidth(width);
                    table.setLockedWidth(true);
                    table.setKeepTogether(true);
                    table.setSplitLate(false);
                    table.setSplitRows(true);
                    Font FontProve = new Font(bf, 10, 0);
                    //表格数据填写
                    for(int i=0;i<row;i++){
                        List<String> list = lists.get(i);
                        for(int j=0;j<column;j++){
                            Paragraph paragraph = new Paragraph(String.valueOf(list.get(j)), FontProve);
                            PdfPCell cell = new PdfPCell(paragraph);
                            cell.setBorderWidth(1);
                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setLeading(0, (float) 1.4);
                            table.addCell(cell);
                        }
                    }
                    table.writeSelectedRows(0, -1, signRect.getLeft(), signRect.getTop(), pcb);
                }
            }

            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();
            doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            int pageNum = reader.getNumberOfPages();
            for(int i = 1;i <= pageNum;i++){
                PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), i);
                copy.addPage(importPage);
            }
        } catch (IOException e) {
            System.out.println(e);
        } catch (DocumentException e) {
            System.out.println(e);
        }finally {
            try {
                if (doc != null) {
                    doc.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getFonts(){
        return this.getClass().getClassLoader().getResource("fonts/simsun.ttc").getPath();
    }
    /**
     * 返回byte数组
     * @param map
     * @param templatePath
     * @param response
     */
    public static byte[] creatPdfByte(Map<String, Object> map, String templatePath, HttpServletResponse response) {
        PdfReader reader = null;
        PdfStamper stamper;
        ByteArrayOutputStream bos = null;
        try {
            PDFUtils pdfUtils = new PDFUtils();
            String fonts = pdfUtils.getFonts() + ",0";
//            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);

            BaseFont bf =  BaseFont.createFont(fonts, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            stamper.setFormFlattening(true);
            AcroFields form = stamper.getAcroFields();
            // 文字类的内容处理
            Map<String, String> datamap = (Map<String, String>) map.get("datamap");
            form.addSubstitutionFont(bf);
            for (String key : datamap.keySet()) {
                String value = datamap.get(key);
                form.setField(key, value);
            }
            // 图片类的内容处理
            Map<String, String> imgmap = (Map<String, String>) map.get("imgmap");
            if(imgmap != null){
                for (String key : imgmap.keySet()) {
                    String value = imgmap.get(key);
                    String imgpath = value;
                    int pageNo = form.getFieldPositions(key).get(0).page;
                    Rectangle signRect = form.getFieldPositions(key).get(0).position;
                    float x = signRect.getLeft();
                    float y = signRect.getBottom();
                    // 根据路径读取图片
                    Image image = Image.getInstance(imgpath);
                    // 获取图片页面
                    PdfContentByte under = stamper.getOverContent(pageNo);
                    // 图片大小自适应
                    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                    // 添加图片
                    image.setAbsolutePosition(x, y);
                    under.addImage(image);
                }
            }

            // 表格类
            Map<String, List<List<String>>> listMap =  (Map<String, List<List<String>>>) map.get("list");
            if(listMap != null){
                for (String key : listMap.keySet()) {
                    List<List<String>> lists = listMap.get(key);
                    int pageNo = form.getFieldPositions(key).get(0).page;
                    PdfContentByte pcb = stamper.getOverContent(pageNo);
                    Rectangle signRect = form.getFieldPositions(key).get(0).position;
                    //表格位置
                    int column = lists.get(0).size();
                    int row = lists.size();
                    PdfPTable table =  new PdfPTable(column);
                    float tatalWidth = signRect.getRight() - signRect.getLeft() - 1;
                    int size = lists.get(0).size();
                    float width[] = new float[size];
                    for(int i=0;i<size;i++){
                        if(i==0){
                            width[i]=60f;
                        }else{
                            width[i]=(tatalWidth-60)/(size-1);
                        }
                    }
                    table.setTotalWidth(width);
                    table.setLockedWidth(true);
                    table.setKeepTogether(true);
                    table.setSplitLate(false);
                    table.setSplitRows(true);
                    Font FontProve = new Font(bf, 10, 0);
                    //表格数据填写
                    for(int i=0;i<row;i++){
                        List<String> list = lists.get(i);
                        for(int j=0;j<column;j++){
                            Paragraph paragraph = new Paragraph(String.valueOf(list.get(j)), FontProve);
                            PdfPCell cell = new PdfPCell(paragraph);
                            cell.setBorderWidth(1);
                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setLeading(0, (float) 1.4);
                            table.addCell(cell);
                        }
                    }
                    table.writeSelectedRows(0, -1, signRect.getLeft(), signRect.getTop(), pcb);
                }
            }
            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();
            return bos.toByteArray();

        } catch (IOException e) {
            System.out.println(e);
        } catch (DocumentException e) {
            System.out.println(e);
        }finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /*---------------------------*/


    public static void main(String[] args) {
        Map<String, Object> datamap = new HashMap<>();
        datamap.put("name", "张三");
        datamap.put("sex", "男");
        datamap.put("age", "24");
        datamap.put("phone", "21324");
        datamap.put("Email", "3213@com.cn");
        Map<String, Object> imgmap = new HashMap<>();
        imgmap.put("img", "C:/Users/Administrator/Desktop/capacityModel/1.png");
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("datamap", datamap);
        inputMap.put("imgmap", imgmap);
        String newPdfPath = "C:/Users/Administrator/Desktop/test/test.pdf";
//        creatPdf(inputMap, newPdfPath);
//        pdfOut(inputMap);
    }
}

