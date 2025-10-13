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

/**
 * 收藏记录服务实现类
 * 实现用户收藏记录的增删查功能
 */
@Slf4j
@Service
public class FavoriteRecordServiceImpl implements FavoriteRecordService {

    @Autowired
    private FavoriteRecordMapper favoriteRecordMapper;

    @Autowired
    private DetectionRecordMapper detectionRecordMapper;

    /**
     * 添加收藏
     *
     * @param favoriteRecordDTO 前端传入的收藏数据 DTO（包含 userId 和 detectionId）
     * @return 返回收藏后的 FavoriteRecordVO 对象
     * @throws RuntimeException 如果检测记录不存在或已收藏过
     */
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

        // 4️⃣ 返回结果，将实体对象转换为 VO 对象
        FavoriteRecordVO favoriteRecordVO = new FavoriteRecordVO();
        BeanUtils.copyProperties(favoriteRecord, favoriteRecordVO);

        return favoriteRecordVO;
    }

    /**
     * 获取用户收藏列表
     *
     * @param userId 用户ID
     * @return 返回当前用户的收藏记录列表（FavoriteRecordVO）
     */
    @Override
    public List<FavoriteRecordVO> listFavoriteByUserId(Long userId) {

        List<DetectionRecord> list = detectionRecordMapper.selectList(
                new LambdaQueryWrapper<DetectionRecord>()
                        .eq(DetectionRecord::getUserId, userId)
        );

        // 将 DetectionRecord 转换为 FavoriteRecordVO 返回前端
        return list.stream().map(detectionRecord -> {
            FavoriteRecordVO vo = new FavoriteRecordVO();
            BeanUtils.copyProperties(detectionRecord, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 删除收藏记录
     *
     * @param favoriteId 收藏记录ID
     * @return 返回是否删除成功（true/false）
     */
    @Override
    public Boolean deleteFavorite(Long favoriteId) {
        return favoriteRecordMapper.deleteById(favoriteId) > 0;
    }

    /**
     * 根据缺陷名称搜索用户收藏
     *
     * @param userId  用户ID
     * @param keyword 缺陷名称关键字
     * @return 返回匹配的收藏记录列表（FavoriteRecordVO）
     */
    @Override
    public List<FavoriteRecordVO> searchFavorites(Long userId, String keyword) {

        // 调用 Mapper 自定义方法按缺陷名搜索
        List<FavoriteRecord> list = favoriteRecordMapper.searchByDefectName(userId, keyword);

        // 将实体对象转换为 VO 对象返回前端
        return list.stream().map(record -> {
            FavoriteRecordVO vo = new FavoriteRecordVO();
            BeanUtils.copyProperties(record, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
