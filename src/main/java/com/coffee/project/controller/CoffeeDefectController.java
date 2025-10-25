package com.coffee.project.controller;

import com.coffee.project.common.Result;
import com.coffee.project.service.DetectionRecordService;
import com.coffee.project.vo.CoffeeBeanGradeInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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

    @Value("${file.detect-dir}")
    private String detectDirPath;
    /**
     * 缺陷检测接口
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
    public Result<CoffeeBeanGradeInfoVO> detectDefects(@RequestParam("file") MultipartFile file,
                                                    @RequestHeader("userId") Long userId) {
        // 1. 判断文件是否为空
        if (file == null || file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        // 2. 判断 userId 是否为空
        if (userId == null) {
            return Result.error("缺少用户ID");
        }

        try {

            // 获取项目根目录
            String projectDir = System.getProperty("user.dir");

            // 结合 detectDirPath，自动处理斜杠
            File detectDir = new File(projectDir, detectDirPath);

            // 如果 detectImages 目录不存在就创建
            if (!detectDir.exists()) {
                detectDir.mkdirs();
            }

            // 防止文件名重复
            String originalFileName = file.getOriginalFilename();
            String fileName = "user_" + System.currentTimeMillis() + "_" + originalFileName;
            File destFile = new File(detectDir, fileName);

            // 保存文件
            file.transferTo(destFile);


            //调用咖啡豆检测服务
            CoffeeBeanGradeInfoVO vo = detectionRecordService.detectAndSaveAndReturnDTO(destFile.getAbsolutePath(), userId);

            return Result.success(vo);

        } catch (Exception e) {
            // 7. 捕获异常，打印栈信息，并返回错误
            e.printStackTrace();
            return Result.error("检测失败：" + e.getMessage());

        }
    }
}
