package com.yb.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.base.vo.BaseDeptinfoVO;
import com.yb.message.entity.ImMessage;
import com.yb.message.vo.ImMessageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author 乐天
 * @since 2018-10-08
 */
@Mapper
public interface ImMessageMapper extends BaseMapper<ImMessage> {


    /**
     * 自定义分页
     *
     * @param page
     * @param
     * @return
     */
    List<ImMessageVO> selectImMessagePage(IPage page, ImMessageVO messageVO);

    List<ImMessageVO> selectImMessageMachinePage(IPage page, ImMessageVO messageVO);

    List<ImMessageVO> selectImMessageChatPage(IPage<ImMessageVO> page, ImMessageVO messageVO);
}
