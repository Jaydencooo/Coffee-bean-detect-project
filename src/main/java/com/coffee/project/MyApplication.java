package com.coffee.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 启动类
 *
 * 这是项目的入口类，用于启动整个 Spring Boot 应用
 *
 * @SpringBootApplication 是一个组合注解，包含：
 *  - @Configuration：表示这是一个配置类
 *  - @EnableAutoConfiguration：启用 Spring Boot 自动配置机制
 *  - @ComponentScan：开启组件扫描，自动发现并注册 Spring 组件
 *
 * @MapperScan("com.coffee.project.mapper") 告诉 MyBatis 去哪个包扫描 Mapper 接口，
 * 这样 Spring 容器会自动生成 Mapper 的代理对象，方便注入使用
 */
@SpringBootApplication
@MapperScan("com.coffee.project.mapper")
public class MyApplication {

    /**
     * main 方法是 Java 应用的入口，执行该方法启动 Spring Boot 应用
     * SpringApplication.run 会启动内嵌的 Tomcat 容器并加载 Spring 容器
     *
     * @param args 启动参数，一般从命令行传入
     */
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
