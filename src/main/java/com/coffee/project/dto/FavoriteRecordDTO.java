package com.coffee.project.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteRecordDTO {

        private Long userId;       // 谁收藏的

        private Long detectionId;  // 收藏哪个检测记录

}
