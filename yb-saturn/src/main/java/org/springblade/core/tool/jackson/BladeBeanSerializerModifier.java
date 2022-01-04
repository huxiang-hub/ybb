//package org.springblade.core.tool.jackson;
package org.springblade.core.tool.jackson;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/*****
 * 重写blade-core核心包中的返回过滤文件，解决-1对象的问题。
 */
public class BladeBeanSerializerModifier extends BeanSerializerModifier {
    public BladeBeanSerializerModifier() {
    }

    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        beanProperties.forEach((writer) -> {
            if (!writer.hasNullSerializer()) {
                JavaType type = writer.getType();
                Class<?> clazz = type.getRawClass();
                if (type.isTypeOrSubTypeOf(Number.class)) {
                    writer.assignNullSerializer(BladeBeanSerializerModifier.NullJsonSerializers.NUMBER_JSON_SERIALIZER);
                } else if (type.isTypeOrSubTypeOf(Boolean.class)) {
                    writer.assignNullSerializer(BladeBeanSerializerModifier.NullJsonSerializers.BOOLEAN_JSON_SERIALIZER);
                } else if (type.isTypeOrSubTypeOf(Character.class)) {
                    writer.assignNullSerializer(BladeBeanSerializerModifier.NullJsonSerializers.STRING_JSON_SERIALIZER);
                } else if (type.isTypeOrSubTypeOf(String.class)) {
                    writer.assignNullSerializer(BladeBeanSerializerModifier.NullJsonSerializers.STRING_JSON_SERIALIZER);
                } else if (!type.isArrayType() && !clazz.isArray() && !type.isTypeOrSubTypeOf(Collection.class)) {
                    if (type.isTypeOrSubTypeOf(OffsetDateTime.class)) {
                        writer.assignNullSerializer(BladeBeanSerializerModifier.NullJsonSerializers.STRING_JSON_SERIALIZER);
                    } else if (!type.isTypeOrSubTypeOf(Date.class) && !type.isTypeOrSubTypeOf(TemporalAccessor.class)) {
                        writer.assignNullSerializer(BladeBeanSerializerModifier.NullJsonSerializers.OBJECT_JSON_SERIALIZER);
                    } else {
                        writer.assignNullSerializer(BladeBeanSerializerModifier.NullJsonSerializers.STRING_JSON_SERIALIZER);
                    }
                } else {
                    writer.assignNullSerializer(BladeBeanSerializerModifier.NullJsonSerializers.ARRAY_JSON_SERIALIZER);
                }

            }
        });
        return super.changeProperties(config, beanDesc, beanProperties);
    }

    public interface NullJsonSerializers {
        JsonSerializer<Object> STRING_JSON_SERIALIZER = new JsonSerializer<Object>() {
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                //gen.writeString("");
                gen.writeObject(null);
            }
        };
        JsonSerializer<Object> NUMBER_JSON_SERIALIZER = new JsonSerializer<Object>() {
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                //gen.writeNumber(-1);
                gen.writeObject(null);
            }
        };
        JsonSerializer<Object> BOOLEAN_JSON_SERIALIZER = new JsonSerializer<Object>() {
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//                gen.writeObject(Boolean.FALSE);
                gen.writeObject(null);
            }
        };
        JsonSerializer<Object> ARRAY_JSON_SERIALIZER = new JsonSerializer<Object>() {
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartArray();
                gen.writeEndArray();
            }
        };
        JsonSerializer<Object> OBJECT_JSON_SERIALIZER = new JsonSerializer<Object>() {
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartObject();
                gen.writeEndObject();
            }
        };
    }
}
