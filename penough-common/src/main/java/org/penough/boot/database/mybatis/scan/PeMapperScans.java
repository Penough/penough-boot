package org.penough.boot.database.mybatis.scan;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * 复制自{@link org.mybatis.spring.annotation.MapperScans}
 *
 * @author Penough
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(PeMapperScannerRegistrar.RepeatingRegistrar.class)
public @interface PeMapperScans {
    PeMapperScan[] value();
}