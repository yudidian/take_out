package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhy.DTO.DishDto;
import com.dhy.common.R;
import com.dhy.entity.Category;
import com.dhy.entity.Dish;
import com.dhy.entity.DishFlavor;
import com.dhy.service.CategoryService;
import com.dhy.service.DishFlavorService;
import com.dhy.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@ResponseBody
@Slf4j
public class DishController {

  @Autowired
  private DishService dishService;
  @Autowired
  private CategoryService categoryService;

  @Autowired
  private DishFlavorService dishFlavorService;

  // 添加菜品
  @PostMapping
  @CacheEvict(value = "dish", key = "#dishDto.categoryId + '1'")
  public R<String> addDish(@RequestBody DishDto dishDto) {
    dishService.saveDish(dishDto);
    return R.success(null, "添加成功");
  }

  // 分页查询
  @GetMapping("/page")
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
  @Cacheable(value = "dish", key = "#id + '1'")
  public R<DishDto> getById(@PathVariable Long id) {
    DishDto dishDto = dishService.getByIdWithFlavor(id);
    return R.success(dishDto, "获取成功");
  }

  //  修改商品信息以及口味表
  @PutMapping
  @CacheEvict(value = "dish", key = "#dishDto.categoryId + '1'")
  public R<String> updateAndFlavor(@RequestBody DishDto dishDto) {
    dishService.updateAndFlavor(dishDto);
    return R.success(null, "修改成功");
  }

  // 批量修改商品
  @PutMapping("/all")
  @CacheEvict(value = "dish", key = "#dishDto.categoryId + '1'")
  public R<String> updateAllById(@RequestBody DishDto dishDto) {
    List<String> allId = dishDto.getAllId();
    List<Long> collect = allId.stream().map(Long::valueOf).collect(Collectors.toList());
    dishService.updateListById(collect,dishDto);
    return R.success(null,"批量操作成功");
  }
  // 根据id修改商品状态(删除或销售状态)
  @PutMapping("/status")
  @CacheEvict(value = "dish", key = "#dishDto.categoryId + '1'")
  public R<String> updateStatus(@RequestBody DishDto dishDto) {
    boolean flag = dishService.updateById(dishDto);
    if (flag){
      return R.success(null, "修改成功");
    }else {
      return R.error("修改商品状态失败");
    }
  }
  // 根据菜品分类获取旗下对应的菜品 (开启缓存)
  @GetMapping("/list")
  @Cacheable(value = "dish", key = "#dishDto.categoryId + '1'")
  public R<List<DishDto>> listDish(DishDto dishDto){
    Long categoryId = dishDto.getCategoryId();
    LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(categoryId !=null, Dish::getCategoryId, categoryId);
    queryWrapper.eq(Dish::getStatus,1);
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
    return R.success(collect,"获取对应菜品成功");
  }
}
