package com.dhy.DTO;

import com.dhy.entity.Setmeal;
import com.dhy.entity.SetmealDish;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {
  private List<SetmealDish> setmealDishes = new ArrayList<>();
  private List<String> ids = new ArrayList<>();
  private String categoryName;
}
