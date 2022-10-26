package com.dhy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dhy.DTO.OrdersDto;
import com.dhy.common.R;
import com.dhy.entity.OrderDetail;
import com.dhy.entity.Orders;
import java.util.List;

public interface OrdersService extends IService<Orders> {
    public R<String> generateOrder(Long addressId, Integer payMethods, String remark, Long userId);

    public R<List<OrderDetail>> getNewOrdersDetail(Long userId);

    public R<Page<OrdersDto>> getAllOrdersList(int page, int size, Long userId);
}
