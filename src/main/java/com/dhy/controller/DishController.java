package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhy.DTO.DishDto;
import com.dhy.ValidatedGroup.DishDeleteGroup;
import com.dhy.ValidatedGroup.DishSaveGroup;
import com.dhy.common.R;
import com.dhy.entity.Category;
import com.dhy.entity.Dish;
import com.dhy.entity.DishFlavor;
import com.dhy.service.CategoryService;
import com.dhy.service.DishFlavorService;
import com.dhy.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.util.Base64Utils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@ResponseBody
@Slf4j
@Validated
@Api(tags = "菜品相关操作")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    // 添加菜品
    @PostMapping
    @CacheEvict(cacheNames = "dishList", key = "#dishDto.categoryId")
    @ApiOperation(value = "添加菜品")
    @ApiImplicitParam(name = "dishDto", value = "菜品数据", dataType = "DishDto", dataTypeClass = DishDto.class, paramType = "body", required = true)
    @Validated(DishSaveGroup.class)
    public R<String> addDish(@Valid @RequestBody DishDto dishDto) {
        dishService.saveDish(dishDto);
        return R.success(null, "添加成功");
    }

    // 分页查询
    @GetMapping("/page")
    @ApiOperation(value = "菜品分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页", name = "page", dataType = "int", paramType = "query", required = true, dataTypeClass = int.class),
            @ApiImplicitParam(value = "每页数量", name = "pageSize", dataType = "int", paramType = "query", required = true, dataTypeClass = int.class),
            @ApiImplicitParam(value = "搜索的菜品名称", name = "name", dataType = "String", paramType = "query", required = false, dataTypeClass = String.class)
    })
    public R<Page> getPage(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.eq(Dish::getIsDeleted, 0);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage, "获取成功");
    }

    // 根据id 获取添加页面初始信息
    @GetMapping("/{id}")
    @Cacheable(cacheNames = "dishDetail", key = "#id")
    @ApiOperation(value = "根据id 获取添加页面初始信息")
    @ApiImplicitParam(value = "菜品ID", name = "id", dataType = "String", required = true, dataTypeClass = String.class, paramType = "path")
    public R<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto, "获取成功");
    }

    //  修改商品信息以及口味表
    @PutMapping
    @Caching(evict = {
            @CacheEvict(cacheNames = "dishDetail", key = "#dishDto.id"),
            @CacheEvict(cacheNames = "dishList", key = "#dishDto.categoryId")
    })
    @ApiOperation(value = "修改商品信息以及口味表")
    @ApiImplicitParam(name = "dishDto", value = "菜品数据", dataTypeClass = DishDto.class, paramType = "body", required = true)
    public R<String> updateAndFlavor(@RequestBody DishDto dishDto) {
        dishService.updateAndFlavor(dishDto);
        return R.success(null, "修改成功");
    }

    // 批量修改商品
    @PutMapping("/all")
    @Caching(evict = {
            @CacheEvict(cacheNames = "dishDetail", allEntries = true),
            @CacheEvict(cacheNames = "dishList", allEntries = true)
    })
    @ApiOperation(value = "批量修改商品")
    @ApiImplicitParam(name = "dishDto", value = "菜品数据", dataTypeClass = DishDto.class, paramType = "body", required = true)
    public R<String> updateAllById(@RequestBody DishDto dishDto) {
        List<String> allId = dishDto.getAllId();
        List<Long> collect = allId.stream().map(Long::valueOf).collect(Collectors.toList());
        dishService.updateListById(collect, dishDto);
        return R.success(null, "批量操作成功");
    }

    // 根据id修改商品状态(删除或销售状态)
    @PutMapping("/status")
    @Caching(evict = {
            @CacheEvict(cacheNames = "dishDetail", key = "#dish.id"),
            @CacheEvict(cacheNames = "dishList", key = "#dish.categoryId")
    })
    @ApiOperation(value = "根据id修改商品状态(删除或销售状态)")
    @ApiImplicitParam(name = "dishDto", value = "菜品数据", dataTypeClass = DishDto.class, paramType = "body", required = true)
    @Validated(DishDeleteGroup.class)
    public R<Dish> updateStatus(@Valid @RequestBody Dish dish) {
        dishService.updateById(dish);
        Dish dishInfo = dishService.getById(dish.getId());
        BeanUtils.copyProperties(dishInfo, dish);
        return R.success(dish, "修改成功");
    }

    // 根据菜品分类获取旗下对应的菜品
    @GetMapping("/list")
    @ApiOperation(value = "根据菜品分类获取旗下对应的菜品")
    @Cacheable(cacheNames = "dishList", key = "#dishDto.categoryId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "分类ID", dataType = "String", dataTypeClass = String.class, required = true)
    })
    public R<List<DishDto>> listDish(DishDto dishDto) {
        Long categoryId = dishDto.getCategoryId();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null, Dish::getCategoryId, categoryId);
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> collect = list.stream().map(item -> {
            DishDto dishDto1 = new DishDto();
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> list1 = dishFlavorService.list(queryWrapper1);
            BeanUtils.copyProperties(item, dishDto1);
            dishDto1.setFlavors(list1);
            return dishDto1;
        }).collect(Collectors.toList());
        return R.success(collect, "获取对应菜品成功");
    }
}
