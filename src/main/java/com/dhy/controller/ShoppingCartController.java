package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dhy.common.R;
import com.dhy.entity.ShoppingCart;
import com.dhy.service.ShoppingCartService;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cart")
@ResponseBody
public class ShoppingCartController {

  @Autowired
  private ShoppingCartService shoppingCartService;

//  添加进购物车
  @PostMapping("/add")
  public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
    Long userId = Long.valueOf(session.getAttribute("userId").toString());
    shoppingCart.setUserId(userId);
    // 查询当前购物车下是否有相同的对应菜品
    LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ShoppingCart::getUserId, userId);
    queryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
    queryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
    ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
    if (cart != null) {
      cart.setNumber(cart.getNumber() + 1);
      cart.setCreateTime(LocalDateTime.now());
      shoppingCartService.updateById(cart);
    } else {
      shoppingCart.setNumber(1);
      shoppingCart.setCreateTime(LocalDateTime.now());
      shoppingCartService.save(shoppingCart);
      cart = shoppingCart;
    }
    return R.success(cart, "添加成功");
  }
//  获取购物车列表

  @GetMapping("/list")
  public R<List<ShoppingCart>> list(HttpSession session) {
    Long userId = Long.valueOf(session.getAttribute("userId").toString());
    LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ShoppingCart::getUserId,userId);
    queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
    List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
    return R.success(list,"用户购物车列表获取成功");
  }
//  清空购物车
  @DeleteMapping("/delete/all")
  public R<String> cleanAll(HttpSession session){
    Long userId = Long.valueOf(session.getAttribute("userId").toString());
    LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ShoppingCart::getUserId, userId);
    shoppingCartService.remove(queryWrapper);
    return R.success(null, "清除成功");
  }
  // 订单数的数量修改
  @PutMapping("/less")
  public R<ShoppingCart> changeCount(@RequestBody ShoppingCart shoppingCart, HttpSession session){
    LambdaQueryWrapper<ShoppingCart> queryWrapper = getUserIdAndIdentificationId(shoppingCart, session);
    ShoppingCart shop = shoppingCartService.getOne(queryWrapper);
    if (shop.getNumber() <= 1) {
      shoppingCartService.remove(queryWrapper);
      return R.success(null, "移出成功");
    } else {
      shop.setNumber(shop.getNumber() - 1);
      shoppingCartService.updateById(shop);
      return R.success(shop, "操作成功");
    }
  }
  @DeleteMapping("/delete/one")
  public R<String> deleteCartList(@RequestBody ShoppingCart shoppingCart, HttpSession session){
    LambdaQueryWrapper<ShoppingCart> queryWrapper = getUserIdAndIdentificationId(shoppingCart, session);
    shoppingCartService.remove(queryWrapper);
    return R.success(null, "操作成功");
  }

  private LambdaQueryWrapper<ShoppingCart> getUserIdAndIdentificationId(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
    Long userId = Long.valueOf(session.getAttribute("userId").toString());
    LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ShoppingCart::getUserId, userId);
    if (shoppingCart.getDishId() != null) {
      queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
    } else {
      queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
    }
    return queryWrapper;
  }
}
