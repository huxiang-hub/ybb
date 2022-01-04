package com.anaysis.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;


@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = {DataSourceConfig.PACKAGE}, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {
    static final String PACKAGE = "com.anaysis.mysqlmapper";//对应的mysql dao层包

    /**
     * 根据配置参数创建数据源。使用派生的子类。
     *
     * @return 数据源
     */
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource() throws IllegalAccessException {
//        DataSourceBuilder builder = DataSourceBuilder.create();
//        builder.type(DynamicDataSource.class);
//        DataSource dataSource = builder.build();
        DataSource dataSource = initDDS();
        return dataSource;
    }

    private DataSource initDDS() throws IllegalArgumentException, IllegalAccessException {
        org.apache.tomcat.jdbc.pool.DataSource dds = new org.apache.tomcat.jdbc.pool.DataSource();
        dds.setUrl("jdbc:mysql://39.98.57.236:3378/yb_test?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8");
        dds.setValidationQuery("SELECT 1");
        dds.setUsername("root");
        dds.setPassword("ybbqwert");
        dds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dds.setInitialSize(20);
        dds.setMaxIdle(10);
        dds.setMaxActive(1000);
        dds.setMaxWait(60000);
        dds.setMinEvictableIdleTimeMillis(300000);
        dds.setTestWhileIdle(true);
        dds.setTestOnBorrow(true);
        dds.setTestOnReturn(true);
        return dds;
    }
    /**
     * 创建会话工厂。
     *
     * @param dataSource 数据源
     * @return 会话工厂
     */
    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:/com/anaysis/mysqlmapper/*.xml"));
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();

        mybatisConfiguration.setLogImpl(StdOutImpl.class);
        sessionFactory.setConfiguration(mybatisConfiguration);

        return sessionFactory.getObject();
    }

    @Bean
    @Primary
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDialectType("mysql");
        return paginationInterceptor;
    }
}

