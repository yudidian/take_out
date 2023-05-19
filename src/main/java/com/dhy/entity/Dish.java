package com.dhy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.dhy.ValidatedGroup.DishDeleteGroup;
import com.dhy.ValidatedGroup.DishSaveGroup;
import com.dhy.ValidatedGroup.DishUpdateGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
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
    @NotNull(message = "菜品ID不能为空", groups = {DishDeleteGroup.class, DishUpdateGroup.class})
    private Long id;


    //菜品名称
    @ApiModelProperty("菜品名称")
    @NotNull(message = "菜品名称不能为空", groups = {DishSaveGroup.class})
    private String name;


    //菜品分类id
    @ApiModelProperty("菜品分类id")
    @NotNull(message = "菜品分类id不能为空", groups = {DishSaveGroup.class})
    private Long categoryId;


    //菜品价格
    @ApiModelProperty("菜品价格")
    @NotNull(message = "菜品价格不能为空", groups = {DishSaveGroup.class})
    private BigDecimal price;


    //商品码
    @ApiModelProperty("商品码")
    private String code;


    //图片
    @ApiModelProperty("菜品图片")
    @NotNull(message = "菜品图片不能为空", groups = {DishSaveGroup.class})
    private String image;


    //描述信息
    @ApiModelProperty("描述信息")
    @NotNull(message = "描述信息不能为空", groups = {DishSaveGroup.class})
    private String description;

    // 销量
    @ApiModelProperty("销量")
    private Long salesVolume;


    // 评论条数
    @ApiModelProperty("评论条数")
    private int reviewCount;

    //0 停售 1 起售
    @ApiModelProperty("0 停售 1 起售")
    @Range(min = 0, max = 1, message = "字段错误")
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
    @Range(min = 0, max = 1, message = "字段错误")
    private Integer isDeleted;

}