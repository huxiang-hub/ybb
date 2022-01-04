package com.anaysis.Influx;

import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/9/14 16:11
 */
@Component
@Slf4j
public class InfluxDBRepo<T> {
    @Autowired
    InfluxDB influxDB;

    private String dbName = "ybsaturn";
    private String tableName = "collect_data";

    /**
     * 添加
     *
     * @param obj
     * @param sendTime
     */
    public void post(Object obj, Long sendTime, String database) {
        //判断数据库是否存在，不存在创建数据库
        if (!influxDB.databaseExists(database)) {
            log.info("数据库不存在，创建数据库！");
            influxDB.createDatabase(database);
        }
        //获取对象字段名和字段值
        Field[] fields = obj.getClass().getDeclaredFields();
        //创建写入对象
        Point.Builder point = Point.measurement(tableName);
        point = addKeyValue(obj, fields, point);
        //设置添加时间
        point.time(sendTime, TimeUnit.MILLISECONDS);
        //设置操作数据库
        influxDB.setDatabase(database);
        //写入
        influxDB.write(point.build());
        //关闭
        influxDB.close();
    }

    /**
     * 查询
     *
     * @param sql
     * @param aClass
     * @return
     */
    public List<T> gets(String sql, Class<T> aClass) {
        //判断数据库是否存在，不存在创建数据库
        if (!influxDB.databaseExists(dbName)) {
            throw new RuntimeException("数据库不存在！");
        }
        QueryResult queryResult = influxDB.query(new Query(sql, dbName));
        influxDB.close();
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(queryResult, aClass);
    }

    private Point.Builder addKeyValue(Object obj, Field[] fields, Point.Builder point) {
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String fieldName = field.getName();
                String typeName = field.getGenericType().getTypeName();
                int start = typeName.lastIndexOf(".") + 1;
                //数据类型
                String type = typeName.substring(start, typeName.length());
                //字段值
                Object o = field.get(obj);
                //判断类型，为空设置默认值
                if (o == null) {
                    point.addField(fieldName, "");
                    continue;
                }
                switch (type) {
                    case "Instant":
                        break;
                    case "String":
                        point.addField(fieldName, (String) o);
                        break;
                    case "Integer":
                        point.addField(fieldName, (Integer) o);
                        break;
                    case "long":
                        point.addField(fieldName, (Long) o);
                        break;
                    default:
                        throw new IllegalArgumentException("参数异常:fieldName:" + fieldName);
                }
            } catch (Exception e) {
                log.error("参数异常:{}", e.getMessage());
            }
        }
        return point;
    }


}
