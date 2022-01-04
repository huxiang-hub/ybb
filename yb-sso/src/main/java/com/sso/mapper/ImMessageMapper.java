package com.sso.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.chatapi.message.entity.ImMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper 接口
 *
 * @author 乐天
 * @since 2018-10-08
 */
@Mapper
public interface ImMessageMapper extends BaseMapper<ImMessage> {

}
