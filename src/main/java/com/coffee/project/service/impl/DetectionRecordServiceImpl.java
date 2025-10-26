package com.coffee.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coffee.project.domain.CoffeeBeanGradeInfo;
import com.coffee.project.domain.DetectionRecord;
import com.coffee.project.dto.DetectionResultDTO;
import com.coffee.project.mapper.CoffeeBeanGradeInfoMapper;
import com.coffee.project.mapper.DetectionRecordMapper;
import com.coffee.project.service.DetectionHistoryService;
import com.coffee.project.service.DetectionRecordService;
import com.coffee.project.vo.CoffeeBeanGradeInfoVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

@Service
@Slf4j
public class DetectionRecordServiceImpl implements DetectionRecordService {

    @Autowired
    private DetectionRecordMapper detectionRecordMapper;


    @Autowired
    private CoffeeBeanGradeInfoMapper coffeeBeanGradeInfoMapper;

    @Autowired
    private DetectionHistoryService detectionHistoryService;

    private List<String> labels = new ArrayList<>();  //存储 YOLO 模型识别的类别标签。

    @Value("${detection.labels-file}")
    private String labelsPath;  //标签文本文件

    @Value("${detection.pythonInterpreterPath}")
    private String pythonInterpreterPath;  //Python 解释器路径

    @Value("${detection.inferScriptPath}")
    private String inferScriptPath;  //YOLO 推理脚本路径

    /** 初始化加载 labels 文件
     * 功能：从文件读取 YOLO 分类标签到 labels 列表
     * 作用：保证在调用检测方法前，labels 已经加载完成
     * */
    @PostConstruct //用来标记一个方法 在依赖注入完成后、Spring 容器初始化 Bean 之后立即执行。它的主要作用是做 初始化工作。
    public void init() {
        try {
            labels = Files.readAllLines(Paths.get(labelsPath));
            log.info("缺陷标签加载完成: {}", labels);
        } catch (IOException e) {
            log.error("加载 labels 文件失败: {}", labelsPath, e);
        }
    }

    /** 调用 Python YOLO 脚本并返回原始 JSON */
    private String runYoloDetection(String imagePath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(pythonInterpreterPath, inferScriptPath, imagePath);
        pb.redirectErrorStream(true);

        Process process = pb.start();
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n"); // 加换行符便于调试
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("推理脚本执行失败，退出码：" + exitCode + "\n输出：" + output);
        }

        String raw = output.toString();
        int jsonIndex = raw.indexOf("{");
        if (jsonIndex == -1) {
            throw new RuntimeException("未找到 JSON 内容，原始输出: " + raw);
        }

        return raw.substring(jsonIndex);
    }

    /**
     * 检测图片、保存记录并返回 VO
     */
    @Override
    public CoffeeBeanGradeInfoVO detectAndSaveAndReturnDTO(String imagePath, Long userId) throws IOException, InterruptedException {
        // 1. 调用 Python 检测
        String defectsJson = runYoloDetection(imagePath);

        // 2. 解析 JSON 并生成缺陷列表
        DetectionResultDTO dto = parseJsonToDTO(defectsJson);

        // 3. 提取缺陷名字
        String defectsName = dto.getDefects().stream()
                .map(DetectionResultDTO.Defect::getType)
                .collect(Collectors.joining(", "));


        // 4. 保存到数据库
        DetectionRecord record = new DetectionRecord();
        record.setUserId(userId);
        record.setImagePath(imagePath);
        record.setDefectsJson(defectsJson);
        record.setDefectsName(defectsName);
        record.setCreatedAt(LocalDateTime.now());
        detectionRecordMapper.insert(record);

        // 5. 保存历史
        try {
            detectionHistoryService.addHistory(userId, defectsName, imagePath);
        } catch (Exception e) {
            log.error("保存检测历史失败", e);
        }

        //根据咖啡豆检测历史的咖啡豆名来返回咖啡豆相关的信息
        LambdaQueryWrapper<CoffeeBeanGradeInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CoffeeBeanGradeInfo::getEnglishName, record.getDefectsName());
        CoffeeBeanGradeInfo coffeeBeanGradeInfo = coffeeBeanGradeInfoMapper.selectOne(wrapper);
        CoffeeBeanGradeInfoVO coffeeBeanGradeInfoVO = new CoffeeBeanGradeInfoVO();
        if (coffeeBeanGradeInfo != null) {
            BeanUtils.copyProperties(coffeeBeanGradeInfo, coffeeBeanGradeInfoVO);
        }
        //返回检测id
        coffeeBeanGradeInfoVO.setDetectionId(record.getId());

        return coffeeBeanGradeInfoVO;
    }

    /** 将 Python JSON 输出解析为 DTO */
    private DetectionResultDTO parseJsonToDTO(String defectsJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(defectsJson);

        DetectionResultDTO dto = new DetectionResultDTO();
        dto.setStatus("success");

        List<DetectionResultDTO.Defect> defectList = new ArrayList<>();

        // 优先读取 defects 数组
        JsonNode defectsArray = root.get("defects");
        if (defectsArray != null && defectsArray.isArray() && defectsArray.size() > 0) {
            for (JsonNode node : defectsArray) {
                DetectionResultDTO.Defect defect = new DetectionResultDTO.Defect();
                defect.setType(node.get("type").asText());
                defect.setConfidence(node.get("confidence").asDouble());
                defectList.add(defect);
            }
        } else {
            // fallback 到 output 数组
            JsonNode outputArray = root.get("output");
            if (outputArray != null && outputArray.isArray()) {
                for (int i = 0; i < outputArray.size(); i++) {
                    double confidence = outputArray.get(i).asDouble();
                    if (confidence > 0.5) { // confidence 阈值可配置
                        DetectionResultDTO.Defect defect = new DetectionResultDTO.Defect();
                        defect.setType(i < labels.size() ? labels.get(i) : "unknown");
                        defect.setConfidence(confidence);
                        defectList.add(defect);
                    }
                }
            }
        }

        dto.setDefects(defectList);
        return dto;
    }
}
