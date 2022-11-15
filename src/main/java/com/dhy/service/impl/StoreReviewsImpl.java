package com.dhy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhy.entity.StoreReviews;
import com.dhy.mapper.StoreReviewsMapper;
import com.dhy.service.StoreReviewsService;
import org.springframework.stereotype.Service;

@Service
public class StoreReviewsImpl extends ServiceImpl<StoreReviewsMapper, StoreReviews> implements StoreReviewsService {
}
