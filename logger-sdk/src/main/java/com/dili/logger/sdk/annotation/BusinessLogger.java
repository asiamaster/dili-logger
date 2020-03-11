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
     * @return
     */
    String businessType() default "";

    /**
     * 业务类型
     * @return
     */
    String operationType();

    /**
     * 日志内容，支持beetl模板
     * @return
     */
    String content() default "";

    /**
     * 系统编码
     * @return
     */
    String systemCode() default "";

    /**
     * 业务id
     * @return
     */
    long businessId() default -1;

    /**
     * 业务编码
     * @return
     */
    String businessCode() default "";

    /**
     * 备注说明
     * @return
     */
    String notes() default "";

}
