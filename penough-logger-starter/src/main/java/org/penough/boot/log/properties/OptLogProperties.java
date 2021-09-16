package org.penough.boot.log.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.penough.boot.log.properties.OptLogProperties.PREFIX;

/**
 * 操作日志配置类
 *
 * @author Penough
 * @date 2021-09-16
 */
@ConfigurationProperties(prefix = PREFIX)
@Data
@NoArgsConstructor
public class OptLogProperties {
    public static final String PREFIX = "penough.log";

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 日志存储类型
     */
    private OptLogType type = OptLogType.DB;
}
