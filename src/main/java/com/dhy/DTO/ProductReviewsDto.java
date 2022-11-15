package com.dhy.DTO;

import com.dhy.entity.ProductReviews;
import lombok.Data;

import java.lang.reflect.Array;
import java.time.LocalDateTime;

@Data
public class ProductReviewsDto extends ProductReviews {
    // 用户名
    private String username;
    private String[] images;
    // 回复类容
    private String storeReview;
    // 回复时间
    private LocalDateTime reviewTime;
}
