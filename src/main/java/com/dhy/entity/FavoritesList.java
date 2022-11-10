package com.dhy.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("收藏列表")
public class FavoritesList implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主表ID")
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("菜品ID")
    private Long dishId;

    @ApiModelProperty("套餐ID")
    private Long setmealId;

    @ApiModelProperty("创建时间")
    private LocalDateTime creatTime;
}
