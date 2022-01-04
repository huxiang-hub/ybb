package com.yb.dingding.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.common.DateUtil;
import com.yb.dingding.config.URLConstant;
import com.yb.dingding.entity.*;
import com.yb.dingding.service.DingDeptService;
import com.yb.dingding.service.DingUserinfoService;
import com.yb.dingding.util.DingAccessTokenUtil;
import com.yb.dingding.util.ServiceResult;
import com.yb.fastdfs.FileSystem;
import com.yb.fastdfs.controller.FileController;
import com.yb.fastdfs.utils.ImageUtils;
import com.yb.system.user.service.SaIUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.common.tool.ObjectMapperUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业内部内部-小程序Quick-Start示例代码 实现了最简单的免密登录（免登）功能
 */
@RestController
@Slf4j
@RequestMapping("/InternalH5")//标识内部H5应用
@Api(tags = "钉钉数据导入")
public class InternalH5Controller {
    private static final Logger bizLogger = LoggerFactory.getLogger(InternalH5Controller.class);

    @Autowired
    private DingDeptService dingDeptService;
    @Autowired
    private DingUserinfoService dingUserinfoService;
    @Autowired
    private IBaseStaffinfoService baseStaffinfoService;
    @Autowired
    private FileController fileController;
    @Autowired
    private SaIUserService saIUserService;

    /*分页查询所需*/
    private final Long OFFSET = 0L;
    private final Long SIZE = 100L;
    /**
     * 欢迎页面,通过url访问，判断后端服务是否启动
     */
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome(String apUnique) {
        String accessToken = DingAccessTokenUtil.getToken(apUnique);
        return "welcome:"+accessToken;
    }

    /**
     * 钉钉用户登录，显示当前登录用户的userId和名称
     *
     * @param requestAuthCode 免登临时code
     *
     */
    @RequestMapping(value = "/ddlogin", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult login(@RequestParam(value = "authCode") String requestAuthCode, String apUnique) {
        //获取accessToken,注意正是代码要有异常流处理
        String accessToken = DingAccessTokenUtil.getToken(apUnique);
        //获取用户信息
        DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_GET_USER_INFO);
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(requestAuthCode);
        request.setHttpMethod("GET");

        OapiUserGetuserinfoResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
        //3.查询得到当前用户的userId
        // 获得到userId之后应用应该处理应用自身的登录会话管理（session）,避免后续的业务交互（前端到应用服务端）每次都要重新获取用户身份，提升用户体验
        String userId = response.getUserid();
        String userName = getUserName(accessToken, userId);
        System.out.println(userId);
        System.out.println(userName);
        //返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("userName", userName);
        ServiceResult serviceResult = ServiceResult.success(resultMap);
        return serviceResult;
    }

    /**
     * 获取用户姓名
     * @param accessToken
     * @param userId
     * @return
     */
    public static String getUserName(String accessToken, String userId) {
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_USER_GET);
            OapiUserGetRequest request = new OapiUserGetRequest();
            request.setUserid(userId);
            request.setHttpMethod("GET");
            OapiUserGetResponse response = client.execute(request, accessToken);
            return response.getName();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/importUser")
    @ApiOperation(value = "钉钉用户单个导入或修改", notes = "传入用户的钉钉id和标识符")
    public R importUser(String userId, String apUnique){
        String accessToken = DingAccessTokenUtil.getToken(apUnique);
        dingUserinfoService.remove(new QueryWrapper<DingUserinfo>().eq("userid", userId));
        /*保存用户信息*/
        saveDDuser(userId, accessToken);
        return R.success("导入成功");
    }

    /**
     * 导入钉钉数据(企业内部应用)
     * 部门数据
     * @return
     */
    @GetMapping("importDdDept")
    @ApiOperation(value = "钉钉部门用户导入", notes = "传入应用标识符")
    public R importDdDept(String apUnique){
        String accessToken = DingAccessTokenUtil.getToken(apUnique);
        try {
            /*所有部门数据*/
            List<Department> departmentList = selectDdDeptList(accessToken);
            for(Department department : departmentList){
                Integer departmentId = department.getId();//钉钉部门id
                /*部门下的所有用户信息*/
                saveDdUser(accessToken, departmentId.longValue());
                // importUseListDD(accessToken, departmentId);
                /*保存钉钉部门信息*/
                saveDdDept(department);
            }
            return R.success("数据导入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.fail("数据导入失败");
    }

    /**
     * 查询钉钉部门列表信息
     * @param accessToken
     * @return
     */
    protected List<Department> selectDdDeptList(String accessToken){
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_DEPARTMENT_LIST);
            OapiDepartmentListRequest req = new OapiDepartmentListRequest();
            req.setHttpMethod("GET");
            OapiDepartmentListResponse rsp = client.execute(req, accessToken);
            JsonDepartmentErrBean jsonDepartmentErrBean = ObjectMapperUtil.toObject(rsp.getBody(), JsonDepartmentErrBean.class);
            /*所有部门数据*/
            return jsonDepartmentErrBean.getDepartment();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存钉钉部门信息
     * @param department
     */
    protected void saveDdDept(Department department){
        Integer departmentId = department.getId();//钉钉部门id
        /*查询数据库是否已存在该id的部门*/
        DingDept deptServiceOne =
                dingDeptService.getOne(new QueryWrapper<DingDept>()
                        .eq("dd_id", departmentId.toString()));
        if(deptServiceOne == null){
            String ext = department.getExt();
            Boolean autoAddUser = department.getAutoAddUser();
            Long parentid = department.getParentid();
            Boolean createDeptGroup = department.getCreateDeptGroup();
            Date date = new Date();
            DingDept dingDept = new DingDept();
            if(autoAddUser != null){
                dingDept.setAutoAddUser(autoAddUser.toString());
            }
            dingDept.setCreateAt(date);
            if(createDeptGroup != null){
                dingDept.setCreateDeptGroup(createDeptGroup.toString());
            }
            dingDept.setExt(ext);
            dingDept.setName(department.getName());
            if(parentid != null){
                dingDept.setParentid(parentid.toString());
            }
            dingDept.setUpdateAt(date);
            dingDept.setDdId(departmentId.toString());
            dingDeptService.save(dingDept);
        }
    }

    /**
     * 每个部门下的人员信息
     * @param departmentId 钉钉部门id
     */
    protected void importUseListDD(String accessToken, Integer departmentId){
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_USER_LISTBYPAGE);
            OapiUserListbypageRequest req = new OapiUserListbypageRequest();
            req.setDepartmentId(departmentId.longValue());
            req.setOffset(OFFSET);
            req.setSize(SIZE);
            req.setHttpMethod("GET");
            OapiUserListbypageResponse rsp = client.execute(req, accessToken);
            JsonDDUserErrBean jsonDDUserErrBean = ObjectMapperUtil.toObject(rsp.getBody(), JsonDDUserErrBean.class);
            /*用户列表*/
            List<DDUser> ddUserlist = jsonDDUserErrBean.getUserlist();
            for(DDUser ddUser : ddUserlist){
                System.out.println(ddUser);
            }
//            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }

    }

    /**
     * 只获取钉钉用户的用户名和userid的列表
     * @param accessToken
     * @param departmentId
     */
    protected List<DDUser> ddUseNameIdList(String accessToken, Long departmentId){
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_USER_SIMPLELIAT);
            OapiUserSimplelistRequest req = new OapiUserSimplelistRequest();
            req.setDepartmentId(departmentId);
            req.setHttpMethod("GET");
            OapiUserSimplelistResponse rsp = client.execute(req, accessToken);
            JsonDDUserErrBean jsonDDUserErrBean = ObjectMapperUtil.toObject(rsp.getBody(), JsonDDUserErrBean.class);
            List<DDUser> ddUserlist = jsonDDUserErrBean.getUserlist();
            return ddUserlist;
//            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断钉钉用户是否存在,不存在则保存
     * @param accessToken
     * @param departmentId
     */
    protected void saveDdUser(String accessToken, Long departmentId){
        List<DDUser> ddUserList = ddUseNameIdList(accessToken, departmentId);
        for(DDUser ddUser : ddUserList){
            String userId = ddUser.getUserid();
            /*查询该id的钉钉用户是否已存在*/
            List<DingUserinfo> dingUserinfoList =
                    dingUserinfoService.list(new QueryWrapper<DingUserinfo>().eq("userid", userId));
            if(dingUserinfoList.isEmpty()){//如不存在则导入
                saveDDuser(accessToken, userId);//保存钉钉用户信息
            }
        }
    }

    /**
     * 查询钉钉用户详情
     * @param accessToken
     * @param userId
     */
    public OapiUserGetResponse ddUserDetail(String accessToken, String userId){
        try {
            DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_USER_GET);
            OapiUserGetRequest request = new OapiUserGetRequest();
            request.setUserid(userId);
            request.setHttpMethod("GET");
            OapiUserGetResponse response = client.execute(request, accessToken);
            /*if(response != null){
                saveDDuser(response);
            }*/

            return response;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存钉钉用户信息
     * @param accessToken
     * @param userId
     */
    public void saveDDuser(String accessToken, String userId){
        OapiUserGetResponse response = ddUserDetail(accessToken, userId);
        Date date = new Date();
        List<Long> departmentList = response.getDepartment();
        StringBuffer department = new StringBuffer();
        int i = 0;
        for (Long departmentId : departmentList) {//拼接用户id
            if(i == 0){
                department.append("|");
            }
            department.append(departmentId);
            department.append("|");
            i++;
        }
        DingUserinfo dingUserinfo = new DingUserinfo();
        Boolean active = response.getActive();
        String avatar = response.getAvatar();//头像
        if(active != null){
            dingUserinfo.setActive(active.toString());
        }
        dingUserinfo.setAvatar(avatar);//头像
        dingUserinfo.setCreateAt(date);
        if(department != null){
            dingUserinfo.setDepartment(department.toString());
        }
        dingUserinfo.setEmail(response.getEmail());
        Date hiredDate = response.getHiredDate();
        if(hiredDate != null){
            dingUserinfo.setHiredDate(DateUtil.format(hiredDate, "yyyy-MM-dd HH:mm:ss"));
        }
        String mobile = response.getMobile();//电话
        dingUserinfo.setMobile(mobile);
        dingUserinfo.setName(response.getName());
        dingUserinfo.setOpenId(response.getOpenId());
        dingUserinfo.setPosition(response.getPosition());
        dingUserinfo.setRemark(response.getRemark());
        dingUserinfo.setStateCode(response.getStateCode());
        dingUserinfo.setUnionid(response.getUnionid());
        dingUserinfo.setUpdateAt(date);
        dingUserinfo.setUserid(userId);
        Boolean isAdmin = response.getIsAdmin();
        if(isAdmin != null){
            dingUserinfo.setIsAdmin(isAdmin.toString());
        }
        Boolean isBoss = response.getIsBoss();
        if(isBoss != null){
            dingUserinfo.setIsBoss(isBoss.toString());
        }
        Boolean isHide = response.getIsHide();
        if(isHide != null){
            dingUserinfo.setIsHide(isHide.toString());
        }
        dingUserinfo.setIsLeaderinDepts(response.getIsLeaderInDepts());
        Boolean isSenior = response.getIsSenior();
        if(isSenior != null){
            dingUserinfo.setIsSenior(isSenior.toString());
        }
        dingUserinfo.setJobnumber(response.getJobnumber());
        dingUserinfo.setOrderinDepts(response.getOrderInDepts());
        Boolean realAuthed = response.getRealAuthed();
        if(realAuthed != null){
            dingUserinfo.setRealAuthed(realAuthed.toString());
        }
//                dingUserinfo.setTags();//没有
        dingUserinfo.setTel(response.getTel());
        dingUserinfo.setWorkPlace(response.getWorkPlace());
        /*保存钉钉用户信息*/
        dingUserinfoService.save(dingUserinfo);
        /*修改staffinfo里钉钉id*/
        updateStaffinfoDDid(dingUserinfo);
    }

    /**
     * 上传用户头像到服务器
     * @param url
     */
//    public String updateUserAvatar(String url, String userId){
//        ImageUtils imageUtils = new ImageUtils();
//        InputStream imageStream = imageUtils.getImageStream(url);
//
//        String fileName = userId + ".jpg";
//        try {
//            MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "", imageStream);
//            R<FileSystem> upload = fileController.upload(multipartFile);
//            String filePath = upload.getData().getFilePath();
//            return filePath;
//        } catch (IOException e) {
//            log.error("修改头像文件类型转化时出错");
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 修改staffinfo里钉钉id
     * @param dingUserinfo
     */
    @Transactional(rollbackFor = Exception.class)
    protected void updateStaffinfoDDid(DingUserinfo dingUserinfo){
        String userid = dingUserinfo.getUserid();
        String mobile = dingUserinfo.getMobile();
        String avatar = dingUserinfo.getAvatar();
        /*删除图形库里的用户头像*/
//        deleteAvatar(mobile);
//        String userAvatar = null;
//        if (!StringUtil.isEmpty(avatar)) {
//            /*上传用户头像到服务器*/
//            userAvatar = updateUserAvatar(avatar, userid);
//        }
//        saIUserService.updateAvatarByPhone(mobile, userAvatar);
        /*根据电话匹配钉钉用户*/
        baseStaffinfoService.updateStaffinfoDDid(userid, mobile);
    }

    /**
     * 删除图形库里的用户头像
     * @param mobile
     */
    public void deleteAvatar(String mobile){
        /*查询用户是否存在头像*/
        String avatar = saIUserService.getAvatar(mobile);
        if (!StringUtil.isEmpty(avatar)) {//如果存在则删除
            int index = avatar.indexOf("/");
            try {
                fileController.deleteFile(avatar.substring(0, index), avatar.substring(index + 1));
            } catch (IOException e) {
                log.error("删除图片库时出错");
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/importDdShiftset")
    @ApiOperation(value = "导入钉钉班次信息")
    public R importDdShiftset(String apUnique){
        String accessToken = DingAccessTokenUtil.getToken(apUnique);
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getsimplegroups");
            OapiAttendanceGetsimplegroupsRequest req = new OapiAttendanceGetsimplegroupsRequest();
            OapiAttendanceGetsimplegroupsResponse rsp = client.execute(req, accessToken);
            OapiAttendanceGetsimplegroupsResponse.AtGroupListForTopVo result = rsp.getResult();
            List<OapiAttendanceGetsimplegroupsResponse.AtGroupForTopVo> groups = result.getGroups();//考勤组列表
            for(OapiAttendanceGetsimplegroupsResponse.AtGroupForTopVo group : groups){
                String groupName = group.getGroupName();//考勤组名称
                String type = group.getType();//考勤类型，FIXED为固定排班，TURN为轮班排班，NONE为无班次
                List<String> classesList = group.getClassesList();//一周的班次时间展示列表
                for(String classes : classesList){

                }
                Long defaultClassId = group.getDefaultClassId();//默认班次id
                List<Long> deptIds = group.getDeptIds();
                List<String> deptNameList = group.getDeptNameList();//关联的部门
                Long groupId = group.getGroupId();//考勤组id
                Boolean isDefault = group.getIsDefault();//是否默认考勤组
                List<String> managerList = group.getManagerList();//考勤组负责人
                Long memberCount = group.getMemberCount();//成员人数
                String ownerUserId = group.getOwnerUserId();
                List<OapiAttendanceGetsimplegroupsResponse.AtClassVo> selectedClass =
                        group.getSelectedClass();//考勤组对应的考勤班次列表
                for(OapiAttendanceGetsimplegroupsResponse.AtClassVo selected :  selectedClass){
                    Long classId = selected.getClassId();//考勤班次id
                    String className = selected.getClassName();//考勤班次名称
                    List<OapiAttendanceGetsimplegroupsResponse.AtSectionVo> sections = selected.getSections();//班次打卡时间段
                    for(OapiAttendanceGetsimplegroupsResponse.AtSectionVo section : sections){
                        List<OapiAttendanceGetsimplegroupsResponse.SetionTimeVO> times = section.getTimes();//时间段列表
                        for(OapiAttendanceGetsimplegroupsResponse.SetionTimeVO time : times){
                            Long across = time.getAcross();//打卡时间跨度
                            Date checkTime = time.getCheckTime();//打卡时间
                            String checkType = time.getCheckType();//打卡类型枚举（Onduty和OffDuty）
                        }
                    }
                    OapiAttendanceGetsimplegroupsResponse.ClassSettingVo setting = selected.getSetting();//考勤组班次配置
                    Long classSettingId = setting.getClassSettingId();//考勤组班次id
                    Long absenteeismLateMinutes = setting.getAbsenteeismLateMinutes();//旷工迟到时长，单位分钟
                    String isOffDutyFreeCheck = setting.getIsOffDutyFreeCheck();//Y表示下班不强制打卡，N表示下班强制打卡
                    Long permitLateMinutes = setting.getPermitLateMinutes();//允许迟到时长，单位分钟
                    OapiAttendanceGetsimplegroupsResponse.AtTimeVo restBeginTime = setting.getRestBeginTime();//休息开始时间，只有一个时间段的班次有
                    Date checkBeginTime = restBeginTime.getCheckTime();//开始时间
                    OapiAttendanceGetsimplegroupsResponse.AtTimeVo restEndTime = setting.getRestEndTime();//休息结束时间，只有一个时间段的班次有
                    Date checkEndTime = restEndTime.getCheckTime();//结束时间
                    Long seriousLateMinutes = setting.getSeriousLateMinutes();//严重迟到时长，单位分钟
                    Long workTimeMinutes = setting.getWorkTimeMinutes();//工作时长，单位分钟，-1表示关闭该功能
                }
                List<String> userIds = group.getUserIds();
                List<String> workDayList = group.getWorkDayList();//固定班次的工作日班次
            }
            return R.data(result);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        String apUnique = "";
        String  accessToken = DingAccessTokenUtil.getToken(apUnique);
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list?access_token="+accessToken+"");
            OapiDepartmentListRequest req = new OapiDepartmentListRequest();
            req.setHttpMethod("GET");
            OapiDepartmentListResponse rsp = client.execute(req, "");
            JsonDepartmentErrBean jsonDepartmentErrBean = ObjectMapperUtil.toObject(rsp.getBody(), JsonDepartmentErrBean.class);
            // System.out.println(jsonRootBean);
            List<Department> departmentList = jsonDepartmentErrBean.getDepartment();
            for(Department department : departmentList){
                System.out.println(department.getId());
            }
            //System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

}


