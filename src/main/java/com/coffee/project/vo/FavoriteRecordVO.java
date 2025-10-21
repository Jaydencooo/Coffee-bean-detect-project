package com.coffee.project.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * FavoriteRecordVO（收藏记录视图对象）
 *
 * 用于向前端展示用户收藏的缺陷咖啡豆记录。
 * 一般作为接口返回的数据载体（VO：View Object）。
 *
 * 主要包含：
 * - 缺陷豆名称
 * - 图片路径
 * - 收藏时间
 */
@Data
public class FavoriteRecordVO {

    /**
     * 缺陷豆名称
     * 例如："发霉豆"、"破碎豆" 等。
     */
    private String defectsName;

    /**
     * 图片路径
     * 对应收藏时的咖啡豆检测结果图片的存储路径，
     * 前端可通过该路径加载图片。
     */
    private String imagePath;

    /**
     * 收藏时间
     * 表示用户收藏该检测结果的时间，
     * 通常由数据库自动生成。
     */
    private LocalDateTime createdAt;
}
