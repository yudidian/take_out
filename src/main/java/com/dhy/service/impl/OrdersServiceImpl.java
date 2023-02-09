package com.dhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhy.DTO.OrdersDto;
import com.dhy.common.R;
import com.dhy.entity.*;
import com.dhy.mapper.OrdersMapper;
import com.dhy.service.*;
import com.dhy.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public R<String> generateOrder(Long addressId, Integer payMethods, String remark, Long userId) {
        Orders orders = new Orders();
        BigDecimal amount = BigDecimal.valueOf(0);
        LambdaQueryWrapper<ShoppingCart> queryWrapperShopping = new LambdaQueryWrapper<>();
        User user = userService.getById(userId);
        AddressBook addressBook = addressBookService.getById(addressId);
        queryWrapperShopping.eq(ShoppingCart::getUserId, userId);
        orders.setNumber(UUIDUtils.getUUID());
        orders.setUserId(userId);
        orders.setAddressBookId(addressId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayMethod(payMethods);
        orders.setRemark(remark);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserName(user.getName());
        // 没有支付功能所有订单全部为待派送
        orders.setStatus(2);
        // 查询购物车
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapperShopping);
        for (ShoppingCart shopp : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(shopp.getName());
            orderDetail.setOrderId(orders.getNumber());
            orderDetail.setImage(shopp.getImage());
            orderDetail.setDishId(shopp.getDishId());
            orderDetail.setAmount(shopp.getAmount());
            orderDetail.setSetmealId(shopp.getSetmealId());
            if (shopp.getDishId() != null) {
                Dish dish = dishService.getById(shopp.getDishId());
                dish.setSalesVolume(shopp.getNumber() + dish.getSalesVolume());
                dishService.updateById(dish);
            }
            if(shopp.getSetmealId() != null) {
                Setmeal setmeal = setmealService.getById(shopp.getSetmealId());
                setmeal.setSalesVolume(shopp.getNumber() + setmeal.getSalesVolume());
                setmealService.updateById(setmeal);
            }
            amount = amount.add(BigDecimal.valueOf(shopp.getNumber()).multiply(new BigDecimal(String.valueOf(shopp.getAmount()))));
            orderDetailService.save(orderDetail);
        }
        orders.setAmount(amount);
        // 添加数据
        this.save(orders);
        // 移出购物车数据
        shoppingCartService.remove(queryWrapperShopping);
        return R.success(null, "下单成功");
    }

    @Override
    public R<List<OrderDetail>> getNewOrdersDetail(Long userId) {
        // 1获取该用户下的最新订单列
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        queryWrapper.last("limit 1");
        Orders orders = this.getOne(queryWrapper);
        if (orders == null) {
            return R.success(null, "暂无订单");
        }
        // 获取订单号
        String number = orders.getNumber();
        // 根据订单号查询商品
        LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, number);
        List<OrderDetail> list = orderDetailService.list(orderDetailLambdaQueryWrapper);

        return R.success(list, "最新订单列表获取成功");
    }

    @Override
    public R<Page<OrdersDto>> getAllOrdersList(int page, int size, int state, Long userId) {
        Page<Orders> pageList = new Page<>(page, size);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // state 1待付款，2待派送，3已派送，4已完成
        ordersLambdaQueryWrapper.eq(Orders::getUserId, userId).orderByDesc(Orders::getOrderTime);
        if (state == 4) {
            // 查询历史订单（已完成）
            ordersLambdaQueryWrapper.eq(Orders::getStatus, state);
        } else {
            // 查询待派送，已派送订单
            ordersLambdaQueryWrapper.ne(Orders::getStatus, 4);
        }
        return getPageR(pageList, ordersDtoPage, ordersLambdaQueryWrapper);
    }

    @Override
    public R<String> confirmOrCancelOrders(Boolean flag, String number) {
        // 确认收货
        if (flag) {
            LambdaUpdateWrapper<Orders> ordersLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            ordersLambdaUpdateWrapper.eq(Orders::getNumber,number)
                    .set(Orders::getStatus, 4)
                    .set(Orders::getOrderCompleteTime, new Date());
            this.update(ordersLambdaUpdateWrapper);
            return R.success(null, "确认收货完成");
        } else {
            // 取消订单
            LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
            ordersLambdaQueryWrapper.eq(Orders::getNumber, number);
            this.remove(ordersLambdaQueryWrapper);
            // 移除订单详情
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, number);
            orderDetailService.remove(orderDetailLambdaQueryWrapper);
            return R.success(null, "取消订单成功");
        }
    }
    @Override
    public R<Page<OrdersDto>> manageGetAllOrdersList(int page, int size, int state) {
        Page<Orders> pageList = new Page<>(page, size);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // state 1待付款，2待派送，3已派送，4已完成
        ordersLambdaQueryWrapper.orderByDesc(Orders::getOrderTime);
        if (state != 0) {
            // 查询历史订单（已完成）
            ordersLambdaQueryWrapper.eq(Orders::getStatus, state);
        }
        return getPageR(pageList, ordersDtoPage, ordersLambdaQueryWrapper);
    }

    private R<Page<OrdersDto>> getPageR(Page<Orders> pageList, Page<OrdersDto> ordersDtoPage, LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper) {
        List<Orders> ordersList = this.page(pageList, ordersLambdaQueryWrapper).getRecords();
        BeanUtils.copyProperties(pageList, ordersDtoPage, "records");
        List<OrdersDto> collect = ordersList.stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, item.getNumber());
            List<OrderDetail> list = orderDetailService.list(orderDetailLambdaQueryWrapper);
            ordersDto.setOrderDetailList(list);
            return ordersDto;
        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(collect);
        return R.success(ordersDtoPage, "订单列表获取成功");
    }
}
