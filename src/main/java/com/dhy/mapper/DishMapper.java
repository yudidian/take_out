package com.dhy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhy.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
