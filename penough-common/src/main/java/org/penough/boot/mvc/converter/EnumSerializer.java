package org.penough.boot.mvc.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.penough.boot.database.enumeration.BaseEnum;

import java.io.IOException;

/**
 * 枚举类序列化支持
 * 将枚举类输出为Desp
 *
 * @author Penough
 * @date 2021-09-17
 */
public class EnumSerializer extends StdSerializer<BaseEnum> {
    public final static EnumSerializer INSTANCE = new EnumSerializer();


    protected EnumSerializer() {
        super(BaseEnum.class);
    }

    @Override
    public void serialize(BaseEnum value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(valueToString(value));
    }

    public final String valueToString(BaseEnum value) {
        return value.getDesp();
    }
}
