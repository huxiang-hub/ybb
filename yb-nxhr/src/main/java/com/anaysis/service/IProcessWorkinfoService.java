package com.anaysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.anaysis.entity.ProcessWorkinfo;

import java.util.List;
import java.util.Map;

/**
* @author lzb
* @date 2020-11-25
*/

public interface IProcessWorkinfoService extends IService<ProcessWorkinfo> {

    /**
     * todo 目前未使用
     * 同步工序
     */
    void syn();

    /**
     * 同步工序
     */
    void sync();

    ProcessWorkinfo getByErpId(String erpId);

    List<ProcessWorkinfo> getByErpIds(List<String> processErpIds);

    Map<String, String> bomProcess();
}
