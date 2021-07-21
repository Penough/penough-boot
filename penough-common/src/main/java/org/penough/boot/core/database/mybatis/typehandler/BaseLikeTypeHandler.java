package org.penough.boot.core.database.mybatis.typehandler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * like处理基本类
 *
 * @author Penough
 * @date 2020/10/17
 */
public class BaseLikeTypeHandler extends BaseTypeHandler<CharSequence> {
    private static final String LIKE = "%";
    private final boolean leftlike;
    private final boolean rightlike;

    public BaseLikeTypeHandler(boolean leftlike, boolean rightlike){
        this.leftlike = leftlike;
        this.rightlike = rightlike;
    }

    /**
     * mp like查询转换
     * @param value
     * @return
     */
    public static String likeConvert(String value){
        if(StringUtils.isNotBlank(value)){
            value = value.replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
            return value;
        } else {
            return "";
        }
    }

    public static Object likeConvert(Object value) {
        if (value instanceof String) {
            return likeConvert(String.valueOf(value));
        }
        return value;
    }

    public static String likeConvertProcess(Object value){
        if(value instanceof String){
            return likeConvert(String.valueOf(value));
        }
        return String.valueOf(value);
    }

    private String convert(String value){
        value = value.replaceAll("\\%", "\\\\%").replaceAll("\\_", "\\\\_");
        return value;
    }

    private String like(String parameter){
        String result = convert(parameter);
        if(this.leftlike){
            result = LIKE + result;
        }
        if(this.rightlike){
            result += LIKE;
        }
        return  result;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, CharSequence charSequence, JdbcType jdbcType) throws SQLException {
        if(charSequence == null){
            preparedStatement.setString(i, null);
        } else {
            preparedStatement.setString(i, like(charSequence.toString()));
        }
    }

    @Override
    public CharSequence getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return resultSet.getString(s);
    }

    @Override
    public CharSequence getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getString(i);
    }

    @Override
    public CharSequence getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return callableStatement.getString(i);
    }
}
