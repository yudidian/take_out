package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhy.common.R;
import com.dhy.entity.GoodsDescription;
import com.dhy.service.GoodsDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods/description")
@ResponseBody
public class GoodsDescriptionController {
    @Autowired
    private GoodsDescriptionService goodsDescriptionService;

    @GetMapping("/list")
    private Page<GoodsDescription> getDescription(int page, int pageSize) {
        Page<GoodsDescription> goodsDescriptionPage = new Page<>(page, pageSize);
        goodsDescriptionService.page(goodsDescriptionPage);
        return goodsDescriptionPage;
    }

    @PostMapping("/change")
    private R<String> addDescription(@RequestBody GoodsDescription goodsDescription) {
        LambdaQueryWrapper<GoodsDescription> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsDescription::getDishId, goodsDescription.getDishId());
        GoodsDescription info = goodsDescriptionService.getOne(queryWrapper);
        if (info == null) {
            goodsDescriptionService.save(goodsDescription);
        } else {
            goodsDescriptionService.updateById(goodsDescription);
        }
        return R.success(null, "success");
    }
}
