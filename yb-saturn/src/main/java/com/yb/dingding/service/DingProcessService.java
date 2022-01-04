package com.yb.dingding.service;

import com.yb.dingding.entity.DingProcessinstanceCreate;
import com.yb.dingding.entity.ProcessinstanceCreateParam;

public interface DingProcessService {
    /**
     * 钉钉创建审核流程
     * @param processinstanceCreateParam
     * @return
     */
    DingProcessinstanceCreate processinstanceCreate(ProcessinstanceCreateParam processinstanceCreateParam);
}
