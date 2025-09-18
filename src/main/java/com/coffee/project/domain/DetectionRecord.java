package com.coffee.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 检测记录实体类，映射数据库中 detection_record 表
 * 用来保存每次咖啡豆缺陷检测的相关信息
 */
@Data  // Lombok 注解，自动生成 getter/setter、toString、equals、hashCode 等方法
@TableName("detection_record")  // 指定该实体类对应的数据库表名是 detection_record
public class DetectionRecord {

    /**
     * 主键 ID，数据库自动生成（自增）
     * 该字段是每条记录的唯一标识
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 ID，表示该检测记录是哪个用户上传的文件检测的
     * 与用户表中的用户 ID 关联
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 图片路径，保存检测时上传的图片文件在服务器上的存储路径
     * 前端或后台可以通过此路径访问对应图片
     */
    @TableField("image_path")
    private String imagePath;

    /**
     * 缺陷检测结果，保存为 JSON 格式字符串
     * JSON 内容可能包括检测到的缺陷类型、位置、置信度等信息
     */
    @TableField("defects_json")
    private String defectsJson;

    /**
     * 记录创建时间，保存检测记录的时间戳
     * 使用 Java 8 的 LocalDateTime 类型，方便时间操作
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
