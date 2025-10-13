package com.coffee.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetectionHistoryVO {

    private String defectsName;

    private String imagePath;

    private LocalDateTime createdAt;
}
