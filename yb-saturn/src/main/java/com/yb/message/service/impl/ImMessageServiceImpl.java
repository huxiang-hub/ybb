package com.yb.message.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.message.entity.ImMessage;
import com.yb.message.mapper.ImMessageMapper;
import com.yb.message.service.ImMessageService;
import com.yb.message.vo.ImMessageVO;
import org.springblade.common.tool.ChatType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 *
 * @author 乐天
 * @since 2018-10-08
 */
@Service
@Qualifier(value = "iImMessageService")
public class ImMessageServiceImpl extends ServiceImpl<ImMessageMapper, ImMessage> implements ImMessageService {

    @Override
    public IPage<ImMessageVO> selectImMessagePage(IPage<ImMessageVO> page, ImMessageVO messageVO) {
        if (messageVO.getFromId()==null||"".equals(messageVO.getFromId())){// 发送者 消息来源
            messageVO.setFromId(ChatType.MSG_SYSTEM.getType());//查找机台系统消息 type：1000
        }
        /**
         * 分页模糊查询
         */
        messageVO.setType(ChatType.typeToDesc(messageVO.getType()));
       // messageVO.setType(ChatType.typeToDesc(messageVO.getFromId()));
        List<ImMessageVO> pages = baseMapper.selectImMessagePage(page, messageVO);
        for (ImMessageVO temp : pages){
            temp.setFromId(ChatType.MSG_SYSTEM.getDesc());
           if(ChatType.MAC_QUALITY.getType().equals(temp.getType())){
                temp.setType(ChatType.MAC_QUALITY.getDesc());
           }else if(ChatType.MAC_PROORDER.getType().equals(temp.getType())){
               temp.setType(ChatType.MAC_PROORDER.getDesc());
           }else if(ChatType.MAC_DOWN.getType().equals(temp.getType())){
               temp.setType(ChatType.MAC_DOWN.getDesc());
           }else if(ChatType.MAC_LOGIN.getType().equals(temp.getType())){
               temp.setType(ChatType.MAC_LOGIN.getDesc());
           }else if(ChatType.MAC_ACCEPTORDER.getType().equals(temp.getType())){
               temp.setType(ChatType.MAC_ACCEPTORDER.getDesc());
           }else if(ChatType.MAC_CHANGE.getType().equals(temp.getType())){
               temp.setType(ChatType.MAC_CHANGE.getDesc());
           }else if(ChatType.MAC_FINISHORDER.getType().equals(temp.getType())){
               temp.setType(ChatType.MAC_FINISHORDER.getDesc());
           }else if(ChatType.MAC_LOGOUT.getType().equals(temp.getType())){
               temp.setType(ChatType.MAC_LOGOUT.getDesc());
           }else if(ChatType.MAC_REPORTORDER.getType().equals(temp.getType())){
               temp.setType(ChatType.MAC_REPORTORDER.getDesc());
           }else if(ChatType.MAC_MAINTAIN.getType().equals(temp.getType())){
               temp.setType(ChatType.MAC_MAINTAIN.getDesc());
           }
        }
        return page.setRecords(pages);
    }
    @Override
    public IPage<ImMessageVO> selectImMessageMachinePage(IPage<ImMessageVO> page, ImMessageVO messageVO) {
        if (messageVO.getName()==null) {
            messageVO.setName("maId");
        }
        if (messageVO.getFromId()==null||"".equals(messageVO.getFromId())){
            messageVO.setFromId(ChatType.MSG_SYSTEM.getType());//查找机台系统消息 type：1000
        }
        /**
         * 分页模糊查询
         */
        messageVO.setFromId(ChatType.typeToDesc(messageVO.getFromId()));
        messageVO.setType(ChatType.typeToDesc(messageVO.getType()));
        List<ImMessageVO> pages = baseMapper.selectImMessageMachinePage(page, messageVO);
        for (ImMessageVO temp : pages){
            temp.setFromId(ChatType.MSG_SYSTEM.getDesc());
            if(ChatType.MAC_QUALITY.getType().equals(temp.getType())){
                temp.setType(ChatType.MAC_QUALITY.getDesc());
            }else if(ChatType.MAC_PROORDER.getType().equals(temp.getType())){
                temp.setType(ChatType.MAC_PROORDER.getDesc());
            }else if(ChatType.MAC_DOWN.getType().equals(temp.getType())){
                temp.setType(ChatType.MAC_DOWN.getDesc());
            }else if(ChatType.MAC_LOGIN.getType().equals(temp.getType())){
                temp.setType(ChatType.MAC_LOGIN.getDesc());
            }else if(ChatType.MAC_ACCEPTORDER.getType().equals(temp.getType())){
                temp.setType(ChatType.MAC_ACCEPTORDER.getDesc());
            }else if(ChatType.MAC_CHANGE.getType().equals(temp.getType())){
                temp.setType(ChatType.MAC_CHANGE.getDesc());
            }else if(ChatType.MAC_FINISHORDER.getType().equals(temp.getType())){
                temp.setType(ChatType.MAC_FINISHORDER.getDesc());
            }else if(ChatType.MAC_LOGOUT.getType().equals(temp.getType())){
                temp.setType(ChatType.MAC_LOGOUT.getDesc());
            }else if(ChatType.MAC_REPORTORDER.getType().equals(temp.getType())){
                temp.setType(ChatType.MAC_REPORTORDER.getDesc());
            }else if(ChatType.MAC_MAINTAIN.getType().equals(temp.getType())){
                temp.setType(ChatType.MAC_MAINTAIN.getDesc());
            }
        }
        return page.setRecords(pages);
    }
    @Override
    public IPage<ImMessageVO> selectImMessageChatPage(IPage<ImMessageVO> page, ImMessageVO messageVO) {
        if (messageVO.getFromId()==null||"".equals(messageVO.getFromId())){
            messageVO.setFromId(ChatType.MSG_SYSTEM.getType());//查找机台系统消息 type：1000
        }
        return page.setRecords(baseMapper.selectImMessageChatPage(page,messageVO));
    }
}
