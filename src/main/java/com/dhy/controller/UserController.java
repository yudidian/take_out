package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dhy.common.R;
import com.dhy.entity.AddressBook;
import com.dhy.entity.User;
import com.dhy.service.UserService;
import com.dhy.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@ResponseBody
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/login")
  public R<Map<String, Object>> login(@RequestBody Map<String,Object> map, HttpSession session){
//    if (session.getAttribute("code") == null) {
//      return R.error("验证码未发送");
//    }
//    String code = session.getAttribute("code").toString();
//    long time = (long) session.getAttribute("timeOut");
//    if (new Date().getTime() - time > 1000*60*5) {
//      return R.error("验证码过期");
//    }
//    boolean flag = code.equalsIgnoreCase(map.get("code").toString());
//    if (!flag) {
//      return R.error("验证码错误");
//    }
    String code = "xolu5f";
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(User::getPhone,map.get("phone"));
    User userServiceOne = userService.getOne(queryWrapper);
    if (userServiceOne == null) {
      return R.error("用户未注册");
    }
    HashMap<String, String> jwt = new HashMap<>();
    HashMap<String, Object> info = new HashMap<>();
    jwt.put("phone",userServiceOne.getPhone());
    jwt.put("userId",userServiceOne.getId().toString());
    Long userId = userServiceOne.getId();
    String token = JwtUtils.token(jwt);
    info.put("token",token);;
    info.put("userId",userId);;
    return R.success(info, "登陆成功");
  }
  @PostMapping("/register")
  public R<String> register(@RequestBody User user){
    userService.save(user);
    return R.success(null,"注册成功");
  }
  @GetMapping
  private R<User> getUserInfo(HttpSession session) {
    Long userId = Long.valueOf(session.getAttribute("userId").toString());
    LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
    userLambdaQueryWrapper.eq(User::getId, userId);
    User user = userService.getOne(userLambdaQueryWrapper);
    return R.success(user,"用户信息获取成功");
  }
  // 修改用户信息
  @PutMapping
  private R<String> updateUserInfo(@RequestBody User user) {
    userService.updateById(user);
    return R.success(null, "修改成功");
  }
}
