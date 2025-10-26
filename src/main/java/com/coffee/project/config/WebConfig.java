package com.coffee.project.config;

import com.coffee.project.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/user/reset-password",
                        "/error",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.jpeg",
                        "/**/*.gif",
                        "/uploads/**",
                        "/coze/**",
                        "/coze.html",
                        "/sample/**"
                );
    }

    /**
     * 添加静态资源映射
     * 映射 /detectImages/** URL 到项目根目录 detectImages 文件夹
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String detectImagesPath = System.getProperty("user.dir") + "/detectImages/";
        registry.addResourceHandler("/detectImages/**")
                .addResourceLocations("file:" + detectImagesPath);
    }
}

