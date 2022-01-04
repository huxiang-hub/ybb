package com.anaysis.service;

import com.anaysis.entity.BaseDeptinfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author lzb
* @date 2020-11-25
*/

public interface IBaseDeptinfoService extends IService<BaseDeptinfo> {

    /**
     * 同步部门
     */
    void syn();

    /**
     * 获取erpId和mes的id关系
     * @return
     */
    Map<String, Integer> deptLink();
}
