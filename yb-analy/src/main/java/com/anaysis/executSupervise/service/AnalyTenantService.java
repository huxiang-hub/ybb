package com.anaysis.executSupervise.service;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/7/4 16:07
 */
public interface AnalyTenantService {
    /**
     * 根据盒子的uuid获取租户id并设置数据源地址
     * @param uuid
     * @return
     */
    String getTenantId(String uuid);
}
