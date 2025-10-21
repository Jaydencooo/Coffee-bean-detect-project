package com.coffee.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * WebMvcConfig 类用于自定义 Spring MVC 的配置
 *
 * 主要功能：
 * 1. 配置静态资源访问路径映射
 *    - 让浏览器可以访问本地文件或项目中的静态资源
 * 2. 配置上传文件访问路径
 *    - 将本地上传文件夹映射为 URL，使前端能够直接访问
 *
 * 使用场景：
 * - 当你有上传文件（头像、图片等）保存在本地时
 * - 前端通过 URL 访问这些文件
 * - 配合 Spring Boot 默认静态资源目录使用（static、public）
 */
@Configuration // 声明这是一个 Spring 配置类，Spring 启动时会加载它
public class WebMvcConfig implements WebMvcConfigurer { // 实现 WebMvcConfigurer 接口来自定义 MVC 配置

    // 从 application.yml 或 application.properties 中读取上传目录路径
    // 例如：file.upload-dir=uploads
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 配置资源映射
     * ResourceHandlerRegistry 用来注册 URL 到文件系统或 classpath 的映射
     * 这样浏览器访问 URL 时，就能访问到对应的本地文件
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // ----------------------
        // 1️⃣ 获取项目根目录
        // ----------------------
        Path basePath = Paths.get(System.getProperty("user.dir"));
        // user.dir 是当前项目运行目录，例如：
        // /Users/coconut/Desktop/咖啡豆项目/coffee-bean-detect

        // ----------------------
        // 2️⃣ 获取上传目录路径
        // ----------------------
        Path fullUploadPath = Paths.get(uploadDir); // 读取配置的相对路径或绝对路径
        if (!fullUploadPath.isAbsolute()) {
            // 如果是相对路径，就相对于项目根目录拼接
            fullUploadPath = basePath.resolve(fullUploadPath);
        }

        // ----------------------
        // 3️⃣ 注册 URL 映射
        // ----------------------
        registry.addResourceHandler("/uploads/**") // 访问 URL 模式，例如：http://localhost:8080/uploads/xxx.jpg
                .addResourceLocations("file:" + fullUploadPath.toAbsolutePath() + "/");
        // file: 表示访问的是本地文件系统
        // fullUploadPath.toAbsolutePath() 获取文件夹绝对路径，例如：
        // /Users/coconut/Desktop/咖啡豆项目/coffee-bean-detect/uploads/

        // ----------------------
        // 4️⃣ 控制台打印映射路径，方便调试
        // ----------------------
        System.out.println("上传文件映射路径：" + fullUploadPath.toAbsolutePath());
    }
}
