package org.penough.boot.mvc.controller;

import org.penough.boot.mvc.controller.request.ApiResult;
import org.penough.boot.mvc.service.SuperService;

/**
 * 基础接口
 *
 * @param <Entity> 实体
 * @author Penough
 * @date 2020年03月07日21:56:32
 */
public interface BaseController<Entity> {

    Class<Entity> getEntityClass();

    SuperService<Entity> getBaseService();


    /**
     * 成功返回
     *
     * @param <T>  返回类型
     * @return R
     */
    default <T> ApiResult<T> success() {
        return ApiResult.SUCCESS();
    }

    /**
     * 成功返回
     *
     * @param data 返回内容
     * @param <T>  返回类型
     * @return R
     */
    default <T> ApiResult<T> success(T data) {
        return ApiResult.SUCCESS(data);
    }

    /**
     * 失败返回
     * @param <T> 返回类型
     * @return
     */
    default <T> ApiResult<T> fail() {
        return ApiResult.FAILED();
    }

}
