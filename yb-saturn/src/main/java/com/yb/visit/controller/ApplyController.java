package com.yb.visit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.dingding.entity.DDUser;
import com.yb.dingding.util.DingSendUtil;
import com.yb.dingding.util.getDingUserUtil;
import com.yb.visit.entity.Apply;
import com.yb.visit.service.ApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/Apply")
@Api(tags = "访问申请管理_yb_visit_apply")
public class ApplyController {

    @Autowired
    private ApplyService applyService;

    @PostMapping("/save")
    @ApiOperation(value = "申请访问信息", notes = "传入apply")
    public R save(@RequestBody Apply apply){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        apply.setStatus(0);//待审核
        apply.setCreateAt(new Date());
        boolean save = applyService.save(apply);
        /*消息发送 张攀:0544191535780768*/
        DingSendUtil.dingSend("你有新的访问申请,请及时查看:" +
                "\nhttp://nxhr.hopewellgroup.com.cn:7300/#/pages/member_detail/list?status=2" +
                "\n流水号:" + uuid, "0544191535780768");
        return R.status(save);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改")
    public R update(@RequestBody Apply apply) {
        apply.setExTime(new Date());
        return R.status(applyService.updateById(apply));
    }
    @GetMapping("/audit")
    @ApiOperation(value = "审核")
    public R audit(@ApiParam("审核意见") @RequestParam("examine") String examine,
                   @ApiParam("状态0待审核1通过2不通过")@RequestParam("status")Integer status,
                   @ApiParam("记录id")@RequestParam("id")Integer id) {
        Apply apply = new Apply();
        apply.setId(id);
        apply.setStatus(status);
        apply.setExamine(examine);
        apply.setMgDdid("0544191535780768");
        apply.setManager("张攀");
        apply.setExTime(new Date());
        return R.status(applyService.updateById(apply));
    }

    @GetMapping("/reportTemperature")
    @ApiOperation(value = "上报体温")
    public R reportTemperature(@ApiParam("体温信息") @RequestParam("temperature") Double temperature,
                               @ApiParam("钉钉authCode码") @RequestParam(value = "authCode", required = false) String authCode,
                               @ApiParam("应用标识符") @RequestParam(value = "apUnique", required = false) String apUnique,
                               @ApiParam("记录id") @RequestParam("id") Integer id) {
        /*查询当前访问用户的钉钉信息*/
        DDUser ddUser = null;
        if(!StringUtil.isEmpty(authCode) && !StringUtil.isEmpty(apUnique)){
            ddUser = getDingUserUtil.getDingUserUtil(authCode, apUnique);
        }
        Apply apply = new Apply();
        if (ddUser != null) {
            apply.setGuard(ddUser.getName());
            apply.setDdId(ddUser.getUserid());
        }
        apply.setId(id);
        apply.setStatus(1);
        apply.setTemperature(temperature);
        apply.setTpTime(new Date());
        return R.status(applyService.updateById(apply));
    }

    @GetMapping("/getApplyList")
    @ApiOperation(value = "根据手机号查询列表")
    public R<Apply> getApplyList(@ApiParam("电话")@RequestParam("vaPhone") String vaPhone){
        List<Apply> list = applyService.list(new QueryWrapper<Apply>().eq("va_phone", vaPhone)
                .orderByDesc("create_at"));
        Apply apply = null;
        if(!list.isEmpty()){
            apply = list.get(0);
        }
        return R.data(apply);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "查询详情")
    public R detail(@ApiParam("记录id")@RequestParam("id") Integer id){
        return R.data(applyService.getById(id));
    }

    @GetMapping("/getUnreviewedList")
    @ApiOperation(value = "查询未审核记录")
    public R<List<Apply>> getUnreviewedList() {
        return R.data(applyService.list(new QueryWrapper<Apply>().eq("status", 0)
                .orderByDesc("create_at")));
    }
    @GetMapping("/getReviewedList")
    @ApiOperation(value = "查询已审核记录")
    public R<List<Apply>> getReviewedList() {
        return R.data(applyService.list(new QueryWrapper<Apply>().ne("status", 0)
                .orderByDesc("create_at")));
    }
    @GetMapping("/getNowVisitorList")
    @ApiOperation(value = "查询需要上报体温的记录")
    public R<List<Apply>> getNowVisitorList() {
//        String nowDay = DateUtil.refNowDay();
        return R.data(applyService.list(new QueryWrapper<Apply>()
//                .isNull("temperature")
                .orderByDesc("create_at")));
    }


}
