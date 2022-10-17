package com.dhy.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhy.entity.GoodsDescription;
import com.dhy.mapper.GoodsDescriptionMapper;
import org.springframework.stereotype.Service;

@Service
public class GoodsDescriptionImpl extends ServiceImpl<GoodsDescriptionMapper, GoodsDescription> implements GoodsDescriptionService{
}
