package com.dhy.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SalesVolume implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    // 菜品id
    private Long dishId;
    private Long quantity;
}
