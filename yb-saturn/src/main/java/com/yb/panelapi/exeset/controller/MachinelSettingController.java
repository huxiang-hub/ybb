package com.yb.panelapi.exeset.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.machine.entity.*;
import com.yb.machine.service.*;
import com.yb.machine.vo.MachineClassifyVO;
import com.yb.panelapi.exeset.entity.ModelBean;
import com.yb.panelapi.exeset.service.IBlindBoxInfoService;
import com.yb.panelapi.user.utils.R;
import com.yb.process.entity.ProcessMachlink;
import com.yb.process.service.IProcessMachlinkService;
import com.yb.rule.service.RuleExecuteService;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.service.ISuperviseBoxinfoService;
import com.yb.supervise.service.ISuperviseExecuteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "设备设置",tags = "设备设置")
@RequestMapping("/plapi")
public class MachinelSettingController {

    @Autowired
    private IMachineScreenService iMachineScreenService;
    @Autowired
    private IMachineMixboxService iMachineMixboxService;
    @Autowired
    private IMachineBsinfoService iMachineBsinfoService;
    @Autowired
    private IMachineClassifyService iMachineClassifyService;
    @Autowired
    private IMachineMainfoService iMachineMainfoService;
    @Autowired
    private IBlindBoxInfoService iBlindBoxInfoService;
    @Autowired
    private ISuperviseBoxinfoService iSuperviseBoxinfoService;
    @Autowired
    private IProcessMachlinkService iProcessMachlinkService;
    @Autowired
    private ISuperviseExecuteService superviseExecuteService;
    @Autowired
    private RuleExecuteService ruleExecuteService;

    /*yb_machine_classify, yb_supervise_boxinfo,yb_machine_mainfo;
     *  yb_machine_bsinfo**/

    /**
     * 获取当前设备所有的绑定信息
     * @param maId
     * @return
     */
    @PostMapping("/getblindboxinfo")
    @ApiOperation(value = "获取当前设备所有的绑定信息")
    @ResponseBody
    public R getAllBlindInfo(Integer maId){
        Map<String,Object> data = new HashMap<>();
        data.put("ma_id", maId);
        MachineScreen ScreenInfo = null;
        MachineMixbox boxInfo = null;
        try {
            //屏幕的绑定信息
            ScreenInfo = iMachineScreenService.getOne(new QueryWrapper<MachineScreen>().eq("ma_id", maId));
            //盒子的绑定信息
            boxInfo  = iMachineMixboxService.getOne(new QueryWrapper<MachineMixbox>().eq("ma_id", maId));

        }catch (Exception e){
            e.printStackTrace();
            System.err.println("一台设备绑定了多个盒子或屏幕");
        }
        data.put("ScreenInfo",ScreenInfo);
        data.put("boxInfos",boxInfo);
        data.put("machineBsinfo", iMachineBsinfoService.getMachineBsinfo(maId));
        data.put("MachineClassify", iMachineClassifyService.getMachineInfo(maId));
        data.put("MachineMainInfo", iMachineMainfoService.getMachineMainfo(maId));
        data.put("companyInfo", iMachineMainfoService.getCompanyInfoByMaId(maId));
        data.put("ruleExecute", ruleExecuteService.list(maId));
        return R.ok(data);
    }

    @PostMapping("/setblindbox")
    @ApiOperation(value = "盒子解绑")
    @ResponseBody
    public R AjaxSetBlindboxInfo(@RequestBody ModelBean model){
        // note:盒子表中 maid 为null或者-1都为 空盒子 未绑定的盒子
        if(model.getUuid()==null){
            return R.error();
        }
        //把盒子maid 清空
        iMachineMixboxService.setMixboxByMaId(model.getUuid());
        //把盒子maid 清空
        SuperviseBoxinfo info = iSuperviseBoxinfoService.getBoxInfoByBno(model.getUuid());
        SuperviseExecute execute = superviseExecuteService.getBeanByUUID(model.getUuid());
        if (!Func.isEmpty(info)) {
            info.setMaId(-1);//SuperviseBoxinfo  与设备解绑
            iSuperviseBoxinfoService.updateById(info);
        }
        if (!Func.isEmpty(execute)) {
            execute.setMaId(-1);
            superviseExecuteService.updateById(execute);
        }
        return R.ok("获取盒子解绑信息成功 设置active= 0 maId=NULL");
    }

    @PostMapping("/getallbox")
    @ApiOperation(value = "获取没有绑定的盒子",tags = " ")
    @ResponseBody
    public R getAllBoxinfo(){
        List allbox = iMachineMixboxService.getBlindBox();
        if(allbox==null) return R.error("没有可以绑定的盒子") ;
        return R.ok(allbox,"查找盒子成功");

    }

    @PostMapping("/addblindbox")
    @ApiOperation(value = "盒子绑定",tags = "active 绑定设置1 or 0 ")
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public R AjaxAddBlindboxInfo(@RequestBody ModelBean model){
        if(model.getUuid()==null||model.getMaId()==null){
            return R.error();
        }
        List<MachineMixbox> machineMixboxList =
                iMachineMixboxService.list(new QueryWrapper<MachineMixbox>().eq("ma_id", model.getMaId()));
        if(!machineMixboxList.isEmpty()){
            return R.error("该设备已绑定盒子,请解绑后再试");
        }
        //修改对应盒子的maId
        int flag =  iMachineMixboxService.addMixboxByMaId(model.getUuid(),
                model.getMaId());
        //记录SuperviseBoxinfo表中的uuid
        SuperviseBoxinfo info = iSuperviseBoxinfoService.getBoxInfoByBno(model.getUuid());
        if (!Func.isEmpty(info)) {
//            存在，修改
            info.setMaId(model.getMaId());
            iSuperviseBoxinfoService.updateById(info);
        }else {
//            不存在，增加
            info = new SuperviseBoxinfo();
            info.setUuid(model.getUuid());
            info.setMaId(model.getMaId());
            iSuperviseBoxinfoService.save(info);
        }
        SuperviseExecute execute = superviseExecuteService.getBeanByUUID(model.getUuid());
        if (!Func.isEmpty(execute)) {
            //            存在，修改
            execute.setMaId(model.getMaId());
            superviseExecuteService.updateById(execute);
        }else{
            //            不存在，增加
            execute = new SuperviseExecute();
            execute.setUuid(model.getUuid());
            execute.setMaId(model.getMaId());
            superviseExecuteService.save(execute);
        }

        return R.ok(flag," 盒子绑信息成功");
    }

    /**
     * 获取已经绑定的
     * @param
     * @return
     */
    @PostMapping("/getallscreen")
    @ApiOperation(value = "获取绑定屏幕信息")
    @ResponseBody
    public R getAllScreen(){
        List<MachineScreen> screen = iMachineScreenService.getScreenInfo();//不给参数表示所有都查出来
        return R.ok(screen);

    }


    /**
     * 解除绑定 屏幕的maId设置为null
     * @param model
     * @return
     */
    @PostMapping("/setblindscreen")
    @ApiOperation(value = "解绑屏幕",tags = " ")
    @ResponseBody
    public R AjaxSetBlindScreen(@RequestBody ModelBean model){
        iMachineScreenService.setBlindScreen(model.getUuid());
        return R.ok("解绑成功！");
    }

    /**
     * 绑定屏幕
     * @param model
     * @return
     */
    @PostMapping("/addblindscreen")
    @ApiOperation(value = "绑定屏幕",tags = "需要设备maid 和 uuid ")
    @ResponseBody
    public R AjaxAddBlindScreen(@RequestBody  ModelBean model){
        List<MachineScreen> machineScreenList =
                iMachineScreenService.list(new QueryWrapper<MachineScreen>().eq("ma_id", model.getMaId()));
        if(!machineScreenList.isEmpty()){
            return R.error("该设备已绑定屏幕,请解绑后再试");
        }
        iMachineScreenService.addBlindScreen(model.getMaId(),
                model.getUuid());
        return R.ok("绑定成功");
    }

    /**
     *
     */
    @PostMapping("/addblindMaId")
    @ResponseBody
    public R getBlindMaId(){
        return R.ok(iMachineMainfoService.list());
    }

    /**
     *  mno = #{info.mno},
     *  name = #{info.name},
     *  dp_id = #{info.dpId},
     *  pro_type = #{info.proype}
     * @param info
     * @return
     */
    @PostMapping("/updatemaininfo")
    @ApiOperation(value = "修改设备型号",tags = "yb_machine_mainfo ")
    @ResponseBody
    public R AjaxUpdateMainfo(@RequestBody MachineMainfo info){
        if(info ==null)return R.error("没有接收到信息");
        /**
         * 主要工序修改后，要增加工序关联设备表
         */
        //先查找此工序中间表 有就修改没得就新增
        Map<String,Object> map = new HashMap<>();
        //获取之前的主要工序ID
        MachineMainfo oldMachlink =
                iMachineMainfoService.getById(info.getId());
        if (oldMachlink!=null) {
            Integer oldPrId = oldMachlink.getProId();
            map.clear();//清除数据
            map.put("ma_id",oldMachlink.getId()); //设备id
            map.put("pr_id",oldPrId);//设备的主要助攻ID
        }else{
            return null;
        }
        List<ProcessMachlink> machlinks =
                iProcessMachlinkService.getBaseMapper().selectByMap(map);
        if (!machlinks.isEmpty()) { //存在就直接修改 主要工序
            ProcessMachlink machlink = machlinks.get(0);
            machlink.setPrId(info.getProId());//修改的主要工序ID
            iProcessMachlinkService.updateById(machlink);
        }else { //没有就增加一个主要工序关联
            ProcessMachlink machlink = new ProcessMachlink();
            machlink.setPrId(info.getProId());
            machlink.setMaId(info.getId());
            // TODO 转数从哪儿来
            iProcessMachlinkService.save(machlink);
        }
        iMachineMainfoService.updateById(info);
        return R.ok(info,"修改成功");
    }

    /**
     *    serialno = #{info.serialno},
     *         out_date = #{info.outDate},
     *         weight = #{info.weight},
     *         size = #{info.size},
     *         power = #{info.power},
     *         voitage = #{info.voitage},
     *         phone = #{info.phone},
     *         contact = #{info.contact}
     * @param info
     * @return
     */
    @PostMapping("/updatebainfo")
    @ApiOperation(value = "修改设备扩展信息",tags = " yb_machine_bainfo")
    @ResponseBody
    public R AjaxUpdateBainfo(@RequestBody MachineBsinfo info){
        iMachineBsinfoService.saveOrUpdate(info);
        return R.ok(info);
    }

    /***
     *  brand = #{brand},
     *  model = #{model},
     *  specs = #{specs},
     *  image = #{image}
     * @param info
     * @return
     */
    @PostMapping("/updateinfo")
    @ApiOperation(value = "修改设备",tags = "yb_machine_classify ")
    @ResponseBody
    public R AjaxUpdateinfo(@RequestBody MachineClassifyVO info){
        if(info.getMaId()!=null){
            MachineMainfo mainfo =
                    iMachineMainfoService.getById(info.getMaId());
            mainfo.setMtId(info.getId());
            iMachineMainfoService.updateById(mainfo);
        }
        if(info ==null)return R.error("没有接收到信息");
        iMachineClassifyService.updateById(info);
        return R.ok(info,"修改成功");
    }

    @GetMapping("/getMachins")
    @ApiOperation(value = "修改设备",tags = "yb_machine_classify ")
    @ResponseBody
    public R getMachins(String name){

        return R.ok(iMachineMainfoService.getMachins(name),"");
    }


    @PostMapping("/addBoxPc")
    @ApiOperation(value = "修改设备绑定盒子")
    @ResponseBody

    public R addBoxPc(@RequestBody ModelBean model){
        /**
         * 先通过maid找到绑定的盒子，然后将其清空，再将传进来的uuid绑定maid
         */
        Map<String,Object> map = new HashMap<>();
        map.put("ma_id",model.getMaId());
        List<MachineMixbox>  mixboxes = iMachineMixboxService.getBaseMapper().selectByMap(map);
        if (!mixboxes.isEmpty()) {
            MachineMixbox machineMixbox = mixboxes.get(0);
            machineMixbox.setMaId(-1);//没有绑定
            iMachineMixboxService.updateById(machineMixbox);
        }
        List<SuperviseBoxinfo> boxinfos = iSuperviseBoxinfoService.getBaseMapper().selectByMap(map);
        if (!boxinfos.isEmpty()) {
            SuperviseBoxinfo boxinfo = boxinfos.get(0);
            boxinfo.setMaId(-1);//没有绑定
            iSuperviseBoxinfoService.updateById(boxinfo);
        }
        //再将传进来的uuid绑定maid
        AjaxAddBlindboxInfo(model);//绑定盒子

        return R.ok("更改成功！");
    }

    @PostMapping("/addScreenPc")
    @ApiOperation(value = "修改设备绑定屏幕",tags = "yb_machine_classify ")
    @ResponseBody
    public R addScreenPc(@RequestBody ModelBean model){
        Map<String,Object> map = new HashMap<>();
        map.put("ma_id",model.getMaId());
        List<MachineScreen> list = iMachineScreenService.getBaseMapper().selectByMap(map);
        if (!list.isEmpty()) {
            MachineScreen machineScreen = list.get(0);
            machineScreen.setMaId(-1); //没有绑定
            //把之前绑定的maID清除
            iMachineScreenService.updateById(machineScreen);
        }
        AjaxAddBlindScreen(model);//绑定屏幕

        return R.ok("更改成功！");
    }

    /***
     *
     * @param model
     * @return
     */
    @PostMapping("/getScreenInfoByMaId")
    @ApiOperation(value = "绑定屏幕",tags = "需要设备maid 和 uuid ")
    @ResponseBody
    public R getScreenInfoByMaId(@RequestBody  ModelBean model){
        List<MachineScreen> machineScreenList =
                iMachineScreenService.list(new QueryWrapper<MachineScreen>().eq("ma_id", model.getMaId()));
        if(!machineScreenList.isEmpty()){
            return R.error("该设备已绑定屏幕,请解绑后再试");
        }
        iMachineScreenService.addBlindScreen(model.getMaId(),
                model.getUuid());
        return R.ok("绑定成功");
    }
    /***
     *修改设备屏幕信息
     * @param screen
     * @return
     */
    @PostMapping("/setScreenInfoByObject")
    @ApiOperation(value = "修改设备屏幕信息",tags = "需要设备maid 和 uuid ")
    @ResponseBody
    public R setScreenInfoByObject(@RequestBody MachineScreen screen){
        iMachineScreenService.updateById(screen);
        return R.ok(screen);
    }


    /***
     *根据品牌查询品牌型号
     * @return
     */
    @RequestMapping("/getBrandModel")
    @ResponseBody
    public R getBrandModel(@RequestBody MachineClassify classify){
        MachineClassify machineClassify = iMachineClassifyService.getById(classify.getId());
        if (machineClassify == null) {
            return R.ok("No everything!");
        }
        //通过品牌名称查找出品牌下的型号
        Map<String,Object> map = new HashMap<>();
        map.put("brand",machineClassify.getBrand());
        List<MachineClassify> classifies =
                iMachineClassifyService.getBaseMapper().selectByMap(map);
        return R.ok(classifies);
    }

    @GetMapping("getNotScreenInfo")
    @ApiOperation(value = "获取没有绑定的屏幕信息")
    public R getNotScreenInfo(){
        List<MachineScreen> machineScreenList =
                iMachineScreenService.list(new QueryWrapper<MachineScreen>().isNull("ma_id").or().eq("ma_id", -1));
        return R.ok(machineScreenList);
    }

}
