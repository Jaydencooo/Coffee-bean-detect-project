package com.coffee.project.service.impl;

import com.coffee.project.domain.DetectionRecord;
import com.coffee.project.dto.DetectionResultDTO;
import com.coffee.project.mapper.DetectionRecordMapper;
import com.coffee.project.service.DetectionRecordService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DetectionRecordServiceImpl
 * 调用检测服务（DetectionServiceImpl） + 保存记录 + 返回前端
 * -
 * 功能：
 * 1. 调用 Python 脚本 infer.py 进行咖啡豆缺陷检测
 * 2. 解析检测结果 JSON
 * -
 * 3. 保存检测记录到数据库
 * 4. 将检测结果封装为 DTO 返回给前端
 */
@Service
public class DetectionRecordServiceImpl implements DetectionRecordService {

    @Autowired
    private DetectionRecordMapper detectionRecordMapper; // 数据库操作 Mapper

    private List<String> labels; // 标签列表，用于解析 output 数组
    @Value("${detection.labels-file}")
    private String labelsPath;
    /**
     * 初始化方法，加载标签文件
     * @PostConstruct 表示 Spring 容器初始化完毕后执行该方法
     */
    @PostConstruct
    public void init() {
        String projectDir = System.getProperty("user.dir");
        String fullLabelsPath = Paths.get(projectDir, labelsPath).toString();

        try {
            labels = Files.readAllLines(Paths.get(labelsPath)); // 读取所有标签到 List
        } catch (IOException e) {
            e.printStackTrace();
            labels = new ArrayList<>(); // 读取失败时使用空列表
        }
    }

    /**
     * 调用 Python 脚本进行咖啡豆缺陷检测
     *
     * @param imagePath 待检测图片路径
     * @return Python 脚本输出的 JSON 字符串
     * @throws IOException 读取脚本输出异常
     * @throws InterruptedException 脚本执行中断异常
     */
    private String runYoloDetection(String imagePath) throws IOException, InterruptedException {
        // Python 解释器路径（虚拟环境路径）
        String pythonInterpreterPath = "/Users/coconut/Desktop/咖啡豆项目/coffee-bean-detect/venv/bin/python3";
        // Python 推理脚本路径
        String inferScriptPath = "/Users/coconut/Desktop/咖啡豆项目/coffee-bean-detect/scripts/infer.py";

        // 构建命令：python3 infer.py imagePath
        ProcessBuilder pb = new ProcessBuilder(pythonInterpreterPath, inferScriptPath, imagePath);
        pb.redirectErrorStream(true); // 将标准错误流合并到标准输出流

        Process process = pb.start(); // 启动进程执行脚本

        // 读取 Python 脚本输出
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        }

        int exitCode = process.waitFor(); // 等待脚本执行完成
        if (exitCode != 0) {
            throw new RuntimeException("推理脚本执行失败，退出码：" + exitCode + "\n输出：" + output);
        }

        String rawOutput = output.toString();
        System.out.println("Python原始输出: " + rawOutput);

        // 提取 JSON 字符串
        int jsonIndex = rawOutput.indexOf("{");
        if (jsonIndex == -1) {
            throw new RuntimeException("咖啡豆缺陷检测失败: 未找到 JSON 内容\n原始输出: " + rawOutput);
        }

        String jsonStr = rawOutput.substring(jsonIndex);
        System.out.println("提取JSON: " + jsonStr);
        return jsonStr;
    }

    /**
     * 调用 Python 检测并保存检测记录到数据库
     *
     * @param imagePath 待检测图片路径
     * @param userId 用户 ID
     * @return 保存的 DetectionRecord 对象
     * @throws IOException 脚本输出读取异常
     * @throws InterruptedException 脚本执行中断异常
     */
    @Override
    public DetectionRecord detectAndSave(String imagePath, Long userId) throws IOException, InterruptedException {
        // 调用 Python 检测，获取 defectsJson
        String defectsJson = runYoloDetection(imagePath);

        // 构建检测记录对象
        DetectionRecord record = new DetectionRecord();
        record.setUserId(userId);
        record.setImagePath(imagePath);
        record.setDefectsJson(defectsJson);
        record.setCreatedAt(LocalDateTime.now());

        // 插入数据库
        detectionRecordMapper.insert(record);

        return record;
    }

    /**
     * 调用 detectAndSave 并直接返回 DTO 给前端
     *
     * @param imagePath 待检测图片路径
     * @param userId 用户 ID
     * @return DetectionResultDTO，封装前端需要的数据
     * @throws IOException 脚本输出解析异常
     * @throws InterruptedException 脚本执行中断异常
     */
    public DetectionResultDTO detectAndSaveAndReturnDTO(String imagePath, Long userId) throws IOException, InterruptedException {
        DetectionRecord record = detectAndSave(imagePath, userId);
        return parseOutputToDTO(record.getDefectsJson());
    }

    /**
     * 将 defectsJson 解析为 DetectionResultDTO
     *
     * @param defectsJson Python 脚本输出的 JSON 字符串
     * @return DetectionResultDTO 封装缺陷检测结果
     * @throws IOException JSON 解析异常
     */
    private DetectionResultDTO parseOutputToDTO(String defectsJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(defectsJson); // 解析 JSON

        DetectionResultDTO dto = new DetectionResultDTO();
        dto.setStatus("success"); // 状态固定为 success

        List<DetectionResultDTO.Defect> defectList = new ArrayList<>();

        // 尝试获取 "defects" 数组
        JsonNode defectsArray = root.get("defects");

        if (defectsArray != null && defectsArray.isArray() && defectsArray.size() > 0) {
            for (JsonNode defectNode : defectsArray) {
                DetectionResultDTO.Defect defect = new DetectionResultDTO.Defect();
                defect.setType(defectNode.get("type").asText());
                defect.setConfidence(defectNode.get("confidence").asDouble());

                defectList.add(defect);
            }
        } else {
            // 如果 "defects" 不存在，尝试解析 "output" 数组
            JsonNode outputArray = root.get("output");
            if (outputArray != null && outputArray.isArray()) {
                for (int i = 0; i < outputArray.size(); i++) {
                    double confidence = outputArray.get(i).asDouble();
                    // 根据阈值过滤缺陷
                    if (confidence > 0.5) {
                        DetectionResultDTO.Defect defect = new DetectionResultDTO.Defect();
                        defect.setType(labels != null && labels.size() > i ? labels.get(i) : "unknown");
                        defect.setConfidence(confidence);
                        defectList.add(defect);
                    }
                }
            } else {
                System.out.println("defects 数组和 output 数组都为空！");
            }
        }

        dto.setDefects(defectList);
        return dto;
    }

}
