package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhy.ApiModel.ReviewsModel;
import com.dhy.DTO.ProductReviewsDto;
import com.dhy.common.R;
import com.dhy.entity.Dish;
import com.dhy.entity.ProductReviews;
import com.dhy.entity.Setmeal;
import com.dhy.entity.User;
import com.dhy.service.DishService;
import com.dhy.service.ProductReviewsService;
import com.dhy.service.SetmealService;
import com.dhy.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@ResponseBody
@RequestMapping("/reviews")
@Api(tags = "菜品评论相关")
public class ProductReviewsController {
    @Autowired
    private ProductReviewsService productReviewsService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private UserService userService;

    @PostMapping()
    @ApiOperation(value = "评论菜品")
    @ApiImplicitParam(name = "productReviews", value = "评论实体类", dataTypeClass = ProductReviews.class)
    private R<String> savaReviews(@RequestBody ProductReviews productReviews, HttpSession session) {
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        User use = userService.getById(userId);
        productReviews.setAvatar(use.getAvatar());
        productReviews.setUserId(userId);
        productReviews.setCreateTime(LocalDateTime.now());
        productReviewsService.save(productReviews);
        if (productReviews.getDishId() != null) {
            Dish byId = dishService.getById(productReviews.getDishId());
            byId.setReviewCount(byId.getReviewCount() + 1);
            dishService.updateById(byId);
        } else {
            Setmeal setmeal = setmealService.getById(productReviews.getSetmealId());
            setmeal.setReviewCount(setmeal.getReviewCount() + 1);
            setmealService.updateById(setmeal);
        }
        // 评论保存成功后对应菜品评论数量增加

        return R.success(null, "评论成功");
    }

    //根据商品ID获取评论列表
    @GetMapping("/list")
    @ApiOperation(value = "根据商品ID获取评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dishId", dataType = "String", value = "菜品ID", dataTypeClass = String.class),
            @ApiImplicitParam(name = "setmealId", dataType = "String", value = "套餐ID", dataTypeClass = String.class),
            @ApiImplicitParam(name = "reta", dataType = "int", value = "评分等级", dataTypeClass = int.class),
            @ApiImplicitParam(name = "page", value = "当前页", dataType = "int", required = true, paramType = "query", dataTypeClass = int.class),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", required = true, paramType = "query", dataTypeClass = int.class),
    })
    private R<Page<ProductReviewsDto>> getReviews(Long dishId, Long setmealId, int reta, HttpSession session, int page, int pageSize) {
        // reta 0 表示全部
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        return productReviewsService.getReviewsList(dishId, setmealId, reta, userId, page, pageSize);
    }

    @GetMapping("/count")
    @ApiOperation(value = "获取各个评论数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dishId", dataType = "String", value = "菜品ID", dataTypeClass = String.class),
            @ApiImplicitParam(name = "setmealId", dataType = "String", value = "套餐ID", dataTypeClass = String.class)
    })
    private R<Map<String, Object>> getReviewsCount(Long dishId, Long setmealId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        LambdaQueryWrapper<ProductReviews> AllQueryWrapper = new LambdaQueryWrapper<>();
        AllQueryWrapper.eq(dishId != null, ProductReviews::getDishId, dishId).eq(setmealId != null, ProductReviews::getSetmealId, setmealId);
        hashMap.put("all", productReviewsService.count(AllQueryWrapper));
        LambdaQueryWrapper<ProductReviews> QueryWrapper_1 = new LambdaQueryWrapper<>();
        QueryWrapper_1.eq(dishId != null, ProductReviews::getDishId, dishId).eq(setmealId != null, ProductReviews::getSetmealId, setmealId).eq(ProductReviews::getRating, 1);
        hashMap.put("rate_1", productReviewsService.count(QueryWrapper_1));
        LambdaQueryWrapper<ProductReviews> QueryWrapper_2 = new LambdaQueryWrapper<>();
        QueryWrapper_2.eq(dishId != null, ProductReviews::getDishId, dishId).eq(setmealId != null, ProductReviews::getSetmealId, setmealId).eq(ProductReviews::getRating, 2);
        hashMap.put("rate_2", productReviewsService.count(QueryWrapper_2));
        LambdaQueryWrapper<ProductReviews> QueryWrapper_3 = new LambdaQueryWrapper<>();
        QueryWrapper_3.eq(dishId != null, ProductReviews::getDishId, dishId).eq(setmealId != null, ProductReviews::getSetmealId, setmealId).eq(ProductReviews::getRating, 3);
        hashMap.put("rate_3", productReviewsService.count(QueryWrapper_3));
        LambdaQueryWrapper<ProductReviews> QueryWrapper_4 = new LambdaQueryWrapper<>();
        QueryWrapper_4.eq(dishId != null, ProductReviews::getDishId, dishId).eq(setmealId != null, ProductReviews::getSetmealId, setmealId).eq(ProductReviews::getRating, 4);
        hashMap.put("rate_4", productReviewsService.count(QueryWrapper_4));
        LambdaQueryWrapper<ProductReviews> QueryWrapper_5 = new LambdaQueryWrapper<>();
        QueryWrapper_5.eq(dishId != null, ProductReviews::getDishId, dishId).eq(setmealId != null, ProductReviews::getSetmealId, setmealId).eq(ProductReviews::getRating, 5);
        hashMap.put("rate_5", productReviewsService.count(QueryWrapper_5));
        return R.SuccessPlus(hashMap, "获取成功");
    }

    @GetMapping("/all")
    @ApiOperation(value = "获取全部评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dishId", value = "菜品ID", dataType = "String", paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "setmealId", value = "套餐ID", dataType = "String", paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "page", value = "当前页", dataType = "int", required = true, paramType = "query", dataTypeClass = int.class),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", required = true, paramType = "query", dataTypeClass = int.class),
    })
    private R<Page<ProductReviewsDto>> getAllReviews(@ApiIgnore @RequestParam Map<String, Object> map) {
        return productReviewsService.getAllReviewsList(map);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据ID删除评论")
    @ApiImplicitParam(name = "id", value = "评论ID", paramType = "path", required = true, dataTypeClass = String.class)
    private R<String> deleteReviews(@PathVariable Long id) {
        productReviewsService.removeById(id);
        return R.success(null, "删除成功");
    }
}
