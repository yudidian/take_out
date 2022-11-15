package com.dhy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dhy.ApiModel.ReviewsModel;
import com.dhy.DTO.ProductReviewsDto;
import com.dhy.common.R;
import com.dhy.entity.ProductReviews;

import java.util.List;
import java.util.Map;

public interface ProductReviewsService extends IService<ProductReviews> {
    public R<Page<ProductReviewsDto>> getReviewsList(Long dishId, Long setmealId, int reta, Long userId, int page, int pageSize);

    public R<Page<ProductReviewsDto>> getAllReviewsList(Map<String, Object> map);
}
