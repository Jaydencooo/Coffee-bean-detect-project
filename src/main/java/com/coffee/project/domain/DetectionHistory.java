package com.coffee.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("detection_history")
public class DetectionHistory {

    @TableId(type = IdType.AUTO)
    private Long id;                // 自增主键

    private Long userId;            // 用户ID

    private String defectsName;     // 缺陷豆名字

    private String imagePath;       // 图片路径

    private LocalDateTime createdAt; // 识别时间
}
