package com.coffee.project.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUploadUtils {

    // 静态变量，启动时读取系统属性或默认路径
    private static final String UPLOAD_DIR;

    static {
        // 尝试从系统属性读取，如果没有则用默认 uploads
        UPLOAD_DIR = System.getProperty("file.upload-dir", "uploads");
    }

    /**
     * 上传文件
     * @param file MultipartFile
     * @return 可访问 URL，例如 /uploads/xxx.jpg
     */
    public static String upload(MultipartFile file) throws IOException {
        // 创建目录
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        // 生成新文件名
        String newFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // 保存文件
        File dest = new File(dir, newFileName);
        file.transferTo(dest);

        // 返回 URL（相对于服务器根路径）
        return "/" + UPLOAD_DIR + "/" + newFileName;
    }
}
