package com.coffee.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏记录实体类
 * 用于记录用户对咖啡豆检测记录中缺陷豆的收藏信息
 */
@Data
@TableName("favorite_record") // 对应数据库表名：favorite_record
public class FavoriteRecord {

    /**
     * 主键ID
     * 自增长
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     * 对应收藏该记录的用户
     */
    private Long userId;

    /**
     * 检测记录ID
     * 对应被收藏的咖啡豆检测记录
     */
    private Long detectionId;

    /**
     * 缺陷豆名称
     * 收藏的缺陷豆类型名称，例如“霉豆”、“黑豆”等
     */
    private String defectsName;

    /**
     * 图片路径
     * 收藏的缺陷豆图片存储路径
     */
    private String imagePath;

    /**
     * 收藏时间
     * 记录该条收藏的创建时间
     */
    private LocalDateTime createdAt;
}
