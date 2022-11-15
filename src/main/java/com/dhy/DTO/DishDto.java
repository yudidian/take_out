package com.dhy.DTO;


import com.dhy.entity.Dish;
import com.dhy.entity.DishFlavor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "DishDto", description = "菜品超类")
public class DishDto extends Dish {

    @ApiModelProperty("口味数据")
    private List<DishFlavor> flavors = new ArrayList<>();

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("批量删除时用户ID集合")
    private List<String> allId = new ArrayList<>();

    @ApiModelProperty("分类type")
    private String type;

    @ApiModelProperty("x")
    private Integer copies;
}
