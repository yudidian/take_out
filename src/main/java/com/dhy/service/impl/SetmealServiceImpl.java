package com.dhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhy.DTO.SetmealDto;
import com.dhy.common.CustomException;
import com.dhy.entity.Category;
import com.dhy.entity.Setmeal;
import com.dhy.entity.SetmealDish;
import com.dhy.mapper.SetmealMapper;
import com.dhy.service.CategoryService;
import com.dhy.service.SetmealDishService;
import com.dhy.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
  @Autowired
  private SetmealDishService setmealDishService;
  @Autowired
  @Lazy
  private CategoryService categoryService;
  @Override
  public void saveSetmealAndSetmealDish(SetmealDto setmealDto) {
    //保存套餐
    this.save(setmealDto);
    List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
    List<SetmealDish> collect = setmealDishes.stream().peek(item -> {
      item.setSetmealId(setmealDto.getId());
    }).collect(Collectors.toList());
    setmealDishService.saveBatch(collect);
  }

  @Override
  public Page<SetmealDto> setmealPage(int page, int pageSize, String name) {
    Page<Setmeal> setmealPage = new Page<>(page,pageSize);
    Page<SetmealDto> setmealDtoPage = new Page<>();
    LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.like(name != null, Setmeal::getName,name);
    this.page(setmealPage,queryWrapper);
    BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
    List<SetmealDto> setmealDtos = setmealPage.getRecords().stream().map(item -> {
      SetmealDto setmealDto = new SetmealDto();
      BeanUtils.copyProperties(item, setmealDto);
      Category category = categoryService.getById(item.getCategoryId());
      setmealDto.setCategoryName(category.getName());
      return setmealDto;
    }).collect(Collectors.toList());
    setmealDtoPage.setRecords(setmealDtos);
    return setmealDtoPage;
  }

  @Override
  public void updateSetmealAndSetmealDish(SetmealDto setmealDto) {
    this.updateById(setmealDto);
    LambdaUpdateWrapper<SetmealDish> updateWrapper = new LambdaUpdateWrapper<>();
    setmealDishService.updateBatchById(setmealDto.getSetmealDishes());
  }

  @Override
  public SetmealDto getMealById(Long id) {
    SetmealDto setmealDto = new SetmealDto();
    Setmeal setmeal = this.getById(id);
    Category category = categoryService.getById(setmeal.getCategoryId());
    BeanUtils.copyProperties(setmeal,setmealDto);
    LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
    queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
    List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
    setmealDto.setSetmealDishes(setmealDishes);
    setmealDto.setCategoryName(category.getName());
    return setmealDto;
  }

  @Override
  public void deleteByIds(List<Long> ids) {
    LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.in(Setmeal::getId,ids);
    queryWrapper.eq(Setmeal::getStatus,1);
    int count = this.count(queryWrapper);
    if (count > 0) {
      throw new CustomException("有商品售卖中不能删除");
    }
    this.removeByIds(ids);
    LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper<>();
    queryWrapper2.in(SetmealDish::getSetmealId,ids);
    setmealDishService.remove(queryWrapper2);
  }
}
