package com.anaysis.executSupervise.service.impl;

import com.anaysis.common.StringUtils;
import com.anaysis.dynamicData.datasource.DBIdentifier;
import com.anaysis.executSupervise.ExecutConstant;
import com.anaysis.executSupervise.mapper.SaTenantMapper;
import com.anaysis.executSupervise.service.AnalyTenantService;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.exception.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/7/4 16:08
 */
@Service
@Slf4j
public class AnalyTenantServiceImpl implements AnalyTenantService {
    @Autowired
    private SaTenantMapper saTenantMapper;

    @Override
    public String getTenantId(String uuid) {
        String tenantId = ExecutConstant.boxTenantCache.get(uuid);
        //确定盒子属于哪个租户
        if (StringUtils.isNotBlank(tenantId)) {
            tenantId = ExecutConstant.boxTenantCache.get(uuid);
        } else {
            tenantId = saTenantMapper.getTenantIdByUuid(uuid);
            if (tenantId == null) {
                log.info("未查询到盒子对应租户信息:[uuid:{}]", uuid);
                throw new CommonException(HttpStatus.NOT_FOUND.value(), "未找到盒子对应租户信息id");
            }
            ExecutConstant.boxTenantCache.put(uuid, tenantId);
        }
        //设置跳转到不同数据库
        DBIdentifier.setProjectCode(tenantId);
        return tenantId;
    }
}
