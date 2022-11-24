package com.dhy.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JwtUtils {
  private static final String STRING = "taotao";

  // 生成token
  public static String token(Map<String, String> map) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, 7);
    // 创建jwt
    JWTCreator.Builder builder = JWT.create();
    map.forEach(builder::withClaim);
    return builder.withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(STRING));
  }
  // 验证token 合法性
  public static void verify(String token){
    JWT.require(Algorithm.HMAC256(STRING)).build().verify(token);
  }
  // 获取token 信息
  public static DecodedJWT getTokenInfo(String token){
    return JWT.require(Algorithm.HMAC256(STRING)).build().verify(token);
  }
}
