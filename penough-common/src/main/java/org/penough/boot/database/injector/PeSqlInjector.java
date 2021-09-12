package org.penough.boot.database.injector;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.penough.boot.database.entity.SuperEntity;
import org.penough.boot.database.injector.method.UpdateAllById;

import java.util.List;

/**
 * SQL拓展方法注入器
 * 注入insertBatchSomeColumn
 * 注入UpdateAllById
 *
 * @author Penough
 * @date 2020/10/17
 */
public class PeSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methods = super.getMethodList(mapperClass, tableInfo);
        methods.add(new InsertBatchSomeColumn(i -> i.getFieldFill() != FieldFill.UPDATE && !i.isLogicDelete()));
        methods.add(new UpdateAllById(field -> !ArrayUtil.containsAny(new String[]{SuperEntity.CREATE_TIME_COLUMN, SuperEntity.CREATE_USER_COLUMN},
                field.getColumn())));
        return super.getMethodList(mapperClass, tableInfo);
    }
}
