package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dhy.common.R;
import com.dhy.entity.Swiper;
import com.dhy.service.SwiperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/swiper")
@ResponseBody
public class SwiperController {

    @Autowired
    private SwiperService swiperService;

    @GetMapping()
    private R<List<Swiper>> getSwiperList() {
        LambdaQueryWrapper<Swiper> swiperLambdaQueryWrapper = new LambdaQueryWrapper<>();
        swiperLambdaQueryWrapper.orderByAsc(Swiper::getSort).orderByDesc(Swiper::getCreateTime);
        List<Swiper> list = swiperService.list(swiperLambdaQueryWrapper);
        return R.success(list,"获取成功");
    }

    @PostMapping
    private R<String> addSwiperList(@Validated @RequestBody Swiper swiper, HttpSession session) {
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        swiper.setCreateUser(userId);
        swiper.setCreateTime(new Date());
        swiperService.save(swiper);
        return R.success(null,"添加成功");
    }

    @PutMapping
    private R<String> updateSwiperList(@Validated @RequestBody Swiper swiper, HttpSession session) {
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        swiper.setCreateUser(userId);
        swiper.setCreateTime(new Date());
        swiperService.updateById(swiper);
        return R.success(null,"修改成功");
    }
    @DeleteMapping
    private R<String> deleteSwiperList(Long id) {
        swiperService.removeById(id);
        return R.success(null,"删除成功");
    }
}
