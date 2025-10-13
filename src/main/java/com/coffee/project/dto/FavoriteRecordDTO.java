package com.coffee.project.dto;

import lombok.Data;

/**
 * 收藏记录数据传输对象（DTO）。
 * <p>
 * 该类用于封装用户收藏检测记录的相关信息，便于在不同层之间传递数据。
 * 它主要用于表示用户与检测记录之间的收藏关系。
 * </p>
 */
@Data
public class FavoriteRecordDTO {
        /**
         * 用户的唯一标识符，表示收藏行为的主体。
         * <p>
         * 该字段存储了收藏该检测记录的用户ID，用于标识是哪个用户进行了收藏操作。
         * </p>
         */
        private Long userId;

        /**
         * 检测记录的唯一标识符，表示被收藏的检测记录。
         * <p>
         * 该字段存储了被收藏的检测记录ID，用于标识用户收藏的具体检测记录。
         * </p>
         */
        private Long detectionId;
}