package com.yb.dynamicData.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.yb.dynamicData.datasource.DynamicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 数据源配置管理。
 *
 * @author elon
 * @version 2018年2月26日
 */
@Configuration
@MapperScan(basePackages = "com.yb.**.mapper", value = "sqlSessionFactory")
public class DataSourceConfig {

    /**
     * 根据配置参数创建数据源。使用派生的子类。
     *
     * @return 数据源
     */
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource() {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.type(DynamicDataSource.class);
        DataSource dataSource = builder.build();
        return dataSource;
    }

    /**
     * 创建会话工厂。
     *
     * @param dataSource 数据源
     * @return 会话工厂
     */
    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws IOException {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        try {
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/yb/**/mapper/*Mapper.xml"));
            bean.setDataSource(dataSource);
            MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
            mybatisConfiguration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
            mybatisConfiguration.addInterceptor(mybatisPlusInterceptor());
            bean.setConfiguration(mybatisConfiguration);
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //向Mybatis过滤器链中添加分页拦截器
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(10000L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}

