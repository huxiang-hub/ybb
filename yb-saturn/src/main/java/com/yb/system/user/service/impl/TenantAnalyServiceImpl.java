package com.yb.system.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.system.user.entity.Tenant;
import com.yb.system.user.mapper.SaTenantMapper;
import com.yb.system.user.service.TenantAnalyService;
import com.yb.system.user.util.TenantDateEncodeUtils;
import com.yb.system.user.vo.TenantAnalyBoxStatusVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务类
 *
 * @author 解析对外接口
 */
@Service
@AllArgsConstructor
@Slf4j
public class TenantAnalyServiceImpl extends BaseServiceImpl<SaTenantMapper, Tenant> implements TenantAnalyService {

    @Autowired
    private SaTenantMapper saTenantMapper;

    @Override
    public R getAnalyTenantBoxInfo(String tenantStr) {
        Tenant tenant = saTenantMapper.getAnalyTenantId(tenantStr);
        if (tenant == null) {
            log.debug("解析租户获取盒子信息失败:[str:{}]", tenantStr);
            return R.fail("租户信息错误");
        }
        DBIdentifier.setProjectCode(tenant.getTenantId());
        List<String> boxUuidS = saTenantMapper.findAnalyTenantIdBoxUuid(tenant.getTenantId());
        if (boxUuidS.isEmpty()) {
            return R.fail("暂无设备盒子信息");
        }
        List<TenantAnalyBoxStatusVO> boxInfo = saTenantMapper.findBoxInfo(boxUuidS);
        System.out.println(JSONObject.toJSONString(boxInfo));
        byte[] encrypt = TenantDateEncodeUtils.encrypt(JSONObject.toJSONBytes(boxInfo), tenant.getTenDeckey());
        byte[] decrypt = TenantDateEncodeUtils.decrypt(encrypt, tenant.getTenDeckey());
        System.out.println(JSONObject.toJSONString(decrypt));
        System.out.println("!!!!!!!"+new String(decrypt));
        return R.data(encrypt);
    }
}
