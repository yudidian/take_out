package com.dhy.DTO;


import com.dhy.entity.Dish;
import com.dhy.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private List<String> allId = new ArrayList<>();

    private String type;

    private Integer copies;
}
