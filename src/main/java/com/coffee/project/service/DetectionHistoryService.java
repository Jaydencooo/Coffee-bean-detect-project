package com.coffee.project.service;

import com.coffee.project.vo.DetectionHistoryVO;

import java.util.List;

/**
 * 检测历史服务接口（DetectionHistoryService）。
 * <p>
 * 该接口定义了与检测历史记录相关的业务逻辑操作，包括根据用户ID获取检测历史记录和添加新的检测记录。
 * </p>
 */
public interface DetectionHistoryService {

    /**
     * 根据用户ID获取检测历史记录。
     * <p>
     * 该方法通过用户ID查询数据库，获取该用户的所有检测历史记录，并将其转换为 DetectionHistoryVO 对象列表返回。
     * </p>
     *
     * @param userId 用户的唯一标识符，用于查询其检测历史记录。
     * @return 包含检测历史记录的 DetectionHistoryVO 对象列表。
     */
    List<DetectionHistoryVO> getHistoryByUserId(Long userId);

    /**
     * 添加新的检测记录。
     * <p>
     * 该方法创建一个新的检测记录，设置其属性（用户ID、缺陷名称、图像路径等），并将其插入到数据库中。
     * </p>
     *
     * @param userId 用户的唯一标识符，表示该检测记录所属的用户。
     * @param defectsName 检测到的缺陷名称。
     * @param imagePath 检测图像的路径。
     */
    void addHistory(Long userId, String defectsName, String imagePath);
}