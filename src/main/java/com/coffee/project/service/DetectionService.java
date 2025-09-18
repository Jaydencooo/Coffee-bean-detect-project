package com.coffee.project.service;

/**
 * DetectionService 是图像检测服务接口
 * 定义了调用外部 Python 推理脚本进行检测的方法
 */
public interface DetectionService {

    /**
     * 传入图片路径，调用 Python 脚本进行推理检测
     * 返回检测结果的 JSON 格式字符串
     *
     * @param imagePath 要检测的图片文件路径
     * @return 检测结果，通常是 JSON 格式的字符串
     * @throws Exception 调用 Python 脚本过程中可能抛出的异常
     */
    String runInference(String imagePath) throws Exception;
}
