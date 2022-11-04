package com.dhy.utils;

import com.dhy.common.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ValidateCodeUtil {
    //Random 不是密码学安全的，加密相关的推荐使用 SecureRandom
    private static final Random RANDOM = new SecureRandom();

    private static final String randomString = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWSYZ";

    @Resource
    private RedisUtil redisUtil;

    /**
     * 生成6位随机数字
     *
     * @return 返回6位数字验证码
     */
    public String getCode(String email) {
        String code = (String) redisUtil.get(email);
        char[] nonceChars = new char[6];
        // 验证码为空说明要么没有获取要么失效
        Integer sendCount = (Integer) redisUtil.get("sendCount"+email);
        // 每天获取三次验证码
        if (sendCount == null || sendCount < 4) {
            if (code == null) {
                for (int index = 0; index < nonceChars.length; ++index) {
                    nonceChars[index] = randomString.charAt(RANDOM.nextInt(randomString.length()));
                }
                redisUtil.set(email, new String(nonceChars), 5*60);
                redisUtil.set("sendEmailTime"+email, String.valueOf(new Date().getTime()), 60);
                redisUtil.set("sendCount"+email, String.valueOf((sendCount == null ? 0 : sendCount + 1)), 24 * 60 * 60);
            } else {
                if (redisUtil.get("sendEmailTime"+email) != null) {
                    throw new CustomException("验证码一分钟只能发送一次");
                }
                throw new CustomException("验证码获取频繁");
            }
        }
        return new String(nonceChars);
    }
}
