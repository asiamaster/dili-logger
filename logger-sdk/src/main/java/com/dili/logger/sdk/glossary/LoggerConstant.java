package com.dili.logger.sdk.glossary;

/**
 * 日志常量
 */
public interface LoggerConstant {

    /**
     * 业务类型
     */
    String LOG_BUSINESS_TYPE = "businessType";

    /**
     * 日志备注信息
     */
    String LOG_NOTES_KEY = "notes";

    /**
     * 日志内容
     */
    String LOG_CONTENT_KEY = "content";

    /**
     * 日志所属系统信息
     */
    String LOG_SYSTEM_CODE_KEY = "systemCode";

    /**
     * 日志操作人 key
     */
    String LOG_OPERATOR_ID_KEY = "operatorId";
    /**
     * 日志操作人名称 key
     */
    String LOG_OPERATOR_NAME_KEY = "operatorName";
    /**
     * 日志市场 key
     */
    String LOG_MARKET_ID_KEY = "marketId";
    /**
     * 日志业务编码 key
     */
    String LOG_BUSINESS_CODE_KEY = "businessCode";
    /**
     * 日志业务id key
     */
    String LOG_BUSINESS_ID_KEY = "businessId";
    /**
     * 业务日志操作类型 key
     */
    String LOG_OPERATION_TYPE_KEY = "operationType";
    /**
     * 异常类型 key
     */
    String LOG_EXCEPTION_TYPE_KEY = "exceptionType";

    /**
     * 异常客户端ip key
     */
    String LOG_REMOTE_IP_KEY = "remoteIp";

    //=================MQ 相关配置信息 ============//
    /**
     * MQ 交换机配置
     */
    String MQ_LOGGER_TOPIC_EXCHANGE = "dili.logger.topicExchange";
    /**
     * MQ 异常日志Queue
     */
    String MQ_LOGGER_ADD_EXCEPTION_QUEUE = "dili.logger.addExceptionQueue";
    /**
     * MQ 业务日志Key
     */
    String MQ_LOGGER_ADD_EXCEPTION_KEY = "dili.logger.addExceptionKey";
    /**
     * MQ 业务日志Queue
     */
    String MQ_LOGGER_ADD_BUSINESS_QUEUE = "dili.logger.addBusinessQueue";
    /**
     * MQ 业务日志Key
     */
    String MQ_LOGGER_ADD_BUSINESS_KEY = "dili.logger.addBusinessKey";
}
