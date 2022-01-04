package com.yb.supervise.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.supervise.entity.SuperviseRunregular;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 设备状态间隔表
 * @Author my
 * @Date Created in 2020/6/9
 */
@Mapper
public interface SuperviseRunregularMapper extends BaseMapper<SuperviseRunregular> {

    /**
     * 根据uuid获取最新的定时检测状态信息的第一条记录
     *
     * @param uuid
     * @return
     */
    SuperviseRunregular getByuuid(@Param("uuid") String uuid);

    int saveRunregular(SuperviseRunregular runregular);

    int updateRunregular(SuperviseRunregular runregular);
}
