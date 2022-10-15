package com.dhy.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class FavoritesList implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private Long dishId;
}
