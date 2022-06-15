package com.dhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhy.common.CustomException;
import com.dhy.entity.Category;
import com.dhy.entity.Dish;
import com.dhy.entity.Setmeal;
import com.dhy.mapper.CategoryMapper;
import com.dhy.service.CategoryService;
import com.dhy.service.DishService;
import com.dhy.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
  @Autowired
  private DishService dishService;
  @Autowired
  private SetmealService setmealService;
  @Autowired
  private CategoryMapper categoryMapper;
  @Override
  public void myRemove(Long id) {
    // 查询是否有相关联的菜品
    LambdaQueryWrapper<Dish> DQueryWrapper = new LambdaQueryWrapper<>();
    DQueryWrapper.eq(Dish::getCategoryId,id);
    int DCount = dishService.count(DQueryWrapper);
    if (DCount>0){
      throw new CustomException("改分类有相关联菜品，不能删除");
    }
    LambdaQueryWrapper<Setmeal> SQueryWrapper = new LambdaQueryWrapper<>();
    SQueryWrapper.eq(Setmeal::getCategoryId,id);
    int SCount = setmealService.count(SQueryWrapper);
    if (SCount>0){
      throw new CustomException("改分类有相关联套餐，不能删除");
    }
    categoryMapper.deleteById(id);
  }
}
