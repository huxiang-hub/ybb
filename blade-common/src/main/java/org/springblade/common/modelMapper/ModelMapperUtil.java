package org.springblade.common.modelMapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

/**
 * @author :   my
 * @date :     2020-07-19
 * description: ModelMapper配置
 */
public class ModelMapperUtil {

    /**
     *
     * @return
     */
    public static ModelMapper get() {
        return new ModelMapper();
    }

    /**
     * 严格匹配模式
     * @return modelMapper
     */
    public static ModelMapper getStrictModelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

}
