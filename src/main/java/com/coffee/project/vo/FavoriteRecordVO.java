package com.coffee.project.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteRecordVO {
    private String defectsName;         // 缺陷豆名字
    private String imagePath;    // 图片路径
    private LocalDateTime createdAt; // 收藏时间
}
