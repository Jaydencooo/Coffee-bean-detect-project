package com.coffee.project.service.impl;

import com.coffee.project.service.DetectionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;

/**
 * DetectionServiceImpl
 * 负责调用 Python 脚本进行咖啡豆缺陷检测
 * 使用配置文件动态获取 Python 路径和脚本路径，方便部署到不同电脑
 */
@Service
public class DetectionServiceImpl implements DetectionService {

    // 从配置文件读取 Python 解释器路径
    @Value("${detection.python-exe}")
    private String pythonExe;

    // 从配置文件读取 Python 推理脚本路径
    @Value("${detection.infer-script}")
    private String scriptPath;

    @Override
    public String runInference(String imagePath) throws Exception {

        // 获取项目根目录，确保脚本路径可以在不同电脑运行
        String projectDir = System.getProperty("user.dir");
        String fullScriptPath = Paths.get(projectDir, scriptPath).toString();

        // 构建执行命令
        ProcessBuilder pb = new ProcessBuilder(pythonExe, scriptPath, imagePath);
        pb.redirectErrorStream(true); // 将错误流合并到输出流

        // 启动进程执行 Python 脚本
        Process process = pb.start();

        // 读取输出
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }

        // 等待脚本执行完成
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Python脚本执行失败，退出码：" + exitCode + "\n输出: " + result);
        }

        return result.toString();
    }
}
