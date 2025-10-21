package com.coffee.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 检测历史视图对象（DetectionHistoryVO）。
 * <p>
 * 该类用于封装检测历史记录的相关信息，以便在不同层之间传递数据。
 * 它是一个简单的视图对象（VO），通常没有业务逻辑，仅用来接收或返回数据。
 * </p>
 */
@Data // 自动生成所有属性的 getter/setter、toString、equals 等方法
@NoArgsConstructor // 自动生成无参构造函数
@AllArgsConstructor // 自动生成全参构造函数
public class DetectionHistoryVO {

    /**
     * 检测到的缺陷名称。
     * <p>
     * 该字段存储了检测过程中发现的缺陷名称，用于标识具体的缺陷类型。
     * </p>
     */
    private String defectsName;

    /**
     * 检测图像的路径。
     * <p>
     * 该字段存储了检测图像的存储路径，可用于在用户界面中显示检测图像。
     * </p>
     */
    private String imagePath;

    /**
     * 检测记录的创建时间。
     * <p>
     * 该字段记录了检测记录的创建时间，通常在检测完成后自动生成。
     * </p>
     */
    private LocalDateTime createdAt;
}