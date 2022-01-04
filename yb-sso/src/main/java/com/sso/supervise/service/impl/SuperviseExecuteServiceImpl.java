package com.sso.supervise.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.mapper.SuperviseExecuteMapper;
import com.sso.supervise.entity.SuperviseExecute;
import com.sso.supervise.service.ISuperviseExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备清零日志表 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class SuperviseExecuteServiceImpl extends ServiceImpl<SuperviseExecuteMapper, SuperviseExecute> implements ISuperviseExecuteService {

    @Autowired
    private SuperviseExecuteMapper superviseExecuteMapper;

    @Override
    public SuperviseExecute getExecuteOrder(Integer maId) {
        return superviseExecuteMapper.getExecuteOrder(maId);
    }
}
