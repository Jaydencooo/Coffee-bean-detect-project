package com.coffee.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coffee.project.domain.DetectionHistory;
import com.coffee.project.vo.DetectionHistoryVO;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 咖啡豆检测历史记录 Mapper 接口
 * 继承 MyBatis-Plus 的 BaseMapper 提供基础 CRUD 操作
 * 额外提供自定义查询方法 getUserHistory，用于获取用户检测历史记录列表
 */
@Mapper
public interface DetectionHistoryMapper extends BaseMapper<DetectionHistory> {

    /**
     * 获取指定用户的检测历史记录列表
     * 查询结果按创建时间倒序排列（最新的在前）
     *
     * @param userId 用户ID
     * @return 返回 DetectionHistoryVO 列表，只包含 defects_name、image_path 和 created_at 字段
     */
    @Select("SELECT defects_name, image_path, created_at " +
            "FROM detection_history " +
            "WHERE user_id = #{userId} " +
            "ORDER BY created_at DESC")
    List<DetectionHistoryVO> getUserHistory(@Param("userId") Long userId);
}
