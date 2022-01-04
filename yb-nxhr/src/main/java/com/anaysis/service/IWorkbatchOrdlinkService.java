package com.anaysis.service;

import com.anaysis.entity.WorkbatchOrdlink;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/30
 **/
public interface IWorkbatchOrdlinkService extends IService<WorkbatchOrdlink> {

    /**
     * 同步排产单（同步排产单之前先同步设备和工序）
     */
    void sync();

    List<WorkbatchOrdlink> notImportWorkPlan(List<String> workOrderNotImport);
}
