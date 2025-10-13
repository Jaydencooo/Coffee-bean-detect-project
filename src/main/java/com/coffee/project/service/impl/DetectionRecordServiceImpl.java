package com.coffee.project.service.impl;

import com.coffee.project.domain.DetectionRecord;
import com.coffee.project.dto.DetectionResultDTO;
import com.coffee.project.mapper.DetectionRecordMapper;
import com.coffee.project.service.DetectionHistoryService;
import com.coffee.project.service.DetectionRecordService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

/**
 * DetectionRecordServiceImpl
 *
 * <p>功能：
 * <ul>
 *     <li>调用 Python 脚本进行咖啡豆缺陷检测</li>
 *     <li>解析 Python 脚本输出的 JSON 结果</li>
 *     <li>保存检测记录到数据库</li>
 *     <li>将检测结果封装为 DTO 返回给前端</li>
 * </ul>
 *
 * <p>执行流程：
 * <ol>
 *     <li>初始化加载缺陷标签文件（labels.txt），在 Spring 容器初始化完成后执行 {@link #init()}</li>
 *     <li>调用 Python 推理脚本（Yolo 模型）执行缺陷检测，获取原始 JSON 输出</li>
 *     <li>将检测结果保存为 {@link DetectionRecord} 并插入数据库</li>
 *     <li>将缺陷 JSON 解析为 {@link DetectionResultDTO} 并返回前端</li>
 * </ol>
 *
 * <p>异常处理：
 * <ul>
 *     <li>Python 脚本执行失败或退出码非 0 抛出 RuntimeException</li>
 *     <li>JSON 解析异常抛出 IOException</li>
 *     <li>进程执行中断抛出 InterruptedException</li>
 * </ul>
 *
 * <p>注意事项：
 * <ul>
 *     <li>确保 Python 环境和依赖库已正确安装</li>
 *     <li>labels 文件路径和脚本路径需根据部署环境配置</li>
 *     <li>缺陷阈值可根据需求调整（当前为 confidence > 0.5）</li>
 * </ul>
 *
 * @author
 * @version 1.0
 * @since 2025-09-18
 */
@Service
@Slf4j
public class DetectionRecordServiceImpl implements DetectionRecordService {

    @Autowired
    private DetectionRecordMapper detectionRecordMapper;

    @Autowired
    private DetectionHistoryService detectionHistoryService;

    /** 标签列表，用于解析 output 数组 */
    private List<String> labels;

    /** 标签文件路径，从配置文件读取 */
    @Value("${detection.labels-file}")
    private String labelsPath;

    @Value("${detection.pythonInterpreterPath}")
    private String pythonInterpreterPath;

    @Value("${detection.inferScriptPath}")
    private String inferScriptPath;
    /**
     * 初始化方法，加载标签文件
     *
     * <p>在 Spring 容器初始化完成后执行，读取 labels 文件到 {@link #labels} 列表。
     * 如果读取失败，使用空列表以避免空指针异常。
     */
    @PostConstruct
    public void init() {
        String projectDir = System.getProperty("user.dir");
        String fullLabelsPath = Paths.get(projectDir, labelsPath).toString();

        try {
            labels = Files.readAllLines(Paths.get(labelsPath));
        } catch (IOException e) {
            e.printStackTrace();
            labels = new ArrayList<>();
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


        ProcessBuilder pb = new ProcessBuilder(pythonInterpreterPath, inferScriptPath, imagePath);
        pb.redirectErrorStream(true);

        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("推理脚本执行失败，退出码：" + exitCode + "\n输出：" + output);
        }

        String rawOutput = output.toString();
        System.out.println("Python原始输出: " + rawOutput);

        int jsonIndex = rawOutput.indexOf("{");
        if (jsonIndex == -1) {
            throw new RuntimeException("咖啡豆缺陷检测失败: 未找到 JSON 内容\n原始输出: " + rawOutput);
        }

        return rawOutput.substring(jsonIndex);
    }

    /**
     * 调用 Python 检测并保存检测记录到数据库
     *
     * @param imagePath 待检测图片路径
     * @param userId 用户 ID
     * @return {@link DetectionRecord} 保存的检测记录对象
     * @throws IOException 脚本输出读取异常
     * @throws InterruptedException 脚本执行中断异常
     */
    @Override
    public DetectionRecord detectAndSave(String imagePath, Long userId) throws IOException, InterruptedException {
        // 调用 Python 脚本获取 defectsJson
        String defectsJson = runYoloDetection(imagePath);

        // 解析 JSON 为 DTO
        DetectionResultDTO dto = parseOutputToDTO(defectsJson);

        // 提取缺陷名字，逗号分隔
        String defectsName = extractDefectsName(dto);

        // 构建 DetectionRecord 实体
        DetectionRecord record = new DetectionRecord();
        record.setUserId(userId);
        record.setImagePath(imagePath);
        record.setDefectsJson(defectsJson);
        record.setDefectsName(defectsName); // 新增字段
        record.setCreatedAt(LocalDateTime.now());

        // 保存到数据库
        detectionRecordMapper.insert(record);
        try {
            detectionHistoryService.addHistory(record.getUserId(), record.getDefectsName(), record.getImagePath());
        } catch (Exception e) {
            log.error("保存检测历史失败", e);
        }
        return record;
    }

    /**
     * 从 DTO 中提取缺陷名字，逗号分隔
     */
    private String extractDefectsName(DetectionResultDTO dto) {
        List<DetectionResultDTO.Defect> defects = dto.getDefects();
        if (defects == null || defects.isEmpty()) return "";
        return defects.stream()
                .map(DetectionResultDTO.Defect::getType)
                .collect(Collectors.joining(", "));

    }


    /**
     * 调用 detectAndSave 并直接返回 DTO 给前端
     *
     * @param imagePath 待检测图片路径
     * @param userId 用户 ID
     * @return {@link DetectionResultDTO} 封装前端需要的缺陷检测结果
     * @throws IOException 脚本输出解析异常
     * @throws InterruptedException 脚本执行中断异常
     */
    public DetectionResultDTO detectAndSaveAndReturnDTO(String imagePath, Long userId) throws IOException, InterruptedException {
        DetectionRecord record = detectAndSave(imagePath, userId);
        return parseOutputToDTO(record.getDefectsJson());
    }

    /**
     * 将 defectsJson 解析为 {@link DetectionResultDTO}
     *
     * @param defectsJson Python 脚本输出的 JSON 字符串
     * @return {@link DetectionResultDTO} 封装缺陷检测结果
     * @throws IOException JSON 解析异常
     */
    private DetectionResultDTO parseOutputToDTO(String defectsJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(defectsJson);

        DetectionResultDTO dto = new DetectionResultDTO();
        dto.setStatus("success");

        List<DetectionResultDTO.Defect> defectList = new ArrayList<>();
        JsonNode defectsArray = root.get("defects");

        if (defectsArray != null && defectsArray.isArray() && defectsArray.size() > 0) {
            for (JsonNode defectNode : defectsArray) {
                DetectionResultDTO.Defect defect = new DetectionResultDTO.Defect();
                defect.setType(defectNode.get("type").asText());
                defect.setConfidence(defectNode.get("confidence").asDouble());
                defectList.add(defect);
            }
        } else {
            JsonNode outputArray = root.get("output");
            if (outputArray != null && outputArray.isArray()) {
                for (int i = 0; i < outputArray.size(); i++) {
                    double confidence = outputArray.get(i).asDouble();
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
