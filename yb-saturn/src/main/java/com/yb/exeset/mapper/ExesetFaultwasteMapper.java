package com.yb.exeset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.exeset.entity.ExesetFaultwaste;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 故障停机设置_yb_exeset_fault Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ExesetFaultwasteMapper extends BaseMapper<ExesetFaultwaste> {
    /**
     * 查询当前设备的停机废品信息接口
     * @param maId
     * @return
     */
    List<ExesetFaultwaste> getExesetFaultwaste(Integer maId);
    /**
     * 修改当前设备的停机废品信息接口
     * @param exesetFaultwaste
     * @return
     */
    boolean setFaultwaste(ExesetFaultwaste exesetFaultwaste);
}
