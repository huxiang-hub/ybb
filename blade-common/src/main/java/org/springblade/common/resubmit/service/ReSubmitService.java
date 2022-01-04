package org.springblade.common.resubmit.service;

import java.util.Map;

/**
 * @Author my
 * @Date Created in 2020/6/10
 */
public interface ReSubmitService {
    /**
     * 通过请求地址url获取上次请求数据
     *
     * @param key 数据保存key
     * @return 上次数据
     */
    Map getByKey(String key);

    /**
     * 通过请求地址url保存请求数据
     *
     * @param key 请求地址
     * @param dataMap 请求数据
     * @return Map
     */
    Map setByKey(String key, Map dataMap);
}
