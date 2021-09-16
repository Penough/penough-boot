package org.penough.boot.core.utils;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.penough.boot.core.constants.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 本地线程存储工具
 *
 * @author Penough
 * @date 2020/11/05
 */

public class ThreadLocalUtil {
    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal();

    public static void set(String key, Object value){
        Map<String, Object> map = getLocalMap();
        map.put(key, value);
    }

    public static <T> T get(String key, Class<T> type){
        Map<String, Object> map = getLocalMap();
        return Convert.convert(type, map.get(key));
    }

    public static Map<String, Object> getLocalMap(){
        Map<String, Object> map = THREAD_LOCAL.get();
        if(map == null){
            map = new LinkedHashMap<>(10);
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setUser(Object user){
        set(Constants.LOCAL_USER_KEY, user);
    }

    public static Object getUser(){
        Map<String, Object> map = getLocalMap();
        return map.get(Constants.LOCAL_USER_KEY);
    }
}
