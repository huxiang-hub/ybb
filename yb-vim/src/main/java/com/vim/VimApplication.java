package com.vim;

import org.springblade.core.launch.BladeApplication;
import org.springblade.core.launch.constant.AppConstant;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author by SUMMER
 * @date 2020/3/9.
 */
@SpringBootApplication
@SpringCloudApplication
@EnableFeignClients(basePackages = AppConstant.BASE_PACKAGES)
public class VimApplication {
    public static void main(String[] args) {

        BladeApplication.run("yb-vim", VimApplication.class, args);
    }
}
