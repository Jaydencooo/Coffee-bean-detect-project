package com.coffee.project.service;

import com.coffee.project.domain.DetectionRecord;
import com.coffee.project.dto.DetectionResultDTO;

import java.io.IOException;

/**
 * DetectionRecordService 接口
 * 负责处理咖啡豆缺陷检测的业务逻辑，包括：
 * 1. 调用模型进行缺陷检测
 * 2. 保存检测结果到数据库
 * 3. 返回检测记录或前端可直接使用的 DTO
 */
public interface DetectionRecordService {

    /**
     * 调用检测模型对指定图片进行缺陷检测
     * 并将检测结果保存为数据库中的 DetectionRecord 实体对象
     *
     * @param imagePath 图片文件路径
     * @param userId    当前用户的 ID，用于关联检测记录
     * @return 保存到数据库的 DetectionRecord 实体对象，包含缺陷 JSON 和创建时间
     * @throws IOException          图片读取或写入异常
     * @throws InterruptedException 调用外部脚本或模型执行时的等待异常
     */
    DetectionRecord detectAndSave(String imagePath, Long userId) throws IOException, InterruptedException;

    /**
     * 调用检测模型对指定图片进行缺陷检测
     * 将检测结果保存到数据库，并将结果解析为前端可直接使用的 DTO 返回
     *
     * @param imagePath 图片文件路径
     * @param userId    当前用户的 ID，用于关联检测记录
     * @return DetectionResultDTO 对象，包含缺陷类型列表及置信度
     * @throws IOException          图片读取或写入异常
     * @throws InterruptedException 调用外部脚本或模型执行时的等待异常
     */
    DetectionResultDTO detectAndSaveAndReturnDTO(String imagePath, Long userId) throws IOException, InterruptedException;
}
