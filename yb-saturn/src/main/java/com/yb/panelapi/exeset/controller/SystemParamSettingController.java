package com.yb.panelapi.exeset.controller;

import com.yb.exeset.entity.ExesetNetwork;
import com.yb.exeset.service.IExesetNetworkService;
import com.yb.exeset.vo.ExesetNetworkVO;
import com.yb.panelapi.user.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "系统参数设置",tags = "yb_exeset_network")
@RequestMapping("/plapi")
public class SystemParamSettingController {

    @Autowired
    private IExesetNetworkService iExesetNetworkService;
    @PostMapping("/getNet")
    @ApiOperation(value = "读取已经设置是否通信",tags = "isUpdate and isChart 开关设置1 or 0 ")
    @ResponseBody
    public R getExesetNetwork(ExesetNetwork exesetNetwork){
        Map<String,Object> map = new HashMap<>();
        map.put("ma_id",exesetNetwork.getMaId());
        List<ExesetNetwork> networks =  iExesetNetworkService.getBaseMapper().selectByMap(map);
        ExesetNetwork network = null;
        if (!networks.isEmpty()) {
            network = networks.get(0);
        }
        return R.ok(network);
    }
    @PostMapping("/setNet")
    @ApiOperation(value = "设置屏幕，服务企业，盒子的联网信息",tags = "isUpdate and isChart 开关设置1 or 0 ")
    @ResponseBody
    public  R ajaxSetNetwork(@RequestBody ExesetNetwork exesetNetwork){
        if(exesetNetwork!=null){
            if (exesetNetwork.getId()!=null){
                exesetNetwork.setUpdateAt(new Date());
                iExesetNetworkService.updateById(exesetNetwork);
            }else {
                iExesetNetworkService.save(exesetNetwork);
            }
            return R.ok(exesetNetwork,"更新联网信息成功！");
        }else{
            return R.error("更新联网信息失败!");
        }
    }
    @PostMapping("/setischart")
    @ApiOperation(value = "设置是否通信",tags = "isUpdate and isChart 开1 关 0 ")
    @ResponseBody
    public R setIsChart(@RequestBody ExesetNetworkVO exesetNetworkvo){
        Map<String,Object> map = new HashMap<>();
        map.put("ma_id",exesetNetworkvo.getMaId());
        List<ExesetNetwork> networks =  iExesetNetworkService.getBaseMapper().selectByMap(map);
        ExesetNetwork network = null;
        if (!networks.isEmpty()) {
            network = networks.get(0);
            network.setIsChart(exesetNetworkvo.getIsChart());//切换状态
            iExesetNetworkService.updateById(network);
            return R.ok(network,"isChart==0 不通信 ==1通信");
        }else {
            //如果没有找到就新增
            iExesetNetworkService.save(exesetNetworkvo);
            return R.ok(exesetNetworkvo,"isChart==0 不通信 ==1通信");
        }

    }
    @PostMapping("/getischart")
    @ApiOperation(value = "获取设置是否通信",tags = "isUpdate and isChart 开1 关0 设置  ")
    @ResponseBody
    public R getIsChart(@RequestBody ExesetNetwork exesetNetwork){
        Map<String,Object> map = new HashMap<>();
        map.put("ma_id",exesetNetwork.getMaId());
        List<ExesetNetwork> networks =  iExesetNetworkService.getBaseMapper().selectByMap(map);
        ExesetNetwork network = null;
        if (!networks.isEmpty()) {
            network = networks.get(0);
        }
        return R.ok(network);
    }
}
