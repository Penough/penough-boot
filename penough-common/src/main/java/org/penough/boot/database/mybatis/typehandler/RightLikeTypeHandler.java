package org.penough.boot.database.mybatis.typehandler;
/**
 * 右like处理
 *
 * @author Penough
 * @date 2020/10/17
 */
public class RightLikeTypeHandler extends BaseLikeTypeHandler {
    public RightLikeTypeHandler() {
        super(false, true);
    }
}
