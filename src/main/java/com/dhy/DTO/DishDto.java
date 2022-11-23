package com.dhy.DTO;


import com.dhy.ValidatedGroup.DishDeleteGroup;
import com.dhy.ValidatedGroup.DishSaveGroup;
import com.dhy.entity.Dish;
import com.dhy.entity.DishFlavor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "DishDto", description = "菜品超类")
public class DishDto extends Dish {

    @ApiModelProperty("口味数据")
    @NotNull(message = "口味数据不能为空", groups = {DishSaveGroup.class})
    private List<DishFlavor> flavors = new ArrayList<>();

    @ApiModelProperty("分类名称")
    @NotNull(message = "分类名称不能为空", groups = {DishSaveGroup.class})
    private String categoryName;

    @ApiModelProperty("批量删除时用户ID集合")
    @NotNull(message = "批量删除时用户ID集合不能为空", groups = {DishDeleteGroup.class})
    private List<String> allId = new ArrayList<>();

    @ApiModelProperty("分类type")
    @NotNull(message = "分类type不能为空", groups = {DishSaveGroup.class})
    private String type;

    @ApiModelProperty("x")
    private Integer copies;
}
