package org.penough.boot.core.database.datasource;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.penough.boot.core.database.injector.PeSqlInjector;
import org.penough.boot.core.database.mybatis.typehandler.FullLikeTypeHandler;
import org.penough.boot.core.database.mybatis.typehandler.LeftLikeTypeHandler;
import org.penough.boot.core.database.mybatis.typehandler.RightLikeTypeHandler;
import org.penough.boot.core.database.properties.DatabaseProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.util.Collections;
import java.util.List;

@Slf4j
public class BaseMybatisConfiguration {

    protected DatabaseProperties databaseProperties;

    public BaseMybatisConfiguration(DatabaseProperties databaseProperties){
        this.databaseProperties = databaseProperties;
    }

    /**
     * 引入Mp插件集合，包含分页插件，乐观锁插件，sql规范插件，防止全表更新/删除插件
     *
     * @author Penough
     * @return
     */
    @Order(5)
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        // 声明MP拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();



        // 自定义MP分页器前内联插件添加
        List<InnerInterceptor> beforePaginationInterceptors = getBeforePaginationInterceptor();
        if(!beforePaginationInterceptors.isEmpty()){
            beforePaginationInterceptors.forEach(interceptor::addInnerInterceptor);
        }

        // 定义分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 分页条数限制
        paginationInnerInterceptor.setMaxLimit(databaseProperties.getLimit());
        // 数据库类型设置，不同数据库分页sql不同
        paginationInnerInterceptor.setDbType(databaseProperties.getDbType());
        // 溢出总页数处理
        paginationInnerInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        // 自定义MP分页器后内联插件添加
        List<InnerInterceptor> afterPaginationInterceptors = getAfterPaginationInterceptor();
        if(!afterPaginationInterceptors.isEmpty()){
            afterPaginationInterceptors.forEach(interceptor::addInnerInterceptor);
        }

        // 是否启用禁全表操作插件
        if(databaseProperties.getIsBlockAttack()){
            BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
            interceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        }
        // 是否启用sql性能规范插件
        if(databaseProperties.getIsIllegalSql()){
            IllegalSQLInnerInterceptor illegalSQLInnerInterceptor = new IllegalSQLInnerInterceptor();
            interceptor.addInnerInterceptor(illegalSQLInnerInterceptor);
        }
        return interceptor;
    }


    /**
     * 自定义分页器前内联插件,子类继承后按需要重写该方法
     *
     * 否则直接返回空列表
     * @return
     */
    protected List<InnerInterceptor> getBeforePaginationInterceptor() {
        return Collections.emptyList();
    }


    /**
     * 自定义分页器后内联插件,子类继承后按需要重写该方法
     *
     * 否则直接返回空列表
     * @return
     */
    protected List<InnerInterceptor> getAfterPaginationInterceptor() {
        return Collections.emptyList();
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

    @Bean("myMetaObjectHandler")
    @ConditionalOnMissingBean
    public MetaObjectHandler getPeMetaObjectHandler(){
        return new PeMetaObjectHandler();
    }

    /**
     * MB 自定义类型处理器
     * 在mapper.xml中 #{name, typeHandler=leftLike}使用
     * @return
     */
    @Bean
    public LeftLikeTypeHandler getLeftLikeTypeHandler(){
        return new LeftLikeTypeHandler();
    }

    /**
     * MB 自定义类型处理器
     * 在mapper.xml中 #{name, typeHandler=rightLike}使用
     * @return
     */
    @Bean
    public RightLikeTypeHandler getRightLikeTypeHandler(){
        return new RightLikeTypeHandler();
    }
    /**
     * MB 自定义类型处理器
     * 在mapper.xml中 #{name, typeHandler=fullLike}使用
     * @return
     */
    @Bean
    public FullLikeTypeHandler getFullLikeTypeHandler(){
        return new FullLikeTypeHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public PeSqlInjector getPeSqlInjector(){
        return new PeSqlInjector();
    }
}
