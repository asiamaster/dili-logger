package com.dili.logger;

import com.dili.ss.retrofitful.annotation.RestfulScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author yuehongbo
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.dili.logger.mapper", "com.dili.ss.dao"})
@ComponentScan(basePackages = {"com.dili.*"})
@RestfulScan({"com.dili.uap.sdk.rpc"})
@EnableFeignClients(basePackages = {"com.dili.*"})
public class LoggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoggerApplication.class, args);
    }

}
