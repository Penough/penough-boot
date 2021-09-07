package org.penough.boot.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 棘突MP的 BaseMapper新增2个方法：insertBatchSomeColumn,updateAllById
 *
 * @param <T> 实体
 * @author Penough
 * @date 2020/10/22
 */
public interface SuperMapper<T> extends BaseMapper<T> {

    /**
     * 全量修改所有字段
     *
     * @param entity 实体
     * @return
     */
    int updateAllById(@Param(Constants.ENTITY) T entity);


    /**
     * 批量插入所有字段
     *
     * @param entityList 实体列表
     * @return
     */
    int insertBatchSomeColumn(List<T> entityList);
}
