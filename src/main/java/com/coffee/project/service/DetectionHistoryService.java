package com.coffee.project.service;

import com.coffee.project.vo.DetectionHistoryVO;

import java.util.List;

public interface DetectionHistoryService {
    List<DetectionHistoryVO> getHistoryByUserId(Long userId);

    void addHistory(Long userId, String defectsName, String imagePath);
}
