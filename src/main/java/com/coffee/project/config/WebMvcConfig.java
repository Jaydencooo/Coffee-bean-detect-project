package com.coffee.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * WebMvcConfig 类用于配置 Spring MVC 的一些行为
 * 这里主要是配置静态资源访问路径映射
 */
@Configuration  // 表示这是一个配置类，Spring 容器会自动加载它
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")  // 从 application.yml 中读取上传目录
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. 保留默认的 static、public、META-INF/resources 目录映射
        registry.addResourceHandler("/**")
                .addResourceLocations(
                        "classpath:/static/",
                        "classpath:/public/",
                        "classpath:/META-INF/resources/"
                );

        // 2. 处理上传文件目录
        Path basePath = Paths.get(System.getProperty("user.dir")); // 项目运行根目录
        Path fullUploadPath = Paths.get(uploadDir);
        if (!fullUploadPath.isAbsolute()) {
            fullUploadPath = basePath.resolve(fullUploadPath);
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + fullUploadPath.toAbsolutePath() + "/");

        System.out.println("静态资源映射已启用，映射目录：" + fullUploadPath.toAbsolutePath());
    }

}
