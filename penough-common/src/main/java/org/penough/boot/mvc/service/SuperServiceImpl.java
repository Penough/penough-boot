package org.penough.boot.mvc.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.penough.boot.core.exceptions.BaseBusinessException;
import org.penough.boot.core.exceptions.code.ExceptionCode;
import org.penough.boot.database.mapper.SuperMapper;

import java.util.List;

public class SuperServiceImpl<M extends SuperMapper<T>,T> extends ServiceImpl<M, T> implements SuperService<T> {

    public SuperMapper getSuperMapper(){
        if(baseMapper instanceof SuperMapper){
            return baseMapper;
        }
        throw BaseBusinessException.wrap(ExceptionCode.SUPER_MAPPER_TRERROR);
    }

    @Override
    public boolean saveBatchSomeColumn(List<T> entityList){
        if(entityList.isEmpty()){
            return true;
        }
        if(entityList.size() > 5000){
            throw BaseBusinessException.wrap(ExceptionCode.TOO_MANY_DATA_ERROR);
        }
        return SqlHelper.retBool(getSuperMapper().insertBatchSomeColumn(entityList));
    }

    @Override
    public boolean updateAllById(T entity) {
        return SqlHelper.retBool(getSuperMapper().updateAllById(entity));
    }
}
