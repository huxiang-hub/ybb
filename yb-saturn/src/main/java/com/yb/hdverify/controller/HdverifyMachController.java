package com.yb.hdverify.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.common.DateUtil;
import com.yb.hdverify.entity.HdverifyMach;
import com.yb.hdverify.service.IHdverifyMachService;
import com.yb.hdverify.vo.HdverifyMachVO;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.service.ISuperviseBoxinfoService;
import com.yb.supervise.vo.SuperviseBoxinfoVO;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/hdverify")
public class HdverifyMachController extends BladeController {
    final Integer CHECK_STATUS_WILL = 1;//未开始
    final Integer CHECK_STATUS_ING = 2;//正在验证
    final Integer CHECK_STATUS_ED = 3;//结束
    private IHdverifyMachService hdverifyMachService;
    private ISuperviseBoxinfoService boxinfoService;
    @Autowired
    private IMachineMainfoService machineMainfoService;
    //第一步  先获取列表

    /**
     * 获取效验计数的所有设备
     *
     * @param machVO
     * @return 通过前端去找到对应的状态的记录 1-->未开始验证 2-->正在验证 3-->结束  模糊查询等
     */
    @RequestMapping("/getHdverifyMachList")
    public R getHdverifyMachList(HdverifyMachVO machVO) {


        List<HdverifyMachVO> machVOList =
                hdverifyMachService.getHdverifyMachList(machVO);
        return R.data(machVOList);
    }
    /**
     * 获取待效验计数的所有设备
     *
     * @return 通过前端去找到对应的状态的记录 1-->未开始验证 2-->正在验证 3-->结束  模糊查询等
     */
    @RequestMapping("/getMachList")
    public R getMachList() {
        List<SuperviseBoxinfoVO> machVOList = hdverifyMachService.getMachList();
        return R.data(machVOList);
    }

    /**
     * 第二步  获取当前盒子记录的数据
     *
     * @param maId
     * @return
     */
    @RequestMapping("/getStartOrEndBoxSum")
    @ApiOperation(value = "分页")
    public R getStartOrEndBoxSum(Integer maId) {

        SuperviseBoxinfo boxinfo
                = boxinfoService.getOne(new QueryWrapper<SuperviseBoxinfo>().eq("ma_id", maId));
        if (boxinfo.getNumber() == null) {
            boxinfo.setNumber(0); //盒子当前的计数
        }
        return R.data(boxinfo);
    }

    // 第三步  点击验证 输入开始机台，盒子，时间的开始值
    @RequestMapping("/startCheck")
    public R startCheck(Integer maId, Integer maStartNum, String operator) {
        HdverifyMach hdverifyMach = new HdverifyMach();
        MachineMainfo machineMainfo = machineMainfoService.getById(maId);
        //盒子计数的开始值
        SuperviseBoxinfo boxinfo =
                boxinfoService.getOne(new QueryWrapper<SuperviseBoxinfo>().eq("ma_id", maId));
        Integer bxStartNum = boxinfo.getNumber() == null ? 0 : boxinfo.getNumber(); //盒子的开始数量
        //机台计数的开始值（手动输入）
        hdverifyMach.setMaId(maId);//设备id
        hdverifyMach.setMaName(machineMainfo.getName());//设备名称
        hdverifyMach.setBxId(boxinfo.getId());//盒子流水号
        hdverifyMach.setBxUuid(boxinfo.getUuid());//盒子的uuid
        hdverifyMach.setBxStartnum(bxStartNum);//盒子开始计数
        hdverifyMach.setOperator(operator);//操作人
        hdverifyMach.setMaStartnum(maStartNum == null ? 0 : maStartNum);//设备开始计数
        hdverifyMach.setStartTime(new Date());
        hdverifyMach.setBxUuid(boxinfo.getUuid());
        //修改为正在验证
        hdverifyMach.setStatus(CHECK_STATUS_ING);
        hdverifyMachService.save(hdverifyMach); //更新保存的开始计数
        return R.data(hdverifyMach);
    }


    //第四步 验证结束 算出误差率

    /**
     * 正在验证中的--》获取效验计数的单个设备的详情 ，并折算出误差率
     * @param hdverifyMach
     * @return
     */

    @RequestMapping("/endCheck")
    public R endCheck(@RequestBody HdverifyMach hdverifyMach) {
        //获取此条计数的详情
        HdverifyMach mach =
                hdverifyMachService.getOne(new QueryWrapper<HdverifyMach>().eq("ma_id", hdverifyMach.getMaId()).eq("status", 2));
        //盒子计数的结束值
        SuperviseBoxinfo boxinfo = boxinfoService.getById(mach.getBxId());
        Integer bxEndNum = 0;
        if (boxinfo.getNumber() != null) {
            bxEndNum = boxinfo.getNumber(); //盒子计数的结束值
        }
        //机台计数的结束值（手动输入）
        Integer maEndnum = hdverifyMach.getMaEndnum();
        if(maEndnum == null){
            maEndnum = 0;
        }
        // (机台差值-盒子差值) 绝对值 / 机台差值
        //机台计数的差值
        Integer maDiff = Math.abs(maEndnum - mach.getMaStartnum());
        //盒子计算的差值
        Integer boxDiff = Math.abs(bxEndNum - mach.getBxStartnum());
        //计算出误差率
        if(maDiff == 0){
            return R.fail("机台开始计数与机台结束计数不能相同");
        }
        Double diffRate =  ((Math.abs(maDiff - boxDiff) / (double)maDiff)) * 1000;
        //记录在
        mach.setDiffRate(diffRate);
        mach.setStatus(CHECK_STATUS_ED);//设置为结束
        mach.setMaEndnum(maEndnum); //结束机台计数
        mach.setBxEndnum(bxEndNum);//结束盒子计数
        //盒子计算的差值
        mach.setBxDiff(boxDiff);
        //机台计数的差值
        mach.setMaDiff(maDiff);
        //记录时间差值（单位为秒）
        mach.setEndTime(new Date());//结束时间
        int stayTime = (int) DateUtil.durationTime(mach.getStartTime()); //计算出时间差
        mach.setStayTime(Integer.valueOf(stayTime));
        hdverifyMachService.updateById(mach);
        return R.data(mach);
    }

    /**
     * 获取详情 单个设备的详情
     *
     * @param machVO
     * @return
     */
    @RequestMapping("/detail")
    @ApiOperation(value = "分页")
    public R detail(HdverifyMachVO machVO) {
        System.out.println(machVO.getId());
        //获取此条计数的详情
        HdverifyMach mach =
                hdverifyMachService.getById(machVO.getId());
        return R.data(mach);
    }

}
