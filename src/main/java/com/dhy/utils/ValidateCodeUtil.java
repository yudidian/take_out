package com.dhy.utils;

import com.dhy.common.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Slf4j
public class ValidateCodeUtil {
    private RedisUtil redisUtil;
    //Random 不是密码学安全的，加密相关的推荐使用 SecureRandom
    private static final Random RANDOM = new SecureRandom();

    private static final String randomString = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWSYZ";

    public ValidateCodeUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 生成6位随机数字
     *
     * @return 返回6位数字验证码
     */
    public String getCode(String email) {
        String code = (String) redisUtil.get(email);
        char[] nonceChars = new char[6];
        // 验证码为空说明要么没有获取要么失效
        int sendCount = redisUtil.get("sendCount" + email) == null ? 0 : Integer.parseInt(String.valueOf(redisUtil.get("sendCount"+email)));
        // 每天获取三次验证码
        if (sendCount < 4) {
            if (code == null) {
                for (int index = 0; index < nonceChars.length; ++index) {
                    nonceChars[index] = randomString.charAt(RANDOM.nextInt(randomString.length()));
                }
                redisUtil.set(email, new String(nonceChars), 5*60);
                redisUtil.set("sendEmailTime"+email, String.valueOf(new Date().getTime()), 60);
                redisUtil.set("sendCount"+email, String.valueOf(sendCount + 1), 24 * 60 * 60);
            } else {
                if (redisUtil.get("sendEmailTime"+email) != null) {
                    throw new CustomException("验证码一分钟只能发送一次");
                }
            }
        } else {
            throw new CustomException("验证码获取频繁每天只能获取三次");
        }
        return new String(nonceChars);
    }
}
