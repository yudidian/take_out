package com.dhy.DTO;

import com.dhy.entity.ProductReviews;
import lombok.Data;

import java.lang.reflect.Array;

@Data
public class ProductReviewsDto extends ProductReviews {
    private String username;
    private String[] images;
}
