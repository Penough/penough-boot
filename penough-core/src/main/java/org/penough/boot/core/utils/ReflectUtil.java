package org.penough.boot.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectUtil {

    /**
     * 反射调用方法
     * @param obj 对象
     * @param methodName 方法名
     * @param argTypes 参数类型
     * @param args 参数值
     * @return
     */
    public static Object reflectInvokeMethod(Object obj, String methodName, Class[] argTypes, Object... args){
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
}
