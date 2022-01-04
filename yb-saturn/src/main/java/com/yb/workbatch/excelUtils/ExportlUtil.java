package com.yb.workbatch.excelUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;

/**
 * Exce导出下载地址
 *
 * @Author: guandezhi
 * @Date: 2019/3/9 9:47
 */
public class ExportlUtil {

    public static BufferedOutputStream getBufferedOutputStream(String fileName, HttpServletResponse response) throws Exception {
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;filename="
                + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        return new BufferedOutputStream(response.getOutputStream());
    }


}

