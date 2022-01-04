package com.yb.synchrodata.service.impl;

import com.yb.mater.vo.HrMaterial;
import com.yb.synchrodata.constant.NxhrUrlConstant;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * @Author lzb
 * @Date 2021/1/10
 **/
@Service
public class NxhrMaterialService {

    private static RestTemplate restTemplate = new RestTemplate();

    public HrMaterial selectByBarCode(String barCode) {
        return restTemplate.getForObject(NxhrUrlConstant.GET_MATERIAL_URL+"?barCode=" + barCode, HrMaterial.class);
    }
}
