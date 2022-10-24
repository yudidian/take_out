package com.dhy.controller;

import com.dhy.common.R;
import com.dhy.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@ResponseBody
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @GetMapping
    private R<String> submitOrders(Long addressBookId, Integer payMethod,String remark, HttpSession session) {
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        return ordersService.generateOrder(addressBookId, payMethod, remark, userId);
    }
}
