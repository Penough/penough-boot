package org.penough.boot.mvc.config;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.penough.boot.common.utils.DateUtils;
import org.penough.boot.mvc.jackson.MyJacksonModule;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 基础配置类
 *
 * Jackson处理xml和json是会通过Jackson2ObjectMapperBuilder进行ObjectMapper的构建<br>
 * JacksonAutoConfiguration通过Jackson2ObjectMapperBuilderCustomizer对Jackson2ObjectMapperBuilder进行客制化配置<br>
 * 这里使用抽象类，是为了通过继承的方式，给应用提供一种泛用性较强的预配置，节省配置工作量<br>
 * 子类继承该类，将会在基于已经costomiz的builder类构建的ObjectMapper技术上，进行配置，也就是说，会覆盖spring.jackson的配置 <br>
 *
 *
 * @see JacksonAutoConfiguration
 * @author Penough
 * @date 2020-11-25
 * @since 0.0.1
 */
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public abstract class BaseConfig {

    /**
     * 全局配置 序列化和反序列化规则<br>
     * <ul>
     * addSerializer：序列化 （Controller 返回 给前端的json）
     * <li> Long -> string</li>
     * <li> BigInteger -> string</li>
     * <li> BigDecimal -> string</li>
     * <li> date -> string</li>
     * <li> LocalDateTime -> "yyyy-MM-dd HH:mm:ss"</li>
     * <li> LocalDate -> "yyyy-MM-dd"</li>
     * <li> LocalTime -> "HH:mm:ss"</li>
     * <li> BaseEnum -> {"code": "xxx", "desc": "xxx"}</li>
     * </ul>
     * <p>
     * <ul>
     * addDeserializer: 反序列化 （前端调用接口时，传递到后台的json）
     * <li>  {"code": "xxx"} -> Enum </li>
     * <li> "yyyy-MM-dd HH:mm:ss" -> LocalDateTime </li>
     * <li> "yyyy-MM-dd" -> LocalDate </li>
     * <li> "HH:mm:ss" -> LocalTime </li>
     * </ul>
     * @apiNote 由于使用该类并添加@Configuration注解的情况下，会自动注册序列化器和反序列化器，因此不使用Convertor继续数据转换处理
     * @param builder
     * @return ObjectMapper
     */
    @Bean
    @Primary
    @ConditionalOnClass(ObjectMapper.class)
    @ConditionalOnMissingBean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper
                .setLocale(Locale.CHINA)
                //去掉默认的时间戳格式
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                // 时区
                .setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
                //Date参数日期格式
                .setDateFormat(new SimpleDateFormat(DateUtils.DEFAULT_DATE_TIME_FORMAT, Locale.CHINA))
                //该特性决定parser是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）。 如果该属性关闭，则如果遇到这些字符，则会抛出异常。JSON标准说明书要求所有控制符必须使用引号，因此这是一个非标准的特性
                .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
                // 忽略不能转移的字符
                .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true)
                .findAndRegisterModules()
                //在使用spring boot + jpa/hibernate，如果实体字段上加有FetchType.LAZY，并使用jackson序列化为json串时，会遇到SerializationFeature.FAIL_ON_EMPTY_BEANS异常
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                //忽略未知字段
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                //单引号处理
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                //反序列化时，属性不存在的兼容处理，防止抛出JsonMappingException
                .getDeserializationConfig()
                .withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new MyJacksonModule());
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }
}
