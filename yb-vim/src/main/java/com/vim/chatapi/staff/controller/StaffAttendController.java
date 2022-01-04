package com.vim.chatapi.staff.controller;

import com.vim.chatapi.staff.entity.StaffDayoff;
import com.vim.chatapi.staff.entity.StaffFlow;
import com.vim.chatapi.staff.entity.StaffLeave;
import com.vim.chatapi.staff.entity.StaffUscheck;
import com.vim.chatapi.staff.service.*;
import com.vim.chatapi.staff.vo.*;
import com.vim.chatapi.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("api/leave/")
@Api(value = "考勤接口", tags = "考勤")
public class StaffAttendController {

    @Autowired
    private IStaffDayoffService iStaffDayoffService;
    @Autowired
    private IStaffLeaveService iStaffLeaveService;
    @Autowired
    private IActsetFlowService iActsetFlowService;
    @Autowired
    private IStaffFlowService iStaffFlowService;
    @Autowired
    private IStaffUscheckService iStaffUscheckService;
    @Autowired
    private IWorkbatchShiftinfoService shiftinfoService;

    /***
     * 请假，补卡流程  获取相应审核人的信息 已经测试
     * @param
     *  //http://192.168.0.103:2022/api/leave/applyLeave?flag=2   已联调
     */
    @PostMapping("applyLeave")
    @ApiOperation(value = "开始申请请假",tags = "显示审核人")
    @ResponseBody
    public R applyLeave(Integer flag){// flag表示类型 1 请假 2 补卡
        //获取请假人的部门信息
        return  R.data(iActsetFlowService.startApplyLeave(flag));

    }
    /***
     * 进入考勤界面操作前先获取当前登录人的信息班次
     * @param
     *  //http://192.168.0.103:2022/api/leave/applyLeave?flag=2    已联调
     */
    @PostMapping("getStaffWorkInfo")
    @ApiOperation(value = "获取该用户的排版信息",tags = "班次等信息")
    @ResponseBody
    public R getStaffWorkInfo(Integer userId,String date) throws ParseException {
        /**
         * 查看是否今天有排版记录 如果有就正常打卡 没有就提醒是否继续操作
         */
        Date time = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df1 = new SimpleDateFormat("HH:mm:ss");
        DateFormat df3 = new SimpleDateFormat("HH:mm:ss");
        /***
         *
         */
        WorkbatchShiftinfoVO workToDay =
                shiftinfoService.getWorkbatchShiftinfoVO(userId,date);
        WorkbatchShiftinfoVO workYesDay
                = shiftinfoService.getWorkbatchShiftinfoVO(userId,
                DateUtils.operDate(date,-1)); // 看看没有有夜班没有下班的情况
        if(workToDay!=null&&workYesDay!=null){
            long time1=Math.abs(df1.parse(df3.format(time)).getTime()-
                    df1.parse(df3.format(workYesDay.getEndTime())).getTime()
            );
            long time2=Math.abs(df1.parse(df3.format(time)).getTime()-
                    df1.parse(df3.format(workToDay.getStartTime())).getTime()
            );
            if (time1>=time2) {
                workToDay.setWorkDate(df.format(workToDay.getCheckDate()));
                workToDay.setWorkStartTime(df1.format(workToDay.getStartTime()));
                workToDay.setWorkEndTime(df1.format(workToDay.getEndTime()));
                return R.data(workToDay);
            }else {
                workYesDay.setWorkDate(df.format(workYesDay.getCheckDate()));
                workYesDay.setWorkStartTime(df1.format(workYesDay.getStartTime()));
                workYesDay.setWorkEndTime(df1.format(workYesDay.getEndTime()));
               return R.data(workYesDay);
            }
        }else if(workToDay!=null&&workYesDay==null){
            workToDay.setWorkDate(df.format(workToDay.getCheckDate()));
            workToDay.setWorkStartTime(df1.format(workToDay.getStartTime()));
            workToDay.setWorkEndTime(df1.format(workToDay.getEndTime()));
            return R.data(workToDay);
        }else if(workToDay==null&&workYesDay!=null){
            workYesDay.setWorkDate(df.format(workYesDay.getCheckDate()));
            workYesDay.setWorkStartTime(df1.format(workYesDay.getStartTime()));
            workYesDay.setWorkEndTime(df1.format(workYesDay.getEndTime()));
            return R.data(workYesDay);
        }else {
            /**
             *
             * 没有排版记录 加班
             * 打卡情况
             */


        }
        return R.success("没有排版信息");
    }

    /***
     * 请假、补卡流程 提交申请人的信息  已经测试   已联调
     * @param
     */
    @PostMapping("commitApplyLeave")
    @ApiOperation(value = "开始提交请假",tags = "查询申请记录")
    @ResponseBody
    public R commitApplyLeave(@RequestBody CheckModelVO modelVO){
        Date time = new Date();
        if(modelVO.getStaffDayoff()!=null){  //提交的是请假申请

            modelVO.getStaffDayoff().setStatus(1);//（提交）待审核
            modelVO.getStaffDayoff().setCreateAt(time);//创建时间
            iStaffDayoffService.saveStaffDayoff(modelVO.getStaffDayoff());
            StaffFlow staffFlow = new StaffFlow();
            staffFlow.setAfId(1);// 1 为请假
            staffFlow.setDbId(modelVO.getStaffDayoff().getId());
            staffFlow.setStatus(1);//待审核
            staffFlow.setResult(modelVO.getStaffDayoff().getReasons());//原因
            staffFlow.setCreateAt(time);//创建时间
            staffFlow.setUsId(modelVO.getActsetFlow().getUsId());//设置审批人ID
            iStaffFlowService.saveStaffFlow(staffFlow);


        }else if(modelVO.getStaffLeave()!=null){ //提交的是补卡
            modelVO.getStaffLeave().setCreateAt(time);
            //modelVO.getStaffLeave().setRectifyTime(time);
            modelVO.getStaffLeave().setStatus(1);//提交待审核
            iStaffLeaveService.saveStaffLeave(modelVO.getStaffLeave());
            StaffFlow staffFlow = new StaffFlow();
            staffFlow.setAfId(2);// 2 为补卡
            staffFlow.setDbId(modelVO.getStaffLeave().getId());
            staffFlow.setStatus(1);//待审核
            staffFlow.setResult(modelVO.getStaffLeave().getReasons());//原因
            staffFlow.setCreateAt(time);//创建时间
            staffFlow.setUsId(modelVO.getActsetFlow().getUsId());//设置审批人ID
            iStaffFlowService.saveStaffFlow(staffFlow);

        }
            return  R.data("申请信息已提交！");
    }

    /***
     * 当前登录人员查看自己要审核的记录（没有审核的记录和已经审核的记录）
     * @return  需要当前登录人的userId
     * // url http://192.168.0.103:2022/api/leave/getApplyLeaves?userId=13  已联调
     * status 为状态 1表示待审核的  2 表示已经审核的（同意和不同意的）
     */
    @PostMapping("getApplyLeaves")
    @ApiOperation(value = "获取提交请假，补卡/已经审批的，未审批的",tags = "查询申请记录")
    @ResponseBody
    public R getApplyLeaves(Integer userId,Integer status){
        /***
         * 查出当前登录用户的有关审核记录
         */
        Map<String,Object> result = new HashMap<>();

        List<StaffDayoffVO> listDayoffs =
                iStaffDayoffService.checkApplyDayoff(userId,status);
        List<StaffLeaveVO> listLeaves =
                iStaffLeaveService.checkApplyLeave(userId,status);

        result.put("listDayoffs",listDayoffs);
        result.put("listLeaves",listLeaves);
        return  R.data(result);
    }



    /**
     * 获取当前登录人员发起的申请（待审批的和已审批的）
     * @param userId
     * @param status
     * @return
     * status 为状态 1表示待审核的  2 表示已经审核的（同意和不同意的）
     */
    @PostMapping("getApplyOneself")
    @ApiOperation(value = "获取自己的请假，补卡/已经审批的，未审批的",tags = "查询申请记录")
    @ResponseBody
    public R getApplyOneself(Integer userId,Integer status){
        /***
         * 查出当前登录用户的有关申请记录
         */
        Map<String,Object> result = new HashMap<>();

        List<StaffDayoff> listDayoffs =
                iStaffDayoffService.getStaffDayoff(userId,status);
        List<StaffLeave> listLeaves =
                iStaffLeaveService.getStaffLeaves(userId,status);

        result.put("listDayoffs",listDayoffs);
        result.put("listLeaves",listLeaves);
        return  R.data(result,"当前需要待审批的请求");
    }

    /**
     * 获取具体的某一个申请记录   没测试
     * @return
     * 补卡详情和请假通用
     * flag = 0 请假 flag =1 补卡
     */
    @PostMapping("getApplyById")
    @ApiOperation(value = "获取具体的某一个申请记录",tags = "查询申请记录")
    @ResponseBody
    public R getApplyById(Integer flag,Integer id){ // 需要一个审核人信息
        Map<String,Object> result = new HashMap<>();
        if(flag == 0){
            result.put("staffDayoff",iStaffDayoffService.getStaffDayoffById(id));

        }else{

            result.put("staffLeave",iStaffLeaveService.getStaffLeaveById(id));
        }
        result.put("flag",flag);
        return R.data(result);
    }


    /***
     * 审核人员审核请求的记录（请假、补卡记录）
     * @return
     */
    @PostMapping("checkApplyLeaves")
    @ApiOperation(value = "审核提交请假，补卡记录",tags = "修改记录状态")
    @ResponseBody
    public R checkApplyLeaves(@RequestBody CheckModelVO modelVO){
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd");
        StaffFlow staff = new StaffFlow();

        if (modelVO.getStaffLeave()!=null) { //表示是提交的是补卡请求
         Integer checkId =
                 iActsetFlowService.startApplyLeave(2).getUsId();//获取对应审核人的Id
            //修改申请人的申请状态，时间等
            iStaffLeaveService.updateStaffLeave(modelVO.getStaffLeave());
            //设置被审批的主键ID
            staff.setUsId(checkId);//设置审核人Id
            staff.setDbId(modelVO.getStaffLeave().getId());
            staff.setAfId(2);//补卡
            staff.setResult(modelVO.getStaffLeave().getReasons());

            if(modelVO.getStaffLeave().getStatus()==2){ // 同意申请
               // staff.setStatus(2);
                //填补缺卡当天内容  找出补卡人的需要补卡的那一条记录
                String date = dateFormat.format(
                        modelVO.getStaffLeave().getRectifyTime());
                Integer usId = modelVO.getStaffLeave().getUsId();
               List<StaffUscheck> staffUschecks =
                       iStaffUscheckService.getStaffUschecks(usId,date,null);
                StaffUscheckVO staffUscheckVO = new StaffUscheckVO();
                //有一个问题 就是如果有多个补卡记录时，就会数据紊乱
                if (staffUschecks.size()>=1) {  //查找出需要补卡的补卡记录
                    /**差一个插入时间到考勤表*/
                  Date time1 =
                          modelVO.getStaffLeave().getRectifyTime();//获取补卡的时间
                    if (time1.getHours()>=12){ // 补下班卡
                        staffUschecks.get(0).setOutTime(
                                modelVO.getStaffLeave().getRectifyTime());
                        staffUschecks.get(0).setCkStatus(1);//考勤为正常
                        iStaffUscheckService.updateSUscheck(staffUschecks.get(0));
                    }else{ // 补上班卡
                        staffUschecks.get(0).setInTime(
                                modelVO.getStaffLeave().getRectifyTime());
                        staffUschecks.get(0).setCkStatus(1);//考勤为正常
                        iStaffUscheckService.updateSUscheck(staffUschecks.get(0));
                    }

                }else {// 全天卡
                    /**************创建整天的打卡记录*******************/
                    StaffUscheck staffUscheck = new StaffUscheck();
                    // 补卡的时期
                    staffUscheck.setCkDate(dateFormat.format(
                            modelVO.getStaffLeave().getRectifyTime()
                    ));
                    //补卡人的id
                    staffUscheck.setUsId(modelVO.getStaffLeave().getUsId());
                    //补卡创建时间
                    staffUscheck.setCreateAt(new Date());
                    iStaffUscheckService.saveStaffUscheck(staffUscheck);
                }
            }else {
                staff.setResult(modelVO.getStaffLeave().getReasons());//写入拒绝理由
            }

        }

        if (modelVO.getStaffDayoff()!=null) { //表示是提交的是请假请求
            Integer checkId =
                    iActsetFlowService.startApplyLeave(1).getUsId();//获取对应审核人的Id
            //修改申请人的申请状态，时间等
            iStaffDayoffService.updateStaffDayoff(modelVO.getStaffDayoff());
            //设置被审核记录的主键ID
            staff.setDbId(modelVO.getStaffDayoff().getId());
            staff.setUsId(checkId);//设置审核人的Id
            staff.setAfId(1);//请假
            staff.setResult(modelVO.getStaffDayoff().getReasons());//请假原因

            if (modelVO.getStaffDayoff().getStatus()==2) { //表示同意请假
                //插入一天考勤就标注状态为请假 ck_status=4
                StaffUscheck staffUscheck = new StaffUscheck();
                staffUscheck.setCreateAt(new Date());
                staffUscheck.setUsId(modelVO.getStaffDayoff().getUsId());
                staffUscheck.setCkDate(dateFormat.format(new Date()));
                staffUscheck.setCkStatus(4);//设置为请假 ck_status=4
                iStaffUscheckService.saveStaffUscheck(staffUscheck);
            }else {
                staff.setResult(modelVO.getStaffDayoff().getReasons());//拒绝理由
            }
        }
        //设置审核时间
        staff.setCheckTime(new Date());
        //设置审核状态为已审核 --> 3
        staff.setStatus(3);
        iStaffFlowService.updateStaffFlow(staff);

        return  R.data("审核成功");
    }

    /***
     * 考勤绩效  查看绩效   查看绩效
     * author qinbo
     * date  2020/3/29
     * model 1 为加班打卡
     *  model 0 为正常打卡
     *
     */
    @PostMapping("getUsChecks")
    @ApiOperation(value = "考勤绩效  查看绩效",tags = "模糊查询 like ck_date")
    @ResponseBody
    public R getUsChecks(Integer usId, String date,Integer model){ // 只需要传进来一个用户ID
        //当月的考勤
       List<StaffUscheck> list =
               iStaffUscheckService.getStaffUschecks(usId,date,model);

        return  R.data(list,"");
    }

    /**
     *  只需要传进来一个用户ID,  开始和结束时
     *  用户查询考勤记录
     * @param date
     * @return
     */
    @PostMapping("getUsCheckByDate")
    @ApiOperation(value = "考勤绩效  查看绩效",tags = "模糊查询 like ck_date")
    @ResponseBody
    public R getUsCheckByDate(@RequestBody DateModelVO date){
        List<StaffUscheck> list =
                iStaffUscheckService.getStaffUscheckByDate(date);
        return  R.data(list,"");
    }

    /***
     * 考勤绩效  打卡接口
     * author qinbo
     * date  2020/3/29
     *
     */
    @PostMapping("addStartUsCheck")
    @ApiOperation(value = " 考勤绩效  上班打卡接口",tags = "")
    @ResponseBody
    public R addUsCheck(@RequestBody StaffUscheckVO staffUscheck ) throws ParseException {
        /**
         * 打卡情况1 迟到 ，早退 ，缺卡 ，缺勤
         */
        Date time = new Date();
        DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
        /**先获取打卡人规定的班次信息*/
        /**
         * 用前端的DATE 处理
         */
        WorkbatchShiftinfoVO workSet =
                shiftinfoService.getWorkbatchShiftinfoVO(staffUscheck.getUsId(),
                        staffUscheck.getCkDate());
        if (staffUscheck.getFlag()==1) { // 上班打卡
            if (time.getHours() > workSet.getStartTime().getHours()) {//迟到情况1
                    staffUscheck.setCkStatus(2);//迟到打卡
                    workSet.setCkStatus(2);//2为迟到
                staffUscheck.setModel(0);//非加班
            }else if(time.getHours() == workSet.getStartTime().getHours()
                    &&time.getMinutes()>workSet.getStartTime().getMinutes()){
                staffUscheck.setCkStatus(2);//迟到打卡
                staffUscheck.setModel(0);//非加班
                workSet.setCkStatus(2);//2为迟到
            } else {
                staffUscheck.setCkStatus(1);//正常打卡
                workSet.setCkStatus(1);//1为正常
                staffUscheck.setModel(0);//非加班
            }
            staffUscheck.setCreateAt(time);
            staffUscheck.setInTime(time);
            iStaffUscheckService.updateById(staffUscheck);
            //排班是初始化好的
            shiftinfoService.updateById(workSet);
        }else if(staffUscheck.getFlag()==0){ //下班打卡
            /**
             * 如果没有打上班卡直接下班卡的情况
             * 不存在 进入考勤就会初始化 打卡记录和 排版记录
             *
             */
            if (time.getHours()<workSet.getEndTime().getHours()) {
                staffUscheck.setCkStatus(3);//早退打卡
                workSet.setCkStatus(3);
                staffUscheck.setModel(0);//非加班
            }else{
                staffUscheck.setCkStatus(1);//正常打卡
                workSet.setCkStatus(1);
                staffUscheck.setModel(0);//非加班
            }
            staffUscheck.setOutTime(time);
          //  staffUscheck.setCkDate(df.format(time));//下班时间日期格式
            iStaffUscheckService.updateById(staffUscheck);
            shiftinfoService.updateById(workSet);
        }else if(staffUscheck.getFlag()==2){ //加班打卡（比如刚下班打卡完成又打卡继续加班）
            StaffUscheck uscheck = iStaffUscheckService.getStaffUscheckInfo(staffUscheck.getUsId(),
                    df.format(time),staffUscheck.getModel());
            if(uscheck!=null) { //已经有一条加班记录打卡了  做修改
                uscheck.setInTime(time);//打卡时间
                uscheck.setCkDate(df.format(time));//打卡日期
                uscheck.setInAdd(staffUscheck.getInAdd()); //打卡地点
                uscheck.setInLnglat(staffUscheck.getInLnglat());//经纬度
                iStaffUscheckService.updateById(staffUscheck);
            }else{
                staffUscheck.setInTime(time);
                staffUscheck.setCkDate(df.format(time));
                staffUscheck.setCreateAt(time);
                staffUscheck.setModel(1);//加班打卡
                iStaffUscheckService.save(staffUscheck);
            }
        }else if (staffUscheck.getFlag()==3){ //加班打卡下班
            /**
             * 加班打卡时 跨越时间的情况  打加班下班卡时 就应该找到当前时间上一天的记录作修改
             */
            //如果跨天打下班卡  就找不到加班打卡记录
            StaffUscheck toDayUscheck = iStaffUscheckService.getStaffUscheckInfo(staffUscheck.getUsId(),
                    df.format(time),staffUscheck.getModel());//在一天完成的加班 直接修改下班时间
             String yesDay = DateUtils.operDate(df.format(time),-1);//当前日期的前一天
            //如果跨天打下班卡  就找上一天的
            StaffUscheck yesDayUscheck = iStaffUscheckService.getStaffUscheckInfo(staffUscheck.getUsId(),
                    yesDay,staffUscheck.getModel());//
            if(toDayUscheck!=null) { //
                toDayUscheck.setOutTime(time);//打卡时间
                toDayUscheck.setCkDate(df.format(time));//打卡日期
                toDayUscheck.setOutAdd(staffUscheck.getOutAdd());//打卡地点
                toDayUscheck.setOutLnglat(staffUscheck.getOutLnglat());//经纬度
                iStaffUscheckService.updateById(toDayUscheck);

                return R.data(200,"打卡成功！");
            }
            if(yesDay!=null) { //
                yesDayUscheck.setOutTime(time);//打卡时间
                yesDayUscheck.setOutAdd(staffUscheck.getOutAdd());//打卡地点
                yesDayUscheck.setOutLnglat(staffUscheck.getOutLnglat());//经纬度
                iStaffUscheckService.updateById(yesDayUscheck);
            }

        }
        return  R.data(200,"打卡成功！");
    }

    /**
     *查看自己当前的打卡状况信息
     * @param userId
     * @param date
     * @return
     */
    @PostMapping("getStaffUscheck")
    @ApiOperation(value = " 考勤绩效 ",tags = "查询当前考勤状态")
    @ResponseBody
    public R getStaffUscheck(Integer userId ,String date,Integer model) throws ParseException {
        Map<String,Object> result = new HashMap<>();
        /**
         * 查看是否今天有排版记录 如果有就正常打卡 没有就提醒是否继续操作
         */
        Date time = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        WorkbatchShiftinfoVO workToDay =
                shiftinfoService.getWorkbatchShiftinfoVO(userId,date);
        /*根据排班信息找到是否有打卡记录*/
        StaffUscheck staff = iStaffUscheckService.getStaffUscheckInfo(userId,df.format(
                workToDay.getCheckDate()
        ),model);
        //进入APP初始化打卡记录
        if (workToDay!=null) { //如果排版不为空 就初始化一条打卡
            if (staff==null) {
                staff = new StaffUscheck();
                staff.setUsId(userId);
                staff.setCreateAt(time);
                staff.setCkDate(df.format(
                        workToDay.getCheckDate()));
                staff.setClassNum(workToDay.getCkName()==1?"白班":
                        workToDay.getCkName()==2?"夜班":"晚班");//
                staff.setCkStatus(5);//缺卡
                staff.setModel(0);
                /*返回无ID*/
                iStaffUscheckService.save(staff);
                staff = iStaffUscheckService.getStaffUscheckInfo(userId,df.format(time),model);

                result.put("staff",staff);

            }else {
                result.put("staff",staff);
         }
        }else{
           return R.success("今天没有你的排班信息！");
        }
        return  R.data(result);
    }

}
