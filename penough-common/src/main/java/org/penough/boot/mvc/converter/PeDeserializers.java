package org.penough.boot.mvc.converter;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.type.ClassKey;
import org.penough.boot.database.enumeration.BaseEnum;
import org.penough.boot.mvc.jackson.MyJacksonModule;

/**
 * 反序列化器集
 * 重写findEnumDeserializer方法，用以兼容通用枚举类反序列化器获取 <br/>
 * 在 {@link MyJacksonModule#MyJacksonModule() } 中首先初始化
 * @author penough
 * @since 0.0.1
 * @date 2021-09-14
 */
public class PeDeserializers extends SimpleDeserializers  {
    @Override
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type,
                                                    DeserializationConfig config, BeanDescription beanDesc)
            throws JsonMappingException {
        if (_classMappings == null) {
            return null;
        }
        JsonDeserializer<?> deser = _classMappings.get(new ClassKey(type));
        if (deser == null) {
            // to support BaseEnum extends class or interface implements conditions
            if (BaseEnum.class.isAssignableFrom(type)) {
                deser = _classMappings.get(new ClassKey(BaseEnum.class));
                if(deser!=null) return deser;
            }
            if (_hasEnumDeserializer && type.isEnum()) {
                deser = _classMappings.get(new ClassKey(Enum.class));
            }
        }
        return deser;
    }
}
