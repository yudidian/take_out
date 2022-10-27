package com.dhy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhy.entity.ProductReviews;
import com.dhy.mapper.ProductReviewsMapper;
import com.dhy.service.ProductReviewsService;
import org.springframework.stereotype.Service;

@Service
public class ProductReviewsImpl extends ServiceImpl<ProductReviewsMapper, ProductReviews> implements ProductReviewsService {
}
