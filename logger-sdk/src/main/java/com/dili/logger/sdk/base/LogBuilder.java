package com.dili.logger.sdk.base;

import com.dili.logger.sdk.domain.BaseLog;

import java.lang.reflect.Method;

/**
 * 日志对象构建器
 */
public interface LogBuilder {

    /**
     * 在业务方法调用后，构建日志变量
     * @param method
     * @param args
     * @return  返回构建的日志对象，可以是BusinessLog或者ExceptionLog
     */
    BaseLog build(Method method, Object[] args);

}
