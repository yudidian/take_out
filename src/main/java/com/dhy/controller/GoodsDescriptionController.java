package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhy.common.R;
import com.dhy.entity.Dish;
import com.dhy.entity.GoodsDescription;
import com.dhy.service.DishService;
import com.dhy.service.GoodsDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods/description")
@ResponseBody
public class GoodsDescriptionController {
    @Autowired
    private GoodsDescriptionService goodsDescriptionService;

    @Autowired
    private DishService dishService;

    /**
     * 获取菜品描述列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/list")
    private R<Page<GoodsDescription>> getDescription(int page, int pageSize, String name) {
        LambdaQueryWrapper<GoodsDescription> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(name != null, GoodsDescription::getName, name);
        Page<GoodsDescription> goodsDescriptionPage = new Page<>(page, pageSize);
        goodsDescriptionService.page(goodsDescriptionPage, queryWrapper);
        return R.success(goodsDescriptionPage, "success");
    }

    @GetMapping("/set")
    private R<String> setDescription() {
        List<Dish> list = dishService.list();
        for (Dish dish : list) {
            GoodsDescription goodsDescription = new GoodsDescription();
            goodsDescription.setImage(dish.getImage());
            goodsDescription.setDishId(dish.getId());
            goodsDescription.setName(dish.getName());
            goodsDescriptionService.save(goodsDescription);
        }
        return R.success(null, "success");
    }

    /*
    改变菜品描述
     */
    @PostMapping("/change")
    private R<String> addDescription(@RequestBody GoodsDescription goodsDescription) {
        LambdaQueryWrapper<GoodsDescription> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsDescription::getDishId, goodsDescription.getDishId());
        GoodsDescription info = goodsDescriptionService.getOne(queryWrapper);
        if (info == null) {
            goodsDescriptionService.save(goodsDescription);
        } else {
            goodsDescriptionService.updateById(goodsDescription);
        }
        return R.success(null, "success");
    }
    /*
    根据菜品id获取对应描述
     */
    @GetMapping("/{id}")
    private R<GoodsDescription> getByDishId(@PathVariable Long id) {
        LambdaQueryWrapper<GoodsDescription> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsDescription::getDishId, id);
        GoodsDescription description = goodsDescriptionService.getOne(queryWrapper);
        return R.success(description,"success");
    }
}
