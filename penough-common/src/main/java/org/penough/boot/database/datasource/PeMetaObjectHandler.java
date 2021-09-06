package org.penough.boot.database.datasource;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.penough.boot.core.constants.Constants;
import org.penough.boot.common.utils.ThreadLocalUtil;
import org.penough.boot.database.entity.BaseEntity;
import org.penough.boot.database.entity.SuperEntity;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;


/**
 * Mybatis plus 元数据处理
 * 用于自动注入数据库常规字段，本处理器默认id不进行注入【数据表采用id自增】，后期会增加id属性注入【考虑雪花算法注入】
 *
 * @author penough
 * @date 2020/10/16
 */
@Slf4j
@NoArgsConstructor
public class PeMetaObjectHandler implements MetaObjectHandler {

    /**
     * 数据库常规注入字段
     */
    private final static String CREATE_TIME = "createTime";
    private final static String MODIFY_TIME = "modifyTime";
    private final static String CREATE_USER = "createUser";
    private final static String MODIFY_USER = "modifyUser";
    /**
     * getter方法前缀
     */
    private final static String GETTER_PREFIX = "get";

    @Override
    public void insertFill(MetaObject metaObject) {
        Object orginalObj = metaObject.getOriginalObject();
        if(orginalObj instanceof BaseEntity){
            setTimeField(metaObject, orginalObj, CREATE_TIME);
            setUserField(metaObject, orginalObj, CREATE_USER);
        }
        if(orginalObj instanceof SuperEntity){
            setTimeField(metaObject, orginalObj, MODIFY_TIME);
            setUserField(metaObject, orginalObj, MODIFY_USER);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object orginalObj = metaObject.getOriginalObject();
        if(orginalObj instanceof SuperEntity){
            setTimeField(metaObject, orginalObj, MODIFY_TIME);
            setUserField(metaObject, orginalObj, MODIFY_USER);
        }
    }

    /**
     * 设置时间属性
     * @param obj sql元对象
     * @param entity 实体
     * @param timeField 时间属性
     */
    private void setTimeField(MetaObject obj, Object entity, String timeField){
        Object time = reflectInvokeMethod(entity, GETTER_PREFIX + upperFirst(timeField), null);
        if(time == null){
            this.setFieldValByName(timeField, LocalDateTime.now(), obj);
        }
    }

    /**
     * 设置用户属性
     * @param obj
     * @param entity
     * @param userField
     */
    private void setUserField(MetaObject obj, Object entity, String userField){
        Object user = reflectInvokeMethod(entity, GETTER_PREFIX + upperFirst(userField), null);
        if(StringUtils.isBlank((String)user)){
            // 获取用户账号 —> 设置用户字段
            // 获取本地用户对象
            Object userObj = ThreadLocalUtil.getUser();
            if(userObj == null) return;
            // 反射获取用户编码getUserCode()
            String userCode = String.valueOf(reflectInvokeMethod(userObj, GETTER_PREFIX + upperFirst(Constants.USER_CODE_FIELD), null));
            this.setFieldValByName(userField, userCode, obj);
        }
    }



    /**
     * 反射调用方法
     * @param obj 对象
     * @param methodName 方法名
     * @param argTypes 参数类型
     * @param args 参数值
     * @return
     */
    private Object reflectInvokeMethod(Object obj, String methodName, Class[] argTypes, Object... args){
        Class cla = obj.getClass();
        Object res = null;
        try{
            Method method = cla.getMethod(methodName, argTypes);
            res = method.invoke(obj, args);
        }catch (NoSuchMethodException e){
            log.info("实体异常，未获取" + methodName + "方法");
        }catch (IllegalAccessException| InvocationTargetException e){
            e.printStackTrace();
            log.info("方法访问异常" + methodName);
        }
        return res;
    }

    /**
     * 大写首字母
     * @param src
     * @return
     */
    public static String upperFirst(String src){
        if (Character.isLowerCase(src.charAt(0))){
            return 1 == src.length()? src.toUpperCase():Character.toUpperCase(src.charAt(0)) + src.substring(1);
        }
        return src;
    }
}
