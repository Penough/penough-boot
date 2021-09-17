package org.penough.boot.database.properties;

import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.penough.boot.database.properties.DatabaseProperties.PREFIX;

/**
 * 数据库属性配置
 *
 * @author Penough
 * @date 2020/10/16
 */
@ConfigurationProperties(prefix =  PREFIX)
@Data
@NoArgsConstructor
public class DatabaseProperties {
    public static final String PREFIX = "penough.database";

    /**
     * 是否启用攻击SQL阻断解析器
     */
    private Boolean isBlockAttack = false;

    /**
     * 是否启用  sql性能规范插件
     */
    public Boolean isIllegalSql = false;

    /**
     * 是否启用seata
     */
    private Boolean isSeata = false;

    /**
     * 是否启用数据查询权限
     */
    private Boolean isDataScope = false;

    /**
     * 事务超时时间
     */
    private int txTimeout = 60 * 60;

    /**
     * 分页大小限制
     */
    protected long limit = -1;

    /**
     * 数据库类型，默认Mysql
     */
    protected DbType dbType = DbType.MYSQL;

    /**
     * 是否启用mybatis二级缓存<br>
     * 正常建议不启动，在分布式环境下使用本地缓存，会存在诸多隐患<br>
     * 如脏读，数据延迟等<br>
     * 但是在数据服务/分布式缓存服务不可用，仍需要服务提供数据的情况，可启用该数据缓存<br>
     */
    protected Boolean localCacheEnabled = false;

    /**
     * 需要参与事务管理的操作
     */
    private List<String> txOptList = new ArrayList<>(Arrays.asList("add", "save", "insert*", "create*",
            "update*", "edit*", "upload*", "delete*", "remove*",
            "clean*", "recycle*", "batch*", "mark*", "disable*", "enable*", "handle*",
            "syn*", "reg*", "gen*", "*Tx"));

}
