package com.coffee.project.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传工具类
 * 可从 application.yml 的 file.upload-dir 配置上传目录
 */
public class FileUploadUtils {

    // 默认上传目录，如果 yml 没配置则使用 uploads
    private static final String UPLOAD_DIR = System.getProperty("file.upload-dir", "uploads");

    /**
     * 上传文件
     * @param file 前端传来的 MultipartFile
     * @return 返回可访问 URL，比如 /uploads/xxxx.jpg
     */
    public static String upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("上传文件为空");
        }

        // 获取项目根目录
        File projectDir = new File(System.getProperty("user.dir"));

        // 构建上传目录
        File uploadDir = new File(projectDir, UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // 自动创建目录
        }

        // 构建新文件名
        String originalFilename = file.getOriginalFilename();
        String newFileName = System.currentTimeMillis() + "_" + originalFilename;

        // 保存文件
        File dest = new File(uploadDir, newFileName);
        file.transferTo(dest);

        // 返回前端可访问 URL
        return "/" + UPLOAD_DIR + "/" + newFileName;
    }
}
