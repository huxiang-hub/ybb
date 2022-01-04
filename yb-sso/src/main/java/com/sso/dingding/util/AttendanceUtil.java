package com.sso.dingding.util;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceRecordUploadRequest;
import com.dingtalk.api.response.OapiAttendanceRecordUploadResponse;
import com.sso.dingding.entity.DingAttendance;
import com.taobao.api.ApiException;

import static com.sso.dingding.config.URLConstant.ATTENDANCE_RECORD_UPLOAD;

public class AttendanceUtil {
    /**
     * 导入钉钉考勤记录数据
     * @param dingAttendance 参数对象
     */
    public static void importAttendance(DingAttendance dingAttendance) {
        //获取accessToken,注意正是代码要有异常流处理
        String accessToken = AccessTokenUtil.getToken("InternalH5");
        try {
            DingTalkClient client = new DefaultDingTalkClient(ATTENDANCE_RECORD_UPLOAD);
            OapiAttendanceRecordUploadRequest req = new OapiAttendanceRecordUploadRequest();
            req.setUserid(dingAttendance.getUserid());
            req.setDeviceName(dingAttendance.getDeviceName());
            req.setDeviceId(dingAttendance.getDeviceId());
            req.setPhotoUrl(dingAttendance.getPhotoUrl());
            req.setUserCheckTime(dingAttendance.getUserCheckTime());
            OapiAttendanceRecordUploadResponse rsp = client.execute(req, accessToken);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
