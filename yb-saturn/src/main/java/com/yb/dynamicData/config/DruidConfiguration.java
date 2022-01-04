package com.yb.dynamicData.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * druid监控界面设置
 */
@Configuration
public class DruidConfiguration {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource dataSourceOne() throws SQLException {

        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://39.100.199.29:3306/yb_test?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=Asia/Shanghai");
        dataSource.setUsername("root");
        dataSource.setPassword("ybbqwert");
        //连接池配置
        dataSource.setMaxActive(30);
        dataSource.setMinIdle(20);
        dataSource.setInitialSize(6);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(2000);
        dataSource.setMinEvictableIdleTimeMillis(600000);
        dataSource.setTestWhileIdle(true);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setPoolPreparedStatements(true);
        dataSource.setKeepAlive(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(30);
        dataSource.addFilters("stat");
        dataSource.addFilters("wall");
        return dataSource ;
    }

    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatViewServle() {
        //注册服务
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
                new StatViewServlet(), "/druid/*");
        // 白名单(为空表示,所有的都可以访问,多个IP的时候用逗号隔开)
        // 设置登录的用户名和密码
        servletRegistrationBean.addInitParameter("loginUsername", "root");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        // 是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    //注册一个filters
    @Bean
    public FilterRegistrationBean druidStatFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
 
