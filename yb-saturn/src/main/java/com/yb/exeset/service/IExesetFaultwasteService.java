package com.yb.exeset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.exeset.entity.ExesetFaultwaste;

import java.util.List;

public interface IExesetFaultwasteService extends IService<ExesetFaultwaste> {
    /**
     * 查询当前设备的停机废品信息接口
     * @param maId
     * @return
     */
    List<ExesetFaultwaste> getExesetFaultwaste(Integer maId);
    /**
     * 修改当前设备的停机废品信息接口
     * @param exesetFaultwaste
     * @return
     */
    boolean setFaultwaste(ExesetFaultwaste exesetFaultwaste);
}
