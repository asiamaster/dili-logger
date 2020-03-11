package com.dili.logger.sdk.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 日志上下文
 * @author asiamaster
 */
public class LoggerContext {

    private static ThreadLocal<Map<String, Object>> local = new ThreadLocal<Map<String, Object>>();

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) get("req");
    }

    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) get("resp");
    }


    public static void resetLocal(){
        local.remove();
    }


    public static void put(HttpServletRequest request) {
        put("req", request);
    }

    public static void put(HttpServletResponse response) {
        put("resp", response);
    }

    public static void put(String key, Object obj) {
        Map<String, Object> map = local.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            local.set(map);
        }
        map.put(key, obj);
    }

    public static Object get(String key) {
        Map<String, Object> map = local.get();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    public static void clean() {
        local.set(null);
    }


}
