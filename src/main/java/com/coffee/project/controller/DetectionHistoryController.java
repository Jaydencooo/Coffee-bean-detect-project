package com.coffee.project.controller;


import com.coffee.project.common.Result;
import com.coffee.project.service.DetectionHistoryService;
import com.coffee.project.vo.DetectionHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detection")
public class DetectionHistoryController {

    @Autowired
    private DetectionHistoryService detectionHistoryService;

    // 获取识别历史
    @GetMapping("/history")
    public Result<List<DetectionHistoryVO>> getHistory(@RequestParam Long userId) {
        List<DetectionHistoryVO> list = detectionHistoryService.getHistoryByUserId(userId);
        return Result.success(list);
    }

    // 添加识别记录（可以在识别完成后自动添加识别记录 这个接口仅供测试用）
    //    @PostMapping("/history")
    //    public Result<Object> addHistory(@RequestParam Long userId,
    //                                   @RequestParam String defectsName,
    //                                   @RequestParam String imagePath) {
    //        detectionHistoryService.addHistory(userId, defectsName, imagePath);
    //        return Result.success();
    //    }

}
