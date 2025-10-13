package com.coffee.project.controller;

import com.coffee.project.common.Result;
import com.coffee.project.dto.FavoriteRecordDTO;
import com.coffee.project.service.FavoriteRecordService;
import com.coffee.project.vo.FavoriteRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收藏记录控制器
 * 提供用户对咖啡豆缺陷收藏记录的增删改查接口
 */
@RestController
@RequestMapping("/favorite") // 请求路径前缀 /favorite
@Slf4j
public class FavoriteRecordController {

    @Autowired
    private FavoriteRecordService favoriteRecordService;

    /**
     * 添加收藏
     * @param favoriteRecordDTO 前端传入的收藏数据DTO
     * @return 返回收藏成功后的VO对象
     */
    @PostMapping("/add")
    public Result<FavoriteRecordVO> addFavorite(@RequestBody FavoriteRecordDTO favoriteRecordDTO){
        FavoriteRecordVO favoriteRecordVO = favoriteRecordService.addFavorite(favoriteRecordDTO);
        return Result.success(favoriteRecordVO);
    }

    /**
     * 获取当前用户收藏列表
     * @param userId 用户ID
     * @return 返回当前用户的所有收藏记录列表
     */
    @GetMapping("/list/{userId}")
    public Result<List<FavoriteRecordVO>> listFavoriteByUserId(@PathVariable Long userId){
        List<FavoriteRecordVO> favoriteRecordVOList = favoriteRecordService.listFavoriteByUserId(userId);
        return Result.success(favoriteRecordVOList);
    }

    /**
     * 删除收藏
     * @param favoriteId 收藏记录ID
     * @return 返回操作结果：成功或失败
     */
    @DeleteMapping("/delete/{favoriteId}")
    public Result<String> deleteFavorite(@PathVariable Long favoriteId){
        Boolean delete = favoriteRecordService.deleteFavorite(favoriteId);
        return delete ? Result.success("删除成功!") : Result.error("删除失败！");
    }

    /**
     * 按缺陷名搜索收藏
     * @param userId 用户ID
     * @param keyword 缺陷名称关键字
     * @return 返回符合条件的收藏记录列表
     */
    @GetMapping("/search")
    public Result<List<FavoriteRecordVO>> searchFavorites(
            @RequestParam Long userId,
            @RequestParam String keyword) {
        List<FavoriteRecordVO> list = favoriteRecordService.searchFavorites(userId, keyword);
        return Result.success(list);
    }
}
