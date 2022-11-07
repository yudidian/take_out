package com.dhy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品
 */
@Data
@ApiModel(value = "Dish", description = "菜品实体类")
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜品ID")
    private Long id;


    //菜品名称
    @ApiModelProperty("菜品名称")
    private String name;


    //菜品分类id
    @ApiModelProperty("菜品分类id")
    private Long categoryId;


    //菜品价格
    @ApiModelProperty("菜品价格")
    private BigDecimal price;


    //商品码
    @ApiModelProperty("商品码")
    private String code;


    //图片
    @ApiModelProperty("菜品图片")
    private String image;


    //描述信息
    @ApiModelProperty("描述信息")
    private String description;

    // 销量
    @ApiModelProperty("销量")
    private Long salesVolume;


    // 评论条数
    @ApiModelProperty("评论条数")
    private int reviewCount;

    //0 停售 1 起售
    @ApiModelProperty("0 停售 1 起售")
    private Integer status;


    //顺序
    @ApiModelProperty("顺序")
    private Integer sort;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建人")
    private Long createUser;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新人")
    private Long updateUser;


    //是否删除
    @ApiModelProperty("是否删除")
    private Integer isDeleted;

}
