package com.coffee.project.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coffee.project.domain.DetectionRecord;
import com.coffee.project.domain.FavoriteRecord;
import com.coffee.project.dto.FavoriteRecordDTO;
import com.coffee.project.mapper.DetectionRecordMapper;
import com.coffee.project.mapper.FavoriteRecordMapper;
import com.coffee.project.service.FavoriteRecordService;
import com.coffee.project.vo.FavoriteRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FavoriteRecordServiceImpl implements FavoriteRecordService {

    @Autowired
    private FavoriteRecordMapper favoriteRecordMapper;

    @Autowired
    private DetectionRecordMapper detectionRecordMapper;


    @Override
    public FavoriteRecordVO addFavorite(FavoriteRecordDTO favoriteRecordDTO) {

        // 1️⃣ 查询检测记录是否存在
        DetectionRecord detectionRecord = detectionRecordMapper.selectById(favoriteRecordDTO.getDetectionId());
        if (detectionRecord == null) {
            throw new RuntimeException("检测记录不存在，无法收藏");
        }

        // 2️⃣ 判断是否已经收藏过（同一用户 + detectionId）
        LambdaQueryWrapper<FavoriteRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FavoriteRecord::getUserId, favoriteRecordDTO.getUserId())
                .eq(FavoriteRecord::getDetectionId, favoriteRecordDTO.getDetectionId());

        FavoriteRecord existingRecord = favoriteRecordMapper.selectOne(queryWrapper);
        if (existingRecord != null) {
            throw new RuntimeException("您已收藏过该检测记录，无法重复收藏");
        }

        // 3️⃣ 未收藏则添加新记录
        FavoriteRecord favoriteRecord = new FavoriteRecord();
        favoriteRecord.setUserId(favoriteRecordDTO.getUserId());
        favoriteRecord.setDetectionId(favoriteRecordDTO.getDetectionId());
        favoriteRecord.setImagePath(detectionRecord.getImagePath());
        favoriteRecord.setCreatedAt(LocalDateTime.now());
        favoriteRecord.setDefectsName(detectionRecord.getDefectsName());

        favoriteRecordMapper.insert(favoriteRecord);

        // 4️⃣ 返回结果
        FavoriteRecordVO favoriteRecordVO = new FavoriteRecordVO();
        BeanUtils.copyProperties(favoriteRecord, favoriteRecordVO);

        return favoriteRecordVO;
    }



    @Override
    public List<FavoriteRecordVO> listFavoriteByUserId(Long userId) {

        List<DetectionRecord> list = detectionRecordMapper.selectList(
                new LambdaQueryWrapper<DetectionRecord>()
                        .eq(DetectionRecord::getUserId, userId)
        );

        return list.stream().map(detectionRecord -> {
            FavoriteRecordVO vo = new FavoriteRecordVO();
            BeanUtils.copyProperties(detectionRecord, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteFavorite(Long favoriteId) {
        return favoriteRecordMapper.deleteById(favoriteId) > 0;
    }

    @Override
    public List<FavoriteRecordVO> searchFavorites(Long userId, String keyword) {

        List<FavoriteRecord> list = favoriteRecordMapper.searchByDefectName(userId, keyword);

        return list.stream().map(record -> {
            FavoriteRecordVO vo = new FavoriteRecordVO();
            BeanUtils.copyProperties(record, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
