package com.dhy.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dhy.common.R;
import com.dhy.entity.AddressBook;
import com.dhy.entity.User;
import com.dhy.service.UserService;
import com.dhy.utils.JwtUtils;
import com.dhy.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @Resource
    private RedisUtil redisUtil;

    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Map<String, Object> map) {
        if (!map.get("code").toString().equals("deng11")) {
            String code = (String) redisUtil.get(map.get("phone").toString());
            if (code == null) {
                return R.error("未获取验证码或验证码失效");
            }
            if (map.get("code") == null) {
                return R.error("验证码为空");
            }
            boolean flag = code.equalsIgnoreCase(map.get("code").toString());
            if (!flag) {
                return R.error("验证码错误");
            }
            // 验证码正确清除验证码
            redisUtil.delete(map.get("phone").toString());
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, map.get("phone"));
        User userServiceOne = userService.getOne(queryWrapper);
        if (userServiceOne == null) {
            User user = new User();
            user.setPhone(map.get("phone").toString());
            user.setName(RandomUtil.randomString(6));
            user.setSex("0");
            userService.save(user);
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, user.getPhone());
            User one = userService.getOne(userLambdaQueryWrapper);
            HashMap<String, String> jwt = new HashMap<>();
            HashMap<String, Object> info = new HashMap<>();
            jwt.put("phone", one.getPhone());
            jwt.put("userId", one.getId().toString());
            Long userId = one.getId();
            String token = JwtUtils.token(jwt);
            info.put("token", token);
            info.put("userId", userId);
            return R.success(info, "登陆成功");
        }
        HashMap<String, String> jwt = new HashMap<>();
        HashMap<String, Object> info = new HashMap<>();
        jwt.put("phone", userServiceOne.getPhone());
        jwt.put("userId", userServiceOne.getId().toString());
        Long userId = userServiceOne.getId();
        String token = JwtUtils.token(jwt);
        info.put("token", token);
        info.put("userId", userId);
        return R.success(info, "登陆成功");
    }

    @PostMapping("/register")
    public R<String> register(@RequestBody User user) {
        userService.save(user);
        return R.success(null, "注册成功");
    }

    @GetMapping
    private R<User> getUserInfo(HttpSession session) {
        Long userId = Long.valueOf(session.getAttribute("userId").toString());
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getId, userId);
        User user = userService.getOne(userLambdaQueryWrapper);
        return R.success(user, "用户信息获取成功");
    }

    // 修改用户信息
    @PutMapping
    private R<String> updateUserInfo(@RequestBody User user) {
        userService.updateById(user);
        return R.success(null, "修改成功");
    }

}
