package com.coffee.project.service;

import com.coffee.project.dto.DetectionResultDTO;
import com.coffee.project.vo.CoffeeBeanGradeInfoVO;

import java.io.IOException;

/**
 * DetectionRecordService 接口
 * 负责处理咖啡豆缺陷检测的业务逻辑：
 * 1. 调用模型进行缺陷检测
 * 2. 保存检测结果到数据库
 * 3. 返回前端可直接使用的 DTO
 */
public interface DetectionRecordService {

    /**
     * 对指定图片进行缺陷检测
     * 并将检测结果保存到数据库，同时返回前端可使用的 DTO
     *
     * @param imagePath 图片文件路径
     * @param userId    当前用户 ID，用于关联检测记录
     * @return DetectionResultDTO 对象，包含缺陷类型列表及置信度
     * @throws IOException          图片读取或写入异常
     * @throws InterruptedException 调用外部脚本或模型执行时的等待异常
     */
    CoffeeBeanGradeInfoVO detectAndSaveAndReturnDTO(String imagePath, Long userId) throws IOException, InterruptedException;
}
