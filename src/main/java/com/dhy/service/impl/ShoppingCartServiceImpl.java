package com.dhy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhy.entity.ShoppingCart;
import com.dhy.mapper.ShoppingCartMapper;
import com.dhy.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
