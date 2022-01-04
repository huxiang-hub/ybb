package com.yb.config;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/29.
 */
@Configuration
public class BladeBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(
            SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        beanProperties.forEach(writer ->{
            if (writer.hasNullSerializer()){
                return;
            }
        });
        return super.changeProperties(config, beanDesc, beanProperties);
    }
}
