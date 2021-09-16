package org.penough.boot.log;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.penough.boot.core.utils.JsonUtil;
import org.penough.boot.log.aspect.PeLogAspect;
import org.penough.boot.log.listener.PeOptLogListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 日志自动配置
 *
 * @author Penough
 * @date 2021-09-16
 */
@Slf4j
@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
//@ConditionalOnProperty(prefix = OptLogProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class LogAutoConfiguration {

    private static final String MSG_PATTERN = "{}|{}|{}";

    @Bean
    @ConditionalOnMissingBean
    public PeLogAspect peLogAspect() {
        return new PeLogAspect();
    }

//    @Bean
//    public MdcMvcConfigurer getMdcMvcConfigurer() {
//        return new MdcMvcConfigurer();
//    }

    /**
     * 在无装载jacksonObjectMapper的情况下，实例化ObjectMapperBean，最低优先级
     * @return
     */
    @Bean
    @Order
    @ConditionalOnMissingBean
    public ObjectMapper jacksonObjectMapper(){
        return new ObjectMapper();
    }

    /**
     * 默认logger日志处理方式<br>
     * 可替代
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnExpression("${penough.log.enabled:true} && 'LOGGER'.equals('${penough.log.type:LOGGER}')")
    public PeOptLogListener sysLogListener() {
        return new PeOptLogListener((l) -> {
            log.debug(MSG_PATTERN, System.currentTimeMillis(), "OPT_LOG", JsonUtil.parseJsonString(l));
        });
    }

}