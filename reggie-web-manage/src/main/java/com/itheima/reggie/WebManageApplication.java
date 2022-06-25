package com.itheima.reggie;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication // 启动类
@MapperScan("com.itheima.reggie.mapper")// 扫描mybatis接口创建代理对象
@EnableTransactionManagement // 开启事务注解支持
@Slf4j
public class WebManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebManageApplication.class, args);
        log.info("项目启动成功");
    }
}