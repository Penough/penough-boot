package org.penough.boot.core.exceptions;

/**
 * 业务异常基础类
 *
 * @author Penough
 * @date 2020/10/22
 */
public interface BaseException {

    /**
     * 统一参数验证异常码
     */
    int BASE_VALID_PARAM = -1;

    /**
     * 获取异常信息
     * @return
     */
    String getMessage();

    /**
     * 获取异常码【自定义】
     * @return
     */
    String getCode();


    /**
     * 获取状态码【RFC规范】
     * @return
     */
    int getStatus();
}
