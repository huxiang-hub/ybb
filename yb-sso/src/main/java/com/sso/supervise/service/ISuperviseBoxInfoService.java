package com.sso.supervise.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sso.supervise.entity.SuperviseBoxinfo;

/**
 * 设备当前状态表boxinfo-视图 服务类
 *
 * @author my
 * @since 2020-05-28
 */
public interface ISuperviseBoxInfoService extends IService<SuperviseBoxinfo> {

    SuperviseBoxinfo getBoxInfoByMid(Integer maId);

}
