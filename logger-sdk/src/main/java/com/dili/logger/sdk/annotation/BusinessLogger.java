package com.dili.logger.sdk.annotation;

import java.lang.annotation.*;

@Inherited
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessLogger {

    /**
     * 日志构建器
     * @return
     */
    String logBuilder() default "businessLogBuilder";

    /**
     * 业务类型
     * 优先从LoggerContext中获取，如未获取到，则再从此注解中获取
     * since 0.0.1
     * @return
     */
    String businessType() default "";

    /**
     * 操作类型编码
     * 优先从LoggerContext中获取，如未获取到，则再从此注解中获取
     * since 0.0.1
     * @return
     */
    String operationType() default "";

    /**
     * 日志内容，支持beetl模板
     * 优先从LoggerContext中获取，如未获取到，则再从此注解中获取
     * since 1.0.1
     * @return
     */
    String content() default "";

    /**
     * 系统编码
     * 优先从LoggerContext中获取，如未获取到，则再从此注解中获取
     * since 0.0.1
     * @return
     */
    String systemCode() default "";

    /**
     * 业务id
     * 已弃用，数据将从LoggerContext中获取
     * since 1.0.1
     * @return
     */
    @Deprecated
    long businessId() default -1;

    /**
     * 业务编码
     * 已弃用，数据将从LoggerContext中获取
     * since 1.0.1
     * @return
     */
    @Deprecated
    String businessCode() default "";

    /**
     * 备注说明
     * 优先从LoggerContext中获取，如未获取到，则再从此注解中获取
     * since 0.0.1
     * @return
     */
    String notes() default "";

    /**
     * 远程客户端IP
     * 已弃用，数据将优先从LoggerContext中获取，如未获取到，再自动获取
     * since 1.0.1
     * @return
     */
    @Deprecated
    String remoteIp() default "";

}
