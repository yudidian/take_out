package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dhy.common.R;
import com.dhy.entity.StoreReviews;
import com.dhy.service.StoreReviewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Api(tags = "店家回复相关接口")
@RestController
@RequestMapping("/store/reviews")
@ResponseBody
public class StoreReviewsController {
    @Autowired
    private StoreReviewsService storeReviewsService;

    @PostMapping()
    @ApiOperation(value = "回复用户评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name="productReviewsId",value = "回复评论的ID", dataType = "String", paramType = "body", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name="text", value = "回复时间", dataType = "String", paramType = "body", required = true, dataTypeClass = String.class)
    })
    private R<String> reviewUser(@ApiIgnore @RequestBody @Validated StoreReviews storeReviews) {
        LambdaQueryWrapper<StoreReviews> storeReviewsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        storeReviewsLambdaQueryWrapper.eq(StoreReviews::getProductReviewsId, storeReviews.getProductReviewsId());
        StoreReviews reviews = storeReviewsService.getOne(storeReviewsLambdaQueryWrapper);
        if (reviews != null) {
            storeReviewsService.update(storeReviews,storeReviewsLambdaQueryWrapper);
            return R.error("修改成功");
        }
        storeReviews.setCreateTime(LocalDateTime.now());
        storeReviews.setIsReview(1);
        storeReviewsService.save(storeReviews);
        return R.success(null, "回复成功");
    }

    @ApiOperation(value = "修改店家回复")
    @ApiImplicitParams({
            @ApiImplicitParam(name="productReviewsId",value = "回复评论的ID", dataType = "String", paramType = "body", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name="text", value = "回复内容", dataType = "String", paramType = "body", required = true, dataTypeClass = String.class)
    })
    @PutMapping()
    private R<String> editReview(@RequestBody @Validated StoreReviews storeReviews) {
        LambdaQueryWrapper<StoreReviews> storeReviewsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        storeReviewsLambdaQueryWrapper.eq(StoreReviews::getProductReviewsId, storeReviews.getProductReviewsId());
        storeReviewsService.update(storeReviews,storeReviewsLambdaQueryWrapper);
        return R.success(null, "修改成功");
    }

    @ApiOperation(value = "删除店家回复")
    @ApiImplicitParam(name="productReviewsId",value = "回复评论的ID", dataType = "String", paramType = "body", required = true, dataTypeClass = String.class)
    @DeleteMapping("/{id}")
    private R<String> deleteReview(@PathVariable @Min(value = 10000000000000000L, message = "ID错误") @NotNull(message = "ID不能为空") Long id) {
        LambdaQueryWrapper<StoreReviews> storeReviewsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        storeReviewsLambdaQueryWrapper.eq(StoreReviews::getProductReviewsId, id);
        storeReviewsService.remove(storeReviewsLambdaQueryWrapper);
        return R.success(null, "删除成功");
    }

}
