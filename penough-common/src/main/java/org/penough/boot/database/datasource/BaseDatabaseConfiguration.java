package org.penough.boot.database.datasource;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.penough.boot.database.mybatis.scan.PeMapperScan;
import org.penough.boot.database.properties.DatabaseProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 基础数据库配置类<br>
 * 动态数据源相关功能直接采用MP的多数据源处理
 *
 * @author Penough
 * @date 2020/12/01
 */
@Slf4j
@Configuration
@PeMapperScan(basePackages = {"${penough.database.base-package}"}, annotationClass = Repository.class)
@AllArgsConstructor
public abstract class BaseDatabaseConfiguration implements InitializingBean {

    protected final DatabaseProperties databaseProperties;
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

    /**
     * 通过预先装载SqlSessionFactory对{@link com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration#sqlSessionTemplate(SqlSessionFactory)}来获得动态连接池的效果<br>
     * 利用{@link MapperScan#sqlSessionTemplateRef()}来生成特定连接池包装的数据源<br>
     * 由于MP已支持Seata，Druid，P6SPY，所以不在需要以上操作<br>
     * 但还是要为SqlSession提供Template操作，根据ExecutorType处理。
     *
     * @see com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration
     * @see MapperScan
     * @param sqlSessionFactory
     * @return
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        ExecutorType executorType = this.properties.getExecutorType();
        if (executorType != null) {
            return new SqlSessionTemplate(sqlSessionFactory, executorType);
        } else {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }



}
