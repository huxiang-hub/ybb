package org.springblade.common.modelMapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.*;

/**
 * @author :   my
 * @date :     2020-07-19
 * description: 数据转换类-ModelMapper
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    @Scope("singleton")
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        //默认为standard模式，设置为strict模式
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //验证映射
        modelMapper.validate();

        // 配置代码
        return modelMapper;
    }
}
