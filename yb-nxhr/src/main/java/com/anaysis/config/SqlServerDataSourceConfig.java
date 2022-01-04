package com.anaysis.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
//扫描 Mapper 接口并容器管理
@MapperScan(basePackages = SqlServerDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "sqlserverSqlSessionFactory")
public class SqlServerDataSourceConfig {

    // 精确到 sqlserver 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.anaysis.sqlservermapper";
    static final String MAPPER_LOCATION = "classpath:/com/anaysis/sqlservermapper/*.xml";

    @Value("${sqlserver.datasource.url}")
    private String url;

    @Value("${sqlserver.datasource.username}")
    private String user;

    @Value("${sqlserver.datasource.password}")
    private String password;

    @Value("${sqlserver.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "sqlserverDataSource")
    public DataSource sqlserverDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }
    @Bean(name = "secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(@Qualifier("sqlserverDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


    @Bean(name = "sqlserverTransactionManager")
    public DataSourceTransactionManager sqlserverTransactionManager(@Qualifier("sqlserverDataSource") DataSource sqlServerDataSource) {
        return new DataSourceTransactionManager(sqlServerDataSource);
    }


    @Bean(name = "sqlserverSqlSessionFactory")
    public SqlSessionFactory sqlserverSqlSessionFactory(@Qualifier("sqlserverDataSource") DataSource sqlserverDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        mybatisConfiguration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        sessionFactory.setConfiguration(mybatisConfiguration);
        sessionFactory.setDataSource(sqlserverDataSource);
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(SqlServerDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }



    @Bean(name = "sqlserverSqlSessionTemplate")
    public SqlSessionTemplate sqlserverSqlSessionTemplate(
            @Qualifier("sqlserverSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
