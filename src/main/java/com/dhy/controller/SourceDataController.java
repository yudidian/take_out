package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dhy.common.R;
import com.dhy.entity.*;
import com.dhy.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/data")
@ResponseBody
@Api(value = "大数据看板相关接口")
public class SourceDataController {
    @Autowired
    private UserService userService;
    @Autowired
    private DishService dishService;

    @Autowired
    private ProductReviewsService productReviewsService;

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private SetmealService setmealService;
    @ApiOperation(value = "获取总的用户数据")
    @GetMapping("/userCount")
    private R<Map<String,Object>> getUserCount(){
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        int AllCount = userService.count();
        userLambdaQueryWrapper.eq(User::getStatus, 0);
        int liveCount = userService.count(userLambdaQueryWrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("allCount", AllCount);
        hashMap.put("liveCount", liveCount);
        return R.success(hashMap,"获取成功");
    }

    @ApiOperation(value = "获取菜品的销售信息")
    @GetMapping("/dish")
    private R<Map<String, Object>> getAllDishInfo() {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getStatus, 1).orderByDesc(Dish::getSalesVolume);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus, 1).orderByDesc(Setmeal::getSalesVolume);
        List<Dish> dish = dishService.list(dishLambdaQueryWrapper);
        List<Setmeal> setmeal = setmealService.list(setmealLambdaQueryWrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        BigDecimal first = dish.get(0).getPrice().multiply(new BigDecimal(dish.get(0).getSalesVolume()));
        BigDecimal second = dish.get(1).getPrice().multiply(new BigDecimal(dish.get(1).getSalesVolume()));
        BigDecimal three = dish.get(2).getPrice().multiply(new BigDecimal(dish.get(2).getSalesVolume()));
        AtomicReference<BigDecimal> allSales = new AtomicReference<>(BigDecimal.ZERO);
        dish.forEach(item -> {
            allSales.updateAndGet(v-> {
                v = v.add(item.getPrice().multiply(new BigDecimal(item.getSalesVolume())));
                return v;
            });
        });
        hashMap.put("dish",dish);
        hashMap.put("setmeal", setmeal);
        hashMap.put("first", first);
        hashMap.put("second", second);
        hashMap.put("three", three);
        hashMap.put("allSales", allSales);
        return R.success(hashMap, "获取成功");
    }
    @ApiOperation(value = "获取评论情况")
    @GetMapping("/review")
    private R<Map<String, Object>> getReviewInfo() {
        LambdaQueryWrapper<ProductReviews> reviewsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        reviewsLambdaQueryWrapper.eq(ProductReviews::getRating, 5);
        int allCount = productReviewsService.count();
        int goodReviewCount = productReviewsService.count(reviewsLambdaQueryWrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("allCount", allCount);
        hashMap.put("goodReviewCount", goodReviewCount);
        return R.success(hashMap, "获取成功");
    }
    @ApiOperation(value = "获取订单情况")
    @GetMapping("/order")
    private R<Map<String, Object>> getOrdersInfo() {
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.orderByDesc(Orders::getCheckoutTime).last("limit 10");
        List<Orders> list = ordersService.list(ordersLambdaQueryWrapper);
        int allCount = ordersService.count();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("allCount", allCount);
        hashMap.put("list", list);
        return R.success(hashMap, "获取成功");
    }
}
