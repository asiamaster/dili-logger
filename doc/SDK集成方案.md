
# 1. 引用依赖
``` xml
<dependency>
    <groupId>com.dili</groupId>
    <artifactId>logger-sdk</artifactId>
    <version>${logger.version}</version>
</dependency>
```

# 2.引入权限spring bean
Spring Boot项目在启动类Application.java中添加权限注解扫描路径
```
@ComponentScan(basePackages={"com.dili.ss", "com.dili.logger.sdk"})
```
Spring 项目需要扫描`com.dili.logger.sdk`包


# 3. application.properties
```
logger.enable=true
#MQ配置，不发消息可以不配置
spring.rabbitmq.host=10.28.12.214
spring.rabbitmq.port=5672
spring.rabbitmq.username=admin
spring.rabbitmq.password=123456
spring.rabbitmq.virtual-host=/
# 开启发送确认
spring.rabbitmq.publisher-confirm-type=correlated
# 开启发送失败退回
spring.rabbitmq.publisher-returns=true
```

# 4. 注解使用
```
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    @BusinessLogger(businessType="test", content="业务类型编号:${businessCode}，业务id:${businessId},用户id:${operatorId}, 市场id:${marketId}，公司名:${name}。", operationType="edit", notes = "备注", systemCode = "UAP")
    public @ResponseBody BaseOutput update(Firm firm) {
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, "firm0001");
        LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, firm.getId());
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket != null) {
            LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
            LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
        }
        firmService.updateSelective(firm);
        return BaseOutput.success("修改成功");
    }
```
## 4.1 content参数
content为beetl模板，支持@RequestParam注解的变更，key是注解的name属性
没有注解的DTO对象(这里是Firm)或Map对象，支持直接使用字段名称为key
List对象如果没有@RequestParam注解，key为list

## 4.2 四个动态参数
业务编号、业务id、操作人id和市场id需要在代码中设置，例:
```
LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, "firm0001");
LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, firm.getId());
UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
if(userTicket != null) {
    LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
    LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
}
```