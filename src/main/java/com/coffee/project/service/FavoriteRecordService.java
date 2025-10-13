package com.coffee.project.service;

import com.coffee.project.dto.FavoriteRecordDTO;
import com.coffee.project.vo.FavoriteRecordVO;

import java.util.List;

public interface FavoriteRecordService {
    FavoriteRecordVO addFavorite(FavoriteRecordDTO favoriteRecordDTO);

    List<FavoriteRecordVO> listFavoriteByUserId(Long userId);

    Boolean deleteFavorite(Long favoriteId);

    List<FavoriteRecordVO> searchFavorites(Long userId, String keyword);
}
