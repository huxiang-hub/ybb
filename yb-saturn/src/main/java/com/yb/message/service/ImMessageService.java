package com.yb.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.base.vo.BaseDeptinfoVO;
import com.yb.message.entity.ImMessage;
import com.yb.message.vo.ImMessageVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 乐天
 * @since 2018-10-08
 */
public interface ImMessageService extends IService<ImMessage> {

    /**
     * 自定义分页
     *
     * @param page
     * @param
     * @return
     */
    IPage<ImMessageVO> selectImMessagePage(IPage<ImMessageVO> page, ImMessageVO messageVO);

    IPage<ImMessageVO> selectImMessageMachinePage(IPage<ImMessageVO> page, ImMessageVO messageVO);

    IPage<ImMessageVO> selectImMessageChatPage(IPage<ImMessageVO> page, ImMessageVO messageVO);
}
