package com.dhy.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(value = "轮播图实体类")
public class Swiper {
    public Long id;

    @ApiModelProperty(value = "图片地址")
    @NotNull(message = "图片链接不能为空")
    public String imageUrl;

    @ApiModelProperty(value = "创建时间")
    public Date createTime;

    @ApiModelProperty(value = "创建人ID")
    public Long createUser;

    @ApiModelProperty(value = "顺序")
    public int sort;
}
