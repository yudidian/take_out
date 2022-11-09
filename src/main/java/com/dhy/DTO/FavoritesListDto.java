package com.dhy.DTO;

import com.dhy.entity.FavoritesList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("收藏列表超类")
@Data
public class FavoritesListDto extends FavoritesList {

    @ApiModelProperty("菜品ID")
    private Long dishId;

    @ApiModelProperty("套餐ID")
    private Long setmealId;

    @ApiModelProperty("套餐或菜品标识")
    private String type;

    @ApiModelProperty("套餐或者菜品名称")
    private String name;

    @ApiModelProperty("套餐或者菜品图片")
    private String image;
}
