package com.coffee.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 检测历史记录实体类
 * 用于存储用户每次检测咖啡豆缺陷的记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("detection_history")
public class DetectionHistory {

    /**
     * 自增主键
     * 数据库中唯一标识每条检测记录的ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     * 关联用户表的ID，表示是哪个用户进行的检测
     */
    private Long userId;

    /**
     * 缺陷豆名字
     * 检测到的咖啡豆缺陷的名称，例如“黑豆”、“霉变豆”等
     */
    private String defectsName;

    /**
     * 图片路径
     * 检测时上传的咖啡豆图片的存储路径
     */
    private String imagePath;

    /**
     * 识别时间
     * 记录检测操作发生的时间，精确到秒
     */
    private LocalDateTime createdAt;

    /**
     * 检测记录id 返回给前端
     */
    private Long detectionId;
}