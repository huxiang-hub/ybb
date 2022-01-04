package com.anaysis.dynamicData.datasource;


import com.anaysis.dynamicData.dbmgr.ProjectDBMgr;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 定义动态数据源派生类。从基础的DataSource派生，动态性自己实现。
 *
 * @author elon
 * @version 2018-02-25
 */
public class DynamicDataSource extends DataSource {

    private static Logger log = LogManager.getLogger(DynamicDataSource.class);

    /**
     * 改写本方法是为了在请求不同工程的数据时去连接不同的数据库。
     */
    @Override
    public Connection getConnection() {
        String projectCode = DBIdentifier.getProjectCode();
        if (projectCode == null) {
            projectCode = "000000";
        }
        //1、获取数据源
        DataSource dds = DDSHolder.instance().getDDS(projectCode);
        //2、如果数据源不存在则创建
        if (dds == null) {
            try {
                DataSource newDDS = initDDS(projectCode);
                DDSHolder.instance().addDDS(projectCode, newDDS);
            } catch (Exception e) {
                log.error("Init data source fail. projectCode:" + projectCode);
                return null;
            }
        }
        dds = DDSHolder.instance().getDDS(projectCode);
        try {
            return dds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 以当前数据对象作为模板复制一份。
     *
     * @return dds
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private DataSource initDDS(String projectCode) throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {

        DataSource dds = new DataSource();

        // 2、复制PoolConfiguration的属性
        PoolProperties property = new PoolProperties();
        Field[] pfields = PoolProperties.class.getDeclaredFields();
        for (Field f : pfields) {
            f.setAccessible(true);
            Object value = f.get(this.getPoolProperties());

            try {
                f.set(property, value);
            } catch (Exception e) {
                //有一些static final的属性不能修改。忽略。
                log.info("Set value fail. attr name:" + f.getName());
                continue;
            }
        }
        dds.setPoolProperties(property);
        String url = "jdbc:mysql://%s:%s?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=Asia/Shanghai";

        // 3、设置数据库名称和IP(一般来说，端口和用户名、密码都是统一固定的)
        String urlFormat = this.getUrl();
        String DBIP = ProjectDBMgr.instance().getDBIP(projectCode);
        String DBNAME = ProjectDBMgr.instance().getDBName(projectCode);
        if (!DBNAME.equals("baofeng")) {
            urlFormat = String.format(url, DBIP, DBNAME);
        }
//        if (projectCode.equals("ceshi")) {
//            dds.setUsername("root");
//            dds.setPassword("ADMIN");
//        }
        System.out.println(projectCode+"!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        dds.setUrl(urlFormat);
        dds.setValidationQuery("SELECT 1");
        dds.setInitialSize(10);
        dds.setMaxIdle(10);
        dds.setMaxActive(200);
        dds.setMaxWait(60000);
        dds.setMinEvictableIdleTimeMillis(30000);
        dds.setTestWhileIdle(true);
        dds.setTestOnBorrow(true);
        dds.setTestOnReturn(true);
        return dds;
    }
}
