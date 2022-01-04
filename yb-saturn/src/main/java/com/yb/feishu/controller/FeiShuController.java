package com.yb.feishu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.common.DateUtil;
import com.yb.feishu.config.FeiShuURLConstant;
import com.yb.feishu.entity.FeishuDept;
import com.yb.feishu.entity.FeishuUserinfo;
import com.yb.feishu.service.FeishuDeptService;
import com.yb.feishu.service.FeishuUserinfoService;
import com.yb.feishu.utils.ClockInHeadersutil;
import com.yb.feishu.utils.FeiShuAccessTokenUtil;
import com.yb.feishu.utils.FeiShuResult;
import com.yb.feishu.utils.HttpClientUtil;
import com.yb.feishu.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.tool.ObjectMapperUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.yb.feishu.config.FeiShuURLConstant.*;

@RestController
@RequestMapping("/FeiShuController")
@Api(tags = "飞书对接接口")
@Slf4j
public class FeiShuController {

    @Autowired
    private FeishuDeptService feishuDeptService;
    @Autowired
    private FeishuUserinfoService feishuUserinfoService;
    @Autowired
    private IBaseStaffinfoService baseStaffinfoService;

    /*分页默认*/
    private final int PAGE_SIZE = 100;

    @GetMapping("/importFeiShuDept")
    @ApiOperation(value = "导入飞书部门数据")
    public R importFeiShuDept() {
        /*获取通讯录授权对象*/
        ScopeGet scopeGet = getScopeGet();
        if (scopeGet == null) {
            return R.fail("导入失败");
        }
        /*批量获取飞书部门详情*/
        FeiShuDeptinfo feiShuDeptinfo = getFeiShuDept(scopeGet);
        if (feiShuDeptinfo == null) {
            return R.fail("导入失败");
        }
        List<DepartmentInfos> departmentInfos = feiShuDeptinfo.getDepartmentInfos();
        FeishuDept feishuDept;
        Date date;
        for (DepartmentInfos departmentInfo : departmentInfos) {
            String fsId = departmentInfo.getId();
            feishuDept = feishuDeptService.getOne(new QueryWrapper<FeishuDept>().eq("fs_id", fsId));
            date = new Date();
            if (feishuDept == null) {
                feishuDept = new FeishuDept();
            }
            feishuDept.setChatId(departmentInfo.getChatId());
            feishuDept.setLeaderOpenId(departmentInfo.getLeaderOpenId());
            if (departmentInfo.getMemberCount() != null) {
                feishuDept.setMemberCount(departmentInfo.getMemberCount().toString());
            }
            feishuDept.setLeaderEmployeeId(departmentInfo.getLeaderEmployeeId());
            feishuDept.setName(departmentInfo.getName());
            feishuDept.setOpenDepartmentId(departmentInfo.getOpenDepartmentId());
            feishuDept.setParentId(departmentInfo.getParentId());
            feishuDept.setParentOpenDepartmentId(departmentInfo.getParentOpenDepartmentId());
            if (departmentInfo.getStatus() != null) {
                feishuDept.setStatus(departmentInfo.getStatus().toString());
            }
            feishuDept.setUpdateAt(date);
            feishuDept.setFsId(departmentInfo.getId());
            feishuDept.setCreateAt(date);
            feishuDeptService.saveOrUpdate(feishuDept);
        }
        List<String> authedOpenDepartments = scopeGet.getAuthedOpenDepartments();
        for (String authedOpenDepartment : authedOpenDepartments) {
            try {
                importFeiShuUser(authedOpenDepartment, null);
            } catch (RuntimeException e) {
                log.error("--------------------插入飞书用户数据时出错------------------");
                return R.fail("导入失败");
            }
        }
        return R.success("导入成功");
    }

    /**
     * 获取通讯录授权对象
     *
     * @return
     */
    public ScopeGet getScopeGet() {
        try {
            String tenantAccessToken = FeiShuAccessTokenUtil.getTenantAccessToken();
            FeiShuResult feiShuResult =
                    ObjectMapperUtil.toObject(HttpClientUtil.sendHttpGet(
                            FeiShuURLConstant.SCOPE_GET, tenantAccessToken), FeiShuResult.class);
            ScopeGet scopeGet = ObjectMapperUtil.toObject(ObjectMapperUtil.toJSON(feiShuResult.getData()), ScopeGet.class);
            return scopeGet;
        } catch (Exception e) {
            log.error("--------------获取通讯录授权对象出现错误-----------------");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量获取飞书部门详情
     *
     * @param scopeGet
     * @return
     */
    public FeiShuDeptinfo getFeiShuDept(ScopeGet scopeGet) {
        try {
            List<String> authedOpenDepartments = scopeGet.getAuthedOpenDepartments();
            StringBuffer departmentIdBuffer = new StringBuffer();
            for (int i = 0; i < authedOpenDepartments.size(); i++) {
                String authedOpenDepartment = authedOpenDepartments.get(i);
                if (i == 0) {
                    departmentIdBuffer.append("?");
                }
                departmentIdBuffer.append("department_ids=");
                departmentIdBuffer.append(authedOpenDepartment);
                if (i < authedOpenDepartments.size() - 1) {
                    departmentIdBuffer.append("&");
                }
            }
            String url = FeiShuURLConstant.DEPARTMENT_DETAIL_BATCH_GET;
            if (departmentIdBuffer != null) {
                url = url + departmentIdBuffer.toString();
            }
            String tenantAccessToken = FeiShuAccessTokenUtil.getTenantAccessToken();
            FeiShuResult feiShuResult = ObjectMapperUtil.toObject(HttpClientUtil.sendHttpGet(
                    url, tenantAccessToken), FeiShuResult.class);
            FeiShuDeptinfo feiShuDeptinfo =
                    ObjectMapperUtil.toObject(ObjectMapperUtil.toJSON(feiShuResult.getData()), FeiShuDeptinfo.class);
            return feiShuDeptinfo;
        } catch (Exception e) {
            log.error("--------------飞书获取部门时出错---------------");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询飞书部门下人员信息
     *
     * @param openDepartmentId
     * @return
     */
    public FeiShuUser getFeiShuUser(String openDepartmentId, String pageToken) {

        try {
            String tenantAccessToken = FeiShuAccessTokenUtil.getTenantAccessToken();
            String url = FeiShuURLConstant.USER_DETAIL_LIST +
                    "?open_department_id=" + openDepartmentId + "&page_size=" + PAGE_SIZE + "&fetch_child=true";
            if (!StringUtil.isEmpty(pageToken)) {
                url = url + "&page_token" + pageToken;
            }
            FeiShuResult feiShuResult = ObjectMapperUtil.toObject(
                    HttpClientUtil.sendHttpGet(url, tenantAccessToken), FeiShuResult.class);
            FeiShuUser feiShuUser =
                    ObjectMapperUtil.toObject(ObjectMapperUtil.toJSON(feiShuResult.getData()), FeiShuUser.class);
            return feiShuUser;
        } catch (Exception e) {
            log.error("-------------获取飞书用户信息出错--------------");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导入飞书部门下人员信息
     *
     * @param openDepartmentId
     * @return
     */
    public void importFeiShuUser(String openDepartmentId, String pageToken) {

        try {
            String tenantAccessToken = FeiShuAccessTokenUtil.getTenantAccessToken();
            String url = FeiShuURLConstant.USER_DETAIL_LIST +
                    "?open_department_id=" + openDepartmentId + "&page_size=" + PAGE_SIZE + "&fetch_child=true";
            if (!StringUtil.isEmpty(pageToken)) {
                url = url + "&page_token" + pageToken;
            }
            FeiShuResult feiShuResult = ObjectMapperUtil.toObject(
                    HttpClientUtil.sendHttpGet(url, tenantAccessToken), FeiShuResult.class);
            FeiShuUser feiShuUser =
                    ObjectMapperUtil.toObject(ObjectMapperUtil.toJSON(feiShuResult.getData()), FeiShuUser.class);
            /*调用插入数据库的方法*/
            List<UserInfos> userInfos = feiShuUser.getUserInfos();
            for (UserInfos userInfo : userInfos) {
                setFeiShuUserinfo(userInfo);
            }
            pageToken = feiShuUser.getPageToken();
            if (!StringUtil.isEmpty(pageToken)) {//如果还有下一页用户再调用自身
                importFeiShuUser(openDepartmentId, pageToken);
            }
        } catch (Exception e) {
            log.error("-------------获取飞书用户信息出错--------------");
            e.printStackTrace();
        }
    }

    /**
     * 插入飞书数据
     *
     * @param userInfo
     */
    @Transactional(rollbackFor = Exception.class)
    public void setFeiShuUserinfo(UserInfos userInfo) throws RuntimeException {
        String unionId = userInfo.getUnionId();
        String mobile = userInfo.getMobile();
        String openId = userInfo.getOpenId();
        FeishuUserinfo feishuUserinfo =
                feishuUserinfoService.getOne(new QueryWrapper<FeishuUserinfo>().eq("union_id", unionId));
        Date date = new Date();
        if (feishuUserinfo == null) {
            feishuUserinfo = new FeishuUserinfo();
            feishuUserinfo.setCreateAt(date);
        }
        feishuUserinfo.setAvatar(userInfo.getAvatarUrl());
        feishuUserinfo.setAvatar_72(userInfo.getAvatar72());
        feishuUserinfo.setCity(userInfo.getCity());
        feishuUserinfo.setCountry(userInfo.getCountry());
        if (userInfo.getCustomAttrs() != null) {
            feishuUserinfo.setCustomAttrs(userInfo.getCustomAttrs().toString());
        }
        if (!userInfo.getDepartments().isEmpty()) {
            feishuUserinfo.setDepartments(userInfo.getDepartments().toString());
        }
        feishuUserinfo.setDescription(userInfo.getDescription());
        feishuUserinfo.setEmail(userInfo.getEmail());
        feishuUserinfo.setEmployeeId(userInfo.getEmployeeId());
        feishuUserinfo.setEmployeeNo(userInfo.getEmployeeNo());
        if (userInfo.getEmployeeType() != null) {
            feishuUserinfo.setEmployeeType(userInfo.getEmployeeType().toString());
        }
        feishuUserinfo.setEnName(userInfo.getEnName());
        if (userInfo.getGender() != null) {
            feishuUserinfo.setGender(userInfo.getGender().toString());
        }
        if (userInfo.getIsTenantManager() != null) {
            feishuUserinfo.setIsTenantManager(userInfo.getIsTenantManager().toString());
        }
        feishuUserinfo.setJoinTime(userInfo.getJoinTime());
        feishuUserinfo.setLeaderEmployeeId(userInfo.getLeaderEmployeeId());
        feishuUserinfo.setLeaderOpenId(userInfo.getLeaderOpenId());
        feishuUserinfo.setLeaderUnionId(userInfo.getLeaderUnionId());
        feishuUserinfo.setMobile(userInfo.getMobile());
        feishuUserinfo.setName(userInfo.getName());
        feishuUserinfo.setNamePy(userInfo.getNamePy());
        if (!userInfo.getOpenDepartments().isEmpty()) {
            feishuUserinfo.setOpenDepartments(userInfo.getOpenDepartments().toString());
        }
        feishuUserinfo.setOpenId(openId);
        if (userInfo.getStatus() != null) {
            feishuUserinfo.setStatus(userInfo.getStatus().toString());
        }
        feishuUserinfo.setUnionId(userInfo.getUnionId());
        feishuUserinfo.setUpdateAt(date);
        if (userInfo.getUpdateTime() != null) {
            feishuUserinfo.setUpdateTime(userInfo.getUpdateTime().toString());
        }
        feishuUserinfo.setWorkStation(userInfo.getWorkStation());
        feishuUserinfoService.saveOrUpdate(feishuUserinfo);
        mobile = mobile.substring(3);//+86开头
        /*todo:字段未加 */
        baseStaffinfoService.updateFeiShuIdByPhone(openId, mobile);
    }


    /**
     * 获取飞书打卡流水记录
     *
     * @return
     */
    @GetMapping("/getFeiShuUsersCheckListRecords")
    @ApiOperation(value = "获取飞书打卡流水记录")
    public R getFeiShuUsersCheckListRecords(@ApiParam(value = "用户的飞书employeeId") @RequestParam("employeeNos") List<String> employeeNos,
                                            @ApiParam(value = "开始时间'例:20201212'") @RequestParam("checkDateFrom") Integer checkDateFrom,
                                            @ApiParam(value = "结束时间") @RequestParam("checkDateTo") Integer checkDateTo) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> headers = ClockInHeadersutil.getHeaders();
//        List<String> employeeNos = new ArrayList();
//        employeeNos.add("6eb4fgef");
//        employeeNos.add("ae173f1c");
        map.put("employeeNos", employeeNos);
        map.put("checkDateFrom", checkDateFrom);
        map.put("checkDateTo", checkDateTo);
        map.put("employeeKeyType", 1);
        map.put("timeZone", "UTC+8");
        GetCheckDataVO getCheckDataVO = ObjectMapperUtil.toObject(
                HttpClientUtil.sendHttpPost(GET_USERS_CHECK_LIST_RECORDS, ObjectMapperUtil.toJSON(map), headers),
                GetCheckDataVO.class);
        List<String> invalidEmployeeNos = getCheckDataVO.getInvalidEmployeeNos();
        if (!invalidEmployeeNos.isEmpty()) {
            log.error("出现无效的情况包含,不合法的 employeeNo 或 employeeNo 不在授权范围" + invalidEmployeeNos);
        }
        if (getCheckDataVO.getErrcode() != 0) {
            return R.fail("获取飞书打卡流水记录失败");
        }
        return R.data(getCheckDataVO.getRecordresult());
    }

    @PostMapping("/importUserCheckRecords")
    @ApiOperation(value = "飞书打卡流水导入")
    public R importUserCheckRecords(@RequestBody ImportCheckParamVO importCheckParamVO) {
        String toJSON = ObjectMapperUtil.toJSON(importCheckParamVO);
        Map<String, String> headers = ClockInHeadersutil.getHeaders();
        GetCheckDataVO getCheckDataVO = ObjectMapperUtil.toObject(
                HttpClientUtil.sendHttpPost(IMPORT_USER_CHECK_RECORDS, toJSON, headers), GetCheckDataVO.class);
        if (getCheckDataVO.getErrcode() == 0) {
            return R.success("导入成功");
        }
        return R.fail("导入失败");
    }

    @PostMapping("/feiShuSendText")
    @ApiOperation(value = "飞书消息发送", notes = "用户的飞书open_id, 飞书群id必传其一;消息类型必传(text),消息内容必传")
    public R feiShuSendText(@RequestBody FeiShuSendVO feiShuSendVO) {
        String toJSON = ObjectMapperUtil.toJSON(feiShuSendVO);
        String tenantAccessToken = FeiShuAccessTokenUtil.getTenantAccessToken();
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + tenantAccessToken);
        String httpPost = HttpClientUtil.sendHttpPost(SEND, toJSON, header);
        System.err.println(httpPost);
        return R.success("消息发送成功");
    }


    public static void main(String[] args) {
//        Calendar cal = Calendar.getInstance();
//        // 返回当前系统的UTC时间，具体实现可参看JDK源码
//        long expiredTime = cal.getTimeInMillis() + 600;
//        System.out.println(expiredTime);
//        String appSecret = "d8c8524ad1e12d7752931d3e82383112";
////        String signatureNonce = "5gdggdig30gdgdifdgne24eteddgggyu";
//        String signatureNonce = UUID.randomUUID().toString().trim().replaceAll("-", "");
////        long expiredTime = 1609982710;
//        String encrypt = HmacUtils.hmacSha1Hex(appSecret, signatureNonce + expiredTime);
//        System.out.println(signatureNonce);
//        System.out.println(encrypt);

        Calendar cal = Calendar.getInstance();
        // 返回当前系统的UTC时间，具体实现可参看JDK源码
        long expiredTime = cal.getTimeInMillis() + 1000 * 60 * 10;//十分钟过期
        Date date = new Date(expiredTime);
        System.out.println(DateUtil.toDatestr(date, "yyyy-MM-dd HH:mm:ss"));
    }

}
