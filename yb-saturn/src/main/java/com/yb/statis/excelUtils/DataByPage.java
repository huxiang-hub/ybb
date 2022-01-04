package com.yb.statis.excelUtils;

import java.util.ArrayList;
import java.util.List;

public class DataByPage {

    static int pagesize = 100; // 每页记录数

    /**
     * 根据每页显示多少条数据计算总页数
     *
     * @param dataList 数据库查询的数据
     */
    public static int countPages(List<?> dataList) {
        int recordcount = dataList.size(); // 总记录数
        return (recordcount + pagesize - 1) / pagesize;
    }

    public static List<Page> byPage(List<?> dataList) {
        int pagecount; // 总页数
        int nowDataListPoint = 0; // 读取到接收的哪一条数据

        pagecount = countPages(dataList); // 计算页码
        List<Page> pageList = new ArrayList<Page>(); // 页面分页
        for (int i = 0; i < pagecount; i++) {

            List<Object> pagedata = new ArrayList<Object>();
            // 把传来的数据取出
            while (nowDataListPoint < dataList.size()) {
                pagedata.add(dataList.get(nowDataListPoint));
                nowDataListPoint += 1;
                if (nowDataListPoint != 0 && nowDataListPoint % pagesize == 0) {
                    break;
                }
            }
            Page page = new Page("排产单_" + (i + 1), (i + 1) + "", pagecount + "", pagedata);
            pageList.add(page);
        }
        return pageList;
    }

}
