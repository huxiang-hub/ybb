package com.sso.launcher;

import org.springblade.common.constant.LauncherConstant;
import org.springblade.core.launch.constant.NacosConstant;
import org.springblade.core.launch.service.LauncherService;
import org.springblade.core.launch.utils.PropsUtil;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Properties;

/**
 * @author by my
 * @date 2020/05/29.
 */
public class SsoLauncherImpl implements LauncherService {
    @Override
    public void launcher(SpringApplicationBuilder builder, String appName, String profile) {
        Properties props = System.getProperties();
        PropsUtil.setProperty(props, "spring.cloud.nacos.config.ext-config[0].data-id", NacosConstant.dataId("yb-sso", profile));
        PropsUtil.setProperty(props, "spring.cloud.nacos.config.ext-config[0].group", NacosConstant.NACOS_CONFIG_GROUP);
        PropsUtil.setProperty(props, "spring.cloud.nacos.config.ext-config[0].refresh", NacosConstant.NACOS_CONFIG_REFRESH);
        //配置服务注册中心地址。
        props.setProperty("spring.cloud.nacos.discovery.server-addr", LauncherConstant.NACOS_DEV_ADDR);
    }

    @Override
    public int getOrder() {
        return 20;
    }
}
