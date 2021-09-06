package org.penough.boot.database.datasource;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 基础数据库配置类
 *
 * todo 动态数据源相关功能没有添加，需要考虑设计
 *
 * @author pengcheng
 * @date 2020/12/01
 */
@Slf4j
@AllArgsConstructor
public abstract class BaseDatabaseConfiguration implements InitializingBean {

    protected final MybatisPlusProperties properties;

    private final List<MybatisPlusPropertiesCustomizer> mybatisPlusPropertiesCustomizers;
    private final ResourceLoader resourceLoader;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!CollectionUtils.isEmpty(this.mybatisPlusPropertiesCustomizers)){
            this.mybatisPlusPropertiesCustomizers.forEach(i -> i.customize(this.properties));
        }
        this.checkConfigFileExists();
    }

    private void checkConfigFileExists(){
        if(this.properties.isCheckConfigLocation() && StringUtils.hasText(this.properties.getConfigLocation())){
            Resource resource = this.resourceLoader.getResource(this.properties.getConfigLocation());
            Assert.state(resource.exists(), "Cannot find config location: " + resource + " (please add config file or check your Mybatis configuration)");
        }
    }

}
