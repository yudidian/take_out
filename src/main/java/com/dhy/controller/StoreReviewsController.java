package com.dhy.controller;

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
            @ApiImplicitParam(name="id",value = "回复评论的ID", dataType = "String", paramType = "body", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name="text", value = "回复时间", dataType = "String", paramType = "body", required = true, dataTypeClass = String.class)
    })
    private R<String> reviewUser(@ApiIgnore @RequestBody @Validated StoreReviews storeReviews) {
        storeReviews.setCreateTime(LocalDateTime.now());
        storeReviewsService.save(storeReviews);
        return R.success(null, "回复成功");
    }
}
