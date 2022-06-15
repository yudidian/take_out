package com.dhy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dhy.DTO.SetmealDto;
import com.dhy.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
  void saveSetmealAndSetmealDish(SetmealDto setmealDto);
  Page<SetmealDto> setmealPage(int page,int pageSize,String name);
  void updateSetmealAndSetmealDish(SetmealDto setmealDto);
  SetmealDto getMealById(Long id);
  void deleteByIds(List<Long> ids);
}
