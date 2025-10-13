package com.coffee.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coffee.project.domain.FavoriteRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 收藏记录的 Mapper 接口。
 * <p>
 * 该接口用于定义与收藏记录相关的数据库操作方法，继承了 MyBatis-Plus 的 BaseMapper，
 * 提供了基本的 CRUD 操作，并通过自定义方法扩展了特定的查询功能[^4^]。
 * </p>
 */
@Mapper // 标注该接口为 MyBatis 的 Mapper 接口，用于数据库操作
public interface FavoriteRecordMapper extends BaseMapper<FavoriteRecord> {

    /**
     * 根据用户ID和缺陷名模糊搜索收藏记录。
     * <p>
     * 该方法通过用户ID和缺陷名的关键字进行模糊匹配，查询出符合条件的收藏记录，
     * 并按创建时间降序排列[^4^]。
     * </p>
     *
     * @param userId 用户的唯一标识符，用于限定查询的用户范围。
     * @param keyword 缺陷名的关键字，用于模糊匹配缺陷名。
     * @return 符合条件的收藏记录列表。
     */
    @Select("SELECT * FROM favorite_record WHERE user_id = #{userId} AND defects_name LIKE CONCAT('%', #{keyword}, '%') ORDER BY created_at DESC")
    List<FavoriteRecord> searchByDefectName(@Param("userId") Long userId, @Param("keyword") String keyword);
}