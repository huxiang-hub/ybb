package com.yb.dingding.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiProcessGetByNameRequest;
import com.dingtalk.api.request.OapiProcessinstanceCreateRequest;
import com.dingtalk.api.request.OapiProcessinstanceExecuteV2Request;
import com.dingtalk.api.request.OapiProcessinstanceGetRequest;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.yb.common.DateUtil;
import com.yb.dingding.entity.*;
import com.yb.dingding.service.DingProcessService;
import com.yb.dingding.util.DingAccessTokenUtil;
import com.yb.dingding.util.DingUserUtil;
import com.yb.execute.entity.ExecuteSpverify;
import com.yb.execute.service.IExecuteSpverifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.common.tool.ObjectMapperUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.stream.Collectors;

import static com.yb.dingding.config.URLConstant.*;

@RestController
@Api(tags = "钉钉审核流程")
public class DingProcessController {

    @Autowired
    private DingProcessService dingProcessService;
    @Autowired
    private IExecuteSpverifyService executeSpverifyService;

    @GetMapping("/getDingUserId")
    @ApiOperation(value = "根据免登code获取钉钉当前用户id")
    public R getDingUserId(@RequestParam("authCode") String authCode){
        String dingUserId = DingUserUtil.getDingUserId(authCode);
        return R.data(dingUserId);
    }



    @PostMapping("/processinstanceCreate")
    @ApiOperation(value = "钉钉创建审核流程")
    public DingProcessinstanceCreate processinstanceCreate(@RequestBody ProcessinstanceCreateParam processinstanceCreateParam) {
        DingProcessinstanceCreate dingProcessinstanceCreate = dingProcessService.processinstanceCreate(processinstanceCreateParam);
        return dingProcessinstanceCreate;
    }

    @GetMapping("/processinstanceGet")
    @ApiOperation(value = "获取单个审批实例详情 ")
    public ProcessinstanceGet processinstanceGet(@ApiParam("审批实例id")@RequestParam("processInstanceId") String processInstanceId){
        return getOneProcessinstance(processInstanceId);
    }

    private ProcessinstanceGet getOneProcessinstance(String processInstanceId) {
        try {
            String token = DingAccessTokenUtil.getToken("InternalH5");
            DingTalkClient client = new DefaultDingTalkClient(PROCESSINSTANCE_GET);
            OapiProcessinstanceGetRequest req = new OapiProcessinstanceGetRequest();
            req.setProcessInstanceId(processInstanceId);
            OapiProcessinstanceGetResponse rsp = client.execute(req, token);
            ProcessinstanceGet processinstanceGet = ObjectMapperUtil.toObject(rsp.getBody(), ProcessinstanceGet.class);
            if(processinstanceGet.getErrcode() != 0){
                return null;
            }
            ProcessInstance processInstance = processinstanceGet.getProcessInstance();
            String status = processInstance.getStatus();//审批状态：NEW：新创建, RUNNING：审批中, TERMINATED：被终止, COMPLETED：完成, CANCELED：取消
            ExecuteSpverify executeSpverify =
                    executeSpverifyService.getOne(new QueryWrapper<ExecuteSpverify>().eq("process_instance_id", processInstanceId));
            if(executeSpverify != null){
                switch (status){
                    case "TERMINATED":{
                        executeSpverify.setExStatus(2);
                        break;
                    }
                    case "COMPLETED":{
                        executeSpverify.setExStatus(3);
                        break;
                    }
                    case "CANCELED":{
                        executeSpverify.setExStatus(4);
                        break;
                    }
                }
                executeSpverifyService.updateById(executeSpverify);
            }
            return processinstanceGet;
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/processInstanceExecute")
    @ApiOperation(value = "执行审批操作带附件")
    public R<InstanceExecuteResult> processInstanceExecute(@RequestBody DingInstanceExecute dingInstanceExecute){
        try {
            String apUnique = "InternalH5";
            String token = DingAccessTokenUtil.getToken(apUnique);
            String processInstanceId = dingInstanceExecute.getProcessInstanceId();
            Long taskId = dingInstanceExecute.getTaskId();
            String actionerUserid = dingInstanceExecute.getActionerUserid();
            String result = dingInstanceExecute.getResult();
            String remark = dingInstanceExecute.getRemark();
            DingFile dingFile = dingInstanceExecute.getFile();
            DingTalkClient client = new DefaultDingTalkClient(PROCESS_INSTANCE_EXECUTE);
            OapiProcessinstanceExecuteV2Request req = new OapiProcessinstanceExecuteV2Request();
            OapiProcessinstanceExecuteV2Request.ExecuteTaskRequest executeTaskRequest = new OapiProcessinstanceExecuteV2Request.ExecuteTaskRequest();
            if(dingFile != null){
                OapiProcessinstanceExecuteV2Request.File file = new OapiProcessinstanceExecuteV2Request.File();
                List<Attachments> attachmentsList = dingFile.getAttachments();
                if(attachmentsList != null && !attachmentsList.isEmpty()){
                    List<OapiProcessinstanceExecuteV2Request.Attachment> attachmentList = new ArrayList<>();
                    OapiProcessinstanceExecuteV2Request.Attachment attachment = new OapiProcessinstanceExecuteV2Request.Attachment();
                    for(Attachments attachments : attachmentsList){
                        attachment.setFileId(attachments.getFileId());
                        attachment.setFileName(attachments.getFileName());
                        attachment.setFileSize(attachments.getFileSize());
                        attachment.setFileType(attachments.getFileType());
                        attachment.setSpaceId(attachments.getSpaceId());
                        attachmentList.add(attachment);
                    }
                    file.setAttachments(attachmentList);
                    file.setPhotos(dingFile.getPhotos());
                }
                executeTaskRequest.setFile(file);
            }
            executeTaskRequest.setProcessInstanceId(processInstanceId);
            executeTaskRequest.setActionerUserid(actionerUserid);
            executeTaskRequest.setTaskId(taskId);
            executeTaskRequest.setRemark(remark);
            executeTaskRequest.setResult(result);

            req.setRequest(executeTaskRequest);
            OapiProcessinstanceExecuteV2Response rsp = client.execute(req, token);
            InstanceExecuteResult instanceExecuteResult = ObjectMapperUtil.toObject(rsp.getBody(), InstanceExecuteResult.class);
            if(instanceExecuteResult.getErrcode() != 0){
                return R.fail("当前审批已被处理,请刷新后重试");
            }
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            RequestContextHolder.setRequestAttributes(servletRequestAttributes,true);//设置子线程共享
            new Thread(() -> this.processinstanceGet(processInstanceId)).start();
            return R.data(instanceExecuteResult);
        } catch (ApiException e) {
            e.printStackTrace();
            return R.fail("出现异常");
        }
    }

    @GetMapping("/reviewProcess")
    @ApiOperation(value = "获取审核流程")
    public R<ReviewProcess> reviewProcess(@ApiParam("审批实例id")@RequestParam("processInstanceId") String processInstanceId){
        return R.data(getOneReviewProcess(processInstanceId));
    }

    @GetMapping("/reviewProcessList")
    @ApiOperation(value = "获取多个审核流程")
    public R<List<ReviewProcess>> reviewProcessList(@ApiParam("审批实例ids")@RequestParam("processInstanceIds") String processInstanceIds){
        List<String> ids = Func.toStrList(processInstanceIds);
        if (ids.size() == 0) {
            return null;
        }
        List<ReviewProcess> result = new ArrayList<>();
        for (String id : ids) {
            ReviewProcess oneReviewProcess = getOneReviewProcess(id);
            Integer spMold = executeSpverifyService.getSpMoldByProinstanceId(id);
            oneReviewProcess.setSpMold(spMold);
            result.add(oneReviewProcess);
        }
        return R.data(result);
    }

    private ReviewProcess getOneReviewProcess(String processInstanceId) {
        try {
            String token = DingAccessTokenUtil.getToken("InternalH5");
            DingTalkClient client = new DefaultDingTalkClient(PROCESSINSTANCE_GET);
            OapiProcessinstanceGetRequest req = new OapiProcessinstanceGetRequest();
            req.setProcessInstanceId(processInstanceId);
            OapiProcessinstanceGetResponse rsp = client.execute(req, token);
            ProcessinstanceGet processinstanceGet = ObjectMapperUtil.toObject(rsp.getBody(), ProcessinstanceGet.class);
            if(processinstanceGet.getErrcode() != 0){
                throw new ServiceException("出现异常");
            }
            ProcessInstance processInstance = processinstanceGet.getProcessInstance();//审批实例详情
            String status = processInstance.getStatus();//审批状态：NEW：新创建, RUNNING：审批中, TERMINATED：被终止, COMPLETED：完成, CANCELED：取消
            String originatorUserid = processInstance.getOriginatorUserid();//发起人的userid
            OapiUserGetResponse originatorUserDetail = DingUserUtil.getDingUserDetail(token, originatorUserid);//发起人用户详情
            List<Tasks> tasks = processInstance.getTasks();//任务列表
            ReviewProcess reviewProcess = new ReviewProcess();
            reviewProcess.setOriginatorUserName(originatorUserDetail.getName());
            reviewProcess.setStatus(status);
            Iterator<Tasks> iterator = tasks.iterator();
            List<OperationRecords> operationRecords = processInstance.getOperationRecords();
            while (iterator.hasNext()){
                Tasks task = iterator.next();
                String taskResult = task.getTaskResult();//结果：AGREE：同意, REFUSE：拒绝, REDIRECTED：转交
                String taskStatus = task.getTaskStatus();//任务状态：NEW:未启动, RUNNING：处理中, PAUSED：暂停, CANCELED：取消, COMPLETED：完成, TERMINATED：终止
//                if("CANCELED".equals(taskStatus) && "NONE".equals(taskResult)){
                if(!"COMPLETED".equals(taskStatus) && !"TERMINATED".equals(taskStatus)){
                    iterator.remove();
                    continue;
                }
                task.setStatus(0);
                if("COMPLETED".equals(taskStatus)){
                    switch (taskResult){
                        case "AGREE":{
                            task.setStatus(1);
                            break;
                        }
                        case "REFUSE":{
                            task.setStatus(2);
                            break;
                        }
                    }
                }
                String userid = task.getUserid();//任务处理人
                OapiUserGetResponse dingUserDetail = DingUserUtil.getDingUserDetail(token, userid);//审核人用户详情
                task.setReviewUserName(dingUserDetail.getName());
                String finishTime = task.getFinishTime();
                for(OperationRecords operationRecord : operationRecords){
                    String date = operationRecord.getDate();
                    Date changeDay = DateUtil.toDate(date, "yyyy-MM-dd HH:mm:ss");
                    long l = changeDay.getTime() + 1000;
                    String format = DateUtil.format(new Date(l), "yyyy-MM-dd HH:mm:ss");
                    String remark = operationRecord.getRemark();
                    if(task.getUserid().equals(operationRecord.getUserid()) && (date.equals(finishTime) || format.equals(finishTime))){
                        task.setRemark(remark);
                    }
                }
            }
            tasks.sort(Comparator.comparing(Tasks::getCreateTime));//按创建时间升序排序
            reviewProcess.setTasksList(tasks);
            return reviewProcess;
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/getInstanceExecuteParam")
    @ApiOperation(value = "根据审核流程id和钉钉用户id获取审核的节点id")
    public R getInstanceExecuteParam(@ApiParam("审批实例id")@RequestParam("processInstanceId") String processInstanceId,
                                     @ApiParam("审核人钉钉id")@RequestParam("userid") String userid){

        try {
            ProcessinstanceGet processinstanceGet = this.getOneProcessinstance(processInstanceId);
            ProcessInstance processInstance = processinstanceGet.getProcessInstance();
            List<Tasks> tasks = processInstance.getTasks();
            DingInstanceExecute dingInstanceExecute = null;
            for(Tasks task : tasks){
                String taskStatus = task.getTaskStatus();//任务状态：NEW:未启动, RUNNING：处理中, PAUSED：暂停, CANCELED：取消, COMPLETED：完成, TERMINATED：终止
                if("RUNNING".equals(taskStatus)){
                    if(userid.equals(task.getUserid())){
                        dingInstanceExecute = new DingInstanceExecute();
                        String taskid = task.getTaskid();
                        dingInstanceExecute.setTaskId(Long.valueOf(taskid));
                        dingInstanceExecute.setActionerUserid(userid);
                        dingInstanceExecute.setProcessInstanceId(processInstanceId);
                        return R.data(dingInstanceExecute);
                    }
                }
            }
            if(dingInstanceExecute == null){
                return R.fail("该用户没有审核权限");
            }
            return R.data(dingInstanceExecute);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("出现异常");
        }
    }

}
