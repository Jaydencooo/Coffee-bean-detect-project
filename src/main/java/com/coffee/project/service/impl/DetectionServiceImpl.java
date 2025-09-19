package com.coffee.project.service.impl;

import com.coffee.project.service.DetectionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;

/**
 * DetectionServiceImpl
 *
 * <p>功能：
 * <ul>
 *     <li>调用 Python 脚本进行咖啡豆缺陷检测</li>
 *     <li>支持从配置文件动态读取 Python 解释器路径和推理脚本路径，便于跨环境部署</li>
 *     <li>处理脚本输出并返回检测结果</li>
 * </ul>
 *
 * <p>执行流程：
 * <ol>
 *     <li>获取项目根目录，拼接脚本绝对路径</li>
 *     <li>构建 Python 执行命令</li>
 *     <li>启动进程运行脚本，并合并标准输出与错误输出</li>
 *     <li>读取脚本输出，等待脚本执行完成</li>
 *     <li>根据退出码判断执行是否成功，失败时抛出异常</li>
 * </ol>
 *
 * <p>异常处理：
 * <ul>
 *     <li>脚本执行失败（退出码非 0）抛出 RuntimeException，包含脚本输出信息</li>
 *     <li>IO 或进程异常由方法抛出，调用方需捕获</li>
 * </ul>
 *
 * <p>安全与部署注意事项：
 * <ul>
 *     <li>Python 脚本路径和解释器路径需在配置文件中正确配置</li>
 *     <li>确保部署环境安装所需 Python 版本及依赖库</li>
 * </ul>
 *
 * @author
 * @version 1.0
 * @since 2025-09-18
 */
@Service
public class DetectionServiceImpl implements DetectionService {

    /** Python 解释器路径，从配置文件读取 */
    @Value("${detection.python-exe}")
    private String pythonExe;

    /** Python 推理脚本路径，从配置文件读取 */
    @Value("${detection.infer-script}")
    private String scriptPath;

    /**
     * 调用 Python 脚本进行咖啡豆缺陷检测
     *
     * <p>执行脚本并返回结果字符串（通常为 JSON 或分类信息）
     *
     * @param imagePath 待检测图片的绝对或相对路径
     * @return String 脚本输出结果，通常包含检测类别或概率
     * @throws Exception 如果脚本执行失败或 IO 错误，将抛出异常
     */
    @Override
    public String runInference(String imagePath) throws Exception {

        // 获取项目根目录，确保脚本路径可跨环境使用
        String projectDir = System.getProperty("user.dir");
        String fullScriptPath = Paths.get(projectDir, scriptPath).toString();

        // 构建执行命令
        ProcessBuilder pb = new ProcessBuilder(pythonExe, scriptPath, imagePath);
        pb.redirectErrorStream(true); // 将错误流合并到输出流

        // 启动进程执行 Python 脚本
        Process process = pb.start();

        // 读取脚本输出
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
