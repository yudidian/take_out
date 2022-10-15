package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dhy.common.R;
import com.dhy.entity.FavoritesList;
import com.dhy.service.FavoritesListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/favorites")
@ResponseBody
public class FavoritesListController {
    @Autowired
    private FavoritesListService favoritesListService;

    @GetMapping("/change")
    private R<String> addFavorites(FavoritesList favoritesList, HttpSession session) {
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        if (!Objects.equals(favoritesList.getUserId(), userId)) {
            return R.error("非法操作");
        }
        LambdaQueryWrapper<FavoritesList> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FavoritesList::getDishId, favoritesList.getDishId());
        queryWrapper.eq(FavoritesList::getUserId, userId);
        FavoritesList list = favoritesListService.getOne(queryWrapper);
        if (list != null) {
            favoritesListService.remove(queryWrapper);
        } else {
            favoritesListService.save(favoritesList);
        }
        return R.success(null,"操作成功");
    }

    @GetMapping("/{id}")
    private R<String> addFavorites(@PathVariable Long id, HttpSession session) {
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        LambdaQueryWrapper<FavoritesList> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FavoritesList::getDishId, id);
        queryWrapper.eq(FavoritesList::getUserId, userId);
        FavoritesList list = favoritesListService.getOne(queryWrapper);
        Map<String,Object> map = new HashMap<>();
        if (list != null) {
            map.put("isFavorites", true);
        } else {
            map.put("isFavorites", false);
        }
        return R.SuccessPlus(map,"获取成功");
    }
}
