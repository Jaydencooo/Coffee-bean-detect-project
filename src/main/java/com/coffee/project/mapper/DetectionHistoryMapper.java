package com.coffee.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coffee.project.domain.DetectionHistory;
import com.coffee.project.vo.DetectionHistoryVO;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DetectionHistoryMapper extends BaseMapper<DetectionHistory> {

    @Select("SELECT defects_name, image_path, created_at " +
            "FROM detection_history " +
            "WHERE user_id = #{userId} " +
            "ORDER BY created_at DESC")
    List<DetectionHistoryVO> getUserHistory(@Param("userId") Long userId);
}

