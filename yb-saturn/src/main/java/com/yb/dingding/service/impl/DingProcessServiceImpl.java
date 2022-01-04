package com.yb.dingding.service.impl;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiProcessGetByNameRequest;
import com.dingtalk.api.request.OapiProcessinstanceCreateRequest;
import com.dingtalk.api.response.OapiProcessGetByNameResponse;
import com.dingtalk.api.response.OapiProcessinstanceCreateResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.taobao.api.ApiException;
import com.yb.dingding.entity.DingAppinfo;
import com.yb.dingding.entity.DingProcessinstanceCreate;
import com.yb.dingding.entity.ProcessCode;
import com.yb.dingding.entity.ProcessinstanceCreateParam;
import com.yb.dingding.service.DingProcessService;
import com.yb.dingding.util.DingAccessTokenUtil;
import com.yb.dingding.util.DingUserUtil;
import org.springblade.common.tool.ObjectMapperUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yb.dingding.config.URLConstant.GET_BY_NAME;
import static com.yb.dingding.config.URLConstant.PROCESSINSTANCE_CREATE;

@Service
public class DingProcessServiceImpl implements DingProcessService {


    @Override
    public DingProcessinstanceCreate processinstanceCreate(ProcessinstanceCreateParam processinstanceCreateParam) {
        String templateName = processinstanceCreateParam.getTemplateName();//模板名称
        String userId = processinstanceCreateParam.getUserId();//钉钉用户id
        Map<String, String> map = processinstanceCreateParam.getMap();//输入框名及值
        String apUnique = "InternalH5";
        String token = DingAccessTokenUtil.getToken(apUnique);
        DingAppinfo dingAppinfo = DingAccessTokenUtil.getDingAppinfo(apUnique);
        if (dingAppinfo == null) {
            return null;
        }
        String agentid = dingAppinfo.getAgentid();
        OapiUserGetResponse dingUserDetail = DingUserUtil.getDingUserDetail(token, userId);
        if (dingUserDetail == null) {
            return null;
        }
        Long deptId = null;
        List<Long> department = dingUserDetail.getDepartment();
        if (department != null && !department.isEmpty()) {
            deptId = department.get(0);
        }
        try {
            String processCode = getByName(templateName);
            if (StringUtil.isEmpty(processCode)) {
                return null;
            }
            DingTalkClient client = new DefaultDingTalkClient(PROCESSINSTANCE_CREATE);
            OapiProcessinstanceCreateRequest req = new OapiProcessinstanceCreateRequest();
            req.setAgentId(Long.valueOf(agentid));
            req.setProcessCode(processCode);
            req.setOriginatorUserId(userId);
            req.setDeptId(deptId);
            List<OapiProcessinstanceCreateRequest.FormComponentValueVo> list2 = new ArrayList<>();
            OapiProcessinstanceCreateRequest.FormComponentValueVo obj3;
            for (String key : map.keySet()) {
                obj3 = new OapiProcessinstanceCreateRequest.FormComponentValueVo();
                obj3.setName(key);
                obj3.setValue(map.get(key));
                list2.add(obj3);
            }
            req.setFormComponentValues(list2);
            OapiProcessinstanceCreateResponse rsp = client.execute(req, token);
            DingProcessinstanceCreate dingProcessinstanceCreate = ObjectMapperUtil.toObject(rsp.getBody(), DingProcessinstanceCreate.class);
            int errcode = dingProcessinstanceCreate.getErrcode();
            if (errcode != 0) {
                return null;
            }
            return dingProcessinstanceCreate;
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据模板名称获取模板code
     * @param templateName
     * @return
     */
    private String getByName(String templateName) {
        try {
            String token = DingAccessTokenUtil.getToken("InternalH5");
            DingTalkClient client = new DefaultDingTalkClient(GET_BY_NAME);
            OapiProcessGetByNameRequest req = new OapiProcessGetByNameRequest();
            req.setName(templateName);
            OapiProcessGetByNameResponse rsp = client.execute(req, token);
            System.out.println(rsp.getBody());
            ProcessCode processCode = ObjectMapperUtil.toObject(rsp.getBody(), ProcessCode.class);
            return processCode.getProcessCode();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }
}
