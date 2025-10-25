package com.coffee.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coffee.project.domain.DetectionHistory;
import com.coffee.project.mapper.DetectionHistoryMapper;
import com.coffee.project.service.DetectionHistoryService;
import com.coffee.project.vo.DetectionHistoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 检测历史服务实现类（DetectionHistoryServiceImpl）。
 * <p>
 * 该类实现了 DetectionHistoryService 接口，提供了与检测历史相关的业务逻辑实现。
 * 主要功能包括根据用户ID获取检测历史记录和添加新的检测记录。
 * </p>
 */
@Service // 标注该类为 Spring 的服务组件
@Slf4j // 使用 Lombok 提供的日志功能
public class DetectionHistoryServiceImpl implements DetectionHistoryService {

    @Autowired
    private DetectionHistoryMapper detectionHistoryMapper; // 注入检测历史 Mapper

    /**
     * 根据用户ID获取检测历史记录。
     * <p>
     * 该方法通过用户ID查询数据库，获取该用户的所有检测历史记录，
     * 并将其转换为 DetectionHistoryVO 对象列表返回。
     * 如果没有任何记录，将返回一个空列表（[]），而不是 null。
     * </p>
     *
     * @param userId 用户的唯一标识符，用于查询其检测历史记录。
     * @return 包含检测历史记录的 DetectionHistoryVO 对象列表（可能为空）。
     */
    @Override
    public List<DetectionHistoryVO> getHistoryByUserId(Long userId) {
        // 查询用户的检测历史记录
        List<DetectionHistory> list = detectionHistoryMapper.selectList(
                new LambdaQueryWrapper<DetectionHistory>()
                        .eq(DetectionHistory::getUserId, userId)
        );

        // 如果为空，直接返回一个空列表，避免返回 null
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        // 将查询结果转换为 VO 列表
        return list.stream().map(detectionHistory -> {
            DetectionHistoryVO detectionHistoryVO = new DetectionHistoryVO();
            BeanUtils.copyProperties(detectionHistory, detectionHistoryVO);
            return detectionHistoryVO;
        }).collect(Collectors.toList());
    }


    /**
     * 添加新的检测记录。
     * <p>
     * 该方法创建一个新的 DetectionHistory 对象，设置其属性，并将其插入到数据库中。
     * </p>
     *
     * @param userId 用户的唯一标识符，表示该检测记录所属的用户。
     * @param defectsName 检测到的缺陷名称。
     * @param imagePath 检测图像的路径。
     */
    @Override
    public void addHistory(Long userId, String defectsName, String imagePath) {
        DetectionHistory detectionHistory = new DetectionHistory(); // 创建新的检测历史对象
        detectionHistory.setUserId(userId); // 设置用户ID
        detectionHistory.setDefectsName(defectsName); // 设置缺陷名称
        detectionHistory.setImagePath(imagePath); // 设置图像路径
        detectionHistory.setCreatedAt(LocalDateTime.now()); // 设置创建时间为当前时间
        detectionHistoryMapper.insert(detectionHistory); // 插入到数据库
    }
}