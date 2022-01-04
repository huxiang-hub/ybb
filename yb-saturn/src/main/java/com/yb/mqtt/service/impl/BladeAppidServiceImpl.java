package com.yb.mqtt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.auth.filter.UrlFilter;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.mqtt.entity.BladeAppid;
import com.yb.mqtt.mapper.BladeAppidMapper;
import com.yb.mqtt.service.IBladeAppidService;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 租户连接app管理对应信息表 服务实现类
 *
 * @author my
 * @since 2021-04-12
 */
@Service
@Slf4j
public class BladeAppidServiceImpl implements IBladeAppidService {

    @Autowired
    private BladeAppidMapper bladeAppidMapper;

    @Override
    public BladeAppid info() {
        DBIdentifier.setProjectCode("mqtt");
        HttpServletRequest request = WebUtil.getRequest();
        StringBuffer requestURL = request.getRequestURL();
        String tenant = UrlFilter.getTenant(requestURL);
        BladeAppid bladeAppid = bladeAppidMapper.selectOne(new QueryWrapper<BladeAppid>().eq("tenant_id", tenant));
        return bladeAppid;
    }
}
