package com.coffee.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("favorite_record")
public class FavoriteRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;         // 用户ID
    private Long detectionId;    // 检测记录ID
    private String defectsName;         // 缺陷豆名字
    private String imagePath;    // 图片路径
    private LocalDateTime createdAt; // 收藏时间

}
