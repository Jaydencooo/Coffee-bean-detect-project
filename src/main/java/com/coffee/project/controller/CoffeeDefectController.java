package com.coffee.project.controller;

import com.coffee.project.common.Result;
import com.coffee.project.dto.DetectionResultDTO;
import com.coffee.project.service.DetectionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * CoffeeDefectController
 *
 * 咖啡豆缺陷检测控制器
 * 1. 提供文件上传接口
 * 2. 提供缺陷检测接口
 * 3. 返回统一 Result 封装的 JSON 数据
 */
@RestController // 表示该类是一个 REST 风格的控制器，方法返回 JSON
@RequestMapping("/coffee") // 所有接口前缀为 /coffee
public class CoffeeDefectController {

    // 自动注入检测记录服务，调用检测逻辑和数据库操作
    @Autowired
    private DetectionRecordService detectionRecordService;

    /**
     * 文件上传接口
     *
     * 1. 接收前端上传的 MultipartFile 文件
     * 2. 判断文件是否为空
     * 3. 生成唯一文件名，避免覆盖
     * 4. 保存文件到服务器 /uploads/ 目录
     * 5. 返回文件路径给前端
     *
     * @param file 前端上传的文件
     * @return Result<String> 返回上传文件路径或错误信息
     */

    @Value("${file.upload-dir}")
    private String uploadDir; // 从 application.yml 注入

    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传文件为空");
        }

        try {
            // 生成唯一文件名
            String original = file.getOriginalFilename();
            String ext = "";
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + ext;

            // 计算保存目录：若 uploadDir 是相对路径，则相对于运行目录(System.getProperty("user.dir"))
            Path base = Paths.get(System.getProperty("user.dir"));
            Path targetDir = Paths.get(uploadDir);
            if (!targetDir.isAbsolute()) {
                targetDir = base.resolve(targetDir);
            }

            // 创建目录（若不存在）
            Files.createDirectories(targetDir);

            // 保存文件
            Path targetFile = targetDir.resolve(fileName);
            file.transferTo(targetFile.toFile());

            // 构建前端可访问的 URL （自动包含 context-path）
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(fileName)
                    .toUriString();

            return Result.success(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 缺陷检测接口
     *
     * 1. 接收前端上传的图片
     * 2. 从请求头获取用户 ID
     * 3. 将上传的 MultipartFile 保存为临时文件
     * 4. 调用 DetectionRecordService.detectAndSaveAndReturnDTO 进行检测
     * 5. 返回 DetectionResultDTO 给前端
     *
     * @param file   上传的图片文件
     * @param userId 用户 ID（请求头传入，用于关联检测记录）
     * @return Result<DetectionResultDTO> 返回检测结果或错误信息
     */
    @PostMapping("/detect")
    public Result<DetectionResultDTO> detectDefects(@RequestParam("file") MultipartFile file,
                                                    @RequestHeader("userId") Long userId) {
        // 1. 判断文件是否为空
        if (file == null || file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        // 2. 判断 userId 是否为空
        if (userId == null) {
            return Result.error("缺少用户ID");
        }

        File tempFile = null;
        try {
            // 3. 创建临时文件用于存储上传的图片
            tempFile = File.createTempFile("upload-", ".jpg");

            // 4. 将 MultipartFile 写入临时文件
            file.transferTo(tempFile);

            // 5. 调用服务方法进行检测，并直接返回 DTO
            DetectionResultDTO dto = detectionRecordService.detectAndSaveAndReturnDTO(
                    tempFile.getAbsolutePath(), userId
            );

            // 6. 返回成功结果
            return Result.success(dto);

        } catch (Exception e) {
            // 7. 捕获异常，打印栈信息，并返回错误
            e.printStackTrace();
            return Result.error("检测失败：" + e.getMessage());

        } finally {
            // 8. 删除临时文件，避免占用磁盘空间
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
