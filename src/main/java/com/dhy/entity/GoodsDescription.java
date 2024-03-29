package com.dhy.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsDescription implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Long dishId;
    private String image;
    private String description;
    private String mainMaterial;
    private String productionMethod;
    private int count;
    private String taste;
}
