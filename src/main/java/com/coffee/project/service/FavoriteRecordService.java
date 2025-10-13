package com.coffee.project.service;

import com.coffee.project.dto.FavoriteRecordDTO;
import com.coffee.project.vo.FavoriteRecordVO;

import java.util.List;

/**
 * 收藏记录服务接口（FavoriteRecordService）。
 * <p>
 * 该接口定义了与收藏记录相关的业务逻辑操作，包括添加收藏记录、根据用户ID查询收藏记录列表、删除收藏记录以及搜索收藏记录。
 * </p>
 */
public interface FavoriteRecordService {

    /**
     * 添加一个新的收藏记录。
     * <p>
     * 该方法接收一个 FavoriteRecordDTO 对象，包含用户ID和检测记录ID等信息，用于创建一个新的收藏记录。
     * </p>
     *
     * @param favoriteRecordDTO 包含收藏记录信息的数据传输对象。
     * @return 添加成功的收藏记录视图对象（FavoriteRecordVO）。
     */
    FavoriteRecordVO addFavorite(FavoriteRecordDTO favoriteRecordDTO);

    /**
     * 根据用户ID查询收藏记录列表。
     * <p>
     * 该方法通过用户ID查询数据库，获取该用户的所有收藏记录，并将其转换为 FavoriteRecordVO 对象列表返回。
     * </p>
     *
     * @param userId 用户的唯一标识符，用于查询其收藏记录。
     * @return 包含收藏记录的 FavoriteRecordVO 对象列表。
     */
    List<FavoriteRecordVO> listFavoriteByUserId(Long userId);

    /**
     * 根据收藏记录ID删除收藏记录。
     * <p>
     * 该方法通过收藏记录ID在数据库中查找并删除对应的收藏记录。
     * </p>
     *
     * @param favoriteId 收藏记录的唯一标识符，用于删除指定的收藏记录。
     * @return 操作结果，true 表示删除成功，false 表示删除失败。
     */
    Boolean deleteFavorite(Long favoriteId);

    /**
     * 搜索收藏记录。
     * <p>
     * 该方法通过用户ID和关键字进行模糊搜索，查询出符合条件的收藏记录，并将其转换为 FavoriteRecordVO 对象列表返回。
     * </p>
     *
     * @param userId 用户的唯一标识符，用于限定搜索的用户范围。
     * @param keyword 搜索关键字，用于模糊匹配收藏记录的相关字段。
     * @return 包含搜索结果的 FavoriteRecordVO 对象列表。
     */
    List<FavoriteRecordVO> searchFavorites(Long userId, String keyword);
}