package com.coffee.project.controller;


import com.coffee.project.common.Result;
import com.coffee.project.dto.FavoriteRecordDTO;
import com.coffee.project.service.FavoriteRecordService;
import com.coffee.project.vo.FavoriteRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
@Slf4j
public class FavoriteRecordController {


    @Autowired
    private FavoriteRecordService favoriteRecordService;

    /**
     * 添加收藏
     * @param favoriteRecordDTO
     * @return
     */
    @PostMapping("/add")
    public Result<FavoriteRecordVO> addFavorite(@RequestBody FavoriteRecordDTO favoriteRecordDTO){

       FavoriteRecordVO favoriteRecordVO = favoriteRecordService.addFavorite(favoriteRecordDTO);
        return Result.success(favoriteRecordVO);
    }

    /**
     * 获取当前用户收藏列表
     */
    @GetMapping("/list/{userId}")
    public Result<List<FavoriteRecordVO>> listFavoriteByUserId(@PathVariable Long userId){
        List<FavoriteRecordVO> favoriteRecordVOList = favoriteRecordService.listFavoriteByUserId(userId);
        return Result.success(favoriteRecordVOList);
    }

    /**
     * 删除收藏
     */
    @DeleteMapping("/delete/{favoriteId}")
    public Result<String> deleteFavorite(@PathVariable Long favoriteId){

       Boolean delete = favoriteRecordService.deleteFavorite(favoriteId);

        return delete ? Result.success("删除成功!") : Result.error("删除失败！");
    }

    /**
     * 按缺陷名搜索收藏
     */
    @GetMapping("/search")
    public Result<List<FavoriteRecordVO>> searchFavorites(
            @RequestParam Long userId,
            @RequestParam String keyword) {
        List<FavoriteRecordVO> list = favoriteRecordService.searchFavorites(userId, keyword);
        return Result.success(list);
    }


}
