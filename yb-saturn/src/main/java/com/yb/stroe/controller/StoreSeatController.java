package com.yb.stroe.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.service.ExecuteTraycardService;
import com.yb.stroe.entity.StoreSeat;
import com.yb.stroe.service.StoreSeatService;
import com.yb.stroe.vo.StoreSeatVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/StoreSeat")
@Api(tags = "库位管理")
public class StoreSeatController {

    @Autowired
    private StoreSeatService storeSeatService;
    @Autowired
    private ExecuteTraycardService executeTraycardService;

    /**
     * 新增仓库的占位
     *
     * @param storeSeat
     * @return
     */
    @PostMapping("save")
    @ApiOperation(value = "新增", notes = "storeSeat")
    public R save(StoreSeat storeSeat) {
        Integer userId = SaSecureUtil.getUserId();
        storeSeat.setUsId(userId);
        Date date = new Date();
        storeSeat.setCreateAt(date);
        storeSeat.setUpdateAt(date);
        int insert = storeSeatService.getBaseMapper().insert(storeSeat);
        if(insert > 0){
            return R.data(storeSeat);
        }
        return R.fail("保存失败");
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除", notes = "传入ids")
    public R delete(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(storeSeatService.removeByIds(Func.toIntList(ids)));
    }

    @PostMapping("update")
    @ApiOperation(value = "修改", notes = "传入storeSeat")
    public R update(StoreSeat storeSeat) {
        storeSeat.setUpdateAt(new Date());
        return R.status(storeSeatService.updateById(storeSeat));
    }

    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public R list(StoreSeatVO storeSeatVO) {
        QueryWrapper<StoreSeat> queryWrapper = new QueryWrapper<>();
        if (storeSeatVO != null) {
            Integer srId = storeSeatVO.getSrId();
            /*类型（数据字典1、半成品2、成品 3、原料4、辅料5、备品备件）*/
            Integer stType = storeSeatVO.getStType();
            if (srId != null && srId != 0) {
                queryWrapper.eq("sr_id", srId);
            }
            if (stType != null) {
                queryWrapper.eq("st_type", stType);
            }
        }
        List<StoreSeat> storeSeatList = storeSeatService.list(queryWrapper);
        return R.data(storeSeatList);
    }


    @GetMapping("/getStoreSeatByExId")
    @ApiOperation(value = "根据执行单查询本工序库位详情列表")
    public R<List<StoreSeatVO>> getStoreSeatByExId(@ApiParam("执行单id")@RequestParam Integer exId){
//        List<ExecuteTraycard> executeTraycardList =
//                executeTraycardService.list(new QueryWrapper<ExecuteTraycard>().eq("ex_id", exId));
//        if(executeTraycardList.isEmpty()){
//            return R.fail("执行单id没查到托盘信息");
//        }
//        List<Integer> idList = new ArrayList<>();
//        executeTraycardList.forEach(e -> {
//            Integer mpId = e.getMpId();
//            idList.add(mpId);
//        });
        List<StoreSeatVO> storeSeatList = storeSeatService.getStoreSeatByExId(exId);
        return R.data(storeSeatList);
    }

    @GetMapping("/upStoreSeatList")
    @ApiOperation(value = "根据工单编号查询上工序库位信息")
    public R<List<StoreSeatVO>> upStoreSeatList(@ApiParam("排产班次id")@RequestParam("wfId") Integer wfId){
        List<StoreSeatVO> storeSeatList = storeSeatService.upStoreSeatList(wfId);
        return R.data(storeSeatList);
    }
}
