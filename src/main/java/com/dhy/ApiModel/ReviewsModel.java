package com.dhy.ApiModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "获取全部评论Model")
public class ReviewsModel {
    @ApiModelProperty("菜品ID")
    public Long dishId;

    @ApiModelProperty("套餐ID")
    public Long setmealId;

    @ApiModelProperty("当前页")
    public int page;

    @ApiModelProperty("每页数量")
    public int pageSize;
}
