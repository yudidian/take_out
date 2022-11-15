package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhy.DTO.FavoritesListDto;
import com.dhy.common.R;
import com.dhy.entity.Dish;
import com.dhy.entity.FavoritesList;
import com.dhy.entity.Setmeal;
import com.dhy.service.DishService;
import com.dhy.service.FavoritesListService;
import com.dhy.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorites")
@ResponseBody
@Api(tags = "收藏列表相关接口")
public class FavoritesListController {
    @Autowired
    private FavoritesListService favoritesListService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @GetMapping("/change")
    @ApiOperation(value = "收藏或取消")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "菜品或套餐ID", name = "id", dataType = "String", required = true, paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(value = "套餐或者菜品标识", name = "type", dataType = "String", required = true, paramType = "query", dataTypeClass = String.class)
    })
    private R<String> addFavorites(@ApiIgnore @RequestParam Map<String, Object> map, @ApiIgnore HttpSession session) {
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        FavoritesList favoritesList = new FavoritesList();
        favoritesList.setUserId(userId);
        favoritesList.setCreatTime(LocalDateTime.now());
        LambdaQueryWrapper<FavoritesList> queryWrapper = new LambdaQueryWrapper<>();
        if (map.get("type").toString().equals("1")) {
            favoritesList.setDishId(Long.valueOf(map.get("id").toString()));
            queryWrapper.eq(FavoritesList::getDishId, Long.valueOf(map.get("id").toString()));
        } else {
            favoritesList.setSetmealId(Long.valueOf(map.get("id").toString()));
            queryWrapper.eq(FavoritesList::getSetmealId, Long.valueOf(map.get("id").toString()));
        }
        queryWrapper.eq(FavoritesList::getUserId, userId);
        FavoritesList list = favoritesListService.getOne(queryWrapper);
        if (list != null) {
            favoritesListService.remove(queryWrapper);
        } else {
            favoritesListService.save(favoritesList);
        }
        return R.success(null, "操作成功");
    }

    @GetMapping("/{id}/{type}")
    @ApiOperation(value = "查看对应用户的菜品是否有收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜品或者套餐ID", dataType = "String", required = true, paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "type", value = "菜品或者套餐标识1菜品2套餐", required = true, paramType = "query", dataTypeClass = String.class)
    })
    private R<String> getFavoritesType(@PathVariable Long id, @PathVariable String type, @ApiIgnore HttpSession session) {
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        LambdaQueryWrapper<FavoritesList> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FavoritesList::getUserId, userId);
        if (Objects.equals(type, "1")) {
            queryWrapper.eq(FavoritesList::getDishId, id);
        } else {
            queryWrapper.eq(FavoritesList::getSetmealId, id);
        }
        FavoritesList list = favoritesListService.getOne(queryWrapper);
        Map<String, Object> map = new HashMap<>();
        if (list != null) {
            map.put("isFavorites", true);
        } else {
            map.put("isFavorites", false);
        }
        return R.SuccessPlus(map, "获取成功");
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取用户收藏列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", dataType = "int", required = true, paramType = "query", dataTypeClass = int.class),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", required = true, paramType = "query", dataTypeClass = int.class)
    })
    private R<Page<FavoritesListDto>> getFavoritesList(int page, int pageSize,@ApiIgnore HttpSession session) {
        Long userId = Long.valueOf(session.getAttribute("userId").toString());

        Page<FavoritesList> favoritesListPage = new Page<>(page, pageSize);
        Page<FavoritesListDto> favoritesListDtoPage = new Page<>();

        LambdaQueryWrapper<FavoritesList> favoritesListLambdaQueryWrapper = new LambdaQueryWrapper<>();
        favoritesListLambdaQueryWrapper.eq(FavoritesList::getUserId, userId);
        favoritesListService.page(favoritesListPage, favoritesListLambdaQueryWrapper);


        BeanUtils.copyProperties(favoritesListPage, favoritesListDtoPage, "records");


        List<FavoritesListDto> collect = favoritesListPage.getRecords().stream().map(item -> {
            FavoritesListDto favoritesListDto = new FavoritesListDto();
            if (item.getDishId() != null) {
                Dish dish = dishService.getById(item.getDishId());
                favoritesListDto.setDishId(dish.getId());
                favoritesListDto.setName(dish.getName());
                favoritesListDto.setImage(dish.getImage());
                favoritesListDto.setType("1");
            } else {
                Setmeal setmeal = setmealService.getById(item.getSetmealId());
                favoritesListDto.setDishId(setmeal.getId());
                favoritesListDto.setName(setmeal.getName());
                favoritesListDto.setImage(setmeal.getImage());
                favoritesListDto.setType("2");
            }
            return favoritesListDto;
        }).collect(Collectors.toList());
        favoritesListDtoPage.setRecords(collect);
        return R.success(favoritesListDtoPage, "用户收藏列表获取成功");
    }
}
