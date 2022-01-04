package com.yb.order.config;

import com.yb.interceptor.ReSubmitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 图片绝对地址与虚拟地址映射
 */

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ReSubmitInterceptor reSubmitInterceptor;

    @Value("${web.upload-path}")
    private String uploadFolder; //D：/temp
    //    private String uploadFolder = "/home/img/"; //D：/temp
    @Value("${web.download-path}")
    private String url; //  /img/
//    private String url = "/home/download/"; //  /img/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler(url + "*").addResourceLocations("file:" + uploadFolder);
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    /**
     * 自定义拦截规则
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(reSubmitInterceptor).addPathPatterns("/**");
    }
}
