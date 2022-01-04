package com.anaysis.executSupervise.mapper;


import com.anaysis.executSupervise.entity.SuperviseRegular;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 设备状态间隔表
 * @Author my
 * @Date Created in 2020/6/9
 */
@Mapper
public interface SuperviseRegularMapper extends BaseMapper<SuperviseRegular> {

    /**
     * 根据uuid获取最新的定时检测状态信息的第二条记录
     *
     * @param uuid
     * @return
     */
    SuperviseRegular getPreviousRegularLog(@Param("uuid") String uuid);


    /**
     * 根据uuid获取最新的检测状态信息的第二条记录
     *
     * @param uuid
     * @return
     */
    SuperviseRegular getPreviousLog(@Param("uuid") String uuid);
}
