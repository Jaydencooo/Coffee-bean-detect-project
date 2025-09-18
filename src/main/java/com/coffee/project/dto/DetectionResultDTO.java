package com.coffee.project.dto;

import lombok.Data;

import java.util.List;

/**
 * 检测结果的数据传输对象（DTO）
 * 用于封装检测接口返回给前端的缺陷检测结果信息
 */
@Data  // 自动生成 getter/setter、toString、equals 等方法
public class DetectionResultDTO {

    /**
     * 状态信息，比如 "success" 表示检测成功
     */
    private String status;

    /**
     * 检测到的缺陷列表，可能有多个缺陷
     */
    private List<Defect> defects;

    /**
     * 内部静态类，表示单个缺陷的详细信息
     */
    @Data
    public static class Defect {

        /**
         * 缺陷类型，比如 "crack"（裂纹）、"spot"（斑点）等
         */
        private String type;

        /**
         * 置信度，表示检测模型对该缺陷的确信程度，值越大越可信
         * 一般范围是0到1之间的小数
         */
        private Double confidence;

    }
}
