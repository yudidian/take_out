package com.dhy.utils;

import com.dhy.common.CustomException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Slf4j
public class ValidateCodeUtil {
  //Random 不是密码学安全的，加密相关的推荐使用 SecureRandom
  private static final Random RANDOM = new SecureRandom();

  private static final String randomString = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWSYZ";
  private final static String code = "code";
  /**
   * 生成6位随机数字
   * @return 返回6位数字验证码
   */
  public String getCode(HttpServletRequest request) {
    char[] nonceChars = new char[6];
    HttpSession session = request.getSession();
    if (session.getAttribute("timeOut") == null){
      for (int index = 0; index < nonceChars.length; ++index) {
        nonceChars[index] = randomString.charAt(RANDOM.nextInt(randomString.length()));
      }
      //移除之前的session中的验证码信息
      session.removeAttribute(code);
      //将验证码放入session
      session.setAttribute(code,new String(nonceChars));//设置token,参数token是要设置的具体值
      session.setAttribute("timeOut",new Date().getTime());
    } else {
      // 验证码有效时间为5分钟
      if (new Date().getTime() - (long) session.getAttribute("timeOut") < 1000 * 60) {
        throw new CustomException("验证码获取频繁");
      }
      for (int index = 0; index < nonceChars.length; ++index) {
        nonceChars[index] = randomString.charAt(RANDOM.nextInt(randomString.length()));
      }
      //移除之前的session中的验证码信息
      session.removeAttribute(code);
      //将验证码放入session
      session.setAttribute(code,new String(nonceChars));//设置token,参数token是要设置的具体值
      session.setAttribute("timeOut",new Date().getTime());
    }
    return new String(nonceChars);
  }
}
