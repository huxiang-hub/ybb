package com.vim.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 图片绝对地址与虚拟地址映射
 */

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${web.upload-path}")
    private String uploadFolder;
    @Value("${web.download-path}")
    private String url;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler(url+"*").addResourceLocations("file:" + uploadFolder);
        super.addResourceHandlers(registry);
    }
}
