package com.yb.statis.excelUtils;

import java.util.List;

public class Page {

    /**
     * 页面信息
     */
    private String sheetName; // 每个sheet名字
    private String currentPage; // 当前页
    private String tolalPage; // 总页

    /**
     * 页面遍历的数据 List 的泛型自行设置，如果所有数据都来着同一个类就写那个类，
     * 不是同一个类有继承就写继承类的泛型，没有就写问号。
     */
    private List<?> data;

    /**
     * 一页只保存一个人的信息
     */
    private Object onlyOne;


    public Page(String sheetName, String currentPage, String tolalPage, List<?> data) {
        super();
        this.sheetName = sheetName;
        this.currentPage = currentPage;
        this.tolalPage = tolalPage;
        this.data = data;
    }

    public Page() {
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getTolalPage() {
        return tolalPage;
    }

    public void setTolalPage(String tolalPage) {
        this.tolalPage = tolalPage;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public Object getOnlyOne() {
        return onlyOne;
    }

    public void setOnlyOne(Object onlyOne) {
        this.onlyOne = onlyOne;
    }
}

 
