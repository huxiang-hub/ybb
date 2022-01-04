package com.anaysis.mongodb;

import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @Description: 租户对应数据名
 * @Author my
 * @Date Created in 2020/7/2
 */
@Component
public class MongoDbs {
    public static HashMap<String, String> map = new HashMap();

    MongoDbs() {
        //宝峰
        map.put("baofeng", "baofeng");
        map.put("nxjsj", "jsj");
        map.put("fuli", "fuli");
        map.put("hbhr", "herui");
        map.put("xingyi", "xingyi");
        map.put("demo", "demo");
        map.put("nxhr", "nxhr");
    }
}
