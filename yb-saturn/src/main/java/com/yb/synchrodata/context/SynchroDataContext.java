package com.yb.synchrodata.context;

import com.yb.synchrodata.service.SynchroDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author lzb
 * @Date 2020/12/15 13:24
 **/
@Service
public class SynchroDataContext {

    @Autowired
    Map<String, SynchroDataService> map;

    public SynchroDataService getSynchroDataService(String tenantId) {
        return map.get(tenantId);
    }
}
