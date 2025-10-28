package com.coffee.project.task;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coffee.project.domain.DetectionHistory;
import com.coffee.project.domain.DetectionRecord;
import com.coffee.project.mapper.DetectionHistoryMapper;
import com.coffee.project.mapper.DetectionRecordMapper;
import com.coffee.project.service.DetectionHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class DetectionHistoryCleanTask {

    @Autowired
    private DetectionHistoryMapper detectionHistoryMapper;

    @Autowired
    private DetectionRecordMapper detectionRecordMapper;

    /**
     * 每月1号凌晨2点执行一次清理任务
     */
    @Scheduled(cron = "0 0 2 1 * ?")
    public void cleanOldRecords() {
        // 当前时间减去30天
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);

        log.info("开始清理30天前的检测历史记录...");

        //删除检测记录 会自动删除历史的记录
        int recordDeleted = detectionRecordMapper.delete(
                new LambdaQueryWrapper<DetectionRecord>()
                        .lt(DetectionRecord::getCreatedAt, threshold)
        );

        log.info("检测数据清理完成，共删除 {} 条记录", recordDeleted);

    }
}
