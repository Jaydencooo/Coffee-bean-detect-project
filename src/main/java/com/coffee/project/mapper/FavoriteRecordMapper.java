package com.coffee.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coffee.project.domain.FavoriteRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface FavoriteRecordMapper extends BaseMapper<FavoriteRecord> {

    /**
     * 根据用户ID和缺陷名模糊搜索收藏
     */
    @Select("SELECT * FROM favorite_record WHERE user_id = #{userId} AND defects_name LIKE CONCAT('%', #{keyword}, '%') ORDER BY created_at DESC")
    List<FavoriteRecord> searchByDefectName(@Param("userId") Long userId, @Param("keyword") String keyword);


}

