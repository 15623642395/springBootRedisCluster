package com;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class RedisClusterApplication {

    public static void main(String[] args) {
        //1、实例化SpringApplication
        SpringApplication application = new SpringApplication(RedisClusterApplication.class);

        //2、启动程序
        application.run(args);

        log.info("启动成功");
    }
}
