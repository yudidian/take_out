package com.dhy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhy.common.R;
import com.dhy.entity.*;
import com.dhy.mapper.OrdersMapper;
import com.dhy.service.*;
import com.dhy.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
        orders.setAddress(addressBook.getProvinceName()+addressBook.getCityName()+addressBook.getDistrictName()+addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserName(user.getName());
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
}
