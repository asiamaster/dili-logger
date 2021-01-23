package com.dili.logger.enums;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/23 14:58
 */
public enum LoggerClassify {
    /**
     * 业务日志
     */
    BUSINESS(1, "业务日志"),
    /**
     * 异常日志
     */
    EXCEPTION(2, "异常日志"),
    ;
    @Getter
    private Integer code;
    @Getter
    private String value;

    LoggerClassify(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
    /**
     * 初始化map
     */
    private static Map<Integer, LoggerClassify> initMaps = Maps.newHashMap();

    static {
        for (LoggerClassify as : LoggerClassify.values()) {
            initMaps.put(as.getCode(), as);
        }
    }

    /**
     * 获取某个枚举值实例信息
     *
     * @param code
     * @return
     */
    public static LoggerClassify getInstance(Integer code) {
        return initMaps.getOrDefault(code,null);
    }

    /**
     * 对比枚举值是否相等
     * @param code
     * @return
     */
    public Boolean equalsToCode(Integer code) {
        return this.getCode().equals(code);
    }

}
