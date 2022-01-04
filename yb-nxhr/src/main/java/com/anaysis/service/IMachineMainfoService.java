package com.anaysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.anaysis.entity.MachineMainfo;
/**
* @author lzb
* @date 2020-11-26
*/

public interface IMachineMainfoService extends IService<MachineMainfo> {
    /**
     * todo 目前未使用
     * 同步
     */
    void syn();
    /**
     * 同步设备（同步设备前同步工序）
     */
    void sync();
}
