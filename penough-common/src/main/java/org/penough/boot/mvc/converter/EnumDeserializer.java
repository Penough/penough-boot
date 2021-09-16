package org.penough.boot.mvc.converter;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.penough.boot.database.enumeration.BaseEnum;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * enum反序列化工具
 *
 * @author Penough
 *
 */
@Slf4j
public class EnumDeserializer extends StdDeserializer<BaseEnum<?>> {
    public final static EnumDeserializer INSTANCE = new EnumDeserializer();
    private final static String ALL_ENUM_STRING_CONVERT_METHOD = "getByDesp";
    private final static String ALL_ENUM_KEY_FIELD = "code";

    public EnumDeserializer() {
        super(Enum.class);
    }

    @Override
    public BaseEnum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken token = p.getCurrentToken();
        if (!token.equals(JsonToken.VALUE_STRING)) {
            log.warn("解析枚举失败！参数类型非字符串，无法处理！");
            return null;
        }
        String value = p.getValueAsString();
        if (StrUtil.isBlank(value)) {
            return null;
        }

        Object obj = p.getCurrentValue();
        if (obj == null) {
            return null;
        }
        Field field = ReflectUtil.getField(obj.getClass(), p.getCurrentName());
        Class<?> fieldType = field.getType();
        try {
            Method method = fieldType.getMethod(ALL_ENUM_STRING_CONVERT_METHOD, String.class);
            return (BaseEnum<?>) method.invoke(null, value);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            log.warn("解析枚举失败", e);
            return null;
        }
    }


}
