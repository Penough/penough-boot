package org.penough.boot.database.enumeration;

import com.baomidou.mybatisplus.annotation.IEnum;

import java.io.Serializable;

public interface BaseEnum<T extends Serializable> extends IEnum {
    /**
     * 务必重写，getCode方法
     * @return
     */
    T getCode();

    /**
     * 务必重写，getDesp方法
     * @return
     */
    String getDesp();

    /**
     * 数据库存值
     * @return
     */
    @Override
    default T getValue() { return getCode(); }


}
