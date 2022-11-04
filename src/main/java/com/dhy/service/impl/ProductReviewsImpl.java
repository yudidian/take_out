package com.dhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhy.DTO.ProductReviewsDto;
import com.dhy.common.R;
import com.dhy.entity.ProductReviews;
import com.dhy.entity.User;
import com.dhy.mapper.ProductReviewsMapper;
import com.dhy.service.ProductReviewsService;
import com.dhy.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductReviewsImpl extends ServiceImpl<ProductReviewsMapper, ProductReviews> implements ProductReviewsService {
    @Autowired
    private UserService userService;

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
        this.page(productReviewsPage, productReviewsLambdaQueryWrapper);
        BeanUtils.copyProperties(productReviewsPage, productReviewsDtoPage, "records");
        List<ProductReviewsDto> collect = productReviewsPage.getRecords().stream().map(item -> {
            User user = userService.getById(item.getUserId());
            ProductReviewsDto productReviewsDto = new ProductReviewsDto();
            BeanUtils.copyProperties(item, productReviewsDto);
            productReviewsDto.setImages(item.getImage().split(","));
            productReviewsDto.setAvatar(user.getAvatar());
            productReviewsDto.setUsername(user.getName());
            return productReviewsDto;
        }).collect(Collectors.toList());
        productReviewsDtoPage.setRecords(collect);
        return R.success(productReviewsDtoPage, "获取评论列表成功");
    }
}
