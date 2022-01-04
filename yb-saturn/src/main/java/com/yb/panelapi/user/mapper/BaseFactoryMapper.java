package com.yb.panelapi.user.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.panelapi.user.entity.BaseFactory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseFactoryMapper extends BaseMapper<BaseFactory> {
    /**
     * 获取厂区名
     * @return
     */
    String getFactoryName();
}
