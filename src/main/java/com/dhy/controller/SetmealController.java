package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhy.DTO.SetmealDto;
import com.dhy.common.R;
import com.dhy.entity.Category;
import com.dhy.entity.Setmeal;
import com.dhy.service.SetmealDishService;
import com.dhy.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@ResponseBody
public class SetmealController {
  @Autowired
  private SetmealService setmealService;

  // 添加套餐
  @PostMapping
  @CacheEvict(value = "setMeal", key = "#setmealDto.id")
  public R<String> addSetmeal(@RequestBody SetmealDto setmealDto) {
    setmealService.saveSetmealAndSetmealDish(setmealDto);
    return R.success(null, "套餐添加成功");
  }

  // 获取套餐分页数据
  @GetMapping("/page")
  public R<Page<SetmealDto>> page(int page, int pageSize, String name) {
    Page<SetmealDto> setmealDtoPage = setmealService.setmealPage(page, pageSize, name);
    return R.success(setmealDtoPage, "套餐分页数据获取成功");
  }

  // 修改套餐
  @PutMapping
  @CacheEvict(value = "setMeal", key = "#setmealDto.id")
  public R<String> updateMeal(@RequestBody SetmealDto setmealDto) {
    setmealService.updateSetmealAndSetmealDish(setmealDto);
    return R.success(null, "修改成功");
  }

  // 根据id获取对应套餐信息
  @GetMapping("/{id}")
  @Cacheable(value = "setMeal", key = "#id")
  public R<SetmealDto> getMealById(@PathVariable Long id) {
    SetmealDto setmealDto = setmealService.getMealById(id);
    return R.success(setmealDto, "对应信息获取成功");
  }

  // 批量操作
  @PutMapping("/list")
  public R<String> updateList(@RequestBody SetmealDto setmealDto) {
    LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper.in(Setmeal::getId, setmealDto.getIds());
    setmealService.update(setmealDto, updateWrapper);
    return R.success(null, "修改成功");
  }

  @DeleteMapping("/list")
  public R<String> deleteList(@RequestParam List<Long> ids) {
    setmealService.deleteByIds(ids);
    return R.success(null, "删除成功");
  }

  @GetMapping("/list")
  @Cacheable(value = "setMealList", key = "#setmeal.categoryId + '1'")
  public R<List<Setmeal>> getSetmealList(Setmeal setmeal) {
    LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
    lambdaQueryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
    lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
    List<Setmeal> list = setmealService.list(lambdaQueryWrapper);
    return R.success(list,"获取成功");
  }
}
