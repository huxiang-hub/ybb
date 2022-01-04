package com.anaysis.command;

import com.anaysis.command.mapper.HdverifyCommandMapper;
import com.anaysis.executSupervise.service.AnalyTenantService;
import io.netty.channel.ChannelHandlerContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/21 10:22
 */
@RestController
@AllArgsConstructor
@RequestMapping("/command")
@Api(tags = "解析指令")
@Slf4j
public class CommandController {

    @Autowired
    private HdverifyCommandMapper hdverifyCommandMapper;

    @Autowired
    private AnalyTenantService analyTenantService;

    /**
     * 发送解析指令
     */
    @GetMapping("/sendCommand")
    @ApiOperation(value = "发送解析指令")
    public R waitList(String uuid, CommandEnum.Order command) {
        log.debug("给盒子发送命令:[uuid:{}, command:{}]", uuid, command);

        ChannelHandlerContext channelHandlerContext = Command.map.get(uuid);
        if (channelHandlerContext != null) {
            System.out.println("发送命令中");
            CommandUtils.sendClient(channelHandlerContext, command.getDesc());
        }
//        String tenantId = analyTenantService.getTenantId(uuid);
//        Date date = new Date();
//        HdverifyCommand hdverifyCommand = new HdverifyCommand();
//        hdverifyCommand.setCreateAt(date);
//        hdverifyCommand.setUuid(uuid);
//        hdverifyCommand.setHcType(1);
//        hdverifyCommand.setSendContent(command.getDesc());
//        hdverifyCommand.setSendTime(date);
//        hdverifyCommandMapper.insert(hdverifyCommand);
        return R.success("ok");
    }
}
