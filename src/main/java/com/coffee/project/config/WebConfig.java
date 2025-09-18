package com.coffee.project.config;

import com.coffee.project.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置类
 * 实现 WebMvcConfigurer 接口用于自定义Spring MVC的配置
 * 通过 @Configuration 注解标识该类为配置类，Spring Boot启动时会自动加载该类中的配置
 */
@Configuration // 标识该类为配置类，Spring会将其中的配置方法应用于整个应用上下文
public class WebConfig implements WebMvcConfigurer {

    // 自动注入登录检查拦截器实例
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    /**
     * 添加拦截器配置
     * 重写 WebMvcConfigurer 接口的 addInterceptors 方法
     * @param registry 拦截器注册器，用于注册和管理拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                // 只拦截 API 请求
                //不用加上/api
                .addPathPatterns("/**")
                // 排除登录、注册和静态资源
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/user/reset-password",
                        "/error"
                );
    }


}