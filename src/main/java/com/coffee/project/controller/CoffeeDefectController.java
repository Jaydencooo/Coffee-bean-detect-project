package com.coffee.project.controller;

import com.coffee.project.common.Result;
import com.coffee.project.service.DetectionRecordService;
import com.coffee.project.vo.CoffeeBeanGradeInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/coffee")
public class CoffeeDefectController {

    @Autowired
    private DetectionRecordService detectionRecordService;

    // yml 配置中的相对路径
    @Value("${file.detect-dir}")
    private String detectDirPath;


    @PostMapping("/detect")
    public Result<CoffeeBeanGradeInfoVO> detectDefects(@RequestParam("file") MultipartFile file,
                                                       @RequestHeader("userId") Long userId) {
        if (file == null || file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        if (userId == null) {
            return Result.error("缺少用户ID");
        }

        try {
            // 获取项目根目录
            String projectDir = System.getProperty("user.dir");

            // 构建保存目录（相对路径）
            File detectDir = new File(projectDir, detectDirPath);
            if (!detectDir.exists()) {
                detectDir.mkdirs();
            }

            // 文件名
            String originalFileName = file.getOriginalFilename();
            String fileName = "user_" + userId + "_" + System.currentTimeMillis() + "_" + originalFileName;

            // 保存文件
            File destFile = new File(detectDir, fileName);
            file.transferTo(destFile);

            // 构造相对路径（全程使用）
            String relativePath = detectDirPath + "/" + fileName;

            // 调用检测逻辑 —— 仅传相对路径
            CoffeeBeanGradeInfoVO vo = detectionRecordService.detectAndSaveAndReturnDTO(relativePath, userId);

            return Result.success(vo);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("检测失败：" + e.getMessage());
        }
    }
}
