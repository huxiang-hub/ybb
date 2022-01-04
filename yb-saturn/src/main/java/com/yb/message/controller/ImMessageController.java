package com.yb.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.message.vo.ImMessageVO;
import com.yb.message.entity.ImMessage;
import com.yb.message.service.ImMessageService;
import com.yb.message.wrapper.ImMessageWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
/**
 * im_message 控制器
 * 消息的管理
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/msg")
@Api(value = "im_message", tags = "im_message")
public class ImMessageController extends BladeController {

    private ImMessageService messageService;
    /**
     * 自定义分页 人员消息
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "ImMessage")
    public R<IPage<ImMessageVO>> page(ImMessageVO messageVO, Query query) {
        IPage<ImMessageVO> pages = null;
        if (messageVO.getFlag()==1) {//查询系统给用户的
            pages = messageService.selectImMessagePage(Condition.getPage(query), messageVO);
        }else if(messageVO.getFlag()==0){//查询系统给设备的
            pages = messageService.selectImMessageMachinePage(Condition.getPage(query), messageVO);
        }else if(messageVO.getFlag()==2){//查询用户聊天的
            pages = messageService.selectImMessageChatPage(Condition.getPage(query),messageVO);
        }
        return R.data(pages);
    }

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "ImMessage")
    public R<ImMessageVO> detail(@RequestBody ImMessageVO messageVO) {
        ImMessage detail = messageService.getOne(Condition.getQueryWrapper(messageVO));
        return R.data(ImMessageWrapper.build().entityVO(detail));
    }

    /**
     * 新增 部门结构_yb_ba_dept
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "ImMessage")
    public R save(@Valid @RequestBody ImMessageVO messageVO) {

        return R.status(messageService.save(messageVO));
    }

    /**
     * 新增或修改 部门结构_yb_ba_dept
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入baseDeptinfo")
    public R submit(@Valid @RequestBody ImMessageVO messageVO) {
        return R.status(messageService.saveOrUpdate(messageVO));
    }
    /**
     * 删除 部门结构_yb_ba_dept
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(messageService.removeByIds(Func.toIntList(ids)));
    }
}
