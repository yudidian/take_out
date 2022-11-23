package com.dhy.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("商家回复实体类")
public class StoreReviews implements Serializable {
    @ApiModelProperty("ID")
    public Long id;

    @ApiModelProperty("评论ID")
    @NotNull
    @Min(value = 10000000000000000L, message = "ID错误")
    public Long productReviewsId;

    @ApiModelProperty("回复内容")
    @NotBlank(message = "回复内容不能为空")
    public String text;

    @ApiModelProperty("回复时间")
    public LocalDateTime createTime;

    @ApiModelProperty("店家回复标志 0未回复 1回复")
    public int isReview;
}
