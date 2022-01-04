package com.sso.auth.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.auth.service.BaseFactoryService;
import com.sso.base.entity.BaseFactory;
import com.sso.mapper.BaseFactoryMapper;
import org.springframework.stereotype.Service;

@Service
public class BaseFactoryServiceImpl extends ServiceImpl<BaseFactoryMapper, BaseFactory> implements BaseFactoryService {
}
