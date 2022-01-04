package com.anaysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.anaysis.entity.BladeUser;
/**
* @author lzb
* @date 2020-11-25
*/

public interface IBladeUserService extends IService<BladeUser> {
    /**
     * 同步系统员工
     */
    void sync();

    /**
     * 同步车间工人，如果人事表存在车间工人则关联班组；
     * 人事表不存在则直接保存（人事表不存在的车间工人信息只有姓名）
     * 同步车间工人之前先同步系统员工
     */
    void syncDeptStaff();
}
