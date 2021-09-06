package org.penough.boot.database.mybatis.typehandler;
/**
 * 全like处理
 *
 * @author Penough
 * @date 2020/10/17
 */
public class FullLikeTypeHandler extends BaseLikeTypeHandler {
    public FullLikeTypeHandler() {
        super(true, true);
    }
}
