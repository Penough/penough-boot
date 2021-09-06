package org.penough.boot.database.mybatis.typehandler;

/**
 * 左like处理
 *
 * @author Penough
 * @date 2020/10/17
 */
public class LeftLikeTypeHandler extends BaseLikeTypeHandler {
    public LeftLikeTypeHandler() {
        super(true, false);
    }
}
