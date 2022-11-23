package com.dhy.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "ProductReviews", description = "评论实体类")
public class ProductReviews implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("普通菜品ID")
    private Long dishId;

    @ApiModelProperty("套餐ID")
    private Long setmealId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("评分等级")
    private int rating;

    @ApiModelProperty("评论类容")
    private String text;

    @ApiModelProperty("评论时间")
    private LocalDateTime createTime;

    @ApiModelProperty("评论图片")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String image;
}
