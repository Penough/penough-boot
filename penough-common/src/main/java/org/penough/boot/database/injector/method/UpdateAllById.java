package org.penough.boot.database.injector.method;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;

import java.util.function.Predicate;

/**
 * 注入更新方法
 *
 * @author Penough
 * @date 2020/10/17
 */
public class UpdateAllById extends AlwaysUpdateSomeColumnById {

    public UpdateAllById(Predicate<TableFieldInfo> predicate){
        super(predicate);
    }

    @Override
    public String getMethod(SqlMethod sqlMethod) {
        // 自定义mapper方法
        return "updateAllById";
    }
}
