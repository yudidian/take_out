package com.dhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhy.ApiModel.ReviewsModel;
import com.dhy.DTO.ProductReviewsDto;
import com.dhy.common.R;
import com.dhy.entity.ProductReviews;
import com.dhy.entity.StoreReviews;
import com.dhy.entity.User;
import com.dhy.mapper.ProductReviewsMapper;
import com.dhy.service.ProductReviewsService;
import com.dhy.service.StoreReviewsService;
import com.dhy.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductReviewsImpl extends ServiceImpl<ProductReviewsMapper, ProductReviews> implements ProductReviewsService {
    @Autowired
    private UserService userService;

    @Autowired
    private StoreReviewsService storeReviewsService;

    @Override
    public R<Page<ProductReviewsDto>> getReviewsList(Long dishId, Long setmealId, int reta, Long userId, int page, int pageSize) {
        Page<ProductReviews> productReviewsPage = new Page<>(page, pageSize);
        Page<ProductReviewsDto> productReviewsDtoPage = new Page<>();
        LambdaQueryWrapper<ProductReviews> productReviewsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        productReviewsLambdaQueryWrapper.eq(dishId != null, ProductReviews::getDishId, dishId)
                .eq(setmealId != null, ProductReviews::getSetmealId, setmealId)
                .eq(reta != 0 && reta != -1, ProductReviews::getRating, reta);
        if (reta == -1) {
            productReviewsLambdaQueryWrapper.orderByDesc(ProductReviews::getCreateTime);
        }
        getReviews(productReviewsPage, productReviewsDtoPage, productReviewsLambdaQueryWrapper);
        return R.success(productReviewsDtoPage, "获取评论列表成功");
    }

    private void getReviews(Page<ProductReviews> productReviewsPage, Page<ProductReviewsDto> productReviewsDtoPage, LambdaQueryWrapper<ProductReviews> productReviewsLambdaQueryWrapper) {
        this.page(productReviewsPage, productReviewsLambdaQueryWrapper);
        BeanUtils.copyProperties(productReviewsPage, productReviewsDtoPage, "records");
        List<ProductReviewsDto> collect = productReviewsPage.getRecords().stream().map(item -> {
            User user = userService.getById(item.getUserId());
            LambdaQueryWrapper<StoreReviews> storeReviewsLambdaQueryWrapper = new LambdaQueryWrapper<>();
            storeReviewsLambdaQueryWrapper.eq(StoreReviews::getProductReviewsId, item.getId());
            StoreReviews storeReviews = storeReviewsService.getOne(storeReviewsLambdaQueryWrapper);
            ProductReviewsDto productReviewsDto = new ProductReviewsDto();
            BeanUtils.copyProperties(item, productReviewsDto);
            if (storeReviews != null) {
                productReviewsDto.setStoreReview(storeReviews.getText());
                productReviewsDto.setReviewTime(storeReviews.getCreateTime());
            }
            productReviewsDto.setImages(item.getImage().split(","));
            productReviewsDto.setAvatar(user.getAvatar());
            productReviewsDto.setUsername(user.getName());
            return productReviewsDto;
        }).collect(Collectors.toList());
        productReviewsDtoPage.setRecords(collect);
    }

    @Override
    public R<Page<ProductReviewsDto>> getAllReviewsList(Map<String, Object> map) {
        Page<ProductReviews> productReviewsPage = new Page<>(Long.parseLong(map.get("page").toString()), Long.parseLong(map.get("pageSize").toString()));
        Page<ProductReviewsDto> productReviewsDtoPage = new Page<>();
        LambdaQueryWrapper<ProductReviews> productReviewsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        productReviewsLambdaQueryWrapper.eq(map.get("dishId") != null, ProductReviews::getDishId, (Long) map.get("dishId"))
                .eq(map.get("setmealId") != null, ProductReviews::getSetmealId, (Long) map.get("setmealId"))
                .orderByDesc(ProductReviews::getCreateTime);
        getReviews(productReviewsPage, productReviewsDtoPage, productReviewsLambdaQueryWrapper);
        return R.success(productReviewsDtoPage, "获取成功");
    }
}
