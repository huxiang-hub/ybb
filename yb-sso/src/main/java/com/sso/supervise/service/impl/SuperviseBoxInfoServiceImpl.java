package com.sso.supervise.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.mapper.SuperviseBoxinfoMapper;
import com.sso.supervise.entity.SuperviseBoxinfo;
import com.sso.supervise.service.ISuperviseBoxInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备当前状态表boxinfo-视图 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class SuperviseBoxInfoServiceImpl extends ServiceImpl<SuperviseBoxinfoMapper, SuperviseBoxinfo> implements ISuperviseBoxInfoService {

    @Autowired
    private SuperviseBoxinfoMapper boxinfoMapper;

    @Override
    public SuperviseBoxinfo getBoxInfoByMid(Integer maId) {
        return boxinfoMapper.getBoxInfoByMid(maId);
    }

}
