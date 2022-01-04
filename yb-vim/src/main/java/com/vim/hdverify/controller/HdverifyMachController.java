package com.vim.hdverify.controller;
import com.vim.common.utils.DateUtil;
import com.vim.hdverify.entity.HdverifyMach;
import com.vim.hdverify.entity.SuperviseBoxinfo;
import com.vim.hdverify.service.IHdverifyMachService;
import com.vim.hdverify.service.ISuperviseBoxinfoService;
import com.vim.hdverify.vo.HdverifyMachVO;
import com.vim.hdverify.wrapper.HdverifyMachWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    //第一步  先获取列表
    /**
     * 获取效验计数的所有设备
     * @param machVO
     * @return
     * 通过前端去找到对应的状态的记录 1-->未开始验证 2-->正在验证 3-->结束  模糊查询等
     */
    @RequestMapping("/getHdverifyMachList")
    public R getHdverifyMachList( HdverifyMachVO machVO) {
        //machVO.setStatus(CHECK_STATUS_WILL); //查出准备验证的
        List<HdverifyMachVO> machVOList =
                hdverifyMachService.getHdverifyMachList(machVO);
        return R.data(machVOList);
    }

    // 第二步  点击验证 输入开始机台，盒子，时间的开始值
    @RequestMapping("/startCheck")
    public R startCheck( HdverifyMachVO machVO) {
        //获取此条计数的详情
        HdverifyMach mach =
                hdverifyMachService.getById(machVO.getId());
        //盒子计数的开始值
        SuperviseBoxinfo boxinfo = boxinfoService.getById(mach.getBxId());
        Integer bxStartNum =0;
        if (boxinfo!=null) {
            bxStartNum = boxinfo.getNumber(); //盒子的开始数量
        }
        //机台计数的开始值（手动输入）
        Integer maStartNum = machVO.getMaStartnum()==null?0:machVO.getMaStartnum();
        mach.setBxStartnum(bxStartNum);
        mach.setMaStartnum(maStartNum);
        mach.setStartTime(new Date());
        mach.setBxUuid(boxinfo.getUuid());
        //修改为正在验证
        mach.setStatus(CHECK_STATUS_ING);
        hdverifyMachService.updateById(mach); //更新保存的开始计数
        return R.data(mach);
    }
    /**
     * 获取当前盒子记录的数据
     * @param machVO
     * @return
     */
    @RequestMapping("/getStartOrEndBoxSum")
    @ApiOperation(value = "分页")
    public R getStartOrEndBoxSum( HdverifyMachVO machVO) {
        //获取此条计数的详情
        HdverifyMach mach =
                hdverifyMachService.getById(machVO.getId());
        SuperviseBoxinfo boxinfo = boxinfoService.getById(machVO.getBxId());
        machVO =  HdverifyMachWrapper.build().entityVO(mach);
        machVO.setNumber(boxinfo.getNumber()); //盒子当前的计数
        return R.data(machVO);
    }

    //第三步 验证结束 算出误差率
    /**
     * 正在验证中的--》获取效验计数的单个设备的详情 ，并折算出误差率
     * @param machVO
     * @return
     */
    @RequestMapping("/endCheck")
    public R endCheck( HdverifyMachVO machVO) {
        //获取此条计数的详情
        HdverifyMach mach =
                hdverifyMachService.getById(machVO.getId());
        //盒子计数的结束值
        SuperviseBoxinfo boxinfo = boxinfoService.getById(mach.getBxId());
        Integer bxEndNum =0;
        if (boxinfo!=null) {
            bxEndNum = boxinfo.getNumber(); //盒子计数的结束值
        }
        //机台计数的结束值（手动输入）
        Integer maEndNum = machVO.getMaStartnum()==null?0:machVO.getMaStartnum();
        // (机台差值-盒子差值) 绝对值 / 机台差值
        //机台计数的差值
        Integer maDiff = Math.abs(maEndNum - mach.getMaStartnum());
        //盒子计算的差值
        Integer boxDiff = Math.abs(bxEndNum - mach.getBxStartnum());
        //计算出误差率
        Double diffRate = (double)(Math.abs(maDiff-boxDiff)/maDiff);
        //记录在
        mach.setDiffRate(diffRate);
        mach.setStatus(CHECK_STATUS_ED);//设置为结束
        mach.setEndTime(new Date());//结束时间
        mach.setMaEndnum(maEndNum); //结束机台计数
        mach.setBxEndnum(bxEndNum);//结束盒子计数
        //记录时间差值（单位为秒）
        int stayTime =(int) DateUtil.durationTime(mach.getStartTime()); //计算出时间差
        mach.setStayTime(Integer.valueOf(stayTime));
        hdverifyMachService.updateById(mach);
        return R.data(mach);
    }

    /**
     * 获取详情 单个设备的详情
     * @param machVO
     * @return
     */
    @RequestMapping("/detail")
    @ApiOperation(value = "分页")
    public R detail( HdverifyMachVO machVO) {
        //获取此条计数的详情
        HdverifyMach mach =
                hdverifyMachService.getById(machVO.getId());
        return R.data(mach);
    }

}
