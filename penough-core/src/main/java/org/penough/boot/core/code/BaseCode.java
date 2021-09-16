package org.penough.boot.core.code;

/**
 * 异常码接口类
 *
 * @author Penough
 * @date 2020/10/22
 */
public interface BaseCode {

    /**
     * 获取业务信息
     * @return
     */
    String getMsg();

    /**
     * 获取业务码【自定义】
     * @return
     */
    String getCode();

    /**
     * 获取http状态码【RFC规范】
     * @return
     */
    int getStatus();
}
