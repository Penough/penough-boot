package org.penough.boot.core.mvc.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 基于MP IService新增2个方法，和Mapper对应，saveBatchSomeColumn,updateAllById
 *
 *
 */
public interface SuperService<T> extends IService<T> {

    /**
     * 批量保存数据
     *
     * @param entityList
     * @return
     */
    boolean saveBatchSomeColumn(List<T> entityList);

    /**
     * 根据ID修改entity所有的字段
     * @param entity
     * @return
     */
    boolean updateAllById(T entity);
}
