package com.coffee.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coffee.project.domain.DetectionHistory;
import com.coffee.project.domain.DetectionRecord;
import com.coffee.project.mapper.DetectionHistoryMapper;
import com.coffee.project.service.DetectionHistoryService;
import com.coffee.project.vo.DetectionHistoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DetectionHistoryServiceImpl implements DetectionHistoryService {

    @Autowired
    private DetectionHistoryMapper detectionHistoryMapper;


    @Override
    public List<DetectionHistoryVO> getHistoryByUserId(Long userId) {

        List<DetectionHistory> list = detectionHistoryMapper.selectList(
                new LambdaQueryWrapper<DetectionHistory>()
                        .eq(DetectionHistory::getUserId, userId)
        );

       return list.stream().map(detectionHistory -> {
            DetectionHistoryVO detectionHistoryVO = new DetectionHistoryVO();
            BeanUtils.copyProperties(detectionHistory, detectionHistoryVO);
            return detectionHistoryVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void addHistory(Long userId, String defectsName, String imagePath) {
        DetectionHistory detectionHistory = new DetectionHistory();
        detectionHistory.setUserId(userId);
        detectionHistory.setDefectsName(defectsName);
        detectionHistory.setImagePath(imagePath);
        detectionHistory.setCreatedAt(LocalDateTime.now());
        detectionHistoryMapper.insert(detectionHistory);
    }
}
