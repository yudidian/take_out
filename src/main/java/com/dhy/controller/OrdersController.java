package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhy.DTO.OrdersDto;
import com.dhy.common.R;
import com.dhy.entity.OrderDetail;
import com.dhy.entity.Orders;
import com.dhy.service.OrderDetailService;
import com.dhy.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    // 下单
    @GetMapping
    private R<String> submitOrders(Long addressBookId, Integer payMethod, String remark, HttpSession session) {
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        return ordersService.generateOrder(addressBookId, payMethod, remark, userId);
    }

    // 获取最新订单列表
    @GetMapping("/latest")
    private R<List<OrderDetail>> getLatestList(HttpSession session) {
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        return ordersService.getNewOrdersDetail(userId);
    }

    // 获取订单列表
    @GetMapping("/list")
    private R<Page<OrdersDto>> getOrdersList(int page, int pageSize, int state, HttpSession session) {
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        return ordersService.getAllOrdersList(page, pageSize, state,userId);
    }
    // 删除某个订单

    @DeleteMapping("/{id}")
    private R<String> deleteOneOrders(@PathVariable String id) {
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getNumber, id);
        orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, id);
        ordersService.remove(ordersLambdaQueryWrapper);
        orderDetailService.remove(orderDetailLambdaQueryWrapper);
        return R.success(null, "订单删除成功");
    }
}
