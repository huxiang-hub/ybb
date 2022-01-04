package com.yb.machine.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.yb.machine.entity.MachineStafflink;
import com.yb.machine.mapper.MachineStafflinkMapper;
import com.yb.machine.request.MachineAuthorizationRequest;
import com.yb.machine.request.MachineAuthorizationSelectUserRequest;

import com.yb.machine.response.EquipmentUserPageVo;
import com.yb.machine.response.getEquipmentVo;
import com.yb.machine.service.MachineStafflinkService;

import com.yb.machine.vo.UserFaceVO;
import com.yb.system.user.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;

import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/MachineStafflink")
@Api(tags = "人员和机台绑定_yb_machine_stafflink")
public class MachineStafflinkController {
    @Autowired
    private MachineStafflinkService machineStafflinkService;

    @Autowired
    private MachineStafflinkMapper machineStafflinkMapper;

    @PostMapping("/page")
    @ApiOperation(value = "分页")
    public R<IPage<UserPageVo>> page(@RequestBody MachineAuthorizationSelectUserRequest request) {
        IPage<UserPageVo> page = machineStafflinkService.page(Condition.getPage(request), request);
        return R.data(page);
    }

    @PostMapping("/getEquimentUserList")
    @ApiOperation(value = "获取设备授权用户")
    public R<IPage<EquipmentUserPageVo>> getEquimentUserList(@RequestBody getEquipmentVo getequipmentVo){
        IPage<EquipmentUserPageVo> equimentUserListVOS=null;
        if(getequipmentVo.getMaIdList()!=null) {
            equimentUserListVOS = machineStafflinkService.getEquimentUserList(Condition.getPage(getequipmentVo), getequipmentVo);
        }
        return R.data(equimentUserListVOS);
    }


    @PostMapping("/accData")
    @ApiOperation(value = "授权")
    public R roleAuthor(@RequestBody @Validated MachineAuthorizationRequest request) {
        List<RoleAuthor> rlist=new ArrayList<>();
        for(int i=0;i<request.getMaIds().size();i++){
            for(int j=0;j<request.getUsIds().size();j++){
                if(machineStafflinkMapper.selectUsId(request.getUsIds().get(j),request.getMaIds().get(i))>0){
                    return R.fail("存在已授权人员"+request.getUsIds().get(j));
                }
                RoleAuthor roleAuthor=new RoleAuthor();
                roleAuthor.setMa_id(request.getMaIds().get(i));
                roleAuthor.setUs_id(request.getUsIds().get(j));
                roleAuthor.setJobs(request.getJobs().get(j));
                rlist.add(roleAuthor);
            }
        }
        if(rlist.size()>0){
            machineStafflinkService.equipmentAuthor(rlist);
            return R.success("角色授权成功");
        }
        return R.fail("授权失败");
    }


    @PostMapping("/delete")
    @ApiOperation(value = "删除")
    public R delete(@RequestBody getEquipmentVo getequipmentVo) {
        if(getequipmentVo.getUsIdList().size()>0 && getequipmentVo.getUsIdList()!=null && getequipmentVo.getMaNameList()!=null && getequipmentVo.getMaNameList().size()!=0){
            machineStafflinkService.deleteEquipmentUser(getequipmentVo);
        }
        return R.success("删除成功");
    }

    /**
     * 获取班组信息
     * */
    @PostMapping("/getTeamList")
    @ApiOperation(value = "获取班组信息")
    public R<List<TeamVo>> getTeamList(){
        List<TeamVo> tList=machineStafflinkService.teamInformation();
        return R.data(tList);
    }

    /******************************勿删，机台接口代码***************************************************/


    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入machineStafflink")
    public R save(MachineStafflink machineStafflink){
        Integer maId = machineStafflink.getMaId();
        if(maId == null){
            return R.fail("设备id不能为空");
        }
        Integer usId = machineStafflink.getUsId();
        if(usId == null){
            return R.fail("用户id不能为空");
        }
        MachineStafflink stafflink = machineStafflinkService.getOne(
                new QueryWrapper<MachineStafflink>().eq("us_id", usId).eq("ma_id", maId));
        if(stafflink != null){
            return R.fail("本设备与该用户已绑定,不能重复绑定");
        }
        machineStafflink.setCreateAt(new Date());
        return R.status(machineStafflinkService.save(machineStafflink));
    }
    @GetMapping("/detail")
    @ApiOperation(value = "查询详情信息")
    public R<MachineStafflink> detail(MachineStafflink machineStafflink){
        return R.data(machineStafflinkService.getOne(Condition.getQueryWrapper(machineStafflink)));
    }
    @GetMapping("/page")
    @ApiOperation(value = "分页查询所有信息", notes = "传入machineStafflink,")
    public R<IPage<MachineStafflink>> getIpage(MachineStafflink machineStafflink, Query query){
        IPage<MachineStafflink> pages = machineStafflinkService.page(Condition.getPage(query), Condition.getQueryWrapper(machineStafflink));
        return R.data(pages);
    }
    @PostMapping("/update")
    @ApiOperation(value = "修改", notes = "传入machineStafflink")
    public R update(MachineStafflink machineStafflink){
        Integer maId = machineStafflink.getMaId();
        if(maId == null){
            return R.fail("设备id不能为空");
        }
        Integer usId = machineStafflink.getUsId();
        if(usId == null){
            return R.fail("用户id不能为空");
        }
        MachineStafflink stafflink = machineStafflinkService.getOne(
                new QueryWrapper<MachineStafflink>().eq("us_id", usId).eq("ma_id", maId));
        if(stafflink != null){
            return R.fail("本设备与该用户已绑定,不能重复绑定");
        }
        return R.status(machineStafflinkService.updateById(machineStafflink));
    }
    @PostMapping("/submit")
    @ApiOperation(value = "新增或修改", notes = "传入machineStafflink")
    public R submit(MachineStafflink machineStafflink){
        Integer maId = machineStafflink.getMaId();
        if(maId == null){
            return R.fail("设备id不能为空");
        }
        Integer usId = machineStafflink.getUsId();
        if(usId == null){
            return R.fail("用户id不能为空");
        }
        MachineStafflink stafflink = machineStafflinkService.getOne(
                new QueryWrapper<MachineStafflink>().eq("us_id", usId).eq("ma_id", maId));
        if(stafflink != null){
            return R.fail("本设备与该用户已绑定,不能重复绑定");
        }
        return R.status(machineStafflinkService.saveOrUpdate(machineStafflink));
    }
    @PostMapping("/remove")
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@RequestParam("ids") String ids){
        return R.status(machineStafflinkService.removeByIds(Func.toIntList(ids)));
    }
    @GetMapping("/getUsersByMaId")
    @ApiOperation(value = "根据设备id查询人员信息", notes = "传入maId")
    public R<List<UserFaceVO>> getUsersByMaId(@RequestParam("maId") Integer maId){
        return R.data(machineStafflinkService.getUsersByMaId(maId));
    }

}
