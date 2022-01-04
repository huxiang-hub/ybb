package org.springblade.common.resubmit.service.impl;


import org.springblade.common.resubmit.service.ReSubmitService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author my
 * @Date Created in 2020/6/10
 */
@Service
public class ReSubmitServiceImpl implements ReSubmitService {

    /**
     * 通过请求地址url获取上次请求数据
     *
     * @param key key
     * @return 上次数据
     */
    @Override
    @Cacheable(value = "sys:cache:resubmit", key = "#key", sync = true)
    public Map getByKey(String key) {
        return null;
    }

    /**
     * 通过请求地址url保存请求数据
     *
     * @param key     key
     * @param dataMap 请求数据
     */
    @Override
    @CachePut(value = "sys:cache:resubmit", key = "#key")
    public Map setByKey(String key, Map dataMap) {
        return dataMap;
    }
}
