package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhy.DTO.OrdersDto;
import com.dhy.common.R;
import com.dhy.entity.OrderDetail;
import com.dhy.entity.Orders;
import com.dhy.service.OrderDetailService;
import com.dhy.service.OrdersService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/unfinished")
    private R<Map<String, Object>> getUnfinishedOrders(HttpSession session) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper =new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId,userId).eq(Orders::getStatus,2);
        List<Orders> list = ordersService.list(ordersLambdaQueryWrapper);
        hashMap.put("size", list.size());
        return R.SuccessPlus(hashMap,"待收货数量获取成功");
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
    // 确认或取消订单
    @GetMapping("/confirmOrCancel")
    private R<String> confirmOrCancelOrders(String ordersId, Boolean flag) {
        return ordersService.confirmOrCancelOrders(flag,ordersId);
    }
    // 获取订单状态  1待付款，2待派送，3已派送，4已完成
    @GetMapping("/state")
    @ApiOperation(value = "获取订单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单编号", name="number", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(value = "发货标志", name="flag", required = true, dataTypeClass = int.class)
    })
    private R<Map<String,Object>> getOrders(String number, int flag) {
        // 0 获取当前订单状态， 1 卖家发货
        HashMap<String, Object> hashMap = new HashMap<>();
        if (flag == 0) {
            LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
            ordersLambdaQueryWrapper.eq(Orders::getNumber,number);
            Orders orders = ordersService.getOne(ordersLambdaQueryWrapper);
            hashMap.put("state", orders.getStatus());
            return R.SuccessPlus(hashMap, "当前订单状态获取成功");
        } else {
            LambdaUpdateWrapper<Orders> ordersLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            ordersLambdaUpdateWrapper.eq(Orders::getNumber,number).set(Orders::getStatus,3);
            ordersService.update(ordersLambdaUpdateWrapper);
            return R.SuccessPlus(hashMap, "已发货");
        }
    }
    // 获取新订单
    @GetMapping("/new/count")
    @ApiOperation(value = "获取新订单")
    private R<Integer> getNewsOrder() {
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getStatus, 2);
        int count = ordersService.count(ordersLambdaQueryWrapper);
        return R.success(count, "获取成功");
    }
    @GetMapping("/manage/list")
    @ApiOperation(value = "管理页面获取订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页", name="page", required = true, dataTypeClass = int.class),
            @ApiImplicitParam(value = "每页数量", name="pageSize", required = true, dataTypeClass = int.class),
            @ApiImplicitParam(value = "订单的状态", name="state", required = true, dataTypeClass = int.class)
    })
    private R<Page<OrdersDto>> manageGetAllOrdersList(int page, int pageSize, int state) {
        return ordersService.manageGetAllOrdersList(page, pageSize, state);
    }
    @GetMapping("/cancel")
    @ApiOperation(value = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单号", name="number", required = true, dataTypeClass = String.class),
    })
    private R<Map<String, Object>> manageGetAllOrdersList(String number) {
        HashMap<String, Object> hashMap = new HashMap<>();
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getNumber, number);
        Orders orders = ordersService.getOne(ordersLambdaQueryWrapper);
        if (orders.getStatus() != 2) {
            hashMap.put("cancel", false);
            return R.success(hashMap, "订单已不能取消");
        } else {
            // 取消订单
            ordersService.removeById(orders.getId());
            hashMap.put("cancel", true);
            return R.success(hashMap, "订单已取消");
        }
    }
}
