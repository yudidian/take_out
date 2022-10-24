package com.dhy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dhy.common.R;
import com.dhy.entity.Orders;

import javax.servlet.http.HttpSession;

public interface OrdersService extends IService<Orders> {
    public R<String> generateOrder(Long addressId, Integer payMethods, String remark, Long userId);
}
