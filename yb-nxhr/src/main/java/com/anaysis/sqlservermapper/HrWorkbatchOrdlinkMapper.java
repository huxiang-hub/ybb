package com.anaysis.sqlservermapper;

import com.anaysis.entity.WorkbatchOrdlink;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/30 15:35
 **/
public interface HrWorkbatchOrdlinkMapper {

    List<String> getAllErpIds();

    List<WorkbatchOrdlink> getByErpIds(List<String> addErpIds);

    List<WorkbatchOrdlink> getSplit(List<String> erpIds);
}
