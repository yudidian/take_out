package com.dhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhy.DTO.DishDto;
import com.dhy.common.R;
import com.dhy.entity.Category;
import com.dhy.entity.Dish;
import com.dhy.entity.DishFlavor;
import com.dhy.mapper.CategoryMapper;
import com.dhy.mapper.DishMapper;
import com.dhy.service.CategoryService;
import com.dhy.service.DishFlavorService;
import com.dhy.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
  @Autowired
  private DishFlavorService dishFlavorService;
  @Autowired
  private CategoryMapper categoryMapper;
  @Override
  public void saveDish(DishDto dishDto) {
    this.save(dishDto);
    Long id = dishDto.getId();
    dishDto.getFlavors().forEach(item ->{
      item.setDishId(id);
    });
    dishFlavorService.saveBatch(dishDto.getFlavors());
  }

  @Override
  public DishDto getByIdWithFlavor(Long id) {
    Dish dish = this.getById(id);
    Long categoryId = dish.getCategoryId();
    Category category = categoryMapper.selectById(categoryId);
    DishDto dishDto = new DishDto();
    BeanUtils.copyProperties(dish,dishDto);
    if (category != null) {
      dishDto.setCategoryName(category.getName());
    }
    LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(DishFlavor::getDishId,dish.getId());
    List<DishFlavor> list = dishFlavorService.list(queryWrapper);
    dishDto.setFlavors(list);
    return dishDto;
  }

  @Override
  public void updateAndFlavor(DishDto dishDto) {
    this.updateById(dishDto);
    LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
    dishFlavorService.remove(queryWrapper);
    List<DishFlavor> list = dishDto.getFlavors();
    List<DishFlavor> collect = list.stream().peek(item -> item.setDishId(dishDto.getId())).collect(Collectors.toList());
    dishFlavorService.saveBatch(collect);
  }

  @Override
  public void updateListById(List<Long> list, DishDto dishDto) {
      list.forEach(item->{
        dishDto.setId(item);
        this.updateById(dishDto);
      });
    }
}
