package com.coffee.project.controller;

import com.coffee.project.common.Result;
import com.coffee.project.service.DetectionHistoryService;
import com.coffee.project.vo.DetectionHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检测历史控制器（DetectionHistoryController）。
 * <p>
 * 该控制器用于处理与检测历史相关的 HTTP 请求，提供获取检测历史记录的功能。
 * 它通过调用 DetectionHistoryService 来实现业务逻辑，并将结果封装在 Result 对象中返回给客户端。
 * </p>
 */
@RestController
@RequestMapping("/detection") // 定义控制器的基础路由路径
public class DetectionHistoryController {

    @Autowired
    private DetectionHistoryService detectionHistoryService; // 注入检测历史服务

    /**
     * 获取用户的检测历史记录。
     * <p>
     * 该方法通过用户ID获取其所有的检测历史记录，并返回一个包含检测历史记录的列表。
     * </p>
     *
     * @param userId 用户的唯一标识符，用于查询其检测历史记录。
     * @return 包含检测历史记录的 Result 对象。
     */
    @GetMapping("/history") // 定义获取检测历史记录的 GET 请求路由
    public Result<List<DetectionHistoryVO>> getHistory(@RequestParam Long userId) {
        List<DetectionHistoryVO> list = detectionHistoryService.getHistoryByUserId(userId); // 调用服务获取检测历史记录
        return Result.success(list); // 将结果封装在 Result 对象中并返回
    }

    /**
     * 添加检测记录（仅供测试用）。
     * <p>
     * 该方法用于添加一个新的检测记录，通常在检测完成后自动添加记录，此接口仅供测试使用。
     * </p>
     *
     * @param userId       用户的唯一标识符，表示该检测记录所属的用户。
     * @param defectsName  检测到的缺陷名称。
     * @param imagePath    检测图像的路径。
     * @return 包含操作结果的 Result 对象。
     */
    //    @PostMapping("/history") // 定义添加检测记录的 POST 请求路由
    //    public Result<Object> addHistory(@RequestParam Long userId,
    //                                   @RequestParam String defectsName,
    //                                   @RequestParam String imagePath) {
    //        detectionHistoryService.addHistory(userId, defectsName, imagePath); // 调用服务添加检测记录
    //        return Result.success(); // 返回操作成功的结果
    //    }
}