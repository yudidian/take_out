package com.dhy.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ProductReviews implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long dishId;
    private Long setmealId;
    private Long userId;
    private String avatar;
    private int rating;
    private String text;
    private LocalDateTime createTime;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String image;
}
