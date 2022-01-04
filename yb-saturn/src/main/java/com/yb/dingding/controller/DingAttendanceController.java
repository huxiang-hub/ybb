package com.yb.dingding.controller;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceRecordUploadRequest;
import com.dingtalk.api.response.OapiAttendanceRecordUploadResponse;
import com.taobao.api.ApiException;
import com.yb.dingding.entity.AttendanceParam;
import com.yb.dingding.util.DingAccessTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import static com.yb.dingding.config.URLConstant.ATTENDANCE_RECORD_UPLOAD;

@RestController
@RequestMapping("/DingAttendance")
@Api(tags = "钉钉考勤打卡导入")
public class DingAttendanceController {


    @PostMapping("/importAttendance")
    @ApiOperation(value = "导入钉钉考勤记录")
    public R importAttendance(@RequestBody AttendanceParam attendanceParam) {
        //获取accessToken,注意正是代码要有异常流处理
        String accessToken = DingAccessTokenUtil.getToken("InternalH5");
        try {
            DingTalkClient client = new DefaultDingTalkClient(ATTENDANCE_RECORD_UPLOAD);
            OapiAttendanceRecordUploadRequest req = new OapiAttendanceRecordUploadRequest();
            req.setUserid(attendanceParam.getUserid());
            req.setDeviceName(attendanceParam.getDeviceName());
            req.setDeviceId(attendanceParam.getDeviceId());
            req.setPhotoUrl(attendanceParam.getPhotoUrl());
            req.setUserCheckTime(attendanceParam.getUserCheckTime());
            OapiAttendanceRecordUploadResponse rsp = client.execute(req, accessToken);
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
            return R.fail("操作失败");
        }
        return R.success("操作成功");
    }

}
