package com.dhy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dhy.DTO.DishDto;
import com.dhy.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
  // 添加商品
  void saveDish(DishDto dishDto);
  // 根据id 获取相对应的商品信息
  DishDto getByIdWithFlavor(Long id);

  // 修改商品信息以及口味
  void updateAndFlavor(DishDto dishDto);

  void updateListById(List<Long> list,DishDto dishDto);
}
