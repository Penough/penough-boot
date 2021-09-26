package org.penough.boot.database.mybatis.scan;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 复制自{@link org.mybatis.spring.annotation.MapperScannerRegistrar}<br>
 * 新增实现{@link EnvironmentAware}接口，改写 {@link PeMapperScannerRegistrar#registerBeanDefinitions(AnnotationMetadata,AnnotationAttributes,BeanDefinitionRegistry,String)}方法<br>
 * 增加从配置动态读取{@link PeMapperScan#basePackages()}的功能
 *
 * @author Penough
 * @since 0.0.1
 */
@Slf4j
public class PeMapperScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private Environment environment;
    /**
     * {@inheritDoc}
     *
     * @deprecated Since 2.0.2, this method not used never.
     */
    @Override
    @Deprecated
    public void setResourceLoader(ResourceLoader resourceLoader) {
        // NOP
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(PeMapperScan.class.getName()));
        if (mapperScanAttrs != null) {
            registerBeanDefinitions(importingClassMetadata, mapperScanAttrs, registry,
                    generateBaseBeanName(importingClassMetadata, 0));
        }
    }

    void registerBeanDefinitions(AnnotationMetadata annoMeta, AnnotationAttributes annoAttrs,
                                 BeanDefinitionRegistry registry, String beanName) {

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
        builder.addPropertyValue("processPropertyPlaceHolders", true);

        Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
        if (!Annotation.class.equals(annotationClass)) {
            builder.addPropertyValue("annotationClass", annotationClass);
        }

        Class<?> markerInterface = annoAttrs.getClass("markerInterface");
        if (!Class.class.equals(markerInterface)) {
            builder.addPropertyValue("markerInterface", markerInterface);
        }

        Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            builder.addPropertyValue("nameGenerator", BeanUtils.instantiateClass(generatorClass));
        }

        Class<? extends MapperFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
        if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
            builder.addPropertyValue("mapperFactoryBeanClass", mapperFactoryBeanClass);
        }

        String sqlSessionTemplateRef = annoAttrs.getString("sqlSessionTemplateRef");
        if (StringUtils.hasText(sqlSessionTemplateRef)) {
            builder.addPropertyValue("sqlSessionTemplateBeanName", annoAttrs.getString("sqlSessionTemplateRef"));
        }

        String sqlSessionFactoryRef = annoAttrs.getString("sqlSessionFactoryRef");
        if (StringUtils.hasText(sqlSessionFactoryRef)) {
            builder.addPropertyValue("sqlSessionFactoryBeanName", annoAttrs.getString("sqlSessionFactoryRef"));
        }

        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(
                Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));

        // tag 改写部分
//        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
//                .collect(Collectors.toList()));
        Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
                .forEach(item -> resolveEvnProperty(basePackages, item));
        // tag 实现动态读取配置

        basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName)
                .collect(Collectors.toList()));

        if (basePackages.isEmpty()) {
            basePackages.add(getDefaultBasePackage(annoMeta));
        }

        String lazyInitialization = annoAttrs.getString("lazyInitialization");
        if (StringUtils.hasText(lazyInitialization)) {
            builder.addPropertyValue("lazyInitialization", lazyInitialization);
        }

        String defaultScope = annoAttrs.getString("defaultScope");
        if (!AbstractBeanDefinition.SCOPE_DEFAULT.equals(defaultScope)) {
            builder.addPropertyValue("defaultScope", defaultScope);
        }

        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));

        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

    }

    // DEFAULT_PLACEHOLDER块匹配正则
    private static final String REG_STR = "\\$\\{(\\S*?)\\}";
    /**
     * 通过正则表达式解析配置字符串
     * @param param 配置参数，exp:${a.b.c}，支持如 "com.a.b,${p1.p2.p3},com.c,${p1.p2.p4}"这样的配置
     * @return
     */
    private void resolveEvnProperty(List basePackages, String param){
        String[] pkgs = param.split(StringPool.COMMA);
        Pattern pat = Pattern.compile(REG_STR);
        Matcher m;
        String key,val;
        List<String> vals;
        for (int i = 0; i < pkgs.length; i++) {
            m = pat.matcher(pkgs[i]);
            if(!m.find()){
                basePackages.add(pkgs[i]);
            }else{
                key = m.group(1);
                val = environment.getProperty(key);
                log.debug("find placeholder value " + val + " for key " + key);
                if(val == null){
                    //maybe a list
                    log.debug("maybe a list for key " + key);
                    resolveEvnListProperty(basePackages, key);
                } else {
                    basePackages.add(val);
                }
            }
        }
    }

    private void resolveEvnListProperty(List basePackages, String key) {
        Integer i = 0;
        String testKey = key + StringPool.LEFT_SQ_BRACKET + i + StringPool.RIGHT_SQ_BRACKET;
        Assert.notNull(environment.getProperty(testKey), "can not read value from " + key);
        String val;
        while ( (val=environment.getProperty(testKey))!=null) {
            log.debug("find placeholder value " + val + " for key " + key);
            basePackages.add(val);
            testKey = key + StringPool.LEFT_SQ_BRACKET + (++i) + StringPool.RIGHT_SQ_BRACKET;
        }
    }

    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
        return importingClassMetadata.getClassName() + "#" + org.mybatis.spring.annotation.MapperScannerRegistrar.class.getSimpleName() + "#" + index;
    }

    private static String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
        return ClassUtils.getPackageName(importingClassMetadata.getClassName());
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * A {@link org.mybatis.spring.annotation.MapperScannerRegistrar} for {@link PeMapperScans}.
     *
     * @since 0.0.1
     */
    static class RepeatingRegistrar extends PeMapperScannerRegistrar {
        /**
         * {@inheritDoc}
         */
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            AnnotationAttributes mapperScansAttrs = AnnotationAttributes
                    .fromMap(importingClassMetadata.getAnnotationAttributes(PeMapperScans.class.getName()));
            if (mapperScansAttrs != null) {
                AnnotationAttributes[] annotations = mapperScansAttrs.getAnnotationArray("value");
                for (int i = 0; i < annotations.length; i++) {
                    registerBeanDefinitions(importingClassMetadata, annotations[i], registry,
                            generateBaseBeanName(importingClassMetadata, i));
                }
            }
        }
    }

}